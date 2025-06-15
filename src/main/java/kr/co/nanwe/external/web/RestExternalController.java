package kr.co.nanwe.external.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.external.service.ExternalService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: RestExternalController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RestController
public class RestExternalController {
	
	/** ExternalService */
	@Resource(name = "externalService")
	private ExternalService externalService;	
	
	/**
	 * 사용자 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value = "/external/search/userList.json")
	public List<UserVO> userList(HttpServletRequest request){		
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
		Map<String, Object> map = externalService.selectUserList(paramMap);
		return (List<UserVO>) map.get("list");
	}
	
	/**
	 * 부서 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/external/search/deptList.json")
	public List<DeptVO> deptList(HttpServletRequest request){		
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
		return (List<DeptVO>) map.get("list");
	}
	
	/**
	 * 최하위 부서 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/external/search/childDeptList.json")
	public List<DeptVO> childDeptList(HttpServletRequest request){		
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
		Map<String, Object> map = externalService.selectChildDeptList(paramMap);
		return (List<DeptVO>) map.get("list");
	}
	
	/**
	 * 부서 사용자 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/external/search/deptUserList.json")
	public List<UserVO> deptUserList(HttpServletRequest request, @RequestParam(value = "deptCd[]", required=false) List<String> deptCdList){
		return externalService.selectUserListByDeptList(deptCdList);
	}
	
	/**
	 * REST SERVICE
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/external/searchOne.json")
	public Map<String, Object> searchOne(HttpServletRequest request
										, @RequestParam(value = "sqlId", required=false) String sqlId
										, @RequestParam(value = "param", required=false) Object param){
		return externalService.searchOne(sqlId, param);
	}
	
	/**
	 * REST SERVICE
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/external/searchList.json")
	public List<Map<String, Object>> searchList(HttpServletRequest request
										, @RequestParam(value = "sqlId", required=false) String sqlId
										, @RequestParam(value = "param", required=false) Object param){
		return externalService.searchList(sqlId, param);
	}
}