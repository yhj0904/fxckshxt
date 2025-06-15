package kr.co.nanwe.prog.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgSearchVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgVO;
import kr.co.nanwe.user.web.UserController;

/**
 * @Class Name 		: MyProgController
 * @Description 	: 나의 프로그램 신청내역 Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.18		신한나			최초생성
 */

@RequestMapping(value = "/myprog")
@Program(code="MYPROG", name="나의 프로그램 신청내역")
@Controller
public class MyProgController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(MyProgController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/program/user";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "progService")
	private ProgService progService;
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Constructor */
	public MyProgController() {		
		RequestMapping requestMapping = MyProgController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/** Root Forward */
	@RequestMapping(value="")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/list.do");
	}
	
	/**
	 * 사용자 신청한 프로그램 목록
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="MY_LIST", name="나의 프로그램 목록 화면")
	public String myProgList(Model model, HttpServletRequest request
								, @ModelAttribute ProgSearchVO search){
		
		LoginVO loginVO = SessionUtil.getLoginUser(); 
		
		//로그인 사용자가 아닌경우
		if(loginVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		Map<String, Object> map = progService.selectMyProgList(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH + "/list");
	}
	
	/**
	 * 프로그램 신청취소
	 * @param 
	 * @return
	 * @exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel.do", method = RequestMethod.POST)
	public int saveAppr(Model model, HttpServletRequest request
							, @RequestParam(value = "progReqstCd", required=false) String progReqstCd
							, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		System.out.println("================= checkedSId>>>>>>>>>>>>>>>>> " + checkedSId);
		
		return progService.updateProgCancelUser(checkedSId, progReqstCd);
		
	}
	
}
