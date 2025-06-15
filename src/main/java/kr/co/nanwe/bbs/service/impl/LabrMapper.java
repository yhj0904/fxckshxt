package kr.co.nanwe.bbs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.bbs.service.LaborVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: LabrMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.11.03		신한나			최초생성
 */

@Mapper("labrMapper")
public interface LabrMapper {
	/** 전체 Row 개수 */
	int selectLaborTotCnt(Map<String, Object> paramMap);
	
	/** wk인력풀 등록 */
	int insertLabor(LaborVO laborVO);
	
	/** wk인력풀 상세조회 */
	LaborVO selectLabr(@Param("labrId") String labrId);
	
	/** 수정 */
	int updateLabr(LaborVO laborVO);
	
	/** 삭제 */
	int deleteLabr(@Param("labrId") String labrId);
	
	/** 목록 조회 */
	List<LaborVO> selectLaborList(Map<String, Object> paramMap);
	
	/** 인력풀 엑셀다운 */
	void selectLaborListForExcel(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);

	/** 인력풀 엑셀 전체 다운 */
	void selectLaborListForExcelAll(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);

}
