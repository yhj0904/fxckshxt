package kr.co.nanwe.bbs.web;

import kr.co.nanwe.bbs.service.*;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.CryptoUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Class Name 		: RestLaborController
 * @Description 	: RestController
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2024.06.25		정기훈			최초생성
 */

@RestController
public class RestLaborController {
		
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
	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	/** Service */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;

	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;


		
}
