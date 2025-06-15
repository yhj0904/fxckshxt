package kr.co.nanwe.prog.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import com.ibm.icu.text.SimpleDateFormat;
import com.mysql.cj.Session;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgUserVO;
import kr.co.nanwe.prog.service.ProgVO;
import kr.co.nanwe.prog.service.impl.ProgServiceImpl;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: RestProgController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.16		신한나			최초생성
 */

@RequestMapping(value = "/prog")
@RestController
public class RestProgController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestProgController.class);
		
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
	@Resource(name = "progService")
	private ProgService progService;
	
	/** Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/**
	 * 사용자 프로그램 신청
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/apply.json")
	public Map<String, Object> apply (HttpServletRequest request
									, @RequestParam(value = "progId", defaultValue="") int progId){
		//RESULT
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//id 없을 경우
		if(progId == 0) {
			resultMap.put("result", "");
			resultMap.put("resultMsg", messageUtil.getMessage("message.program.error"));
			resultMap.put("resultCode", "ERROR_CREATE");
			return resultMap;
		}
		
		//신청인원, 마감일 체크
		ProgVO program = progService.selectProgUserView(progId);
		if(program == null) {
			resultMap.put("result", "");
			resultMap.put("resultMsg", messageUtil.getMessage("message.program.nodata"));
			resultMap.put("resultCode", "ERROR_CREATE");
			return resultMap;
		} else {
			int rqNmpr = progService.selectApplUserTotCnt(progId);
			if(program.getReqstNmpr() <= rqNmpr) {
				resultMap.put("result", "");
				resultMap.put("resultMsg", messageUtil.getMessage("message.program.end"));
				resultMap.put("resultCode", "ERROR_END");
				return resultMap;
			}
			
			//신청일 체크
			boolean checkDate = false;
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.");
			//현재날짜
			Date currentTime = new Date ();
			String current = format.format(currentTime);
			try {
				Date today = format.parse(current);
				String startDate = program.getReqstSdt();
				String endDate = program.getReqstEdt();			
				Date start = format.parse(startDate);            
				Date end = format.parse(endDate);
				int startResult = today.compareTo(start);
				int endResult = end.compareTo(today);			
				if(startResult >= 0 && endResult >= 0) {
					checkDate = true;
				}
				
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
				checkDate = false;
			}
			
			if(!checkDate) {
				resultMap.put("result", "");
				resultMap.put("resultMsg", messageUtil.getMessage("message.program.end"));
				resultMap.put("resultCode", "ERROR_END");
				return resultMap;
			}
		}

		resultMap.put("result", "");
		//resultMap.put("resultMsg", messageUtil.getMessage("message.program.success"));
		resultMap.put("resultMsg", messageUtil.getMessage("message.success.search"));
		resultMap.put("resultCode", "OK");
		return resultMap;
	}
	
}
