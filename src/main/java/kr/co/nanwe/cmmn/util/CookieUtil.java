package kr.co.nanwe.cmmn.util;

import javax.servlet.http.Cookie;

/**
 * @Class Name 		: CookieUtil
 * @Description 	: 쿠키 관련 유틸 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class CookieUtil {
	
	public static void createCookie(String name, String value, int days, String path){
				
		Cookie c = new Cookie(name, value);
		
		//쿠키 지속시간 수정
		if(days > 1) {
			c.setMaxAge(60 * 60 * 24 * days);
		} else {
			c.setMaxAge(60 * 60 * 24);
		}
		
		if(!StringUtil.isNull(path)) {
			c.setPath(path);
		} else {
			c.setPath("/");
		}
		
		RequestUtil.getCurrentResponse().addCookie(c);
	};

	public static String getCookie(String name){
		Cookie[] cookies = RequestUtil.getCurrentRequest().getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i].getValue();
				}
			}
		}
		return null;
	};

	public static void removeCookie(String name){
		Cookie c = new Cookie(name, null);
		c.setMaxAge(0);
		RequestUtil.getCurrentResponse().addCookie(c);
	};
}
