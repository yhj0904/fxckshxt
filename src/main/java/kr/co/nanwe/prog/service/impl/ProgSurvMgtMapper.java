package kr.co.nanwe.prog.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.prog.service.ProgSurvMgtVO;
import kr.co.nanwe.prog.service.ProgVO;

/**
 * @Class Name 		: SurvMgtMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("progSurvMgtMapper")
public interface ProgSurvMgtMapper {
	
	/** 전체 Row 개수 */
	int selectSurvMgtTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<ProgSurvMgtVO> selectSurvMgtList(Map<String, Object> paramMap);
	
	/** 프로그램 정보조회 */
	ProgVO selectProg(String progId);
	
	/** 상세 조회 */
	ProgSurvMgtVO selectSurvMgt(String survId);
	
	/** 등록 */
	int insertSurvMgt(ProgSurvMgtVO ProgSurvMgtVO);
	
	/** 수정 */
	int updateSurvMgt(ProgSurvMgtVO ProgSurvMgtVO);
	
	/** 삭제 */
	int deleteSurvMgt(String id);
	
	/** 설문상태 변경 */
	int updateSurvState(ProgSurvMgtVO ProgSurvMgtVO);
	
	/** 사용자 체크 후 진행 가능한 설문 목록 조회 */
	List<ProgSurvMgtVO> selectSurveyList(Map<String, Object> paramMap);
	
	/** 사용자 체크 후 진행 가능한 설문 상세조회 */
	ProgSurvMgtVO selectSurvey(Map<String, Object> paramMap);

	/** 최근 진행중인 설문 목록 */
	List<ProgSurvMgtVO> selectRecentSurvList();
	
	/** 설문지 생성(프로시저) */
	void executeSurvList(Map<String, Object> paramMap);
	
	/** 사용자 미설문개수 */
	int selectCntMySurv(String userId);
}
