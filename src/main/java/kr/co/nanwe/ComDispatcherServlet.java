package kr.co.nanwe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

import kr.co.nanwe.cmmn.util.RequestUtil;

public class ComDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.removeAttribute("IS_REST_REQUEST");
		if(RequestUtil.isRestRequest(request)) {
			request.setAttribute("IS_REST_REQUEST", "true");
		}
		super.noHandlerFound(request, response);
	}
}
