package kr.co.nanwe.bbs.service;

import java.util.Map;

/**
 * @Class Name 		: BbsCmntService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface BbsCmntService {
	
	/** 목록 조회 */
	Map<String, Object> selectBbsCmntList(BbsMgtVO bbsMgtVO, String bbsId, String currentPage);
	
	/** 상세 조회 */
	BbsCmntVO selectBbsCmnt(BbsMgtVO bbsMgtVO, String bbsId, String cmntId);
	
	/** 등록 */
	int insertBbsCmnt(BbsMgtVO bbsMgtVO, BbsCmntVO bbsCmntVO);
	
	/** 삭제 */
	int deleteBbsCmnt(BbsMgtVO bbsMgtVO, BbsCmntVO bbsCmntVO);
	
}
