package kr.co.nanwe.push.web;

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

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.push.service.PushGrpMstVO;
import kr.co.nanwe.push.service.PushGrpService;

/**
 * @Class Name 		: PushGroupController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RequestMapping(value = "/push/group")
@Program(code="PUSH_GROUP", name="그룹")
@Controller
public class PushGroupController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(PushGroupController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/push/group";
	
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
	
	/** Service */
	@Resource(name = "pushGrpService")
	private PushGrpService pushGrpService;
	
	/** Constructor */
	public PushGroupController() {		
		RequestMapping requestMapping = PushGroupController.class.getAnnotation(RequestMapping.class);
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
	 * 그룹 목록조회
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
		Map<String, Object> map = pushGrpService.selectPushGrpMstListByLoginUser(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 그룹 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search, @RequestParam(value = "sId", defaultValue="") String grpCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushGrpMstVO pushGrpMstVO = pushGrpService.selectPushGrpMstByLoginUser(grpCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushGrpMstVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//그룹원 목록
		pushGrpMstVO.setMemList(pushGrpService.selectPushGrpMemListByLoginUser(pushGrpMstVO.getGrpCd()));
		
		//조회결과 MODEL ADD
		model.addAttribute("pushGrpMstVO", pushGrpMstVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 그룹관리 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 생성
		PushGrpMstVO pushGrpMstVO = new PushGrpMstVO();
		
		//조회결과 MODEL ADD
		model.addAttribute("pushGrpMstVO", pushGrpMstVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 그룹관리 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,PushGrpMstVO pushGrpMstVO ,BindingResult pushGrpMstBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(pushGrpMstVO, pushGrpMstBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (pushGrpMstBindingResult.hasErrors()) {
			model.addAttribute("pushGrpMstVO", pushGrpMstVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(pushGrpMstVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("pushGrpMstVO", pushGrpMstVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushGrpService.insertPushGrpMst(pushGrpMstVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushGrpMstVO.getGrpCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 그룹관리 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search ,@RequestParam(value = "sId", defaultValue="") String grpCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushGrpMstVO pushGrpMstVO = pushGrpService.selectPushGrpMstByLoginUser(grpCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushGrpMstVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//그룹원 목록
		pushGrpMstVO.setMemList(pushGrpService.selectPushGrpMemListByLoginUser(pushGrpMstVO.getGrpCd()));
		
		//조회결과 MODEL ADD
		model.addAttribute("pushGrpMstVO", pushGrpMstVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 그룹관리 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search,PushGrpMstVO pushGrpMstVO ,BindingResult pushGrpMstBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(pushGrpMstVO, pushGrpMstBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (pushGrpMstBindingResult.hasErrors()) {
			model.addAttribute("pushGrpMstVO", pushGrpMstVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(pushGrpMstVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("pushGrpMstVO", pushGrpMstVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushGrpService.updatePushGrpMst(pushGrpMstVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushGrpMstVO.getGrpCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushGrpMstVO.getGrpCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 그룹관리 삭제 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeAction.do")
	@ProgramInfo(code="REMOVE", name="삭제폼 화면")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String grpCd){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		PushGrpMstVO pushGrpMstVO = pushGrpService.selectPushGrpMstByLoginUser(grpCd);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(pushGrpMstVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushGrpService.deletePushGrpMst(grpCd);
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
			resultParam.put("sId", pushGrpMstVO.getGrpCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
	}
	
	/**
	 * 그룹관리 선택 삭제처리
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
		int result = pushGrpService.deleteCheckedPushGrpMstByLoginUser(checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		
		return web.returnSuccess();
		
	}
	
}
