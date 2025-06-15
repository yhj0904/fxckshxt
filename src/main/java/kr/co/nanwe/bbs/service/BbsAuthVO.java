package kr.co.nanwe.bbs.service;

import lombok.Data;

/**
 * @Class Name 		: BbsAuthVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class BbsAuthVO {
	/**  */
	private String bbsCd;
	/**  */
	private String authCd;
	/**  */
	private String userId;
	/**  */
	private String listAuth;
	/**  */
	private String viewAuth;
	/**  */
	private String regiAuth;
	/**  */
	private String replyAuth;
	/**  */
	private String cmntAuth;
	/**  */
	private String note;
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
	
	private String action;
}
