package kr.co.nanwe;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.EgovHttpSessionBindingListener;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.login.service.SessionBindingListener;
import kr.co.nanwe.login.service.SsoService;
import kr.co.nanwe.popup.service.PopupService;
import kr.co.nanwe.prog.service.impl.ProgSurvMgtMapper;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;

/**
 * @Class Name 		: MainController
 * @Description 	: 메인 컨트롤러
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class MainController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main";
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Mapper */
	@Resource(name = "progSurvMgtMapper")
	private ProgSurvMgtMapper progSurvMgtMapper;	
	
	/** PopupService */
	@Resource(name = "popupService")
	private PopupService popupService;
	
	/** Service */
	@Resource(name = "loginService")
	private LoginService loginService;
	
	/** SsoService */
	@Resource(name = "ssoService")
	private SsoService ssoService;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	/**
	 * 메인페이지
	 * @param 
	 * @return 
	 * @exception 
	 */
	@RequestMapping(value = "/index.do")
	public String index(Model model, HttpServletRequest request){
		
		//사이트 정보
		String siteCd = "";
		SiteVO siteVO = siteService.selectSiteByDomain(RequestUtil.getDomain());
		if(siteVO != null && !StringUtil.isNull(siteVO.getSiteCd())) {
			siteCd = siteVO.getSiteCd();
		} else {
			siteCd = CodeConfig.MAIN_SITE_CD;
		}
		
		//비로그인 가능 인지
		String loginYn = siteVO.getLoginYn();
		
		LoginVO loginVO = null;
		
		//로그인 후 이용가능한 사이트이며 비로그인 인경우
		if(StringUtil.isEqual(loginYn, "Y") && SessionUtil.getLoginUser() == null) {
			
			//SSO 사용하는 경우 체크
			if(web.isSsoUse()) {
				loginVO = ssoService.getSsoLoginInfo(request);
				if(loginVO != null) {					
					SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, loginVO);
					
					//로그 등록
					loginService.insertLoginLog(loginVO);
					
					//중복로그인 리스너
					if(!web.isMultiLogin()) {
						String loginSiteCd = siteCd;
						//관리자인 경우
						if(SessionUtil.isAdmin(loginVO)){
							loginSiteCd = CodeConfig.SYS_SITE_CD;
						}
						String sessionKey = EncryptUtil.encryptMsg(loginSiteCd + "#" + loginVO.getLoginId());
						EgovHttpSessionBindingListener listener = EgovHttpSessionBindingListener.getInstance();
						request.getSession().setAttribute(sessionKey, listener);
					} else {
						SessionBindingListener listener = SessionBindingListener.getInstance();
						request.getSession().setAttribute(loginVO.getLoginId(), listener);
					}					
					return web.returnLogin();
				}
			}
			
			//VO 생성
			loginVO = new LoginVO();			
			//조회결과 MODEL ADD
			model.addAttribute("loginVO", loginVO);			
			return web.returnView(VIEW_PATH, "/login/login", "LOGIN");
		}
		
		if(SessionUtil.getLoginUser() != null) {
			int unsurvCnt = 0;
			unsurvCnt = progSurvMgtMapper.selectCntMySurv(SessionUtil.getLoginUser().getLoginId());
			
			model.addAttribute("unsurvCnt", unsurvCnt);
		}
		
		//접속기록 TOP 5
		model.addAttribute("LOGIN_LOG_LIST", loginService.selectRecentLoginLogListByLoginId());
		
		//팝업목록
		model.addAttribute("COM_POPUP_LIST", popupService.selectPopupByMain(siteCd));	
		
		return web.returnView(VIEW_PATH, "/index", "MAIN");
	}
}