package kr.co.nanwe.cmmn.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: SessionUtil
 * @Description 	: 세션관련 유틸클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class SessionUtil {
	
	public static final String LOGIN_SESSION_KEY = "LOGIN_USER"; //로그인 세션 키값
	
	/** 세션 값을 Set한다  */
	public static void setSessionVal(String key, Object value) {
		if(value != null) {
			RequestContextHolder.getRequestAttributes().setAttribute(key, value, RequestAttributes.SCOPE_SESSION);
		}
	}
	
	/** 세션 값을 Get한다  */
	public static Object getSessionVal(String key) {
		Object value = (Object)RequestContextHolder.getRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_SESSION);
		if(value != null) {
			return value;
		} else {
			return null;
		}
	}
	
	/** 로그인정보를 가져온다  */
	public static LoginVO getLoginUser() {
		Object value = (Object)RequestContextHolder.getRequestAttributes().getAttribute(LOGIN_SESSION_KEY, RequestAttributes.SCOPE_SESSION);
		if(value != null) {			
			LoginVO loginVO = (LoginVO) value;
			return loginVO;		
		} else {
			return null;
		}
	}
	
	/** 관리자인지 체크  */
	public static boolean isAdmin() {
		Object value = (Object)RequestContextHolder.getRequestAttributes().getAttribute(LOGIN_SESSION_KEY, RequestAttributes.SCOPE_SESSION);
		if(value != null) {			
			LoginVO loginVO = (LoginVO) value;
			if(SessionUtil.isAdmin(loginVO)){
				return true;
			}
		}
		return false;
	}
	
	/** 관리자인지 체크  */
	public static boolean isAdmin(LoginVO loginVO) {
		if(loginVO == null) {
			return false;
		}
		if(CodeConfig.COM_USER_CODE.equals(loginVO.getUserDvcd()) && CodeConfig.COM_USER_CODE.equals(loginVO.getWorkDvcd()) && CodeConfig.SYS_USER_CODE.equals(loginVO.getStatDvcd())){
			return true;
		}
		return false;
	}
	
	/** 공통 사용자인지 체크  */
	public static boolean isComUser() {
		Object value = (Object)RequestContextHolder.getRequestAttributes().getAttribute(LOGIN_SESSION_KEY, RequestAttributes.SCOPE_SESSION);
		if(value != null) {			
			LoginVO loginVO = (LoginVO) value;
			if(loginVO != null){
				if(CodeConfig.COM_USER_CODE.equals(loginVO.getUserDvcd()) && CodeConfig.COM_USER_CODE.equals(loginVO.getWorkDvcd())){
					return true;
				}
			}
		}
		return false;
	}
	
	/** 로그아웃 한다  */
	public static void logout(HttpServletRequest request) {
		RequestContextHolder.getRequestAttributes().removeAttribute(LOGIN_SESSION_KEY, RequestAttributes.SCOPE_SESSION);
	    request.getSession().invalidate();
	}
}
