package kr.co.nanwe.user.service;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: UserVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			필드 추가
 */

@Data
public class UserVO {
	
	/** 사용자ID */
	private String userId;
	/** 사용자명 */
	private String userNm;
	/** 패스워드 */
	private String password;
	/** 패스워드 확인 */
	private String passwordCheck;
	/** 소금키 */
	private String saltKey;
	/** 사용시작일시 */
	private String useSttDttm;
	/** 사용종료일시 */
	private String useEndDttm;
	/** 부서코드 */
	private String deptCd;
	/** 부서명 */
	private String deptNm;
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
	/** 사용여부 */
	private String useYn;
	/** 비고 */
	private String note;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
	/** 탈퇴여부 */
	private String delYn;
	/** 탈퇴ID */
	private String delId;
	/** 탈퇴IP */
	private String delIp;
	/** 탈퇴일시 */
	private String delDttm;
	
	/** 권한 */
	private String authCd;
	
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
	
	
	/** 신분구분 */
	private String userTypeCd;
	/** 신분구분 */
	private String userTypeCdNm;
	/** 신분상세 */
	private String userTypeDetCd;
	/** 신분상세 */
	private String userTypeDetCdNm;
	/** 학번(재학생) */
	private String stdNo;
	/** 단과대학코드 */
	private String colgCd;
	/** 단과대학코드명 */
	private String colgNm;
	/** 학년(1/2/3/4/5/6) */
	private String grade;
	/** 성별(1:남자,2:여자) */
	private String sex;
	/** 성별(1:남자,2:여자) */
	private String sexNm;
	/** 거주지(도내/도외) */
	private String local;
	/** 거주지(도내/도외) 명 */
	private String localNm;
	
	
	private int fileNo;
	private MultipartFile uploadFile;
	private ViewFileVO viewFile;
	
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

	/** 추가 */
	private String belong;
	private String itvExp;
	private String dsrdWage;
	private String dsrdWorkCdt;
	private String bandMember;
}
