package kr.co.nanwe.prog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.prog.service.ProgSurvItemVO;

/**
 * @Class Name 		: SurvItemMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("progSurvItemMapper")
public interface ProgSurvItemMapper {
	
	/** 관리자 목록 조회 */
	List<ProgSurvItemVO> selectSysSurvItemList(@Param("survId") String survId, @Param("quesIdx") int quesIdx, @Param("useYn") String useYn);
	
	/** 목록 조회 */
	List<ProgSurvItemVO> selectSurvItemList(@Param("progId") int progId, @Param("quesIdx") int quesIdx, @Param("useYn") String useYn);
	
	/** 등록 */
	int insertSurvItem(ProgSurvItemVO ProgSurvItemVO);
	
	/** 수정 */
	int updateSurvItem(ProgSurvItemVO ProgSurvItemVO);
	
	/** 삭제 */
	int deleteSurvItem(ProgSurvItemVO ProgSurvItemVO);
	
	/** 설문삭제시 항목삭제 */
	int deleteSurvItemBySurvId(@Param("survId") String id);
	
	/** 문항삭제시 항목삭제 */
	int deleteSurvItemByQuesIdx(@Param("survId") String survId, @Param("quesIdx") int quesIdx);
	
	/** 설문결과조회 */
	List<ProgSurvItemVO> selectSurvItemResult(@Param("progId") int progId, @Param("quesIdx") int quesIdx, @Param("totalCount") int totalCount);
	
	/** 프로그램 id에 따른 설문 항목 개수 체크 */
	int selectProgSurvItemCnt(@Param("progId") String string);
	
	/** 설문결과 엑셀다운로드 */
	void selectSurvItemResultForExcel(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler, @Param("progId") int progId, @Param("totalCnt") int totalCnt);
	
	/** 엑셀결과용 참여자 카운트 */
	int selectSurvItemTotCntForExcel(@Param("progId") int progId);
}
