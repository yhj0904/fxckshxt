package kr.co.nanwe.login.service;

import lombok.Data;

/**
 * @Class Name 		: LoginLogVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class LoginLogVO {

	/** 로그코드 */
	private String loginCode;
	/** 로그인 유형 */
	private String loginType;
	/** ID */
	private String loginId;
	/** 이름 */
	private String loginNm;
	/** 아이피 */
	private String loginIp;
	/** OS */
	private String loginOs;
	/** DEVICE */
	private String loginDevice;
	/** BROWSER */
	private String loginBrowser;
	/** 로그인 시각 */
	private String loginDttm;
	/** 로그인 성공여부 */
	private String loginSuccess;
	/** 에러메시지 */
	private String loginErrMsg;
	/** 접속사이트 */
	private String loginUrl;
	
}