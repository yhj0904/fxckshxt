package kr.co.nanwe.surv.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.surv.service.SurvItemVO;

/**
 * @Class Name 		: SurvItemMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("survItemMapper")
public interface SurvItemMapper {
	
	/** 목록 조회 */
	List<SurvItemVO> selectSurvItemList(@Param("survId") String survId, @Param("quesIdx") int quesIdx, @Param("useYn") String useYn);
	
	/** 등록 */
	int insertSurvItem(SurvItemVO survItemVO);
	
	/** 수정 */
	int updateSurvItem(SurvItemVO survItemVO);
	
	/** 삭제 */
	int deleteSurvItem(SurvItemVO survItemVO);
	
	/** 설문삭제시 항목삭제 */
	int deleteSurvItemBySurvId(@Param("survId") String survId);
	
	/** 문항삭제시 항목삭제 */
	int deleteSurvItemByQuesIdx(@Param("survId") String survId, @Param("quesIdx") int quesIdx);
	
	/** 설문결과조회 */
	List<SurvItemVO> selectSurvItemResult(@Param("survId") String survId, @Param("quesIdx") int quesIdx, @Param("totalCount") int totalCount);
	
}
