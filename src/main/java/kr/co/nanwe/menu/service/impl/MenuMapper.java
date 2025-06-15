package kr.co.nanwe.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.menu.service.MenuVO;

/**
 * @Class Name 		: MenuMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("menuMapper")
public interface MenuMapper {
		
	/** 목록 조회 */
	List<MenuVO> selectMenuList(Map<String, Object> paramMap);
	
	/** 목록 조회 By HI_MENU_ID */
	List<MenuVO> selectMenuListByHiMenuId(String hiCd);
	
	/** 상세 조회 */
	MenuVO selectMenu(String menuId);
	
	/** 등록 */
	int insertMenu(MenuVO menuVO);
	
	/** 수정 */
	int updateMenu(MenuVO menuVO);
	
	/** 삭제 */
	int deleteMenu(String menuId);
	
	/** 삭제 */
	int deleteMenuByMenuCd(String menuCd);

	/** 중복코드 조회 */
	int selectMenuIdExist(String menuId);
	
	/** 메뉴정보조회 (전체메뉴) */
	List<MenuVO> selectMenuListByMenuCd(Map<String, Object> paramMap);

	/** 컨텐츠 목록 조회 */
	List<Map<String, Object>> selectContentList();

	/** 게시판 목록 조회 */
	List<Map<String, Object>> selectBbsMgtList();

	/** 일정 목록 조회 */
	List<Map<String, Object>> selectSchMgtList();

	/** 파일 수정 */
	int updateMenuFile(MenuVO menuVO);

	/** 관리자 메뉴 목록 조회 */
	List<MenuVO> selectSysMenuList(Map<String, Object> paramMap);

	/** 현재메뉴 정보 조회 */
	MenuVO selectPresentMenuInfo(Map<String, Object> paramMap);

	/** 메뉴경로 조회 */
	List<MenuVO> selectMenuPathList(@Param("menuCd") String menuCd, @Param("menuId") String menuId);
	
}
