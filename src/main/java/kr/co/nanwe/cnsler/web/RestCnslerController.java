package kr.co.nanwe.cnsler.web;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.CryptoUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.cnsler.service.CnslerVO;
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
 * @Class Name 		: CnslerController
 * @Description 	: RestController
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2024.06.27		정기훈			최초생성
 */

@RestController
public class RestCnslerController {
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;

	/** 상담원 Service */
	@Resource(name = "cnslerService")
	private CnslerService cnslerService;

	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;

	@RequestMapping(value = "/getCnslDeptCd.json", method = RequestMethod.POST)
	public Map<String, Object> getCnslDeptCd(@RequestParam Map<String, Object> params) {
		Map<String, Object> returnMap = new HashMap<>();

		// 학과 조회
		List<CnslerVO> cnslerVOs = cnslerService.selectCnslerDeptCd(params);
		if(cnslerVOs != null) {
			returnMap.put("data", cnslerVOs);
			returnMap.put("rs", "SUCCESS");
		} else {
			returnMap.put("data", "");
			returnMap.put("rs", "FAILED");
		}

		return returnMap;
	}
}
