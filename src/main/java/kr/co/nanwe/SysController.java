package kr.co.nanwe;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.EgovHttpSessionBindingListener;
import kr.co.nanwe.login.service.LoginLogVO;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.login.service.SessionBindingListener;
import kr.co.nanwe.stat.service.StatService;
import kr.co.nanwe.surv.service.SurvMgtVO;
import kr.co.nanwe.surv.service.SurvService;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.userip.service.UserIpService;

/**
 * @Class Name 		: SysController
 * @Description 	: 관리자 컨트롤러
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class SysController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "loginService")
	private LoginService loginService;
	
	/** Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** Service */
	@Resource(name = "userIpService")
	private UserIpService userIpService;
	
	/** Service */
	@Resource(name = "statService")
	private StatService statService;
	
	/** Service */
	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	/** Service */
	@Resource(name = "survService")
	private SurvService survService;
	
	/** Constructor */
	public SysController() {		
		RequestMapping requestMapping = SysController.class.getAnnotation(RequestMapping.class);
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
		return web.forward(REDIRECT_PATH + "/index.do");
	}
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	
	@RequestMapping(value = "/sys.do")
    public String sysDotDo(Model model, HttpServletRequest request) {
        return web.forward("/sys/index.do");
    }
	/**
	 * 관리자 메인페이지
	 * @param 
	 * @return 
	 * @exception 
	 */
	  @RequestMapping(value = "/sys/index.do")
	public String index(Model model, HttpServletRequest request){
		
		//로그인중이 아닌경우
		if(SessionUtil.getLoginUser() == null) {
			LoginVO loginVO = new LoginVO();
			model.addAttribute("loginVO", loginVO);			
			model.addAttribute("command", loginVO); // 추가
			return web.returnView(VIEW_PATH, "/login/login", "LOGIN");
		}
		
		//접속자 통계
		Map<String, Object> statMap = statService.selectSysMainStat();
		model.addAttribute("statMap", statMap);
		
		//최근 관리자 로그인 기록
		List<LoginLogVO> sysLoginLogList = loginService.selectRecentSysLoginLogList();
		model.addAttribute("sysLoginLogList", sysLoginLogList);
		
		//최근 로그인
		List<LoginLogVO> loginLogLst = loginService.selectRecentLoginLogList();
		model.addAttribute("loginLogLst", loginLogLst);
		
		//현재 접속자 리스트
		int loginCount = 0;
		List<LoginVO> loginList = null;
		if(!web.isMultiLogin()) {
			EgovHttpSessionBindingListener listener = EgovHttpSessionBindingListener.getInstance();
			loginCount = listener.getUserCount();
			loginList = listener.getUserList();
		} else {
			SessionBindingListener listener = SessionBindingListener.getInstance();
			loginCount = listener.getUserCount();
			loginList = listener.getUserList();
		}
		model.addAttribute("loginCount", loginCount);
		model.addAttribute("loginList", loginList);
		
		//최근 게시물
		List<BbsVO> bbsList = bbsService.selectRecentBbsList();
		model.addAttribute("bbsList", bbsList);
		
		//진행중 설문목록
		List<SurvMgtVO> survList = survService.selectRecentSurvList();
		model.addAttribute("survList", survList);
		
		return web.returnView(VIEW_PATH, "/index", "MAIN");
	}
	
	/**
	 * 시스템 관리
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/sys/system.do")
	public String system (Model model, HttpServletRequest request){
		return web.returnView(VIEW_PATH, "/system/system");
	}
	
}
