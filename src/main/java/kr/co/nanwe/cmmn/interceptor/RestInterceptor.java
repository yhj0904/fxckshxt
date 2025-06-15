package kr.co.nanwe.cmmn.interceptor;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.rest.service.RestService;

/**
 * @Class Name 		: RestInterceptor
 * @Description 	: REST 인터셉터
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class RestInterceptor extends HandlerInterceptorAdapter {
	
	/** Service */
	@Resource(name = "restService")
	private RestService restService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		request.setAttribute("IS_REST_REQUEST", "true");
		
		//요청 주소 체크
		String uri = RequestUtil.getRequestURI();
		
		//요청 도메인
		String domain = "";
		String origin = request.getHeader("origin");
		if(origin != null) {
			domain = RequestUtil.getDomainByUrl(origin);
		}
		
		//API인 경우 
		if(uri != null && uri.startsWith("/api/")) {
			
			String code = restService.checkApiKey(request);
			if(!"0".equals(code)) {
				request.setAttribute("errorCode", code);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/errors/errorKey.do");
				dispatcher.forward(request, response);
				return false;
			}
			return true;	
			
		} else {
			
			String requestDomain = RequestUtil.getDomain();
			
			//요청주소와 도메인이 다른경우 false
			if(!StringUtil.isNull(domain) && !domain.contains(requestDomain)) {
				return false;
			}
			if(SessionUtil.getLoginUser() != null) {			
				if(uri.startsWith("/sys/") && !SessionUtil.isAdmin()) { //시스템인경우 false
					return false;
				}
				return true;
			}
		}		
		return false;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
