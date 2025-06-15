package kr.co.nanwe.sch.service;

import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: SchMgtService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface SchMgtService {
	
	/** 목록 조회 */
	Map<String, Object> selectSchMgtList(SearchVO search);
	
	/** 상세 조회 */
	SchMgtVO selectSchMgt(String code);
	
	/** 등록 */
	int insertSchMgt(SchMgtVO schMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth);
	
	/** 수정 */
	int updateSchMgt(SchMgtVO schMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth);
	
	/** 삭제 */
	int deleteSchMgt(String code);
	
	/** 존재하는 코드인지 체크 */
	boolean selectSchMgtCodeExist(String code);
	
	/** 일정 권한 확인*/
	SchMgtVO selectSchMgtByAuth(String code, String action);
	
}
