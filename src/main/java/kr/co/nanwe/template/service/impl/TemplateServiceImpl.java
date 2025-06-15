package kr.co.nanwe.template.service.impl;

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
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.template.service.TemplateCodeVO;
import kr.co.nanwe.template.service.TemplateMgtVO;
import kr.co.nanwe.template.service.TemplateService;

/**
 * @Class Name 		: TemplateServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("templateService")
public class TemplateServiceImpl extends EgovAbstractServiceImpl implements TemplateService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	/** Mapper */
	@Resource(name = "templateMapper")
	private TemplateMapper templateMapper;
	
	@Resource(name = "codeMirrorManager")
	private CodeMirrorManager codeMirrorManager;
	
	@Override
	@MethodDescription("템플릿 사용목록조회")
	public List<TemplateMgtVO> selectTemplateUseList() {
		return templateMapper.selectTemplateUseList();
	}
	
	@Override
	@MethodDescription("템플릿 목록조회")
	public Map<String, Object> selectTemplateList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = templateMapper.selectTemplateTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<TemplateMgtVO> list = templateMapper.selectTemplateList(paramMap);
		System.out.println("test>>>>>:"+list);
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	@Override
	@MethodDescription("템플릿 폼")
	public TemplateMgtVO getTemplateForm() {
		TemplateMgtVO templateMgtVO = new TemplateMgtVO();
		templateMgtVO.setUseYn("Y");
		
		String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"sys"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+"copy";		

		templateMgtVO.setMainCode(codeMirrorManager.getFileContent(templatePath, "main.jsp"));
		templateMgtVO.setSubCode(codeMirrorManager.getFileContent(templatePath, "sub.jsp"));
		templateMgtVO.setLoginCode(codeMirrorManager.getFileContent(templatePath, "login.jsp"));
		templateMgtVO.setEmptyCode(codeMirrorManager.getFileContent(templatePath, "empty.jsp"));
		templateMgtVO.setPopCode(codeMirrorManager.getFileContent(templatePath, "pop.jsp"));
		
		templateMgtVO.setLayoutHeader(codeMirrorManager.getFileContent(templatePath, "header.jsp"));
		templateMgtVO.setLayoutFooter(codeMirrorManager.getFileContent(templatePath, "footer.jsp"));
		templateMgtVO.setLayoutGnb(codeMirrorManager.getFileContent(templatePath, "gnb.jsp"));
		
		templateMgtVO.setMainCss(codeMirrorManager.getFileContent(templatePath, "main.css"));
		templateMgtVO.setSubCss(codeMirrorManager.getFileContent(templatePath, "sub.css"));
		templateMgtVO.setLoginCss(codeMirrorManager.getFileContent(templatePath, "login.css"));
		templateMgtVO.setEmptyCss(codeMirrorManager.getFileContent(templatePath, "empty.css"));
		templateMgtVO.setPopCss(codeMirrorManager.getFileContent(templatePath, "pop.css"));
		templateMgtVO.setLayoutCss(codeMirrorManager.getFileContent(templatePath, "layout.css"));
		
		return templateMgtVO;
	}

	@Override
	public TemplateMgtVO selectTemplateCopyForm(String templateCd) {
		if(templateCd == null) {
			return null;
		}
		TemplateMgtVO templateMgtVO = templateMapper.selectTemplate(templateCd);
		if(templateMgtVO != null) {
			
			//코드 초기화
			templateMgtVO.setTemplateCd("");
			
			String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+templateCd.toLowerCase();
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+templateCd.toLowerCase();
		
			templateMgtVO.setMainCode(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "main.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			templateMgtVO.setSubCode(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "sub.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			templateMgtVO.setLoginCode(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "login.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			templateMgtVO.setEmptyCode(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "empty.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			templateMgtVO.setPopCode(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "pop.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			
			templateMgtVO.setLayoutHeader(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "header.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			templateMgtVO.setLayoutFooter(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "footer.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			templateMgtVO.setLayoutGnb(StringUtil.getStrReplaceAll(codeMirrorManager.getFileContent(templatePath, "gnb.jsp"), "/template/"+templateCd.toLowerCase()+"/", "/template/\\{TEMPLATE_CODE\\}/"));
			
			templateMgtVO.setMainCss(codeMirrorManager.getFileContent(cssPath, "main.css"));
			templateMgtVO.setSubCss(codeMirrorManager.getFileContent(cssPath, "sub.css"));
			templateMgtVO.setLoginCss(codeMirrorManager.getFileContent(cssPath, "login.css"));
			templateMgtVO.setEmptyCss(codeMirrorManager.getFileContent(cssPath, "empty.css"));
			templateMgtVO.setPopCss(codeMirrorManager.getFileContent(cssPath, "pop.css"));
			templateMgtVO.setLayoutCss(codeMirrorManager.getFileContent(cssPath, "layout.css"));
		}
		return templateMgtVO;
	}

	@Override
	@MethodDescription("템플릿 상세조회")
	public TemplateMgtVO selectTemplate(String templateCd) {
		if(templateCd == null) {
			return null;
		}
		TemplateMgtVO templateMgtVO = templateMapper.selectTemplate(templateCd);
		if(templateMgtVO != null) {
			String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+templateCd.toLowerCase();
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+templateCd.toLowerCase();
		
			templateMgtVO.setMainCode(codeMirrorManager.getFileContent(templatePath, "main.jsp"));
			templateMgtVO.setSubCode(codeMirrorManager.getFileContent(templatePath, "sub.jsp"));
			templateMgtVO.setLoginCode(codeMirrorManager.getFileContent(templatePath, "login.jsp"));
			templateMgtVO.setEmptyCode(codeMirrorManager.getFileContent(templatePath, "empty.jsp"));
			templateMgtVO.setPopCode(codeMirrorManager.getFileContent(templatePath, "pop.jsp"));
			
			templateMgtVO.setLayoutHeader(codeMirrorManager.getFileContent(templatePath, "header.jsp"));
			templateMgtVO.setLayoutFooter(codeMirrorManager.getFileContent(templatePath, "footer.jsp"));
			templateMgtVO.setLayoutGnb(codeMirrorManager.getFileContent(templatePath, "gnb.jsp"));
			
			templateMgtVO.setMainCss(codeMirrorManager.getFileContent(cssPath, "main.css"));
			templateMgtVO.setSubCss(codeMirrorManager.getFileContent(cssPath, "sub.css"));
			templateMgtVO.setLoginCss(codeMirrorManager.getFileContent(cssPath, "login.css"));
			templateMgtVO.setEmptyCss(codeMirrorManager.getFileContent(cssPath, "empty.css"));
			templateMgtVO.setPopCss(codeMirrorManager.getFileContent(cssPath, "pop.css"));
			templateMgtVO.setLayoutCss(codeMirrorManager.getFileContent(cssPath, "layout.css"));
		}
		return templateMgtVO;
	}

	@Override
	@MethodDescription("템플릿 등록")
	public int insertTemplate(TemplateMgtVO templateVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		templateVO.setInptId(userId);
		templateVO.setInptIp(userIp);
		
		//템플릿 코드
		String lowerTemplateCd = templateVO.getTemplateCd().toLowerCase();
		String upperTemplateCd = templateVO.getTemplateCd().toUpperCase();
		
		templateVO.setTemplateCd(upperTemplateCd);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = templateMapper.insertTemplate(templateVO);
		if(result > 0) {
			String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			
			codeMirrorManager.makeFile(templatePath, "main.jsp", StringUtil.getStrReplaceAll(templateVO.getMainCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "sub.jsp", StringUtil.getStrReplaceAll(templateVO.getSubCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "login.jsp", StringUtil.getStrReplaceAll(templateVO.getLoginCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "empty.jsp", StringUtil.getStrReplaceAll(templateVO.getEmptyCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "pop.jsp", StringUtil.getStrReplaceAll(templateVO.getPopCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			
			codeMirrorManager.makeFile(templatePath, "header.jsp", StringUtil.getStrReplaceAll(templateVO.getLayoutHeader(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "footer.jsp", StringUtil.getStrReplaceAll(templateVO.getLayoutFooter(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "gnb.jsp", StringUtil.getStrReplaceAll(templateVO.getLayoutGnb(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			
			codeMirrorManager.makeFile(cssPath, "main.css", templateVO.getMainCss());
			codeMirrorManager.makeFile(cssPath, "sub.css", templateVO.getSubCss());
			codeMirrorManager.makeFile(cssPath, "login.css", templateVO.getLoginCss());
			codeMirrorManager.makeFile(cssPath, "empty.css", templateVO.getEmptyCss());
			codeMirrorManager.makeFile(cssPath, "pop.css", templateVO.getPopCss());
			codeMirrorManager.makeFile(cssPath, "layout.css", templateVO.getLayoutCss());
		}
		
		return result;
	}

	@Override
	@MethodDescription("템플릿 수정")
	public int updateTemplate(TemplateMgtVO templateVO) {
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		templateVO.setModiId(userId);
		templateVO.setModiIp(userIp);
		
		//템플릿 코드
		String upperTemplateCd = templateVO.getTemplateCd().toUpperCase();
		String lowerTemplateCd = templateVO.getTemplateCd().toLowerCase();
		
		//수정 처리 (수정된 경우 row 수 return)	
		int result = templateMapper.updateTemplate(templateVO);
		if(result > 0) {
			
			//변경되기전 파일 조회 후 DB에 등록한다.
			String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			
			if(FileUtil.isFolder(web.getRootPath() + FileUtil.SEPERATOR + templatePath) && FileUtil.isFolder(web.getRootPath() + FileUtil.SEPERATOR + cssPath)) {
				TemplateCodeVO templateCodeVO = new TemplateCodeVO();
				templateCodeVO.setTemplateCd(upperTemplateCd);
				templateCodeVO.setInptId(userId);
				templateCodeVO.setInptIp(userIp);
				
				templateCodeVO.setMainCode(codeMirrorManager.getFileContent(templatePath, "main.jsp"));
				templateCodeVO.setSubCode(codeMirrorManager.getFileContent(templatePath, "sub.jsp"));
				templateCodeVO.setLoginCode(codeMirrorManager.getFileContent(templatePath, "login.jsp"));
				templateCodeVO.setEmptyCode(codeMirrorManager.getFileContent(templatePath, "empty.jsp"));
				templateCodeVO.setPopCode(codeMirrorManager.getFileContent(templatePath, "pop.jsp"));
				
				templateCodeVO.setLayoutHeader(codeMirrorManager.getFileContent(templatePath, "header.jsp"));
				templateCodeVO.setLayoutFooter(codeMirrorManager.getFileContent(templatePath, "footer.jsp"));
				templateCodeVO.setLayoutGnb(codeMirrorManager.getFileContent(templatePath, "gnb.jsp"));
				
				templateCodeVO.setMainCss(codeMirrorManager.getFileContent(cssPath, "main.css"));
				templateCodeVO.setSubCss(codeMirrorManager.getFileContent(cssPath, "sub.css"));
				templateCodeVO.setLoginCss(codeMirrorManager.getFileContent(cssPath, "login.css"));
				templateCodeVO.setEmptyCss(codeMirrorManager.getFileContent(cssPath, "empty.css"));
				templateCodeVO.setPopCss(codeMirrorManager.getFileContent(cssPath, "pop.css"));
				templateCodeVO.setLayoutCss(codeMirrorManager.getFileContent(cssPath, "layout.css"));
				
				//템플릿 코드 등록
				templateMapper.insertTemplateCode(templateCodeVO);
			}
			
			codeMirrorManager.makeFile(templatePath, "main.jsp", StringUtil.getStrReplaceAll(templateVO.getMainCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "sub.jsp", StringUtil.getStrReplaceAll(templateVO.getSubCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "login.jsp", StringUtil.getStrReplaceAll(templateVO.getLoginCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "empty.jsp", StringUtil.getStrReplaceAll(templateVO.getEmptyCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "pop.jsp", StringUtil.getStrReplaceAll(templateVO.getPopCode(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			
			codeMirrorManager.makeFile(templatePath, "header.jsp", StringUtil.getStrReplaceAll(templateVO.getLayoutHeader(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "footer.jsp", StringUtil.getStrReplaceAll(templateVO.getLayoutFooter(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			codeMirrorManager.makeFile(templatePath, "gnb.jsp", StringUtil.getStrReplaceAll(templateVO.getLayoutGnb(), "\\{TEMPLATE_CODE\\}", lowerTemplateCd));
			
			codeMirrorManager.makeFile(cssPath, "main.css", templateVO.getMainCss());
			codeMirrorManager.makeFile(cssPath, "sub.css", templateVO.getSubCss());
			codeMirrorManager.makeFile(cssPath, "login.css", templateVO.getLoginCss());
			codeMirrorManager.makeFile(cssPath, "empty.css", templateVO.getEmptyCss());
			codeMirrorManager.makeFile(cssPath, "pop.css", templateVO.getPopCss());
			codeMirrorManager.makeFile(cssPath, "layout.css", templateVO.getLayoutCss());
		}
		
		return result;
	}

	@Override
	@MethodDescription("템플릿 삭제")
	public int deleteTemplate(String templateCd) {
		
		if(templateCd == null) {
			return 0;
			
		}
		
		//템플릿 코드
		String lowerTemplateCd = templateCd.toLowerCase();
		String upperTemplateCd = templateCd.toUpperCase();
		
		//기본 템플릿 또는 시스템 템플릿인경우 삭제 x
		if("BASIC".equals(upperTemplateCd) || CodeConfig.SYS_TILES_CD.equals(upperTemplateCd)) {
			return 0;
		}
		
		int result = templateMapper.deleteTemplate(upperTemplateCd);
		//첨부파일 삭제
		if(result > 0) {
			String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			//템플릿 삭제
			codeMirrorManager.deleteFolder(templatePath);
			codeMirrorManager.deleteFolder(cssPath);
			codeMirrorManager.deleteFile(FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"config"+FileUtil.SEPERATOR+"tiles", "layout-"+lowerTemplateCd+".xml");
			
			//코드 목록 삭제
			templateMapper.deleteTemplateCodeByTemplateCd(upperTemplateCd);
			
		}
		return result;
	}

	@Override
	@MethodDescription("템플릿 코드 중복조회")
	public boolean selectTemplateCdExist(String templateCd) {
		
		//코드가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(templateCd)) {
			return true;
		}
		//템플릿 코드
		String upperTemplateCd = templateCd.toUpperCase();
		
		//BASIC 이거나 SYSTEM 인경우 return
		if("BASIC".equals(upperTemplateCd) || CodeConfig.SYS_TILES_CD.equals(upperTemplateCd)) {
			return false;
		}
		
		int count = templateMapper.selectTemplateCdExist(upperTemplateCd);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("템플릿 코드 목록조회")
	public Map<String, Object> selectTemplateCodeList(SearchVO search, String templateCd) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("templateCd", templateCd);
		
		//전체 ROW COUNT
		int totCnt = templateMapper.selectTemplateCodeTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<TemplateCodeVO> list = templateMapper.selectTemplateCodeList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("템플릿 코드 상세조회")
	public TemplateCodeVO selectTemplateCode(String templateCd, int seq) {
		return templateMapper.selectTemplateCode(templateCd, seq);
	}

	@Override
	@MethodDescription("템플릿 코드 복원")
	public int updateTemplateByCode(String templateCd, int seq) {
		
		int result = 0;
		
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//복원 데이터
		TemplateCodeVO templateCodeVO = templateMapper.selectTemplateCode(templateCd, seq);
		
		if(templateCodeVO != null) {
			
			//템플릿 코드
			String lowerTemplateCd = templateCodeVO.getTemplateCd().toLowerCase();
			
			//변경되기전 파일 조회 후 DB에 등록한다.
			String templatePath = FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			String cssPath = FileUtil.SEPERATOR+"css"+FileUtil.SEPERATOR+"template"+FileUtil.SEPERATOR+lowerTemplateCd;
			
			if(FileUtil.isFolder(web.getRootPath() + FileUtil.SEPERATOR + templatePath) && FileUtil.isFolder(web.getRootPath() + FileUtil.SEPERATOR + cssPath)) {
				
				TemplateCodeVO oriTemplateCodeVO = new TemplateCodeVO();
				oriTemplateCodeVO.setTemplateCd(templateCd);
				oriTemplateCodeVO.setInptId(userId);
				oriTemplateCodeVO.setInptIp(userIp);
				
				oriTemplateCodeVO.setMainCode(codeMirrorManager.getFileContent(templatePath, "main.jsp"));
				oriTemplateCodeVO.setSubCode(codeMirrorManager.getFileContent(templatePath, "sub.jsp"));
				oriTemplateCodeVO.setLoginCode(codeMirrorManager.getFileContent(templatePath, "login.jsp"));
				oriTemplateCodeVO.setEmptyCode(codeMirrorManager.getFileContent(templatePath, "empty.jsp"));
				oriTemplateCodeVO.setPopCode(codeMirrorManager.getFileContent(templatePath, "pop.jsp"));
				
				oriTemplateCodeVO.setLayoutHeader(codeMirrorManager.getFileContent(templatePath, "header.jsp"));
				oriTemplateCodeVO.setLayoutFooter(codeMirrorManager.getFileContent(templatePath, "footer.jsp"));
				oriTemplateCodeVO.setLayoutGnb(codeMirrorManager.getFileContent(templatePath, "gnb.jsp"));
				
				oriTemplateCodeVO.setMainCss(codeMirrorManager.getFileContent(cssPath, "main.css"));
				oriTemplateCodeVO.setSubCss(codeMirrorManager.getFileContent(cssPath, "sub.css"));
				oriTemplateCodeVO.setLoginCss(codeMirrorManager.getFileContent(cssPath, "login.css"));
				oriTemplateCodeVO.setEmptyCss(codeMirrorManager.getFileContent(cssPath, "empty.css"));
				oriTemplateCodeVO.setPopCss(codeMirrorManager.getFileContent(cssPath, "pop.css"));
				oriTemplateCodeVO.setLayoutCss(codeMirrorManager.getFileContent(cssPath, "layout.css"));
				
				//템플릿 코드 등록
				result = templateMapper.insertTemplateCode(templateCodeVO);
				
				if(result > 0) {
					
					codeMirrorManager.makeFile(templatePath, "main.jsp", templateCodeVO.getMainCode());
					codeMirrorManager.makeFile(templatePath, "sub.jsp", templateCodeVO.getSubCode());
					codeMirrorManager.makeFile(templatePath, "login.jsp", templateCodeVO.getLoginCode());
					codeMirrorManager.makeFile(templatePath, "empty.jsp", templateCodeVO.getEmptyCode());
					codeMirrorManager.makeFile(templatePath, "pop.jsp", templateCodeVO.getPopCode());
					
					codeMirrorManager.makeFile(templatePath, "header.jsp", templateCodeVO.getLayoutHeader());
					codeMirrorManager.makeFile(templatePath, "footer.jsp", templateCodeVO.getLayoutFooter());
					codeMirrorManager.makeFile(templatePath, "gnb.jsp", templateCodeVO.getLayoutGnb());
					
					codeMirrorManager.makeFile(cssPath, "main.css", templateCodeVO.getMainCss());
					codeMirrorManager.makeFile(cssPath, "sub.css", templateCodeVO.getSubCss());
					codeMirrorManager.makeFile(cssPath, "login.css", templateCodeVO.getLoginCss());
					codeMirrorManager.makeFile(cssPath, "empty.css", templateCodeVO.getEmptyCss());
					codeMirrorManager.makeFile(cssPath, "pop.css", templateCodeVO.getPopCss());
					codeMirrorManager.makeFile(cssPath, "layout.css", templateCodeVO.getLayoutCss());
					
					//복원한 데이터는 삭제한다
					templateMapper.deleteTemplateCode(templateCd, seq);
				}				
			}
		}
		
		return result;
	}

	@Override
	public int checkSiteUseTemplate(String templateCd) {
		//null인 경우 사용중인거로 간주
		if(templateCd == null) {
			return 1;
		}
		return templateMapper.checkSiteUseTemplate(templateCd.toUpperCase());
	}
		
}
