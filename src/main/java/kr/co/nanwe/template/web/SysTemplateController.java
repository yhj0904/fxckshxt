package kr.co.nanwe.template.web;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.template.service.TemplateCodeVO;
import kr.co.nanwe.template.service.TemplateMgtVO;
import kr.co.nanwe.template.service.TemplateService;

/**
 * @Class Name 		: SysTemplateController
 * @Description 	: 템플릿 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="COM_TEMPLATE", name="템플릿관리")
@Controller
public class SysTemplateController {
	
	/** View Path */
	private static final String VIEW_PATH = "sys/template";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/template";
	
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
	@Resource(name = "templateService")
	private TemplateService templateService;
	
	/** Constructor */
	public SysTemplateController() {		
		RequestMapping requestMapping = SysTemplateController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 템플릿관리 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/template.do", "/sys/template/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = templateService.selectTemplateList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 템플릿관리 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String templateCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		TemplateMgtVO templateMgtVO = templateService.selectTemplate(templateCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(templateMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("templateMgtVO", templateMgtVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 템플릿관리 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 생성 (템플릿 생성 폼)
		TemplateMgtVO templateMgtVO = templateService.getTemplateForm();
		
		//조회결과 MODEL ADD
		model.addAttribute("templateMgtVO", templateMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 템플릿관리 복사
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/copy.do")
	@ProgramInfo(code="COPY_FORM", name="복사폼 화면")
	public String copyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
							,@RequestParam(value = "sId", defaultValue="") String templateCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		TemplateMgtVO templateMgtVO = templateService.selectTemplateCopyForm(templateCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(templateMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("templateMgtVO", templateMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 템플릿관리 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,TemplateMgtVO templateMgtVO ,BindingResult templateBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(templateMgtVO, templateBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (templateBindingResult.hasErrors()) {
			model.addAttribute("templateMgtVO", templateMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(templateMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		if(templateService.selectTemplateCdExist(templateMgtVO.getTemplateCd())) {
			addtionalValid = false;
			templateBindingResult.rejectValue("templateCd", "message.alert.existdata");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("templateMgtVO", templateMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = templateService.insertTemplate(templateMgtVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", templateMgtVO.getTemplateCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 템플릿관리 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String templateCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		TemplateMgtVO templateMgtVO = templateService.selectTemplate(templateCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(templateMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("templateMgtVO", templateMgtVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 템플릿관리 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,TemplateMgtVO templateMgtVO ,BindingResult templateBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(templateMgtVO, templateBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (templateBindingResult.hasErrors()) {
			model.addAttribute("templateMgtVO", templateMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(templateMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("templateMgtVO", templateMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = templateService.updateTemplate(templateMgtVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", templateMgtVO.getTemplateCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", templateMgtVO.getTemplateCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 템플릿관리 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/removeAction.do")
	@ProgramInfo(code="REMOVE", name="삭제폼 화면")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String templateCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		TemplateMgtVO templateMgtVO = templateService.selectTemplate(templateCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(templateMgtVO == null) {			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");				
			return web.returnError();
		}
		
		//삭제전 해당 템플릿을 사용하는 사이트가 있는지 체크한다.
		if(templateService.checkSiteUseTemplate(templateMgtVO.getTemplateCd()) > 0) {
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("resultMsg", messageUtil.getMessage("errors.usedata"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = templateService.deleteTemplate(templateCd);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시	
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			return web.returnSuccess();
			
		} else { //실패시	
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", templateMgtVO.getTemplateCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 템플릿관리 코드 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/code/list.do")
	@ProgramInfo(code="CODE_LIST", name="코드 목록조회")
	public String codeList(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
							, @RequestParam(value = "templateCd", required=false) String templateCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("templateCd", templateCd);
		
		//목록조회
		Map<String, Object> map = templateService.selectTemplateCodeList(search, templateCd);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/code_list", "pop");
	}
	
	/**
	 * 템플릿관리 코드 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/code/view.do")
	@ProgramInfo(code="CODE_VIEW", name="코드 상세조회")
	public String codeView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "templateCd", defaultValue="") String templateCd
						,@RequestParam(value = "seq", defaultValue="0") int seq){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		TemplateCodeVO templateCodeVO = templateService.selectTemplateCode(templateCd, seq);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(templateCodeVO == null) {			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/code/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("templateCodeVO", templateCodeVO);
		
		return web.returnView(VIEW_PATH, "/code_view", "pop");
	}
	
	/**
	 * 템플릿관리 코드 복원
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/code/modifyAction.json", method = RequestMethod.POST)
	@ProgramInfo(code="CODE_MODIFY", name="코드 복원")
	@ResponseBody
	public boolean modifyAction( HttpServletRequest request , @RequestParam(value = "templateCd", defaultValue="") String templateCd, @RequestParam(value = "seq", defaultValue="0") int seq) {
		//수정 처리
		int result = templateService.updateTemplateByCode(templateCd, seq);		
		if(result > 0) { //성공시
			return true;
			
		}
		return false;		
	}
	
	/**
	 * 템플릿관리 미리보기
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/preview.do")
	@ProgramInfo(code="PREVIEW", name="미리보기")
	public String preview(Model model, HttpServletRequest request
							, @RequestParam(value = "sId", defaultValue="") String templateCd
							, @RequestParam(value = "type", defaultValue="MAIN") String type){
		
		//상세조회
		TemplateMgtVO templateMgtVO = templateService.selectTemplate(templateCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(templateMgtVO == null) {			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/code/list.do");			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("type", type.toUpperCase());
		model.addAttribute("sId", templateCd);
		
		return web.returnView(VIEW_PATH, "/preview/preview", "pop");
	}
	
	/**
	 * 템플릿관리 미리보기 프레임
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/template/previewFrame.do")
	public String previewFrame(Model model, HttpServletRequest request
							, @RequestParam(value = "sId", defaultValue="") String templateCd
							, @RequestParam(value = "type", defaultValue="MAIN") String type){		
		//조회결과 MODEL ADD
		model.addAttribute("GV_TEMPLATE_CODE", templateCd.toLowerCase());
		model.addAttribute("type", type.toUpperCase());
		
		return web.returnJsp(VIEW_PATH+ "/preview/preview_frame");
	}
}
