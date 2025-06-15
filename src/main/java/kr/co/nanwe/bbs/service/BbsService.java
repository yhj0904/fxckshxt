package kr.co.nanwe.bbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


public interface BbsService {
	
	/** 목록 조회  */
	Map<String, Object> selectBbsList(BbsMgtVO bbsMgtVO, SearchVO search, String category);
	
	/** 상세 조회 */
	BbsVO selectBbs(BbsMgtVO bbsMgtVO, String bbsId);
	
	/** 등록 */
	int insertBbs(BbsMgtVO bbsMgtVO, BbsVO bbsVO);
	
	/** 수정 */
	int updateBbs(BbsMgtVO bbsMgtVO, BbsVO bbsVO);
	
	/** 삭제 */
	int deleteBbs(BbsMgtVO bbsMgtVO,  String bbsId);
	
	/** 선택 삭제 */
	int deleteCheckedBbs(BbsMgtVO bbsMgtVO, String checkedSId);
	
	/** 조회수 증가 */
	int updateBbsViewCnt(String bbsCd, String bbsId);

	/** 공지사항 목록 조회 */
	List<BbsVO> selectBbsNoticeList(BbsMgtVO bbsMgtVO);

	/** 이전글 & 다음글 조회 */
	Map<String, Object> selectNearBbs(BbsMgtVO bbsMgtVO, BbsVO bbsVO, String category);
	
	/** 권한 체크 후 상세조회  */
	Map<String, Object> selectBbsByAuth(BbsMgtVO bbsMgtVO, String bbsId, String action, String bbsPw);

	/** 최근 게시물 */
	List<BbsVO> selectRecentBbsList();
	
	/** 최근 게시물 */
	List<BbsVO> selectRecentBbsListByParam(Map<String, Object> param);
	

	
	/** 목록 조회  */
	Map<String, Object> selectLaborList(BbsMgtVO bbsMgtVO, SearchVO search, String category);
	
	/** 인력풀 엑셀 다운 */
	void selectLaborListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);

	/** 인력풀 엑셀 전체 다운 */
	void selectLaborListForExcelAll(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);
	
	/** wk인력풀 등록 */
	int insertLabor(BbsMgtVO bbsMgtVO, LaborVO laborVO);
	
	/** wk인력풀 상세조회 */
	LaborVO selectLabr(BbsMgtVO bbsMgtVO, String labrId);
	
	/** 권한 체크 후 상세조회  */
	Map<String, Object> selectLabrByAuth(BbsMgtVO bbsMgtVO, String labrId, String action, String pw);
	
	/** 수정 */
	int updateLabr(BbsMgtVO bbsMgtVO, LaborVO laborVO);
	
	/** 삭제 */
	int deleteLabr(BbsMgtVO bbsMgtVO,  String labrId);
	
	/** 선택 삭제 */
	int deleteCheckedLabr(BbsMgtVO bbsMgtVO, String checkedSId);

}
