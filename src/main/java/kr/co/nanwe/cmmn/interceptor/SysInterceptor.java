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
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.menu.service.MenuService;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: SysInterceptor
 * @Description 	: 관리자 인터셉터
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class SysInterceptor extends HandlerInterceptorAdapter {
	
	private static final List<String> PASS_LIST = new ArrayList<String>(Arrays.asList(
		"/sys/login.do"
		, "/sys/loginAction.do"
		, "/sys.do"
		, "/sys/index.do"
	));
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Menu Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** Menu Service */
	@Resource(name = "menuService")
	private MenuService menuService;
	
	/** Service */
	@Resource(name = "loginService")
	private LoginService loginService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		//요청 주소 체크
		String uri = RequestUtil.getURI();
		
		//관리자가 아니면 false
		if(SessionUtil.getLoginUser() != null && !SessionUtil.isAdmin()) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorAuth.do");
			dispatcher.forward(request, response);
			return false;
		}
		
		//관리자 접속 가능한 도메인인지 체크
		SiteVO siteVO = siteService.selectSysSiteByDomain(RequestUtil.getDomain());
		if(siteVO == null) {
			
			//관리자 계정이 등록되어있는지 확인
			boolean admCheck = loginService.checkSysUserExist();		
			if(!admCheck) {
				//서버 아이피와 유저의 아이피가 동일한 경우 관리자 등록 화면으로 이동 
				String userIp = ClientUtil.getUserIp();
				if("127.0.0.1".equals(userIp) || "localhost".equals(userIp)) {
					if("/sys/start/step1.do".equals(uri) || "/sys/start/step1Action.do".equals(uri)) {
						return true;
					}				
					response.sendRedirect(request.getContextPath() + "/sys/start/step1.do");
					return false;
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorAuth.do");
					dispatcher.forward(request, response);
					return false;
				}
			}
			
			//사이트가 등록되어있는지 확인
			int siteCount = siteService.selectSiteCount();
			if(siteCount == 0) {
				//서버 아이피와 유저의 아이피가 동일한 경우 사이트 등록 화면으로 이동 
				String userIp = ClientUtil.getUserIp();
				if("127.0.0.1".equals(userIp) || "localhost".equals(userIp)) {				
					if("/sys/start/step2.do".equals(uri) || "/sys/start/step2Action.do".equals(uri)) {
						return true;
					}
					response.sendRedirect(request.getContextPath() + "/sys/start/step2.do");
					return false;
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorAuth.do");
					dispatcher.forward(request, response);
					return false;
				}
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorAuth.do");
				dispatcher.forward(request, response);
				return false;
			}
		} else {
			request.setAttribute("SITE_INFO", siteVO);
		}
		
		//로그인중인경우
		if(SessionUtil.getLoginUser() != null) {
			
			//관리자 권한이 아니면 메인 REDIRECT
			if(!SessionUtil.isAdmin()) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorAuth.do");
				dispatcher.forward(request, response);
				return false;
			}
			
			//요청주소가 login관련이면 메인으로
			if (uri.equals("/sys/login.do") || uri.equals("/sys/loginAction.do")) {
				response.sendRedirect(request.getContextPath() + "/sys.do");
				return false;
			}
			
			//현재 메뉴 정보
			if (handler instanceof HandlerMethod) {
				Method method = ((HandlerMethod) handler).getMethod();
				if (method.getDeclaringClass().isAnnotationPresent(Controller.class) && method.getDeclaringClass().isAnnotationPresent(Program.class)) {
					Program program = method.getDeclaringClass().getAnnotation(Program.class);
					if(program != null) {
						String progNm = program.name();
						if(!StringUtil.isNull(progNm)) {
							request.setAttribute("GV_PROG_NM", progNm);
						}
					}
				}				
			}
			
			return true;
			
		} else {
			
			if(PASS_LIST.contains(uri)) {
				return true;
			}
			
			if(request.getAttribute("returnLogin") != null) {
				response.sendRedirect(request.getContextPath() + "/sys/login.do");
			} else {				
				String returnLogin = uri;
				//Action이 아닌경우				
				if(returnLogin.contains("Action.do") && request.getMethod().equals("POST")) {
					response.sendRedirect(request.getContextPath() + "/sys/login.do?returnLogin=" + returnLogin);
				} else {
					Enumeration<String> params = request.getParameterNames();
					if(params != null) {
						int paramCnt = 0;
						while (params.hasMoreElements()){						
							
							String name = (String)params.nextElement();						
							
							if(RequestUtil.DENIED_PARAM_LIST.contains(name)) {
								continue;
							}
							
							//전자정부 중복방지 파라미터는 continue;
							if("egovframework.double.submit.preventer.parameter.name".equals(name)) {
								continue;
							}
							
							//값이 없는 경우 continue
							if(StringUtil.isNull(request.getParameter(name))) {
								continue;
							}
							
							if(paramCnt == 0) {
								returnLogin += "?";
							} else {
								returnLogin += "&";
							}
							returnLogin += name + "=" + request.getParameter(name);
							paramCnt++;
						}
					}
					returnLogin = URLEncoder.encode(returnLogin, "UTF-8");
					response.sendRedirect(request.getContextPath() + "/sys/login.do?returnLogin=" + returnLogin);
				}
			}
			
			return false;
		}
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
		if(modelAndView != null && handler instanceof HandlerMethod) {	
			
			String uri = RequestUtil.getURI();
			String viewName = modelAndView.getViewName();
			
			if (!(viewName.indexOf("redirect:") > -1) && !(viewName.indexOf("forward:") > -1) && uri.indexOf(".do") > -1 ) {
				
				//언어 체크
				String language = web.getLanguage();
				modelAndView.addObject("GV_LANGUAGE", language);
				
				//프로그램 주소
				String presentPath = "";
				
				//컨트롤러의 RequestMapping을 확인 후 해당 RequestMapping을 모델에 담는다.
				Method method = ((HandlerMethod) handler).getMethod();
				if (method.getDeclaringClass().isAnnotationPresent(Controller.class) && method.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
					if(requestMapping != null) {
						presentPath = requestMapping.value()[0];
			        }
				}
				modelAndView.addObject("GV_PRESENT_PATH", presentPath);
				
				//사이트 정보
				if(request.getAttribute("SITE_INFO") != null) {
					modelAndView.addObject("GV_SITE_INFO", request.getAttribute("SITE_INFO"));
				}
				
				//메뉴정보
				//확장자가 팝업인 경우 메뉴정보를 조회하지 않는다.
				if(!"pop".equals(StringUtil.getExtension(viewName))) {
					Map<String, Object> menuMap = menuService.getSysMenuList(language);
					modelAndView.addAllObjects(menuMap);
				}
				
				//템플릿 설정
				if(!viewName.startsWith("cmmn/") && !viewName.startsWith("error/") && !"sys/template/preview/preview_frame".equals(viewName)) {
					if(!viewName.startsWith("/")) {
						viewName = "/" + viewName;
					}
					modelAndView.setViewName(CodeConfig.SYS_TILES_CD + viewName);
				}
			}
			
		}
		
		super.postHandle(request, response, handler, modelAndView);
	}
}
