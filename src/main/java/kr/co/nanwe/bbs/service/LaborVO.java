package kr.co.nanwe.bbs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: LaborVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.11.07		신한나			최초생성
 */
@Data
public class LaborVO {
	/** 아이디 */
	private String labrId;
	/** 회원ID */
	private String userId;
	/** 회원명 */
	private String userNm;
	/** 신분구분(USER_TYPE) */
	private String userTypeCd;
	/** 학번(원대생) */
	private String stdNo;
	/** 단과대학코드 */
	private String colgCd;
	/** 단과대학코드명 */
	private String colgNm;
	/** 학과코드 */
	private String deptCd;
	/** 학과코드명 */
	private String deptNm;
	/** 졸업년도 */
	private String grYear;
	/** 졸업년도 */
	private String year;
	/** 학년 */
	private String grade;
	/** 학년 */
	private String gradeNm;
	/** 성별 */
	private String sexCd;
	/** 성별 */
	private String sexNm;
	/** 이메일 */
	private String email;
	/** 연락처 */
	private String mbphNo;
	/** 학점 */
	private double colgScore;
	/** 대외활동 */
	private String extAct;
	/** 자격증 */
	private String license;
	/** 어학능력(점수) */
	private String langScore;
	/** 희망직무 */
	private String whJob;
	/** 희망기업 */
	private String whCompany;
	/** 희망근무지 */
	private String whLocal;
	/** 희망 프로그램_01 */
	private String whProgram01;
	/** 희망 프로그램_02 */
	private String whProgram02;
	/** 취업준비계획 */
	private String jobPrepPlan;
	/** 투자한 평균시간 */
	private String jobPrepTime;
	/** 궁금한점 */
	private String whQuestions;
	/** 개인정보 동의여부 */
	private String agreeYn;
	/** 비밀번호 */
	private String pw;
	/** 비밀글여부 */
	private String secret;
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
	/**  */
	private String delYn;
	/**  */
	private String delId;
	/**  */
	private String delIp;
	/**  */
	private String delDttm;
	
	/** 희망근무지역 목록*/
	private List<String> whLocalList;
	/** 참여희망프로그램 목록 */
	private List<String> whProgList01;
	
	/** 소속 */
	private String belong;
	/** 면접경험 */
	private String itvExp;
	/** 희망임금(만원) */
	private String dsrdWage;
	/** 희망근로조건 */
	private String dsrdWorkCdt;
	/** 희망근로조건 이름 */
	private String workCdtNm;
	/** 밴드가입여부 */
	private String bandMember;
	/** 기타 학년 */
	private String etcGrade;
	/** 기타 프로그램 */
	private String etcProgram;
}
