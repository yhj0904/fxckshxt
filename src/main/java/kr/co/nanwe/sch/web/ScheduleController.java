package kr.co.nanwe.sch.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.sch.service.SchMgtService;
import kr.co.nanwe.sch.service.SchMgtVO;
import kr.co.nanwe.sch.service.SchService;
import kr.co.nanwe.sch.service.SchVO;

/**
 * @Class Name 		: ScheduleController
 * @Description 	: 일정 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/schedule")
@Program(code="SCHEDULE", name="일정")
@Controller
public class ScheduleController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/schedule";
	
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
	@Resource(name = "schService")
	private SchService schService;
	
	/** Service */
	@Resource(name = "schMgtService")
	private SchMgtService schMgtService;
	
	/** Root Forward */
	@RequestMapping(value = "/{path}.do")
	public String root(@PathVariable("path") String path, HttpServletRequest request) {
		return web.forward("/schedule/"+path+"/list.do");
	}
	
	/**
	 * 일정화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@PathVariable("path") String path
						,@RequestParam(value = "sCate", defaultValue="ALL") String category
						,@RequestParam(value = "sDate", defaultValue="") String date
						,@RequestParam(value = "sOption", defaultValue="") String option){
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "LIST");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//일정 스킨
		if(!StringUtil.isNull(schMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_SCHEDULE_SKIN_CODE", schMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);
		
		//목록조회
		String viewName = "list";
		Map<String, Object> map = null;
		String typeCd = schMgtVO.getTypeCd();
		if(StringUtil.isNull(typeCd)) {
			map = schService.selectSchList(schMgtVO, search, category);
		} else {
			viewName = typeCd.toLowerCase();
			map = schService.selectSchCalendar(schMgtVO, search, category, date, option);			
			model.addAttribute("sDate", map.get("sDate"));
		}
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/"+viewName);
	}
	
	/**
	 * 일정화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@PathVariable("path") String path
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category
						,@RequestParam(value = "sDate", defaultValue="") String date
						,@RequestParam(value = "sOption", defaultValue="") String option){
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "VIEW");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//일정 스킨
		if(!StringUtil.isNull(schMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_SCHEDULE_SKIN_CODE", schMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);
		
		//상세조회
		Map<String, Object> scheduleMap = schService.selectSchByAuth(schMgtVO, id, "VIEW");
		boolean authCheck = (boolean) scheduleMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) scheduleMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		SchVO schVO = (SchVO) scheduleMap.get("schVO");
		
		//조회수 증가
		schService.updateSchViewCnt(schVO.getSchCd(), schVO.getSchId());
		
		//조회결과 MODEL ADD
		model.addAttribute("schVO", schVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 일정화면 등록폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@PathVariable("path") String path
								,@RequestParam(value = "sCate", defaultValue="ALL") String category
								,@RequestParam(value = "sDate", defaultValue="") String date
								,@RequestParam(value = "sOption", defaultValue="") String option){
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "REGI");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//일정 스킨
		if(!StringUtil.isNull(schMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_SCHEDULE_SKIN_CODE", schMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);
		
		//VO 생성
		SchVO schVO = new SchVO();
		
		//로그인 정보
		if(SessionUtil.getLoginUser() != null) {
			schVO.setWriter(SessionUtil.getLoginUser().getLoginNm());
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("schVO", schVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 일정화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,SchVO schVO ,BindingResult schBindingResult
								,@PathVariable("path") String path
								,@RequestParam(value = "sCate", defaultValue="ALL") String category
								,@RequestParam(value = "sDate", defaultValue="") String date
								,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "REGI");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//일정 스킨
		if(!StringUtil.isNull(schMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_SCHEDULE_SKIN_CODE", schMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);

		//유효성 검증
		beanValidator.validate(schVO, schBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (schBindingResult.hasErrors()) {
			model.addAttribute("schVO", schVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(schVO == null) {
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//첨부파일 체크
		if ("Y".equals(schMgtVO.getFileYn()) && schVO.getUploadFiles() != null) {
			
			int uploadCount = schVO.getUploadFiles().size();
			
			//첨부파일 개수 확인
			if(uploadCount > schMgtVO.getFileCnt()) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", "/schedule/"+path+"/register.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCate", category);
				resultParam.put("sDate", date);
				model.addAttribute("resultParam", resultParam);	
				return web.returnError();
			}
			
			//첨부파일 확장자 체크
			if(!StringUtil.isNull(schMgtVO.getFileExt())) {
				boolean denyCheck = false;
				//등록된 확장자
				List<String> extList = new ArrayList<String>(Arrays.asList(schMgtVO.getFileExt().split(",")));
				
				if(extList != null && extList.size() > 0) {
					//현재 첨부파일의 확장자를 비교
					for (MultipartFile uploadFile : schVO.getUploadFiles()) {
						if(!extList.contains(FileUtil.getFileExt(uploadFile))) {
							denyCheck = true;
							break;
						}
					}
				}				
				//false일 경우 return;
				if(denyCheck) {
					model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadext"));
					model.addAttribute("redirectUrl", "/schedule/"+path+"/register.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
					resultParam.put("sCate", category);
					resultParam.put("sDate", date);
					model.addAttribute("resultParam", resultParam);	
					return web.returnError();
				}
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("schVO", schVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = schService.insertSch(schMgtVO, schVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", "/schedule/"+path+"/view.do");			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schVO.getSchId());
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnSuccess();
			
		} else { //실패시			
			model.addAttribute("redirectUrl", "/schedule/"+path+"/register.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();			
		}
		
	}
	
	/**
	 * 일정화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@PathVariable("path") String path
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category
								,@RequestParam(value = "sDate", defaultValue="") String date
								,@RequestParam(value = "sOption", defaultValue="") String option){
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "REGI");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//일정 스킨
		if(!StringUtil.isNull(schMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_SCHEDULE_SKIN_CODE", schMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);
		
		//상세조회
		Map<String, Object> scheduleMap = schService.selectSchByAuth(schMgtVO, id, "MODI");
		boolean authCheck = (boolean) scheduleMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) scheduleMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		SchVO schVO = (SchVO) scheduleMap.get("schVO");
		
		//조회결과 MODEL ADD
		model.addAttribute("schVO", schVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 일정화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,SchVO schVO ,BindingResult schBindingResult
									,@PathVariable("path") String path
									,@RequestParam(value = "sCate", defaultValue="ALL") String category
									,@RequestParam(value = "sDate", defaultValue="") String date
									,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "REGI");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//일정 스킨
		if(!StringUtil.isNull(schMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_SCHEDULE_SKIN_CODE", schMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);

		//유효성 검증
		beanValidator.validate(schVO, schBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (schBindingResult.hasErrors()) {
			model.addAttribute("schVO", schVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(schVO == null) {
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//상세조회
		Map<String, Object> scheduleMap = schService.selectSchByAuth(schMgtVO, schVO.getSchId(), "MODI");
		boolean authCheck = (boolean) scheduleMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) scheduleMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		SchVO oriSchVO = (SchVO) scheduleMap.get("schVO");
		
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		
		//비로그인인 경우
		if(loginVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//관리자인지 또는 등록자 아이디와 로그인정보가 맞는지 확인
		if(!SessionUtil.isAdmin() && !loginVO.getLoginId().equals(oriSchVO.getInptId())){
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//첨부파일 체크
		if ("Y".equals(schMgtVO.getFileYn()) && schVO.getUploadFiles() != null) {
			
			//업로드된 파일개수
			int uploadCount = schVO.getUploadFiles().size();
			
			//기등록된 파일개수
			if(schVO.getViewFiles() != null && schVO.getViewFiles().size() > 0) {
				for(ViewFileVO viewFileVO : schVO.getViewFiles()) {
					if (viewFileVO.getFno() == 0) uploadCount++; //0일경우 삭제가 안된상태
				}
			}			
			
			//첨부파일 개수 확인
			if(uploadCount > schMgtVO.getFileCnt()) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", "/schedule/"+path+"/modify.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sId", schVO.getSchId());
				resultParam.put("sCate", category);
				resultParam.put("sDate", date);
				model.addAttribute("resultParam", resultParam);	
				return web.returnError();
			}
			
			//첨부파일 확장자 체크
			if(!StringUtil.isNull(schMgtVO.getFileExt())) {
				boolean denyCheck = false;
				//등록된 확장자
				List<String> extList = new ArrayList<String>(Arrays.asList(schMgtVO.getFileExt().split(",")));
				
				if(extList != null && extList.size() > 0) {
					//현재 첨부파일의 확장자를 비교
					for (MultipartFile uploadFile : schVO.getUploadFiles()) {
						if(!extList.contains(FileUtil.getFileExt(uploadFile))) {
							denyCheck = true;
							break;
						}
					}
				}				
				//false일 경우 return;
				if(denyCheck) {
					model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadext"));
					model.addAttribute("redirectUrl", "/schedule/"+path+"/modify.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
					resultParam.put("sId", schVO.getSchId());
					resultParam.put("sCate", category);
					resultParam.put("sDate", date);
					model.addAttribute("resultParam", resultParam);	
					return web.returnError();
				}
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("schVO", schVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = schService.updateSch(schMgtVO, schVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", "/schedule/"+path+"/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schVO.getSchId());
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", "/schedule/"+path+"/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schVO.getSchId());
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();			
		}		
	}
	
	/**
	 * 일정화면 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@PathVariable("path") String path
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category
								,@RequestParam(value = "sDate", defaultValue="") String date
								,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "REGI");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);
		
		//상세조회
		Map<String, Object> scheduleMap = schService.selectSchByAuth(schMgtVO, id, "REMOVE");
		boolean authCheck = (boolean) scheduleMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) scheduleMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		SchVO schVO = (SchVO) scheduleMap.get("schVO");
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = schService.deleteSch(schMgtVO, schVO.getSchId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", "/schedule/"+path+"/view.do");			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", schVO.getSchId());
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();				
		}
		
	}
	
	/**
	 * 일정화면 선택 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/{path}/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @PathVariable("path") String path
									, @RequestParam(value = "sCate", defaultValue="ALL") String category
									, @RequestParam(value = "checkedSId", required=false) String checkedSId
									,@RequestParam(value = "sDate", defaultValue="") String date
									,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		//일정 코드
		model.addAttribute("schedulePath", path);
		String code = StringUtil.camelToUpperUnderscore(path);
		
		//일정 정보 및 권한 체크
		SchMgtVO schMgtVO = schMgtService.selectSchMgtByAuth(code, "REGI");
		if(schMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("schMgtVO", schMgtVO);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		model.addAttribute("sDate", date);
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//삭제권한 체크
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		
		//비로그인인 경우
		if(loginVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//관리자인지 확인
		if(!SessionUtil.isAdmin()){
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/schedule/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			resultParam.put("sDate", date);
			model.addAttribute("resultParam", resultParam);	
			return web.returnError();
		}
		
		//삭제처리
		int result = schService.deleteCheckedSch(schMgtVO, checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", "/schedule/"+path+".do");
		Map<String, Object> resultParam = new HashMap<String, Object>();
		resultParam.put("sCate", category);
		resultParam.put("sDate", date);
		model.addAttribute("resultParam", resultParam);	
		
		return web.returnSuccess();
		
	}
		
}
