package kr.co.nanwe.login.service;

import java.io.Serializable;

import lombok.Data;

/**
 * @Class Name 		: LoginVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class LoginVO implements Serializable{

	private static final long serialVersionUID = -8274004534207618049L;
	
	/** 로그인 유형 */
	private String loginType;
	
	/** 로그인 사용자 유형 */
	private String loginUserType;
	
	/** 사용자ID */
	private String loginId;
	
	/** 사용자명 */
	private String loginNm;
	
	/** 패스워드 */
	private String loginPw;
	
	/** 인증키 */
	private String loginCertKey;
	
	/** 로그인 아이피 */
	private String loginIp;
	
	/** OS */
	private String loginOs;
	
	/** DEVICE */
	private String loginDevice;
	
	/** BROWSER */
	private String loginBrowser;
	
	/** 로그인 시각 */
	private String loginDttm;
	
	/** 최초로그인 시각 */
	private String loginStartDttm;
	
	/** 로그인 가능 여부 */
	private String loginYn;
	
	/** 로그인 성공 여부 */
	private String loginSuccess;
	
	/** 에러코드 */
	private String loginErrCode;
	
	/** 에러메시지 */
	private String loginErrMsg;
	
	/** 사용자분류 */
	private String userDvcd;
	/** 사용자분류 */
	private String userDvnm;
	
	/** 신분코드 */
	private String workDvcd;
	/** 신분코드 */
	private String workDvnm;
	
	/** 상태코드 */
	private String statDvcd;
	/** 상태코드 */
	private String statDvnm;
	
	/** 부가정보 */
	private String telNo;
	private String mbphNo; 
	private String email;
	private String postNo;
	private String addr; 
	private String detlAddr;
	private String deptCd;
	private String deptNm;
	
	/** 이미지 */
	private int fileNo;
	private String userImgSrc;
	
	private String sessionKey;
	
	private String loginUrl;
	
	/** SNS */
	private String snsYn;
	private String accessToken;
	private String snsType;
	private String snsId;
	
}