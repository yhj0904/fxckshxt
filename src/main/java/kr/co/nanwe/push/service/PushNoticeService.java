package kr.co.nanwe.push.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: PushNoticeService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface PushNoticeService {

	/** 알림전송 목록  */
	Map<String, Object> selectPushNoticeList(SearchVO search);

	/** 알림전송 조회  */
	PushNoticeVO selectPushNotice(int noticeNo);

	/** 알림전송 등록  */
	int insertPushNotice(PushNoticeVO pushNoticeVO);

	/** 알림전송 삭제  */
	int deletePushNotice(int noticeNo);

	/** 알림전송 선택삭제  */
	int deleteCheckedPushNotice(String checkedSId);

	/** 알림전송 엑셀 목록 조회  */
	void selectPushNoticeExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);
	
	/** 로그인 사용자 알림전송 목록  */
	Map<String, Object> selectPushNoticeListByLoginUser(SearchVO search);

	/** 로그인 사용자 알림전송 조회  */
	PushNoticeVO selectPushNoticeByLoginUser(int noticeNo);
	
	/** 발송건별 확인  */
	int selectPushNoticeSendCountByState(String type);
	
	/** 캘린더 발송건별  */
	Map<String, Object> selectPushNoticeSendCountByDate(String date);
	
	/** 채널별 월 사용량 (프로그레스바)  */
	Map<String, Object> selectPushNoticeSendCountByMonthProgressBar();
	
	/** 채널별 월 사용량 (그래프)  */
	Map<String, Object> selectPushNoticeSendCountByMonthGraph();
	
	/** 최근 발송알림  */
	List<Map<String, Object>> selectRecentPushNoticeList();
	
}
