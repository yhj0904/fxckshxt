package kr.co.nanwe.push.service;

import lombok.Data;

/**
 * @Class Name 		: PushGrpMembVO
 * @Description 	: 그룹구성원
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class PushGrpMembVO {
	/** 그룹 관리 순 */
	private int grpSno;
	/** 그룹코드 */
	private String grpCd;
	/** 그룹 멤버 아이디 */
	private String grpMembId;
	/** 그룹 멤버 이름 */
	private String grpMembNm;
	/** 그룹 멤버 구분 */
	private String grpMembDv;
	/** 그룹 멤버 모바일 */
	private String grpMembMobile;
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
