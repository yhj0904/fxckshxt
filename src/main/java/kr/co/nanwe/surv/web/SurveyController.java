package kr.co.nanwe.surv.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.surv.service.SurvMgtVO;
import kr.co.nanwe.surv.service.SurvService;

/**
 * @Class Name 		: SurveyController
 * @Description 	: 설문 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/survey")
@Program(code="SURVEY", name="설문")
@Controller
public class SurveyController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SurveyController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/survey";
	
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
	@Resource(name = "survService")
	private SurvService survService;
	
	/** Constructor */
	public SurveyController() {		
		RequestMapping requestMapping = SurveyController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/** Root Forward */
	@RequestMapping(value = "")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/view.do");
	}
	
	/**
	 * 설문조사 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="등록 화면")
	public String view(Model model, HttpServletRequest request, @RequestParam(value = "survId", defaultValue="") String survId){
		
		//상세조회
		SurvMgtVO survMgtVO = survService.selectSurvey(survId);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(survMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("survMgtVO.error"));
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("survMgtVO", survMgtVO);
		
		return web.returnView(VIEW_PATH, "/view", "POP");
	}
	
	/**
	 * 응답결과 등록
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록")
	@ResponseBody
	public Map<String, Object> manageAction(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		Map<String, Object> returnMap = survService.insertSurveyAnswer(map);
		String errMsg = messageUtil.getMessage((String)returnMap.get("errCd"));
		returnMap.put("errMsg", errMsg);
		return returnMap;
	}
}
