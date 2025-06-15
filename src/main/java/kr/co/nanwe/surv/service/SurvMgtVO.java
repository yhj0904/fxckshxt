package kr.co.nanwe.surv.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: SurvMgtVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SurvMgtVO {
	
	/** 설문ID */
	private String survId;
	/** 설문제목 */
	private String survTitle;
	/** 설문설명 */
	private String survMemo;
	/** 시작일자 */
	private String survDate1;
	/** 종료일자 */
	private String survDate2;
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
	/** 설문상태 */
	private String survState;
	
	/** 설문항목 */
	private List<SurvQuesVO> quesList;
	
	/** 설문대상 */
	private List<SurvTrgtVO> trgtList;
	
	/** 점수 */
	private double totalPoint;
	/** 응답자 수 */
	private int totalCount;
}
