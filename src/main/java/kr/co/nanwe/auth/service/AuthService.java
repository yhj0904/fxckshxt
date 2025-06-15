package kr.co.nanwe.auth.service;

import java.util.List;

/**
 * @Class Name 		: ExternalService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface AuthService {
	
	/** 목록 조회 */
	List<AuthVO> selectAuthList();
	
	/** 목록 조회 */
	List<AuthVO> selectAuthUseList();

	/** 상세 조회 */
	AuthVO selectAuth(String authCd);
	
	/** 권한 맵 목록 조회 */
	List<AuthMapVO> selectAuthMapList(String authCd);

	/** 코드 중복체크 */
	boolean selectAuthCdExist(String authCd);

	/** 신분권한 등록 */
	int insertAuth(AuthVO authVO);

	/** 신분권한 수정 */
	int updateAuth(AuthVO authVO);

	/** 신분권한 삭제 */
	int deleteAuth(String authCd);
	
}
