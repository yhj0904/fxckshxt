package kr.co.nanwe.user.service;

import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: SnsUserService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface SnsUserService {

	boolean checkSnsUserExist(UserVO userVO);

	LoginVO selectLoginSnsUser(UserVO userVO);

	int insertSnsUser(UserVO userVO);
	
}