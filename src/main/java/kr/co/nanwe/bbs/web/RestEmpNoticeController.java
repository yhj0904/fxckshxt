package kr.co.nanwe.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.JobApiService;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;

/**
 * @Class Name 		: RestEmpNoticeController
 * @Description 	: RestController
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.30		신한나			최초생성
 */

@RestController
public class RestEmpNoticeController {
		
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
	@Resource(name = "jobApiService")
	private JobApiService jobApiService;
	
	
	/**
	 * 최근 등록된 워크넷 채용정보 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/emp/getWorknet.json", method = RequestMethod.POST)
	public List<Map<String, Object>> getWorknet(HttpServletRequest request
									,@RequestParam(value = "pagePerRow", defaultValue="") int pagePerRow) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pagePerRow", pagePerRow);
		
		return jobApiService.mainWorknet(param);
	}
		
}
