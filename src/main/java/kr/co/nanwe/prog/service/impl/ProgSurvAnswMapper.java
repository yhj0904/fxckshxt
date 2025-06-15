package kr.co.nanwe.prog.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.prog.service.ProgSurvAnswVO;

/**
 * @Class Name 		: SurvAnswMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("progSurvAnswMapper")
public interface ProgSurvAnswMapper {
	
	/** 등록 */
	int insertSurvAnsw(ProgSurvAnswVO ProgSurvAnswVO);
	
	/** 설문등록 */
	int insertSurvAnswList(Map<String, Object> paramMap);
	
	/** 설문삭제시 응답삭제 */
	int deleteSurvAnswBySurvId(String survId);
	
	/** 해당 사용자 응답데이터 삭제 */
	int deleteSurvAnswByLoginId(@Param("progId") int progId, @Param("loginId") String loginId);

	/** 설문결과 - 응답자 목록 조회 */
	List<ProgSurvAnswVO> selectSurvAnswUserList(@Param("progId") int progId);

	/** 기타 의견 목록 조회 */
	List<String> selectSurvAnswEtcList(@Param("progId") int progId, @Param("quesIdx") int quesIdx, @Param("itemIdx") int itemIdx);
	
	/** 설문결과조회 */
	List<ProgSurvAnswVO> selectSurvAnswListByUser(@Param("progId") int progId, @Param("quesIdx") int quesIdx, @Param("inptId") String inptId);
	
}
