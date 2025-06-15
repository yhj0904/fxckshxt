package kr.co.nanwe.banner.web;

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
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.banner.service.BannerService;
import kr.co.nanwe.banner.service.BannerVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;

/**
 * @Class Name 		: SysBannerController
 * @Description 	: 배너 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/banner")
@Program(code="COM_BANNER", name="배너관리")
@Controller
public class SysBannerController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysBannerController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/banner";
	
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
	@Resource(name = "bannerService")
	private BannerService bannerService;
	
	/** Constructor */
	public SysBannerController() {		
		RequestMapping requestMapping = SysBannerController.class.getAnnotation(RequestMapping.class);
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
	 * 배너화면 목록조회
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
		Map<String, Object> map = bannerService.selectBannerList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 배너화면 상세조회
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
		
		//공통 코드 조회
		CommCdVO commCd = commCdService.selectCommCd("BANNER");
		model.addAttribute("commCd", commCd);
		
		//상세조회
		BannerVO bannerVO = bannerService.selectBanner(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bannerVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("bannerVO", bannerVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 배너화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//공통 코드 조회
		CommCdVO commCd = commCdService.selectCommCd("BANNER");
		model.addAttribute("commCd", commCd);
		
		//VO 생성
		BannerVO bannerVO = new BannerVO();
		
		//기본값 세팅
		bannerVO.setUseYn("Y");
		bannerVO.setStartDttm(DateUtil.getDate());
		bannerVO.setEndDttm(DateUtil.getDate());
		
		//조회결과 MODEL ADD
		model.addAttribute("bannerVO", bannerVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 배너화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,BannerVO bannerVO ,BindingResult bannerBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//공통 코드 조회
		CommCdVO commCd = commCdService.selectCommCd("BANNER");
		model.addAttribute("commCd", commCd);

		//유효성 검증
		beanValidator.validate(bannerVO, bannerBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bannerBindingResult.hasErrors()) {
			model.addAttribute("bannerVO", bannerVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bannerVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(bannerVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(bannerVO.getUploadFile()))) {
			addtionalValid = false;
			bannerBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bannerVO", bannerVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bannerService.insertBanner(bannerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
					
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bannerVO.getBannerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시

			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");

			return web.returnError();
			
		}
		
	}
	
	/**
	 * 배너화면 수정폼 화면
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
		
		//공통 코드 조회
		CommCdVO commCd = commCdService.selectCommCd("BANNER");
		model.addAttribute("commCd", commCd);
		
		//상세조회
		BannerVO bannerVO = bannerService.selectBanner(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bannerVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("bannerVO", bannerVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 배너화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,BannerVO bannerVO ,BindingResult bannerBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//공통 코드 조회
		CommCdVO commCd = commCdService.selectCommCd("BANNER");
		model.addAttribute("commCd", commCd);

		//유효성 검증
		beanValidator.validate(bannerVO, bannerBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bannerBindingResult.hasErrors()) {
			model.addAttribute("bannerVO", bannerVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bannerVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(bannerVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(bannerVO.getUploadFile()))) {
			addtionalValid = false;
			bannerBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bannerVO", bannerVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bannerService.updateBanner(bannerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
					
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bannerVO.getBannerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bannerVO.getBannerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 배너화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do")
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		BannerVO bannerVO = bannerService.selectBanner(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bannerVO == null) {
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
							
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bannerService.deleteBanner(id);
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
			resultParam.put("sId", bannerVO.getBannerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 배너화면 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			
			//결과메시지  (생략가능)
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = bannerService.deleteCheckedBanner(checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

		return web.returnSuccess();
		
	}	
}
