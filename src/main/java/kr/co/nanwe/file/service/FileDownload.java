package kr.co.nanwe.file.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.WebUtil;

/**
 * @Class Name 		: FileDownload
 * @Description 	: 첨부파일 다운로드 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component
public class FileDownload extends AbstractView {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileDownload.class);
	
	public static final String SEPERATOR = File.separator;
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> fileMap = (Map<String, Object>)model.get("fileMap");
		
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("<script>");
		sbuffer.append("alert('" + messageUtil.getMessage("message.error.file.notexist") + "');");
		sbuffer.append("history.back();");
		sbuffer.append("</script>");
				
		if(fileMap == null) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(sbuffer.toString());
			printWriter.flush();
			return;
		}
		
		String rootPath = serverProp.getProperty("server.file.path");
		
		String filePath = (String) fileMap.get("filePath");
		String fileName = (String) fileMap.get("fileName");
		String orgName = (String) fileMap.get("orgName");
		
		String path = filePath + SEPERATOR + fileName;
		path = WebUtil.filePathBlackList(path);
		
		//Root경로와 다른 경우 return
		if(!path.contains(rootPath)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(sbuffer.toString());
			printWriter.flush();
			return;
		}
		
		File file = new File(path);
		
		//파일이 없는 경우
		if (!file.exists()) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(sbuffer.toString());
			printWriter.flush();
			return;
		}

		//파일이 아닌 경우
		if (!file.isFile()) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(sbuffer.toString());
			printWriter.flush();
			return;
		}
		
		//파일 로그 등록
		int fileNo = 0;
		if (fileMap.get("fileNo") != null) {
			fileNo = (int) fileMap.get("fileNo");			
			if(fileNo > 0) {
				comFileManager.downloadFileLog(fileNo, orgName, filePath, fileName);
			}
		}
		
		String mimetype = "application/x-msdownload";
		
		HashMap<String,String> result = ClientUtil.getUserBrowser();
		if ( !ClientUtil.MSIE.equals(result.get(ClientUtil.TYPEKEY)) ) {
			mimetype = "application/x-stuff";
		}
		
		String contentDisposition = ClientUtil.getDisposition(orgName, "UTF-8");
		response.setContentType(mimetype);
		response.setHeader("Content-Disposition", contentDisposition);
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(response.getOutputStream());

			FileCopyUtils.copy(in, out);
			out.flush();
			
			in.close();
			out.close();
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		} finally {
			in.close();
			out.close();
		}
	}
}
