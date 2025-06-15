package kr.co.nanwe.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: UserService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */


public interface UserService {
	
	/** 목록 조회 */
	Map<String, Object> selectUserList(SearchVO search);
	
	/** 상세 조회 */
	UserVO selectUser(String id);
	
	/** 등록 */
	int insertUser(UserVO userVO);
	
	/** 수정 */
	int updateUser(UserVO userVO);
	
	/** 삭제 */
	int deleteUser(UserVO userVO);
	
	/** 존재하는 아이디인지 체크 */
	boolean selectUserIdExist(String id);
	
	/** 존재하는 아이디인지 체크 */
	boolean selectUserIdExistBySysUser(String userId);
	
	/** 사용가능 유저 목록 조회 */
	List<UserVO> selectUserListByUse();

	/** 아이디 찾기 */
	Map<String, Object> findUserId(Map<String, Object> map);

	/** 아이디 찾기 발송 */
	boolean sendFindUserId(Map<String, Object> map);
	
	/** 비밀번호 찾기 */
	Map<String, Object> findUserPw(Map<String, Object> map);
	
	/** 비밀번호 변경 */
	int updateUserPassword(String userId, String password);

	/** 날짜 지난 회원 삭제 */
	void deleteUserListByAfterSaveMonth(int saveMonth);

	/** 탈퇴 회원 목록 */
	Map<String, Object> selectDelUserList(SearchVO search);
	
	/** 탈퇴사용자 상태 변경 */
	int updateUserDelState(String userId, String deleteState);

	/** 사용자 상세 조회 (마스킹) */
	UserVO selectMaskingUserByLoginUser(LoginVO loginVO);

	/** 핸드폰번호 존재하는지 체크 */
	boolean selectUserPhoneExist(String userId, String mbphNo);

	/** 이메일 존재하는지 체크 */
	boolean selectUserEmailExist(String userId, String email);
	
	/** 사용가능 유저 목록 조회 */
	List<UserVO> selectCnslerUserList();
	
	/** 엑셀 다운을 위한 사용자목록조회 */
	void selectUserListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search);
	
	/**
	 * 만료된 사용자 데이터를 삭제합니다.
	 * @param retentionDate 보관 기간 기준일
	 */
	void deleteExpiredUserData(Date retentionDate) throws Exception;
}