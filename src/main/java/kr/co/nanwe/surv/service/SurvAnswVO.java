package kr.co.nanwe.surv.service;

import lombok.Data;

/**
 * @Class Name 		: SurvAnswVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SurvAnswVO {
	/** 설문ID */
	private String survId;
	/** 문항IDX */
	private int quesIdx;
	/** 항목IDX */
	private int itemIdx;
	/** 항목제목 */
	private String itemTitle;
	/** 항목설명 */
	private String itemMemo;
	/** 점수 */
	private int itemPoint;
	/** 기타여부 */
	private String itemEtc;
	/** 응답 */
	private String answContent;
	/** 점수 */
	private int answPoint;
	/**  */
	private String inptId;
	/**  */
	private String inptNm;
	/**  */
	private String inptIp;
	/**  */
	private String inptDttm;
}
