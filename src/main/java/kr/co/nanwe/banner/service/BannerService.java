package kr.co.nanwe.banner.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: BannerService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface BannerService {
	
	/** 목록 조회 */
	Map<String, Object> selectBannerList(SearchVO search);
	
	/** 상세 조회 */
	BannerVO selectBanner(String id);
	
	/** 등록 */
	int insertBanner(BannerVO bannerVO);
	
	/** 수정 */
	int updateBanner(BannerVO bannerVO);
	
	/** 삭제 */
	int deleteBanner(String id);
	
	/** 선택 삭제 */
	int deleteCheckedBanner(String checkedSId);
	
	/** 팝업 목록 조회 By 코드 */
	List<BannerVO> selectBannerByCode(String code);
}
