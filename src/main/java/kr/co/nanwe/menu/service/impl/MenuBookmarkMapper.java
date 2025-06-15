package kr.co.nanwe.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.menu.service.MenuBookmarkVO;

/**
 * @Class Name 		: MenuBookmarkMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("menuBookmarkMapper")
public interface MenuBookmarkMapper {
		
	/** 목록 조회 */
	List<MenuBookmarkVO> selectMenuBookmarkList(@Param("userId") String userId, @Param("language") String language);
	
	/** 목록 조회 */
	List<MenuBookmarkVO> selectMenuBookmarkListByForm(Map<String, Object> paramMap);
	
	/** 등록 */
	int insertMenuBookmark(MenuBookmarkVO menuBookmarkVO);
	
	/** 메뉴 삭제시 BOOKMARK 삭제 */
	int deleteMenuBookmarkByMenuId(String menuId);
	
	/** 사용자 BOOKMARK 삭제 */
	int deleteMenuBookmarkByUserId(String userId);

	/** 사이트 삭제시 BOOKMARK 삭제 */
	int deleteMenuBookmarkByMenuCd(String menuCd);
	
}
