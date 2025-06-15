package kr.co.nanwe.user.web;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.external.service.ExternalService;
import kr.co.nanwe.login.service.CertificationService;
import kr.co.nanwe.user.service.UserService;

/**
 * @Class Name 		: FindUserController
 * @Description 	: 사용자 찾기 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/find")
@Controller
public class FindUserController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(FindUserController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/find";
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** Service */
	@Resource(name = "externalService")
	private ExternalService externalService;
	
	/** Service */
	@Resource(name = "certificationService")
	private CertificationService certificationService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;

	
	/**
	 * 아이디 찾기
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/userId.do")
	public String userId(Model model, HttpServletRequest request){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		
		//아이디 비밀번호 찾기 방식
		String findType = web.getServerProp("server.find.type");
		if(findType != null) {			
			returnMap.put("findType", findType);
			result = true;
		}
		returnMap.put("result", result);
		
		model.addAllAttributes(returnMap);
		
		return web.returnView(VIEW_PATH, "/find_id", "EMPTY");
	}
	
	/**
	 * 아이디 찾기 결과
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/userIdResult.do")
	public String userIdResult(Model model, HttpServletRequest request, @RequestParam Map<String, Object> param){
		model.addAttribute("param" , param);
		Map<String, Object> result =  userService.findUserId(param);
		model.addAttribute("result" , result);
		return web.returnView(VIEW_PATH, "/find_id_result", "EMPTY");
	}
	
	/**
	 * 사용자 아이디 찾기
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sendUserId.json")
	@ResponseBody
	public boolean sendUserId (HttpServletRequest request, @RequestBody Map<String, Object> param){
		return userService.sendFindUserId(param);
	}
	
	/**
	 * 비밀번호 찾기
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/userPw.do")
	public String userPw(Model model, HttpServletRequest request){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		
		//아이디 비밀번호 찾기 방식
		String findType = web.getServerProp("server.find.type");
		if(findType != null) {			
			returnMap.put("findType", findType);
			result = true;
		}
		returnMap.put("result", result);
		
		model.addAllAttributes(returnMap);
		
		//2차인증 초기화
		certificationService.resetCertification(request);
		
		return web.returnView(VIEW_PATH, "/find_pw", "EMPTY");
	}
	
	/**
	 * 인증번호 발송
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/findUserPwInfo.json")
	@ResponseBody
	public Map<String, Object> findUserPwInfo (HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> param){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> findUserInfo =  userService.findUserPw(param);
		boolean result = (boolean) findUserInfo.get("result");
		if(result) {			
			String sendType = (String) findUserInfo.get("sendType");
			String userId = (String) findUserInfo.get("id");
			String userPhone = (String) findUserInfo.get("phone");
			String userEmail = (String) findUserInfo.get("email");
			
			//정보가 있는 경우 2차인증 진행
			Map<String, Object> certification = certificationService.initCertification(request, response, sendType, userId, userPhone, userEmail);
			returnMap.put("certification", certification);
		}
		returnMap.put("result", result);
		return returnMap;
	}
	
	/**
	 * 2차인증 갱신
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/reCertification.json")
	@ResponseBody
	public Map<String, Object> reCertification (HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> param) {
		if(param.get("sendType") == null) {
			return null;
		}
		String sendType = (String) param.get("sendType");
		Map<String, Object> findUserInfo =  userService.findUserPw(param);
		boolean result = (boolean) findUserInfo.get("result");
		if(result) {
			return certificationService.refreshCertification(request, response, sendType);
		}
		return null;
	}
	
	/**
	 * 2차인증 체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/checkCertification.json")
	@ResponseBody
	public Map<String, Object> checkCertification (HttpServletRequest request, @RequestBody Map<String, Object> param) {
		if(param.get("sendType") == null || param.get("sendType") == null) {
			return null;
		}
		String sendType = (String) param.get("sendType");
		String certKey = (String) param.get("certKey");
		Map<String, Object> findUserInfo =  userService.findUserPw(param);
		boolean result = (boolean) findUserInfo.get("result");
		if(result) {
			return certificationService.checkCertification(request, sendType, certKey);
		}
		return null;
	}
	
	/**
	 * 비밀번호 변경
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyUserPw.do")
	public String modifyUserPw(Model model, HttpServletRequest request, @RequestParam Map<String , Object> param){
		
		if(param.get("encryptCertKey") == null) {
			model.addAttribute("redirectUrl", "/find/userPw.do");
			return web.returnError();
		}
		String encryptCertKey = (String) param.get("encryptCertKey");
		
		boolean certResult = certificationService.checkEncryptCertKey(request, encryptCertKey);
		if(!certResult) {
			model.addAttribute("redirectUrl", "/find/userPw.do");
			return web.returnError();
		}
		Map<String, Object> findUserInfo =  userService.findUserPw(param);
		boolean result = (boolean) findUserInfo.get("result");
		if(result) {
			//세션에 아이디 세팅
			request.getSession().setAttribute("findUserType", findUserInfo.get("userType"));
			request.getSession().setAttribute("findUserId", findUserInfo.get("id"));
			
			model.addAttribute("userType", findUserInfo.get("userType"));
			model.addAttribute("userId", findUserInfo.get("id"));
			return web.returnView(VIEW_PATH, "/find_pw_modify", "EMPTY");
		} else {
			model.addAttribute("redirectUrl", "/find/userPw.do");
			return web.returnError();
		}
	}
	
	/**
	 * 비밀번호체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/checkPw.json")
	@ResponseBody
	public Map<String, Object> checkPw(Model model, HttpServletRequest request
							, @RequestParam(value="password", defaultValue="") String password
							, @RequestParam(value="passwordCheck", defaultValue="") String passwordCheck){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		String errMsg = "";
		if(request.getSession().getAttribute("findUserType") == null || request.getSession().getAttribute("findUserId") == null) {
			return null;
		}
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			return null;
		}
		
		String userId = (String) request.getSession().getAttribute("findUserId");
		password = rsaUtil.decryptRsa(privateKey, password);
		passwordCheck = rsaUtil.decryptRsa(privateKey, passwordCheck);
		
		if(StringUtil.isNull(password)) {
			errMsg = messageUtil.getMessage("errors.password");
		} else if(StringUtil.isNull(passwordCheck)) {
			errMsg = messageUtil.getMessage("errors.password.check");	
		} else if(!password.equals(passwordCheck)) {
			errMsg = messageUtil.getMessage("errors.password.notequal");			
		} else if(userId.equals(password)) {
			errMsg = messageUtil.getMessage("errors.password.same");			
		} else if(!Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$", password)) {
			errMsg = messageUtil.getMessage("errors.password");		
		} else {
			result = true;
		}
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		return returnMap;
	}
	
	/**
	 * 아이디 찾기 결과
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/userPwResult.do")
	public String userPwResult(Model model, HttpServletRequest request
								, @RequestParam(value="userType", defaultValue="") String userType
								, @RequestParam(value="userId", defaultValue="") String userId
								, @RequestParam(value="password", defaultValue="") String password
								, @RequestParam(value="passwordCheck", defaultValue="") String passwordCheck){
		
		if(request.getSession().getAttribute("findUserType") == null || request.getSession().getAttribute("findUserId") == null) {
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		String findUserType = (String) request.getSession().getAttribute("findUserType");
		String findUserId = (String) request.getSession().getAttribute("findUserId");
		if(!findUserType.equals(userType) || !findUserId.equals(userId)) {
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		
		request.getSession().removeAttribute("findUserType");
		request.getSession().removeAttribute("findUserId");
		
		//RSA 검증
		PrivateKey privateKey = rsaUtil.getPrivateKey(request);
		if (privateKey == null) {
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		
		password = rsaUtil.decryptRsa(privateKey, password);
		passwordCheck = rsaUtil.decryptRsa(privateKey, passwordCheck);
		
		boolean result = false;
		String errMsg = "";
		if(StringUtil.isNull(password)) {
			errMsg = messageUtil.getMessage("errors.password");
		} else if(StringUtil.isNull(passwordCheck)) {
			errMsg = messageUtil.getMessage("errors.password.check");	
		} else if(!password.equals(passwordCheck)) {
			errMsg = messageUtil.getMessage("errors.password.notequal");			
		} else if(userId.equals(password)) {
			errMsg = messageUtil.getMessage("errors.password.same");			
		} else if(!Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$", password)) {
			errMsg = messageUtil.getMessage("errors.password");
		} else {
			if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
				int updateResult = 0;
				if(web.isExternalUse() && userType.contentEquals(CodeConfig.EXTERANL_USER_CODE)) {
					updateResult = externalService.updateUserPassword(userId, password);
				} else {
					updateResult = userService.updateUserPassword(userId, password);
				}
				
				if(updateResult > 0) {
					result = true;
				}
			}
		}
		
		if(result) {
			return web.returnView(VIEW_PATH, "/find_pw_result", "EMPTY");
		} else {
			model.addAttribute("redirectUrl", "/find/userPw.do");
			model.addAttribute("resultMsg", errMsg);
			return web.returnError();
		}		
	}
}
