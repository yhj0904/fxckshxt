package kr.co.nanwe.auth.service;

import lombok.Data;

/**
 * @Class Name 		: AuthMapVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class AuthMapVO {
	/** 권한코드 */
	private String authCd;
	/** 권한구분 */
	private String authDvcd;
	/** 사용자분류 */
	private String userDvcd;
	/** 사용자분류명 */
	private String userDvnm;
	/** 신분권한 */
	private String workDvcd;
	/** 신분권한명 */
	private String workDvnm;
	/** 상태코드 */
	private String statDvcd;
	/** 상태명 */
	private String statDvnm;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
}
