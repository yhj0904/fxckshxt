package kr.co.nanwe.surv.service;

import lombok.Data;

/**
 * @Class Name 		: SurvTrgtVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SurvTrgtVO {
	/** 설문ID */
	private String survId;
	/** 대상유형 */
	private String trgtType;
	/** 대상권한코드 */
	private String trgtCd;
	/** 대상ID */
	private String trgtId;
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
}
