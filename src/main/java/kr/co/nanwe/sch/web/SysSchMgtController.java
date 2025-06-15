package kr.co.nanwe.sch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.auth.service.AuthService;
import kr.co.nanwe.auth.service.AuthVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.sch.service.SchMgtService;
import kr.co.nanwe.sch.service.SchMgtVO;

/**
 * @Class Name 		: SysSchMgtController
 * @Description 	: 일정관리 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/schMgt")
@Program(code="COM_SCH_MGT", name="일정관리")
@Controller
public class SysSchMgtController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysSchMgtController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/schMgt";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Service */
	@Resource(name = "schMgtService")
	private SchMgtService schMgtService;
	
	/** Service */
	@Resource(name = "authService")
	private AuthService authService;
	
	/** Constructor */
	public SysSchMgtController() {		
		RequestMapping requestMapping = SysSchMgtController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/** Root Forward */
	@RequestMapping(value = "")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/list.do");
	}
	
	/**
	 * 일정관리화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = schMgtService.selectSchMgtList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 일정관리화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SchMgtVO schMgtVO = schMgtService.selectSchMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(schMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("schMgtVO", schMgtVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 일정관리화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//일정 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("SCHSKIN");
		model.addAttribute("skinCode", skinCode);
		
		//VO 생성
		SchMgtVO schMgtVO = new SchMgtVO();
		
		//기본값 세팅
		schMgtVO.setFileYn("N");
		schMgtVO.setFileCnt(2);
		schMgtVO.setFileSize("2000000"); //2MB
		schMgtVO.setCateYn("N");
		schMgtVO.setSupplYn("N");
		schMgtVO.setSuppl01Yn("N");
		schMgtVO.setSuppl02Yn("N");
		schMgtVO.setSuppl03Yn("N");
		schMgtVO.setSuppl04Yn("N");
		schMgtVO.setSuppl05Yn("N");
		schMgtVO.setSuppl06Yn("N");
		schMgtVO.setSuppl07Yn("N");
		schMgtVO.setSuppl08Yn("N");
		schMgtVO.setSuppl09Yn("N");
		schMgtVO.setSuppl10Yn("N");
		
		//조회결과 MODEL ADD
		model.addAttribute("schMgtVO", schMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 일정관리화면 복사폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/copy.do")
	@ProgramInfo(code="COPY_FORM", name="복사폼 화면")
	public String copyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//일정 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("SCHSKIN");
		model.addAttribute("skinCode", skinCode);
		
		//상세조회
		SchMgtVO schMgtVO = schMgtService.selectSchMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(schMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
							
			return web.returnError();
		}
		
		//코드 초기화
		schMgtVO.setCode("");
		
		//조회결과 MODEL ADD
		model.addAttribute("schMgtVO", schMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 일정관리화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,SchMgtVO schMgtVO ,BindingResult schMgtBindingResult
								,@RequestParam(value = "schListAuth", required=false) String[] listAuth
								,@RequestParam(value = "schViewAuth", required=false) String[] viewAuth
								,@RequestParam(value = "schRegiAuth", required=false) String[] regiAuth) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//일정 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("SCHSKIN");
		model.addAttribute("skinCode", skinCode);

		//유효성 검증
		beanValidator.validate(schMgtVO, schMgtBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (schMgtBindingResult.hasErrors()) {
			model.addAttribute("schMgtVO", schMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(schMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		} 
		if(schMgtService.selectSchMgtCodeExist(schMgtVO.getCode())) {
			addtionalValid = false;
			schMgtBindingResult.rejectValue("code", "message.alert.existdata");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("schMgtVO", schMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = schMgtService.insertSchMgt(schMgtVO, listAuth, viewAuth, regiAuth);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");

			return web.returnError();
			
		}
		
	}
	
	/**
	 * 일정관리화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//일정 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("SCHSKIN");
		model.addAttribute("skinCode", skinCode);
		
		//상세조회
		SchMgtVO schMgtVO = schMgtService.selectSchMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(schMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
							
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("schMgtVO", schMgtVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 일정관리화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,SchMgtVO schMgtVO ,BindingResult schMgtBindingResult
									,@RequestParam(value = "schListAuth", required=false) String[] listAuth
									,@RequestParam(value = "schViewAuth", required=false) String[] viewAuth
									,@RequestParam(value = "schRegiAuth", required=false) String[] regiAuth) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//일정 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("SCHSKIN");
		model.addAttribute("skinCode", skinCode);

		//유효성 검증
		beanValidator.validate(schMgtVO, schMgtBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (schMgtBindingResult.hasErrors()) {
			model.addAttribute("schMgtVO", schMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(schMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("schMgtVO", schMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = schMgtService.updateSchMgt(schMgtVO, listAuth, viewAuth, regiAuth);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
						
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 일정관리화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//VO 조회
		SchMgtVO schMgtVO = schMgtService.selectSchMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(schMgtVO == null) {
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = schMgtService.deleteSchMgt(schMgtVO.getCode());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
						
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시			
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
						
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
		
}
