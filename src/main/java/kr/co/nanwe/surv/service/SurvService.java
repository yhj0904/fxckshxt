package kr.co.nanwe.surv.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: SurvService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface SurvService {
	
	/** 설문 목록 조회 */
	Map<String, Object> selectSurvMgtList(SearchVO search);
	
	/** 설문 상세 조회 */
	SurvMgtVO selectSurvMgt(String id);
	
	/** 설문 등록 */
	int insertSurvMgt(SurvMgtVO survMgtVO);
	
	/** 설문 수정 */
	int updateSurvMgt(SurvMgtVO survMgtVO);
	
	/** 설문 삭제 */
	int deleteSurvMgt(String id);
	
	/** 설문 상태 변경 */
	boolean updateSurvState(String survId, String survState);
	
	/** 설문 문항 및 항목 등록 */
	boolean updateSurvQuesAndItem(Map<String, Object> surveyMap);
	
	/** 사용자 체크 후 진행 가능한 설문 목록 출력 */
	List<SurvMgtVO> selectSurveyList();
	
	/** 사용자 체크 후 설문정보 조회*/
	SurvMgtVO selectSurvey(String survId);
	
	/** 설문 응답 등록 */
	Map<String, Object> insertSurveyAnswer(Map<String, Object> surveyMap);

	/** 설문결과 */
	Map<String, Object> selectSurveyResult(String id);

	/** 기타 의견 목록 조회 */
	List<String> selectSurvAnswEtcList(String survId, int quesIdx, int itemIdx);
	
	/** 설문결과 */
	Map<String, Object> selectSurveyResultByUser(String survId, String userId);

	/** 최근 진행중인 설문 목록 */
	List<SurvMgtVO> selectRecentSurvList();
	
}
