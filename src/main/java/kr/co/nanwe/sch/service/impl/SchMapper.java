package kr.co.nanwe.sch.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.sch.service.SchVO;

/**
 * @Class Name 		: SchMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("schMapper")
public interface SchMapper {
	
	/** 전체 Row 개수 */
	int selectSchTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<SchVO> selectSchList(Map<String, Object> paramMap);
	
	/** 목록 조회 
	 * @param map */
	List<SchVO> selectSchListByCalendar(@Param("params") Map<String, Object> params, @Param("calendar") Map<String, Object> calendar);
	
	/** 상세 조회 */
	SchVO selectSch(@Param("schCd") String schCd, @Param("schId") String schId);
	
	/** 등록 */
	int insertSch(SchVO schMgtVO);
	
	/** 수정 */
	int updateSch(SchVO schMgtVO);
	
	/** 삭제 */
	int deleteSch(@Param("schCd") String schCd, @Param("schId") String schId);

	/** 조회수 증가 */
	int updateSchViewCnt(@Param("schCd") String schCd, @Param("schId") String schId);

	/** 최근 일정 조회 */
	List<SchVO> selectRecentSchListByParam(Map<String, Object> param);
	
	/** 프로그램 일정 및 일정 조회 
	 * @param map */
	List<SchVO> selectProgSchListByCalendar(@Param("params") Map<String, Object> params, @Param("calendar") Map<String, Object> calendar);
	
}
