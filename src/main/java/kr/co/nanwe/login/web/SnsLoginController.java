package kr.co.nanwe.login.web;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.EgovHttpSessionBindingListener;
import kr.co.nanwe.login.service.FacebookManager;
import kr.co.nanwe.login.service.GoogleManager;
import kr.co.nanwe.login.service.KakaoManager;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.login.service.NaverManager;
import kr.co.nanwe.login.service.SessionBindingListener;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;
import kr.co.nanwe.user.service.SnsUserService;
import kr.co.nanwe.user.service.UserVO;

@Controller
public class SnsLoginController {
	
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
	@Resource(name = "snsUserService")
	private SnsUserService snsUserService;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	@Resource(name = "kakao")
	private KakaoManager kakao;
	
	@Resource(name = "naver")
	private NaverManager naver;
	
	@Resource(name = "google")
	private GoogleManager google;
	
	@Resource(name = "facebook")
	private FacebookManager facebook;

	@RequestMapping(value = "/login/sns/{path}.do")
	public String snsLogin(Model model, HttpServletRequest request, @PathVariable("path") String snsType) {
		
		if(StringUtil.isNull(snsType)) {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
		
		snsType = snsType.toLowerCase();
		
		UserVO user = null;
		
		switch (snsType) {		
			case "kakao":
				user = kakao.getUserInfo(request);
				break;				
			case "naver":
				user = naver.getUserInfo(request);
				break;				
			case "google":
				user = google.getUserInfo(request);				
				break;				
			case "facebook":
				user = facebook.getUserInfo(request);
				break;				
		}
		
		if(user != null) {
			//회원가입 여부 확인
			boolean isJoin = snsUserService.checkSnsUserExist(user);			
			if(isJoin) {
				//회원가입이 된 경우 로그인 처리
				LoginVO loginInfo = snsUserService.selectLoginSnsUser(user);
				if("Y".equals(loginInfo.getLoginSuccess())) {
					
					//로그 등록
					loginService.insertLoginLog(loginInfo);
					
					SessionUtil.setSessionVal(SessionUtil.LOGIN_SESSION_KEY, loginInfo);
					
					//중복로그인 리스너
					if(!web.isMultiLogin()) {
						String siteCd = "";
						SiteVO siteVO = siteService.selectSiteByDomain(RequestUtil.getDomain());
						if(siteVO != null && !StringUtil.isNull(siteVO.getSiteCd())) {
							siteCd = siteVO.getSiteCd();
						} else {
							siteCd = CodeConfig.MAIN_SITE_CD;
						}
						String sessionKey = EncryptUtil.encryptMsg(siteCd + "#" + loginInfo.getLoginId());
						EgovHttpSessionBindingListener listener = EgovHttpSessionBindingListener.getInstance();
						request.getSession().setAttribute(sessionKey, listener);
					} else {
						SessionBindingListener listener = SessionBindingListener.getInstance();
						request.getSession().setAttribute(loginInfo.getLoginId(), listener);
					}
					
					return web.redirect(model, "/index.do");
				} else {
					//로그기록
					loginService.insertLoginLog(loginInfo);
					model.addAttribute("redirectUrl", "/login.do");
					model.addAttribute("resultMsg", messageUtil.getMessage("errors.login"));
					return web.returnError();
				}
			} else {
				//비회원인 경우 회원가입 페이지로 이동
				//랜덤 키 생성
				String joinToken = UUID.randomUUID().toString().replaceAll("-", "");
				request.getSession().setAttribute("joinToken", joinToken);
				return web.redirect(model, "/join/sns/"+snsType+"/agree.do?joinToken="+joinToken+"&authToken="+user.getAccessToken());
			}
			
		} else {
			model.addAttribute("redirectUrl", "/login.do");
			return web.returnError();
		}
	}

}