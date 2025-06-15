package kr.co.nanwe.external.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: ExternalService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface ExternalService {

	/** 사용자 목록 */
	Map<String, Object> selectUserList(Map<String, Object> paramMap);

	/** 부서 & 학과 목록  */
	Map<String, Object> selectDeptList(Map<String, Object> paramMap);

	/** 앱사용자 동기화 */
	int insertPushAppUserFromViewUser();

	/** 부서 사용자 목록 조회 */
	List<UserVO> selectUserListByDeptList(List<String> deptCdList);

	/** 최하위 부서 목록 조회 */
	Map<String, Object> selectChildDeptList(Map<String, Object> paramMap);
	
	/** 권한코드 조회 */
	List<Map<String, Object>> selectUserDvcdList();
	
	/** 권한코드 조회 */
	List<Map<String, Object>> selectWorkDvcdList(String userDvcd);
	
	/** 권한코드 조회 */
	List<Map<String, Object>> selectStatDvcdList(String userDvcd, String workDvcd);
	
	/** searchOne  */
	Map<String, Object> searchOne(String sqlId);
	
	/** searchOne  */
	Map<String, Object> searchOne(String sqlId, Object param);
	
	/** searchList  */
	List<Map<String, Object>> searchList(String sqlId);
	
	/** searchList  */
	List<Map<String, Object>> searchList(String sqlId, Object param);

	/** 비밀번호 변경 */
	int updateUserPassword(String userId, String password);

	
}