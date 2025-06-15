package kr.co.nanwe.log.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import kr.co.nanwe.cmmn.bean.ProgramControllerScan;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.ExcelDownload;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.log.service.DbLogVO;
import kr.co.nanwe.log.service.SysLogService;
import kr.co.nanwe.log.service.SysLogVO;

/**
 * @Class Name 		: SysLogController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/log")
@Program(code="SYS_LOG", name="시스템로그")
@Controller
public class SysLogController {	

	//private static final Logger LOGGER = LoggerFactory.getLogger(SysLogController.class);
	
	/** View Path */
	private static final String VIEW_PATH = "sys/sysLog";
	
	/** Redirect Path */
	private String REDIRECT_PATH = "";
	
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
	@Resource(name = "sysLogService")
	private SysLogService sysLogService;
	
	/** Constructor */
	public SysLogController() {		
		RequestMapping requestMapping = SysLogController.class.getAnnotation(RequestMapping.class);
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
	 * 시스템로그 목록조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/list.do")
	@ProgramInfo(code="LIST", name="목록조회")
	public String list(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						, @RequestParam(value = "sProgram", defaultValue="ALL") String sProgram){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sProgram", sProgram);
		
		//목록조회
		Map<String, Object> map = sysLogService.selectSysLogList( search, sProgram);
		
		//프로그램 목록 조회
		List<Map<String, Object>> programList = ProgramControllerScan.getProgramList();		
		
		//조회결과 MODEL ADD
		model.addAllAttributes(map);
		model.addAttribute("programList", programList);
		
		return web.returnView(VIEW_PATH, "/list");
	}
	
	/**
	 * 시스템로그 상세조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/view.do")
	@ProgramInfo(code="VIEW", name="상세조회")
	public String view(Model model, HttpServletRequest request, @ModelAttribute SearchVO search
						, @RequestParam(value = "sId", defaultValue="") String sysCode
						, @RequestParam(value = "sProgram", defaultValue="ALL") String sProgram){
		
		//검색조건 MODEL ADD
		model.addAttribute("search", search);
		model.addAttribute("sProgram", sProgram);
		
		//상세조회
		SysLogVO sysLogVO = sysLogService.selectSysLog(sysCode);
		
		//조회결과가 없는 경우 RESULT VIEW 이동
		if(sysLogVO == null) {	
			
			//리턴페이지 (생략시 메인페이지 리턴)
			model.addAttribute("redirectUrl", REDIRECT_PATH + "/list.do");
			
			return web.returnError();
		}
		
		//DB로그목록 조회
		List<DbLogVO> dbLogList = sysLogService.selectDbLogListBySysCode(sysCode);
		
		//조회결과 MODEL ADD
		model.addAttribute("sysLogVO", sysLogVO);
		model.addAttribute("dbLogList", dbLogList);
		
		return web.returnView(VIEW_PATH, "/view");
	}
	
	/**
	 * 샘플화면 엑셀다운
	 * @param 
	 * @return
	 * @exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/excelDown.do")
	@ProgramInfo(code = "EXCEL_DOWN", name = "샘플 엑셀다운")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SearchVO search
						, @RequestParam(value = "sProgram", defaultValue="ALL") String sProgram) throws IOException {

		//엑셀 파일명
		String excelName = "샘플 엑셀";
				
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles = {"SYS_CODE", "PROGRAM_CD", "PROGRAM_NM", "INFO_CD", "INFO_NM", "PROGRAM_URI", "CLASS_NAME", "METHOD_NAME", "METHOD_DESC", "PROCESS_CODE", "PROCESS_TIME", "LOG_PARAM", "PRIVACY_YN", "LOG_ID", "LOG_NAME", "LOG_IP", "LOG_OS", "LOG_DEVICE", "LOG_BROWSER", "LOG_URL", "LOG_DTTM", "ERR_YN"};
		//엑셀 컬럼 코드 정보 (해당 키 값으로 Result Data 에서 값을 뽑아온다.)
        String[] columnCodes = {"SYS_CODE", "PROGRAM_CD", "PROGRAM_NM", "INFO_CD", "INFO_NM", "PROGRAM_URI", "CLASS_NAME", "METHOD_NAME", "METHOD_DESC", "PROCESS_CODE", "PROCESS_TIME", "LOG_PARAM", "PRIVACY_YN", "LOG_ID", "LOG_NAME", "LOG_IP", "LOG_OS", "LOG_DEVICE", "LOG_BROWSER", "LOG_URL", "LOG_DTTM", "ERR_YN"};
        
		
        //WORK BOOK 생성 (파라미터 : 배치 행수)
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);
		//엑셀 다운로드 핸들러 ( Workbook, 엑셀명, 컬럼정보, 컬럼코드, 순번표시여부)
		ExcelDownloadHandler<HashMap<String, Object>> handler = new ExcelDownloadHandler<>(wb, excelName, columnTitles, columnCodes, true);
		
		//데이터 조회
		sysLogService.selectSysLogExcelList(handler, search, sProgram);
		
		//엑셀 다운로드
		ExcelDownload.excelDownload(request, response, excelName, wb);
		
	}

}
