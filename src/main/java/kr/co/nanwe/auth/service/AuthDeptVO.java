package kr.co.nanwe.auth.service;

import lombok.Data;

/**
 * @Class Name 		: AuthDeptVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class AuthDeptVO {
	/** 권한코드 */
	private String authCd;
	/** 권한구분 */
	private String authDvcd;
	/** 부서코드 */
	private String deptCd;
	/** 부서명 */
	private String deptNm;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
}
