package kr.co.nanwe.push.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: PushGrpService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface PushGrpService {
	
	/** 그룹 목록  */
	Map<String, Object> selectPushGrpMstList(SearchVO search);

	/** 그룹 상세조회  */
	PushGrpMstVO selectPushGrpMst(String grpCd);

	/** 그룹 등록  */
	int insertPushGrpMst(PushGrpMstVO pushGrpMstVO);

	/** 그룹 수정  */
	int updatePushGrpMst(PushGrpMstVO pushGrpMstVO);

	/** 그룹 삭제  */
	int deletePushGrpMst(String grpCd);

	/** 그룹 선택삭제  */
	int deleteCheckedPushGrpMst(String checkedSId);

	/** 사용자 그룹원 목록 (코드목록) */
	List<PushGrpMembVO> selectPushGrpMemListByGrpCdList(List<String> grpCdList);

	/** 로그인 사용자 그룹 전체 목록  */
	List<PushGrpMstVO> selectPushGrpMstAllListByLoginUser();

	/** 로그인 사용자 그룹 목록  */
	Map<String, Object> selectPushGrpMstListByLoginUser(SearchVO search);

	/** 로그인 사용자 그룹 상세조회  */
	PushGrpMstVO selectPushGrpMstByLoginUser(String grpCd);

	/** 로그인 사용자 그룹 선택삭제 */
	int deleteCheckedPushGrpMstByLoginUser(String checkedSId);

	/** 로그인 사용자 그룹원 목록  */
	List<PushGrpMembVO> selectPushGrpMemListByLoginUser(String grpCd);
	
}
