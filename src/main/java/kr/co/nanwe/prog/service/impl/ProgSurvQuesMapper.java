package kr.co.nanwe.prog.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.prog.service.ProgSurvQuesVO;

/**
 * @Class Name 		: SurvQuesMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("progSurvQuesMapper")
public interface ProgSurvQuesMapper {
	
	/** 관리자 목록 조회 */
	List<ProgSurvQuesVO> selectSysSurvQuesList(@Param("survId") String survId, @Param("useYn") String useYn);
	
	/** 목록 조회 */
	List<ProgSurvQuesVO> selectSurvQuesList(@Param("progId") int progId, @Param("useYn") String useYn);
	
	/** 등록 */
	int insertSurvQues(ProgSurvQuesVO ProgSurvQuesVO);
	
	/** 수정 */
	int updateSurvQues(ProgSurvQuesVO ProgSurvQuesVO);
	
	/** 삭제 */
	int deleteSurvQues(ProgSurvQuesVO ProgSurvQuesVO);
	
	/** 설문삭제시 문항삭제 */
	int deleteSurvQuesBySurvId(@Param("survId") String survId);
	
	/** 설문결과 조회 */
	List<ProgSurvQuesVO> selectSurvQuesResult(@Param("progId") int progId, @Param("totalCount") int totalCount);

	/** 설문결과 조회 - 사용자 */
	List<ProgSurvQuesVO> selectSurvQuesResultByUser(@Param("progId") int progId, @Param("inptId") String inptId);

	/** 프로그램 id에 따른 설문 문항 개수 체크 */
	int selectProgSurvQuesCnt(@Param("progId") String string);
}
