package kr.co.nanwe.dept.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.external.service.ExternalService;

/**
 * @Class Name 		: RestDeptController
 * @Description 	: Rest Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			학과/단과대학 조회 추가
 */

@RequestMapping(value = "/dept")
@RestController
public class RestDeptController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestDeptController.class);
		
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "deptService")
	private DeptService deptService;
	
	/** ExternalService */
	@Resource(name = "externalService")
	private ExternalService externalService;
	
	/**
	 * 부서코드 상세정보
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/view.json")
	public DeptVO view(HttpServletRequest request, @RequestParam(value = "deptCd", defaultValue="") String deptCd){
		return deptService.selectDept(deptCd);
	}	
	
	/**
	 * 부서코드 중복체크
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/isCdExist.json")
	public boolean isCdExist(HttpServletRequest request, @RequestParam(value = "deptCd", defaultValue="") String deptCd){
		return deptService.selectDeptCdExist(deptCd);
	}
	
	/**
	 * 부서 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getDeptList.json")
	public List<DeptVO> getDeptList(HttpServletRequest request, @RequestBody Map<String, Object> paramMap){
		
		List<DeptVO> list = null;
		if(paramMap == null) {
			return null;
		}		
		String authDvcd = "";
		if(paramMap.get("authDvcd") != null) authDvcd = (String) paramMap.get("authDvcd");		
		if(web.isExternalUse() && CodeConfig.EXTERANL_USER_CODE.equals(authDvcd)) {
			Map<String, Object> map = externalService.selectDeptList(paramMap);
			if(map.get("list") != null) {
				list = (List<DeptVO>) map.get("list");
			}
		} else if (CodeConfig.COM_USER_CODE.equals(authDvcd)) {
			list = deptService.selectDeptListByUse();
		}		
		return list;
	}
	
	/**
	 * 부서 목록 조회 - 회원가입 시 사용
	 * @param 
	 * @return
	 * @exception 
	 */	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColgList.json")
	public List<DeptVO> getColgList(HttpServletRequest request, @RequestBody Map<String, Object> paramMap, SearchVO search){
		
		System.out.println("############---1111");
		List<DeptVO> list = null;
		if(paramMap == null) {
			System.out.println("############---11113333");
			return null;
		}
		System.out.println("############---11112222");
		String authDvcd = "";
		if(paramMap.get("authDvcd") != null) authDvcd = (String) paramMap.get("authDvcd");		
		if(web.isExternalUse() && CodeConfig.EXTERANL_USER_CODE.equals(authDvcd)) {
			System.out.println("############---111144444");
			Map<String, Object> map = externalService.selectDeptList(paramMap);
			if(map.get("list") != null) {
				list = (List<DeptVO>) map.get("list");
			}
		} else if (CodeConfig.COM_USER_CODE.equals(authDvcd)) {
			System.out.println("############---1111555");
			list = deptService.selectColgListByUse(search);
		}		
		return list;
	}
}
