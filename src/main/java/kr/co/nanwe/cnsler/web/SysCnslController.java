package kr.co.nanwe.cnsler.web;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.cnsler.service.CnslerVO;
import kr.co.nanwe.cnsler.service.impl.CnslerMapper;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: SysBbsController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="CNSL_MST", name="상담관리")
@Controller
public class SysCnslController {
	
	/** View Path */
	private static final String VIEW_PATH = "sys/cnsl";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/cnsl";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** 부서코드 Service */
	@Resource(name = "deptService")
	private DeptService deptService;
	
	/** 상담원 Service */
	@Resource(name = "cnslerMapper")
	private CnslerMapper cnslerMapper;
	
	/** 상담원 Service */
	@Resource(name = "cnslerService")
	private CnslerService cnslerService;
	
	/** 연락처 Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Constructor */
	public SysCnslController() {		
		RequestMapping requestMapping = SysCnslController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 게시글화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/cnsl.do", "/sys/cnsl/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = cnslerService.selectCnslList(search);
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 게시글화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,UserVO userVO
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "userId", defaultValue="") String userId){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		CnslerVO cnslerVO = cnslerService.selectCnsl(id);

		if(cnslerVO.getMbphNo() != null && !cnslerVO.getMbphNo().equals("")) {
			userVO.setMbphNo(cnslerVO.getMbphNo());
		}

		//조회결과 MODEL ADD
		model.addAttribute("cnslerVO", cnslerVO);
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 게시글화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		CnslerVO cnslerVO = cnslerService.selectCnsl(id);
		model.addAttribute("cnslerVO", cnslerVO);
		
		//학과코드
		List<DeptVO> deptList = deptService.selectOnlyColgList();
		model.addAttribute("deptList", deptList);
		
		//CODE SET
		Map<String, Object> map = new HashMap<String, Object>();
		
		//상담분야(CNSL_TYPE)
		map.put("cnslType", commCdService.selectComAuthList("CNSL_TYPE"));
		
		//교육방법(PROG_MTH)
		map.put("progMth", commCdService.selectComAuthList("PROG_MTH"));
		
		//맵 결과
		model.addAllAttributes(map);
		
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 게시글화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,CnslerVO cnslerVO ,BindingResult bbsBindingResult
									,@RequestParam(value = "sCode", defaultValue="") String code
									,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.updateCnsl(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}		
	}
	
	/**
	 * 게시글화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//삭제 전 조회
		CnslerVO cnslerVO = cnslerService.selectCnsler(id); 
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.deleteCnsler(id);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			//resultParam.put("sCode", bbsVO.getBbsCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			//resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 게시글화면 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);				
			return web.returnError();
		}
		
		//삭제처리
		int result = cnslerService.deleteCheckCnsl(checkedSId);
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
		
	}
	
	/**
	 * 상담 상태 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/updateCnslStat.do", method = RequestMethod.POST)
	@ProgramInfo(code="UPDATE", name="수정처리")
	public String updateCnslStat(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "checkedSId", required=false) String checkedSId
									, CnslerVO cnslerVO) {
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);				
			return web.returnError();
		}
		
		//삭제처리
		int result = cnslerService.updateCnslStat(cnslerVO, checkedSId);
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 엑셀 다운로드
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsl/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search) throws IOException {
		
		//엑셀 파일명
		String excelName = "상담 신청자 목록";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"순번", "신청자명", "신분구분", "단과대학", "학과", "학년", "성별", "SNS아이디", "상담원", "진로", "취업", "생활", "입사지원서", "모의면접", "상담방법", "요청사항", "신청일자", "예약일자", "에약시간", "상담장소", "진행상태"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes =  {"ROWNUM", "USER_NM", "USER_TYPE_NM", "COLG_NM", "DEPT_NM", "GRADE", "SEX_CD", "CHAT_ID", "CNSLER_NM", "CNSL_TYPE_TRACK", "CNSL_TYPE_JOB", "CNSL_TYPE_LIFE", "CNSL_TYPE_RESUME", "CNSL_TYPE_INTV", "CNSL_MTH_NM", "REQST_TEXT", "REQST_DT", "HOPE_DT", "HOPE_TM", "CNSL_PLACE", "CNSL_STATUS_NM"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook();
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, false);
		
		//데이터 조회
		cnslerService.selectCnslListForExcel(handler);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
		
}
