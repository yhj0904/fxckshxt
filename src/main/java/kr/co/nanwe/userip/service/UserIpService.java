package kr.co.nanwe.userip.service;

import java.util.HashMap;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: UserIpService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface UserIpService {
	
	/** 목록 조회 */
	Map<String, Object> selectUserIpList(SearchVO search);
	
	/** 상세 조회 */
	UserIpVO selectUserIp(String id, String ip);
	
	/** 등록 */
	int insertUserIp(UserIpVO userIpVO);
	
	/** 수정 */
	int updateUserIp(UserIpVO userIpVO);
	
	/** 삭제 */
	int deleteUserIp(String id, String ip);
	
	/** 선택 삭제 */
	int deleteCheckedUserIp(String checkedSId);
	
	/** 존재하는 아이피인지 체크 */
	boolean selectUserIpExist(String id, String ip);
	
	/** 엑셀 다운을 위한 목록 조회  */
	void selectUserIpExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);	

}
