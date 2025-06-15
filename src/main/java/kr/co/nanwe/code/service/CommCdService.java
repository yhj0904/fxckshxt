package kr.co.nanwe.code.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: CommCdService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface CommCdService {
	
	/** 목록 조회 */
	Map<String, Object> selectCommCdList(SearchVO search);
	
	/** 목록 조회 */
	Map<String, Object> selectWorkCommCdList(String cd);
	
	/** 상세 조회 */
	CommCdVO selectCommCd(String cd);
	
	/** 등록 */
	int insertCommCd(CommCdVO commCdVO);
	
	/** 수정 */
	int updateCommCd(CommCdVO commCdVO);
	
	/** 삭제 */
	int deleteCommCd(String cd);
	
	/** 선택 삭제 */
	int deleteCheckedCommCd(String checkedSId);

	/** 존재하는 코드인지 체크 */
	boolean selectCommCdExist(String cd);

	/** 공통사용자 권한 코드 목록 */
	List<CommCdVO> selectAuthCdList();

	/** 공통사용자 권한 코드 목록 (관리자제외)*/
	List<CommCdVO> selectComUserAuthCdList();
	
	/** 코드 목록 조회 (관리자제외)*/
	List<CommCdVO> selectComAuthList(String cd);
	
	/** 코드 목록 조회 */
	List<CommCdVO> selectCommCdList(String cd);

}
