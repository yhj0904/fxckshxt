package kr.co.nanwe.content.service;

import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: ContentService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface ContentService {
	
	/** 목록 조회 */
	Map<String, Object> selectContentList(SearchVO search);
	
	/** 상세 조회 */
	ContentVO selectContent(String contId);
	
	/** 등록 */
	int insertContent(ContentVO contentVO);
	
	/** 수정 */
	int updateContent(ContentVO contentVO);
	
	/** 삭제 */
	int deleteContent(String contId);
	
	/** 선택 삭제 */
	int deleteCheckedContent(String checkedSId);
	
	/** 백업 목록 조회 */
	Map<String, Object> selectContentBackList(SearchVO search, String contId);

	/** 백업 상세 조회 */
	ContentVO selectContentBack(String contId, int seq);
	
	/** 컨텐츠 원복 */
	int updateContentByBack(String contId, int seq);
	
}
