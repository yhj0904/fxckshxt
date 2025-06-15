package kr.co.nanwe.menu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.auth.service.AuthService;
import kr.co.nanwe.auth.service.AuthVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.menu.service.MenuService;
import kr.co.nanwe.menu.service.MenuVO;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: SysMenuController
 * @Description 	: 메뉴 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/menu")
@Program(code="COM_MENU", name="메뉴관리")
@Controller
public class SysMenuController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysMenuController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/menu";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** Service */
	@Resource(name = "menuService")
	private MenuService menuService;
	
	/** Service */
	@Resource(name = "authService")
	private AuthService authService;
	
	/** Constructor */
	public SysMenuController() {		
		RequestMapping requestMapping = SysMenuController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/** Root Forward */
	@RequestMapping(value = "")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/list.do");
	}
		
	/**
	 * 메뉴화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search, @RequestParam(value = "sMenuCd", defaultValue="") String sMenuCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//사이트 목록 조회
		List<SiteVO> siteList = siteService.selectSiteCdList();
		model.addAttribute("siteList", siteList);
		if(siteList != null) {
			
			if(StringUtil.isNull(sMenuCd)) {
				sMenuCd = siteList.get(0).getSiteCd();
			}
			
			//목록조회
			Map<String, Object> map = menuService.selectMenuList(search, sMenuCd);	
			
			//조회결과 MODEL ADD
			model.addAllAttributes(map);
		}
		
		model.addAttribute("sMenuCd", sMenuCd);
		
		//메뉴 VO 생성
		MenuVO menuVO = new MenuVO();
		model.addAttribute("menuVO", menuVO);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 메뉴화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search, @RequestParam(value = "sMenuCd", defaultValue="") String sMenuCd
								,MenuVO menuVO ,BindingResult menuBindingResult
								,@RequestParam(value = "menuAuth", required=false) String[] menuAuthArr) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//사이트 목록 조회
		List<SiteVO> siteList = siteService.selectSiteCdList();
		model.addAttribute("siteList", siteList);
		if(siteList != null) {
			
			if(StringUtil.isNull(sMenuCd)) {
				sMenuCd = siteList.get(0).getSiteCd();
			}
			
			//목록조회
			Map<String, Object> map = menuService.selectMenuList(search, sMenuCd);	
			
			//조회결과 MODEL ADD
			model.addAllAttributes(map);
		}
		
		model.addAttribute("sMenuCd", sMenuCd);

		//유효성 검증
		beanValidator.validate(menuVO, menuBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (menuBindingResult.hasErrors()) {
			model.addAttribute("menuVO", menuVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(menuVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();			
		}
		//중복 코드 검사
		if(menuService.selectMenuIdExist(menuVO.getMenuId())) {
			addtionalValid = false;
			menuBindingResult.rejectValue("cd", "message.alert.existdata");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("menuVO", menuVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = menuService.insertMenu(menuVO, menuAuthArr);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시

			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);

			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);

			return web.returnError();			
		}		
	}
	
	/**
	 * 메뉴화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search, @RequestParam(value = "sMenuCd", defaultValue="") String sMenuCd
									,MenuVO menuVO ,BindingResult menuBindingResult
									,@RequestParam(value = "menuAuth", required=false) String[] menuAuthArr) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//사이트 목록 조회
		List<SiteVO> siteList = siteService.selectSiteCdList();
		model.addAttribute("siteList", siteList);
		if(siteList != null) {
			
			if(StringUtil.isNull(sMenuCd)) {
				sMenuCd = siteList.get(0).getSiteCd();
			}
			
			//목록조회
			Map<String, Object> map = menuService.selectMenuList(search, sMenuCd);	
			
			//조회결과 MODEL ADD
			model.addAllAttributes(map);
		}
		
		model.addAttribute("sMenuCd", sMenuCd);

		//유효성 검증
		beanValidator.validate(menuVO, menuBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (menuBindingResult.hasErrors()) {
			model.addAttribute("menuVO", menuVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(menuVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("menuVO", menuVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = menuService.updateMenu(menuVO, menuAuthArr);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
		
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);
			
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);
			
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 메뉴화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search, @RequestParam(value = "sMenuCd", defaultValue="") String sMenuCd, MenuVO menuVO) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sMenuCd", sMenuCd);
				
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(menuVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = menuService.deleteMenu(menuVO.getMenuId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);
	
			return web.returnSuccess();
			
		} else { //실패시			

			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sMenuCd", sMenuCd);
			model.addAttribute("resultParam", resultParam);

			return web.returnError();				
		}
		
	}
	
}
