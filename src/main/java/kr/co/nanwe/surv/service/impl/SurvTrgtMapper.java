package kr.co.nanwe.surv.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.surv.service.SurvTrgtVO;

/**
 * @Class Name 		: SurvTrgtMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("survTrgtMapper")
public interface SurvTrgtMapper {
	
	/** 설문대상 조회 */
	List<SurvTrgtVO> selectSurvTrgtList(@Param("survId") String survId);
	
	/** 등록 */
	int insertSurvTrgt(Map<String, Object> paramMap);
	
	/** 설문삭제시 대상삭제 */
	int deleteSurvTrgtBySurvId(String survId);
}
