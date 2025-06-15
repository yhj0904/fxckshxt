package kr.co.nanwe.push.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.push.service.PushGrpMstVO;

/**
 * @Class Name 		: PushGrpMstMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("pushGrpMstMapper")
public interface PushGrpMstMapper {

	/** 그룹 총 카운트  */
	int selectPushGrpMstTotCnt(Map<String, Object> paramMap);

	/** 그룹 목록  */
	List<PushGrpMstVO> selectPushGrpMstList(Map<String, Object> paramMap);

	/** 그룹 조회  */
	PushGrpMstVO selectPushGrpMst(String grpCd);

	/** 그룹 등록  */
	int insertPushGrpMst(PushGrpMstVO pushGrpMstVO);

	/** 그룹 수정  */
	int updatePushGrpMst(PushGrpMstVO pushGrpMstVO);

	/** 그룹 삭제  */
	int deletePushGrpMst(String id);

	/** 로그인 사용자 그룹 전체 목록  */
	List<PushGrpMstVO> selectPushGrpMstAllListByUser(String grpCreUser);

	/** 로그인 사용자 그룹 상세 조회  */
	PushGrpMstVO selectPushGrpMstByUser(@Param("grpCreUser") String grpCreUser, @Param("grpCd") String grpCd);

	/** 로그인 사용자 그룹 선택삭제 */
	int deletePushGrpMstByLoginUser(@Param("grpCreUser") String grpCreUser, @Param("grpCd") String grpCd);

}
