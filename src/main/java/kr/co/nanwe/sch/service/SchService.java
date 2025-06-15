package kr.co.nanwe.sch.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: SchService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface SchService {
	
	/** 목록 조회  */
	Map<String, Object> selectSchList(SchMgtVO schMgtVO, SearchVO search, String category);
	
	/** 목록 조회  */
	Map<String, Object> selectSchCalendar(SchMgtVO schMgtVO, SearchVO search, String category, String date, String option);
	
	/** 상세 조회 */
	SchVO selectSch(SchMgtVO schMgtVO, String schId);
	
	/** 등록 */
	int insertSch(SchMgtVO schMgtVO, SchVO schVO);
	
	/** 수정 */
	int updateSch(SchMgtVO schMgtVO, SchVO schVO);
	
	/** 삭제 */
	int deleteSch(SchMgtVO schMgtVO,  String schId);
	
	/** 선택 삭제 */
	int deleteCheckedSch(SchMgtVO schMgtVO, String checkedSId);
	
	/** 조회수 증가 */
	int updateSchViewCnt(String schCd, String schId);
	
	/** 권한 체크 후 상세조회 */
	Map<String, Object> selectSchByAuth(SchMgtVO schMgtVO, String schId, String action);
	
	/** 최근 일정 */
	List<SchVO> selectRecentSchListByParam(Map<String, Object> param);
	
}
