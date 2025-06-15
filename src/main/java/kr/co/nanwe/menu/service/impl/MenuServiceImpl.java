package kr.co.nanwe.menu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.menu.service.MenuAuthVO;
import kr.co.nanwe.menu.service.MenuBookmarkVO;
import kr.co.nanwe.menu.service.MenuService;
import kr.co.nanwe.menu.service.MenuVO;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: MenuServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("menuService")
public class MenuServiceImpl extends EgovAbstractServiceImpl implements MenuService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	@Resource(name = "menuMapper")
	private MenuMapper menuMapper;
	
	@Resource(name="menuAuthMapper")
    private MenuAuthMapper menuAuthMapper;
	
	@Resource(name = "menuBookmarkMapper")
	private MenuBookmarkMapper menuBookmarkMapper;

	@Override
	public Map<String, Object> selectMenuListByUser(SiteVO siteVO, String presentPath, String progCd, String language) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("language", language);
		paramMap.put("menuCd", siteVO.getSiteCd());
		if(!StringUtil.isNull(progCd)) {
			paramMap.put("progCd", progCd);
		}
		
		String authDvcd = "";
		String userDvcd = "";
		String workDvcd = "";
		String statDvcd = "";
		String deptCd = "";
		
		//로그인 정보
		if(SessionUtil.getLoginUser() != null) {	
			LoginVO loginVO = SessionUtil.getLoginUser();			
			authDvcd = StringUtil.isNullToString(loginVO.getLoginUserType());
			userDvcd = StringUtil.isNullToString(loginVO.getUserDvcd());
			workDvcd = StringUtil.isNullToString(loginVO.getWorkDvcd());
			statDvcd = StringUtil.isNullToString(loginVO.getStatDvcd());	
			deptCd = StringUtil.isNullToString(loginVO.getDeptCd());		
		} else {
			authDvcd = CodeConfig.NO_LOGIN;
			userDvcd = CodeConfig.NO_LOGIN;
			workDvcd = CodeConfig.NO_LOGIN;
			statDvcd = CodeConfig.NO_LOGIN;
		}
		
		if(!SessionUtil.isAdmin()) {
			paramMap.put("authDvcd", authDvcd);
			paramMap.put("userDvcd", userDvcd);
			paramMap.put("workDvcd", workDvcd);
			paramMap.put("statDvcd", statDvcd);
			paramMap.put("deptCd", deptCd);
		}

		//전체 메뉴 조회
		List<MenuVO> allMenu = menuMapper.selectMenuListByMenuCd(paramMap);
		if(allMenu != null && allMenu.size() > 0) {
			for (int i = 0; i < allMenu.size(); i++) {					
				if(allMenu.get(i).getFileNo() > 0) {
					allMenu.get(i).setIconSrc(comFileManager.getImageSrc(allMenu.get(i).getImgPath(), allMenu.get(i).getImgName(), allMenu.get(i).getImgMime()));						
				}
			}
		}
		map.put("GV_GNB", allMenu);
		
		//즐겨찾는 메뉴
		//List<MenuBookmarkVO> bookmarkList = menuBookmarkMapper.selectMenuBookmarkList(loginVO.getLoginId(), language);
		//map.put("COM_BOOKMARK_LIST", bookmarkList);
		
		return map;
	}

	@Override
	@MethodDescription("메뉴 목록 조회")
	public Map<String, Object> selectMenuList(SearchVO search, String menuCd) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("menuCd", menuCd);

		//목록 조회
		List<MenuVO> list = menuMapper.selectMenuList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("메뉴 상세조회")
	public MenuVO selectMenu(String menuId) {
		MenuVO menuVO = menuMapper.selectMenu(menuId);
		if(menuVO != null) {
			
			//이미지 파일 조회
			if(menuVO.getFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(menuVO.getFileNo());
				menuVO.setViewFile(viewFile);
			}
			
			//해당 메뉴의 하위목록 조회
			List<MenuVO> list = menuMapper.selectMenuListByHiMenuId(menuVO.getMenuId());
			menuVO.setList(list);
		}
		return menuVO;
	}

	@Override
	@MethodDescription("메뉴 등록")
	public int insertMenu(MenuVO menuVO, String[] menuAuthArr) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		menuVO.setInptId(userId);
		menuVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = menuMapper.insertMenu(menuVO);
		
		//등록 결과 확인
		if(result > 0) {
			
			//이미지 등록
			if(FileUtil.isFile(menuVO.getUploadFile())) {				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_MENU";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(menuVO.getUploadFile(), uploadPath, "COM_MENU", menuVO.getMenuId());
				if(fileNo > 0) {
					menuVO.setFileNo(fileNo);
					menuMapper.updateMenuFile(menuVO);
				}
			}
			
			//메뉴 권한 등록
			if(menuAuthArr != null) {
				MenuAuthVO menuAuthVO = new MenuAuthVO();
				menuAuthVO.setMenuCd(menuVO.getMenuCd());
				menuAuthVO.setMenuId(menuVO.getMenuId());
				menuAuthVO.setInptId(userId);
				menuAuthVO.setInptIp(userIp);
				for(String authCd : menuAuthArr) {
					menuAuthVO.setAuthCd(authCd);
					menuAuthMapper.insertMenuAuth(menuAuthVO);
				}
			}
			
			return 1;
		}
		
		return 0;
	}

	@Override
	@MethodDescription("메뉴 수정")
	public int updateMenu(MenuVO menuVO, String[] menuAuthArr) {
		
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		menuVO.setModiId(userId);
		menuVO.setModiIp(userIp);
		
		//수정처리전 원본 데이터
		MenuVO orimenuVO = menuMapper.selectMenu(menuVO.getMenuId());
		if(orimenuVO == null) {
			return 0;
		}
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = menuMapper.updateMenu(menuVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			if(FileUtil.isFile(menuVO.getUploadFile())) {
				
				//기존 이미지가 있는 경우 삭제				
				if(orimenuVO.getFileNo() > 0) {
					comFileManager.removeComFileByParent("COM_MENU", orimenuVO.getMenuId());
				}
				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_MENU";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(menuVO.getUploadFile(), uploadPath, "COM_MENU", menuVO.getMenuId());
				if(fileNo > 0) {
					menuVO.setFileNo(fileNo);
					menuMapper.updateMenuFile(menuVO);		
				}
			}
			
			//전체 권한 삭제후 메뉴 권한 등록
			menuAuthMapper.deleteMenuAuthByMenuId(menuVO.getMenuId());		
			if(menuAuthArr != null) {
				MenuAuthVO menuAuthVO = new MenuAuthVO();
				menuAuthVO.setMenuCd(menuVO.getMenuCd());
				menuAuthVO.setMenuId(menuVO.getMenuId());
				menuAuthVO.setInptId(userId);
				menuAuthVO.setInptIp(userIp);
				for(String authCd : menuAuthArr) {
					menuAuthVO.setAuthCd(authCd);
					menuAuthMapper.insertMenuAuth(menuAuthVO);
				}
			}
			
			return 1;
		}
		
		return 0;
	}

	@Override
	@MethodDescription("메뉴 삭제")
	public int deleteMenu(String menuId) {
		int result = menuMapper.deleteMenu(menuId);
		if(result>0) {
			//첨부파일
			comFileManager.removeComFileByParent("COM_MENU", menuId);
			//메뉴 권한 삭제
			menuAuthMapper.deleteMenuAuthByMenuId(menuId);
			//즐겨찾기 삭제
			menuBookmarkMapper.deleteMenuBookmarkByMenuId(menuId);
		}
		return result;
	}

	@Override
	@MethodDescription("메뉴 중복조회")
	public boolean selectMenuIdExist(String menuId) {
		
		//코드가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(menuId)) {
			return true;
		}
		
		int count = menuMapper.selectMenuIdExist(menuId);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean selectCheckProgramAuth(SiteVO siteVO, String progCd, String actionCd, String uri) {
		
		//관리자이면 TRUE
		if(SessionUtil.isAdmin()) {
			return true;
		}
		
		//설문조사 또는 회원가입인 경우
		if(uri.startsWith("/survey") || uri.startsWith("/join")) {
			return true;
		}
		
		//프로그램 권한 체크
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("menuCd", siteVO.getSiteCd());
		paramMap.put("progCd", progCd);
		
		//권한정보
		String authDvcd = "";
		String userDvcd = "";
		String workDvcd = "";
		String statDvcd = "";
		String deptCd = "";
		
		//로그인인 경우
		if(SessionUtil.getLoginUser() != null) {			
			//로그인 정보
			LoginVO loginVO = SessionUtil.getLoginUser();
			authDvcd = StringUtil.isNullToString(loginVO.getLoginUserType());
			userDvcd = StringUtil.isNullToString(loginVO.getUserDvcd());
			workDvcd = StringUtil.isNullToString(loginVO.getWorkDvcd());
			statDvcd = StringUtil.isNullToString(loginVO.getStatDvcd());	
			deptCd = StringUtil.isNullToString(loginVO.getDeptCd());		
		} else {
			authDvcd = CodeConfig.NO_LOGIN;
			userDvcd = CodeConfig.NO_LOGIN;
			workDvcd = CodeConfig.NO_LOGIN;
			statDvcd = CodeConfig.NO_LOGIN;
		}
		
		paramMap.put("authDvcd", authDvcd);
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		paramMap.put("statDvcd", statDvcd);
		paramMap.put("deptCd", deptCd);
		
		//프로그램의 파라미터가 있는 경우
		String progParam = "";		
		switch (progCd) {
			case "CONTENT":
				progParam = uri;
				progParam = progParam.replaceAll("/content/", "").replace(".do", "");
				paramMap.put("progParam", progParam);
				break;
			case "BOARD":
				progParam = uri;
				progParam = progParam.replaceAll("/board/", "").replace(".do", "");
				if(progParam.indexOf("/") > -1) {
					progParam = progParam.substring(0, progParam.indexOf("/"));
				}
				paramMap.put("progParam", progParam);
				break;
			case "SCHEDULE":
				progParam = uri;
				progParam = progParam.replaceAll("/schedule/", "").replace(".do", "");
				if(progParam.indexOf("/") > -1) {
					progParam = progParam.substring(0, progParam.indexOf("/"));
				}
				paramMap.put("progParam", progParam);
				break;
			default:
				break;
		}
		
		int count = menuAuthMapper.checkProgramAuth(paramMap);
		
		if(count > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<Map<String, Object>> selectContentList() {
		return menuMapper.selectContentList();
	}

	@Override
	public List<Map<String, Object>> selectBbsMgtList() {
		return menuMapper.selectBbsMgtList();
	}

	@Override
	public List<Map<String, Object>> selectSchMgtList() {
		return menuMapper.selectSchMgtList();
	}

	@Override
	public List<MenuBookmarkVO> selectMenuBookmarkList() {
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			return null;
		}
		String language = web.getLanguage();
		return menuBookmarkMapper.selectMenuBookmarkList(loginInfo.getLoginId(), language);
	}

	@Override
	public List<MenuBookmarkVO> selectMenuBookmarkListByForm() {
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			return null;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", loginInfo.getLoginId());
		
		String language = web.getLanguage();
		paramMap.put("language", language);
		paramMap.put("menuCd", CodeConfig.MAIN_SITE_CD);
		
		if(!SessionUtil.isAdmin()) {
			String authDvcd = StringUtil.isNullToString(loginInfo.getLoginUserType());
			String userDvcd = StringUtil.isNullToString(loginInfo.getUserDvcd());
			String workDvcd = StringUtil.isNullToString(loginInfo.getWorkDvcd());
			String statDvcd = StringUtil.isNullToString(loginInfo.getStatDvcd());
			String deptCd = StringUtil.isNullToString(loginInfo.getDeptCd());
			
			paramMap.put("authDvcd", authDvcd);
			paramMap.put("userDvcd", userDvcd);
			paramMap.put("workDvcd", workDvcd);
			paramMap.put("statDvcd", statDvcd);
			paramMap.put("deptCd", deptCd);
		}
		
		return menuBookmarkMapper.selectMenuBookmarkListByForm(paramMap);
	}

	@Override
	@MethodDescription("즐겨찾는 메뉴 등록")
	public int insertMenuBookmark(String[] idArr) {
		
		//로그인정보 및 아이피		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			return 0;
		}
		
		String userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
		
		MenuBookmarkVO menuBookmarkVO = new MenuBookmarkVO();
		menuBookmarkVO.setUserId(userId);
		menuBookmarkVO.setInptId(userId);
		menuBookmarkVO.setInptIp(userIp);
		
		int result = menuBookmarkMapper.deleteMenuBookmarkByUserId(userId);
		
		if(idArr != null) {
			for (int i=0; i<idArr.length; i++) {
				menuBookmarkVO.setSortOrd(i);
				menuBookmarkVO.setMenuId(idArr[i]);
				result += menuBookmarkMapper.insertMenuBookmark(menuBookmarkVO); 
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("메뉴 권한 목록")
	public List<MenuAuthVO> selectMenuAuthList(String menuId) {
		return menuAuthMapper.selectMenuAuthListByMenuId(menuId);
	}

	@Override
	public Map<String, Object> getSysMenuList(String language) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("language", language);
		//전체 메뉴 조회
		List<MenuVO> allMenu = menuMapper.selectSysMenuList(paramMap);
		map.put("GV_GNB", allMenu);
		return map;
	}

	@Override
	public Map<String, Object> selectPresentMenuInfo(SiteVO siteVO, String presentPath, String progCd, String uri, String language) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("language", language);
		paramMap.put("menuCd", siteVO.getSiteCd());
		if(!StringUtil.isNull(progCd)) {
			paramMap.put("progCd", progCd);
			//프로그램의 파라미터가 있는 경우
			String progParam = "";		
			switch (progCd) {
				case "CONTENT":
					progParam = uri;
					progParam = progParam.replaceAll("/content/", "").replace(".do", "");
					paramMap.put("progParam", progParam);
					break;
				case "BOARD":
					progParam = uri;
					progParam = progParam.replaceAll("/board/", "").replace(".do", "");
					if(progParam.indexOf("/") > -1) {
						progParam = progParam.substring(0, progParam.indexOf("/"));
					}
					paramMap.put("progParam", progParam);
					break;
				case "SCHEDULE":
					progParam = uri;
					progParam = progParam.replaceAll("/schedule/", "").replace(".do", "");
					if(progParam.indexOf("/") > -1) {
						progParam = progParam.substring(0, progParam.indexOf("/"));
					}
					paramMap.put("progParam", progParam);
					break;
				default:
					break;
			}
		}
		
		//현재 메뉴 정보
		MenuVO menuVO = menuMapper.selectPresentMenuInfo(paramMap);
		
		//메뉴 경로 정보
		List<MenuVO> menuPath = null;
		
		if(menuVO != null) {
			//이미지 파일 조회
			if(menuVO.getFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(menuVO.getFileNo());
				menuVO.setViewFile(viewFile);
			}			
			//메뉴 경로 정보
			MenuVO home = new MenuVO();
			home.setMenuNm("홈");
			home.setMenuEngNm("Home");
			home.setMenuLink("/");
			menuPath = menuMapper.selectMenuPathList(menuVO.getMenuCd(), menuVO.getMenuId());
			if(menuPath != null && menuPath.size() > 0) {				
				menuPath.add(0, home);		
			} else {
				menuPath = new ArrayList<MenuVO>();
				menuPath.add(home);
				menuPath.add(menuVO);
			}
			MenuVO topMenu = menuPath.get(1);
			String hiMenuCd = topMenu.getMenuCd();
			String hiMenuId = topMenu.getMenuId();
			paramMap.put("menuCd", hiMenuCd);
			paramMap.put("hiMenuId", hiMenuId);
			
			//사이드 메뉴 정보			
			String authDvcd = "";
			String userDvcd = "";
			String workDvcd = "";
			String statDvcd = "";
			String deptCd = "";
			
			//로그인 정보
			if(SessionUtil.getLoginUser() != null) {	
				LoginVO loginVO = SessionUtil.getLoginUser();			
				authDvcd = StringUtil.isNullToString(loginVO.getLoginUserType());
				userDvcd = StringUtil.isNullToString(loginVO.getUserDvcd());
				workDvcd = StringUtil.isNullToString(loginVO.getWorkDvcd());
				statDvcd = StringUtil.isNullToString(loginVO.getStatDvcd());	
				deptCd = StringUtil.isNullToString(loginVO.getDeptCd());		
			} else {
				authDvcd = CodeConfig.NO_LOGIN;
				userDvcd = CodeConfig.NO_LOGIN;
				workDvcd = CodeConfig.NO_LOGIN;
				statDvcd = CodeConfig.NO_LOGIN;
			}
			
			if(!SessionUtil.isAdmin()) {
				paramMap.put("authDvcd", authDvcd);
				paramMap.put("userDvcd", userDvcd);
				paramMap.put("workDvcd", workDvcd);
				paramMap.put("statDvcd", statDvcd);
				paramMap.put("deptCd", deptCd);
			}
			List<MenuVO> sideMenu = menuMapper.selectMenuListByMenuCd(paramMap);
			System.out.println("sideMenu ===>          "+sideMenu);
			if(sideMenu != null) {
				sideMenu.add(0, topMenu);
				map.put("sideMenu", sideMenu);
			}
			
			
		} else {
			
			menuVO = new MenuVO();
			String menuNm = "";
			String menuEngNm = "";
			
			//로그인 관련인경우
			if (uri.startsWith("/login")) {
				menuNm = "로그인";
				menuEngNm = "Login";
			} else if(StringUtil.isEqual(progCd, "JOIN")) {
				menuNm = "회원가입";
				menuEngNm = "Join";
			} else if(StringUtil.isEqual(progCd, "USER")) {
				menuNm = "마이페이지";
				menuEngNm = "My Page";
			} else if(StringUtil.isEqual(progCd, "SURVEY")) {
				menuNm = "설문조사";
				menuEngNm = "Survey";
			} else if(uri.startsWith("/find")) {
				menuNm = "아이디 & 비밀번호찾기";
				menuEngNm = "Find Id & Password";
			} else {
				menuVO = null;
			}
			
			if(menuVO != null) {
				menuVO.setMenuNm(menuNm);
				menuVO.setMenuEngNm(menuEngNm);
				
				//메뉴 경로 정보
				MenuVO home = new MenuVO();
				home.setMenuNm("홈");
				home.setMenuEngNm("Home");
				home.setMenuLink("/");
				menuPath = new ArrayList<MenuVO>();
				menuPath.add(home);
				menuPath.add(menuVO);
			}	
		}
		map.put("presentMenu", menuVO);
		
		if(menuPath != null) {
			map.put("menuPath", menuPath);
		}
		
		return map;
	}
}
