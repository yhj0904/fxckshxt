package kr.co.nanwe.surv.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.surv.service.SurvQuesVO;

/**
 * @Class Name 		: SurvQuesMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("survQuesMapper")
public interface SurvQuesMapper {
	
	/** 목록 조회 */
	List<SurvQuesVO> selectSurvQuesList(@Param("survId") String survId, @Param("useYn") String useYn);
	
	/** 등록 */
	int insertSurvQues(SurvQuesVO survQuesVO);
	
	/** 수정 */
	int updateSurvQues(SurvQuesVO survQuesVO);
	
	/** 삭제 */
	int deleteSurvQues(SurvQuesVO survQuesVO);
	
	/** 설문삭제시 문항삭제 */
	int deleteSurvQuesBySurvId(@Param("survId") String survId);
	
	/** 설문결과 조회 */
	List<SurvQuesVO> selectSurvQuesResult(@Param("survId") String survId, @Param("totalCount") int totalCount);

	/** 설문결과 조회 - 사용자 */
	List<SurvQuesVO> selectSurvQuesResultByUser(@Param("survId") String survId, @Param("inptId") String inptId);
	
}
