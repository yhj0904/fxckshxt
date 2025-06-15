package kr.co.nanwe.login.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Class Name 		: SsoService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface SsoService {
	
	LoginVO getSsoLoginInfo(HttpServletRequest request);

	void ssoLogout(HttpServletRequest request);

}
