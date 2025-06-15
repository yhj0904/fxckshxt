package kr.co.nanwe.login.web;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.CertificationService;
import kr.co.nanwe.login.service.EgovHttpSessionBindingListener;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.login.service.SessionBindingListener;
import kr.co.nanwe.login.service.SsoService;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: LoginController
 * @Description 	: 로그인 Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class LoginController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/login";
	
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
	@Resource(name = "ssoService")
	private SsoService ssoService;
	
	/** Service */
	@Resource(name = "certificationService")
	private CertificationService certificationService;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;

	
	/**
	 * 로그인 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/login.do")
	public String login(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "returnLogin", defaultValue="") String returnLogin){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("returnLogin", returnLogin);
		
		//사이트 정보
		String siteCd = "";
		SiteVO siteVO = siteService.selectSiteByDomain(RequestUtil.getDomain());
		if(siteVO != null && !StringUtil.isNull(siteVO.getSiteCd())) {
			siteCd = siteVO.getSiteCd();
		} else {
			siteCd = CodeConfig.MAIN_SITE_CD;
		}
		
		LoginVO loginVO = null;
		
		//SSO 사용하는 경우 체크
		if(web.isSsoUse()) {
			loginVO = ssoService.getSsoLoginInfo(request);
			if(loginVO != null) {					
				SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, loginVO);
				
				//로그 등록
				loginService.insertLoginLog(loginVO);
				
				//중복로그인 리스너
				if(!web.isMultiLogin()) {
					String loginSiteCd = siteCd;
					//관리자인 경우
					if(SessionUtil.isAdmin(loginVO)){
						loginSiteCd = CodeConfig.SYS_SITE_CD;
					}
					String sessionKey = EncryptUtil.encryptMsg(loginSiteCd + "#" + loginVO.getLoginId());
					EgovHttpSessionBindingListener listener = EgovHttpSessionBindingListener.getInstance();
					request.getSession().setAttribute(sessionKey, listener);
				} else {
					SessionBindingListener listener = SessionBindingListener.getInstance();
					request.getSession().setAttribute(loginVO.getLoginId(), listener);
				}					
				return web.returnLogin();
			}
		}
		
		//VO 생성
		loginVO = new LoginVO();			
		//조회결과 MODEL ADD
		model.addAttribute("loginVO", loginVO);			
		return web.returnView(VIEW_PATH, "/login", "LOGIN");
	}
	
	/**
	 * 로그인처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/loginAction.do", method = RequestMethod.POST)
	public String loginAction(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search
							, @RequestParam(value = "returnLogin", defaultValue="") String returnLogin
							, LoginVO loginVO, BindingResult loginBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("returnLogin", returnLogin);
		
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//RSA 유효성 검증을 위해 객체 생성 후 진행 (에러시 리턴은 파라미터로 넘어온 객체를 리턴)
		if(loginVO == null) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		//2차인증검증
		if(web.isCertUse()) {
			boolean certResult = certificationService.checkEncryptCertKey(request, loginVO.getLoginCertKey());
			if(!certResult) {
				model.addAttribute("redirectUrl", "/login.do");
				return web.returnError();
			}
		}
		
		String loginId = rsaUtil.decryptRsa(privateKey, loginVO.getLoginId());
		String loginPw = rsaUtil.decryptRsa(privateKey, loginVO.getLoginPw());
		
		LoginVO validLoginVO = loginVO;
		validLoginVO.setLoginId(loginId);
		validLoginVO.setLoginPw(loginPw);
		
		//유효성 검증
		beanValidator.validate(validLoginVO, loginBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (loginBindingResult.hasErrors()) {
			model.addAttribute("loginVO", loginVO);
			return web.returnView(VIEW_PATH, "/login", "LOGIN");
		}
		
		//로그인 결과
		boolean result = false; //로그인 결과
		
		//사용자 체크
		LoginVO loginInfo = loginService.selectLoginUser(loginId, loginPw);
		if("Y".equals(loginInfo.getLoginSuccess())) {
			result = true;
			SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, loginInfo);
		}
		
		//성공시
		if(result) {
			
			//로그 등록
			loginService.insertLoginLog(loginInfo);
			
			//중복로그인 리스너
			if(!web.isMultiLogin()) {
				String siteCd = "";
				//관리자인 경우
				if(SessionUtil.isAdmin(loginInfo)){
					siteCd = CodeConfig.SYS_SITE_CD;
				} else {
					SiteVO siteVO = siteService.selectSiteByDomain(RequestUtil.getDomain());
					if(siteVO != null && !StringUtil.isNull(siteVO.getSiteCd())) {
						siteCd = siteVO.getSiteCd();
					} else {
						siteCd = CodeConfig.MAIN_SITE_CD;
					}
				}
				String sessionKey = EncryptUtil.encryptMsg(siteCd + "#" + loginInfo.getLoginId());
				EgovHttpSessionBindingListener listener = EgovHttpSessionBindingListener.getInstance();
				request.getSession().setAttribute(sessionKey, listener);
			} else {
				SessionBindingListener listener = SessionBindingListener.getInstance();
				request.getSession().setAttribute(loginInfo.getLoginId(), listener);
			}
			
			//Redirect URL이 있는지 확인
			if(StringUtil.isNull(returnLogin)) {
				returnLogin = "/";
			}
			
			model.addAttribute("returnLogin", returnLogin);
			return web.returnLogin();
			
		} else { //실패시			
			model.addAttribute("redirectUrl", "/login.do");
			model.addAttribute("resultMsg", messageUtil.getMessage("errors.login"));
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("returnLogin", returnLogin);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}		
	}
	
	/**
	 * 사용자 로그아웃
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/logout.do")
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response){
		
		//현재 언어정보
		String language = web.getLanguage();
		
		request.getSession().removeAttribute("LOGIN_USER");
		request.getSession().invalidate(); 
		
		//로그아웃
		SessionUtil.logout(request);
		
		//SSO사용시
		if(!web.isSsoUse()) {
			ssoService.ssoLogout(request);
		}
		
		//세션 아웃시 언어가 초기화 되므로 재설정
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver != null) {
			localeResolver.setLocale(request, response, StringUtils.parseLocaleString(language));
		}
		
	    return web.redirect(model, "/");
	}
	
}
