package kr.co.nanwe.auth.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.auth.service.AuthService;
import kr.co.nanwe.auth.service.AuthVO;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.external.service.ExternalService;

/**
 * @Class Name 		: RestAuthController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/auth")
@RestController
public class RestAuthController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthController.class);
		
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
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Service */
	@Resource(name = "authService")
	private AuthService authService;
	
	/** ExternalService */
	@Resource(name = "externalService")
	private ExternalService externalService;	
	
	/**
	 * 권한정보
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/view.json")
	public AuthVO view(HttpServletRequest request, @RequestParam(value = "cd", defaultValue="") String authCd){
		return authService.selectAuth(authCd);
	}
	
	/**
	 * 권한목록
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/list.json")
	public List<AuthVO> list(HttpServletRequest request){
		return authService.selectAuthUseList();
	}
	
	/**
	 * 공통코드 중복체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/isCdExist.json")
	public boolean isCdExist(HttpServletRequest request, @RequestParam(value = "cd", defaultValue="") String cd){
		return authService.selectAuthCdExist(cd);
	}
	
	/**
	 * 권한 코드 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/authList.json")
	public List<Map<String, Object>> authList(HttpServletRequest request, @RequestBody Map<String, Object> paramMap){
		List<Map<String, Object>> list = null;
		if(paramMap == null) {
			return null;
		}
		
		String type = "";
		String authDvcd = "";
		String userDvcd = "";
		String workDvcd = "";
		
		if(paramMap.get("type") != null) type = (String) paramMap.get("type");
		if(paramMap.get("authDvcd") != null) authDvcd = (String) paramMap.get("authDvcd");
		if(paramMap.get("userDvcd") != null) userDvcd = (String) paramMap.get("userDvcd");
		if(paramMap.get("workDvcd") != null) workDvcd = (String) paramMap.get("workDvcd");
		
		if(web.isExternalUse() && CodeConfig.EXTERANL_USER_CODE.equals(authDvcd)) {
			switch (type) {
				case "USER_DVCD":
					list= externalService.selectUserDvcdList();
					break;
				case "WORK_DVCD":
					list= externalService.selectWorkDvcdList(userDvcd);
					break;
				case "STAT_DVCD":
					list= externalService.selectStatDvcdList(userDvcd, workDvcd);
					break;
				default:
					break;
			}
		} else if (CodeConfig.COM_USER_CODE.equals(authDvcd)) {
			list = new ArrayList<Map<String, Object>>();
			if("STAT_DVCD".equals(type)) {
				List<CommCdVO> comAuthList = commCdService.selectAuthCdList();
				if(comAuthList != null && comAuthList.size() > 0) {					
					for(CommCdVO commCdVO : comAuthList) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("statDvcd", commCdVO.getCd());
						map.put("statDvnm", commCdVO.getCdNm());
						list.add(map);
					}
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("authDvcd", CodeConfig.COM_USER_CODE);
				map.put("userDvcd", CodeConfig.COM_USER_CODE);
				map.put("userDvnm", CodeConfig.COM_USER_CODE_NM);
				map.put("workDvcd", CodeConfig.COM_USER_CODE);
				map.put("workDvnm", CodeConfig.COM_USER_CODE_NM);
				list.add(map);
			}
		}
		
		return list;
	}
	
}
