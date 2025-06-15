package kr.co.nanwe.menu.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.menu.service.MenuBookmarkVO;
import kr.co.nanwe.menu.service.MenuService;

/**
 * @Class Name 		: BookmarkController
 * @Description 	: 메뉴 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/bookmark")
@Program(code="BOOKMARK", name="즐겨찾기")
@Controller
public class BookmarkController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/bookmark";
	
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
	@Resource(name = "menuService")
	private MenuService menuService;
	
	/** Constructor */
	public BookmarkController() {		
		RequestMapping requestMapping = BookmarkController.class.getAnnotation(RequestMapping.class);
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
		return web.forward(REDIRECT_PATH + "/register.do");
	}
	
	/**
	 * 즐겨찾기화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request){		
		List<MenuBookmarkVO> menuList = menuService.selectMenuBookmarkListByForm();
		model.addAttribute("menuList", menuList);		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 즐겨찾기화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request
								, @RequestParam(value = "menuId", required=false) String[] idArr) {
		
		
		if(idArr == null) {
			model.addAttribute("redirectUrl", "/");
			return web.returnSuccess();
		}		
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = menuService.insertMenuBookmark(idArr);
		}
		
		if(result > 0) {
			model.addAttribute("redirectUrl", "/");
			return web.returnSuccess();			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");			
			return web.returnError();
		}		
	}
}
