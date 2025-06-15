package kr.co.nanwe.bbs.web;

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
import org.springframework.web.bind.annotation.PathVariable;
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
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.file.service.ViewFileVO;

/**
 * @Class Name 		: BoardController
 * @Description 	: 게시글 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/board")
@Program(code="BOARD", name="게시글")
@Controller
public class BoardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

	/** View Path */
	private static final String VIEW_PATH = "main/board";

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

	/** Root Forward */
	@RequestMapping(value = "/{path}.do")
	public String root(@PathVariable("path") String path, HttpServletRequest request) {
		return web.forward("/board/"+path+"/list.do");
	}

	/**
	 * 게시글화면 목록조회
	 * @param
	 * @return
	 * @exception
	 */
	@RequestMapping(value = "/{path}/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@PathVariable("path") String path
						,@RequestParam(value = "sCate", defaultValue="ALL") String category){
		category = category.replaceAll("&amp;gt;",">").trim();
		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

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
	@RequestMapping(value = "/{path}/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						,@PathVariable("path") String path
						,@RequestParam(value = "sId", defaultValue="") String id
						,@RequestParam(value = "sCate", defaultValue="ALL") String category
						,@RequestParam(value = "bbsPw", required=false) String bbsPw){

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "VIEW");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, id, "VIEW", bbsPw);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			//비회원 작성글인 경우
			if(CodeConfig.NO_LOGIN.equals(errMsgCd)) {
				model.addAttribute("sId", id);
				model.addAttribute("formAction", "/board/"+path+"/view.do");
				return web.returnView(VIEW_PATH, "/password");
			} else {
				model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
				model.addAttribute("redirectUrl", "/board/"+path+".do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
		}

		if(!StringUtil.isNull(bbsPw)) {
			model.addAttribute("bbsPw", bbsPw);
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
	@RequestMapping(value = "/{path}/register.do")
	@ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
	public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@PathVariable("path") String path
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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

		//bbsCd 호출
		String bbsCd = bbsMgtVO.getCode();
		System.out.println("test>>>>>>"+bbsCd);

		//로그인 정보
		if(SessionUtil.getLoginUser() != null) {
			bbsVO.setWriter(SessionUtil.getLoginUser().getLoginNm());
		}

		//contents 필드 설정
		switch (bbsCd) {
		case "BOARD27":
			bbsVO.setContents("＊본 게시판은 현재 점프업 프로젝트 참여자가 빌드업 프로젝트로 참여유형 변경 요청 시 작성하는 게시판입니다. \n"
					  + "빌드업 프로젝트로 변경 요청하는 것이 맞는지 다시 한 번 확인해주세요.\n" + "\n" +
					  "＊빌드업 및 접프업 프로젝트에 대한 설명은 위의 사업안내 탭에서 확인하시기 바랍니다.\n" + "\n" +
					  "*아래 서식을 작성해주세요.\n" + "○학과:\n" + "○학번:\n" + "○이름:\n" + "○유형변경사유:");
			break;
		case "BOARD24":
			bbsVO.setContents("＊본 게시판은 현재 점프업 프로젝트 참여자가 빌드업 프로젝트로 참여유형 변경 요청 시 작성하는 게시판입니다. \n"
					  + "빌드업 프로젝트로 변경 요청하는 것이 맞는지 다시 한 번 확인해주세요.\n" +
					  "\n" +
					  "*아래 서식을 작성해주세요.\n" +
					  "○학과:\n" +
					  "○학번:\n" +
					  "○이름:\n" +
					  "○유형변경사유: 빌드업에서 점프업으로 유형변경하여 수당활동을 하기 위함");
			break;
		case "BOARD25":
			bbsVO.setContents("＊본 게시판은 현재 점프업 프로젝트 참여자가 워크넷 IAP(취업활동계획서) 수정 및 승인 요청 시 작성하는 게시판입니다. \n"
					  + "＊현재 참여 중인 유형(말머리글)을 정확히 선택해주세요. \n" +
					  "\n" +
					  "＊본인의 IAP계획은 워크넷>재학생맞춤형고용서비스> 마이페이지> IAP계획관리에서 확인 가능합니다.\n" +
					  "\n" +
					  "＊해당 게시글을 처음 작성하는 경우 IAP 수정 게시판의 'IAP 수정 방법' 게시글을 참고해주시기 바랍니다.\n" +
					  "\n" +
					  "<중점영역>\r\n" +
					  "자기주도형 - 취업역량 / 창업지원 / 구직활동 / 기타 중 택1  \r\n" +
					  "서비스참여형 - 직업훈련 / 일경험(인턴) / 자격취득 / 심리상담 / 기타 중 택1\n" +
					  "\n" +
					  "▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽아래 서식을 작성해주세요▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽\n" +
					  "\n" +
					  "○학과:\n" +
					  "○학번:\n" +
					  "○이름:\n" +
					  "\n" +
					  "---------------------------------------------------------------------------------------------------------------------------------------\r\n" +
					  "<프로그램추가 및 수정>\r\n" +
					  "\n" +
					  "(프로그램 수정 시 수정할 프로그램명:  ) \r\n" +
					  "-중점영역: *위의 중점영역 중 택1\r\n" +
					  "-활동내용:\r\n" +
					  "-참여기관: 교내/교외 - \r\n" +
					  "-참여기간: 202 년 월 ~ 202 년 월\r\n" +
					  "-참여시간: \r\n" +
					  "-목표달성시기: 1학기/여름방학/2학기/겨울방학\r\n" +
					  "\n" +
					  "＊여러 개의 강의를 추가할 경우 위의 서식을 붙여넣어 작성 중인 글에 추가로 작성해주시기 바랍니다.");
			break;
		case "BOARD26":
			bbsVO.setContents("＊본 게시판은 현재 빌드업 프로젝트 참여자가 점프업 프로젝트로 참여유형 변경 요청 시 작성하는 게시판입니다. \n"
	                  + "＊빌드업에서 점프업으로 유형변경을 희망하는 학생은 변경신청 글 작성 후 1차상담 진행하시기 바랍니다. \n" + "\n" +
	                  " * 1차상담 신청 주소: <a href='https://whattime.co.kr/jumpup/meeting22'>https://whattime.co.kr/jumpup/meeting22</a> \n" + "\n" +
	                  " * 23년도 학생은 24년도 자동 사업 신청(연장)이 아닙니다. 신규 사업신청진행바랍니다.\n" +
	                  "(사업신청방법: 점프업- 참여방법안내- 공지사항 확인)\n" + "\n\n" +
	                  "*아래 서식을 작성해주세요.\n" + "○학과:\n" + "○학번:\n" + "○이름:\n" + "○유형변경사유:");
	        break;
		case "BOARD28":
			bbsVO.setContents("＊본 게시판은 수당신청을 위해 워크넷 IAP결과관리 및 포인트관리 작성 후 작성하는 게시판입니다.  \n" +
					  "\n" +
					  "* 워크넷 IAP결과관리 및 포인트관리를 작성하지 않은 학생은 공지사항에서 수당신청 방법 확인하여 진행 바랍니다. \n" +
					  "\n" +
					  "*아래 서식을 작성해주세요.\n" +
					  "○학과:\n" +
					  "○학번:\n" +
					  "○이름:\n" +
					  "○참여유형: 자기주도형/ 서비스참여형\n" +
					  "\n" +
					  "글 제목 예시: 홍길동 / 자기주도형 수당신청 확인 요청드립니다");
			break;
		default:
		    bbsVO.setContents("");
		    break;
		}



		bbsVO.setContents(bbsVO.getContents().replace("\n", "<br>"));

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
	@RequestMapping(value = "/{path}/registerAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REGISTER", name="등록처리")
	public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,BbsVO bbsVO ,BindingResult bbsBindingResult
								,@PathVariable("path") String path
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
			return web.returnView(VIEW_PATH, "/register");
		}

		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsVO == null) {
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
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
				model.addAttribute("redirectUrl", "/board/"+path+"/register.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
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
					model.addAttribute("redirectUrl", "/board/"+path+"/register.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
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
			LOGGER.debug(">>>>>>>>>> bbsVO >>>>>>>>> " + bbsVO);
			result = bbsService.insertBbs(bbsMgtVO, bbsVO);
		}

		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", "/board/"+path+"/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			if(!StringUtil.isNull(bbsVO.getPw())) {
				resultParam.put("bbsPw", bbsVO.getPw());
			}
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
		} else { //실패시
			model.addAttribute("redirectUrl", "/board/"+path+"/register.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
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
	@RequestMapping(value = "/{path}/modify.do")
	@ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
	public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								, @PathVariable("path") String path
								, @RequestParam(value = "sId", defaultValue="") String id
								, @RequestParam(value = "sCate", defaultValue="ALL") String category
								, @RequestParam(value = "bbsPw", required=false) String bbsPw){

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, id, "MODI", bbsPw);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			//비회원 작성글인 경우
			if(CodeConfig.NO_LOGIN.equals(errMsgCd)) {
				model.addAttribute("sId", id);
				model.addAttribute("formAction", "/board/"+path+"/modify.do");
				return web.returnView(VIEW_PATH, "/password");
			} else {
				model.addAttribute("resultMsg", messageUtil.getMessage(errMsgCd));
				model.addAttribute("redirectUrl", "/board/"+path+".do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
		}

		if(!StringUtil.isNull(bbsPw)) {
			model.addAttribute("bbsPw", bbsPw);
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
	@RequestMapping(value = "/{path}/modifyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="MODIFY", name="수정처리")
	public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, BbsVO bbsVO ,BindingResult bbsBindingResult
									, @PathVariable("path") String path
									, @RequestParam(value = "sCate", defaultValue="ALL") String category
									, @RequestParam(value = "bbsPw", required=false) String bbsPw) {

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
			return web.returnView(VIEW_PATH, "/modify");
		}

		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		//유효성 검사 로직 작성
		if(bbsVO == null) {
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			return web.returnError();
		}

		//상세조회
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, bbsVO.getBbsId(), "MODI", bbsPw);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			//비회원 작성글인 경우
			if(CodeConfig.NO_LOGIN.equals(errMsgCd)) {
				model.addAttribute("sId", bbsVO.getBbsId());
				model.addAttribute("formAction", "/board/"+path+"/modify.do");
				return web.returnView(VIEW_PATH, "/password");
			} else {
				model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
				model.addAttribute("redirectUrl", "/board/"+path+".do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
		}

		if(!StringUtil.isNull(bbsPw)) {
			model.addAttribute("bbsPw", bbsPw);
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
				model.addAttribute("redirectUrl", "/board/"+path+"/modify.do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
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
					model.addAttribute("redirectUrl", "/board/"+path+"/modify.do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
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
			model.addAttribute("redirectUrl", "/board/"+path+"/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			if(!StringUtil.isNull(bbsPw)) {
				resultParam.put("bbsPw", bbsPw);
			}
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();

		} else { //실패시

			model.addAttribute("redirectUrl", "/board/"+path+"/modify.do");

			Map<String, Object> resultParam = new HashMap<String, Object>();
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
	@RequestMapping(value = "/{path}/removeAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								, @PathVariable("path") String path
								, @RequestParam(value = "sId", defaultValue="") String id
								, @RequestParam(value = "sCate", defaultValue="ALL") String category
								, @RequestParam(value = "bbsPw", required=false) String bbsPw) {

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			return web.returnError();
		}
		model.addAttribute("bbsMgtVO", bbsMgtVO);

		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sCode", code);
		model.addAttribute("sCate", category);

		//상세조회
		Map<String, Object> boardMap = bbsService.selectBbsByAuth(bbsMgtVO, id, "REMOVE", bbsPw);
		boolean authCheck = (boolean) boardMap.get("result");
		if(!authCheck) {
			String errMsgCd = (String) boardMap.get("errMsgCd");
			//비회원 작성글인 경우
			if(CodeConfig.NO_LOGIN.equals(errMsgCd)) {
				model.addAttribute("sId", id);
				model.addAttribute("formAction", "/board/"+path+"/removeAction.do");
				return web.returnView(VIEW_PATH, "/password");
			} else {
				model.addAttribute("resultMsg", messageUtil.getMessage((String) boardMap.get("errMsgCd")));
				model.addAttribute("redirectUrl", "/board/"+path+".do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
				resultParam.put("sCate", category);
				model.addAttribute("resultParam", resultParam);
				return web.returnError();
			}
		}

		BbsVO bbsVO = (BbsVO) boardMap.get("bbsVO");

		//삭제처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = bbsService.deleteBbs(bbsMgtVO, bbsVO.getBbsId());
		}

		//처리 결과에 따른 return
		if(result > 0) { //성공시
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();

		} else { //실패시
			model.addAttribute("redirectUrl", "/board/"+path+"/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			if(!StringUtil.isNull(bbsPw)) {
				resultParam.put("bbsPw", bbsPw);
			}
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
	@RequestMapping(value = "/{path}/checkRemoveAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REMOVE", name="삭제처리")
	public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
									, @PathVariable("path") String path
									, @RequestParam(value = "sCate", defaultValue="ALL") String category
									, @RequestParam(value = "checkedSId", required=false) String checkedSId) {

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REGI");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}

		//관리자인지 확인
		if(!bbsMgtVO.isAdminUser()){
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}

		//삭제처리
		int result = bbsService.deleteCheckedBbs(bbsMgtVO, checkedSId);

		//결과메시지  (생략가능)
		model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));

		//리턴페이지 (생략시 메인페이지 리턴)
		model.addAttribute("redirectUrl", "/board/"+path+".do");
		Map<String, Object> resultParam = new HashMap<String, Object>();
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
	@RequestMapping(value = "/{path}/reply.do")
	@ProgramInfo(code="REPLY_FORM", name="답변폼 화면")
	public String replyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,@RequestParam(value = "sId", defaultValue="") String parentId
								,@PathVariable("path") String path
								,@RequestParam(value = "sCate", defaultValue="ALL") String category){

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REPLY");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
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
	@RequestMapping(value = "/{path}/replyAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="REPLY", name="등록처리")
	public String replyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,BbsVO bbsVO ,BindingResult bbsBindingResult
								,@PathVariable("path") String path
								,@RequestParam(value = "sCate", defaultValue="ALL") String category) {

		//게시판 코드
		model.addAttribute("boardPath", path);
		String code = StringUtil.camelToUpperUnderscore(path);

		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(code, "REPLY");
		if(bbsMgtVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.auth"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
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
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}

		//부모게시글 확인
		BbsVO parentBbsVO = bbsService.selectBbs(bbsMgtVO, bbsVO.getParentId());
		if(parentBbsVO == null) {
			model.addAttribute("resultMsg", messageUtil.getMessage("message.error.deleteboard"));
			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
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
				model.addAttribute("redirectUrl", "/board/"+path+".do");
				Map<String, Object> resultParam = new HashMap<String, Object>();
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
					model.addAttribute("redirectUrl", "/board/"+path+".do");
					Map<String, Object> resultParam = new HashMap<String, Object>();
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
			model.addAttribute("redirectUrl", "/board/"+path+"/view.do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", bbsVO.getBbsId());
			resultParam.put("sCate", category);
			if(!StringUtil.isNull(bbsVO.getPw())) {
				resultParam.put("bbsPw", bbsVO.getPw());
			}
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();

		} else { //실패시

			model.addAttribute("redirectUrl", "/board/"+path+".do");
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sCate", category);
			model.addAttribute("resultParam", resultParam);
			return web.returnError();
		}

	}

}
