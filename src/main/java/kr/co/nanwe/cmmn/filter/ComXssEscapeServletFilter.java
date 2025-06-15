package kr.co.nanwe.cmmn.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: ComXssEscapeServletFilter
 * @Description 	: Xss 공격 방지 필터
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ComXssEscapeServletFilter implements Filter {

	private ComXssEscapeFilter xssEscapeFilter = ComXssEscapeFilter.getInstance();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request ;
		if (excludeUrl(req)) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(new ComXssEscapeServletFilterWrapper(request, xssEscapeFilter), response);
		}
	}

	@Override
	public void destroy() {
		
	}
	
	private boolean excludeUrl(HttpServletRequest request) {
		String uri = request.getRequestURI().toString().trim();
		if (uri.startsWith("/sys/") && request.getSession().getAttribute(SessionUtil.LOGIN_SESSION_KEY) != null) {
			LoginVO loginVO = (LoginVO) request.getSession().getAttribute(SessionUtil.LOGIN_SESSION_KEY);
			if(SessionUtil.isAdmin(loginVO)) {
				return true;
			}
		} 
		return false;
	}
}
