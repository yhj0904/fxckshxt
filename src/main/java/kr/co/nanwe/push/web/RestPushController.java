package kr.co.nanwe.push.web;

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
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.push.service.PushAppUserService;
import kr.co.nanwe.push.service.PushGrpMembVO;
import kr.co.nanwe.push.service.PushGrpMstVO;
import kr.co.nanwe.push.service.PushGrpService;
import kr.co.nanwe.push.service.PushNoticeService;

/**
 * @Class Name 		: RestPushController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RequestMapping(value = "/push")
@RestController
public class RestPushController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestPushController.class);
		
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
	@Resource(name = "pushAppUserService")
	private PushAppUserService pushAppUserService;
	
	/** Service */
	@Resource(name = "pushGrpService")
	private PushGrpService pushGrpService;
	
	/** Service */
	@Resource(name = "pushNoticeService")
	private PushNoticeService pushNoticeService;
	
	/**
	 * 사용자 토큰 등록
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/users/app.json")
	public Map<String, Object> registerAppToken (HttpServletRequest request
									, @RequestParam(value = "userId", defaultValue="") String userId
									, @RequestParam(value = "token", defaultValue="") String token){
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false; //등록결과
		String errCode = ""; 
		
		//파라미터 검증
		if(!StringUtil.isNull(userId) && !StringUtil.isNull(token)) {
			boolean isExist = pushAppUserService.selectPushAppUserExist(userId, token);
			if(!isExist) {
				int insertResult = pushAppUserService.registPushAppUser(userId, token);
				if(insertResult > 0) {
					result = true;
				} else {
					errCode = "ERR_FAIL"; //등록 실패
				}
			} else {
				errCode = "ERR_EXIST"; //기등록된 사용자
			}
		} else {
			errCode = "ERR_PARAM"; //파라미터 오류
		}
		
		map.put("result", result);
		if(!result) {
			map.put("errCode", errCode);
		}
		return map;
	}
	
	/**
	 * 주소록 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/search/grp/list.json")
	public List<PushGrpMstVO> grpMstList (HttpServletRequest request){
		return pushGrpService.selectPushGrpMstAllListByLoginUser();
	}
	
	/**
	 * 주소록 그룹원 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/search/grp/memList.json")
	public List<PushGrpMembVO> grpMemList (HttpServletRequest request, @RequestParam(value = "grpCd", defaultValue="") String grpCd){
		return pushGrpService.selectPushGrpMemListByLoginUser(grpCd);
	}
	
	/**
	 * 부서 사용자 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/search/grp/memListByCd.json")
	public List<PushGrpMembVO> getMemListByCd(HttpServletRequest request, @RequestParam(value = "grpCd[]", required=false) List<String> grpCdList){
		return pushGrpService.selectPushGrpMemListByGrpCdList(grpCdList);
	}

	
	/**
	 * 발송건별확인
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/getSendCount.json")
	public int getSendCount(HttpServletRequest request, @RequestParam(value = "type", defaultValue="") String type){
		if(StringUtil.isNull(type)) {
			return 0;
		}
		return pushNoticeService.selectPushNoticeSendCountByState(type);
	}

	
	/**
	 * 캘린더 발송 카운트 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/getPushCountByDate.json")
	public Map<String, Object> getPushCountByDate(HttpServletRequest request, @RequestParam(value = "date", defaultValue="") String date){
		return pushNoticeService.selectPushNoticeSendCountByDate(date);
	}
}
