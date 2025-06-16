package kr.co.nanwe.log.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.login.service.LoginLogVO;
import kr.co.nanwe.login.service.LoginService;

/**
 * @Class Name 		: LoginLogController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Program(code="LOGIN_LOG", name="로그인 로그")
@Controller
public class SysLoginLogController {	

	//private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/loginLog";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "/sys/log/login";
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "loginService")
	private LoginService loginService;
	
	/** Constructor */
	public SysLoginLogController() {		
		RequestMapping requestMapping = SysLoginLogController.class.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			this.REDIRECT_PATH = requestMapping.value()[0];
		}
	}
	
	/**
	 * 로그인로그 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = {"/sys/log/login.do", "/sys/log/login/list.do"})
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//목록조회
		Map<String, Object> map = loginService.selectLoginLogList(search);
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 로그인로그 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/sys/log/login/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						, @RequestParam(value = "sId", defaultValue="") String loginCode){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		
		//상세조회
		LoginLogVO loginLogVO = loginService.selectLoginLog(loginCode);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(loginLogVO == null) {	
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//조회결과 MODEL ADD
		model.addAttribute("loginLogVO", loginLogVO);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 로그인 로그 엑셀 다운
	 * @param 
	 * @return
	 * @exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/sys/log/login/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search) throws IOException {
		
		//엑셀 파일명
		String excelName = "login_log";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles =  {"LOGIN_CODE", "LOGIN_TYPE", "LOGIN_ID", "LOGIN_NM", "LOGIN_IP", "LOGIN_OS", "LOGIN_DEVICE", "LOGIN_BROWSER", "LOGIN_DTTM", "LOGIN_SUCCESS", "LOGIN_ERR_MSG"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes =  {"LOGIN_CODE", "LOGIN_TYPE", "LOGIN_ID", "LOGIN_NM", "LOGIN_IP", "LOGIN_OS", "LOGIN_DEVICE", "LOGIN_BROWSER", "LOGIN_DTTM", "LOGIN_SUCCESS", "LOGIN_ERR_MSG"};
       
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, true);
		
		//데이터 조회
		loginService.selectLoginLogExcelList(handler, search);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
	}
	
}
