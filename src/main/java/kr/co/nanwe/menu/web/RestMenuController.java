package kr.co.nanwe.menu.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.bean.ProgramControllerScan;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.menu.service.MenuService;
import kr.co.nanwe.menu.service.MenuVO;

/**
 * @Class Name 		: RestMenuController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/menu")
@RestController
public class RestMenuController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestComMenuController.class);
		
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
	
	/**
	 * 메뉴 상세정보
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/view.json")
	public MenuVO view(HttpServletRequest request, @RequestParam(value = "id", defaultValue="") String id){
		MenuVO menu =  menuService.selectMenu(id);
		if(menu != null) {
			menu.setAuthList(menuService.selectMenuAuthList(id));
		}
		return menu;
	}
	
	/**
	 * 메뉴 중복체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/isIdExist.json")
	public boolean isIdExist(HttpServletRequest request, @RequestParam(value = "id", defaultValue="") String id){
		return menuService.selectMenuIdExist(id);
	}
	
	/**
	 * 프로그램 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/programList.json")
	public List<Map<String, Object>> programList(HttpServletRequest request, @RequestParam(value = "menuCd", defaultValue=CodeConfig.MAIN_SITE_CD) String menuCd){
		return ProgramControllerScan.getProgramListByMenuCd(menuCd);
	}
	
	/**
	 * 컨텐츠 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/contentList.json")
	public List<Map<String, Object>> contentList(HttpServletRequest request){
		return menuService.selectContentList();
	}
	
	/**
	 * 게시판 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/bbsMgtList.json")
	public List<Map<String, Object>> bbsList(HttpServletRequest request){
		return menuService.selectBbsMgtList();
	}
	
	/**
	 * 일정 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/schMgtList.json")
	public List<Map<String, Object>> schList(HttpServletRequest request){
		return menuService.selectSchMgtList();
	}
	
}
