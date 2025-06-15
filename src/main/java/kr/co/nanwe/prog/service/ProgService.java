package kr.co.nanwe.prog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: ProgService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */


public interface ProgService {
	
	/** 목록 조회  */
	Map<String, Object> selectProgList(ProgSearchVO search);
	
	/** 등록 */
	int insertProg(ProgVO progVO); 
	
	/** 수정 */
	int updateProg(ProgVO progVO); 
	
	/** 상세조회 */
	ProgVO selectProg(int progId);
	
	/** 상세조회 - UPDATE BEFORE */
	ProgVO selectProgUpBfView(int progId);
	
	/** 삭제 */
	int deleteProg(int progId);
	
	/** 선택 삭제 */
	int deleteCheckedProg(String checkedSId) ;
	
	/** 프로그램ID 조회 */
	String selectProgId();
	
	
	/** 사용자 - 목록 조회  */
	Map<String, Object> selectProgUserViewList(ProgSearchVO search);
	
	/** 사용자 - 상세조회 */
	ProgVO selectProgUserView(int progId);
	
	/** 사용자 - 프로그램 신청 */
	int insertProgUser(ProgUserVO progUserVO); 
	
	/** 사용자 - 프로그램 신청 전 중복검사 */
	//int selectApplDuplCnt(ProgUserVO progUserVO); 
	
	/** 사용자 - 신청인원조회 */
	int selectApplUserTotCnt(int progId); 
	
	
	/** 목록 조회 - 프로그램 신청자 */
	Map<String, Object> selectProgApplUserList(ProgSearchVO search, int progId);

	/** 상세조회 - 프로그램 신청자 */
	Map<String, Object> selectProgApplUser(int progId, String userId);
	
	/** 상세조회 - 프로그램 신청자 */
	Map<String, Object> selectProgApplChkUser(int progId, String userId);
		
	/** 선택 삭제 - 프로그램 신청자*/
	int deleteCheckedProgApplUser(int progId, String checkedSId) ;
	
	/** 이수 /미이수 선택 처리 - 프로그램 수강자*/
	int updateProgComplUser(int progId, String complCd, String checkedSId) ;
	
	
	/** 사용자 프로그램 신청 내역 */
	Map<String, Object> selectMyProgList(ProgSearchVO search);
	
	/** 사용자 프로그램 신청취소 / 재신청 */
	int updateProgUser(int progId, String progReqstCd);
	
	/** 사용자 프로그램 신청취소  */
	int updateProgCancelUser(String checkedSId, String progReqstCd);
	
	
	/** 목록 조회 - 메인화면 */
	List<ProgVO> selectProgUserMainList(Map<String, Object> paramMap);
	
	/** 신청자 엑셀다운로드 */
	void selectExcelProgApplUserList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search, int progId);
}
