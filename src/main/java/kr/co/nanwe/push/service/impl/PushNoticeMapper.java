package kr.co.nanwe.push.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.push.service.PushNoticeVO;

/**
 * @Class Name 		: PushNoticeMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("pushNoticeMapper")
public interface PushNoticeMapper {

	/** 알림 총 카운트  */
	int selectPushNoticeTotCnt(Map<String, Object> paramMap);

	/** 알림 목록 */
	List<PushNoticeVO> selectPushNoticeList(Map<String, Object> paramMap);

	/** 알림 상세조회 */
	PushNoticeVO selectPushNotice(int noticeNo);

	/** 알림 등록 */
	int insertPushNotice(PushNoticeVO pushNoticeVO);
	
	/** 알림 수정 */
	int updatePushNotice(PushNoticeVO pushNoticeVO);

	/** 알림  삭제 */
	int deletePushNotice(int noticeNo);

	/** 알림  엑셀 목록 조회*/
	void selectPushNoticeExcelList(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);

	/** 로그인 사용자 알림 조회*/
	PushNoticeVO selectPushNoticeByUser(@Param("userId") String userId, @Param("noticeNo") int noticeNo);

	/** 최근 발송 알림 */
	List<Map<String, Object>> selectRecentPushNoticeList(Map<String, Object> paramMap);

	

}
