package kr.co.nanwe.cmmn.validator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import kr.co.nanwe.cmmn.util.StringUtil;

/**
 * @Class Name 		: ComGenericValidator
 * @Description 	: 커스텀 validator 유효성 검증
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@SuppressWarnings("serial")
public class ComGenericValidator implements Serializable {

	/** 아이디 유효성 검사 */
	public static boolean isValidId(String value) {

		// NULL 또는 공백일 경우 false
		if (StringUtil.isNull(value)) {
			return true;
		}

		return Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,19}$", value);

	}

	/** 비밀번호 유효성 검사 */
	public static boolean isValidPw(String value) {
		
		// NULL 또는 공백일 경우 false
		if (StringUtil.isNull(value)) {
			return true;
		}
		
		return Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$", value); 
	}
	
	/** 날짜 유효성 검사 */
	public static boolean isValidDate(String value) {
				
		//NULL 또는 공백일 경우 true
		if(StringUtil.isNull(value)){
			return true;
		}
	
		if(value.length() < 8) {
			return false;
		}
		
		value = value.replace(".", "");
		value = value.replace("/", "");
		value = value.replace("-", "");
		
		if (value.length() != 8){
			return false;
		}
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.setLenient(false);
			dateFormat.parse(value);
			return true;
		} catch (ParseException e) {
			return false;
		}

	}
	
	/** 글자수 유효성 검사 */
	public static boolean isValidLength(String value, int min, int max) {
		
		//NULL 또는 공백일 경우 true
		if(StringUtil.isNull(value)){
			return true;
		}
		
		int strLength = value.length();
		
		if(strLength >= min && strLength <= max) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/** 숫자인지 체크 */
	public static boolean isValidNum(String value) {

		if(StringUtil.isNull(value)) {
			return true;
		}
		
		return Pattern.matches("^[0-9]*$", value);
	}

	/** 최소숫자 체크 */
	public static boolean isValidMinNum(int value, int min) {
		if(value >= min) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 최대숫자 체크 */
	public static boolean isValidMaxNum(int value, int max) {
		if(value <= max) {
			return true;
		} else {
			return false;
		}
	}

	/** 아이피인지 체크 */
	public static boolean isValidIp(String value) {
		// NULL 또는 공백일 경우 false
		if (StringUtil.isNull(value)) {
			return true;
		}
		
		String regExp = "^([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.([01]?\\d?\\d|2[0-4]\\d|25[0-5])$";

		return Pattern.matches(regExp, value);
	}
}
