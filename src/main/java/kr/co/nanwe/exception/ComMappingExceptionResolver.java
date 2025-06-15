package kr.co.nanwe.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.rest.service.ResponseModelAndView;

/**
 * @Class Name 		: ResponseMessage
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ComMappingExceptionResolver extends org.springframework.web.servlet.handler.SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			String errorCode = "-1";
			Integer statusCode = determineStatusCode(request, viewName);
			if (statusCode != null) {
				applyStatusCodeIfPossible(request, response, statusCode);
			} else {
				statusCode = 400;
			}
			if(RequestUtil.isRestRequest(request)) {
				return getModelAndView(viewName, ex, request, response, errorCode, statusCode);
			} else {
				return super.getModelAndView(viewName, ex);
			}
		} else {
			return null;
		}
	}
	
	protected ModelAndView getModelAndView(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response, String errorCode, Integer statusCode) {
		request.removeAttribute("IS_REST_REQUEST");
		response.setStatus(statusCode);
		return new ResponseModelAndView().returnError(request, statusCode);
	}
}
