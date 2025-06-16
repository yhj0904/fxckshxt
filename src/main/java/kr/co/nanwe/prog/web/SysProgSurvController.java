package kr.co.nanwe.prog.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.prog.service.ProgSurvMgtVO;
import kr.co.nanwe.prog.service.ProgSurvService;
import kr.co.nanwe.surv.service.SurvMgtVO;

/**
 * @Class Name 		: SysBbsController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="PROG_SURV_MST", name="프로그램 만족도 관리")
@Controller
public class SysProgSurvController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysBbsController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/prog/surv";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/prog_surv";
	
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
	
	/** 프로그램 설문 Service */
	@Resource(name = "progSurvService")
	private ProgSurvService progSurvService;
	
	/** Constructor */
	public SysProgSurvController() {		
		RequestMapping requestMapping = SysProgSurvController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 설문 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/prog_surv.do", "/sys/prog_surv/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = progSurvService.selectSurvMgtList(search);	
		
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
	@RequestMapping(value = "/sys/prog_surv/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		ProgSurvMgtVO progSurvMgtVO = progSurvService.selectSysSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(progSurvMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("progSurvMgtVO", progSurvMgtVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 설문 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/register.do")
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
	@RequestMapping(value = "/sys/prog_surv/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,ProgSurvMgtVO progSurvMgtVO ,BindingResult survBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(progSurvMgtVO, survBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (survBindingResult.hasErrors()) {
			model.addAttribute("survMgtVO", progSurvMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(progSurvMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("survMgtVO", progSurvMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progSurvService.insertSurvMgt(progSurvMgtVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progSurvMgtVO.getSurvId());
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
	@RequestMapping(value = "/sys/prog_surv/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		ProgSurvMgtVO progSurvMgtVO = progSurvService.selectSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(progSurvMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("progSurvMgtVO", progSurvMgtVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 설문 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,ProgSurvMgtVO progSurvMgtVO ,BindingResult survBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(progSurvMgtVO, survBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (survBindingResult.hasErrors()) {
			model.addAttribute("survMgtVO", progSurvMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(progSurvMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("progSurvMgtVO", progSurvMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progSurvService.updateSurvMgt(progSurvMgtVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progSurvMgtVO.getSurvId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progSurvMgtVO.getSurvId());
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
	@RequestMapping(value = "/sys/prog_surv/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id) {
		
		System.out.println(">>>>>>>>>>>>> 컨트롤러단 param : " + id);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 조회
		ProgSurvMgtVO progSurvMgtVO = progSurvService.selectSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(progSurvMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progSurvService.deleteSurvMgt(progSurvMgtVO.getSurvId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progSurvMgtVO.getSurvId());
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
	@RequestMapping(value = "/sys/prog_surv/modifyState.do", method = RequestMethod.POST)
	@ProgramInfo(code="STATE", name="문항,항목 수정 처리")
	@ResponseBody
	public boolean modifyState(HttpServletRequest request, @RequestParam(value = "progId", defaultValue="") int progId, @RequestParam(value = "survState", defaultValue="") String survState) {
		return progSurvService.updateSurvState(progId, survState);
	}
	
	
	/**
	 * 설문 문항&항목 관리 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/manage.do")
	@ProgramInfo(code="MANAGE_FORM", name="문항,항목 관리 화면")
	public String manageView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		ProgSurvMgtVO progSurvMgtVO = progSurvService.selectSysSurvMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(progSurvMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//설문 상태가 시작 또는 종료인 경우 문항 항목 변경 불가
		if("S".equals(progSurvMgtVO.getSurvState()) || "E".equals(progSurvMgtVO.getSurvState())) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("survMgtVO", progSurvMgtVO);
		
		return web.returnView(VIEW_PATH, "/manage");
	}
	
	/**
	 * 설문 문항&항목 관리 수정 처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/manageAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MANAGE", name="문항,항목 수정 처리")
	@ResponseBody
	public boolean manageAction(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		return progSurvService.updateSurvQuesAndItem(map);
	}
	
	/**
	 * 설문 결과
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/result.do")
	@ProgramInfo(code="RESULT", name="결과조회")
	public String result(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "progId", defaultValue="") int progId){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		Map<String, Object> surveyResult = progSurvService.selectSurveyResult(progId);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>> controller surveyResult : " + surveyResult);

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
	@RequestMapping(value = "/sys/prog_surv/etcView.do", method = RequestMethod.POST)
	@ProgramInfo(code="ETC_VIEW", name="기타 항목 조회")
	@ResponseBody
	public List<String> etcView(HttpServletRequest request
						, @RequestParam(value = "progId", defaultValue="") int progId
						, @RequestParam(value = "quesIdx", defaultValue="0") int quesIdx
						, @RequestParam(value = "itemIdx", defaultValue="0") int itemIdx) {
		return progSurvService.selectSurvAnswEtcList(progId, quesIdx, itemIdx);
	}
	
	/**
	 * 설문 결과
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/resultUser.do")
	@ProgramInfo(code="RESULT_USER", name="사용자결과조회")
	public String resultUser(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") int progId
						,@RequestParam(value = "sUser", defaultValue="") String userId){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		Map<String, Object> surveyResult = progSurvService.selectSurveyResultByUser(progId, userId);

		//조회결과 MODEL ADD
		model.addAllAttributes(surveyResult);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> surveyResult :" + surveyResult);
		
		return web.returnView(VIEW_PATH, "/result_user");
	}
	
	/**
	 * 설문조사 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/prev_view.do")
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
		
		//조회결과 MODEL ADD
		model.addAttribute("progSurvMgtVO", progSurvMgtVO);
		
		return web.returnView(VIEW_PATH, "/prev_surv", "POP");
	}
	
	/**
	 * 엑셀다운
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog_surv/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search, int progId) throws IOException {
		
		//엑셀 파일명
		String excelName = "프로그램 결과";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"프로그램명", "문항", "문항내용", "문항타입", "순서", "선택문항 및 답변", "답변률"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes =  {"PROG_NM", "QUES_IDX", "QUES_TITLE", "QUES_TYPE", "QUES_SORT", "ITEM_TITLE", "ANSW_PERCENT"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook();
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, false);
		
		//데이터 조회
		progSurvService.selectSurveyResultForExcel(handler, search, progId);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
		
}
