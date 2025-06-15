package kr.co.nanwe.surv.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.surv.service.SurvMgtVO;
import kr.co.nanwe.surv.service.SurvService;

/**
 * @Class Name 		: SysSurvController
 * @Description 	: 설문 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/surv")
@Program(code="COM_SURV", name="설문관리")
@Controller
public class SysSurvController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SurvController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/surv";
	
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
	@Resource(name = "survService")
	private SurvService survService;
	
	/** Constructor */
	public SysSurvController() {		
		RequestMapping requestMapping = SysSurvController.class.getAnnotation(RequestMapping.class);
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
	 * 설문 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = survService.selectSurvMgtList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 설문 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SurvMgtVO survMgtVO = survService.selectSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(survMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("survMgtVO", survMgtVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 설문 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 생성
		SurvMgtVO survMgtVO = new SurvMgtVO();
		
		//기본값 세팅
		survMgtVO.setUseYn("Y");
		survMgtVO.setSurvDate1(DateUtil.getDate());
		survMgtVO.setSurvDate2(DateUtil.getDate());
		
		//조회결과 MODEL ADD
		model.addAttribute("survMgtVO", survMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 설문 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,SurvMgtVO survMgtVO ,BindingResult survBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(survMgtVO, survBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (survBindingResult.hasErrors()) {
			model.addAttribute("survMgtVO", survMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(survMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("survMgtVO", survMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = survService.insertSurvMgt(survMgtVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", survMgtVO.getSurvId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 설문 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SurvMgtVO survMgtVO = survService.selectSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(survMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("survMgtVO", survMgtVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 설문 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,SurvMgtVO survMgtVO ,BindingResult survBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(survMgtVO, survBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (survBindingResult.hasErrors()) {
			model.addAttribute("survMgtVO", survMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(survMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("survMgtVO", survMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = survService.updateSurvMgt(survMgtVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", survMgtVO.getSurvId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", survMgtVO.getSurvId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}		
	}
	
	/**
	 * 설문 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 조회
		SurvMgtVO survMgtVO = survService.selectSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(survMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = survService.deleteSurvMgt(survMgtVO.getSurvId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", survMgtVO.getSurvId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 설문상태변경
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyState.do", method = RequestMethod.POST)
	@ProgramInfo(code="STATE", name="문항,항목 수정 처리")
	@ResponseBody
	public boolean modifyState(HttpServletRequest request, @RequestParam(value = "survId", defaultValue="") String survId, @RequestParam(value = "survState", defaultValue="") String survState) {
		return survService.updateSurvState(survId, survState);
	}
	
	
	/**
	 * 설문 문항&항목 관리 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/manage.do")
	@ProgramInfo(code="MANAGE_FORM", name="문항,항목 관리 화면")
	public String manageView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SurvMgtVO survMgtVO = survService.selectSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(survMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//설문 상태가 시작 또는 종료인 경우 문항 항목 변경 불가
		if("S".equals(survMgtVO.getSurvState()) || "E".equals(survMgtVO.getSurvState())) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("survMgtVO", survMgtVO);
		
		return web.returnView(VIEW_PATH, "/manage");
	}
	
	/**
	 * 설문 문항&항목 관리 수정 처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/manageAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MANAGE", name="문항,항목 수정 처리")
	@ResponseBody
	public boolean manageAction(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		return survService.updateSurvQuesAndItem(map);
	}
	
	/**
	 * 설문 결과
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/result.do")
	@ProgramInfo(code="RESULT", name="결과조회")
	public String result(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		Map<String, Object> surveyResult = survService.selectSurveyResult(id);

		//조회결과 MODEL ADD
		model.addAllAttributes(surveyResult);
		
		return web.returnView(VIEW_PATH, "/result");
	}
	
	/**
	 * 설문 결과 기타문항 보기
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/etcView.do", method = RequestMethod.POST)
	@ProgramInfo(code="ETC_VIEW", name="기타 항목 조회")
	@ResponseBody
	public List<String> etcView(HttpServletRequest request
						, @RequestParam(value = "survId", defaultValue="") String survId
						, @RequestParam(value = "quesIdx", defaultValue="0") int quesIdx
						, @RequestParam(value = "itemIdx", defaultValue="0") int itemIdx) {
		return survService.selectSurvAnswEtcList(survId, quesIdx, itemIdx);
	}
	
	/**
	 * 설문 결과
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/resultUser.do")
	@ProgramInfo(code="RESULT_USER", name="사용자결과조회")
	public String resultUser(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String survId
						,@RequestParam(value = "sUser", defaultValue="") String userId){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		Map<String, Object> surveyResult = survService.selectSurveyResultByUser(survId, userId);

		//조회결과 MODEL ADD
		model.addAllAttributes(surveyResult);
		
		return web.returnView(VIEW_PATH, "/result_user");
	}
	
}
