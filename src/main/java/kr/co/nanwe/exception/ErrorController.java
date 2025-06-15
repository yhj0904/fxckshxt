package kr.co.nanwe.exception;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.rest.service.ResponseModelAndView;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: ErrorController
 * @Description 	: 에러 컨트롤러
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class ErrorController {
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/**
	 * 준비중페이지
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/prepare.do")
	public String prepare(Model model,HttpServletRequest request, HttpServletResponse response){
		SiteVO siteVO = siteService.selectSiteByDomain(RequestUtil.getDomain());
		if(siteVO != null) {
			return web.redirect(model, "/");
		}
		return web.returnJsp("cmmn/prepare");
	}
	
	/**
	 * error
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/error.do")
	public ModelAndView error(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 400;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/error");
		}
	}
		
	/**
	 * auth
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/errorAuth.do")
	public ModelAndView errorAuth(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 401;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/errorAuth");
		}
	}
	
	/**
	 * auth
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/errorProgramAuth.do")
	public ModelAndView errorProgramAuth(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mv = new ModelAndView("cmmn/result_error");
		mv.addObject("resultMsg", messageUtil.getMessage("message.error.auth"));
		mv.addObject("redirectUrl", "/");
		return mv;
	}
	
	/**
	 * REST 키 에러
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/errorKey.do")
	public ModelAndView errorKey(HttpServletRequest request, HttpServletResponse response){		
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			String errorCode = "-98";
			if(request.getAttribute("errorCode") != null) {
				errorCode = (String) request.getAttribute("errorCode");
			}
			response.setStatus(400);
			return new ResponseModelAndView().returnError(request, errorCode);
		} else {
			return new ModelAndView("error/error400");
		}
	}

	/**
	 * 400
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/error400.do")
	public ModelAndView error400(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 400;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/error400");
		}
	}
	
	/**
	 * 401
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/error401.do")
	public ModelAndView error401(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 401;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/error401");
		}
	}
	
	/**
	 * 403
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/error403.do")
	public ModelAndView error403(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 403;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/error403");
		}
	}
	
	/**
	 * 404
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/error404.do")
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 404;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/error404");
		}
	}
	
	/**
	 * 500
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/errors/error500.do")
	public ModelAndView error500(HttpServletRequest request, HttpServletResponse response){
		if(RequestUtil.isRestRequest(request)) {
			request.removeAttribute("IS_REST_REQUEST");
			int statusCode = 500;
			response.setStatus(statusCode);
			return new ResponseModelAndView().returnError(request, statusCode);
		} else {
			return new ModelAndView("error/error500");
		}
	}
}