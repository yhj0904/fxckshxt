package kr.co.nanwe.push.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.push.service.PushNoticeSendVO;

/**
 * @Class Name 		: PushNoticeSendMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("pushNoticeSendMapper")
public interface PushNoticeSendMapper {

	/** 알림 전송 목록 조회 */
	List<PushNoticeSendVO> selectPushNoticeSendList(int noticeNo);

	/** 알림 전송 등록 */
	int insertPushNoticeSend(PushNoticeSendVO pushNoticeSendVO);

	/** 알림 전송 삭제 */
	int deletePushNoticeSendList(int noticeNo);

	/** 발송건별 확인  */
	int selectPushNoticeSendCountByParam(Map<String, Object> paramMap);

	/** 월별 사용량  */
	List<Map<String, Object>> selectPushNoticeSendCountByMonth(Map<String, Object> paramMap);

}
