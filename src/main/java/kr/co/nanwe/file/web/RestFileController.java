package kr.co.nanwe.file.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.CompressUtil;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.file.service.CkEditorUpload;

/**
 * @Class Name 		: RestFileController
 * @Description 	: RestFileController
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/sys/system")
@RestController
public class RestFileController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestSystemController.class);
			
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** 웹에디터 */
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;
	
	/**
	 * 임시 에디터 파일 삭제
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/removeEditorFiles.json")
	public int removeEditorFiles(HttpServletRequest request){
		return ckEditorUpload.deleteTempEditorFiles();
	}
	
	/**
	 * 웹파일 백업
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/backWebFiles.json")
	public boolean backWebFiles(HttpServletRequest request) {		
		//첨부파일 경로
		String path = web.getServerProp("server.path");		
		//백업 경로
		String outputPath =  web.getServerProp("server.backup.web.path");		
		//압축 파일 명
		String outputFileName = "BACK_" + DateUtil.getDate("yyyyMMddHHmmss");
		return CompressUtil.compress(path, outputPath, outputFileName);
	}
	
	/**
	 * 첨부파일 백업
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/backUploadFiles.json")
	public boolean backUploadFiles(HttpServletRequest request) {		
		//첨부파일 경로
		String path = web.getServerProp("server.file.path");		
		//백업 경로
		String outputPath = web.getServerProp("server.backup.file.path");		
		//압축 파일 명
		String outputFileName = "BACK_" + DateUtil.getDate("yyyyMMddHHmmss");
		return CompressUtil.compress(path, outputPath, outputFileName);
	}
	
	/**
	 * 에디터파일 백업
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/backEditorFiles.json")
	public boolean backEditorFiles(HttpServletRequest request) {		
		//첨부파일 경로
		String path = web.getServerProp("server.editor.file.path");		
		//백업 경로
		String outputPath = web.getServerProp("server.backup.editor.path");		
		//압축 파일 명
		String outputFileName = "BACK_" + DateUtil.getDate("yyyyMMddHHmmss");
		return CompressUtil.compress(path, outputPath, outputFileName);
	}
	
}
