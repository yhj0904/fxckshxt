package kr.co.nanwe.rest.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.rest.service.RestService;

/**
 * @Class Name 		: RestServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("restService")
public class RestServiceImpl extends EgovAbstractServiceImpl implements RestService {
	
	@Resource(name="restMapper")
    private RestMapper restMapper;

	@Override
	public String checkApiKey(HttpServletRequest request) {
		
		String code = "-98"; //유효하지 않은 키
		
		String authorization = request.getHeader("authorization");
		if(authorization == null) {
			return code;
		}
		
		String apiKey = authorization.replaceAll("(?i)basic", "").trim();
		if(StringUtil.isNull(apiKey)){
			return code;
		}
		
		int checkApiKey = restMapper.checkApiKey(apiKey);
		if(checkApiKey > 0) {
			code = "-99";
			String origin = "";
			if(request.getHeader("sec-fetch-site") != null) {
				String secFetchSite = request.getHeader("sec-fetch-site");
				if("same-origin".equals(secFetchSite)) {
					origin = RequestUtil.getDomain();
				} else {
					origin = request.getHeader("origin");
				}
			} else {
				origin = request.getHeader("origin");
			}
			String domain = RequestUtil.getDomainByUrl(origin);
			
			int checkApiDomain = restMapper.checkApiDomain(apiKey, domain);
			if(checkApiDomain > 0) {
				code = "0";
			}
		}
		return code;
	}
}
