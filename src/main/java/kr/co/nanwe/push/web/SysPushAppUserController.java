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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.external.service.ExternalService;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.push.service.PushAppUserService;
import kr.co.nanwe.push.service.PushAppUserVO;

/**
 * @Class Name 		: SysPushAppUserController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RequestMapping(value = "/sys/push/user")
@Program(code="PUSH_APP_USER", name="앱사용자관리")
@Controller
public class SysPushAppUserController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysPushAppUserController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/push/user";
	
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
	@Resource(name = "pushAppUserService")
	private PushAppUserService pushAppUserService;
	
	/** Service */
	@Resource(name = "externalService")
	private ExternalService externalService;
	
	/** Constructor */
	public SysPushAppUserController() {		
		RequestMapping requestMapping = SysPushAppUserController.class.getAnnotation(RequestMapping.class);
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
	 * 앱사용자 목록조회
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
		Map<String, Object> map = pushAppUserService.selectPushAppUserList(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 앱사용자 상세조회
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
		PushAppUserVO pushAppUserVO = pushAppUserService.selectPushAppUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushAppUserVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("pushAppUserVO", pushAppUserVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 앱사용자관리 등록폼 화면
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
		PushAppUserVO pushAppUserVO = new PushAppUserVO();
		
		//조회결과 MODEL ADD
		model.addAttribute("pushAppUserVO", pushAppUserVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 앱사용자관리 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,PushAppUserVO pushAppUserVO ,BindingResult pushAppUserBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(pushAppUserVO, pushAppUserBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (pushAppUserBindingResult.hasErrors()) {
			model.addAttribute("pushAppUserVO", pushAppUserVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(pushAppUserVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("pushAppUserVO", pushAppUserVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushAppUserService.insertPushAppUser(pushAppUserVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushAppUserVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 앱사용자관리 수정폼 화면
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
		PushAppUserVO pushAppUserVO = pushAppUserService.selectPushAppUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushAppUserVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("pushAppUserVO", pushAppUserVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 앱사용자관리 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,PushAppUserVO pushAppUserVO ,BindingResult pushAppUserBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(pushAppUserVO, pushAppUserBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (pushAppUserBindingResult.hasErrors()) {
			model.addAttribute("pushAppUserVO", pushAppUserVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(pushAppUserVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("pushAppUserVO", pushAppUserVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushAppUserService.updatePushAppUser(pushAppUserVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushAppUserVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushAppUserVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 앱사용자관리 삭제 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do")
	@ProgramInfo(code="REMOVE", name="삭제폼 화면")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushAppUserVO pushAppUserVO = pushAppUserService.selectPushAppUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushAppUserVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushAppUserService.deletePushAppUser(id);
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
			resultParam.put("sId", pushAppUserVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 앱사용자관리 선택 삭제처리
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
		int result = pushAppUserService.deleteCheckedPushAppUser(checkedSId);
		
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
		pushAppUserService.selectPushAppUserExcelList(handler, search);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
	
	/**
	 * 앱사용자관리 일괄등록
	 * @param 
	 * @return
	 * @exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/syncUser.do")
	@ProgramInfo(code = "SYNC_USER", name = "사용자 동기화")
	public String syncUser(Model model, HttpServletRequest request) {
		
		if(!web.isExternalUse()) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();				
		}
		
		//사용자 동기화
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			externalService.insertPushAppUserFromViewUser();
		}
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		return web.returnSuccess();
	
	}
	
	/**
	 * 앱사용자관리 등록여부 확인
	 * @param 
	 * @return
	 * @exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/isIdExist.json")
	@ResponseBody
	public boolean isIdExist(HttpServletRequest request, @RequestParam(value = "userId", defaultValue="") String userId){
		return pushAppUserService.selectPushAppUserExist(userId, null);
	}
	
}
