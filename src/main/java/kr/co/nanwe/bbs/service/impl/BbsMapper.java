package kr.co.nanwe.bbs.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.bbs.service.BbsVO;

/**
 * @Class Name 		: BbsMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("bbsMapper")
public interface BbsMapper {
	
	/** 전체 Row 개수 */
	int selectBbsTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<BbsVO> selectBbsList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	BbsVO selectBbs(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId);
	
	/** 등록 */
	int insertBbs(BbsVO bbsMgtVO);
	
	/** 수정 */
	int updateBbs(BbsVO bbsMgtVO);
	
	/** 해당 게시글의 답글이 있는지 체크 */
	int selectChildBbsCnt(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId);
	
	/** 삭제상태로 변경 */
	int updateBbsToDelete(BbsVO bbsMgtVO);
	
	/** 삭제 */
	int deleteBbs(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId);
	
	/** 그룹 체크 후 삭제된상태 게시글 삭제 */
	int deleteBbsByGroupId(@Param("bbsCd") String bbsCd, @Param("groupId") String groupId);

	/** 조회수 증가 */
	int updateBbsViewCnt(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId);

	/** 공지사항 목록 조회 */
	List<BbsVO> selectBbsNoticeList(Map<String, Object> paramMap);

	/** 이전글 조회 */
	BbsVO selectPrevBbs(Map<String, Object> paramMap);

	/** 다음글 조회 */
	BbsVO selectNextBbs(Map<String, Object> paramMap);

	/** 최근 게시물 */
	List<BbsVO> selectRecentBbsList();

	/** 최근 게시물 */
	List<BbsVO> selectRecentBbsListByParam(Map<String, Object> paramMap);
}
