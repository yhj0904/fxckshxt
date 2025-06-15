package kr.co.nanwe.login.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.login.service.LoginLogVO;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: LoginMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("loginMapper")
public interface LoginMapper {

	/** SALT KEY 조회 */
	String selectLoginUserSaltKey(String loginId);

	/** 로그인 정보 조회 */
	LoginVO selectLoginUser(@Param("loginId") String loginId, @Param("loginPw") String loginPw);
	
	/** 로그인 아이피 조회 */
	int selectLoginUserIp(@Param("loginId") String loginId, @Param("loginIp") String loginIp);

	/** 로그인 시각 변경 */
	int updateLoginUserDttm(LoginVO loginVO);

	/** 로그인 로그 기록 */
	int insertLoginUserLog(LoginVO loginVO);
	
	/** 로그인 로그 카운트 */
	int selectLoginLogTotCnt(Map<String, Object> paramMap);
	
	/** 로그인 로그 목록 */
	List<LoginLogVO> selectLoginLogList(Map<String, Object> paramMap);
	
	/** 로그인 로그 조회 */
	LoginLogVO selectLoginLog(String loginCode);

	/** 최근 로그인 목록 조회 */
	List<LoginLogVO> selectRecentLoginLogList(String sysUserCd);

	/** 최근 관리자 로그인 목록 조회 */
	List<LoginLogVO> selectRecentSysLoginLogList(String sysUserCd);

	/** 엑셀 목록 조회 (*필수 return void) */
	void selectLoginLogExcelList(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);
	
	/** 비밀번호 오류 로그 등록 */
	int insertPwLog(LoginVO loginVO);
	
	/** 비밀번호 오류 로그 등록 */
	int selectPwLogCnt(LoginVO loginVO);
	
	/** 비밀번호 오류 로그 삭제 */
	int deletePwLog(LoginVO loginVO);
	
	/** 비밀번호 오류 로그 카운트 */
	int selectPwLogTotCnt(Map<String, Object> paramMap);

	/** 비밀번호 오류 로그 목록 */
	List<Map<String, Object>> selectPwLogList(Map<String, Object> paramMap);
	
	/** 비밀번호 오류 로그 상세조회 */
	Map<String, Object> selectPwLog(String userId);
	
	/** 관리자 숫자 확인 */
	int selectSysAdminCount(String sysUserCd);

	/** 최근 로그인 */
	List<LoginLogVO> selectRecentLoginLogListByLoginId(@Param("loginUserType") String loginUserType, @Param("loginId") String loginId);
}
