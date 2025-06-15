package kr.co.nanwe.join.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: TermsService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface TermsService {

	/** 목록 조회 */
	Map<String, Object> selectTermsList(SearchVO search);
	
	/** 상세 조회 */
	TermsVO selectTerms(String termsId);
	
	/** 등록 */
	int insertTerms(TermsVO termsVO);
	
	/** 수정 */
	int updateTerms(TermsVO termsVO);
	
	/** 삭제 */
	int deleteTerms(String contId);
	
	/** 선택 삭제 */
	int deleteCheckedTerms(String checkedSId);

	/** 약관 사용 목록 조회 */
	List<TermsVO> selectTermsListByUse();

	/** 약관 체크 여부 검증 */
	boolean checkTermsList(List<TermsVO> termList);

	/** 회원 */
	int insertTermsJoinUser(JoinVO joinVO);
}