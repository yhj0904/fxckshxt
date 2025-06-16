package kr.co.nanwe.site.web;

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

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.site.service.SiteService;
import kr.co.nanwe.site.service.SiteVO;
import kr.co.nanwe.template.service.TemplateMgtVO;
import kr.co.nanwe.template.service.TemplateService;

/**
 * @Class Name 		: SysSiteController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@Program(code="COM_SITE", name="사이트관리")
@Controller
public class SysSiteController {
	
	/** View Path */
	private static final String VIEW_PATH = "sys/site";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/site";
	
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
	@Resource(name = "siteService")
	private SiteService siteService;
	
	/** Service */
	@Resource(name = "templateService")
	private TemplateService templateService;
	
	/** Constructor */
	public SysSiteController() {		
		RequestMapping requestMapping = SysSiteController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 사이트화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/site.do", "/sys/site/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = siteService.selectSiteList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 사이트화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String siteCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SiteVO siteVO = siteService.selectSite(siteCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(siteVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("siteVO", siteVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 사이트화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 생성
		SiteVO siteVO = new SiteVO();
		
		//기본값 세팅
		siteVO.setUseYn("Y");
		siteVO.setSysAccessYn("N");
		
		//템플릿 목록
		List<TemplateMgtVO> templateList = templateService.selectTemplateUseList();
		model.addAttribute("templateList", templateList);
		
		//조회결과 MODEL ADD
		model.addAttribute("siteVO", siteVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 사이트화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,SiteVO siteVO ,BindingResult siteBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//템플릿 목록
		List<TemplateMgtVO> templateList = templateService.selectTemplateUseList();
		model.addAttribute("templateList", templateList);

		//유효성 검증
		beanValidator.validate(siteVO, siteBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (siteBindingResult.hasErrors()) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//유효성 검사 로직 작성
		if(siteVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		if(siteService.selectSiteCdExist(siteVO.getSiteCd())) {
			addtionalValid = false;
			siteBindingResult.rejectValue("siteCd", "message.alert.existdata");
		}
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(siteVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(siteVO.getUploadFile()))) {
			addtionalValid = false;
			siteBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = siteService.insertSite(siteVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
					
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시

			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");

			return web.returnError();
			
		}
		
	}
	
	/**
	 * 사이트화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String siteCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//템플릿 목록
		List<TemplateMgtVO> templateList = templateService.selectTemplateUseList();
		model.addAttribute("templateList", templateList);
		
		//상세조회
		SiteVO siteVO = siteService.selectSite(siteCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(siteVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("siteVO", siteVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 사이트화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,SiteVO siteVO ,BindingResult siteBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//템플릿 목록
		List<TemplateMgtVO> templateList = templateService.selectTemplateUseList();
		model.addAttribute("templateList", templateList);

		//유효성 검증
		beanValidator.validate(siteVO, siteBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (siteBindingResult.hasErrors()) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(siteVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(siteVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(siteVO.getUploadFile()))) {
			addtionalValid = false;
			siteBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = siteService.updateSite(siteVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
					
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 사이트화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/removeAction.do")
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String siteCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SiteVO siteVO = siteService.selectSite(siteCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(siteVO == null) {						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");							
			return web.returnError();
		}
		
		//메인사이트인 경우 삭제불가 처리
		if(CodeConfig.MAIN_SITE_CD.equals(siteVO.getSiteCd())) {
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			model.addAttribute("resultMsg", "메인페이지는 삭제가 불가능합니다.");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = siteService.deleteSite(siteCd);
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
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 사이트화면 코드수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/code.do")
	@ProgramInfo(code="CODE_FORM", name="코드수정폼 화면")
	public String codeView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String siteCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		SiteVO siteVO = siteService.selectSiteCode(siteCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(siteVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("siteVO", siteVO);
		
		return web.returnView(VIEW_PATH, "/code");
	}
	
	/**
	 * 사이트화면 코드수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/site/codeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="CODE_MODIFY", name="코드수정처리")
	public String codeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,SiteVO siteVO ,BindingResult siteBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(siteVO, siteBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (siteBindingResult.hasErrors()) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView(VIEW_PATH, "/code");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(siteVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("siteVO", siteVO);
			return web.returnView(VIEW_PATH, "/code");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = siteService.updateSiteCode(siteVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
					
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/code.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", siteVO.getSiteCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
}
