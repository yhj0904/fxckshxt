package kr.co.nanwe.login.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.external.service.impl.ExternalMapper;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.login.service.SsoService;

/**
 * @Class Name 		: SsoServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("ssoService")
public class SsoServiceImpl extends EgovAbstractServiceImpl implements SsoService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SsoServiceImpl.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** 외부 데이터 */
	@Resource(name = "externalMapper")
	private ExternalMapper externalMapper;

	@Override
	public LoginVO getSsoLoginInfo(HttpServletRequest request) {
		//TODO SSO 체크
		boolean checkSso = false;		
		if(checkSso) {
			String userId = "";
			LoginVO loginVO = externalMapper.selectLoginUserInfo(userId);
			return loginVO;
		}		
		return null;
	}

	@Override
	public void ssoLogout(HttpServletRequest request) {
		//TODO 로그아웃 처리		
	}
	
}
