package kr.co.nanwe.user.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: SnsUserMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("snsUserMapper")
public interface SnsUserMapper {
	
	/** 회원가입 여부 확인 */
	int checkSnsUserExist(UserVO userVO);

	/** 로그인 정보 조회 */
	LoginVO selectLoginSnsUser(UserVO userVO);

	/** 등록 */
	int insertSnsUser(UserVO userVO);
}