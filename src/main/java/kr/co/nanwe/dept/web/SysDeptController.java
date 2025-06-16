package kr.co.nanwe.dept.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;

/**
 * @Class Name 		: SysDeptController
 * @Description 	: 부서코드 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="COM_DEPT", name="부서코드관리")
@Controller
public class SysDeptController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysDeptController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/dept";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/dept";
	
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
	
	/** Constructor */
	public SysDeptController() {		
		RequestMapping requestMapping = SysDeptController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 부서코드화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/dept.do", "/sys/dept/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = deptService.selectDeptList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		//부서코드 VO 생성
		DeptVO deptVO = new DeptVO();
		model.addAttribute("deptVO", deptVO);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 부서코드화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/dept/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,DeptVO deptVO ,BindingResult deptBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = deptService.selectDeptList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);

		//유효성 검증
		beanValidator.validate(deptVO, deptBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (deptBindingResult.hasErrors()) {
			model.addAttribute("deptVO", deptVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(deptVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//중복 코드 검사
		if(deptService.selectDeptCdExist(deptVO.getDeptCd())) {
			addtionalValid = false;
			deptBindingResult.rejectValue("deptCd", "message.alert.existdata");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("deptVO", deptVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = deptService.insertDept(deptVO);
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
	 * 부서코드화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/dept/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,DeptVO deptVO ,BindingResult deptBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(deptVO, deptBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (deptBindingResult.hasErrors()) {
			model.addAttribute("deptVO", deptVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(deptVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("deptVO", deptVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = deptService.updateDept(deptVO);
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
	 * 부서코드화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/dept/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search,DeptVO deptVO) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
				
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(deptVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = deptService.deleteDept(deptVO.getDeptCd());
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
	
}
