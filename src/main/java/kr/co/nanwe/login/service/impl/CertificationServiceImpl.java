package kr.co.nanwe.login.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.CryptoUtil;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.MailUtil;
import kr.co.nanwe.cmmn.util.MaskingUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SendSmsUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.CertificationService;
import kr.co.nanwe.login.service.CertificationVO;

/**
 * @Class Name 		: CertificationServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("certificationService")
public class CertificationServiceImpl extends EgovAbstractServiceImpl implements CertificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificationServiceImpl.class);
	
	private static final String CERT_KEY = "CERT_KEY";
	private static final long PHONE_CERT_SECOND = 3 * 60 * 1000;
	private static final long EMAIL_CERT_SECOND = 5 * 60 * 1000;
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
    @Resource(name="certificationMapper")
    private CertificationMapper certificationMapper;
    
    @Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;
	
	@Resource(name = "mailUtil")
	private MailUtil mailUtil;
	
	@Resource(name = "sendSmsUtil")
	private SendSmsUtil sendSmsUtil;

	@Override
	@MethodDescription("2차인증 초기화")
	public void resetCertification(HttpServletRequest request) {
		if(request.getSession().getAttribute(CERT_KEY) != null) {
			request.getSession().removeAttribute(CERT_KEY);
		}
	}
	
	@Override
	@MethodDescription("2차인증 init")
	public Map<String, Object> initCertification(HttpServletRequest request, HttpServletResponse response, String certType, String userId, String userPhone, String userEmail) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		String errMsg = messageUtil.getMessage("certification.error.server");
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		
		//2차인증 방식
		if(certType == null) {
			return returnMap;
		}
		
		//기존 키가 있는 경우 삭제
		if(request.getSession().getAttribute(CERT_KEY) != null) {
			request.getSession().removeAttribute(CERT_KEY);
		}
		
		if("PHONE".equals(certType.toUpperCase())) {
		
			errMsg = messageUtil.getMessage("certification.error.phone"); //유효하지 않은 핸드폰번호
			returnMap.put("errMsg", errMsg);
			
			if(StringUtil.isNull(userPhone)) {				
				return returnMap;
			}
			
			//공백제거
			userPhone = userPhone.replaceAll(" " , "").replaceAll("\\p{Z}", "");
			
			//정규표현식 체크
			String regExp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
			if(!userPhone.matches(regExp)){			
				return returnMap;
			}
			//핸드폰번호 하이픈, 닷 제거
			userPhone = userPhone.replaceAll("[^0-9]", "");
			
			returnMap = sendSmsCertification(request, userId, userPhone);
			
		} else if("EMAIL".equals(certType.toUpperCase())) {			
			
			errMsg = messageUtil.getMessage("certification.error.email"); //유효하지 않은 이메일
			returnMap.put("errMsg", errMsg);
			
			if(StringUtil.isNull(userEmail)) {				
				return returnMap;
			}
			
			//공백제거
			userEmail = userEmail.replaceAll(" " , "").replaceAll("\\p{Z}", "");
			
			//이메일 체크			
			if(!EmailValidator.getInstance().isValid(userEmail)){			
				return returnMap;
			}
			
			returnMap = sendEmailCertification(request, userId, userEmail);
			
		}
		
		return returnMap;
	}

	@Override
	@MethodDescription("2차인증 갱신")
	public Map<String, Object> refreshCertification(HttpServletRequest request, HttpServletResponse response, String certType) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		
		String errMsg = messageUtil.getMessage("certification.error.key"); //유효하지않은 키
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		
		//기존 키가 없으면 갱신 안됨
		if(request.getSession().getAttribute(CERT_KEY) == null) {
			return returnMap;
		}
		
		String certValue = (String) request.getSession().getAttribute(CERT_KEY);	
		try {			
			certValue = cryptoUtil.decrypt(certValue);			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			certValue = null;
			LOGGER.error(e.getMessage());
		}
		
		if(certValue == null) {
			return returnMap;
		}
		
		String[] certValueArr = certValue.split(",");
		if(certValueArr.length != 5) {
			request.getSession().removeAttribute(CERT_KEY);
			return returnMap;
		}
		
		String userId = certValueArr[1];
		String userValue = certValueArr[2];
		String sessionDttm = certValueArr[4];
		
		//2차인증 방식
		if(certType == null) {
			return returnMap;
		}
		
		long certTime = 0;		
		if("PHONE".equals(certType.toUpperCase())) {
			certTime = PHONE_CERT_SECOND;
		} else if("EMAIL".equals(certType.toUpperCase())) {	
			certTime = EMAIL_CERT_SECOND;
		}
		
		//시간비교
		String time = DateUtil.getDate("yyyyMMddHHmmss");
		long diffTime = DateUtil.stringToDate(time, "yyyyMMddHHmmss").getTime() - DateUtil.stringToDate(sessionDttm, "yyyyMMddHHmmss").getTime();
		
		//인증시간을 넘기면
		if(diffTime > certTime) {
			returnMap.put("errMsg", messageUtil.getMessage("certification.error.time"));  //인증시간 만료
			return returnMap;
		}
		
		if("PHONE".equals(certType.toUpperCase())) {
			returnMap = sendSmsCertification(request, userId, userValue);
		} else if("EMAIL".equals(certType.toUpperCase())) {	
			returnMap = sendEmailCertification(request, userId, userValue);
		}
		
		return returnMap;
	}

	@Override
	@MethodDescription("2차인증 체크")
	public Map<String, Object> checkCertification(HttpServletRequest request, String certType, String iCertKey) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		
		String errMsg = messageUtil.getMessage("certification.error.key"); //유효하지않은 키
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		
		//기존 키가 없으면 return
		if(request.getSession().getAttribute(CERT_KEY) == null) {
			return returnMap;
		}
		
		String sessionCertValue = (String) request.getSession().getAttribute(CERT_KEY);	
		String certValue = (String) request.getSession().getAttribute(CERT_KEY);	
		try {			
			certValue = cryptoUtil.decrypt(certValue);			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			certValue = null;
			LOGGER.error(e.getMessage());
		}
		
		if(certValue == null) {
			return returnMap;
		}
		
		String[] certValueArr = certValue.split(",");
		if(certValueArr.length != 5) {
			request.getSession().removeAttribute(CERT_KEY);
			return returnMap;
		}
		
		String certUid = certValueArr[0];
		String certKey = certValueArr[3];
		String sessionDttm = certValueArr[4];
		
		//2차인증 방식
		if(certType == null) {
			return returnMap;
		}
		
		long certTime = 0;		
		if("PHONE".equals(certType.toUpperCase())) {
			certTime = PHONE_CERT_SECOND;
		} else if("EMAIL".equals(certType.toUpperCase())) {	
			certTime = EMAIL_CERT_SECOND;
		}
		
		//시간비교
		String time = DateUtil.getDate("yyyyMMddHHmmss");
		long diffTime = DateUtil.stringToDate(time, "yyyyMMddHHmmss").getTime() - DateUtil.stringToDate(sessionDttm, "yyyyMMddHHmmss").getTime();
		
		//인증시간을 넘으면
		if(diffTime > certTime) {
			returnMap.put("errMsg", messageUtil.getMessage("certification.error.time"));  //인증시간 만료
			return returnMap;
		}
		
		if(certKey.equals(StringUtil.isNullToString(iCertKey))) {			
			//인증성공 처리
			CertificationVO certificationVO = new CertificationVO();
			certificationVO.setCertUid(certUid);
			certificationVO.setCertKey(certKey);
			certificationVO.setCertResult("Y"); //초기값 
			certificationMapper.updateCertification(certificationVO);
			
			//암호화 후 파라미터로 전달
			String encryptCertKey = EncryptUtil.encryptMsg(sessionCertValue);
			returnMap.put("encryptCertKey", encryptCertKey);
			result = true;
			errMsg = "";						
		} else {
			errMsg = messageUtil.getMessage("certification.error.correct");  //인증번호 불일치
		}
		
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		
		return returnMap;
	}

	@Override
	@MethodDescription("2차인증 키체크")
	public boolean checkEncryptCertKey(HttpServletRequest request, String encryptCertKey) {
		
		//기존 키가 없으면 return
		if(request.getSession().getAttribute(CERT_KEY) == null) {
			return false;
		}
		
		if(StringUtil.isNull(encryptCertKey)) {
			request.getSession().removeAttribute(CERT_KEY);
			return false;
		}
		
		String certValue = (String) request.getSession().getAttribute(CERT_KEY);
		request.getSession().removeAttribute(CERT_KEY);
		
		//세션 키와 비교
		if(encryptCertKey.equals(EncryptUtil.encryptMsg(certValue))) {
			return true;
		}
		
		return false;
	}

	//SNS 인증 발송
	private Map<String, Object> sendSmsCertification(HttpServletRequest request, String userId, String userPhone){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		String errMsg = "";
		
		//문자발송전 시간제한 체크
		boolean preventSendMessage = false;
		
		//문자발송 로그 기록
		CertificationVO certificationVO = new CertificationVO();
		certificationVO.setCertType("PHONE");
		certificationVO.setCertTrgt(userPhone);
		certificationVO.setInptId(userId);
		certificationVO.setInptIp(ClientUtil.getUserIp());
		
		int preventSecond = certificationMapper.selectCertificationSecond(certificationVO, PHONE_CERT_SECOND / 1000); 
		if(preventSecond == 0) {
			preventSendMessage = true;
		}
		
		if(preventSendMessage) {
			
			//문자내용
			String certKey = "";
			SecureRandom random = new SecureRandom();
			for (int i = 0; i < 6; i++) {
				certKey += random.nextInt(10);
			}
			String message = messageUtil.getMessage("certification.input.key", new String[] {certKey});
			
			//문자발송 체크
			boolean messageResult = sendSmsUtil.sendSms(message, userPhone);
			
			if(messageResult) {
				
				//문자발송 로그 기록
				certificationVO.setCertKey(certKey);
				certificationVO.setCertResult("N"); //초기값 
				certificationMapper.insertCertification(certificationVO);
				
				//세션에 값 키 세팅
				String sessionDttm = DateUtil.getDate("yyyyMMddHHmmss");
				String certValue = StringUtil.isNullToString(certificationVO.getCertUid()) + "," + userId + "," + userPhone + "," + certKey + "," + sessionDttm;
				try {
					certValue = cryptoUtil.encrypt(certValue);
				} catch (UnsupportedEncodingException | GeneralSecurityException e) {
					certValue = null;
					LOGGER.error(e.getMessage());
				}
				
				if(certValue != null) {
					
					request.getSession().setAttribute(CERT_KEY, certValue);
					
					//TODO 삭제해야됨 실제 메세지는 문자로 발송 테스트용임
					returnMap.put("message", message);
					
					//사용자에게 보여줄 메시지
					String maskingPhone = MaskingUtil.getMaskedPhone(userPhone);
					String displayMsg = messageUtil.getMessage("certification.display.phone", new String[] {maskingPhone});
					returnMap.put("displayMsg", displayMsg);
					
					//초시간
					returnMap.put("second", PHONE_CERT_SECOND);
					
					result = true;
				} else {
					errMsg = messageUtil.getMessage("certification.error.send.phone"); //문자발송 오류
				}
			} else {
				errMsg = messageUtil.getMessage("certification.error.send.phone"); //문자발송 오류
			}
		} else {
			errMsg = messageUtil.getMessage("certification.error.count", new String[] {Integer.toString(preventSecond)}); //문자발송 제한
		}
		
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		
		return returnMap;
	}

	//이메일 인증발송
	private Map<String, Object> sendEmailCertification(HttpServletRequest request, String userId, String userEmail) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		String errMsg = "";
		
		//문자발송전 시간제한 체크
		boolean preventSendMessage = false;
		
		//문자발송 로그 기록
		CertificationVO certificationVO = new CertificationVO();
		certificationVO.setCertType("EMAIL");
		certificationVO.setCertTrgt(userEmail);
		certificationVO.setInptId(userId);
		certificationVO.setInptIp(ClientUtil.getUserIp());
		
		int preventSecond = certificationMapper.selectCertificationSecond(certificationVO, EMAIL_CERT_SECOND / 1000); 
		if(preventSecond == 0) {
			preventSendMessage = true;
		}
		
		if(preventSendMessage) {
			
			//이메일 내용
			String certKey = "";
			SecureRandom random = new SecureRandom();
			for (int i = 0; i < 6; i++) {
				certKey += random.nextInt(10);
			}
			String message = messageUtil.getMessage("certification.input.key", new String[] {certKey});
			
			//이메일 발송 체크
			boolean messageResult = mailUtil.sendMail(userEmail, messageUtil.getMessage("certification.title.email"), message);
			
			if(messageResult) {
				
				//이메일발송 로그 기록
				certificationVO.setCertKey(certKey);
				certificationVO.setCertResult("N"); //초기값 
				certificationMapper.insertCertification(certificationVO);
				
				//세션에 값 키 세팅
				String sessionDttm = DateUtil.getDate("yyyyMMddHHmmss");
				String certValue = StringUtil.isNullToString(certificationVO.getCertUid()) + "," + userId + "," + userEmail + "," + certKey + "," + sessionDttm;
				try {
					certValue = cryptoUtil.encrypt(certValue);
				} catch (UnsupportedEncodingException | GeneralSecurityException e) {
					certValue = null;
					LOGGER.error(e.getMessage());
				}
				
				if(certValue != null) {
					
					request.getSession().setAttribute(CERT_KEY, certValue);
					
					//사용자에게 보여줄 메시지
					String maskingEmail = MaskingUtil.getMaskedEmail(userEmail);
					String displayMsg = messageUtil.getMessage("certification.display.email", new String[] {maskingEmail});
					returnMap.put("displayMsg", displayMsg);
					
					//초시간
					returnMap.put("second", EMAIL_CERT_SECOND);
					
					result = true;
				} else {
					errMsg = messageUtil.getMessage("certification.error.send.email"); //이메일발송 오류
				}
			} else {
				errMsg = messageUtil.getMessage("certification.error.send.email"); //이메일발송 오류
			}
		} else {
			errMsg = messageUtil.getMessage("certification.error.count", new String[] {Integer.toString(preventSecond)}); //이메일발송 제한
		}
		
		returnMap.put("result", result);
		returnMap.put("errMsg", errMsg);
		
		return returnMap;
	}
	
}
