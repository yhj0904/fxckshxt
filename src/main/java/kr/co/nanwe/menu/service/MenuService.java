package kr.co.nanwe.menu.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: MenuService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface MenuService {
	
	/** 현재사용자의 메뉴 정보를 조회한다 */
	Map<String, Object> selectMenuListByUser(SiteVO siteVO, String presentPath, String progCd, String language);
	
	/** 목록 조회 */
	Map<String, Object> selectMenuList(SearchVO search, String menuCd);
	
	/** 상세 조회 */
	MenuVO selectMenu(String menuId);
	
	/** 등록 */
	int insertMenu(MenuVO menuVO, String[] menuAuthArr);
	
	/** 수정 */
	int updateMenu(MenuVO menuVO, String[] menuAuthArr);
	
	/** 삭제 */
	int deleteMenu(String menuId);
	
	/** 존재하는 코드인지 체크 */
	boolean selectMenuIdExist(String menuId);

	/** 프로그램 권한 체크 */
	boolean selectCheckProgramAuth(SiteVO siteVO, String progCd, String actionCd, String uri);

	/** 컨텐츠 목록 조회 */
	List<Map<String, Object>> selectContentList();

	/** 게시판 목록 조회 */
	List<Map<String, Object>> selectBbsMgtList();

	/** 일정 목록 조회 */
	List<Map<String, Object>> selectSchMgtList();
	
	/** 즐겨찾기 목록 조회 */
	List<MenuBookmarkVO> selectMenuBookmarkList();
	
	/** 즐겨찾기 폼 화면 조회 */
	List<MenuBookmarkVO> selectMenuBookmarkListByForm();
	
	/** 등록 */
	int insertMenuBookmark(String[] idArr);

	/** 메뉴권한목록 */
	List<MenuAuthVO> selectMenuAuthList(String menuId);

	/** 시스템 메뉴 */
	Map<String, Object> getSysMenuList(String language);

	/** 현재 메뉴 정보 */
	Map<String, Object> selectPresentMenuInfo(SiteVO siteVO, String presentPath, String progCd, String uri, String language);
	
}
