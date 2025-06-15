package kr.co.nanwe.userip.service;

import lombok.Data;

/**
 * @Class Name 		: UserIpVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class UserIpVO {
	
	/** 아이디 */
	private String userId;
	/** 사용자명 */
	private String userNm;
	/** 아이피 */
	private String ip;
	/** 비고 */
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
	
}
