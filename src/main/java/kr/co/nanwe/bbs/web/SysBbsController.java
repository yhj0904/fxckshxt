package kr.co.nanwe.bbs.web;

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
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.file.service.ViewFileVO;

/**
 * @Class Name 		: SysBbsController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.26		신한나			관리자 게시판 구조 변경
 */

@RequestMapping(value = "/sys/bbs")
@Program(code="COM_BBS", name="게시글관리")
@Controller
public class SysBbsController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysBbsController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/bbs";
	
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
	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	/** Service */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;
	
	/** Constructor */
	public SysBbsController() {		
		RequestMapping requestMapping = SysBbsController.class.getAnnotation(RequestMapping.class);
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
	 * 게시글화면 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "LIST");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
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
		Map<String, Object> map = bbsService.selectBbsList(bbsMgtVO, search, category);
		
		//공지사용중이면 공지사항 목록 조회
		if("Y".equals(bbsMgtVO.getNoticeYn())) {
			List<BbsVO> noticeList = bbsService.selectBbsNoticeList(bbsMgtVO);
			model.addAttribute("noticeList", noticeList);
		}
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 게시글화면 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@RequestParam(value = "sCode", defaultValue="") String code
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "VIEW");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
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
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, id, "VIEW", null);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		BbsVO bbsVO = (BbsVO) boardMap.get("bbsVO");
		
		//익명글인경우
		if("Y".equals(bbsMgtVO.getNonameYn())) {
			bbsVO.setWriter("NONAME");
		}
		
		//이전글 & 다음글 조회
		Map<String, Object> nearBbs = bbsService.selectNearBbs(bbsMgtVO, bbsVO, category);
		model.addAttribute("nearBbs", nearBbs);
		
		//조회수 증가
		bbsService.updateBbsViewCnt(bbsVO.getBbsCd(), bbsVO.getBbsId());
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsVO", bbsVO);
		
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
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}

		//WK100 기업
		if("BOARD21".equals(code)) {
			CommCdVO companyCode = this.commCdService.selectCommCd("WK_COMPANY");
			model.addAttribute("companyCode", companyCode);
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//VO 생성
		BbsVO bbsVO = new BbsVO();
		
		//로그인 정보
		if(SessionUtil.getLoginUser() != null) {
			bbsVO.setWriter(SessionUtil.getLoginUser().getLoginNm());
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsVO", bbsVO);
		
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
								,BbsVO bbsVO ,BindingResult bbsBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}

		//WK100 기업
		if("BOARD21".equals(code)) {
			CommCdVO companyCode = this.commCdService.selectCommCd("WK_COMPANY");
			model.addAttribute("companyCode", companyCode);
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);

		//유효성 검증
		beanValidator.validate(bbsVO, bbsBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bbsBindingResult.hasErrors()) {
			model.addAttribute("bbsVO", bbsVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//첨부파일 체크
		if ("Y".equals(bbsMgtVO.getFileYn()) && bbsVO.getUploadFiles() != null) {
			
			int uploadCount = bbsVO.getUploadFiles().size();
			
			//첨부파일 개수 확인
			if(uploadCount > bbsMgtVO.getFileCnt()) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCode", code);
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
			
			//첨부파일 확장자 체크
			if(!StringUtil.isNull(bbsMgtVO.getFileExt())) {
				boolean denyCheck = false;
				//등록된 확장자
				List<String> extList = new ArrayList<String>(Arrays.asList(bbsMgtVO.getFileExt().split(",")));
				
				if(extList != null && extList.size() > 0) {
					//현재 첨부파일의 확장자를 비교
					for (MultipartFile uploadFile : bbsVO.getUploadFiles()) {
						if(!extList.contains(FileUtil.getFileExt(uploadFile))) {
							denyCheck = true;
							break;
						}
					}
				}				
				//false일 경우 return;
				if(denyCheck) {
					model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadext"));
					model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
					resultParam.put("sCode", code);
					resultParam.put("sCate", category);
					model.addAttribute("resultParam", resultParam);
					return web.returnError();
				}
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bbsVO", bbsVO);
			return web.returnView(VIEW_PATH, "/register");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.insertBbs(bbsMgtVO, bbsVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sCate", category);
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
	@RequestMapping(value = "/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);

		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}

		//WK100 기업
		if("BOARD21".equals(code)) {
			CommCdVO companyCode = this.commCdService.selectCommCd("WK_COMPANY");
			model.addAttribute("companyCode", companyCode);
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//상세조회
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, id, "MODI", null);
		boolean result = (boolean) boardMap.get("result");
		if(!result) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.deleteboard"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		BbsVO bbsVO = (BbsVO) boardMap.get("bbsVO");
		
		//에디터 미사용시 줄바꿈 역치환
		if("N".equals(bbsMgtVO.getEditorYn())) {
			bbsVO.setContents(HtmlConvertorUtil.changeBrToLineSeparator(bbsVO.getContents()));
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsVO", bbsVO);
		
		return web.returnView(VIEW_PATH, "/modify");
	}
	
	/**
	 * 게시글화면 수정처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									,BbsVO bbsVO ,BindingResult bbsBindingResult
									,@RequestParam(value = "sCode", defaultValue="") String code
									,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);

		//WK100 기업
		if("BOARD21".equals(code)) {
			CommCdVO companyCode = this.commCdService.selectCommCd("WK_COMPANY");
			model.addAttribute("companyCode", companyCode);
		}
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);

		//유효성 검증
		beanValidator.validate(bbsVO, bbsBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bbsBindingResult.hasErrors()) {
			model.addAttribute("bbsVO", bbsVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//상세조회
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, bbsVO.getBbsId(), "MODI", null);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		BbsVO oriBbsVO = (BbsVO) boardMap.get("bbsVO");
		//부모게시글 확인후 기본값 세팅
		BbsVO parentBbsVO = bbsService.selectBbs(bbsMgtVO, oriBbsVO.getParentId());
		if(parentBbsVO != null) {
			bbsVO.setSecret(parentBbsVO.getSecret());
			bbsVO.setCategory(parentBbsVO.getCategory());
		}
		
		//첨부파일 체크
		if ("Y".equals(bbsMgtVO.getFileYn()) && bbsVO.getUploadFiles() != null) {
			
			//업로드된 파일개수
			int uploadCount = bbsVO.getUploadFiles().size();
			
			//기등록된 파일개수
			if(bbsVO.getViewFiles() != null && bbsVO.getViewFiles().size() > 0) {
				for(ViewFileVO viewFileVO : bbsVO.getViewFiles()) {
					if (viewFileVO.getFno() == 0) uploadCount++; //0일경우 삭제가 안된상태
				}
			}			
			
			//첨부파일 개수 확인
			if(uploadCount > bbsMgtVO.getFileCnt()) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCode", code);
				resultParam.put("sId", bbsVO.getBbsId());
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
			
			//첨부파일 확장자 체크
			if(!StringUtil.isNull(bbsMgtVO.getFileExt())) {
				boolean denyCheck = false;
				//등록된 확장자
				List<String> extList = new ArrayList<String>(Arrays.asList(bbsMgtVO.getFileExt().split(",")));
				
				if(extList != null && extList.size() > 0) {
					//현재 첨부파일의 확장자를 비교
					for (MultipartFile uploadFile : bbsVO.getUploadFiles()) {
						if(!extList.contains(FileUtil.getFileExt(uploadFile))) {
							denyCheck = true;
							break;
						}
					}
				}				
				//false일 경우 return;
				if(denyCheck) {
					model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadext"));
					model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
					resultParam.put("sCode", code);
					resultParam.put("sId", bbsVO.getBbsId());
					resultParam.put("sCate", category);
					model.addAttribute("resultParam", resultParam);
					return web.returnError();
				}
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bbsVO", bbsVO);
			return web.returnView(VIEW_PATH, "/modify");
		}
		
		//수정 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.updateBbs(bbsMgtVO, bbsVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", bbsVO.getBbsId());
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
	@RequestMapping(value = "/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sId", defaultValue="") String id
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//상세조회
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, id, "REMOVE", null);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		BbsVO bbsVO = (BbsVO) boardMap.get("bbsVO");
		
		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.deleteBbs(bbsMgtVO, bbsVO.getBbsId());
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시		
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시				
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
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
	@RequestMapping(value = "/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @RequestParam(value = "sCode", defaultValue="") String code
									, @RequestParam(value = "sCate", defaultValue="ALL") String category
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
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
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);				
			return web.returnError();
		}
		
		//관리자인지 확인
		if(!bbsMgtVO.isAdminUser()){
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//삭제처리
		int result = bbsService.deleteCheckedBbs(bbsMgtVO, checkedSId);
		
		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
		
		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
		
		//리턴시 추가 파라미터 필요한경우 (생략가능)
		Map<String, Object> resultParam = new HashMap<String, Object>();
		resultParam.put("sCode", code);
		resultParam.put("sCate", category);
		model.addAttribute("resultParam", resultParam);
		
		return web.returnSuccess();
		
	}
	
	/**
	 * 게시글화면 답변폼 화면
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/reply.do")
	@ProgramInfo(code="REPLY_FORM", name="답변폼 화면")
	public String replyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String parentId
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REPLY");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);
		
		//게시판 스킨
		if(!StringUtil.isNull(bbsMgtVO.getSkinCdVal())) {
			model.addAttribute("GV_BOARD_SKIN_CODE", bbsMgtVO.getSkinCdVal());
		}
		
		//부모게시글 확인
		BbsVO parentBbsVO = bbsService.selectBbs(bbsMgtVO, parentId);
		if(parentBbsVO == null) {			
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.deleteboard"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);
		
		//VO 생성
		BbsVO bbsVO = new BbsVO();
		
		//로그인 정보
		if(SessionUtil.getLoginUser() != null) {
			bbsVO.setWriter(SessionUtil.getLoginUser().getLoginNm());
		}
		
		//에디터 미사용시 줄바꿈 치환
		String contents = "";
		if("N".equals(bbsMgtVO.getEditorYn())) {
			contents = HtmlConvertorUtil.changeBrToLineSeparator(parentBbsVO.getContents());
		}
		
		//부모값 기본세팅
		bbsVO.setParentId(parentId);
		bbsVO.setContents(contents);
		bbsVO.setCategory(parentBbsVO.getCategory());
		bbsVO.setSecret(parentBbsVO.getSecret());
		
		//조회결과 MODEL ADD
		model.addAttribute("bbsVO", bbsVO);
		
		return web.returnView(VIEW_PATH, "/reply");
	}
	
	/**
	 * 게시글화면 답변처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/replyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REPLY", name="등록처리")
	public String replyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,BbsVO bbsVO ,BindingResult bbsBindingResult
								,@RequestParam(value = "sCode", defaultValue="") String code
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REPLY");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/sys.do");
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

		//유효성 검증
		beanValidator.validate(bbsVO, bbsBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (bbsBindingResult.hasErrors()) {
			model.addAttribute("bbsVO", bbsVO);
			return web.returnView(VIEW_PATH, "/reply");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
				
		//부모게시글 확인
		BbsVO parentBbsVO = bbsService.selectBbs(bbsMgtVO, bbsVO.getParentId());
		if(parentBbsVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.deleteboard"));
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		} else {
			//부모게시글 기본값 세팅
			bbsVO.setSecret(parentBbsVO.getSecret()); //비밀글 여부
			bbsVO.setCategory(parentBbsVO.getCategory()); //카테고리
		}
		
		//첨부파일 체크
		if ("Y".equals(bbsMgtVO.getFileYn()) && bbsVO.getUploadFiles() != null) {
			
			int uploadCount = bbsVO.getUploadFiles().size();
			
			//첨부파일 개수 확인
			if(uploadCount > bbsMgtVO.getFileCnt()) {
				model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadcnt"));
				model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCode", code);
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
			
			//첨부파일 확장자 체크
			if(!StringUtil.isNull(bbsMgtVO.getFileExt())) {
				boolean denyCheck = false;
				//등록된 확장자
				List<String> extList = new ArrayList<String>(Arrays.asList(bbsMgtVO.getFileExt().split(",")));
				
				if(extList != null && extList.size() > 0) {
					//현재 첨부파일의 확장자를 비교
					for (MultipartFile uploadFile : bbsVO.getUploadFiles()) {
						if(!extList.contains(FileUtil.getFileExt(uploadFile))) {
							denyCheck = true;
							break;
						}
					}
				}				
				//false일 경우 return;
				if(denyCheck) {
					model.addAttribute("resultMsg", messageUtil.getMessage("message.error.uploadext"));
					model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
					resultParam.put("sCode", code);
					resultParam.put("sCate", category);
					model.addAttribute("resultParam", resultParam);
					return web.returnError();
				}
			}
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("bbsVO", bbsVO);
			return web.returnView(VIEW_PATH, "/reply");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.insertBbs(bbsMgtVO, bbsVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
			
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", bbsVO.getBbsCd());
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCode", code);
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}
		
	}
		
}
