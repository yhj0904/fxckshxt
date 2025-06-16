package kr.co.nanwe.auth.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.auth.service.AuthService;
import kr.co.nanwe.auth.service.AuthVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;

/**
 * @Class Name 		: SysAuthController
 * @Description 	: 권한 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="COM_AUTH", name="권한관리")
@Controller
public class SysAuthController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysAuthController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/auth";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/auth";
	
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
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Service */
	@Resource(name = "authService")
	private AuthService authService;
	
	/** Constructor */
	public SysAuthController() {		
		RequestMapping requestMapping = SysAuthController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	@RequestMapping(value = {"/sys/auth", "/sys/auth.do", "/sys/auth/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request){
		
		List<Map<String, Object>> authDvcdList = new ArrayList<Map<String, Object>>();
		if(web.isExternalUse()) {
			Map<String, Object> external = new HashMap<String, Object>();
			external.put("authDvcd", CodeConfig.EXTERANL_USER_CODE);
			external.put("authDvnm", CodeConfig.EXTERANL_USER_CODE_NM);
			authDvcdList.add(external);
		}
		Map<String, Object> comUser = new HashMap<String, Object>();
		comUser.put("authDvcd", CodeConfig.COM_USER_CODE);
		comUser.put("authDvnm", CodeConfig.COM_USER_CODE_NM);
		authDvcdList.add(comUser);
		model.addAttribute("authDvcdList", authDvcdList);
		
		List<AuthVO> authList = authService.selectAuthList();
		model.addAttribute("authList", authList);
		
		//권한VO 생성
		AuthVO authVO = new AuthVO();
		model.addAttribute("authVO", authVO);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 권한화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, AuthVO authVO ,BindingResult authBindingResult) {
		
		List<Map<String, Object>> authDvcdList = new ArrayList<Map<String, Object>>();
		if(web.isExternalUse()) {
			Map<String, Object> external = new HashMap<String, Object>();
			external.put("authDvcd", CodeConfig.EXTERANL_USER_CODE);
			external.put("authDvnm", CodeConfig.EXTERANL_USER_CODE_NM);
			authDvcdList.add(external);
		}
		Map<String, Object> comUser = new HashMap<String, Object>();
		comUser.put("authDvcd", CodeConfig.COM_USER_CODE);
		comUser.put("authDvnm", CodeConfig.COM_USER_CODE_NM);
		authDvcdList.add(comUser);
		model.addAttribute("authDvcdList", authDvcdList);
		
		//조회결과 MODEL ADD
		List<AuthVO> authList = authService.selectAuthList();
		model.addAttribute("authList", authList);
		
		//유효성 검증
		beanValidator.validate(authVO, authBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (authBindingResult.hasErrors()) {
			model.addAttribute("authVO", authVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(authVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//중복 코드 검사
		if(authService.selectAuthCdExist(authVO.getAuthCd())) {
			addtionalValid = false;
			authBindingResult.rejectValue("cd", "message.alert.existdata");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("authVO", authVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = authService.insertAuth(authVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnSuccess();
			
		} else { //실패시			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();			
		}		
	}
	
	/**
	 * 권한화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, AuthVO authVO ,BindingResult authBindingResult) {
		
		List<Map<String, Object>> authDvcdList = new ArrayList<Map<String, Object>>();
		if(web.isExternalUse()) {
			Map<String, Object> external = new HashMap<String, Object>();
			external.put("authDvcd", CodeConfig.EXTERANL_USER_CODE);
			external.put("authDvnm", CodeConfig.EXTERANL_USER_CODE_NM);
			authDvcdList.add(external);
		}
		Map<String, Object> comUser = new HashMap<String, Object>();
		comUser.put("authDvcd", CodeConfig.COM_USER_CODE);
		comUser.put("authDvnm", CodeConfig.COM_USER_CODE_NM);
		authDvcdList.add(comUser);
		model.addAttribute("authDvcdList", authDvcdList);
		
		//조회결과 MODEL ADD
		List<AuthVO> authList = authService.selectAuthList();
		model.addAttribute("authList", authList);
		
		//유효성 검증
		beanValidator.validate(authVO, authBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (authBindingResult.hasErrors()) {
			model.addAttribute("authVO", authVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//유효성 검사 로직 작성
		if(authVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = authService.updateAuth(authVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");			
			return web.returnSuccess();			
		} else { //실패시			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");			
			return web.returnError();
		}
	}
	
	/**
	 * 권한화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, AuthVO authVO) {
				
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(authVO == null) {			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = authService.deleteAuth(authVO.getAuthCd());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시					
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");	
			return web.returnSuccess();			
		} else { //실패시			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();				
		}
		
	}
	
	/**
	 * 권한화면 부서목록
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/deptList.do")
	@ProgramInfo(code="DEPT_LIST", name="부서 목록조회")
	public String deptList(Model model, HttpServletRequest request){
		List<Map<String, Object>> authDvcdList = new ArrayList<Map<String, Object>>();
		if(web.isExternalUse()) {
			Map<String, Object> external = new HashMap<String, Object>();
			external.put("authDvcd", CodeConfig.EXTERANL_USER_CODE);
			external.put("authDvnm", CodeConfig.EXTERANL_USER_CODE_NM);
			authDvcdList.add(external);
		}
		Map<String, Object> comUser = new HashMap<String, Object>();
		comUser.put("authDvcd", CodeConfig.COM_USER_CODE);
		comUser.put("authDvnm", CodeConfig.COM_USER_CODE_NM);
		authDvcdList.add(comUser);
		model.addAttribute("authDvcdList", authDvcdList);
		return web.returnView(VIEW_PATH, "/pop", "POP");
	}
}
