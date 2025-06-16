package kr.co.nanwe.prog.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
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
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgSearchVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgVO;

/**
 * @Class Name 		: SysProgController
 * @Description 	: 프로그램 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */

@Program(code="COM_PROG", name="프로그램관리")
@Controller
public class SysProgController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SysProgController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/prog";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/prog";
	
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
	public SysProgController() {		
		this.REDIRECT_PATH = "/sys/prog";
	}
	
	/** Root Forward */
	@RequestMapping(value = "/sys/prog")
	public String root(){
		if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
			return web.returnJsp("error/error404");
		}
		return web.forward(REDIRECT_PATH + "/list.do");
	}
	
	/**
	 * 프로그램화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/prog.do", "/sys/prog/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = progService.selectProgList(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 프로그램화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//id 없을 때
		if(id == null || "".equals(id)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noData"));
			model.addAttribute("redirectUrl", "/sys/prog/list.do");
			return web.returnError();
		}
		
		//DATA SEARCH
		ProgVO progVO = progService.selectProg(Integer.valueOf(id));
		
		//조회결과 MODEL ADD
		model.addAttribute("progVO", progVO);
				
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 프로그램화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){

		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
				
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//VO 생성
		ProgVO progVO = new ProgVO();
		
		//조회결과 MODEL ADD
		model.addAttribute("progVO", progVO);
	
		//CODE SET
		Map<String, Object> map = new HashMap<String, Object>();
		//커리어단계(PROG_CAREER)
		map.put("progCareer", commCdService.selectComAuthList("PROG_CAREER"));
		//특성분류(PROG_TYPE)
		map.put("progType", commCdService.selectComAuthList("PROG_TYPE"));
		//교육방법(PROG_MTH)
		map.put("progMth", commCdService.selectComAuthList("PROG_MTH"));
		//프로그램접근방식(PROG_CONTACT)
		map.put("progContact", commCdService.selectComAuthList("PROG_CONTACT"));
		
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 프로그램화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
								,ProgVO progVO ,BindingResult progBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//CODE SET
		Map<String, Object> map = new HashMap<String, Object>();
		//커리어단계(PROG_CAREER)
		map.put("progCareer", commCdService.selectComAuthList("PROG_CAREER"));
		//특성분류(PROG_TYPE)
		map.put("progType", commCdService.selectComAuthList("PROG_TYPE"));
		//교육방법(PROG_MTH)
		map.put("progMth", commCdService.selectComAuthList("PROG_MTH"));
		//프로그램접근방식(PROG_CONTACT)
		map.put("progContact", commCdService.selectComAuthList("PROG_CONTACT"));
		
		model.addAllAttributes(map);
		
		//유효성 검증
		beanValidator.validate(progVO, progBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (progBindingResult.hasErrors()) {
			model.addAttribute("progVO", progVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//유효성 검사 로직 작성
		if(progVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//파일첨부시 이미지인지 체크 - 프로그램 썸네일
		if(FileUtil.isFile(progVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(progVO.getUploadFile()))) {
			addtionalValid = false;
			progBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		
		//첨부파일 체크 - 프로그램 첨부파일
		if (progVO.getUploadFiles() != null) {
			
			int uploadCount = progVO.getUploadFiles().size();
			
			//첨부파일 개수 확인
			if(uploadCount > 10) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCode", code);
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("progVO", progVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progService.insertProg(progVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progVO.getProgId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	/**
	 * 프로그램화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//CODE SET
		Map<String, Object> map = new HashMap<String, Object>();
		//커리어단계(PROG_CAREER)
		map.put("progCareer", commCdService.selectComAuthList("PROG_CAREER"));
		//특성분류(PROG_TYPE)
		map.put("progType", commCdService.selectComAuthList("PROG_TYPE"));
		//교육방법(PROG_MTH)
		map.put("progMth", commCdService.selectComAuthList("PROG_MTH"));
		//프로그램접근방식(PROG_CONTACT)
		map.put("progContact", commCdService.selectComAuthList("PROG_CONTACT"));
		//진행상태(PROG_STATUS)
		map.put("progStatus", commCdService.selectComAuthList("PROG_STATUS"));
		
		model.addAllAttributes(map);
		
		//DATA 조회
		ProgVO progVO = progService.selectProgUpBfView(Integer.valueOf(id));
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(progVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			return web.returnError();
		}
		
		progVO.setRm(HtmlConvertorUtil.changeBrToLineSeparator(progVO.getRm()));
		
		//조회결과 MODEL ADD
		model.addAttribute("progVO", progVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 프로그램화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
									,ProgVO progVO ,BindingResult progBindingResult
									,@RequestParam(value = "sCode", defaultValue="") String code
									,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		//로그인 사용자 MODEL ADD
		model.addAttribute("loginInfo", loginInfo);
				
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//유효성 검증
		beanValidator.validate(progVO, progBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (progBindingResult.hasErrors()) {
			model.addAttribute("progVO", progVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(progVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//DATA 조회
		ProgVO oriProgVO = progService.selectProgUpBfView(progVO.getProgId());
		if(oriProgVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(progVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(progVO.getUploadFile()))) {
			addtionalValid = false;
			progBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		
		//첨부파일 체크
		if (progVO.getUploadFiles() != null) {
			
			//업로드된 파일개수
			int uploadCount = progVO.getUploadFiles().size();
			
			//기등록된 파일개수
			if(progVO.getViewFiles() != null && progVO.getViewFiles().size() > 0) {
				for(ViewFileVO viewFileVO : progVO.getViewFiles()) {
					if (viewFileVO.getFno() == 0) uploadCount++; //0일경우 삭제가 안된상태
				}
			}			
			
			//첨부파일 개수 확인
			if(uploadCount > 10) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCode", code);
				resultParam.put("sId", progVO.getProgId());
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
			
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("progVO", progVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progService.updateProg(progVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progVO.getProgId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}		
	}
	
	/**
	 * 프로그램화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id) {
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {	
			model.addAttribute("sId", id);
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");		
			return web.returnError();
		}
		
		ProgVO progVO = progService.selectProg(Integer.valueOf(id));
		if(progVO == null) {
			model.addAttribute("sId", id);
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");		
			return web.returnError();
		}
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = progService.deleteProg(progVO.getProgId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", progVO.getProgId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();				
		}
		
	}
	
	/**
	 * 프로그램화면 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/prog/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute ProgSearchVO search
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//현재 로그인 사용자
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {	
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");		
			return web.returnError();
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl",  REDIRECT_PATH + "/list.do");		
			return web.returnError();
		}
		
		//관리자인지 확인
		//if(!bbsMgtVO.isAdminUser()){
		//	model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
		//	model.addAttribute("redirectUrl", "/board/"+path+".do");
		//	Map<String, Object> resultParam = new HashMap<String, Object>();
		//	resultParam.put("sCate", category);
		//	model.addAttribute("resultParam", resultParam);
		//	return web.returnError();
		//}
		
		//삭제처리
		int result = progService.deleteCheckedProg(checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
				
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");		
		
		return web.returnSuccess();
		
	}
		
}
