package kr.co.nanwe;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.EgovHttpSessionBindingListener;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.login.service.SessionBindingListener;
import kr.co.nanwe.site.service.DomainVO;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;
import kr.co.nanwe.template.service.TemplateMgtVO;
import kr.co.nanwe.template.service.TemplateService;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;
import kr.co.nanwe.userip.service.UserIpService;
import kr.co.nanwe.userip.service.UserIpVO;

/**
 * @Class Name 		: StartController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class StartController {
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "loginService")
	private LoginService loginService;
	
	/** Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** Service */
	@Resource(name = "userIpService")
	private UserIpService userIpService;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** Service */
	@Resource(name = "templateService")
	private TemplateService templateService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	/**
	 * 관리자등록
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/sys/start/step1.do")
	public String step1(Model model, HttpServletRequest request){
		
		//관리자 계정이 등록되어있는지 확인
		boolean admCheck = loginService.checkSysUserExist();
		if(admCheck) {
			return web.redirect(model, "/sys.do");
		}
		
		//VO 생성
		UserVO userVO = new UserVO();
		
		//기본값 세팅
		userVO.setUseYn("Y");
		userVO.setAuthCd(CodeConfig.SYS_USER_CODE);
		
		//조회결과 MODEL ADD
		model.addAttribute("userVO", userVO);
		
		return web.returnView("sys", "/start/step1", "EMPTY");
	}
	
	/**
	 * 관리자등록 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/start/step1Action.do")
	public String step1Action(Model model, HttpServletRequest request, UserVO userVO ,BindingResult userBindingResult) {

		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			model.addAttribute("redirectUrl", "/sys/start/step1.do");
			return web.returnError();
		}
		
		//RSA 유효성 검증을 위해 객체 생성 후 진행 (에러시 리턴은 파라미터로 넘어온 객체를 리턴)
		if(userVO == null) {
			model.addAttribute("redirectUrl", "/sys/start/step1.do");
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
			model.addAttribute("userVO", userVO);
			return web.returnView("sys", "/start/step1", "EMPTY");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//중복 아이디 검사
		if(userService.selectUserIdExistBySysUser(validUserVO.getUserId())) {
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
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("userVO", userVO);			
			return web.returnView("sys", "/start/step1", "EMPTY");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			//관리자 권한
			validUserVO.setAuthCd(CodeConfig.SYS_USER_CODE);
			result = userService.insertUser(validUserVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//해당 아이피 있는지 체크후 없으면 등록
			String userIp = ClientUtil.getUserIp();
			boolean ipExist = userIpService.selectUserIpExist(validUserVO.getUserId(), userIp);
			if(!ipExist) {
				UserIpVO userIpVO = new UserIpVO();
				userIpVO.setUserId(validUserVO.getUserId());
				userIpVO.setUserNm(validUserVO.getUserNm());
				userIpVO.setIp(userIp);
				userIpService.insertUserIp(userIpVO);
			}
			
			//로그인 처리
			LoginVO loginInfo = loginService.selectLoginSysAdmUser(validUserVO.getUserId(), password);
			if("Y".equals(loginInfo.getLoginSuccess())) {
				SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, loginInfo);
			}
			
			//중복로그인 리스너
			if(!web.isMultiLogin()) {
				EgovHttpSessionBindingListener listener = EgovHttpSessionBindingListener.getInstance();
				request.getSession().setAttribute(loginInfo.getLoginId(), listener);
			} else {
				SessionBindingListener listener = SessionBindingListener.getInstance();
				request.getSession().setAttribute(loginInfo.getLoginId(), listener);
			}
			
			//사이트가 등록되어있는지 확인
			int siteCount = siteService.selectSiteCount();
			if(siteCount > 0) {
				return web.redirect(model, "/sys.do");
			} else {
				return web.redirect(model, "/sys/start/step2.do");
			}			
		} else { //실패시
			model.addAttribute("redirectUrl", "/sys/start/step1.do");
			return web.returnError();			
		}		
	}
	
	/**
	 * 사이트등록
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/sys/start/step2.do")
	public String step2(Model model, HttpServletRequest request){
		
		//사이트가 등록되어있는지 확인
		int siteCount = siteService.selectSiteCount();
		if(siteCount > 0) {
			return web.redirect(model, "/sys.do");
		}
		
		//VO 생성
		SiteVO siteVO = new SiteVO();
		
		//기본값 세팅
		siteVO.setSiteCd(CodeConfig.MAIN_SITE_CD);
		siteVO.setSysAccessYn("Y");
		siteVO.setLoginYn("Y");
		
		//현재 주소로 최초 한개의 도메인을 설정한다.
		List<DomainVO> domainList = new ArrayList<DomainVO>();
		DomainVO domainVO = new DomainVO();
		domainVO.setDomain(RequestUtil.getDomain());
		domainVO.setDefaultYn("Y");
		domainVO.setUseYn("Y");
		domainList.add(domainVO);
		siteVO.setDomainList(domainList);
		
		//템플릿 목록
		List<TemplateMgtVO> templateList = templateService.selectTemplateUseList();
		model.addAttribute("templateList", templateList);
		
		//조회결과 MODEL ADD
		model.addAttribute("siteVO", siteVO);
		
		return web.returnView("sys", "/start/step2", "EMPTY");
	}
	
	/**
	 * 사이트등록 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/start/step2Action.do")
	public String step2Action(Model model, HttpServletRequest request, SiteVO siteVO ,BindingResult siteBindingResult) {
		
		//템플릿 목록
		List<TemplateMgtVO> templateList = templateService.selectTemplateUseList();
		model.addAttribute("templateList", templateList);
		
		///유효성 검증
		beanValidator.validate(siteVO, siteBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (siteBindingResult.hasErrors()) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView("sys", "/start/step2", "EMPTY");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//유효성 검사 로직 작성
		if(siteVO == null) {
			model.addAttribute("redirectUrl", "/sys/start/step2.do");
			return web.returnError();
		}	
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView("sys", "/start/step2", "EMPTY");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			siteVO.setSiteCd(CodeConfig.MAIN_SITE_CD);
			siteVO.setUseYn("Y");
			siteVO.setSysAccessYn("Y");
			siteVO.setLoginYn("Y");
			result = siteService.insertSysSite(siteVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시			
			model.addAttribute("redirectUrl", "/sys.do");			
			return web.returnSuccess();
			
		} else { //실패시		
			model.addAttribute("redirectUrl", "/sys/start/step2.do");
			return web.returnError();				
		}	
	}
}