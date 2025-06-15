package kr.co.nanwe.banner.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.banner.service.BannerVO;

/**
 * @Class Name 		: BannerMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("bannerMapper")
public interface BannerMapper {
	
	/** 전체 Row 개수 */
	int selectBannerTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<BannerVO> selectBannerList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	BannerVO selectBanner(String bannerId);
	
	/** 등록 */
	int insertBanner(BannerVO bannerVO);
	
	/** 수정 */
	int updateBanner(BannerVO bannerVO);
	
	/** 삭제 */
	int deleteBanner(String bannerId);
	
	/** 파일 수정 */
	int updateBannerFile(BannerVO bannerVO);
	
	/** 팝업 목록 조회 By 코드 */
	List<BannerVO> selectBannerByCode(String bannerCd);
	
}
