package kr.co.nanwe.push.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: PushGrpMstVO
 * @Description 	: 그룹관리
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class PushGrpMstVO {
	/** 그룹코드 */
	private String grpCd;
	/** 그룹생성자 */
	private String grpCreUser;
	/** 그룹명 */
	private String grpNm;
	/** 그룹권한 */
	private String grpPer;
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
	/** 수정IP */
	private String modiDttm;
	/** 수정프로그램 */
	private String modiProg;
	/** 멤버 카운트 */
	private int memCnt;
	/** 멤버 목록 */
	private List<PushGrpMembVO> memList;
}
