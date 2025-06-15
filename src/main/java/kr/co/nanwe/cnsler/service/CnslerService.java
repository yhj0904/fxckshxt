package kr.co.nanwe.cnsler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nanwe.bbs.service.BbsMgtVO;
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


public interface CnslerService {
	/** 상담원 관리 START */
	
	/** 상담원 목록 조회  */
	Map<String, Object> selectCnslerList(SearchVO search);
	/** 상담원 상세 조회 */
	CnslerVO selectCnsler(String cnslerId);
	/** 상담원 등록 */
	int insertCnsler(CnslerVO cnslerVO);
	/** 상담원 수정 */
	int updateCnsler(CnslerVO cnslerVO);
	/** 상담원 삭제 */
	int deleteCnsler(String cnslerId);
	/** 상담원 선택삭제 */
	int deleteCheckCnsler(String checkedSId);
	/** 상담원 부서등록 */
	int insertCnslerColg(CnslerVO cnslerVO, String colg_values, String dept_values, String cnslerId);
	/** 상담원 단과조회 */
	List<CnslerVO> selectColgByCnsler(String cnslerId);
	/** 상담원 부서조회 */
	List<CnslerVO> selectDeptByCnsler(String cnslerId);
	
	/** 상담원 관리 END */
	/** 상담 관리 START */
	
	/** 상담 전체 리스트 */
	Map<String, Object> selectCnslList(SearchVO search);
	/** 상담선택 */
	CnslerVO selectCnsl(String cnslId);
	/** 상담선택 비밀 */
	Map<String, Object> selectCnslSecret(String cnslId, String pw);
	/** 상담 전체 리스트 */
	Map<String, Object> selectMyCnslList(SearchVO search, String userId);
	/** 부서로 상담원 조회 + 상담원이 등록한 일정 */
	List<CnslerVO> selectCnslerByColg(SearchVO search, String cnslColgCd, String schDt);
	/** 상담등록 */
	int insertCnsl(CnslerVO cnslerVO);
	/** 상담 수정 */
	int updateCnsl(CnslerVO cnslerVO);
	/** 상담 상태 변경 */
	int updateCnslStat(CnslerVO cnslerVO, String checkedSId);
	/** 상담삭제 */
	int deleteCheckCnsl(String checkedSId);
	/** 상담신청자 엑셀다운 */
	void selectCnslListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler);
	
	/** 상담 관리 END  */
	/** 상담일정 관리 START */
	
	/** 상담원별 상담일정 조회 */
	Map<String, Object> selectCnslerSchList(SearchVO search, String cnslerId, String date, String option);
	/** 상담원 일정등록 */
	int insertCnslerSch(CnslerVO cnslerVO);
	/** 상담가능시간 조회 */
	List<CnslerVO> selectCnslerTmList(CnslerVO cnslerVO);
	/** 상담일정 중복체크 */
	int selectCnslerSchDupl(CnslerVO cnslerVO);
	/** 상담일정 1개조회 */
	CnslerVO selectCnslerSchDetail(CnslerVO cnslerVO);
	/** 상담일정 삭제 */
	int deleteCnslerSch(CnslerVO cnslerVO);
	/** 상담일정 수정 */
	int updateCnslerSch(CnslerVO cnslerVO);

	List<CnslerVO> selectCnslerDeptCd(Map<String, Object> params);
	
	/** 상담일정 관리 END */
}
