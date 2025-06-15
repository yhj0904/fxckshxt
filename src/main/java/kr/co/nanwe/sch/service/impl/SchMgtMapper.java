package kr.co.nanwe.sch.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.sch.service.SchAuthVO;
import kr.co.nanwe.sch.service.SchMgtVO;

/**
 * @Class Name 		: SchMgtMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("schMgtMapper")
public interface SchMgtMapper {
	
	/** 전체 Row 개수 */
	int selectSchMgtTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<SchMgtVO> selectSchMgtList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	SchMgtVO selectSchMgt(String code);
	
	/** 등록 */
	int insertSchMgt(SchMgtVO schMgtVO);
	
	/** 수정 */
	int updateSchMgt(SchMgtVO schMgtVO);
	
	/** 삭제 */
	int deleteSchMgt(String code);
	
	/** 코드 중복체크 */
	int selectSchMgtCodeExist(String code);
	
	/** 삭제시 해당코드의 모든 일정 삭제 */
	int deleteSchBySchCd(String code);
	
	/** 삭제시 해당코드의 모든 권한 삭제 */
	int deleteSchAuthBySchCd(String code);
	
	/** 액션별 권한 목록 조회 */
	List<SchAuthVO> selectSchAuthListByAction(@Param("code") String code, @Param("action") String action);
	
	/** 권한 조회 */
	SchAuthVO selectSchAuth(Map<String, Object> paramMap);
	
	/** 권한 등록 및 수정 */
	int updateSchAuth(SchAuthVO schAuthVO);
	
	/** 권한 삭제 */
	int deleteSchAuth(SchAuthVO schAuthVO);
	
	/** 권한 초기화 */
	int resetSchAuthBySchCd(String code);
	
	/** 모든 권한이 없는 경우 삭제 */
	int deleteSchAuthByNoAuth(String code);

	/** 권한코드 삭제시 권한 삭제 */
	int deleteSchAuthByAuthCd(String authCd);
	
}
