package kr.co.nanwe.site.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CodeMirrorManager;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.menu.service.impl.MenuAuthMapper;
import kr.co.nanwe.menu.service.impl.MenuBookmarkMapper;
import kr.co.nanwe.menu.service.impl.MenuMapper;
import kr.co.nanwe.site.service.DomainVO;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: SiteServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("siteService")
public class SiteServiceImpl extends EgovAbstractServiceImpl implements SiteService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Mapper */
	@Resource(name="siteMapper")
    private SiteMapper siteMapper;
	
	/** Mapper */
	@Resource(name="domainMapper")
    private DomainMapper domainMapper;
	
	/** Mapper */
	@Resource(name="menuMapper")
    private MenuMapper menuMapper;
	
	@Resource(name="menuAuthMapper")
    private MenuAuthMapper menuAuthMapper;
	
	@Resource(name = "menuBookmarkMapper")
	private MenuBookmarkMapper menuBookmarkMapper;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	@Resource(name = "fileUpload")
	private FileUpload fileUpload;
	
	@Resource(name = "codeMirrorManager")
	private CodeMirrorManager codeMirrorManager;

	@Override
	@MethodDescription("사이트 정보")
	public SiteVO selectSiteByDomain(String domain) {
		if(domain == null) {
			return null;
		}
		SiteVO siteVO = siteMapper.selectSiteByUrl(domain);
		if(siteVO != null) {
			//이미지 파일 조회
			if(siteVO.getSiteLogo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(siteVO.getSiteLogo());
				siteVO.setViewFile(viewFile);
			}
		}		
		return siteVO;
	}

	@Override
	@MethodDescription("사이트 카운트")
	public int selectSiteCount() {
		return siteMapper.selectSiteCount();
	}

	@Override
	@MethodDescription("관리자사이트 조회")
	public SiteVO selectSysSiteByDomain(String domain) {
		if(domain == null) {
			return null;
		}
		SiteVO siteVO = siteMapper.selectSiteByUrl(domain);
		if(siteVO == null) {
			return null;
		}
		if(siteVO.getSysAccessYn() == null || !"Y".equals(siteVO.getSysAccessYn())) {
			return null;
		}
		//이미지 파일 조회
		if(siteVO.getSiteLogo() != 0) {
			ViewFileVO viewFile = comFileManager.getViewFile(siteVO.getSiteLogo());
			siteVO.setViewFile(viewFile);
		}
		return siteVO;
	}

	@Override
	@MethodDescription("사이트 코드 중복 체크")
	public boolean selectSiteCdExist(String siteCd) {
		//코드가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(siteCd)) {
			return true;
		}
		
		siteCd = siteCd.toUpperCase();
		
		//사용할수 없는 사이트 코드인경우 중복
		if(CodeConfig.SYS_SITE_CD.equals(siteCd)) {
			return true;
		}
		
		int count = siteMapper.selectSiteCdExist(siteCd);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("도메인 중복 체크")
	public boolean selectDomainExist(String domain) {
		//도메인이 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(domain)) {
			return true;
		}
		
		int count = domainMapper.selectDomainExist(domain);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("사이트 목록")
	public Map<String, Object> selectSiteList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = siteMapper.selectSiteTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<SiteVO> list = siteMapper.selectSiteList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("사이트 상세조회")
	public SiteVO selectSite(String siteCd) {
		SiteVO siteVO = siteMapper.selectSite(siteCd);
		if(siteVO != null) {
			siteVO.setDomainList(domainMapper.selectDomainList(siteCd));
			
			//이미지 파일 조회
			if(siteVO.getSiteLogo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(siteVO.getSiteLogo());
				siteVO.setViewFile(viewFile);
			}
		}
		return siteVO;
	}

	@Override
	@MethodDescription("사이트 등록")
	public int insertSite(SiteVO siteVO) {
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		siteVO.setInptId(userId);
		siteVO.setInptIp(userIp);
		
		siteVO.setSiteCd(siteVO.getSiteCd().toUpperCase());
		
		int result = siteMapper.insertSite(siteVO);
		
		if(result > 0) {
			//도메인 등록
			if(siteVO.getDomainList() != null) {
				for(DomainVO domainVO : siteVO.getDomainList()) {
					domainVO.setSiteCd(siteVO.getSiteCd());
					domainMapper.insertDomain(domainVO);
				}
			}
			if(FileUtil.isFile(siteVO.getUploadFile())) {				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_SITE";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(siteVO.getUploadFile(), uploadPath, "COM_SITE", siteVO.getSiteCd());
				if(fileNo > 0) {
					siteVO.setSiteLogo(fileNo);
					siteMapper.updateSiteLogoFile(siteVO);
				}
			}
			
			//사이트 등록인 경우 메인페이지 및 CSS 생성
			String viewCode = siteVO.getSiteCd().toLowerCase();
			String copyPath = web.getJspPath()+FileUtil.SEPERATOR+"sys"+FileUtil.SEPERATOR+"site"+FileUtil.SEPERATOR+"copy";
			String jspPath = web.getJspPath()+FileUtil.SEPERATOR+"main"+FileUtil.SEPERATOR+"site"+FileUtil.SEPERATOR+viewCode+".jsp";
			String cssPath = web.getRootPath()+FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"site"+FileUtil.SEPERATOR+viewCode+".css";
			
			fileUpload.copyFile(copyPath+FileUtil.SEPERATOR+"index.jsp", jspPath);
			fileUpload.copyFile(copyPath+FileUtil.SEPERATOR+"index.css", cssPath);			
		}
		
		return result;
	}

	@Override
	@MethodDescription("사이트 수정")
	public int updateSite(SiteVO siteVO) {
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		siteVO.setModiId(userId);
		siteVO.setModiIp(userIp);
		
		//수정처리전 원본 데이터
		SiteVO oriSiteVO = siteMapper.selectSite(siteVO.getSiteCd());
		if(oriSiteVO == null) {
			return 0;
		}
		
		int result = siteMapper.updateSite(siteVO);
		
		if(result > 0) {
			
			//도메인 삭제후 재등록
			domainMapper.deleteDomainBySiteCd(siteVO.getSiteCd());
			
			if(siteVO.getDomainList() != null) {
				for(DomainVO domainVO : siteVO.getDomainList()) {
					domainVO.setSiteCd(siteVO.getSiteCd());
					domainMapper.insertDomain(domainVO);
				}
			}
			
			siteVO.setSiteLogo(oriSiteVO.getSiteLogo());
			
			if(FileUtil.isFile(siteVO.getUploadFile())) {
				
				//기존 이미지가 있는 경우 삭제				
				if(oriSiteVO.getSiteLogo() > 0) {
					comFileManager.removeComFileByParent("COM_SITE", siteVO.getSiteCd());
				}
				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_SITE";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(siteVO.getUploadFile(), uploadPath, "COM_SITE", siteVO.getSiteCd());
				if(fileNo > 0) {
					siteVO.setSiteLogo(fileNo);						
				}
			}
			
			siteMapper.updateSiteLogoFile(siteVO);
		}
		
		return result;
	}

	@Override
	@MethodDescription("사이트 삭제")
	public int deleteSite(String siteCd) {
		int result = siteMapper.deleteSite(siteCd);
		if(result > 0) {
			//도메인 삭제
			domainMapper.deleteDomainBySiteCd(siteCd);
			//메뉴 삭제
			menuMapper.deleteMenuByMenuCd(siteCd);
			//메뉴 권한 삭제
			menuAuthMapper.deleteMenuAuthByMenuCd(siteCd);
			//즐겨찾기 삭제
			menuBookmarkMapper.deleteMenuBookmarkByMenuCd(siteCd);
		}
		return result;
	}

	@Override
	public List<SiteVO> selectSiteCdList() {
		return siteMapper.selectSiteCdList();
	}

	@Override
	@MethodDescription("사이트 코드상세조회")
	public SiteVO selectSiteCode(String siteCd) {
		SiteVO siteVO = siteMapper.selectSite(siteCd);
		if(siteVO != null) {
			String viewCode = siteVO.getSiteCd().toLowerCase();
			String jspPath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"main"+FileUtil.SEPERATOR+"site";
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"site";
			
			siteVO.setIndexCode(codeMirrorManager.getFileContent(jspPath, viewCode+".jsp"));
			siteVO.setIndexCss(codeMirrorManager.getFileContent(cssPath, viewCode+".css"));
		}
		return siteVO;
	}
	


	@Override
	@MethodDescription("사이트 코드수정")
	public int updateSiteCode(SiteVO siteVO) {
		
		//원본데이터 확인
		SiteVO oriSiteVO = siteMapper.selectSite(siteVO.getSiteCd());
		if(oriSiteVO == null) {
			return 0;
		}
		
		String viewCode = siteVO.getSiteCd().toLowerCase();
		String jspPath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"main"+FileUtil.SEPERATOR+"site";
		String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"site";
		
		codeMirrorManager.makeFile(jspPath, viewCode+".jsp", siteVO.getIndexCode());
		codeMirrorManager.makeFile(cssPath, viewCode+".css", siteVO.getIndexCss());
		
		return 1;
	}
	


	@Override
	@MethodDescription("관리자사이트 최초등록")
	public int insertSysSite(SiteVO siteVO) {
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		siteVO.setInptId(userId);
		siteVO.setInptIp(userIp);
		
		siteVO.setSiteCd(siteVO.getSiteCd().toUpperCase());
		
		int result = siteMapper.insertSite(siteVO);
		
		if(result > 0) {
			//도메인 등록
			if(siteVO.getDomainList() != null) {
				for(DomainVO domainVO : siteVO.getDomainList()) {
					domainVO.setSiteCd(siteVO.getSiteCd());
					domainMapper.insertDomain(domainVO);
				}
			}
			if(FileUtil.isFile(siteVO.getUploadFile())) {				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_SITE";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(siteVO.getUploadFile(), uploadPath, "COM_SITE", siteVO.getSiteCd());
				if(fileNo > 0) {
					siteVO.setSiteLogo(fileNo);
					siteMapper.updateSiteLogoFile(siteVO);
				}
			}
						
			String viewCode = siteVO.getSiteCd().toLowerCase();
			String copyPath = web.getJspPath()+FileUtil.SEPERATOR+"sys"+FileUtil.SEPERATOR+"site"+FileUtil.SEPERATOR+"copy";
			String jspPath = web.getJspPath()+FileUtil.SEPERATOR+"main"+FileUtil.SEPERATOR+"site"+FileUtil.SEPERATOR+viewCode+".jsp";
			String cssPath = web.getRootPath()+FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"site"+FileUtil.SEPERATOR+viewCode+".css";
			
			//파일이 있는지 체크 후 사이트 등록
			if(!FileUtil.isFileExist(jspPath)) {
				fileUpload.copyFile(copyPath+FileUtil.SEPERATOR+"index.jsp", jspPath);
			}
			if(!FileUtil.isFileExist(cssPath)) {
				fileUpload.copyFile(copyPath+FileUtil.SEPERATOR+"index.css", cssPath);
			}
		}
		
		return result;
	}
	
}
