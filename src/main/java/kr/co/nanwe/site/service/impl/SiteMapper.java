package kr.co.nanwe.site.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: SiteMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("siteMapper")
public interface SiteMapper {

	/** 사이트 조회 */
	SiteVO selectSiteByUrl(String domain);

	/** 사이트 카운트 */
	int selectSiteCount();

	/** 사이트 코드 중복체크  */
	int selectSiteCdExist(String siteCd);

	/** 사이트 총 개수 */
	int selectSiteTotCnt(Map<String, Object> paramMap);
	
	/** 사이트 목록 */
	List<SiteVO> selectSiteList(Map<String, Object> paramMap);
	
	/** 사이트 조회 */
	SiteVO selectSite(String siteCd);

	/** 사이트 등록 */
	int insertSite(SiteVO siteVO);

	/** 사이트 수정 */
	int updateSite(SiteVO siteVO);

	/** 사이트 삭제 */
	int deleteSite(String siteCd);
	
	/** 사이트 코드 목록 */
	List<SiteVO> selectSiteCdList();

	/** 사이트 로고 변경 */
	int updateSiteLogoFile(SiteVO siteVO);
	
}
