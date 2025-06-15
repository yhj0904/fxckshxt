package kr.co.nanwe.push.service;

import lombok.Data;

/**
 * @Class Name 		: PushNoticeSendVO
 * @Description 	: 알림전송관리
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class PushNoticeSendVO {
	/** 전송순번 */
	private int sendSeq;
	/** 알림순번 */
	private int noticeNo;
	/** 사용자 아이디 */
	private String userId;
	/** 사용자 성명 */
	private String userNm;
	/** 사용자 모바일 */
	private String userMobile;
	/** 디바이스 구분 */
	private String deviceDv;
	/** 전송 구분 */
	private String sendDv;
	/** 전송 시간 */
	private String sendTime;
	/** 전송시도 횟수 */
	private int sendCnt;
	/** 수신구분 */
	private String rcveDv;
	/** 수신자 확인 여부 */
	private String rcveChk;
	/** 수신 실패구분 */
	private String rcveFailDv;
	/** 수신자 확인 시간 */
	private String rcveTime;
	/** 비고 */
	private String note;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 입력프로그램 */
	private String inptProg;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
	/** 수정프로그램 */
	private String modiProg;
}
