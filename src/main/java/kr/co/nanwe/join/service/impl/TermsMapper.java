package kr.co.nanwe.join.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.join.service.TermsUserVO;
import kr.co.nanwe.join.service.TermsVO;

/**
 * @Class Name 		: TermsMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("termsMapper")
public interface TermsMapper {
	
	/** 전체 Row 개수 */
	int selectTermsTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<TermsVO> selectTermsList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	TermsVO selectTerms(String termsId);
	
	/** 등록 */
	int insertTerms(TermsVO termsVO);
	
	/** 수정 */
	int updateTerms(TermsVO termsVO);
	
	/** 삭제 */
	int deleteTerms(String termsId);
	
	/** 약관 동의 목록 */
	List<TermsUserVO> selectTermsUserList(String termsId);

	/** 약관 동의 삭제 */
	int deleteTermsUserList(String termsId);
	
	/**사용자 약관 삭제 */
	int deleteTermsUser(@Param("userType") String userType, @Param("userId") String userId);

	/**사용자 약관 삭제 */
	List<TermsVO> selectTermsListByUse();

	/**사용자 약관 등록 */
	int insertTermsUser(TermsUserVO termsUserVO);
	
}