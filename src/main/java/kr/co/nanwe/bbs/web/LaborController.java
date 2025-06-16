package kr.co.nanwe.bbs.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.util.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.LaborVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: LaborController
 * @Description 	: wk인력풀 Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.11.07		신한나			최초생성
 * @ 2024.06.10		정기훈			인력풀 등록 전체엑셀다운 추가
 */

@Program(code="LABOR", name="WK인력풀")
@Controller
public class LaborController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LaborController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/work";

	/** Pw View Path */
	private static final String VIEW_PATH_SUB = "main/board";

	/** DEFAULT View Path */
	private static final String VIEW_PATH_DEFAULT = "labr";
	
	/** Board Code */
	private static final String BBS_CD = "BOARD09";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/labr";
	
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
	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	/** Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** Service */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;
	
	/** Service */
	@Resource(name = "deptService")
	private DeptService deptService;

	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;
	
	/** Constructor */
	public LaborController() {		
		RequestMapping requestMapping = LaborController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 게시글화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/labr.do", "/labr/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "LIST");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//목록조회
		Map<String, Object> map = bbsService.selectLaborList(bbsMgtVO, search, category);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 게시글화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		//현재 로그인 유저
//		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//게시판 코드
		String code = BBS_CD;
				
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//상세조회
//		UserVO userVO = userService.selectUser(loginInfo.getLoginId());
		
		// 상위 부서
		List<DeptVO> colgList = deptService.selectOnlyColgList();
		
		// 모든 하위 부서를 저장할 리스트
        List<DeptVO> allSubDepts = new ArrayList<>();

        // 각 상위 부서에 대해 하위 부서 목록 조회 및 추가
        for (DeptVO colg : colgList) {
            List<DeptVO> subDepts = deptService.selectDepByHiCd(colg.getDeptCd());
            allSubDepts.addAll(subDepts); // 하위 부서 목록을 allSubDepts 리스트에 추가
        }
        model.addAttribute("deptList", allSubDepts);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
//		if(userVO != null) {
//			model.addAttribute("userVO", userVO);
//		}
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		LaborVO laborVO = new LaborVO();
		model.addAttribute("laborVO", laborVO);
		
		//희망근무지 코드 조회
		model.addAttribute("localList", commCdService.selectCommCd("LABR_LOCAL_CD"));
		
		//대학일자리플러스센터 참여 희망 프로그램  코드 조회
		model.addAttribute("wProgList", commCdService.selectCommCd("LABR_W_PROG"));
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 게시글화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,LaborVO laborVO, BindingResult lbrBindingResult
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) throws GeneralSecurityException, UnsupportedEncodingException {
		
		//현재 로그인 유저
//		LoginVO loginInfo = SessionUtil.getLoginUser();
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
//			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
//			return web.returnError();
//		}
		
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//상세조회
//		UserVO userVO = userService.selectUser(loginInfo.getLoginId());
		
		//조회결과가 없는 경우 RESULT VIEW 이동
//		if(userVO != null) {
//			model.addAttribute("userVO", userVO);
//		}
				
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);

		//희망근무지 코드 조회
		CommCdVO localList = commCdService.selectCommCd("LABR_LOCAL_CD");
		model.addAttribute("localList", localList);
		
		//대학일자리플러스센터 참여 희망 프로그램  코드 조회
		CommCdVO wProgList = commCdService.selectCommCd("LABR_W_PROG");
		model.addAttribute("wProgList", wProgList);
		
		if(laborVO != null) {
			laborVO.setGrYear(StringEscapeUtils.unescapeHtml4(laborVO.getGrYear()));
			laborVO.setLicense(StringEscapeUtils.unescapeHtml4(laborVO.getLicense()));
			laborVO.setLangScore(StringEscapeUtils.unescapeHtml4(laborVO.getLangScore()));
			laborVO.setExtAct(StringEscapeUtils.unescapeHtml4(laborVO.getExtAct()));
			laborVO.setWhJob(StringEscapeUtils.unescapeHtml4(laborVO.getWhJob()));
			laborVO.setWhCompany(StringEscapeUtils.unescapeHtml4(laborVO.getWhCompany()));
			laborVO.setJobPrepPlan(StringEscapeUtils.unescapeHtml4(laborVO.getJobPrepPlan()));
			laborVO.setJobPrepTime(StringEscapeUtils.unescapeHtml4(laborVO.getJobPrepTime()));
			laborVO.setWhQuestions(StringEscapeUtils.unescapeHtml4(laborVO.getWhQuestions()));
			laborVO.setWhProgram02(StringEscapeUtils.unescapeHtml4(laborVO.getWhProgram02()));
		}
		
		//유효성 검증
		beanValidator.validate(laborVO, lbrBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (lbrBindingResult.hasErrors()) {
			model.addAttribute("laborVO", laborVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(laborVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("laborVO", laborVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.insertLabor(bbsMgtVO, laborVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", laborVO.getLabrId());
			resultParam.put("sCate", category);
			//if(!StringUtil.isNull(labrId.getPw())) {
			//	resultParam.put("bbsPw", bbsVO.getPw());
			//}
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();			
		} else { //실패시			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	/**
	 * 게시글화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category
						,@RequestParam(value = "bbsPw", required=false) String pw){
		//현재 로그인 유저
//		LoginVO loginInfo = SessionUtil.getLoginUser();
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
//			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
//			return web.returnError();
//		}
				
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "VIEW");

		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//희망근무지 코드 조회
		CommCdVO localList = commCdService.selectCommCd("LABR_LOCAL_CD");
		model.addAttribute("localList", localList);
		
		//대학일자리플러스센터 참여 희망 프로그램  코드 조회
		CommCdVO wProgList = commCdService.selectCommCd("LABR_W_PROG");
		model.addAttribute("wProgList", wProgList);
		
		//상세조회
		Map<String, Object> boardMap;
		boardMap = bbsService.selectLabrByAuth(bbsMgtVO, id, "VIEW", pw);

		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
//			String errMsgCd = (String) boardMap.get("errMsgCd");
//			model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
//			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
//			Map<String, Object> resultParam = new HashMap<String, Object>();
//			resultParam.put("sCate", category);
//			model.addAttribute("resultParam", resultParam);
//			return web.returnError();

			//test
			String errMsgCd = (String) boardMap.get("errMsgCd");
			//비회원 작성글인 경우
			if(CodeConfig.NO_LOGIN.equals(errMsgCd)) {
				model.addAttribute("sId", id);
				model.addAttribute("formAction", VIEW_PATH_DEFAULT + "/view.do");
				return web.returnView(VIEW_PATH_SUB, "/password");
			} else {
				model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
				model.addAttribute("redirectUrl", VIEW_PATH_DEFAULT + "/list.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
		}
		
		//if(!StringUtil.isNull(bbsPw)) {
		//	model.addAttribute("bbsPw", bbsPw);
		//}
		
		//상세조회 데이터
		LaborVO laborVO = (LaborVO) boardMap.get("laborVO");
		
		//익명글인경우
		//if("Y".equals(bbsMgtVO.getNonameYn())) {
		//	bbsVO.setWriter("NONAME");
		//}
		
		//조회결과 MODEL ADD
		model.addAttribute("laborVO", laborVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 게시글화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								, @RequestParam(value = "sId", defaultValue="") String id
								, @RequestParam(value = "sCate", defaultValue="ALL") String category
								, @RequestParam(value = "bbsPw", required=false) String pw){
		
		//현재 로그인 유저
		LoginVO loginInfo = SessionUtil.getLoginUser();
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
//			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
//			return web.returnError();
//		}
				
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//상세조회
		Map<String, Object> boardMap;
		boardMap = bbsService.selectLabrByAuth(bbsMgtVO, id, "MODI", pw);

		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			model.addAttribute("resultMsg", messageUtil.getMessage(errMsgCd));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//if(!StringUtil.isNull(bbsPw)) {
		//	model.addAttribute("bbsPw", bbsPw);
		//}
		
		LaborVO laborVO = (LaborVO) boardMap.get("laborVO");
		
		// 선택된 학과 코드를 모델에 추가
        if(laborVO != null && laborVO.getDeptCd() != null) {
            model.addAttribute("selectedDeptCd", laborVO.getDeptCd());
        }
		
		// 상위 부서
		List<DeptVO> colgList = deptService.selectOnlyColgList();
		
		// 모든 하위 부서를 저장할 리스트
        List<DeptVO> allSubDepts = new ArrayList<>();

        // 각 상위 부서에 대해 하위 부서 목록 조회 및 추가
        for (DeptVO colg : colgList) {
            List<DeptVO> subDepts = deptService.selectDepByHiCd(colg.getDeptCd());
            allSubDepts.addAll(subDepts); // 하위 부서 목록을 allSubDepts 리스트에 추가
        }
        
        model.addAttribute("deptList", allSubDepts);
        
		//에디터 미사용시 줄바꿈 역치환
		if("N".equals(bbsMgtVO.getEditorYn())) {
			laborVO.setLicense(HtmlConvertorUtil.changeBrToLineSeparator(laborVO.getLicense()));
			
			laborVO.setLangScore(HtmlConvertorUtil.changeBrToLineSeparator(laborVO.getLangScore()));
			laborVO.setExtAct(HtmlConvertorUtil.changeBrToLineSeparator(laborVO.getExtAct()));
			laborVO.setWhCompany(HtmlConvertorUtil.changeBrToLineSeparator(laborVO.getWhCompany()));
			laborVO.setWhQuestions(HtmlConvertorUtil.changeBrToLineSeparator(laborVO.getWhQuestions()));
			laborVO.setWhProgram02(HtmlConvertorUtil.changeBrToLineSeparator(laborVO.getWhProgram02()));
		}
		
		if(laborVO != null) {
			laborVO.setGrYear(StringEscapeUtils.unescapeHtml4(laborVO.getGrYear()));
			laborVO.setLicense(StringEscapeUtils.unescapeHtml4(laborVO.getLicense()));
			laborVO.setLangScore(StringEscapeUtils.unescapeHtml4(laborVO.getLangScore()));
			laborVO.setExtAct(StringEscapeUtils.unescapeHtml4(laborVO.getExtAct()));
			laborVO.setWhJob(StringEscapeUtils.unescapeHtml4(laborVO.getWhJob()));
			laborVO.setWhCompany(StringEscapeUtils.unescapeHtml4(laborVO.getWhCompany()));
			laborVO.setJobPrepPlan(StringEscapeUtils.unescapeHtml4(laborVO.getJobPrepPlan()));
			laborVO.setJobPrepTime(StringEscapeUtils.unescapeHtml4(laborVO.getJobPrepTime()));
			laborVO.setWhQuestions(StringEscapeUtils.unescapeHtml4(laborVO.getWhQuestions()));
			laborVO.setWhProgram02(StringEscapeUtils.unescapeHtml4(laborVO.getWhProgram02()));
		}
		
		//희망근무지 코드 조회
		CommCdVO localList = commCdService.selectCommCd("LABR_LOCAL_CD");
		model.addAttribute("localList", localList);
		
		//대학일자리플러스센터 참여 희망 프로그램  코드 조회
		CommCdVO wProgList = commCdService.selectCommCd("LABR_W_PROG");
		model.addAttribute("wProgList", wProgList);
		
		//조회결과 MODEL ADD
		model.addAttribute("laborVO", laborVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 게시글화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, LaborVO laborVO ,BindingResult labrBindingResult
									, @RequestParam(value = "sCate", defaultValue="ALL") String category
									, @RequestParam(value = "bbsPw", required=false) String pw) {
		//현재 로그인 유저
		LoginVO loginInfo = SessionUtil.getLoginUser();
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
//			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
//			return web.returnError();
//		}
				
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//상세조회
		UserVO userVO = null;
		if(loginInfo != null) {
			userVO = userService.selectUser(loginInfo.getLoginId());
		}

		//조회결과가 없는 경우 RESULT VIEW 이동
		if(userVO != null) {
			model.addAttribute("userVO", userVO);
		}
				
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);

		//희망근무지 코드 조회
		CommCdVO localList = commCdService.selectCommCd("LABR_LOCAL_CD");
		model.addAttribute("localList", localList);
		
		//대학일자리플러스센터 참여 희망 프로그램  코드 조회
		CommCdVO wProgList = commCdService.selectCommCd("LABR_W_PROG");
		model.addAttribute("wProgList", wProgList);
		
		//유효성 검증
		beanValidator.validate(laborVO, labrBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (labrBindingResult.hasErrors()) {
			model.addAttribute("laborVO", laborVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(laborVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		//상세조회
		Map<String, Object> boardMap;
		boardMap = bbsService.selectLabrByAuth(bbsMgtVO, laborVO.getLabrId(), "MODI", pw);

		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");		
			model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//if(!StringUtil.isNull(bbsPw)) {
		//	model.addAttribute("bbsPw", bbsPw);
		//}
		
		LaborVO oriLaborVO = (LaborVO) boardMap.get("laborVO");
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("laborVO", laborVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		if(laborVO != null) {
			laborVO.setGrYear(StringEscapeUtils.unescapeHtml4(laborVO.getGrYear()));
			laborVO.setLicense(StringEscapeUtils.unescapeHtml4(laborVO.getLicense()));
			laborVO.setLangScore(StringEscapeUtils.unescapeHtml4(laborVO.getLangScore()));
			laborVO.setExtAct(StringEscapeUtils.unescapeHtml4(laborVO.getExtAct()));
			laborVO.setWhJob(StringEscapeUtils.unescapeHtml4(laborVO.getWhJob()));
			laborVO.setWhCompany(StringEscapeUtils.unescapeHtml4(laborVO.getWhCompany()));
			laborVO.setJobPrepPlan(StringEscapeUtils.unescapeHtml4(laborVO.getJobPrepPlan()));
			laborVO.setJobPrepTime(StringEscapeUtils.unescapeHtml4(laborVO.getJobPrepTime()));
			laborVO.setWhQuestions(StringEscapeUtils.unescapeHtml4(laborVO.getWhQuestions()));
			laborVO.setWhProgram02(StringEscapeUtils.unescapeHtml4(laborVO.getWhProgram02()));
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.updateLabr(bbsMgtVO, laborVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", "/labr/view.do");	
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", laborVO.getLabrId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", "/labr/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", laborVO.getLabrId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}		
	}
	
	/**
	 * 게시글화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								, @RequestParam(value = "sId", defaultValue="") String id
								, @RequestParam(value = "sCate", defaultValue="ALL") String category
								, @RequestParam(value = "bbsPw", required=false) String pw) {
		
		//현재 로그인 유저
		LoginVO loginInfo = SessionUtil.getLoginUser();
//		if(loginInfo == null) {
//			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
//			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
//			return web.returnError();
//		}
				
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//상세조회
		Map<String, Object> boardMap;
		boardMap = bbsService.selectLabrByAuth(bbsMgtVO, id, "REMOVE", pw);


		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		LaborVO laborVO = (LaborVO) boardMap.get("laborVO");
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			LOGGER.debug("DELETE >>>>>>>>>>>>>>> ");
			result = bbsService.deleteLabr(bbsMgtVO, laborVO.getLabrId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", "/labr/view.do");			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId",laborVO.getLabrId());
			resultParam.put("sCate", category);
			//if(!StringUtil.isNull(bbsPw)) {
			//	resultParam.put("bbsPw", bbsPw);
			//}
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 게시글화면 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/labr/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "sCate", defaultValue="ALL") String category
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//현재 로그인 유저
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.login"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
				
		//게시판 코드
		String code = BBS_CD;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//관리자인지 확인
		if(!bbsMgtVO.isAdminUser()){
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//삭제처리
		int result = bbsService.deleteCheckedLabr(bbsMgtVO, checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		Map<String, Object> resultParam = new HashMap<String, Object>();
		resultParam.put("sCate", category);
		model.addAttribute("resultParam", resultParam);
		
		return web.returnSuccess();
		
	}
	
	@RequestMapping(value = "/labr/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search, @RequestParam(value = "labrIds", required = false) String labrIds) throws IOException {
		
		//엑셀 파일명
		String excelName = "인력풀 등록";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"소속", "성명", "성별", "학번", "학과", "학년", "핸드폰 번호", "학점", "보유자격증", "공인어학점수", "대외활동명", "면접경험", "제공 받고싶은 항목", "희망직종", "희망근무지", "희망임금(만원)", "희망 근로조건", "현재까지 입사지원한 기업", "밴드가입여부", "개인정보 활용동의", "취업준비 계획"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes =  {"BELONG", "USER_NM", "SEX_NM", "STD_NO", "DEPT_NM", "GRADE_NM", "MBPH_NO", "COLG_SCORE", "LICENSE", "LANG_SCORE", "EXT_ACT", "ITV_EXP", "WH_PROGRAM01_NM", "WH_JOB", "WH_LOCAL_NM", "DSRD_WAGE", "DSRD_WORK_CDT_NM", "WH_COMPANY", "BAND_MEMBER", "AGREE_YN", "JOB_PREP_PLAN"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook();
        
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, false);
		
		if (labrIds != null && !labrIds.isEmpty()) {
	        search.setLabrIds(Arrays.asList(labrIds.split(",")));
	    }
		
		//데이터 조회
		bbsService.selectLaborListForExcel(handler, search);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}

	@RequestMapping(value = "/labr/excelDownAll.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDownAll(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search) throws IOException {

		//엑셀 파일명
		String excelName = "인력풀 등록(전체)";

		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"소속", "성명", "성별", "학번", "학과", "학년", "핸드폰 번호", "학점", "보유자격증", "공인어학점수", "대외활동명", "면접경험", "제공 받고싶은 항목", "희망직종", "희망근무지", "희망임금(만원)", "희망 근로조건", "현재까지 입사지원한 기업", "밴드가입여부", "개인정보 활용동의", "취업준비 계획"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
		String[] columnCodes =  {"BELONG", "USER_NM", "SEX_NM", "STD_NO", "DEPT_NM", "GRADE_NM", "MBPH_NO", "COLG_SCORE", "LICENSE", "LANG_SCORE", "EXT_ACT", "ITV_EXP", "WH_PROGRAM01_NM", "WH_JOB", "WH_LOCAL_NM", "DSRD_WAGE", "DSRD_WORK_CDT_NM", "WH_COMPANY", "BAND_MEMBER", "AGREE_YN", "JOB_PREP_PLAN"};


		//WORK BOOK 생성 (파라미터 : 배치 행수)
		SXSSFWorkbook wb = new SXSSFWorkbook();

		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, false);

		//데이터 조회
		bbsService.selectLaborListForExcelAll(handler, search);

		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
}
