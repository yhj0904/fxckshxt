package kr.co.nanwe.push.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: PushNoticeVO
 * @Description 	: 알림
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class PushNoticeVO {
	/** 알림순번 */
	private int noticeNo;
	/** 알림제목 */
	private String noticeTitle;
	/** 알림내용 */
	private String noticeData;
	/** 알림이미지 */
	private String noticeImg;
	/** 알림작성자 ID */
	private String userId;
	/** 알림작성자 이름 */
	private String userNm;
	/** 알림작성자 모바일 */
	private String userMobile;
	/** 알림작성일시 */
	private String noticeDt;
	/** 문자발송 건수 */
	private int smsCnt;
	/** 문자전송 성공 */
	private int smsSuccessCnt;
	/** 문자전송 실패 */
	private int smsFailCnt;
	/** 푸시발송 건수 */
	private int pushCnt;
	/** 푸시발송 성공 */
	private int pushSuccessCnt;
	/** 푸시발송 실패 */
	private int pushFailCnt;
	/** 푸시읽은 건수 */
	private int pushReadCnt;
	/** 전체 건수 */
	private int totalCnt;
	/** 예약전송일 */
	private String reservationYn;
	private String reservationYYYYMMDD;
	private int reservationHour;
	private int reservationMinute;
	private String reservationDt;
	/** 상태 */
	private String pushState;
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
	
	/** 전송목록 */
	List<PushNoticeSendVO> sendList;
	
	/** 첨부파일 */
	private int fileNo;
	private MultipartFile uploadFile;
	private ViewFileVO viewFile;
}
