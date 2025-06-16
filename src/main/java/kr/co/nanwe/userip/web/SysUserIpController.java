package kr.co.nanwe.userip.web;

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
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.userip.service.UserIpService;
import kr.co.nanwe.userip.service.UserIpVO;

/**
 * @Class Name 		: UserIpController
 * @Description 	: 사용자 아이피 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="COM_USER_IP", name="사용자 아이피 관리")
@Controller
public class SysUserIpController {
	
	/** View Path */
	private static final String VIEW_PATH = "sys/userIp";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/userIp";
	
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
	@Resource(name = "userIpService")
	private UserIpService userIpService;
	
	/** Constructor */
	public SysUserIpController() {		
		RequestMapping requestMapping = SysUserIpController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 사용자 아이피 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/userIp.do", "/sys/userIp/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = userIpService.selectUserIpList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 사용자 아이피 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sIp", defaultValue="") String ip){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		UserIpVO userIpVO = userIpService.selectUserIp(id, ip);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userIpVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("userIpVO", userIpVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 사용자 아이피 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 생성
		UserIpVO userIpVO = new UserIpVO();
		
		//조회결과 MODEL ADD
		model.addAttribute("userIpVO", userIpVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 사용자 아이피 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,UserIpVO userIpVO ,BindingResult userIpBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(userIpVO, userIpBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (userIpBindingResult.hasErrors()) {
			model.addAttribute("userIpVO", userIpVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(userIpVO == null) {
			addtionalValid = false;
			userIpBindingResult.rejectValue("userId", "errors.id");
		}
		if(userIpService.selectUserIpExist(userIpVO.getUserId(), userIpVO.getIp())) {
			addtionalValid = false;
			userIpBindingResult.rejectValue("ip", "errors.existdata");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("userIpVO", userIpVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userIpService.insertUserIp(userIpVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userIpVO.getUserId());
			resultParam.put("sIp", userIpVO.getIp());
			model.addAttribute("resultParam", resultParam);
			
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			return web.returnError();			
		}
		
	}
	
	/**
	 * 사용자 아이피 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sIp", defaultValue="") String ip){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		UserIpVO userIpVO = userIpService.selectUserIp(id, ip);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userIpVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("userIpVO", userIpVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 사용자 아이피 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,UserIpVO userIpVO ,BindingResult userIpBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(userIpVO, userIpBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (userIpBindingResult.hasErrors()) {
			model.addAttribute("userIpVO", userIpVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(userIpVO == null) {
			addtionalValid = false;
			userIpBindingResult.rejectValue("userId", "errors.id");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("userIpVO", userIpVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userIpService.updateUserIp(userIpVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userIpVO.getUserId());
			resultParam.put("sIp", userIpVO.getIp());
			model.addAttribute("resultParam", resultParam);
			
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userIpVO.getUserId());
			resultParam.put("sIp", userIpVO.getIp());
			model.addAttribute("resultParam", resultParam);
			
			return web.returnError();			
		}
		
	}
	
	/**
	 * 사용자 아이피 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sIp", defaultValue="") String ip) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 조회
		UserIpVO userIpVO = userIpService.selectUserIp(id, ip);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userIpVO == null) {
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userIpService.deleteUserIp(userIpVO.getUserId(), userIpVO.getIp());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시	
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnSuccess();
			
		} else { //실패시			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userIpVO.getUserId());
			resultParam.put("sIp", userIpVO.getIp());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 사용자 아이피 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/userIp/checkRemoveAction.do", method = RequestMethod.POST)
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
		int result = userIpService.deleteCheckedUserIp(checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		
		return web.returnSuccess();
		
	}
	
	/**
	 * 사용자 아이피 엑셀다운
	 * @param 
	 * @return
	 * @exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/sys/userIp/excelDown.do")
	@ProgramInfo(code = "COM_USER_IP_EXCEL_DOWN", name = "사용자 아이피 엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search) throws IOException {

		//엑셀 파일명
		String excelName = "사용자 아이피 엑셀";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles = {"아이디", "이름", "아이피", "INPT_ID", "INPT_IP", "INPT_DTTM"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes = {"USER_ID", "USER_NM", "IP", "INPT_ID", "INPT_IP", "INPT_DTTM"};
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, true);
		
		//데이터 조회
		userIpService.selectUserIpExcelList(handler, search);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
	
}
