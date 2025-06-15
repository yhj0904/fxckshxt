package kr.co.nanwe.push.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.push.service.PushAppUserVO;

/**
 * @Class Name 		: PushAppUserMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("pushAppUserMapper")
public interface PushAppUserMapper {

	/** 등록여부 확인 */
	int selectPushAppUserExist(@Param("userId") String userId, @Param("deviceId") String deviceId);

	/** 앱사용자 정보 조회 */
	PushAppUserVO selectPushAppUser(String userId);	

	/** 앱사용자 등록 */
	int insertPushAppUser(PushAppUserVO pushAppUserVO);

	/** 앱사용자 수정 */
	int updatePushAppUser(PushAppUserVO pushAppUserVO);

	/** 앱사용자 삭제 */
	int deletePushAppUser(String userId);

	/** 앱사용자 TOTAL COUNT */
	int selectPushAppUserTotCnt(Map<String, Object> paramMap);

	/** 앱사용자 목록 */
	List<PushAppUserVO> selectPushAppUserList(Map<String, Object> paramMap);

	/** 앱사용자 엑셀 목록 */
	void selectPushAppUserExcelList(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);

	/** 앱사용자 카운트 */
	Map<String, Object> selectPushAppUserCount();

}
