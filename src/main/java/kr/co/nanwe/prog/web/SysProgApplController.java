package kr.co.nanwe.prog.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
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
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgSearchVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgVO;

/**
 * @Class Name 		: SysProgApplController
 * @Description 	: 프로그램 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */

@RequestMapping(value = "/sys/prog/apply")
@Program(code="PROG_USER", name="프로그램 수강생 관리")
@Controller
public class SysProgApplController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SysProgApplController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/prog/apply";
	
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
	public SysProgApplController() {		
		RequestMapping requestMapping = SysProgApplController.class.getAnnotation(RequestMapping.class);
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
	 * 프로그램화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);

		//id 없을 때
		if(id == null || "".equals(id)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", "/sys/prog/list.do");
			return web.returnError();
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		
		//DATA SEARCH
		ProgVO progVO = progService.selectProg(Integer.valueOf(id));
		if(progVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", "/sys/prog/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("progVO", progVO);
		
		//목록조회
		Map<String, Object> map = progService.selectProgApplUserList(search, progVO.getProgId());
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		//이수결과 코드
		model.addAttribute("PROG_COMPL", commCdService.selectComAuthList("PROG_COMPL"));
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 프로그램화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
						, @RequestParam(value = "sId", defaultValue="") String id
						, @RequestParam(value = "userId", defaultValue="") String userId){
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//id 없을 때
		if(id == null || "".equals(id) || userId == null || "".equals(userId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//DATA SEARCH
		Map<String, Object> map = progService.selectProgApplUser(Integer.valueOf(id), userId);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
				
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 프로그램화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
								, @RequestParam(value = "sId", defaultValue="") String id
								, @RequestParam(value = "sCode", defaultValue="") String code
								, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		//model.addAttribute("sCode", code);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId) || "".equals(id)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl",  REDIRECT_PATH + "/list.do");		
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//삭제처리
		int result = progService.deleteCheckedProgApplUser(Integer.valueOf(id), checkedSId);
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			
			//결과메시지  (생략가능)
			model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 이수/미이수 처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/checkComplAction.do", method = RequestMethod.POST)
	public String checkComplAction(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
							, @RequestParam(value = "sId", defaultValue="") String id
							, @RequestParam(value = "sCode", defaultValue="") String code
							, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}

		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		//model.addAttribute("sCode", code);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId) || "".equals(id)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl",  REDIRECT_PATH + "/list.do");		
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
				
		int result = progService.updateProgComplUser(Integer.valueOf(id), code, checkedSId);
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			//결과메시지  (생략가능)
			model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkModify", new String[] {Integer.toString(result)}));
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", id);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	@RequestMapping(value = "/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search, int progId) throws IOException {
		
		//엑셀 파일명에 들어갈 프로그램명
		ProgVO progVO = progService.selectProg(progId);
		String progNm = progVO.getProgNm();

		//사용 불가 분자 치환 [ \ / * [ ] : ? ]
		progNm = progNm.replaceAll("\\[","(").replaceAll("]", ")");
		progNm = progNm.replaceAll("[[:]\\\\/?[*]]"," ");
		
		//엑셀 파일명
		String excelName = progNm + " 신청자 목록";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"순번", "프로그램명", "소속학과","이름", "학번", "성별", "교육일자", "교육시간", "회원구분", "교육방법", "핸드폰번호", "이메일주소",   "만족도조사", "교육이수", "신청상태", "신청일"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes =  {"ROWNUM", "PROG_NM", "DEPT_NM", "USER_NM", "STD_NO", "SEX_CD_NM", "PROG_SDT", "PROG_STM", "USER_TYPE_NM", "PROG_MTH_NM", "MBPH_NO", "EMAIL","SURVEY_YN_NM", "COMPL_NM", "PROG_REQST_CD_NM", "INPT_DTTM"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook();
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, false);
		
		//데이터 조회
		progService.selectExcelProgApplUserList(handler, search, progId);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
}
