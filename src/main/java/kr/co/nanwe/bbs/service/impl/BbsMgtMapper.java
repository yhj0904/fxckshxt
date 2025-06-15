package kr.co.nanwe.bbs.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.bbs.service.BbsAuthVO;
import kr.co.nanwe.bbs.service.BbsMgtVO;

/**
 * @Class Name 		: BbsMgtMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("bbsMgtMapper")
public interface BbsMgtMapper {
	
	/** 전체 Row 개수 */
	int selectBbsMgtTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<BbsMgtVO> selectBbsMgtList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	BbsMgtVO selectBbsMgt(String code);
	
	/** 등록 */
	int insertBbsMgt(BbsMgtVO bbsMgtVO);
	
	/** 수정 */
	int updateBbsMgt(BbsMgtVO bbsMgtVO);
	
	/** 삭제 */
	int deleteBbsMgt(String code);
	
	/** 코드 중복체크 */
	int selectBbsMgtCodeExist(String code);
	
	/** 삭제시 해당코드의 모든 게시글 삭제 */
	int deleteBbsByBbsCd(String code);
	
	/** 삭제시 해당코드의 모든 권한 삭제 */
	int deleteBbsAuthByBbsCd(String code);
	
	/** 액션별 권한 목록 조회 */
	List<BbsAuthVO> selectBbsAuthListByAction(@Param("code") String code, @Param("action") String action);
	
	/** 권한 조회 */
	BbsAuthVO selectBbsAuth(Map<String, Object> paramMap);
	
	/** 권한 등록 및 수정 */
	int updateBbsAuth(BbsAuthVO bbsAuthVO);
	
	/** 권한 삭제 */
	int deleteBbsAuth(BbsAuthVO bbsAuthVO);
	
	/** 권한 초기화 */
	int resetBbsAuthByBbsCd(String code);
	
	/** 모든 권한이 없는 경우 삭제 */
	int deleteBbsAuthByNoAuth(String code);

	/** 권한코드 삭제시 권한 삭제 */
	int deleteBbsAuthByAuthCd(String authCd);
	
}
