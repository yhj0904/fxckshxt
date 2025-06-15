package kr.co.nanwe.content.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.content.service.ContentVO;

/**
 * @Class Name 		: ContentMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("contentMapper")
public interface ContentMapper {
	
	/** 전체 Row 개수 */
	int selectContentTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<ContentVO> selectContentList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	ContentVO selectContent(String contId);
	
	/** 등록 */
	int insertContent(ContentVO contentVO);
	
	/** 수정 */
	int updateContent(ContentVO contentVO);
	
	/** 삭제 */
	int deleteContent(String contId);
	
	/** 컨텐츠 백업 Row 개수 */
	int selectContentBackTotCnt(Map<String, Object> paramMap);

	/** 컨텐츠 백업 목록 조회 */
	List<ContentVO> selectContentBackList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	ContentVO selectContentBack(@Param("contId") String contId, @Param("seq") int seq);
	
	/** 컨텐츠 백업 */
	int insertContentBack(ContentVO contentVO);
	
	/** 컨텐츠 백업 삭제 */
	int deleteContentBack(@Param("contId") String contId, @Param("seq") int seq);

	/** 컨텐츠 백업 삭제 (코드) */
	int deleteContentBackByContId(String contId);
	
}
