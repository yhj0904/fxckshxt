package kr.co.nanwe.prog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: BbsService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface ProgSurvService {
	
	/** 설문 목록 조회 */
	Map<String, Object> selectSurvMgtList(SearchVO search);
	
	/** 설문 상세 조회 */
	ProgSurvMgtVO selectSysSurvMgt(String id);
	
	/** 설문 상세 조회 */
	ProgSurvMgtVO selectSurvMgt(String id);
	
	/** 설문 등록 */
	int insertSurvMgt(ProgSurvMgtVO progSurvMgtVO);
	
	/** 설문 수정 */
	int updateSurvMgt(ProgSurvMgtVO progSurvMgtVO);
	
	/** 설문 삭제 */
	int deleteSurvMgt(String id);
	
	/** 설문 상태 변경 */
	boolean updateSurvState(int progId, String survState);
	
	/** 설문 문항 및 항목 등록 */
	boolean updateSurvQuesAndItem(Map<String, Object> progSurveyMap);
	
	/** 사용자 체크 후 진행 가능한 설문 목록 출력 */
	List<ProgSurvMgtVO> selectSurveyList();
	
	/** 프로그램 설문정보 조회*/
	ProgSurvMgtVO selectSurvey(int progId);
	
	/** 설문 응답 등록 */
	Map<String, Object> insertSurveyAnswer(Map<String, Object> progSurveyMap);

	/** 설문결과 */
	Map<String, Object> selectSurveyResult(int progId);

	/** 기타 의견 목록 조회 */
	List<String> selectSurvAnswEtcList(int progId, int quesIdx, int itemIdx);
	
	/** 설문결과 */
	Map<String, Object> selectSurveyResultByUser(int progId, String userId);

	/** 최근 진행중인 설문 목록 */
	List<ProgSurvMgtVO> selectRecentSurvList();
	
	/** 설문결과 */
	int executeSurvList(Map<String, Object> progSurveyMap);
	
	/** 설문결과 엑셀다운 */
	void selectSurveyResultForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search, @Param("progId") int progId);

}
