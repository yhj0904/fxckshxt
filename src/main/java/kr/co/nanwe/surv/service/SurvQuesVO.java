package kr.co.nanwe.surv.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: SurvQuesVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SurvQuesVO {
	/** 설문ID */
	private String survId;
	/** 문항IDX */
	private int quesIdx;
	/** 문항제목 */
	private String quesTitle;
	/** 문항설명 */
	private String quesMemo;
	/** 문항유형 */
	private String quesType;
	/** 응답개수 */
	private int quesCnt;
	/** 순서 */
	private int quesSort;
	/** 사용유무 */
	private String useYn;
	/** 필수여부 */
	private String ncsryYn;
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
	
	/** 아이템 목록 */
	private List<SurvItemVO> itemList;
	
	/** 응답목록 */
	private List<SurvAnswVO> answList;
	
	/** 점수평점 */
	private double avgPoint;
	/** 응답수 */
	private int answCnt;
	/** 응답률 */
	private String answPercent;
}
