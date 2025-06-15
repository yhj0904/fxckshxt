package kr.co.nanwe.join.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: JoinVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			필드 추가
 */

@Data
public class JoinVO {
	
	/** 사용자ID */
	private String userId;
	/** 사용자명 */
	private String userNm;
	/** 패스워드 */
	private String password;
	/** 패스워드 확인 */
	private String passwordCheck;
	/** 생년월일 */
	private String birth;
	/** 우편번호 */
	private String postNo;
	/** 주소 */
	private String addr;
	/** 세부주소 */
	private String detlAddr;
	/** 휴대전화번호 */
	private String mbphNo;
	/** 전화번호 */
	private String telNo;
	/** EMAIL */
	private String email;	
	/** 권한 */
	private String authCd;
	/** 약관 동의 여부 */
	private boolean agree;
	/** 2차인증 */
	private String certKey;
	/** 이용약관 */
	private List<TermsVO> termList;

	/** 신분구분 */
	private String userTypeCd;
	/** 신분상세 */
	private String userTypeDetCd;
	/** 학번(재학생) */
	private String stdNo;
	/** deptCd */
	private String deptCd;
	/** deptNm */
	private String deptNm;
	/** 단과대학코드 */
	private String colgCd;
	/** 학년(1/2/3/4/5/6) */
	private String grade;
	/** 성별(1:남자,2:여자) */
	private String sex;
	/** 거주지(도내/도외) */
	private String local;
	/** 거주지(도내/도외) 명 */
	private String localNm;
	
	/** SNS */
	private String accessToken;
	private String snsType;
	private String snsId;
	
	/** 추가정보 */
	private String userInfo01;
	private String userInfo02;
	private String userInfo03;
	private String userInfo04;
	private String userInfo05;
	private String userInfo06;
	private String userInfo07;
	private String userInfo08;
	private String userInfo09;
	private String userInfo10;
}
