package kr.co.nanwe.content.web;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.taglibs.EgovDoubleSubmitHelper;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.content.service.ContentService;
import kr.co.nanwe.content.service.ContentVO;

/**
 * @Class Name       : SysContentController
 * @Description    : 컨텐츠 CRUD Controller
 * @Modification Information
 * @
 * @ 수정일         수정자         수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06      임문환         최초생성
 */

@Program(code="COM_CONTENT", name="컨텐츠관리")
@Controller
public class SysContentController {
   
   //private static final Logger LOGGER = LoggerFactory.getLogger(SysContentController.class);
   
   /** View Path */
   private static final String VIEW_PATH = "sys/content";
   
   /** Redirect Path */
   private String REDIRECT_PATH = "/sys/content";
   
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
   @Resource(name = "contentService")
   private ContentService contentService;
   
   /** Constructor */
   public SysContentController() {      
      RequestMapping requestMapping = SysContentController.class.getAnnotation(RequestMapping.class);
      if(requestMapping != null) {
         this.REDIRECT_PATH = requestMapping.value()[0];
      }
   }
   
   /** Root Forward */
   @RequestMapping(value = "/sys/content")
   public String root(){
      if(!"do".equals(StringUtil.getExtension(RequestUtil.getURI()))) {
         return web.returnJsp("error/error404");
      }
      return web.forward(REDIRECT_PATH + "/list.do");
   }
   
   /**
    * 컨텐츠관리 목록조회
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = {"/sys/content.do", "/sys/content/list.do"})
   @ProgramInfo(code="LIST", name="목록조회")
   public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      model.addAttribute("GV_PRESENT_PATH", "/sys/content");
      //목록조회
      Map<String, Object> map = contentService.selectContentList(search);   
      
      //조회결과 MODEL ADD
      model.addAllAttributes(map);
      
      return web.returnView(VIEW_PATH, "/list");
   }
   
   /**
    * 컨텐츠관리 상세조회
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/view.do")
   @ProgramInfo(code="VIEW", name="상세조회")
   public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                  ,@RequestParam(value = "sId", defaultValue="") String id){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      
      //상세조회
      ContentVO contentVO = contentService.selectContent(id);
      
      //조회결과가 없는 경우 RESULT VIEW 이동
      if(contentVO == null) {
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
         
         return web.returnError();
      }
      
      //조회결과 MODEL ADD
      model.addAttribute("contentVO", contentVO);
      
      return web.returnView(VIEW_PATH, "/view");
   }
   
   /**
    * 컨텐츠관리 등록폼 화면
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/register.do")
   @ProgramInfo(code="REGISTER_FORM", name="등록폼 화면")
   public String registerView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      
      //VO 생성
      ContentVO contentVO = new ContentVO();
      
      //조회결과 MODEL ADD
      model.addAttribute("contentVO", contentVO);
      
      return web.returnView(VIEW_PATH, "/register");
   }
   
   /**
    * 컨텐츠관리 등록처리
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/registerAction.do", method = RequestMethod.POST)
   @ProgramInfo(code="REGISTER", name="등록처리")
   public String registerAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                        ,ContentVO contentVO ,BindingResult contentBindingResult) {
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);

      //유효성 검증
      beanValidator.validate(contentVO, contentBindingResult);
      
      //유효성 검증 에러인 경우 RETURN
      if (contentBindingResult.hasErrors()) {
         model.addAttribute("contentVO", contentVO);
         return web.returnView(VIEW_PATH, "/register");
      }
      
      /* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
      boolean addtionalValid = true;
      //유효성 검사 로직 작성
      if(contentVO == null) {
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
         return web.returnError();
      }
      //false 인 경우 view로 리턴
      if(!addtionalValid) {
         model.addAttribute("contentVO", contentVO);
         return web.returnView(VIEW_PATH, "/register");
      }
      
      //등록 처리
      int result = 0;
      if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
         result = contentService.insertContent(contentVO);
      }
      
      //처리 결과에 따른 return
      if(result > 0) { //성공시
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
         
         //리턴시 추가 파라미터 필요한경우 (생략가능)
         Map<String, Object> resultParam = new HashMap<String, Object>();
         resultParam.put("sId", contentVO.getContId());
         model.addAttribute("resultParam", resultParam);
         return web.returnSuccess();
         
      } else { //실패시
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/register.do");
         
         return web.returnError();
         
      }
      
   }
   
   /**
    * 컨텐츠관리 수정폼 화면
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/modify.do")
   @ProgramInfo(code="MODIFY_FORM", name="수정폼 화면")
   public String modifyView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                        ,@RequestParam(value = "sId", defaultValue="") String id){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      
      //상세조회
      ContentVO contentVO = contentService.selectContent(id);
      
      //조회결과가 없는 경우 RESULT VIEW 이동
      if(contentVO == null) {
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
            
         return web.returnError();
      }
      
      //조회결과 MODEL ADD
      model.addAttribute("contentVO", contentVO);
      
      return web.returnView(VIEW_PATH, "/modify");
   }
   
   /**
    * 컨텐츠관리 수정처리
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/modifyAction.do", method = RequestMethod.POST)
   @ProgramInfo(code="MODIFY", name="수정처리")
   public String modifyAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                           ,ContentVO contentVO ,BindingResult contentBindingResult) {
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);

      //유효성 검증
      beanValidator.validate(contentVO, contentBindingResult);
      
      //유효성 검증 에러인 경우 RETURN
      if (contentBindingResult.hasErrors()) {
         model.addAttribute("contentVO", contentVO);
         return web.returnView(VIEW_PATH, "/modify");
      }
      
      /* 유효성 추가검증 (Validator 외의 검증이 필요한 경우 작성) */
      boolean addtionalValid = true;
      //유효성 검사 로직 작성
      if(contentVO == null) {
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
         return web.returnError();
      }
      //false 인 경우 view로 리턴
      if(!addtionalValid) {
         model.addAttribute("contentVO", contentVO);
         return web.returnView(VIEW_PATH, "/modify");
      }
      
      //수정 처리
      int result = 0;
      if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
         result = contentService.updateContent(contentVO);
      }
      
      //처리 결과에 따른 return
      if(result > 0) { //성공시
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
         
         //리턴시 추가 파라미터 필요한경우 (생략가능)
         Map<String, Object> resultParam = new HashMap<String, Object>();
         resultParam.put("sId", contentVO.getContId());
         model.addAttribute("resultParam", resultParam);
         return web.returnSuccess();
         
      } else { //실패시
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/modify.do");
         
         //리턴시 추가 파라미터 필요한경우 (생략가능)
         Map<String, Object> resultParam = new HashMap<String, Object>();
         resultParam.put("sId", contentVO.getContId());
         model.addAttribute("resultParam", resultParam);
         return web.returnError();
         
      }
      
   }
   
   /**
    * 컨텐츠관리 삭제 화면
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/removeAction.do")
   @ProgramInfo(code="REMOVE", name="삭제폼 화면")
   public String removeAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                        ,@RequestParam(value = "sId", defaultValue="") String id){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      
      //상세조회
      ContentVO contentVO = contentService.selectContent(id);
      
      //조회결과가 없는 경우 RESULT VIEW 이동
      if(contentVO == null) {
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
            
         return web.returnError();
      }
      
      //삭제처리
      int result = 0;
      if (EgovDoubleSubmitHelper.checkAndSaveToken()) {
         result = contentService.deleteContent(id);
      }
      
      //처리 결과에 따른 return
      if(result > 0) { //성공시   
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");

         return web.returnSuccess();
         
      } else { //실패시   
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/view.do");
         
         //리턴시 추가 파라미터 필요한경우 (생략가능)
         Map<String, Object> resultParam = new HashMap<String, Object>();
         resultParam.put("sId", contentVO.getContId());
         model.addAttribute("resultParam", resultParam);
         return web.returnError();            
      }
   }
   
   /**
    * 컨텐츠관리 선택 삭제처리
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/checkRemoveAction.do", method = RequestMethod.POST)
   @ProgramInfo(code="REMOVE", name="삭제처리")
   public String checkRemoveAction(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                           , @RequestParam(value = "checkedSId", required=false) String checkedSId) {
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      
      //선택된 데이터가 없는 경우
      if(checkedSId == null || "".equals(checkedSId)) {
         
         //결과메시지  (생략가능)
         model.addAttribute("resultMsg", messageUtil.getMessage("message.error.noCheck"));
         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
            
         return web.returnError();
      }
      
      //삭제처리
      int result = contentService.deleteCheckedContent(checkedSId);
      
      //결과메시지  (생략가능)
      model.addAttribute("resultMsg", messageUtil.getMessage("message.success.checkRemove", new String[] {Integer.toString(result)}));
      
      //리턴페이지 (생략시 메인페이지 리턴)
      model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
      
      return web.returnSuccess();
      
   }
   
   /**
    * 컨텐츠관리 백업목록조회
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/back/list.do")
   @ProgramInfo(code="BACK_LIST", name="백업 목록조회")
   public String backList(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                     , @RequestParam(value = "contId", required=false) String contId){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      model.addAttribute("contId", contId);
      
      //목록조회
      Map<String, Object> map = contentService.selectContentBackList(search, contId);   
      
      //조회결과 MODEL ADD
      model.addAllAttributes(map);
      
      return web.returnView(VIEW_PATH, "/back_list", "pop");
   }
   
   /**
    * 컨텐츠관리 백업 상세조회
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/back/view.do")
   @ProgramInfo(code="BACK_VIEW", name="백업 상세조회")
   public String backView(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
                  ,@RequestParam(value = "contId", defaultValue="") String contId
                  ,@RequestParam(value = "seq", defaultValue="0") int seq){
      
      //검색조건 MODEL ADD
      model.addAttribute("search", search);
      
      //상세조회
      ContentVO contentVO = contentService.selectContentBack(contId, seq);
      
      //조회결과가 없는 경우 RESULT VIEW 이동
      if(contentVO == null) {         
         //리턴페이지 (생략시 메인페이지 리턴)
         model.addAttribute("redirectUrl", REDIRECT_PATH + "/back/list.do");         
         return web.returnError();
      }
      
      //조회결과 MODEL ADD
      model.addAttribute("contentVO", contentVO);
      
      return web.returnView(VIEW_PATH, "/back_view", "pop");
   }
   
   /**
    * 컨텐츠관리 백업 복원
    * @param 
    * @return
    * @exception 
    */
   @RequestMapping(value = "/sys/content/back/modifyAction.json", method = RequestMethod.POST)
   @ProgramInfo(code="BACK_MODIFY", name="백업 복원")
   @ResponseBody
   public boolean modifyAction( HttpServletRequest request , @RequestParam(value = "contId", defaultValue="") String contId, @RequestParam(value = "seq", defaultValue="0") int seq) {
      //수정 처리
      int result = contentService.updateContentByBack(contId, seq);      
      if(result > 0) { //성공시
         return true;
         
      }
      return false;      
   }
   
}
