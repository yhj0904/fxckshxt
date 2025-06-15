package kr.co.nanwe.surv.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.surv.service.SurvMgtVO;

/**
 * @Class Name 		: SurvMgtMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("survMgtMapper")
public interface SurvMgtMapper {
	
	/** 전체 Row 개수 */
	int selectSurvMgtTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<SurvMgtVO> selectSurvMgtList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	SurvMgtVO selectSurvMgt(String survId);
	
	/** 등록 */
	int insertSurvMgt(SurvMgtVO survMgtVO);
	
	/** 수정 */
	int updateSurvMgt(SurvMgtVO survMgtVO);
	
	/** 삭제 */
	int deleteSurvMgt(String id);
	
	/** 설문상태 변경 */
	int updateSurvState(SurvMgtVO survMgtVO);
	
	/** 사용자 체크 후 진행 가능한 설문 목록 조회 */
	List<SurvMgtVO> selectSurveyList(Map<String, Object> paramMap);
	
	/** 사용자 체크 후 진행 가능한 설문 상세조회 */
	SurvMgtVO selectSurvey(Map<String, Object> paramMap);

	/** 최근 진행중인 설문 목록 */
	List<SurvMgtVO> selectRecentSurvList();
}
