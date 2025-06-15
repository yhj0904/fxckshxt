package kr.co.nanwe.bbs.service;

import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: BbsMgtService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface BbsMgtService {
	
	/** 목록 조회 */
	Map<String, Object> selectBbsMgtList(SearchVO search);
	
	/** 상세 조회 */
	BbsMgtVO selectBbsMgt(String code);
	
	/** 등록 */
	int insertBbsMgt(BbsMgtVO bbsMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth, String[] replyAuth, String[] cmntAuth);
	
	/** 수정 */
	int updateBbsMgt(BbsMgtVO bbsMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth, String[] replyAuth, String[] cmntAuth);
	
	/** 삭제 */
	int deleteBbsMgt(String code);
	
	/** 존재하는 코드인지 체크 */
	boolean selectBbsMgtCodeExist(String code);
	
	/** 게시판 권한 확인*/
	BbsMgtVO selectBbsMgtByAuth(String code, String action);
	
}
