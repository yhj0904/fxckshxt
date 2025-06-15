package kr.co.nanwe.login.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Class Name 		: CertificationService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface CertificationService {
	
	/** 2차인증 초기화 */
	void resetCertification(HttpServletRequest request);

	/** 2차인증 */
	Map<String, Object> initCertification(HttpServletRequest request, HttpServletResponse response, String certType, String userId, String userPhone, String userEmail);

	/** 2차인증 갱신*/
	Map<String, Object> refreshCertification(HttpServletRequest request, HttpServletResponse response, String certType);

	/** 인증 체크*/
	Map<String, Object> checkCertification(HttpServletRequest request, String certType, String iCertKey);
	
	/** 파라미터 검증 */
	boolean checkEncryptCertKey(HttpServletRequest request, String encryptCertKey);

}
