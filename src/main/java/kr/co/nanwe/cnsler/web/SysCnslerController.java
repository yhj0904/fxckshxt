package kr.co.nanwe.cnsler.web;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
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
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.cnsler.service.CnslerVO;
import kr.co.nanwe.cnsler.service.impl.CnslerMapper;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.prog.service.ProgSurvMgtVO;
import kr.co.nanwe.sch.service.SchMgtVO;
import kr.co.nanwe.sch.service.SchVO;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: SysBbsController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="CNSL_MST", name="상담기준정보")
@Controller
public class SysCnslerController {
	
	/** View Path */
	private static final String VIEW_PATH = "sys/cnsler";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/cnsler";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** 부서코드 Service */
	@Resource(name = "deptService")
	private DeptService deptService;
	
	/** 상담원 Service */
	@Resource(name = "cnslerMapper")
	private CnslerMapper cnslerMapper;
	
	/** 상담원 Service */
	@Resource(name = "userService")
	private UserService userService;
	
	/** 상담원 Service */
	@Resource(name = "cnslerService")
	private CnslerService cnslerService;
	
	/** 공통코드 Service */
	@Resource(name = "commCdService")
	private CommCdService commCdService;
	
	/** Constructor */
	public SysCnslerController() {		
		RequestMapping requestMapping = SysCnslerController.class.getAnnotation(RequestMapping.class);
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
	@RequestMapping(value = {"/sys/cnsler.do", "/sys/cnsler/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = cnslerService.selectCnslerList(search);
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 게시글화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		
		//단과 목록 조회
		List<CnslerVO> colgList = cnslerService.selectColgByCnsler(id);
		model.addAttribute("colgList", colgList);
		
		//부서 목록 조회
		List<CnslerVO> deptList = cnslerService.selectDeptByCnsler(id);
		model.addAttribute("deptList", deptList);
		
		//상세조회
		CnslerVO cnslerVO = cnslerService.selectCnsler(id);
		
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
	@RequestMapping(value = "/sys/cnsler/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//VO선언
		CnslerVO cnslerVO = new CnslerVO();
		model.addAttribute("cnslerVO", cnslerVO);
		
		return web.returnView(VIEW_PATH, "/register");
	}
	
	/**
	 * 게시글화면 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,CnslerVO cnslerVO ,BindingResult bbsBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		int duplChk = 0;
		duplChk = cnslerMapper.cnslerDuplChk(cnslerVO.getCnslerId());
		
		if(duplChk > 0) {
			model.addAttribute("resultMsg", messageUtil.getMessage("errors.id.exist"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			return web.returnError();
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.insertCnsler(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	/**
	 * 게시글화면 수정폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//학과코드
		List<DeptVO> deptList = deptService.selectOnlyColgList();
		model.addAttribute("deptList", deptList);
		
		//상담분야(CNSL_TYPE)
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cnslType", commCdService.selectComAuthList("CNSL_TYPE"));
		model.addAllAttributes(map);
		
		//상세조회
		CnslerVO cnslerVO = cnslerService.selectCnsler(id);
		model.addAttribute("cnslerVO", cnslerVO);
		
		System.out.println(">>>>>>>>>>>>>> cnslerVO : " + cnslerVO);
		
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 게시글화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,CnslerVO cnslerVO ,BindingResult bbsBindingResult
									,@RequestParam(value = "sCode", defaultValue="") String code
									,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.updateCnsler(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
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
	@RequestMapping(value = "/sys/cnsler/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String id) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//삭제 전 조회
		CnslerVO cnslerVO = cnslerService.selectCnsler(id); 
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.deleteCnsler(id);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			//resultParam.put("sCode", bbsVO.getBbsCd());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			//resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", cnslerVO.getCnslerId());
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
	@RequestMapping(value = "/sys/cnsler/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//선택된 데이터가 없는 경우
		if(checkedSId == null || "".equals(checkedSId)) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			model.addAttribute("resultParam", resultParam);				
			return web.returnError();
		}
		
		//삭제처리
		int result = cnslerService.deleteCheckCnsler(checkedSId);
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		
		//리턴시 추가 파라미터 필요한경우 (생략가능)
		Map<String, Object> resultParam = new HashMap<String, Object>();
		model.addAttribute("resultParam", resultParam);
		
		return web.returnSuccess();
		
	}
	
	/**
	 * 부서 선택 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/selectDept.do")
	@ProgramInfo(code="VIEW", name="등록 화면")
	public String view(Model model, HttpServletRequest request, SearchVO search){
		
		//상세조회
		List<DeptVO> deptVO = deptService.selectColgListByUse(search);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(deptVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("survMgtVO.error"));
			model.addAttribute("redirectUrl", "/");
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("deptVO", deptVO);
		
		return web.returnView(VIEW_PATH, "/selectDept", "POP");
	}
	
	/**
	 * 단과/부서 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/colgRegist.do")
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String colgRegisterAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
			,CnslerVO cnslerVO
			, @RequestParam(value = "colg_values", required=false) String colg_values
			, @RequestParam(value = "dept_values", required=false) String dept_values
			, @RequestParam(value = "cnslerId", required=false) String cnslerId) {
		
		
		int result = 0;
		
		result = cnslerService.insertCnslerColg(cnslerVO, colg_values, dept_values, cnslerId);
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
	}
	

	
	/**
	 * 상담원 일정 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerSch.do", method = RequestMethod.POST)
	@ProgramInfo(code="CNSLERSCH", name="상담원일정")
	public String cnslerSch(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
							,@RequestParam(value = "sId", defaultValue="") String id
							,@RequestParam(value = "sDate", defaultValue="") String date
							,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> sId : " + id);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = null;
		map = cnslerService.selectCnslerSchList(search, id, date, option);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		model.addAttribute("sDate", map.get("sDate"));
		model.addAttribute("cnslerId", id);
		
		return web.returnView(VIEW_PATH, "/cnslersch");
		
	}
	
	/**
	 * 상담원 일정 등록 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerRegisterSch.do")
	@ProgramInfo(code="CNSLERSCH", name="상담원일정")
	public String cnslerRegisterSch(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
							,@RequestParam(value = "sId", defaultValue="") String id
							,@RequestParam(value = "sDate", defaultValue="") String date
							,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		System.out.println(">>>>>>>>>>>>>>>>> cnslerId = " + id);
		
		//상담원정보
		UserVO userVO = userService.selectUser(id);
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/cnslersch_register", "POP");
	}
	
	/**
	 * 상담원 일정 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerSchRegisterAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String cnslerSchRegisterAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,CnslerVO cnslerVO ,BindingResult bbsBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//등록시 날짜중복 체크
		int dupl_chk = 0;
		dupl_chk = cnslerService.selectCnslerSchDupl(cnslerVO);
		
		if(dupl_chk > 0) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultMsg", messageUtil.getMessage("message.alert.existdate"));
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.insertCnslerSch(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	/**
	 * 상담원 일정 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerSch_view.do", method = RequestMethod.POST)
	@ProgramInfo(code="CNSLERSCH", name="상담원일정")
	public String cnslerSch_view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
							,@ModelAttribute CnslerVO cnslerVO
							,@RequestParam(value = "sId", defaultValue="") String id
							,@RequestParam(value = "sDate", defaultValue="") String date
							,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sDate", date);
		
		//상담원아이디 입력
		cnslerVO.setCnslerId(id);
		
		//상세조회
		cnslerVO = cnslerService.selectCnslerSchDetail(cnslerVO);
		
		//상담원정보
		UserVO userVO = userService.selectUser(id);
		
		//조회결과 MODEL ADD
		model.addAttribute("cnslerVO", cnslerVO);
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/cnslersch_view");
	}
	
	/**
	 * 상담원 일정 수정 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerSch_modify.do", method = RequestMethod.POST)
	@ProgramInfo(code="CNSLERSCH", name="상담원일정수정")
	public String cnslerSch_modify(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
							,@ModelAttribute CnslerVO cnslerVO
							,@RequestParam(value = "sId", defaultValue="") String id
							,@RequestParam(value = "sDate", defaultValue="") String date
							,@RequestParam(value = "sOption", defaultValue="") String option) {
		
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sDate", date);
		
		//상담원아이디 입력
		cnslerVO.setCnslerId(id);
		
		//상세조회
		cnslerVO = cnslerService.selectCnslerSchDetail(cnslerVO);
		
		//상담원정보
		UserVO userVO = userService.selectUser(id);
		
		//조회결과 MODEL ADD
		model.addAttribute("cnslerVO", cnslerVO);
		model.addAttribute("userVO", userVO);
		
		return web.returnView(VIEW_PATH, "/cnslersch_modify");
	}
	
	/**
	 * 상담원 일정 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerSchModifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String cnslerSchModifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,CnslerVO cnslerVO ,BindingResult bbsBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.updateCnslerSch(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	/**
	 * 상담원 일정 삭제처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/cnsler/cnslerSchRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String cnslerSchRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,CnslerVO cnslerVO ,BindingResult bbsBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//삭제 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = cnslerService.deleteCnslerSch(cnslerVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/cnslerSch.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", cnslerVO.getCnslerId());
			model.addAttribute("resultParam", resultParam);
			return web.returnError();			
		}
		
	}
	
	
	
}
