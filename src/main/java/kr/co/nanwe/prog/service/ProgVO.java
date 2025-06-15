package kr.co.nanwe.prog.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: ProgVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.12		신한나			최초생성
 */
@Data
public class ProgVO {
	/** 프로그램ID */
	private int progId;
	/** 개설년도 */
	private String year;
	/** 개설학기 */
	private String semstrCd;
	/** 프로그램명 */
	private String progNm;
	/** 커리어단계(PROG_CAREER) */
	private String progCareerCd;
	/** 커리어단계(PROG_CAREER) */
	private String progCareerCdNm;
	/** 교육대상(PROG_TGT) */
	private String progTgtCd;
	/** 교육방법(PROG_MTH) */
	private String progMthCd;
	/** 교육방법(PROG_MTH_NM) */
	private String progMthCdNm;
	/** 특성분류(PROG_TYPE) */
	private String progTypeCd;
	/** 특성분류(PROG_TYPE) */
	private String progTypeCdNm;
	/** 프로그램설명 */
	private String progSumry;
	/** 프로그램이미지파일ID */
	private int progFileNo;
	/** 프로그램첨부파일 */
	private int fileNo;
	/** 프로그램접근방식(PROG_CONTACT) */
	private String progContactCd;
	/** 프로그램접근방식(PROG_CONTACT_CD_NM) */
	private String progContactCdNm;
	/** 프로그램URL */
	private String progUrl;
	/** 교육시작일자 */
	private String progSdt;
	/** 교육종료일자 */
	private String progEdt;
	/** 교육시작시간 */
	private String progStm;
	/** 교육마감시간 */
	private String progEtm;
	/** 교육운영시간 */
	private int progTime;
	/** 교육장소 */
	private String progPlace;
	/** 교육기관명 */
	private String eduOrgNm;
	/** 교육담당자명 */
	private String eduMngNm;
	/** 교육담당전화번호 */
	private String eduTelNo;
	/** 교육담당이메일 */
	private String eduEmail;
	/** 교육담당오픈채팅주소 */
	private String eduChatId;
	/** 모집시작일자 */
	private String reqstSdt;
	/** 모집종료일자 */
	private String reqstEdt;
	/** 모집시작여부(화면공지) */
	private String reqstYn;
	/** 모집인원 */
	private int reqstNmpr;
	/** 신청인원 */
	private int applNmpr;
	/** 진행상태(PROG_STATUS) */
	private String statusCd;
	/** 진행상태(PROG_STATUS) */
	private String statusCdNm;
	/** 만족도설문여부 */
	private String surveyYn;
	/** 설문시작일자 */
	private String surveySdt;
	/** 설문종료일자 */
	private String surveyEdt;
	/** 설문답변인원 */
	private int surveyNmpr;
	/** 설문결과점수 */
	private int surveyScore;
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
	/** 삭제 Y/N */
	private String delYn;
	/** 삭제ID */
	private String delId;
	/** 삭제IP */
	private String delIp;
	/** 삭제일시 */
	private String delDttm;
	
	/** D-DAY */
	private String dday;
	/** D-DAY */
	private String ddayCd;
	
	
	/** 첨부파일 목록 */
	private List<ViewFileVO> viewFiles;
	/** 업로드 파일 */
	private List<MultipartFile> uploadFiles;
	/** 업로드파일 -프로그램 썸네일 */
	private MultipartFile uploadFile;
	/** 첨부파일 -프로그램 썸네일 */
	private ViewFileVO viewFile;
}
