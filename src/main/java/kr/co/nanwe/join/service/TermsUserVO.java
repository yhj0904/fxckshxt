package kr.co.nanwe.join.service;

import lombok.Data;

/**
 * @Class Name 		: TermsUserVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class TermsUserVO {
	
	/** 약관구분 */
	private String termsDvcd;
	/** 약관코드 */
	private String termsId;
	/** 사용자분류 */
	private String userType;
	/** 사용자아이디 */
	private String userId;
	/** 사용자명 */
	private String userNm;
	/** 동의여부 */
	private String agreeYn;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	
}
