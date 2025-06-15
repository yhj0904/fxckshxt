package kr.co.nanwe.external.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: ExteranlMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("externalMapper")
public interface ExternalMapper {
	
	/** 아이디 체크 */
	int selectUserIdCnt(@Param("userId") String userId);
	
	/** 유저정보 */
	void executeLoginUserInfo(Map<String, Object> paramMap);
	
	/** 유저정보 */
	LoginVO selectLoginUserInfo(@Param("userId") String userId);
	
	/** 사용자 카운트 */
	int selectUserTotCnt(Map<String, Object> paramMap);
	
	/** 사용자 목록 */
	List<UserVO> selectUserList(Map<String, Object> paramMap);
	
	/** 사용자 조회 */
	UserVO selectUserOne(String userId);

	/** 부서 & 학과 목록 */
	List<DeptVO> selectDeptList(Map<String, Object> paramMap);

	/** 앱사용자 동기화 */
	int insertPushAppUserFromViewUser(Map<String, Object> paramMap);

	/** 부서 사용자 목록 조회 */
	List<UserVO> selectUserListByDeptList(Map<String, Object> paramMap);

	/** 최하위 부서 목록 조회 */
	List<DeptVO> selectChildDeptList(Map<String, Object> paramMap);
	
	/** 사용자 아이디 찾기 */
	Map<String, Object> findUserId(Map<String, Object> paramMap);

	/** 사용자 비밀번호 찾기 */
	Map<String, Object> findUserPw(Map<String, Object> paramMap);

	/** 사용자 비밀번호 변경 */
	int updateUserPw(@Param("userId") String userId, @Param("password") String password);

	/** 사용자분류 */
	List<Map<String, Object>> selectUserDvcdList(Map<String, Object> paramMap);

	/** 직업분류 */
	List<Map<String, Object>> selectWorkDvcdList(Map<String, Object> paramMap);

	/** 상태분류 */
	List<Map<String, Object>> selectStatDvcdList(Map<String, Object> paramMap);

	/** 휴대폰 번호 존재하는지 */
	int selectUserPhoneExist(String mbphNo);

	/** 이메일 존재하는지 */
	int selectUserEmailExist(String email);
	
}