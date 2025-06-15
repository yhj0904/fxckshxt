package kr.co.nanwe.cmmn.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtil {

	/**
	 * 아이디 마스킹
	 */
	public static String getMaskedId(String id) {
		String maskedId = ""; // 마스킹 이름
		String firstId = "";
		String lastId = "";
		int lastIdStartPoint;
		
		if (id.length() > 3) {
			firstId = id.substring(0, 3);
			lastIdStartPoint = id.indexOf(firstId);
			lastId = id.substring(lastIdStartPoint + 1, id.length());

			String makers = "";

			for (int i = 0; i < lastId.length(); i++) {
				makers += "*";
			}

			lastId = lastId.replace(lastId, makers);
			maskedId = firstId + lastId;
		} else if ( id.length() > 1 ) {
			firstId = id.substring(0, 1);
			lastIdStartPoint = id.indexOf(firstId);
			lastId = id.substring(lastIdStartPoint + 1, id.length());

			String makers = "";

			for (int i = 0; i < lastId.length(); i++) {
				makers += "*";
			}

			lastId = lastId.replace(lastId, makers);
			maskedId = firstId + lastId;
		} else {
			maskedId = "*";
		}
		return maskedId;
	}
	
	/**
	 * 이메일 주소 마스킹 처리
	 */
	public static String getMaskedEmail(String email) {
		/*
		 * 요구되는 메일 포맷 {userId}@domain.com
		 */
		String regex = "\\b(\\S+)+@(\\S+.\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(email);
		if (matcher.find()) {
			String id = matcher.group(1); // 마스킹 처리할 부분인 userId
			/*
			 * userId의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고, 세글자인 경우 뒤 두글자만 마스킹, 세글자 미만인 경우
			 * 모두 마스킹 처리
			 */
			int length = id.length();
			if (length < 3) {
				char[] c = new char[length];
				Arrays.fill(c, '*');
				return email.replace(id, String.valueOf(c));
			} else if (length == 3) {
				return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
			} else {
				return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
			}
		}
		return email;
	}

	/**
	 * 이름 마스킹 처리(성을 제외한 이름 마스킹 처리)
	 */
	public static String getMaskedName(String name) {
		String maskedName = ""; // 마스킹 이름
		String firstName = ""; // 성
		String lastName = ""; // 이름
		int lastNameStartPoint; // 이름 시작 포인터

		if (!name.equals("") || name != null) {
			if (name.length() > 1) {
				firstName = name.substring(0, 1);
				lastNameStartPoint = name.indexOf(firstName);
				lastName = name.substring(lastNameStartPoint + 1, name.length());

				String makers = "";

				for (int i = 0; i < lastName.length(); i++) {
					makers += "*";
				}

				lastName = lastName.replace(lastName, makers);
				maskedName = firstName + lastName;
			} else {
				maskedName = "*";
			}
		}

		return maskedName;
	}
	
	
	/**
	 * 휴대폰 번호 마스킹(010****1234 / 011***1234)
	 */
	public static String getMaskedPhone(String phoneNumber) {
		
		if(StringUtil.isNull(phoneNumber)) {
			return "";
		}
		
		String maskedPhoneNum = phoneNumber;
		
		// 공백제거
		maskedPhoneNum = maskedPhoneNum.replaceAll(" ", "");

		// '-'가 포함되어있으면 모두 삭제
		if (maskedPhoneNum.contains("-")) {
			maskedPhoneNum = maskedPhoneNum.replaceAll("[^0-9]", "");
		}
		
		// 11자리 또는 10자리가 되지 않으면 공백 ""
		if (maskedPhoneNum.length() > 11 || maskedPhoneNum.length() < 10) {
			maskedPhoneNum = "";
		} else {
			// 11자리 휴대폰 번호 마스킹 처리
			if (maskedPhoneNum.length() == 11) {
				String num1 = maskedPhoneNum.substring(0, 3);
				String num3 = maskedPhoneNum.substring(7);
				maskedPhoneNum = num1 + "-****-" + num3;
				// 10자리 휴대폰 번호 마스킹 처리
			} else if (maskedPhoneNum.length() == 10) {
				String num1 = maskedPhoneNum.substring(0, 3);
				String num3 = maskedPhoneNum.substring(6);
				maskedPhoneNum = num1 + "-***-" + num3;
			}
		}
		return maskedPhoneNum;
	}
	
	/**
	 * 전화번호 마스킹
	 */
	public static String getMaskedTel(String telNumber) {

		Pattern pattern = Pattern.compile("^(\\d{2,3})-?(\\d{1,2})\\d{2}-?\\d(\\d{3})$");

		if (telNumber == null || "".equals(telNumber)) {
			return "";
		}
		
		Matcher matcher = pattern.matcher(telNumber);

		if (matcher.find()) {
			return matcher.group(1) + "-" + matcher.group(2) + "**" + "-*" + matcher.group(3);
		} else {
			return "***";
		}
	}

}
