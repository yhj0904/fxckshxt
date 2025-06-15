package kr.co.nanwe.cnsler.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.cnsler.service.CnslerVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: CnslerMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("cnslerMapper")
public interface CnslerMapper {
	/** 상담원 관리 START */
	
	/** 상담원 전체 카운트 */
	int selectCnslerTotCnt(Map<String, Object> paramMap);
	/** 상담원 리스트 */
	List<CnslerVO> selectCnslerList(Map<String, Object> paramMap);
	/** 상담원 상세보기 */
	CnslerVO selectCnsler(@Param("cnslerId") String cnslerId);
	/** 상담원등록 */
	int insertCnsler(CnslerVO cnslerVO);
	/** 상담원 수정 */
	int updateCnsler(CnslerVO cnslerVO);
	/** 상담원 삭제 */
	int deleteCnsler(String cnslerId);
	/** 상담원 중복체크 */
	int cnslerDuplChk(String cnslerId);
	/** 상담원 부서 삭제 */
	int deleteCnslerDept(String cnslerId);
	/** 상담원 부서등록 */
	int insertCnslerColg(CnslerVO cnslerVO);
	/** 상담언 담당단과 조회 */
	List<CnslerVO> selectColgByCnsler(String cnslerId);
	/** 상담언 담당부서 조회 */
	List<CnslerVO> selectDeptByCnsler(String cnslerId);
	
	/** 상담원 관리 END */
	/** 상담관리 START */
	
	/** 상담 전체 카운트 */
	int selectCnslTotCnt(Map<String, Object> paramMap);
	/** 상담 전체 리스트 */
	List<Map> selectCnslList(Map<String, Object> paramMap);
	/** 상담 상세조회 */
	CnslerVO selectCnsl(String cnslId);
	/** 상담 상세조회 비밀 */
	Map<String, Object> selectCnslSecret(String cnslId, String pw);
	/** 나의 상담 카운트 */
	int selectMyCnslTotCnt(Map<String, Object> paramMap);
	/** 나의 상담 리스트 */
	List<Map> selectMyCnslList(Map<String, Object> paramMap);
	/** 부서로 상담원 조회 + 가능시간대 조회 */
	List<CnslerVO> selectCnslerByColg(Map<String, Object> paramMap);
	/** 상담 등록 */
	int insertCnsl(CnslerVO cnslerVO);
	/** 상담가능시간 조회 */
	List<CnslerVO> selectCnslerTmList(CnslerVO cnslerVO);
	/** 상담 수정 */
	int updateCnsl(CnslerVO cnslerVO);
	/** 상담상태처리 */
	int updateCnslStat(CnslerVO cnslerVO);
	/** 상담삭제 */
	int deleteCnsl(String cnslId);
	
	/** 상담 엑셀다운 */
	void selectCnslListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler);
	
	/** 상담관리 END */
	
	/** 상담 일정관리 START */
	
	/** 상담일정 총 개수 */
	int selectCnslerSchCnt(Map<String, Object> paramMap);
	/** 상담원별 상담일정 조회 */
	List<Map> selectCnslerSch(Map<String, Object> paramMap);
	/** 상담일정 총개수 */
	int selectCnslerSchTotCnt(Map<String, Object> paramMap);
	/** 상담원별 상담일정 리스트 */
	List<Map> selectCnslerSchList(Map<String, Object> paramMap);
	/** 상담원 날짜별 일정  */
	List<CnslerVO> selectCnslerSchListByCalendar(@Param("params") Map<String, Object> params, @Param("calendar") Map<String, Object> calendar, @Param("cnslerId") String cnslerId);
	/** 상담원 일정등록 */
	int insertCnslerSch(CnslerVO cnslerVO);
	/** 상담일정 중복체크 */
	int selectCnslerSchDupl(CnslerVO cnslerVO);
	/** 상담일정 1개조회 */
	CnslerVO selectCnslerSchDetail(CnslerVO cnslerVO);
	/** 상담일정 삭제 */
	int deleteCnslerSch(CnslerVO cnslerVO);
	/** 상담일정 수정 */
	int updateCnslerSch(CnslerVO cnslerVO);

	List<CnslerVO> selectCnslerDeptCd(Map<String, Object> params);
	
	/** 상담 일정관리 END */
}
