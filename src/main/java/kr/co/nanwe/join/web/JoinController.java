package kr.co.nanwe.join.web;

import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.join.service.JoinVO;
import kr.co.nanwe.join.service.TermsService;
import kr.co.nanwe.join.service.TermsVO;
import kr.co.nanwe.login.service.CertificationService;
import kr.co.nanwe.login.service.FacebookManager;
import kr.co.nanwe.login.service.GoogleManager;
import kr.co.nanwe.login.service.KakaoManager;
import kr.co.nanwe.login.service.NaverManager;
import kr.co.nanwe.user.service.SnsUserService;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: JoinController
 * @Description 	: 약관 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */

@RequestMapping(value="/join")
@Program(code="JOIN", name="회원가입")
@Controller
public class JoinController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/join";
	
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
	
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "termsService")
	private TermsService termsService;
	
	@Resource(name = "certificationService")
	private CertificationService certificationService;
	
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	@Resource(name = "snsUserService")
	private SnsUserService snsUserService;
	
	@Resource(name = "kakao")
	private KakaoManager kakao;
	
	@Resource(name = "naver")
	private NaverManager naver;
	
	@Resource(name = "google")
	private GoogleManager google;
	
	@Resource(name = "facebook")
	private FacebookManager facebook;
	
	/** Constructor */
	public JoinController() {		
		RequestMapping requestMapping = JoinController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/** Root Forward */
	@RequestMapping(value="")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/agree.do");
	}
	
	/**
	 * 회원가입 
	 * STEP 1. 약관동의
	 */
	@RequestMapping(value="/agree.do")
	@ProgramInfo(code="AGREE", name="약관동의")
	public String agree(Model model){
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
		
		//회원가입 약관 목록
		List<TermsVO> termList = termsService.selectTermsListByUse();
		
		JoinVO joinVO = new JoinVO();
		joinVO.setTermList(termList);
		
		model.addAttribute("joinVO", joinVO);
		
		return web.returnView(VIEW_PATH, "/agree", "EMPTY");
	}
	
	/**
	 * 회원가입 
	 * STEP 2. 회원정보 입력
	 */
	@RequestMapping(value="/register.do", method = RequestMethod.POST)
	public String register(Model model , HttpServletRequest request, @ModelAttribute("joinVO") JoinVO joinVO){
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
		
		if(joinVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		//비동의시
		if (!joinVO.isAgree()) {
			model.addAttribute("resultMsg", messageUtil.getMessage("content.join.message.agree") );
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		//약관 동의 체크
		boolean check = termsService.checkTermsList(joinVO.getTermList());
		if(!check) {
			model.addAttribute("resultMsg", messageUtil.getMessage("content.join.message.agree") );
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		model.addAttribute("joinVO", joinVO);
		
		//회원가입 인증 사용시
		boolean certUse = false;
		String certType = "";
		if(StringUtil.isEqual(web.getServerProp("server.user.join.cert.use"), "Y")) {
			certUse = true;
			certType = web.getServerProp("server.user.join.cert.type");
			certificationService.resetCertification(request);
		}
		model.addAttribute("certUse", certUse);
		model.addAttribute("certType", certType);
		
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		//공통사용자 권한 분류
		model.addAttribute("authList", commCdService.selectComUserAuthCdList());
		//신분구분
		model.addAttribute("uerTpList", commCdService.selectComAuthList("USER_TYPE"));
		//신분구분상세(졸업생 / 재학생)
		model.addAttribute("uerTpdetList", commCdService.selectComAuthList("USER_TYPE_DET"));
		//도외/도내 거주지 구분
		model.addAttribute("userLocList", commCdService.selectComAuthList("USER_LOC"));
		
		return web.returnView(VIEW_PATH, "/register", "EMPTY");		
		
	}
	
	/**
	 * 회원가입 
	 * STEP 3. 회원정보 입력 처리
	 */
	@RequestMapping(value="/registerAction.do" , method = RequestMethod.POST)
	public String registerAction(Model model, HttpServletRequest request, RedirectAttributes redirectAttr, JoinVO joinVO, BindingResult joinBindingResult) {
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
				
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		//RSA 유효성 검증을 위해 객체 생성 후 진행 (에러시 리턴은 파라미터로 넘어온 객체를 리턴)
		if(joinVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		//비동의시
		if (!joinVO.isAgree()) {
			model.addAttribute("resultMsg", messageUtil.getMessage("content.join.message.agree") );
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		//약관 동의 체크
		boolean check = termsService.checkTermsList(joinVO.getTermList());
		if(!check) {
			model.addAttribute("resultMsg", messageUtil.getMessage("content.join.message.agree") );
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		//2차인증검증
		boolean certUse = false;
		String certType = "";
		if(StringUtil.isEqual(web.getServerProp("server.user.join.cert.use"), "Y")) {
			certUse = true;
			certType = web.getServerProp("server.user.join.cert.type");
			boolean certResult = certificationService.checkEncryptCertKey(request, joinVO.getCertKey());
			if(!certResult) {
				model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
				return web.returnError();
			} else {
				joinVO.setCertKey("");
				certificationService.resetCertification(request);
			}
		}
		model.addAttribute("certUse", certUse);
		model.addAttribute("certType", certType);
		
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
		
		//권한을 관리자로 설정한경우 FALSE
		if(StringUtil.isEqual(joinVO.getAuthCd(), CodeConfig.SYS_USER_CODE)) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();
		}
		
		String password = rsaUtil.decryptRsa(privateKey, joinVO.getPassword());
		String passwordCheck = rsaUtil.decryptRsa(privateKey, joinVO.getPasswordCheck());
		joinVO.setPassword(password);
		joinVO.setPasswordCheck(passwordCheck);
		
		//유효성 검증
		beanValidator.validate(joinVO, joinBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (joinBindingResult.hasErrors()) {
			joinVO.setPassword("");
			joinVO.setPasswordCheck("");
			model.addAttribute("joinVO", joinVO);
			return web.returnView(VIEW_PATH, "/register", "EMPTY");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//중복 아이디 검사
		if(userService.selectUserIdExist(joinVO.getUserId())) {
			addtionalValid = false;
			joinBindingResult.rejectValue("userId", "errors.id.exist");
		}
		//비밀번호 유효성 검사
		if(StringUtil.isNull(joinVO.getPassword())) {
			addtionalValid = false;
			joinBindingResult.rejectValue("password", "errors.password");
		}
		if(!joinVO.getPassword().equals(joinVO.getPasswordCheck())) {
			addtionalValid = false;
			joinBindingResult.rejectValue("passwordCheck", "errors.password.notequal");
		}
		//인증사용인 경우 해당 컬럼 중복체크
		if(certUse) {
			if("PHONE".equals(certType)) {
				if(userService.selectUserPhoneExist(joinVO.getUserId(), joinVO.getMbphNo())) {
					addtionalValid = false;
					joinBindingResult.rejectValue("mbphNo", "errors.existdata");
				}
			} else if("EMAIL".equals(certType)) {
				if(userService.selectUserEmailExist(joinVO.getUserId(), joinVO.getEmail())) {
					addtionalValid = false;
					joinBindingResult.rejectValue("email", "errors.existdata");
				}
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			joinVO.setPassword("");
			joinVO.setPasswordCheck("");
			model.addAttribute("joinVO", joinVO);
			return web.returnView(VIEW_PATH, "/register", "EMPTY");
		}
		
		UserVO userVO = new UserVO();
		
		userVO.setUserId(joinVO.getUserId());
		userVO.setUserNm(joinVO.getUserNm());
		userVO.setPassword(password);
		userVO.setPasswordCheck(passwordCheck);
		userVO.setBirth(joinVO.getBirth());
		userVO.setPostNo(joinVO.getPostNo());
		userVO.setAddr(joinVO.getAddr());
		userVO.setDetlAddr(joinVO.getDetlAddr());
		userVO.setMbphNo(joinVO.getMbphNo());
		userVO.setTelNo(joinVO.getTelNo());
		userVO.setEmail(joinVO.getEmail());
		userVO.setAuthCd(joinVO.getAuthCd());
		userVO.setDeptCd(joinVO.getDeptCd());
		userVO.setDeptNm(joinVO.getDeptNm());
		
		//유저정보 추가
		userVO.setUserTypeCd(joinVO.getUserTypeCd());
		userVO.setUserTypeDetCd(joinVO.getUserTypeDetCd());
		userVO.setStdNo(joinVO.getStdNo());
		userVO.setColgCd(joinVO.getColgCd());
		userVO.setGrade(joinVO.getGrade());
		userVO.setSex(joinVO.getSex());
		userVO.setLocal(joinVO.getLocal());
		
		//자동승인여부
		userVO.setUseYn(web.getServerProp("server.user.join.auto.approve"));
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = userService.insertUser(userVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			//약관 등록
			termsService.insertTermsJoinUser(joinVO);
			UserVO successUser = userService.selectUser(userVO.getUserId());
			redirectAttr.addFlashAttribute("successUser", successUser);
			return "redirect:/join/result.do";
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/agree.do");
			return web.returnError();			
		}		
	}
	
	/**
	 * 회원가입 
	 * STEP 3. 회원가입 완료
	 */
	@RequestMapping(value="/result.do")
	public String result(Model model , HttpServletRequest request){
		Map<String, ?> redirectMap = RequestContextUtils.getInputFlashMap(request);
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
		
		if(redirectMap != null) {
			UserVO successUser = (UserVO) redirectMap.get("successUser");
			model.addAttribute("successUser", successUser);
		} else {
			return web.redirect(model, "/");
		}
		return web.returnView(VIEW_PATH, "/result", "EMPTY");
	}
	
	/********************************************************************************************************************/
	/** SNS 회원가입 */
	/********************************************************************************************************************/
		
	/**
	 * 회원가입 
	 * STEP 1. 약관동의
	 */
	@RequestMapping(value="/sns/{path}/agree.do")
	@ProgramInfo(code="SNS_AGREE", name="약관동의")
	public String snsAgree(Model model, HttpServletRequest request
							, @PathVariable("path") String snsType
							, @RequestParam(value = "joinToken", defaultValue="") String joinToken
							, @RequestParam(value = "authToken", defaultValue="") String authToken){
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
		
		if (StringUtil.isNull(snsType) || StringUtil.isNull(joinToken) || StringUtil.isNull(authToken)) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		if(request.getSession().getAttribute("joinToken") == null) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		String sessionJoinToken = (String) request.getSession().getAttribute("joinToken");		
		if(!StringUtil.isEqual(joinToken, sessionJoinToken)) {
			request.getSession().removeAttribute("joinToken");
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		JoinVO joinVO = new JoinVO();
		//회원가입 약관 목록
		List<TermsVO> termList = termsService.selectTermsListByUse();
		joinVO.setTermList(termList);
		
		model.addAttribute("joinToken", joinToken);
		model.addAttribute("authToken", authToken);
		model.addAttribute("snsType", snsType);
		model.addAttribute("joinVO", joinVO);
		
		return web.returnView(VIEW_PATH, "/sns/agree", "EMPTY");
	}
	

	
	/**
	 * 회원가입 
	 * STEP 3. 회원정보 입력 처리
	 */
	@RequestMapping(value="/sns/{path}/registerAction.do" , method = RequestMethod.POST)
	public String snsRegisterAction( Model model, HttpServletRequest request, RedirectAttributes redirectAttr
								, @PathVariable("path") String snsType
								, @ModelAttribute("join") JoinVO joinVO
								, @RequestParam(value = "joinToken", defaultValue="") String joinToken
								, @RequestParam(value = "authToken", defaultValue="") String authToken) {
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
		
		if (StringUtil.isNull(snsType) || StringUtil.isNull(joinToken) || StringUtil.isNull(authToken)) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		if(request.getSession().getAttribute("joinToken") == null) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		String sessionJoinToken = (String) request.getSession().getAttribute("joinToken");		
		if(!StringUtil.isEqual(joinToken, sessionJoinToken)) {
			request.getSession().removeAttribute("joinToken");
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//비동의시
		if (!joinVO.isAgree()) {
			model.addAttribute("resultMsg", messageUtil.getMessage("content.join.message.agree"));
			model.addAttribute("redirectUrl", "/join/sns/"+snsType+"/agree.do?joinToken="+joinToken+"&authToken="+authToken);
			return web.returnError();
		}
		
		//약관 동의 체크
		boolean check = termsService.checkTermsList(joinVO.getTermList());
		if(!check) {
			model.addAttribute("resultMsg", messageUtil.getMessage("content.join.message.agree"));
			model.addAttribute("redirectUrl", "/join/sns/"+snsType+"/agree.do?joinToken="+joinToken+"&authToken="+authToken);
			return web.returnError();
		}
		
		snsType = snsType.toLowerCase();		
		UserVO userVO = null;		
		switch (snsType) {		
			case "kakao":
				userVO = kakao.getUserInfo(authToken);
				break;				
			case "naver":
				userVO = naver.getUserInfo(authToken);
				break;				
			case "google":
				userVO = google.getUserInfo(authToken);				
				break;				
			case "facebook":
				userVO = facebook.getUserInfo(authToken);
				break;				
		}
		
		if(userVO == null) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//자동승인여부
		userVO.setUseYn(web.getServerProp("server.user.join.auto.approve"));
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = snsUserService.insertSnsUser(userVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			//약관 등록
			termsService.insertTermsJoinUser(joinVO);
			UserVO successUser = userService.selectUser(userVO.getUserId());
			redirectAttr.addFlashAttribute("successUser", successUser);
			return "redirect:/join/sns/"+snsType+"/result.do";
		} else { //실패시
			model.addAttribute("redirectUrl", "/join/sns/"+snsType+"/agree.do?joinToken="+joinToken+"&authToken="+authToken);
			return web.returnError();			
		}		
	}
	
	/**
	 * 회원가입 
	 * STEP 3. 회원가입 완료
	 */
	@RequestMapping(value="/sns/{path}/result.do")
	public String snsResult(Model model , HttpServletRequest request){
		Map<String, ?> redirectMap = RequestContextUtils.getInputFlashMap(request);
		
		//로그인 중이거나 회원가입이 안될 경우 메인페이지로 이동
		if(SessionUtil.getLoginUser() != null || !StringUtil.isEqual(web.getServerProp("server.user.join.use"), "Y")) {
			return web.redirect(model, "/");
		}
		
		if(redirectMap != null) {
			UserVO successUser = (UserVO) redirectMap.get("successUser");
			model.addAttribute("successUser", successUser);
		} else {
			return web.redirect(model, "/");
		}
		return web.returnView(VIEW_PATH, "/sns/result", "EMPTY");
	}
	
}