package kr.co.nanwe.push.service;

import lombok.Data;

/**
 * @Class Name 		: PushAppUserVO
 * @Description 	: 앱사용자
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class PushAppUserVO {
	
	/** 사용자 아이디 */
	private String userId;
	/** 사용자 소속 */
	private String deptCd;
	/** 사용자 소속명 */
	private String deptNm;
	/** 사용자 성명 */
	private String userNm;
	/** 사용자 암호 */
	private String userPw;
	/** 사용자 구분 */
	private String userDv;
	/** 관리자 구분 */
	private String adminDv;
	/** 디바이스 구분 */
	private String deviceDv;
	/** 디바이스 아이디 */
	private String deviceId;
	/** 사용자 모바일 */
	private String userMobile;
	/** 비고 */
	private String note;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 입력프로그램 */
	private String inptProg;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
	/** 수정프로그램 */
	private String modiProg;
}
