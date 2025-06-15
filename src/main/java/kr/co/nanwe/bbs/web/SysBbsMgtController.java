package kr.co.nanwe.bbs.web;

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
import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
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
 * @Class Name 		: SysBbsMgtController
 * @Description 	: 게시판관리 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/bbsMgt")
@Program(code="COM_BBS_MGT", name="게시판관리")
@Controller
public class SysBbsMgtController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysBbsMgtController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/bbsMgt";
	
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
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;
	
	/** Service */
	@Resource(name = "authService")
	private AuthService authService;
	
	/** Constructor */
	public SysBbsMgtController() {		
		RequestMapping requestMapping = SysBbsMgtController.class.getAnnotation(RequestMapping.class);
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
	 * 게시판관리화면 목록조회
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
		Map<String, Object> map = bbsMgtService.selectBbsMgtList(search);	
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 게시판관리화면 상세조회
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
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bbsMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 게시판관리화면 등록폼 화면
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
		
		//게시판 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("BBSSKIN");
		model.addAttribute("skinCode", skinCode);
		
		//VO 생성
		BbsMgtVO bbsMgtVO = new BbsMgtVO();
		
		//기본값 세팅
		bbsMgtVO.setPageCnt(10);
		bbsMgtVO.setRowCnt(10);
		bbsMgtVO.setFileYn("N");
		bbsMgtVO.setFileCnt(2);
		bbsMgtVO.setFileSize("2000000"); //2MB
		bbsMgtVO.setCateYn("N");
		bbsMgtVO.setReplyYn("N");
		bbsMgtVO.setCmntYn("N");
		bbsMgtVO.setEditorYn("N");
		bbsMgtVO.setEditorFileYn("N");
		bbsMgtVO.setNoticeYn("N");
		bbsMgtVO.setNoticeRowCnt(5);
		bbsMgtVO.setSecretYn("N");
		bbsMgtVO.setNonameYn("N");
		bbsMgtVO.setSupplYn("N");
		bbsMgtVO.setSuppl01Yn("N");
		bbsMgtVO.setSuppl02Yn("N");
		bbsMgtVO.setSuppl03Yn("N");
		bbsMgtVO.setSuppl04Yn("N");
		bbsMgtVO.setSuppl05Yn("N");
		bbsMgtVO.setSuppl06Yn("N");
		bbsMgtVO.setSuppl07Yn("N");
		bbsMgtVO.setSuppl08Yn("N");
		bbsMgtVO.setSuppl09Yn("N");
		bbsMgtVO.setSuppl10Yn("N");
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 게시판관리화면 복사폼 화면
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
		
		//게시판 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("BBSSKIN");
		model.addAttribute("skinCode", skinCode);
		
		//상세조회
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bbsMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
							
			return web.returnError();
		}
		
		//코드 초기화
		bbsMgtVO.setCode("");
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 게시판관리화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,BbsMgtVO bbsMgtVO ,BindingResult bbsMgtBindingResult
								,@RequestParam(value = "bbsListAuth", required=false) String[] listAuth
								,@RequestParam(value = "bbsViewAuth", required=false) String[] viewAuth
								,@RequestParam(value = "bbsRegiAuth", required=false) String[] regiAuth
								,@RequestParam(value = "bbsReplyAuth", required=false) String[] replyAuth
								,@RequestParam(value = "bbsCmntAuth", required=false) String[] cmntAuth) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//게시판 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("BBSSKIN");
		model.addAttribute("skinCode", skinCode);

		//유효성 검증
		beanValidator.validate(bbsMgtVO, bbsMgtBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bbsMgtBindingResult.hasErrors()) {
			model.addAttribute("bbsMgtVO", bbsMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		} 
		if(bbsMgtService.selectBbsMgtCodeExist(bbsMgtVO.getCode())) {
			addtionalValid = false;
			bbsMgtBindingResult.rejectValue("code", "message.alert.existdata");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bbsMgtVO", bbsMgtVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsMgtService.insertBbsMgt(bbsMgtVO, listAuth, viewAuth, regiAuth, replyAuth, cmntAuth);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");

			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");

			return web.returnError();
			
		}
		
	}
	
	/**
	 * 게시판관리화면 수정폼 화면
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
		
		//게시판 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("BBSSKIN");
		model.addAttribute("skinCode", skinCode);
		
		//상세조회
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bbsMgtVO == null) {
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
							
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 게시판관리화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,BbsMgtVO bbsMgtVO ,BindingResult bbsMgtBindingResult
									,@RequestParam(value = "bbsListAuth", required=false) String[] listAuth
									,@RequestParam(value = "bbsViewAuth", required=false) String[] viewAuth
									,@RequestParam(value = "bbsRegiAuth", required=false) String[] regiAuth
									,@RequestParam(value = "bbsReplyAuth", required=false) String[] replyAuth
									,@RequestParam(value = "bbsCmntAuth", required=false) String[] cmntAuth) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//권한 목록 조회
		List<AuthVO> authList = authService.selectAuthUseList();
		model.addAttribute("authList", authList);
		
		//게시판 스킨 코드 조회
		CommCdVO skinCode = commCdService.selectCommCd("BBSSKIN");
		model.addAttribute("skinCode", skinCode);

		//유효성 검증
		beanValidator.validate(bbsMgtVO, bbsMgtBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bbsMgtBindingResult.hasErrors()) {
			model.addAttribute("bbsMgtVO", bbsMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsMgtVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bbsMgtVO", bbsMgtVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsMgtService.updateBbsMgt(bbsMgtVO, listAuth, viewAuth, regiAuth, replyAuth, cmntAuth);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
						
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 게시판관리화면 삭제처리
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
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgt(id);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(bbsMgtVO == null) {
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsMgtService.deleteBbsMgt(bbsMgtVO.getCode());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
						
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시			
						
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
						
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsMgtVO.getCode());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
		
}
