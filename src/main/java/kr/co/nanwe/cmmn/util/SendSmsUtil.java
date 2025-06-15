package kr.co.nanwe.cmmn.util;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * @Class Name 		: SendSmsUtil
 * @Description 	: 문자발송 관련 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("sendSmsUtil")
public class SendSmsUtil {

	@Resource(name = "smsProp")
	private Properties smsProp;

	//TODO 문자발송 API
	public boolean sendSms(String message, String phoneNum) {
		
		boolean result = true;
		
		if(StringUtil.isNull(message) || StringUtil.isNull(phoneNum)) {
			return false;
		}
		
		String receivePhone = phoneNum;
		
		//공백제거
		receivePhone = receivePhone.replaceAll(" " , "").replaceAll("\\p{Z}", "");
		receivePhone = receivePhone.replaceAll("[^0-9]", "");
		
		///정규표현식 체크
		String regExp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
		if(!receivePhone.matches(regExp)){			
			return false;
		}
		
		return result;
	}
}
