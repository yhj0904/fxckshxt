package kr.co.nanwe.push.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.push.service.PushGrpMembVO;

/**
 * @Class Name 		: PushGrpMembMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("pushGrpMembMapper")
public interface PushGrpMembMapper {
	
	/** 그룹원 목록 조회 */
	List<PushGrpMembVO> selectPushGrpMemListByGrpCd(String grpCd);
	
	/** 그룹원 등록 */
	int insertPushGrpMemb(PushGrpMembVO pushGrpMembVO);

	/** 그룹원 삭제 */
	int deletePushGrpMemb(@Param("grpCd") String grpCd, @Param("grpMembId") String grpMembId);	

	/** 그룹원 목록 삭제 */
	int deletePushGrpMembByGrpCd(String grpCd);

	/** 그룹원 목록 조회 (그룹코드리스트) */
	List<PushGrpMembVO> selectPushGrpMemListByGrpCdList(Map<String, Object> paramMap);

}
