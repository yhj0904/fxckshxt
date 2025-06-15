package kr.co.nanwe.push.web;

import java.io.IOException;
import java.util.HashMap;
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
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.push.service.PushNoticeVO;
import kr.co.nanwe.push.service.PushNoticeService;

/**
 * @Class Name 		: SysPushNoticeController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RequestMapping(value = "/sys/push/notice")
@Program(code="PUSH_NOTICE", name="알림발송관리")
@Controller
public class SysPushNoticeController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysPushNoticeController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/push/notice";
	
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
	@Resource(name = "pushNoticeService")
	private PushNoticeService pushNoticeService;
	
	/** Constructor */
	public SysPushNoticeController() {		
		RequestMapping requestMapping = SysPushNoticeController.class.getAnnotation(RequestMapping.class);
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
	 * 알림발송 목록조회
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
		Map<String, Object> map = pushNoticeService.selectPushNoticeList(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 알림발송 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="0") int noticeNo){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushNoticeVO pushNoticeVO = pushNoticeService.selectPushNotice(noticeNo);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushNoticeVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("pushNoticeVO", pushNoticeVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 알림발송관리 등록폼 화면
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
		PushNoticeVO pushNoticeVO = new PushNoticeVO();
		
		//초기값 설정
		pushNoticeVO.setReservationYn("N");
		pushNoticeVO.setReservationYYYYMMDD(DateUtil.getDate("yyyy-MM-dd"));
		pushNoticeVO.setReservationHour(Integer.parseInt(DateUtil.getDate("H")));
		pushNoticeVO.setReservationMinute(Integer.parseInt(DateUtil.getDate("m")));
		
		//조회결과 MODEL ADD
		model.addAttribute("pushNoticeVO", pushNoticeVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 알림발송관리 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,PushNoticeVO pushNoticeVO , BindingResult pushNoticeBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(pushNoticeVO, pushNoticeBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (pushNoticeBindingResult.hasErrors()) {
			model.addAttribute("pushNoticeVO", pushNoticeVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//유효성 검사 로직 작성
		if(pushNoticeVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(pushNoticeVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(pushNoticeVO.getUploadFile()))) {
			addtionalValid = false;
			pushNoticeBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("pushNoticeVO", pushNoticeVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushNoticeService.insertPushNotice(pushNoticeVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushNoticeVO.getNoticeNo());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 알림발송관리 삭제 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do")
	@ProgramInfo(code="REMOVE", name="삭제폼 화면")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="0") int noticeNo){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushNoticeVO pushNoticeVO = pushNoticeService.selectPushNotice(noticeNo);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushNoticeVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushNoticeService.deletePushNotice(noticeNo);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시	
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			return web.returnSuccess();
			
		} else { //실패시	
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushNoticeVO.getNoticeNo());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 알림발송관리 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			
			//결과메시지  (생략가능)
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = pushNoticeService.deleteCheckedPushNotice(checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		
		return web.returnSuccess();
		
	}
	
	/**
	 * 앱사용자관리 엑셀목록
	 * @param 
	 * @return
	 * @exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search) throws IOException {
		
		//엑셀 파일명
		String excelName = "push_app_user";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		//String[] columnTitles =  {"USER_ID","USER_NM","USER_DV","DEVICE_DV","DEVICE_ID","USER_MOBILE","NOTE","INPT_ID","INPT_IP","INPT_DTTM","INPT_PROG","MODI_ID","MODI_IP","MODI_DTTM","MODI_PROG"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        //String[] columnCodes =  {"USER_ID","USER_NM","USER_DV","DEVICE_DV","DEVICE_ID","USER_MOBILE","NOTE","INPT_ID","INPT_IP","INPT_DTTM","INPT_PROG","MODI_ID","MODI_IP","MODI_DTTM","MODI_PROG"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, null, null, true);
		
		//데이터 조회
		pushNoticeService.selectPushNoticeExcelList(handler, search);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
	
}
