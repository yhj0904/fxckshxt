package kr.co.nanwe.bbs.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.bbs.service.BbsCmntVO;

/**
 * @Class Name 		: BbsCmntMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("bbsCmntMapper")
public interface BbsCmntMapper {

	/** 전체 Row 개수 */
	int selectBbsCmntTotCnt(Map<String, Object> paramMap);

	/** 목록 조회 */
	List<BbsCmntVO> selectBbsCmntList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	BbsCmntVO selectBbsCmnt(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId, @Param("cmntId") String cmntId);
	
	/** 등록 */
	int insertBbsCmnt(BbsCmntVO bbsCmntVO);
	
	/** 수정 */
	int updateBbsCmnt(BbsCmntVO bbsCmntVO);
	
	/** 삭제 */
	int deleteBbsCmnt(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId, @Param("cmntId") String cmntId);
	
	/** 게시글 삭제시 댓글삭제 */
	int deleteBbsCmntByBbsId(@Param("bbsCd") String bbsCd, @Param("bbsId") String bbsId);
	
	/** 게시판 삭제시 댓글삭제 */
	int deleteBbsCmntByBbsCd(@Param("bbsCd") String bbsCd);
}
