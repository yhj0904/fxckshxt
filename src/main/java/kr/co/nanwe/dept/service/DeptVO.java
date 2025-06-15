package kr.co.nanwe.dept.service;

import lombok.Data;

/**
 * @Class Name 		: DeptVO
 * @Description 	: 부서
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class DeptVO {
	/** 부서코드 */
	private String deptCd;
	/** 상위부서코드 */
	private String hiDeptCd;

	/** 상위부서한글 */
	private String hiDeptNmKor;
	
	/** 부서명한글 */
	private String deptNmKor;
	/** 부서명영문 */
	private String deptNmEng;
	/** 학위명한글 */
	private String degrNmKor;
	/** 학위명영문 */
	private String degrNmEng;
	/** 단축부서명 */
	private String shtnDeptNm;
	/** 동일부서코드 */
	private String sameDeptCd;
	/** 전화번호 */
	private String telNo;
	/** 부서레벨 */
	private int deptLvl;
	/** 구부서코드 */
	private String oldDeptCd;
	/** 생성일자 */
	private String creDt;
	/** 폐기일자 */
	private String duseDt;
	/** 사용여부 */
	private String useYn;
	/** 정렬순서 */
	private int sortOrd;
	/** 비고 */
	private String note;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
	/** 부서구분 */
	private String deptDvcd;
	
	private int childCnt;
}
