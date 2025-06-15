package kr.co.nanwe.bbs.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.EmpSearchVO;
import kr.co.nanwe.bbs.service.JobApiService;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;

/**
 * @Class Name 		: EmpNoticeController
 * @Description 	: e채용공고  Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.24		신한나			최초생성
 */

@RequestMapping(value = "/emp")
@Program(code="EMP_NOTI", name="채용공고")
@Controller
public class EmpNoticeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmpNoticeController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/emp";
	
	/** Board Code */
	private static final String BBS_CD = "BOARD06";

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
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;

	/** Service */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;
	
	/** Service */
	@Resource(name = "jobApiService")
	private JobApiService jobApiService;
	
	/** Constructor */
	public EmpNoticeController() {		
		RequestMapping requestMapping = EmpNoticeController.class.getAnnotation(RequestMapping.class);
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
		return web.forward(REDIRECT_PATH + "/list.do");
	}
	
	/**
	 * 게시글화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute EmpSearchVO search
						,@RequestParam(value = "sCate", defaultValue="") String category){
		
		//LOGGER.debug(" SEARCH >>>>>>>>>>>>>>> " + search);
		
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "LIST");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//API CODE
		CommCdVO apiList = commCdService.selectCommCd("EXT_API");
		model.addAttribute("apiList", apiList);
		
		//LOGGER.debug("================ category >>>>>>>>>>> " + category);
		//LOGGER.debug("================ apiList >>>>>>>>>>> " + apiList);
		
		//API CODE 조회
		if(StringUtil.isNull(category) && apiList != null) {
			category = apiList.getList().get(0).getCd();
		} 

		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		//model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		String cate = category.toLowerCase();
		switch (cate) {
		case "worknet":
			
			//급여조건
			CommCdVO payCode = commCdService.selectCommCd("SALTP");
			model.addAttribute("payCode", payCode);
			
			////경력선택
			CommCdVO crrCode = commCdService.selectCommCd("CAREER");
			model.addAttribute("crrCode", crrCode);
			
			////학력선택
			CommCdVO eduCode = commCdService.selectCommCd("EDUCATION");
			model.addAttribute("eduCode", eduCode);
			
			////고용조건
			CommCdVO empTpGbCode = commCdService.selectCommCd("EMPTPGB");
			model.addAttribute("empTpGbCode", empTpGbCode);
			
			////검색조건 - 직종코드
			Map<String, Object> ocpCode = commCdService.selectWorkCommCdList("OCCUPATION");
			model.addAllAttributes(ocpCode);
			
			////워크넷 조회
			Map<String, Object> result = jobApiService.worknet(bbsMgtVO, search, code);
			model.addAllAttributes(result);
			break;

		default:

			//급여조건
			CommCdVO payCode2 = commCdService.selectCommCd("SALTP");
			model.addAttribute("payCode", payCode2);
			
			////경력선택
			CommCdVO crrCode2 = commCdService.selectCommCd("CAREER");
			model.addAttribute("crrCode", crrCode2);
			
			////학력선택
			CommCdVO eduCode2 = commCdService.selectCommCd("EDUCATION");
			model.addAttribute("eduCode", eduCode2);
			
			////고용조건
			CommCdVO empTpGbCode2 = commCdService.selectCommCd("EMPTPGB");
			model.addAttribute("empTpGbCode", empTpGbCode2);
			
			////검색조건 - 직종코드
			Map<String, Object> ocpCode2 = commCdService.selectWorkCommCdList("OCCUPATION");
			model.addAllAttributes(ocpCode2);
			
			////워크넷 조회
			Map<String, Object> result2 = jobApiService.worknet(bbsMgtVO, search, code);
			model.addAllAttributes(result2);
			break;
		}
		
		return web.returnView(VIEW_PATH, "/" + category.toLowerCase());
	}
	
}
