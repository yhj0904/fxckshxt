package kr.co.nanwe.external.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.external.service.ExternalService;

/**
 * @Class Name 		: ExternalController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class ExternalController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ExternalController.class);
		
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "externalService")
	private ExternalService externalService;

	
	/**
	 * 사용자 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/external/search/userList.do")
	public String userList(Model model, HttpServletRequest request){		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<String> params = request.getParameterNames();
		if(params != null) {
			while (params.hasMoreElements()){
				String name = (String)params.nextElement();	
				//전자정부 중복방지 파라미터는 continue;
				if("egovframework.double.submit.preventer.parameter.name".equals(name)) {
					continue;
				}
				//값이 없는 경우 continue
				if(StringUtil.isNull(request.getParameter(name))) {
					continue;
				}
				paramMap.put(name, request.getParameter(name));
			}
		}
		model.addAllAttributes(paramMap);
		Map<String, Object> map = externalService.selectUserList(paramMap);
		model.addAllAttributes(map);
		return web.returnView("/external/search", "/user_list", "POP");
	}	
	
	/**
	 * 부서 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/external/search/deptList.do")
	public String deptList(Model model, HttpServletRequest request){		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<String> params = request.getParameterNames();
		if(params != null) {
			while (params.hasMoreElements()){
				String name = (String)params.nextElement();	
				//전자정부 중복방지 파라미터는 continue;
				if("egovframework.double.submit.preventer.parameter.name".equals(name)) {
					continue;
				}
				//값이 없는 경우 continue
				if(StringUtil.isNull(request.getParameter(name))) {
					continue;
				}
				paramMap.put(name, request.getParameter(name));
			}
		}
		Map<String, Object> map = externalService.selectDeptList(paramMap);
		model.addAllAttributes(map);
		return web.returnView("/external/search", "/dept_list", "POP");
	}
}
