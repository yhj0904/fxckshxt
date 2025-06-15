package kr.co.nanwe.cmmn.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.egovframe.rte.fdl.idgnr.impl.Base64;

/**
 * @Class Name 		: TokenMngUtil
 * @Description 	: 토큰 처리 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class TokenMngUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenMngUtil.class);
	
	private static final String TOKEN_KEY = "TOKEN_KEY";

	/**
	 * 로직처리를 위해 세션과 request에 Token 생성
	 * 
	 * @param request
	 */
	public static void saveToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		long systemTime = System.currentTimeMillis();
		byte[] time = Long.toString(systemTime).getBytes();
		byte[] id = session.getId().getBytes();

		try {
			MessageDigest SHA = MessageDigest.getInstance("SHA-256");
			SHA.update(id);
			SHA.update(time);

			String token = Base64.encode(SHA.digest());
			request.setAttribute(TOKEN_KEY, token);
			session.setAttribute(TOKEN_KEY, token);

		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
			resetToken(request);
			return;
		}
	}

	/**
	 * 로직처리 이후 중복방지를 위해 세션의 Token 초기화
	 * 
	 * @param request
	 */
	public static void resetToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.removeAttribute(TOKEN_KEY);
	}

	/**
	 * 세션과 request의 Token이 동일한지 비교
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isTokenValid(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String requestToken = request.getParameter(TOKEN_KEY);
		String sessionToken = (String) session.getAttribute(TOKEN_KEY);

		if (requestToken == null || sessionToken == null) {
			return false;
		} else {
			return requestToken.equals(sessionToken);
		}
	}
}