package kr.co.nanwe.rest.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import kr.co.nanwe.cmmn.util.XmlUtil;

public class ResponseModelAndView {
	
	private ModelAndView modelAndView;
	private ResponseMessage responseMessage;
	private Map<String, Object> result;
	
	public ResponseModelAndView() {
		this.modelAndView = new ModelAndView();	
		this.responseMessage = new ResponseMessage();
		this.result = new HashMap<String, Object>();
	}
	
	public ModelAndView returnSuccess(HttpServletRequest request, Object object) {
		this.responseMessage.setSuccess(object);
		this.result.put("code", responseMessage.getCode());
		this.result.put("data", responseMessage.getData());		
		setModelAndView(request);
		return this.modelAndView;
	}

	public ModelAndView returnError(HttpServletRequest request, String errorCode) {		
		this.responseMessage.setError(errorCode);
		this.result.put("code", responseMessage.getCode());
		this.result.put("msg", responseMessage.getMsg());		
		setModelAndView(request);
		return this.modelAndView;
	}

	public ModelAndView returnError(HttpServletRequest request, int statusCode) {		
		this.responseMessage.setError(statusCode);
		this.result.put("code", responseMessage.getCode());
		this.result.put("msg", responseMessage.getMsg());		
		setModelAndView(request);
		return this.modelAndView;
	}
	
	private void setModelAndView(HttpServletRequest request) {
		String viewName = "jsonView";
		String contentType = request.getHeader("content-type");
		if(contentType != null && "application/xml".equals(contentType)) {
			viewName = "xmlView";
			this.modelAndView.addObject("xmlResult", XmlUtil.objectToXml("result", result));
		} else {			
			this.modelAndView.addObject("result", result);
		}
		this.modelAndView.setViewName(viewName);
	}
}
