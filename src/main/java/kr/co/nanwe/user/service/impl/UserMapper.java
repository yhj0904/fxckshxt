package kr.co.nanwe.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: UserMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */

@Mapper("userMapper")
public interface UserMapper {
	
	/** 전체 Row 개수 */
	int selectUserTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<UserVO> selectUserList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	UserVO selectUser(String id);
	
	/** 등록 */
	int insertUser(UserVO userVO);
	
	/** 수정 */
	int updateUser(UserVO userVO);
	
	/** 삭제상태 변경 */
	int updateUserDeleteState(UserVO userVO);
	
	/** 삭제 */
	int deleteUser(String id);
	
	/** 존재하는 아이디인지 체크 */
	int selectUserIdExist(String id);

	/** 파일 수정 */
	int updateUserFile(UserVO userVO);
	
	/** 목록 조회 */
	List<UserVO> selectUserListByUse();

	/** 사용자 아이디 찾기 */
	Map<String, Object> findUserId(Map<String, Object> paramMap);

	/** 사용자 비밀번호 찾기 */
	Map<String, Object> findUserPw(Map<String, Object> paramMap);

	/** 비밀번호 변경 */
	int updateUserPassword(UserVO userVO);

	/** 날짜 지난 회원 목록 조회 */
	List<UserVO> selectUserListByAfterSaveMonth(int saveMonth);

	/** 휴대폰 번호 존재하는지 */
	int selectUserPhoneExist(@Param("userId") String userId, @Param("mbphNo") String mbphNo);

	/** 이메일 존재하는지 */
	int selectUserEmailExist(@Param("userId") String userId, @Param("email") String email);
	
	/** 목록 조회 */
	List<UserVO> selectCnslerUserList();
	
	/** 사용자 전체 엑셀 다운로드 */
	void selectUserListForExcel(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);
	
	/**
	 * 만료된 사용자 데이터를 삭제합니다.
	 * @param retentionDate 보관 기간 기준일
	 */
	void deleteExpiredUserData(@Param("retentionDate") Date retentionDate);
}