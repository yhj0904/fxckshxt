package kr.co.nanwe.cnsler.service;

import lombok.Data;

/**
 * @Class Name 		: BbsVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class CnslerVO {
	/** 마스터 테이블 */
	/** 상담원 아이디 */
	private String cnslId;
	/** 사용자 아이디 */
	private String userId;
	/** 신청 사용자 이름 */
	private String cnslUserNm;
	/** 사용자 이름 */
	private String userNm;
	/** 신분구분 */
	private String userTypeCd;
	/** 폰번호 */
	private String mbphNo;
	/** 학번 */
	private String stdNo;
	/** 단과대학코드 */
	private String cnslColgCd;
	/** 학과코드 */
	private String cnslDeptCd;
	/** 학년 */
	private String grade;
	/** 성별 */
	private String sexCd;
	/** 이메일또는 카톡 */
	private String chatId;
	/** 상담분야(CNLS_TYPE) */
	private String cnslTypeCd;
	/** 상담분야(진로) */
	private String cnslTypeTrack;
	/** 상담분야(취업) */
	private String cnslTypeJob;
	/** 상담분야(생활) */
	private String cnslTypeLife;
	/** 상담분야(입사지원서) */
	private String cnslTypeResume;
	/** 상담분야(모의면접) */
	private String cnslTypeIntv;
	/** 상담장소 */
	private String cnslPlace;
	/** 상담방법(CNSL_MTH) */
	private String cnslMthCd;
	/** 요청사항 */
	private String reqstText;
	/** 상담년도 */
	private String year;
	/** 상담학기 */
	private String semstr;
	/** 상담신청일자 */
	private String reqstDt;
	/** 상담예약일자 */
	private String hopeDt;
	/** 상담예약시간 */
	private String hopeTm;
	/** 진행상태(CNSL_STATUS) */
	private String cnslStatusCd;
	/** 상담일자 */
	private String cnslDt;
	/** 상담원ID */
	private String cnslerId;
	/** 상담원이름 */
	private String cnslerNm;
	/** 상담원 사용여부 */
	private String useYn;
	/** 상담원 메모 */
	private String cnslMemo;
	/** 상담내용(공개) */
	private String cnslerCnOpen;
	/** 상담내용(비공개) */
	private String cnslerCnSecret;
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
	/** 단과 한글이름*/
	private String colgNmKor;
	/** 학과 한글이름 */
	private String deptNmKor;
	/** 상담제목 */
	private String title;
	/** 비밀번호 */
	private String pw;
	
	/** 상담일정 테이블 */
	/** 상담일자 */
	private String schDt;
	/** 상담년월 */
	private String schYm;
	/** 상담가능시간(오전) */
	private String cnslAmYn;
	/** 상담가능시간(오후) */
	private String cnslPmYn;
	/** 상담가능시근(최종) */
	private String ableTm;
	/** 상담일괄등록 시작일 */
	private String stDt;
	/** 상담일괄등록 종료일 */
	private String edDt;
	/** 상담일괄등록 선택주기 */
	private String selectDay;
}
