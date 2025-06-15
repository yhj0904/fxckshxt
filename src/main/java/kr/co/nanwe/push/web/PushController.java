package kr.co.nanwe.push.web;

import java.util.ArrayList;
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
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.external.service.ExternalService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.push.service.PushGrpMembVO;
import kr.co.nanwe.push.service.PushGrpMstVO;
import kr.co.nanwe.push.service.PushGrpService;
import kr.co.nanwe.push.service.PushNoticeSendVO;
import kr.co.nanwe.push.service.PushNoticeService;
import kr.co.nanwe.push.service.PushNoticeVO;

/**
 * @Class Name 		: PushSendController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.06.02		임문환			최초생성
 */

@RequestMapping(value = "/push")
@Program(code="PUSH", name="알림전송")
@Controller
public class PushController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(PushSendController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "main/push";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Service */
	@Resource(name = "pushNoticeService")
	private PushNoticeService pushNoticeService;
	
	/** Service */
	@Resource(name = "externalService")
	private ExternalService externalService;
	
	/** Service */
	@Resource(name = "pushGrpService")
	private PushGrpService pushGrpService;
	
	/** Constructor */
	public PushController() {		
		RequestMapping requestMapping = PushController.class.getAnnotation(RequestMapping.class);
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
		return web.forward(REDIRECT_PATH + "/send.do");
	}
	
	/**
	 * 알림발송
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/send.do")
	@ProgramInfo(code="SEND", name="발송")
	public String send(Model model, HttpServletRequest request, @ModelAttribute PushGrpMstVO pushGrpMstVO){		
		
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return web.returnError();
		}
		
		//VO 생성
		PushNoticeVO pushNoticeVO = new PushNoticeVO();
		
		//초기값 설정
		pushNoticeVO.setReservationYn("N");
		pushNoticeVO.setReservationYYYYMMDD(DateUtil.getDate("yyyy-MM-dd"));
		pushNoticeVO.setReservationHour(Integer.parseInt(DateUtil.getDate("H")));
		pushNoticeVO.setReservationMinute(Integer.parseInt(DateUtil.getDate("m")));
		
		pushNoticeVO.setUserId(loginVO.getLoginId());
		pushNoticeVO.setUserNm(loginVO.getLoginNm());
		pushNoticeVO.setUserMobile(loginVO.getMbphNo());
		
		//전 화면에서 선택된 아이디가 있는 경우
		if(pushGrpMstVO != null) {
			if(pushGrpMstVO.getMemList() != null && pushGrpMstVO.getMemList().size() > 0) {
				List<PushNoticeSendVO> sendList = new ArrayList<PushNoticeSendVO>();
				for(PushGrpMembVO pushGrpMembVO : pushGrpMstVO.getMemList()) {
					PushNoticeSendVO pushNoticeSendVO = new PushNoticeSendVO();
					pushNoticeSendVO.setUserId(pushGrpMembVO.getGrpMembId());
					pushNoticeSendVO.setUserNm(pushGrpMembVO.getGrpMembNm());
					pushNoticeSendVO.setUserMobile(pushGrpMembVO.getGrpMembMobile());
					sendList.add(pushNoticeSendVO);
				}
				pushNoticeVO.setSendList(sendList);
			}
		}
		
		model.addAttribute("pushNoticeVO", pushNoticeVO);
		
		return web.returnView(VIEW_PATH, "/notice/send");
	}
	
	/**
	 * 알림발송관리 등록처리
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sendAction.do", method = RequestMethod.POST)
	@ProgramInfo(code="SEND", name="등록처리")
	public String sendAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
								,PushNoticeVO pushNoticeVO , BindingResult pushNoticeBindingResult) {
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);

		//유효성 검증
		beanValidator.validate(pushNoticeVO, pushNoticeBindingResult);
		
		//유효성 검증 에러인 경우 RETURN
		if (pushNoticeBindingResult.hasErrors()) {
			model.addAttribute("pushNoticeVO", pushNoticeVO);
			return web.returnView(VIEW_PATH, "/notice/send");
		}
		
		/* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
		boolean addtionalValid = true;
		
		//유효성 검사 로직 작성
		if(pushNoticeVO == null) {
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/send.do");
			return web.returnError();
		}
		
		//파일첨부시 이미지인지 체크
		if(FileUtil.isFile(pushNoticeVO.getUploadFile()) && !"IMAGE".equals(FileUtil.getFileType(pushNoticeVO.getUploadFile()))) {
			addtionalValid = false;
			pushNoticeBindingResult.rejectValue("uploadFile", "errors.file.img");
		}
		
		//false 인 경우 view로 리턴
		if(!addtionalValid) {
			model.addAttribute("pushNoticeVO", pushNoticeVO);
			return web.returnView(VIEW_PATH, "/notice/send");
		}
		
		//등록 처리
		int result = 0;
		if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
			result = pushNoticeService.insertPushNotice(pushNoticeVO);
		}
		
		//처리 결과에 따른 return
		if(result > 0) { //성공시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", "/push/notice/view.do");
			
			//리턴시 추가 파라미터 필요한경우 (생략가능)
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("sId", pushNoticeVO.getNoticeNo());
			model.addAttribute("resultParam", resultParam);
			return web.returnSuccess();
			
		} else { //실패시
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/send.do");
			
			return web.returnError();
			
		}
		
	}
	
	/**
	 * 부서 및 사용자 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/search.do")
	@ProgramInfo(code="SEARCH", name="발송")
	public String search(Model model, HttpServletRequest request){	
		return web.returnView(VIEW_PATH, "/search_pop", "POP");
	}
	
}
