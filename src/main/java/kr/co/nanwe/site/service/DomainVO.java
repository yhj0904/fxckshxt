package kr.co.nanwe.site.service;

import lombok.Data;

/**
 * @Class Name 		: DomainVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class DomainVO {
	/** 사이트코드 */
	private String siteCd;
	/** 도메인 */
	private String domain;
	/** 비고 */
	private String note;
	/** 기본도메인여부 */
	private String defaultYn;
	/** 사용유무 */
	private String useYn;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
}
