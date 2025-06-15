package kr.co.nanwe.prog.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ibm.icu.text.SimpleDateFormat;

import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.bbs.service.LaborVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgSearchVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgUserVO;
import kr.co.nanwe.prog.service.ProgVO;
import kr.co.nanwe.user.service.UserVO;
import kr.co.nanwe.user.web.UserController;

/**
 * @Class Name 		: ProgController
 * @Description 	: 프로그램 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		이가람			최초생성
 * @ 2023.10.15		신한나			수정
 */

@RequestMapping(value = "/prog")
@Program(code="PROG", name="프로그램")
@Controller
public class ProgController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/program";
	
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
	@Resource(name = "progService")
	private ProgService progService;
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Constructor */
	public ProgController() {		
		RequestMapping requestMapping = ProgController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/** Root Forward */
	@RequestMapping(value="")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/list.do");
	}
	
	/**
	 * 프로그램 신청화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("prog", "PROGRAM");
		
		Map<String, Object> map = progService.selectProgUserViewList(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
				
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 프로그램 신청화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		System.out.println("############----1111");
		System.out.println("############----1111");
		System.out.println("############----1111");
		System.out.println("############----1111");
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("prog", "PROGRAM");
		
		//id 없을 때
		if(id == null || "".equals(id)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", "/prog/list.do");
			return web.returnError();
		}
		System.out.println("############----1111222");
		//DATA SEARCH
		ProgVO progVO = progService.selectProgUserView(Integer.valueOf(id));
		System.out.println("############---->>>"+progVO.toString());
		System.out.println("############----1111333");
		//신청일 체크
		//boolean checkDate = false;
		//
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		////현재날짜
		//Date currentTime = new Date ();
		//String current = format.format(currentTime);
		//try {
		//	Date today = format.parse(current);
		//	String startDate = progVO.getReqstSdt();
		//	String endDate = progVO.getReqstEdt();			
		//	Date start = format.parse(startDate);
		//	Date end = format.parse(endDate);
		//	int startResult = today.compareTo(start);
		//	int endResult = end.compareTo(today);			
		//	if(startResult >= 0 && endResult >= 0) {
		//		checkDate = true;
		//	}
		//} catch (Exception e) {
		//	LOGGER.debug(e.getMessage());
		//	checkDate = false;
		//}
		//
		//if(!checkDate) {
		//	model.addAttribute("PROGRAM", "PROG_END");
		//}
		
		//조회결과 MODEL ADD
		model.addAttribute("progVO", progVO);
		System.out.println("############----111122244");
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 프로그램 신청화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/apply.do")
	@ProgramInfo(code="APPLY_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") int id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){

		//프로그램 조회
		ProgVO program = progService.selectProgUserView(id);
		if(program == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", "/prog/list.do");
			return web.returnError();
		} else {
			model.addAttribute("program", program);
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("prog", "PROGRAM");
		
		ProgUserVO progUserVO = new ProgUserVO();
		progUserVO.setProgId(id);
		
		//조회결과 MODEL ADD
		model.addAttribute("progUserVO", progUserVO);
		//
		model.addAttribute("selectAuthDvcd", CodeConfig.COM_USER_CODE);
		//신분구분
		model.addAttribute("uerTpList", commCdService.selectComAuthList("USER_TYPE"));
		//신분구분상세(졸업생 / 재학생)
		model.addAttribute("uerTpdetList", commCdService.selectComAuthList("USER_TYPE_DET"));
		//도외/도내 거주지 구분
		model.addAttribute("userLocList", commCdService.selectComAuthList("USER_LOC"));
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 프로그램 신청화면 신청처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/applyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String applyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,ProgUserVO progUserVO, BindingResult progUserBindingResult
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		System.out.println("progUserVO ==> " + progUserVO);
//		//검색조건 MODEL ADD
//		model.addAttribute("search", search);
//
		//프로그램 조회
		ProgVO program = progService.selectProgUserView( progUserVO.getProgId());
		if(program == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", "/prog/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progUserVO.getProgId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		} else {
			model.addAttribute("program", program);
		}

		//유효성 검증
		beanValidator.validate(progUserVO, progUserBindingResult);

		//유효성 검증 에러인 경우 RETURN
		if (progUserBindingResult.hasErrors()) {
			model.addAttribute("progUserVO", progUserVO);
			return web.returnView(VIEW_PATH, "/register");
		}

		//유효성 검사 로직 작성
		if(progUserVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}

		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progService.insertProgUser(progUserVO);
		}

		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnSuccess();
		} else { //실패시

			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progUserVO.getProgId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
	}
}
