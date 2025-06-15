package kr.co.nanwe.cmmn.interceptor;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.menu.service.MenuService;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: CmmnInterceptor
 * @Description 	: CmmnInterceptor
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class CmmnInterceptor extends HandlerInterceptorAdapter {
	
	private static final List<String> PASS_LIST = new ArrayList<String>(Arrays.asList(
			"/login.do"
			, "/loginAction.do"
			, "/"
			, "/index.do"
			, "/find/userId.do"
			, "/find/userIdResult.do"
			, "/find/userPw.do"
			, "/find/modifyUserPw.do"
			, "/find/userPwResult.do"
		));
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** Service */
	@Resource(name = "menuService")
	private MenuService menuService;
	
	/** Service */
	@Resource(name = "loginService")
	private LoginService loginService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		//요청 주소 체크
		String uri = RequestUtil.getURI();
				
		//등록된 사이트인지 체크
		SiteVO siteVO = siteService.selectSiteByDomain(RequestUtil.getDomain());
		if(siteVO == null) {
			response.sendRedirect(request.getContextPath() + "/prepare.do");
			return false;
		}
		
		request.setAttribute("SITE_INFO", siteVO);
		
		//비로그인 가능 인지
		String loginYn = siteVO.getLoginYn();
		
		//로그인 후 이용가능한 사이트이며 비로그인 인경우
		if(StringUtil.isEqual(loginYn, "Y") && SessionUtil.getLoginUser() == null) {
			
			//PASS 가능하면
			if(PASS_LIST.contains(uri)) {
				return true;
			}
			
			//로그인 페이지로 이동
			if (request.getAttribute("returnLogin") != null) {
				response.sendRedirect(request.getContextPath() + "/login.do");
			} else {
				String returnLogin = uri;
				// Action이 아닌경우
				if (returnLogin.contains("Action.do") && request.getMethod().equals("POST")) {
					response.sendRedirect(request.getContextPath() + "/login.do?returnLogin=" + returnLogin);
				} else {
					Enumeration<String> params = request.getParameterNames();
					if (params != null) {
						int paramCnt = 0;
						while (params.hasMoreElements()) {

							String name = (String) params.nextElement();

							if (RequestUtil.DENIED_PARAM_LIST.contains(name)) {
								continue;
							}

							// 값이 없는 경우 continue
							if (StringUtil.isNull(request.getParameter(name))) {
								continue;
							}
							if (paramCnt == 0) {
								returnLogin += "?";
							} else {
								returnLogin += "&";
							}
							returnLogin += name + "=" + request.getParameter(name);
							paramCnt++;
						}
					}
					returnLogin = URLEncoder.encode(returnLogin, "UTF-8");
					response.sendRedirect(request.getContextPath() + "/login.do?returnLogin=" + returnLogin);
				}					
			}
			
			return false;
		}
		
		//로그인중인경우
		if(SessionUtil.getLoginUser() != null) {			
			//요청주소가 login관련이면 메인으로
			if (uri.equals("/login.do") || uri.equals("/loginAction.do")) {
				response.sendRedirect(request.getContextPath() + "/");
				return false;
			}			
		}
		
		//현재 메뉴 정보 (권한체크)
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			if (method.getDeclaringClass().isAnnotationPresent(Controller.class) && method.getDeclaringClass().isAnnotationPresent(Program.class)) {
				String progCd = "";
				String actionCd = "";
				if(method.isAnnotationPresent(ProgramInfo.class)) {
					ProgramInfo programInfo = method.getAnnotation(ProgramInfo.class);
					if(programInfo != null) {
						actionCd = programInfo.code();
					}
				}
				Program program = method.getDeclaringClass().getAnnotation(Program.class);
				if(program != null) {
					progCd = program.code();
					boolean checkProgramAuth = menuService.selectCheckProgramAuth(siteVO, progCd, actionCd, uri);
					if(!checkProgramAuth) {
						RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorProgramAuth.do");
						dispatcher.forward(request, response);
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
		if(modelAndView != null && handler instanceof HandlerMethod) {	
			
			String uri = RequestUtil.getURI();
			String viewName = modelAndView.getViewName();
			
			if (!(viewName.indexOf("redirect:") > -1) && !(viewName.indexOf("forward:") > -1) && (uri.indexOf(".do") > -1 || uri.equals("/"))) {
				
				//언어 체크
				String language = web.getLanguage();
				modelAndView.addObject("GV_LANGUAGE", language);
				
				//프로그램 주소
				String presentPath = "";
				String progCd = "";
				
				//컨트롤러의 RequestMapping을 확인 후 해당 RequestMapping을 모델에 담는다.
				Method method = ((HandlerMethod) handler).getMethod();
				if (method.getDeclaringClass().isAnnotationPresent(Controller.class) && method.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
					if(requestMapping != null) {
						presentPath = requestMapping.value()[0];
			        }					
					Program program = method.getDeclaringClass().getAnnotation(Program.class);
					if(program != null) {
						progCd = program.code();						
					}
				}
				modelAndView.addObject("GV_PRESENT_PATH", presentPath);
				
				//사이트 정보				
				if(request.getAttribute("SITE_INFO") != null) {
					
					SiteVO siteVO = (SiteVO) request.getAttribute("SITE_INFO");
					
					String siteCd = siteVO.getSiteCd();
					if(!StringUtil.isNull(siteCd)) {
						siteVO.setViewCd(siteCd.toLowerCase());
					}
					modelAndView.addObject("GV_SITE_INFO", siteVO);
					
					//메뉴정보 조회
					//현재 메뉴정보
					Map<String, Object> presentMenuInfo = menuService.selectPresentMenuInfo(siteVO, presentPath, progCd, uri, language);
					modelAndView.addObject("GV_MENU_INFO", presentMenuInfo);
					
					//확장자가 팝업인 경우 메뉴정보를 조회하지 않는다.
					if(!"pop".equals(StringUtil.getExtension(viewName))) {
						Map<String, Object> menuMap = menuService.selectMenuListByUser(siteVO, presentPath, progCd, language);
						modelAndView.addAllObjects(menuMap);
					}
					
					//템플릿 설정
					if(!viewName.startsWith("cmmn/") && !viewName.startsWith("error/")) {
						String templateCd = "basic";
						if(!StringUtil.isNull(siteVO.getTemplateCd())) {
							templateCd = siteVO.getTemplateCd().toLowerCase();
						}
						modelAndView.addObject("GV_TEMPLATE_CODE", templateCd);
						
						if(!viewName.startsWith("/")) {
							viewName = "/" + viewName;
						}
						modelAndView.setViewName(CodeConfig.MAIN_TILES_CD + viewName);
					}
				}
			}
			
		}
		
		super.postHandle(request, response, handler, modelAndView);
	}
}
