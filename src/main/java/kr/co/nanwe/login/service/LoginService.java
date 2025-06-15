package kr.co.nanwe.login.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: LoginService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface LoginService {

	/** 사용자 로그인 */
	LoginVO selectLoginUser(String loginId, String loginPw);
	
	/** 관리자 로그인 */
	LoginVO selectLoginSysAdmUser(String loginId, String loginPw);
	
	/** 로그인 로그 */
	int insertLoginLog(LoginVO loginVO);
	
	/** 로그인 로그 목록 */
	Map<String, Object> selectLoginLogList(SearchVO search);
	
	/** 로그인 로그 상세조회 */
	LoginLogVO selectLoginLog(String loginCode);

	/** 최근 로그인 목록 조회 */
	List<LoginLogVO> selectRecentLoginLogList();

	/** 최근 관리자 로그인 목록 조회 */
	List<LoginLogVO> selectRecentSysLoginLogList();

	/** 로그인 로그 엑셀 목록 조회 */
	void selectLoginLogExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);

	/** 패스워드 로그 목록 */
	Map<String, Object> selectPwLogList(SearchVO search);

	/** 패스워드 오류 초기화 */
	int deletePwLog(String userType, String userId);

	/** 패스워드 오류 선택 초기화 */
	int deleteCheckedPwLog(String checkedSId);
	
	/** 관리자 계정 등록 여부 확인 */
	boolean checkSysUserExist();

	/** 최근 접속기록 TOP 5 */
	List<LoginLogVO> selectRecentLoginLogListByLoginId();

}
