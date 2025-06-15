package kr.co.nanwe.code.web;

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
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;

/**
 * @Class Name 		: SysCommCdController
 * @Description 	: 공통코드 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/commCd")
@Program(code="COMM_CD", name="공통코드관리")
@Controller
public class SysCommCdController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(CommCdController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/commCd";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
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
	
	/** Constructor */
	public SysCommCdController() {		
		RequestMapping requestMapping = SysCommCdController.class.getAnnotation(RequestMapping.class);
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
	 * 공통코드화면 목록조회
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
		Map<String, Object> map = commCdService.selectCommCdList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		//공통코드 VO 생성
		CommCdVO commCdVO = new CommCdVO();
		model.addAttribute("commCdVO", commCdVO);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 공통코드화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,CommCdVO commCdVO ,BindingResult commCdBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = commCdService.selectCommCdList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);

		//유효성 검증
		beanValidator.validate(commCdVO, commCdBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (commCdBindingResult.hasErrors()) {
			model.addAttribute("commCdVO", commCdVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(commCdVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//중복 코드 검사
		if(commCdService.selectCommCdExist(commCdVO.getCd())) {
			addtionalValid = false;
			commCdBindingResult.rejectValue("cd", "message.alert.existdata");
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("commCdVO", commCdVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = commCdService.insertCommCd(commCdVO);
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
	 * 공통코드화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,CommCdVO commCdVO ,BindingResult commCdBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(commCdVO, commCdBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (commCdBindingResult.hasErrors()) {
			model.addAttribute("commCdVO", commCdVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(commCdVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("commCdVO", commCdVO);
			return web.returnView(VIEW_PATH, "/list");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = commCdService.updateCommCd(commCdVO);
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
	 * 공통코드화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search,CommCdVO commCdVO) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
				
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(commCdVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = commCdService.deleteCommCd(commCdVO.getCd());
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
