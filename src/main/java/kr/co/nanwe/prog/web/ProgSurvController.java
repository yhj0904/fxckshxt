package kr.co.nanwe.prog.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.prog.service.ProgSurvMgtVO;
import kr.co.nanwe.prog.service.ProgSurvService;
import kr.co.nanwe.surv.service.SurvMgtVO;
import kr.co.nanwe.surv.service.SurvService;

/**
 * @Class Name 		: BoardController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/prog_surv")
@Program(code="PROG", name="프로그램")
@Controller
public class ProgSurvController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/program/surv";
	
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
	@Resource(name = "progSurvService")
	private ProgSurvService progSurvService;
	
	/** Root Forward */
	@RequestMapping(value = "/{path}.do")
	public String root(@PathVariable("path") String path, HttpServletRequest request) {
		return web.forward("/prog/surv/"+path+"/list.do");
	}
	
	/**
	 * 설문조사 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="등록 화면")
	public String view(Model model, HttpServletRequest request, @RequestParam(value = "progId", defaultValue="") int progId){
		
		//상세조회
		ProgSurvMgtVO progSurvMgtVO = progSurvService.selectSurvey(progId);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(progSurvMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("survMgtVO.error"));
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		
		progSurvMgtVO.setProgId(progId);
		
		//조회결과 MODEL ADD
		model.addAttribute("progSurvMgtVO", progSurvMgtVO);
		
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
		Map<String, Object> returnMap = progSurvService.insertSurveyAnswer(map);
		String errMsg = messageUtil.getMessage((String)returnMap.get("errCd"));
		returnMap.put("errMsg", errMsg);
		return returnMap;
	}
		
}
