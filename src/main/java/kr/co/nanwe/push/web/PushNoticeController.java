package kr.co.nanwe.push.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.push.service.PushNoticeService;
import kr.co.nanwe.push.service.PushNoticeVO;

/**
 * @Class Name 		: PushNoticeController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RequestMapping(value = "/push/notice")
@Program(code="PUSH_NOTICE", name="발송내역")
@Controller
public class PushNoticeController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(PushNoticeController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/push";
	
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
	@Resource(name = "pushNoticeService")
	private PushNoticeService pushNoticeService;
	
	/** Constructor */
	public PushNoticeController() {		
		RequestMapping requestMapping = PushNoticeController.class.getAnnotation(RequestMapping.class);
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
	 * 알림발송 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = pushNoticeService.selectPushNoticeListByLoginUser(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/notice/list");
	}
	
	/**
	 * 알림발송 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="0") int noticeNo){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushNoticeVO pushNoticeVO = pushNoticeService.selectPushNoticeByLoginUser(noticeNo);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushNoticeVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("pushNoticeVO", pushNoticeVO);
		
		return web.returnView(VIEW_PATH, "/notice/view");
	}
}
