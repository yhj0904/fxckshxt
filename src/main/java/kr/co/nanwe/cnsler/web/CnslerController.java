package kr.co.nanwe.cnsler.web;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.co.nanwe.bbs.service.*;
import kr.co.nanwe.cmmn.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.cnsler.service.CnslerVO;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: BoardController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/cnsler")
@Program(code="CNSL", name="상담")
@Controller
public class CnslerController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/cnsler";

	/** Pw View Path */
	private static final String VIEW_PATH_SUB = "main/board";

	/** DEFAULT View Path */
	private static final String VIEW_PATH_DEFAULT = "/cnsler";
	
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
	
	/** 부서코드 Service */
	@Resource(name = "deptService")
	private DeptService deptService;
	
	/** 상담원 Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** 상담원 Service */
	@Resource(name = "cnslerService")
	private CnslerService cnslerService;
	
	/** Root Forward */
	@RequestMapping(value = "")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/list.do");
	}
	
	/**
	 * 게시글화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		LoginVO loginInfo = SessionUtil.getLoginUser(); 
		
		//로그인 사용자가 아닌경우
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.cnsllogin"));
//			model.addAttribute("redirectUrl", "/login.do");
//			return web.returnError();
//		}
		//로그인정보 및 아이피
//		String userId = "tmp";
//		if(loginInfo.getLoginId() != null) {
//			userId = loginInfo.getLoginId();
//		}
//		try {
		String userIp = ClientUtil.getUserIp();
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = cnslerService.selectMyCnslList(search, "tmp");
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 게시글화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
//	@RequestMapping(value = "/view.do")
//	@ProgramInfo(code="VIEW", name="상세조회")
//	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
//						,@RequestParam(value = "sId", defaultValue="") String id
//						,@RequestParam(value = "bbsPw", required = false) String pw){
//
//		LoginVO loginInfo = SessionUtil.getLoginUser();
//
//		//로그인 사용자가 아닌경우
////		if(loginInfo == null) {
////			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.cnsllogin"));
////			model.addAttribute("redirectUrl", "/login.do");
////			return web.returnError();
////		}
//		//로그인정보 및 아이피
////		String userId = loginInfo.getLoginId();
//		String userIp = ClientUtil.getUserIp();
//
//		//검색조건 MODEL ADD
//		model.addAttribute("search", search);
//
//		//상세조회
//		CnslerVO cnslerVO = cnslerService.selectCnsl(id);
//
//		//조회결과 MODEL ADD
//		model.addAttribute("cnslerVO", cnslerVO);
//
//		return web.returnView(VIEW_PATH, "/view");
//	}
	/**
	 * 게시글화면 상세조회
	 * @param
	 * @return
	 * @exception
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
			,@RequestParam(value = "sId", defaultValue="") String id
			,@RequestParam(value = "bbsPw", required = false) String pw){

		LoginVO loginInfo = SessionUtil.getLoginUser();

		String userIp = ClientUtil.getUserIp();

		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//상세조회
		Map<String, Object> boardMap = cnslerService.selectCnslSecret(id, pw);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			//비회원 작성글인 경우
			if(CodeConfig.NO_LOGIN.equals(errMsgCd)) {
				model.addAttribute("sId", id);
				model.addAttribute("formAction", VIEW_PATH_DEFAULT + "/view.do");
				return web.returnView(VIEW_PATH_SUB, "/password");
			} else {
				model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
				model.addAttribute("redirectUrl", VIEW_PATH_DEFAULT + "/list.do");
				return web.returnError();
			}
		}

		//상세조회 데이터
		CnslerVO cnslerVO = (CnslerVO) boardMap.get("cnslerVO");

		//조회결과 MODEL ADD
		model.addAttribute("cnslerVO", cnslerVO);


		return web.returnView(VIEW_PATH, "/view");
	}

	
	/**
	 * 게시글화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
//		LoginVO loginInfo = SessionUtil.getLoginUser();
//
//		//로그인 사용자가 아닌경우
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.cnsllogin"));
//			model.addAttribute("redirectUrl", "/login.do");
//			return web.returnError();
//		}
//		//로그인정보 및 아이피
//		String userId = loginInfo.getLoginId();
//		String userIp = ClientUtil.getUserIp();
//
//		//유저정보
//		UserVO userVO = userService.selectUser(userId);
//		model.addAttribute("userVO", userVO);
		
		//학과코드
//		List<DeptVO> deptList = deptService.selectOnlyColgList();
//		model.addAttribute("deptList", deptList);
		
		//CODE SET
		Map<String, Object> map = new HashMap<String, Object>();
		
		//상담분야(CNSL_TYPE)
		map.put("cnslType", commCdService.selectComAuthList("CNSL_TYPE"));
		
		//교육방법(PROG_MTH)
		map.put("progMth", commCdService.selectComAuthList("PROG_MTH"));

		//신분구분
		model.addAttribute("userTpList", commCdService.selectComAuthList("USER_TYPE"));
		//신분구분상세(졸업생 / 재학생)
		model.addAttribute("userTpdetList", commCdService.selectComAuthList("USER_TYPE_DET"));
		//도외/도내 거주지 구분
		model.addAttribute("userLocList", commCdService.selectComAuthList("USER_LOC"));
		
		//VO선언
		CnslerVO cnslerVO = new CnslerVO();
		model.addAttribute("cnslerVO", cnslerVO);
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/register");
		
	}
	
	/**
	 * 게시글화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,CnslerVO cnslerVO ,BindingResult bbsBindingResult) throws GeneralSecurityException, UnsupportedEncodingException {
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.insertCnsl(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", "/cnsler/view.do");			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();			
		} else { //실패시			
			model.addAttribute("redirectUrl", "/cnsler/register.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	
}
