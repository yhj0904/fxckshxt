package kr.co.nanwe.push.service;

import java.util.HashMap;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: PushAppUserService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface PushAppUserService {

	/** 앱사용자 등록 여부 확인 */
	boolean selectPushAppUserExist(String userId, String token);

	/** 앱사용자 등록  */
	int registPushAppUser(String userId, String token);

	/** 앱사용자 목록  */
	Map<String, Object> selectPushAppUserList(SearchVO search);

	/** 앱사용자 상세조회  */
	PushAppUserVO selectPushAppUser(String userId);

	/** 앱사용자 등록  */
	int insertPushAppUser(PushAppUserVO pushAppUserVO);

	/** 앱사용자 수정  */
	int updatePushAppUser(PushAppUserVO pushAppUserVO);

	/** 앱사용자 삭제  */
	int deletePushAppUser(String userId);

	/** 앱사용자 선택삭제  */
	int deleteCheckedPushAppUser(String checkedSId);
	
	/** 앱사용자 엑셀 목록 조회 */
	void selectPushAppUserExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);
	
	/** 앱사용자 COUNT */
	Map<String, Object> selectPushAppUserCount();
	
}
