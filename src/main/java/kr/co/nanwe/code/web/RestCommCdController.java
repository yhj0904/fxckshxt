package kr.co.nanwe.code.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;

/**
 * @Class Name 		: RestCommCdController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/commCd")
@RestController
public class RestCommCdController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestCommCdController.class);
		
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
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/**
	 * 공통코드 상세정보
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/view.json")
	public CommCdVO view(HttpServletRequest request, @RequestParam(value = "cd", defaultValue="") String cd){
		return commCdService.selectCommCd(cd);
	}	
	
	/**
	 * 공통코드 중복체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/isCdExist.json")
	public boolean isCdExist(HttpServletRequest request, @RequestParam(value = "cd", defaultValue="") String cd){
		return commCdService.selectCommCdExist(cd);
	}
	
}
