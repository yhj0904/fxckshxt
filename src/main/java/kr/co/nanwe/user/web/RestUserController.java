package kr.co.nanwe.user.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RsaUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.cnsler.service.CnslerVO;
import kr.co.nanwe.login.service.CertificationService;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: RestUserController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RestController
public class RestUserController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestUserController.class);
		
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
	@Resource(name = "userService")
	private UserService userService;
	
	/** Service */
	@Resource(name = "cnslerService")
	private CnslerService cnslerService;
	
	/** RSA UTIL */
	@Resource(name = "rsaUtil")
	private RsaUtil rsaUtil;
	
	@Resource(name = "certificationService")
	private CertificationService certificationService;
	
	/**
	 * 사용자화면 아이디 중복체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/user/isIdExist.json")
	public boolean isIdExist(HttpServletRequest request, @RequestParam(value = "sId", defaultValue="") String id){
		return userService.selectUserIdExist(id);
	}
	
	/**
	 * 사용자 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/sys/user/userList.json")
	public List<UserVO> userList(HttpServletRequest request){
		return userService.selectUserListByUse();
	}
	
	/**
	 * 상담원 가능 사용자 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/sys/user/cnslerUserList.json")
	public List<UserVO> selectCnslerUserList(HttpServletRequest request){
		return userService.selectCnslerUserList();
	}
	
	/**
	 * 부서별 상담원 조회 + 가능시간대조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/cnsl/selectCnslerByColg.json")
	public List<CnslerVO> selectCnslerByColg(HttpServletRequest request, SearchVO serach,  @RequestParam(value = "cnslColgCd", defaultValue="") String cnslColgCd, @RequestParam(value = "schDt", defaultValue="") String schDt){
		return cnslerService.selectCnslerByColg(serach, cnslColgCd, schDt);
	}
	
	/**
	 * 상담 가능시간 조회
	 * @param request
	 * @param serach
	 * @param cnslColgCd
	 * @param schDt
	 * @return
	 */
	@RequestMapping(value = "/cnsl/selectCnslerTmList.json")
	public List<CnslerVO> selectCnslerTmList(HttpServletRequest request, @RequestParam(value = "cnslerId", defaultValue="") String cnslerId, @RequestParam(value = "schDt", defaultValue="") String schDt){
		CnslerVO cnslerVO = new CnslerVO();
		cnslerVO.setCnslerId(cnslerId);
		cnslerVO.setSchDt(schDt);
		
		return cnslerService.selectCnslerTmList(cnslerVO);
	}
	
	/**
	 * RSA 갱신
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/user/initRsa.json")
	public String[] initRsa (HttpServletRequest request, HttpServletResponse response) {
		return rsaUtil.createRsaSession(request);
	}
	
	/**
	 * 2차인증 갱신
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/user/initCertification.json")
	@ResponseBody
	public Map<String, Object> initCertification (HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> param) {
		//회원가입 인증 사용시
		if(StringUtil.isEqual(web.getServerProp("server.user.join.cert.use"), "Y")) {
			String certType = StringUtil.isNullToString(web.getServerProp("server.user.join.cert.type"));
			//아이디가 없으면 아이피로 세팅
			String userId = StringUtil.isNullToString(param.get("userId"));
			if(StringUtil.isNull(userId)) {
				userId = ClientUtil.getUserIp();
			}
			String userPhone = null;
			String userEmail = null;
			if("PHONE".equals(certType) && param.get("phone") != null) {
				userPhone = (String) param.get("phone");
			} else if("EMAIL".equals(certType) && param.get("email") != null) {
				userEmail = (String) param.get("email");				
			}
			return certificationService.initCertification(request, response, certType, userId, userPhone, userEmail);
		}
		return null;
	}
	
	/**
	 * 2차인증 갱신
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/user/reCertification.json")
	public Map<String, Object> reCertification (HttpServletRequest request, HttpServletResponse response) {
		return certificationService.refreshCertification(request, response, web.getServerProp("server.user.join.cert.type"));
	}
	
	/**
	 * 2차인증 체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/user/checkCertification.json")
	public Map<String, Object> checkCertification (HttpServletRequest request, @RequestParam(value = "certKey", defaultValue="") String certKey) {
		return certificationService.checkCertification(request, web.getServerProp("server.user.join.cert.type"), certKey);
	}
}
