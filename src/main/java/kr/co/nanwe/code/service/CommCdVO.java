package kr.co.nanwe.code.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: CommCdVO
 * @Description 	: 공통코드 VO
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class CommCdVO {
	
	/** 코드 */
	private String cd;
	/** 상위코드 */
	private String hiCd;
	/** 코드명 */
	private String cdNm;
	/** 코드약어명 */
	private String cdAbbrNm;
	/** 코드영문명 */
	private String cdEngNm;
	/** 코드영문약어명 */
	private String cdEngAbbrNm;
	/** 업무전자게시판코드 */
	private String workBbsCd;
	/** 업무전자게시판세부코드 */
	private String workBbsDetlCd;
	/** 편집허용여부 */
	private String editPermYn;
	/** 정렬순서 */
	private int sortOrd;
	/** 비고 */
	private String note;
	/** 사용여부 */
	private String useYn;
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
	
	/** 하위 코드 개수 */
	private int cdLevel;
	
	/** 하위 코드 개수 */
	private int childCnt;
	/** 하위 코드 목록 */
	private List<CommCdVO> list;
	
}
