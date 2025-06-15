package kr.co.nanwe.prog.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgSurvService;

/**
 * @Class Name 		: RestProgSurvController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */

@RequestMapping(value = "/prog_surv")
@RestController
public class RestProgSurvController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestProgSurvController.class);
		
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
	@Resource(name = "progSurvService")
	private ProgSurvService progSurvService;
	
	/**
	 * 설문지 생성
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/crtSurv.json")
	public Map<String, Object> crtSurv (HttpServletRequest request
									, @RequestParam(value = "progId", defaultValue="") String progId){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("progId", Integer.parseInt(progId));
		
		//로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		map.put("inptId", loginInfo.getLoginId());
		map.put("inptIp", loginInfo.getLoginIp());
		//map.put("result", 0);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", progSurvService.executeSurvList(map));
		
		return resultMap;
	}
	
}
