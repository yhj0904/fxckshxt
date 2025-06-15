package kr.co.nanwe.site.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.site.service.SiteService;

/**
 * @Class Name 		: RestSiteController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RestController
public class RestSiteController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestSiteController.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/**
	 * 사이트 코드 체크
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/sys/site/isCdExist.json")
	public boolean isCdExist (HttpServletRequest request, @RequestParam(value = "siteCd", defaultValue="") String siteCd){
		return siteService.selectSiteCdExist(siteCd);
	}
	
	/**
	 * 도메인 중복 체크
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/sys/site/isDomainExist.json")
	public boolean isDomainExist (HttpServletRequest request, @RequestParam(value = "siteCd", defaultValue="") String siteCd, @RequestParam(value = "domain", defaultValue="") String domain){
		return siteService.selectDomainExist(domain);
	}
}
