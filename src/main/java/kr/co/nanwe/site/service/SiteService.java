package kr.co.nanwe.site.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: SiteService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface SiteService {
	
	/** 사이트 정보 */
	SiteVO selectSiteByDomain(String domain);

	/** 사이트 카운트 */
	int selectSiteCount();

	/** 관리자 사이트 조회 */
	SiteVO selectSysSiteByDomain(String domain);

	/** 사이트 코드 중복 체크 */
	boolean selectSiteCdExist(String siteCd);

	/** 도메인 중복 체크 */
	boolean selectDomainExist(String domain);

	/** 사이트 목록 */
	Map<String, Object> selectSiteList(SearchVO search);

	/** 사이트 상세조회 */
	SiteVO selectSite(String siteCd);
	
	/** 사이트 등록 */
	int insertSite(SiteVO siteVO);

	/** 사이트 수정 */
	int updateSite(SiteVO siteVO);

	/** 사이트 삭제 */
	int deleteSite(String siteCd);
	
	/** 사이트 코드 목록 */
	List<SiteVO> selectSiteCdList();
	
	/** 사이트 코드 상세조회 */
	SiteVO selectSiteCode(String siteCd);

	/** 사이트 코드 수정 */
	int updateSiteCode(SiteVO siteVO);

	/** 관리자사이트 최초 등록 */
	int insertSysSite(SiteVO siteVO);
	
}
