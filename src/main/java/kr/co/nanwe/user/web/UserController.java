package kr.co.nanwe.user.web;

import java.security.PrivateKey;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: UserController
 * @Description 	: 사용자 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */

@RequestMapping(value = "/user")
@Program(code="USER", name="사용자")
@Controller
public class UserController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/user";
	
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
	public UserController() {		
		RequestMapping requestMapping = UserController.class.getAnnotation(RequestMapping.class);
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
		return web.forward(REDIRECT_PATH + "/mypage.do");
	}
		
	/**
	 * 사용자 마이페이지
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/mypage.do")
	@ProgramInfo(code="VIEW", name="목록조회")
	public String mypage(Model model, HttpServletRequest request){
		
		LoginVO loginVO = SessionUtil.getLoginUser(); 
		
		//로그인 사용자가 아닌경우
		if(loginVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//상세조회
		UserVO userVO = userService.selectMaskingUserByLoginUser(loginVO);
				
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/mypage");
	}
	
	/**
	 * 사용자 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request){
		
		LoginVO loginVO = SessionUtil.getLoginUser(); 
		
		//로그인 사용자가 아닌경우
		if(loginVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//외부사용자인 경우 회원정보 수정 불가
		if(StringUtil.isEqual(loginVO.getLoginUserType(), CodeConfig.EXTERANL_USER_CODE)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.external"));
			model.addAttribute("redirectUrl", "/user/mypage.do");
			return web.returnError();
		} 
		
		//공통사용자 권한 분류
		model.addAttribute("authList", commCdService.selectComUserAuthCdList());
		//신분구분
		model.addAttribute("uerTpList", commCdService.selectComAuthList("USER_TYPE"));
		//신분구분상세(졸업생 / 재학생)
		model.addAttribute("uerTpdetList", commCdService.selectComAuthList("USER_TYPE_DET"));
		//도외/도내 거주지 구분
		model.addAttribute("userLocList", commCdService.selectComAuthList("USER_LOC"));
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		
		//상세조회
		UserVO userVO = userService.selectUser(loginVO.getLoginId());
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/");			
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
	public String modifyAction(Model model, HttpServletRequest request,UserVO userVO, BindingResult userBindingResult
								,@RequestParam(value = "hiColgNm", defaultValue="") String hiColgNm) {
		
		LoginVO loginVO = SessionUtil.getLoginUser();
		
		//로그인 사용자가 아닌경우
		if(loginVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//외부사용자인 경우 회원정보 수정 불가
		if(StringUtil.isEqual(loginVO.getLoginUserType(), CodeConfig.EXTERANL_USER_CODE)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.external"));
			model.addAttribute("redirectUrl", "/user/mypage.do");
			return web.returnError();
		}
		
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
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/mypage.do");
			return web.returnError();
		}
		
		//RSA 유효성 검증을 위해 객체 생성 후 진행 (에러시 리턴은 파라미터로 넘어온 객체를 리턴)
		if(userVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/mypage.do");
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
		
		//로그인 아이디와 입력된 아이디가 다른경우
		if(!StringUtil.isEqual(loginVO.getLoginId(), userVO.getUserId())) {
			return web.redirect(model, "/");
		}
				
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
		
		//데이터 변조 방지
		UserVO oriUserVO = userService.selectUser(loginVO.getLoginId());
		if(oriUserVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/");
			return web.returnError();
		}
		
		//권한
		validUserVO.setAuthCd(oriUserVO.getAuthCd());
		//사용유무
		validUserVO.setUseYn(oriUserVO.getUseYn());
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userService.updateUser(validUserVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/mypage.do");
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			return web.returnError();
		}
		
	}
	
}
