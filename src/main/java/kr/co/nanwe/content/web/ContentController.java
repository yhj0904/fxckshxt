package kr.co.nanwe.content.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.content.service.ContentService;
import kr.co.nanwe.content.service.ContentVO;

/**
 * @Class Name 		: ContentController
 * @Description 	: 컨텐츠 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/content")
@Program(code="CONTENT", name="컨텐츠")
@Controller
public class ContentController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ContentController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/content";
	
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
	@Resource(name = "contentService")
	private ContentService contentService;
	

	/**
	 * 컨텐츠 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/{path}.do")
	public String root(@PathVariable("path") String path, Model model, HttpServletRequest request) {
		
		String contId = StringUtil.camelToUpperUnderscore(path);
		
		//상세조회
		ContentVO contentVO = contentService.selectContent(contId);
		
		//조회결과가 없는 경우
		if(contentVO == null) {		
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("contentVO", contentVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
}
