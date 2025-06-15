package kr.co.nanwe.file.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cmmn.util.WebUtil;

/**
 * 파일 유틸 클래스
 */

@Component("codeMirrorManager")
public class CodeMirrorManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeMirrorManager.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	public String getFileContent(String path, String name) {
		
		if(StringUtil.isNull(path) || StringUtil.isNull(name)) {
			return null;
		}
		
		String rootPath = web.getRootPath();
		String filePath = rootPath + WebUtil.clearXSSMinimum(path);
		String returnContent = "";

		String breakLine = System.getProperty("line.separator");

		FileReader fileReader = null;
		BufferedReader bufReader = null;
		try {
			File file = new File(filePath + FileUtil.SEPERATOR + name);
			if (!file.exists()) {
				return null;
			}
			fileReader = new FileReader(file);
			bufReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				returnContent += line;
				returnContent += breakLine;
			}
			bufReader.close();
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return returnContent;
	}
	
	public boolean makeFile(String path, String name, String content) {
		
		boolean result = false;
		
		if(StringUtil.isNull(path) || StringUtil.isNull(name)) {
			return false;
		}
		
		String rootPath = web.getRootPath();
		String filePath = rootPath + WebUtil.clearXSSMinimum(path);
		
		BufferedWriter bufferedWriter = null;
		OutputStreamWriter outputStreamWriter = null;
		FileOutputStream fileOutputStream = null;
		//new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath),"UTF8"));
		
		try {
			File folder = new File(filePath);
			if (!folder.exists()) {
				boolean check = folder.mkdir();
				if (check) {
					fileOutputStream = new FileOutputStream(filePath + FileUtil.SEPERATOR + name);
					outputStreamWriter =  new OutputStreamWriter(fileOutputStream,"UTF8");
					bufferedWriter = new BufferedWriter(outputStreamWriter);
					if(!StringUtil.isNull(content)) {
						bufferedWriter.write(content);
					}
					bufferedWriter.flush();
					result = true;
				} else {
					result = false;
				}
			} else {
				fileOutputStream = new FileOutputStream(filePath + FileUtil.SEPERATOR + name);
				outputStreamWriter =  new OutputStreamWriter(fileOutputStream,"UTF8");
				bufferedWriter = new BufferedWriter(outputStreamWriter);
				if(!StringUtil.isNull(content)) {
					bufferedWriter.write(content);
				}
				bufferedWriter.flush();
				result = true;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
			if (outputStreamWriter != null) {
				try {
					outputStreamWriter.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return result;
	}
	
	public void deleteFile(String path, String name) {
			
		String rootPath = web.getRootPath();
		String filePath = rootPath + WebUtil.clearXSSMinimum(path);

		File file = new File(filePath + FileUtil.SEPERATOR + name);
		if (file.exists()) {
			file.delete();
		}
	}

	public void deleteFolder(String path) {
		
		String rootPath = web.getRootPath();
		String folderPath = rootPath + WebUtil.clearXSSMinimum(path);
		
		File folder = new File(WebUtil.filePathBlackList(folderPath));
		
		if (folder.exists()) {
			File[] fileList = folder.listFiles();
			
			if(fileList != null) {
				for (int i = 0; i < fileList.length; i++) {
					fileList[i].delete();
				}
				
				if(fileList.length == 0 && folder.isDirectory()){
					folder.delete();
				}
			} else {
				if(folder.isDirectory()){
					folder.delete();
				}
			}
		}
	}
}
