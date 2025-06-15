package kr.co.nanwe.code.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.auth.service.UserAuthVO;
import kr.co.nanwe.code.service.CommCdVO;

/**
 * @Class Name 		: CommCdMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("commCdMapper")
public interface CommCdMapper {
		
	/** 목록 조회 */
	List<CommCdVO> selectCommCdList(Map<String, Object> paramMap);		
	
	/** 목록 조회 By HI_CD */
	List<CommCdVO> selectCommCdListByHiCd(String hiCd);
	
	/** 상세 조회 */
	CommCdVO selectCommCd(String cd);
	
	/** 등록 */
	int insertCommCd(CommCdVO commCdVO);
	
	/** 수정 */
	int updateCommCd(CommCdVO commCdVO);
	
	/** 삭제 */
	int deleteCommCd(String cd);

	/** 중복코드 조회 */
	int selectCommCdExist(String cd);
	
	/** 신분권한 조회 */
	List<UserAuthVO> selectUserAuthList(Map<String, Object> paramMap);
	
}
