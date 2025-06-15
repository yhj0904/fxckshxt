package kr.co.nanwe.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.auth.service.AuthDeptVO;
import kr.co.nanwe.auth.service.AuthMapVO;
import kr.co.nanwe.auth.service.AuthVO;

/**
 * @Class Name 		: AuthMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("authMapper")
public interface AuthMapper {
	
	/** 목록조회 */
	List<AuthVO> selectAuthList(Map<String, Object> paramMap);
	
	
	/** 상세조회 */
	AuthVO selectAuth(String authCd);
	
	/** 코드 중복체크 */
	int selectAuthCdExist(String authCd);
	
	/** 등록 */
	int insertAuth(AuthVO authVO);
	
	/** 수정 */
	int updateAuth(AuthVO authVO);
	
	/** 삭제 */
	int deleteAuth(String authCd);
	
	/** 권한 맵핑 목록조회 */
	List<AuthMapVO> selectAuthMapList(String authCd);
	
	/** 권한 맵핑 등록 */
	int insertAuthMap(AuthMapVO authMapVO);
	
	/** 권한 맵핑테이블 삭제 */
	int deleteAuthMapByAuthCd(String authCd);
	
	/** 부서 권한 맵핑 목록조회 */
	List<AuthDeptVO> selectAuthDeptList(String authCd);

	/** 부서 권한 맵핑 등록 */
	int insertAuthDept(AuthDeptVO authDeptVO);
	
	/** 부서 권한 맵핑테이블 삭제 */
	int deleteAuthDeptByAuthCd(String authCd);

	/** 부서 삭제시 권한 삭제 */
	int deleteAuthDeptByDeptCd(@Param("authDvcd") String authDvcd, @Param("deptCd") String deptCd);
	
}
