package kr.co.nanwe.user.web;

import java.io.IOException;
import java.security.PrivateKey;
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
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: SysUserController
 * @Description 	: 사용자 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */

@RequestMapping(value = "/sys/user")
@Program(code="COM_USER", name="사용자관리")
@Controller
public class SysUserController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/user";
	
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
	@Resource(name = "userService")
	private UserService userService;
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	/** Constructor */
	public SysUserController() {		
		RequestMapping requestMapping = SysUserController.class.getAnnotation(RequestMapping.class);
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
	 * 사용자 목록조회
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
		Map<String, Object> map = userService.selectUserList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 사용자 상세조회
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
		UserVO userVO = userService.selectUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 사용자 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한코드
		List<CommCdVO> authList = commCdService.selectAuthCdList();
		model.addAttribute("authList", authList);
		
		//신분구분
		List<CommCdVO> uerTpList = commCdService.selectComAuthList("USER_TYPE");
		model.addAttribute("uerTpList", uerTpList);
		
		//신분구분상세(졸업생 / 재학생)
		List<CommCdVO> uerTpdetList = commCdService.selectComAuthList("USER_TYPE_DET");
		model.addAttribute("uerTpdetList", uerTpdetList);

		//도외/도내 거주지 구분
		List<CommCdVO> userLocList = commCdService.selectComAuthList("USER_LOC");
		model.addAttribute("userLocList", userLocList);
		
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		
		//VO 생성
		UserVO userVO = new UserVO();
		
		//기본값 세팅
		userVO.setUseYn("Y");
		
		//조회결과 MODEL ADD
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 사용자 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,UserVO userVO ,BindingResult userBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한코드
		List<CommCdVO> authList = commCdService.selectAuthCdList();
		model.addAttribute("authList", authList);
		
		//신분구분
		List<CommCdVO> uerTpList = commCdService.selectComAuthList("USER_TYPE");
		model.addAttribute("uerTpList", uerTpList);
		
		//신분구분상세(졸업생 / 재학생)
		List<CommCdVO> uerTpdetList = commCdService.selectComAuthList("USER_TYPE_DET");
		model.addAttribute("uerTpdetList", uerTpdetList);
		
		//도외/도내 거주지 구분
		List<CommCdVO> userLocList = commCdService.selectComAuthList("USER_LOC");
		model.addAttribute("userLocList", userLocList);
		
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//RSA 유효성 검증을 위해 객체 생성 후 진행 (에러시 리턴은 파라미터로 넘어온 객체를 리턴)
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		String password = rsaUtil.decryptRsa(privateKey, userVO.getPassword());
		String passwordCheck = rsaUtil.decryptRsa(privateKey, userVO.getPasswordCheck());
		UserVO validUserVO = userVO;
		validUserVO.setPassword(password);
		validUserVO.setPasswordCheck(passwordCheck);

		//유효성 검증
		beanValidator.validate(validUserVO, userBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (userBindingResult.hasErrors()) {
			validUserVO.setPassword("");
			validUserVO.setPasswordCheck("");
			model.addAttribute("userVO", validUserVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//중복 아이디 검사
		if(userService.selectUserIdExist(validUserVO.getUserId())) {
			addtionalValid = false;
			userBindingResult.rejectValue("userId", "errors.id.exist");
		}
		//비밀번호 유효성 검사
		if(StringUtil.isNull(validUserVO.getPassword())) {
			addtionalValid = false;
			userBindingResult.rejectValue("password", "errors.password");
		}
		if(!validUserVO.getPassword().equals(validUserVO.getPasswordCheck())) {
			addtionalValid = false;
			userBindingResult.rejectValue("passwordCheck", "errors.password.notequal");
		}
		
		//인증사용인 경우 해당 컬럼 중복체크
		String certUse = StringUtil.isNullToString(web.getServerProp("server.user.join.cert.use"));
		if("Y".equals(certUse)) {
			String certType = StringUtil.isNullToString(web.getServerProp("server.user.join.cert.type"));
			if("PHONE".equals(certType)) {
				if(userService.selectUserPhoneExist(userVO.getUserId(), userVO.getMbphNo())) {
					addtionalValid = false;
					userBindingResult.rejectValue("mbphNo", "errors.existdata");
				}
			} else if("EMAIL".equals(certType)) {
				if(userService.selectUserEmailExist(userVO.getUserId(), userVO.getEmail())) {
					addtionalValid = false;
					userBindingResult.rejectValue("email", "errors.existdata");
				}
			}
		}
		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(validUserVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(validUserVO.getUploadFile()))) {
			addtionalValid = false;
			userBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			validUserVO.setPassword("");
			validUserVO.setPasswordCheck("");
			model.addAttribute("userVO", validUserVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userService.insertUser(validUserVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");

			return web.returnError();
			
		}
		
	}
	
	/**
	 * 사용자 수정폼 화면
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
		
		//권한코드
		List<CommCdVO> authList = commCdService.selectAuthCdList();
		model.addAttribute("authList", authList);
		
		//신분구분
		List<CommCdVO> uerTpList = commCdService.selectComAuthList("USER_TYPE");
		model.addAttribute("uerTpList", uerTpList);
		
		//신분구분상세(졸업생 / 재학생)
		List<CommCdVO> uerTpdetList = commCdService.selectComAuthList("USER_TYPE_DET");
		model.addAttribute("uerTpdetList", uerTpdetList);
		
		//도외/도내 거주지 구분
		List<CommCdVO> userLocList = commCdService.selectComAuthList("USER_LOC");
		model.addAttribute("userLocList", userLocList);
		
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		
		//상세조회
		UserVO userVO = userService.selectUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 사용자 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,UserVO userVO ,BindingResult userBindingResult
									,@RequestParam(value = "hiColgNm", defaultValue="") String hiColgNm) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한코드
		List<CommCdVO> authList = commCdService.selectAuthCdList();
		model.addAttribute("authList", authList);
		
		//신분구분
		List<CommCdVO> uerTpList = commCdService.selectComAuthList("USER_TYPE");
		model.addAttribute("uerTpList", uerTpList);
		
		//신분구분상세(졸업생 / 재학생)
		List<CommCdVO> uerTpdetList = commCdService.selectComAuthList("USER_TYPE_DET");
		model.addAttribute("uerTpdetList", uerTpdetList);
		
		//도외/도내 거주지 구분
		List<CommCdVO> userLocList = commCdService.selectComAuthList("USER_LOC");
		model.addAttribute("userLocList", userLocList);
		
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		
		
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//RSA 유효성 검증을 위해 객체 생성 후 진행 (에러시 리턴은 파라미터로 넘어온 객체를 리턴)
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		if(!"".equals(hiColgNm)) {
			userVO.setColgNm(hiColgNm);
		}
		
		String password = rsaUtil.decryptRsa(privateKey, userVO.getPassword());
		String passwordCheck = rsaUtil.decryptRsa(privateKey, userVO.getPasswordCheck());
		UserVO validUserVO = userVO;
		validUserVO.setPassword(password);
		validUserVO.setPasswordCheck(passwordCheck);
		
		//유효성 검증 에러인 경우 RETURN
		if (userBindingResult.hasErrors()) {
			validUserVO.setPassword("");
			validUserVO.setPasswordCheck("");
			model.addAttribute("userVO", validUserVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//비밀번호 유효성 검사 (새 비밀번호 등록시)
		if(!StringUtil.isNull(validUserVO.getPassword())) {
			if(!validUserVO.getPassword().equals(validUserVO.getPasswordCheck())) {
				addtionalValid = false;
				userBindingResult.rejectValue("passwordCheck", "errors.password.notequal");
			}
		}
		
		//인증사용인 경우 해당 컬럼 중복체크
		String certUse = StringUtil.isNullToString(web.getServerProp("server.user.join.cert.use"));
		if("Y".equals(certUse)) {
			String certType = StringUtil.isNullToString(web.getServerProp("server.user.join.cert.type"));
			if("PHONE".equals(certType)) {
				if(userService.selectUserPhoneExist(userVO.getUserId(), userVO.getMbphNo())) {
					addtionalValid = false;
					userBindingResult.rejectValue("mbphNo", "errors.existdata");
				}
			} else if("EMAIL".equals(certType)) {
				if(userService.selectUserEmailExist(userVO.getUserId(), userVO.getEmail())) {
					addtionalValid = false;
					userBindingResult.rejectValue("email", "errors.existdata");
				}
			}
		}
		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(validUserVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(validUserVO.getUploadFile()))) {
			addtionalValid = false;
			userBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			validUserVO.setPassword("");
			validUserVO.setPasswordCheck("");
			model.addAttribute("userVO", validUserVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userService.updateUser(validUserVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 사용자 삭제처리
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
		UserVO userVO = userService.selectUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			
//			//결과메시지  (생략가능)
//			model.addAttribute("resultMsg", "");
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
//			//리턴 요청 방식 (DEFAULT : POST, 생략가능)
//			model.addAttribute("redirectMethod", "POST");
			
//			//리턴시 추가 파라미터 필요한경우 (생략가능)
//			Map<String, Object> resultParam = new HashMap<String, Object>();
//			resultParam.put("KEY", "VALUE");			
//			model.addAttribute("resultParam", resultParam);
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userService.deleteUser(userVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			
//			//결과메시지  (생략가능)
//			model.addAttribute("resultMsg", "");
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
//			//리턴 요청 방식 (DEFAULT : POST, 생략가능)
//			model.addAttribute("redirectMethod", "POST");

			return web.returnSuccess();
			
		} else { //실패시			
			
//			//결과메시지  (생략가능)
//			model.addAttribute("resultMsg", "");
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
//			//리턴 요청 방식 (DEFAULT : POST, 생략가능)
//			model.addAttribute("redirectMethod", "POST");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", userVO.getUserId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 탈퇴사용자  목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/del/list.do")
	@ProgramInfo(code="DEL_LIST", name="목록조회")
	public String delList(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = userService.selectDelUserList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/del_list");
	}
	
	/**
	 * 탈퇴사용자 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/del/view.do")
	@ProgramInfo(code="DEL_VIEW", name="상세조회")
	public String delView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		UserVO userVO = userService.selectUser(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/del/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/del_view");
	}
	
	/**
	 * 탈퇴사용자 상태변경
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/del/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="DEL_MODIFY", name="수정처리")
	public String delModifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								, @RequestParam(value = "userId", defaultValue="") String userId
								, @RequestParam(value = "deleteState", defaultValue="") String deleteState) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 조회
		UserVO userVO = userService.selectUser(userId);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/del/list.do");
			return web.returnError();
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userService.updateUserDelState(userId, deleteState);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/del/list.do");	
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/del/list.do");	
			return web.returnError();			
		}		
	}
	
	@RequestMapping(value = "/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search) throws IOException {
		
		//엑셀 파일명
		String excelName = "전체 사용자 목록";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"순번", "사용자ID", "사용자명", "회원구분", "단과대학", "학과명", "학번", "학년", "휴대전화번호", "성별","EMAIL", "지역", "사용여부", "가입일시"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes =  {"ROWNUM", "USER_ID", "USER_NM", "USER_TYPE_CD_NM", "COLG_NM", "DEPT_NM", "STD_NO", "GRADE", "MBPH_NO", "SEX_NM", "EMAIL", "LOCAL", "USE_YN", "INPT_DTTM"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook();
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, false);
		
		//데이터 조회
		userService.selectUserListForExcel(handler, search);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
	
}
