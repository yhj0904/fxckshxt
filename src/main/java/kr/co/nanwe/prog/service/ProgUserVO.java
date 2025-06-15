package kr.co.nanwe.prog.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: ProgUserVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.12		신한나			최초생성
 */
@Data
public class ProgUserVO {
	/** 프로그램ID */
	private int progId;
	/** 회원ID */
	private String userId;
	/** 회원명 */
	private String userNm;
	/** 휴대전화번호 */
	private String mbphNo;
	/** 이메일 */
	private String email;
	/** 신분구분(USER_TYPE) */
	private String userTypeCd;
	/** 신분구분상세(졸업생 / 재학생) */
	private String userTypeDetCd;
	/** 학번(원대생) */
	private String stdNo;
	/** 신청일자 */
	private String reqstDt;
	/** 단과대학코드 */
	private String colgCd;
	/** 학과코드 */
	private String deptCd;
	/** 학과명 */
	private String deptNm;
	/** 학년 */
	private String grade;
	/** 성별 */
	private String sexCd;
	/** 도외/도내 거주지 구분 */
	private String local;
	/** 신청상태구분(PROG_REQST) */
	private int progReqstCd;
	/** 신청취소일자 */
	private int reqstCnclDt;
	/** 이수상태구분(PROG_COMPL) */
	private String complCd;
	/** 만족도설문여부 */
	private String surveyYn;
	/** 만족도설문점수 */
	private String surveyScore;
	/** 비고 */
	private String rm;
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
	
}
