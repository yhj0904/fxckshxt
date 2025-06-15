package kr.co.nanwe.login.service;

import lombok.Data;

/**
 * @Class Name 		: CertificationVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class CertificationVO {
	
	/** UID */
	private String certUid;
	/** 인증유형 */
	private String certType;
	/** 인증키 */
	private String certKey;
	/** 인증결과 */
	private String certResult;
	/** 인증대상 */
	private String certTrgt;
	/** 인증결과시간 */
	private String certDttm;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	
}