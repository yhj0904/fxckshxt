package kr.co.nanwe.surv.service;

import lombok.Data;

/**
 * @Class Name 		: SurvItemVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SurvItemVO {
	/** 설문ID */
	private String survId;
	/** 설문문항IDX */
	private int quesIdx;
	/** 응답항목IDX */
	private int itemIdx;
	/** 항목제목 */
	private String itemTitle;
	/** 항목설명 */
	private String itemMemo;
	/** 점수 */
	private int itemPoint;
	/** 기타여부 */
	private String itemEtc;
	/** 순서 */
	private int itemSort;
	/** 사용유무 */
	private String useYn;
	/**  */
	private String inptId;
	/**  */
	private String inptIp;
	/**  */
	private String inptDttm;
	/**  */
	private String modiId;
	/**  */
	private String modiIp;
	/**  */
	private String modiDttm;

	/** 응답수 */
	private int answCnt;
	/** 응답률 */
	private String answPercent;
}
