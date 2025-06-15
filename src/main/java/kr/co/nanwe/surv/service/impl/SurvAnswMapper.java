package kr.co.nanwe.surv.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.surv.service.SurvAnswVO;

/**
 * @Class Name 		: SurvAnswMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("survAnswMapper")
public interface SurvAnswMapper {
	
	/** 등록 */
	int insertSurvAnsw(SurvAnswVO survAnswVO);
	
	/** 설문등록 */
	int insertSurvAnswList(Map<String, Object> paramMap);
	
	/** 설문삭제시 응답삭제 */
	int deleteSurvAnswBySurvId(String survId);
	
	/** 해당 사용자 응답데이터 삭제 */
	int deleteSurvAnswByLoginId(@Param("survId") String survId, @Param("loginId") String loginId);

	/** 설문결과 - 응답자 목록 조회 */
	List<SurvAnswVO> selectSurvAnswUserList(String survId);

	/** 기타 의견 목록 조회 */
	List<String> selectSurvAnswEtcList(@Param("survId") String survId, @Param("quesIdx") int quesIdx, @Param("itemIdx") int itemIdx);
	
	/** 설문결과조회 */
	List<SurvAnswVO> selectSurvAnswListByUser(@Param("survId") String survId, @Param("quesIdx") int quesIdx, @Param("inptId") String inptId);
	
}
