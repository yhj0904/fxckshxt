package kr.co.nanwe.login.web;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.CertificationService;
import kr.co.nanwe.login.service.EgovMultiLoginPreventor;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: RestLoginController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@RestController
public class RestLoginController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestLoginController.class);
		
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
	@Resource(name = "certificationService")
	private CertificationService certificationService;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	/**
	 * 로그인 체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/login/checkLogin.json")
	public Map<String, Object> checkLogin (HttpServletRequest request, HttpServletResponse response
										, @RequestParam(value = "loginId", defaultValue="") String loginId
										, @RequestParam(value = "loginPw", defaultValue="") String loginPw) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		String errCd = "";
		String errMsg = "";
		
		//2차 인증을 위한 정보
		String userId = "";
		String userPhone = "";
		String userEmail = "";
		
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			errCd = "SESSION";
			errMsg = messageUtil.getMessage("errors.login.session");
			map.put("result", result);
			map.put("errCd", errCd);
			map.put("errMsg", errMsg);
			return map;
		}
		
		//아이디 공백 체크
		loginId = rsaUtil.decryptRsa(privateKey, loginId);
		if(StringUtil.isNull(loginId)) {
			errCd = "ID";
			errMsg = messageUtil.getMessage("errors.login.id");
			map.put("result", result);
			map.put("errCd", errCd);
			map.put("errMsg", errMsg);
			return map;
		}
		
		//비밀번호 체크
		loginPw = rsaUtil.decryptRsa(privateKey, loginPw);
		if(StringUtil.isNull(loginPw)) {
			errCd = "PW";
			errMsg = messageUtil.getMessage("errors.login.pw");
			map.put("result", result);
			map.put("errCd", errCd);
			map.put("errMsg", errMsg);
			return map;
		}
		
		LoginVO loginInfo = loginService.selectLoginUser(loginId, loginPw);
		if("Y".equals(loginInfo.getLoginSuccess())) {
			result = true;
			userId = loginInfo.getLoginId();
			userPhone = loginInfo.getMbphNo();
			userEmail = loginInfo.getEmail();
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
				if(EgovMultiLoginPreventor.findBySessionKey(sessionKey)) {
					errCd = "IS_LOGIN";
					errMsg = messageUtil.getMessage("message.confirm.isLogin");
				}
			}
		} else {
			errCd = loginInfo.getLoginErrCode();
			if("ERROR_06".equals(errCd)) { //비밀번호 5회오류인 경우
				errMsg = messageUtil.getMessage("errors.login.pwcount");
			} else {
				errMsg = messageUtil.getMessage("errors.login");
			}				
			//로그기록
			loginService.insertLoginLog(loginInfo);
		}
		
		map.put("result", result);
		map.put("errCd", errCd);
		map.put("errMsg", errMsg);
		
		//로그인이 성공이며 2차인증 사용하는 경우
		if(result && web.isCertUse()) {
			Map<String, Object> certification = certificationService.initCertification(request, response, web.getCertType(), userId, userPhone, userEmail);
			map.put("certification", certification);
		}		
		return map;
	}
	
	/**
	 * 관리자 로그인 체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/login/checkLogin.json")
	public Map<String, Object> checkSysLogin (HttpServletRequest request, HttpServletResponse response
										, @RequestParam(value = "loginId", defaultValue="") String loginId
										, @RequestParam(value = "loginPw", defaultValue="") String loginPw) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		String errCd = "";
		String errMsg = "";
		
		//2차 인증을 위한 정보
		String userId = "";
		String userPhone = "";
		String userEmail = "";
		
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			errCd = "SESSION";
			errMsg = messageUtil.getMessage("errors.login.session");
			map.put("result", result);
			map.put("errCd", errCd);
			map.put("errMsg", errMsg);
			return map;
		}
		
		//아이디 공백 체크
		loginId = rsaUtil.decryptRsa(privateKey, loginId);
		if(StringUtil.isNull(loginId)) {
			errCd = "ID";
			errMsg = messageUtil.getMessage("errors.login.id");
			map.put("result", result);
			map.put("errCd", errCd);
			map.put("errMsg", errMsg);
			return map;
		}
		
		//비밀번호 체크
		loginPw = rsaUtil.decryptRsa(privateKey, loginPw);
		if(StringUtil.isNull(loginPw)) {
			errCd = "PW";
			errMsg = messageUtil.getMessage("errors.login.pw");
			map.put("result", result);
			map.put("errCd", errCd);
			map.put("errMsg", errMsg);
			return map;
		}
		
		//사용자 체크
		LoginVO loginInfo = loginService.selectLoginSysAdmUser(loginId, loginPw);
		if("Y".equals(loginInfo.getLoginSuccess())) {
			result = true;
			userId = loginInfo.getLoginId();
			userPhone = loginInfo.getMbphNo();
			userEmail = loginInfo.getEmail();			
			if(!web.isMultiLogin()) {
				String siteCd = CodeConfig.SYS_SITE_CD;
				String sessionKey = EncryptUtil.encryptMsg(siteCd + "#" + loginInfo.getLoginId());
				if(EgovMultiLoginPreventor.findBySessionKey(sessionKey)) {
					errCd = "IS_LOGIN";
					errMsg = messageUtil.getMessage("message.confirm.isLogin");
				}
			}
		} else {
			errCd = loginInfo.getLoginErrCode();
			if("ERROR_06".equals(errCd)) { //비밀번호 5회오류인 경우
				errMsg = messageUtil.getMessage("errors.login.pwcount");
			} else {
				errMsg = messageUtil.getMessage("errors.login.denied");
			}
			//로그기록
			loginService.insertLoginLog(loginInfo);
		}
		
		map.put("result", result);
		map.put("errCd", errCd);
		map.put("errMsg", errMsg);
		
		//로그인이 성공이며 2차인증 사용하는 경우
		if(result && web.isCertUse()) {
			Map<String, Object> certification = certificationService.initCertification(request, response, web.getCertType(), userId, userPhone, userEmail);
			map.put("certification", certification);
		}		
		return map;
	}
	
	/**
	 * 2차인증 갱신
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/login/reCertification.json")
	public Map<String, Object> reCertification (HttpServletRequest request, HttpServletResponse response) {
		return certificationService.refreshCertification(request, response, web.getCertType());
	}
	
	/**
	 * 2차인증 체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/login/checkCertification.json")
	public Map<String, Object> checkCertification (HttpServletRequest request, @RequestParam(value = "certKey", defaultValue="") String certKey) {
		return certificationService.checkCertification(request, web.getCertType(), certKey);
	}
	
}
