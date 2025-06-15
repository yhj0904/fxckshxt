package kr.co.nanwe.userip.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.userip.service.UserIpService;

/**
 * @Class Name 		: RestUserIpController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RestController
public class RestUserIpController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestUserIpController.class);
		
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
	@Resource(name = "userIpService")
	private UserIpService userIpService;
	
	/**
	 * 사용자화면 아이디 중복체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/userIp/isIpExist.json")
	public boolean isIdExist(HttpServletRequest request, @RequestParam(value = "sId", defaultValue="") String id, @RequestParam(value = "sIp", defaultValue="") String ip){
		return userIpService.selectUserIpExist(id, ip);
	}
	
}
