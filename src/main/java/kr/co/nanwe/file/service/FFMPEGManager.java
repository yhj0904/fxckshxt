package kr.co.nanwe.file.service;

/**
 * @Class Name 		: ConvertToMp4
 * @Description 	: 동영상 MP4 변환
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cmmn.util.WebUtil;

@Component("FFMPEGManager")
public class FFMPEGManager {
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FFMPEGManager.class);
	
	private static final String WINDOW_FFMPEG_PATH = "/ffmpeg/bin/ffmpeg.exe";
	
	private static final String LINUX_FFMPEG_PATH = "/ffmpeg/bin/ffmpeg.exe";

	public FileVO convertToMp4(FileVO fileVO) {
		
		if(fileVO == null) {
			return null;
		}
		
		String filePath = fileVO.getFilePath();
		String fileName = fileVO.getFileName();
		String orgName = fileVO.getOrgName();
		
		if(StringUtil.isNull(filePath) || StringUtil.isNull(fileName)) {
			return null;
		}
		int fIndex = fileName.lastIndexOf(".");
		if(fIndex == -1) {
			return null;
		}
		String fExt = fileName.substring(fIndex + 1);
		if("mp4".equals(fExt.toLowerCase())) {
			return null;
		}
		FileVO convertFileVO = null;
		String originalPath = WebUtil.filePathBlackList(filePath + FileUtil.SEPERATOR + fileName);
		if(FileUtil.isFileExist(originalPath)) {			
			Process p = null;
			try {
				
				String rootPath = web.getRootPath();
				String ffmpegPath = rootPath;
				String serverOs = web.getServerProp("server.os");
				
				if("WINDOW".equals(serverOs.toUpperCase())) {
					ffmpegPath += WINDOW_FFMPEG_PATH;
				} else if("LINUX".equals(serverOs.toUpperCase())) {
					ffmpegPath += LINUX_FFMPEG_PATH;
				}
				
				File fOriginal = new File(originalPath);
				String outputName = fileName.substring(0, fIndex) + ".mp4";
				LOGGER.debug(outputName);
				String fPath = WebUtil.filePathBlackList(filePath + System.getProperty("file.separator")+ outputName);
				File fResult = new File(fPath);

				// cmd 창에 날릴 명령어 만들기~
				String[] cmdLine = new String[] { ffmpegPath, "-i", fOriginal.getPath(), "-ar", "11025", "-f", "mp4", fResult.getPath() };

				// 프로세스 속성을 관리하는 ProcessBuilder 생성.
				ProcessBuilder pb = new ProcessBuilder(cmdLine);
				pb.redirectErrorStream(true);
				
				// 프로세스 작업을 실행함.
				p = pb.start();
				
				// 자식 프로세스에서 발생되는 인풋스트림 소비시킴
				exhaustInputStream(p.getInputStream());
				
				// p의 자식 프로세스의 작업이 완료될 동안 p를 대기시킴
				p.waitFor();
				
				if (p.exitValue() != 0) {
					return null;
				}
				
				// 변환을 하는 중 에러가 발생하여 파일의 크기가 0일 경우
				if (fResult.length() == 0) {
					return null;
				}
				
				fOriginal.delete();
				
				
				orgName = orgName.substring(0, orgName.lastIndexOf(".")) + ".mp4";
				
				convertFileVO = new FileVO();
				convertFileVO.setFileName(outputName);
				convertFileVO.setFileExt("mp4");
				convertFileVO.setOrgName(orgName);
				convertFileVO.setFileMime("video/mp4");
				convertFileVO.setFileSize((int) fResult.length());
				
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			} finally {
				if(p != null) {
					p.destroy();
				}
			}
		}		
		return convertFileVO;
	}

	private void exhaustInputStream(final InputStream is) {
		// InputStream.read() 에서 블럭상태에 빠지기 때문에 따로 쓰레드를 구현하여 스트림을
		// 소비한다.
		new Thread() {
			public void run() {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String cmd;
					while ((cmd = br.readLine()) != null) { // 읽어들일 라인이 없을때까지 계속 반복
						LOGGER.debug(cmd);
					}
				} catch (IOException e) {
					LOGGER.debug(e.getMessage());
				}
			}
		}.start();
	}

//	public String getThumImage(String uploadPath, String fileChangeName) {
//		
//		String rootPath = web.getRootPath();
//		String ffmpegPath = rootPath;
//		String serverOs = web.getServerProp("server.os");
//		
//		if("WINDOW".equals(serverOs.toUpperCase())) {
//			ffmpegPath += WINDOW_FFMPEG_PATH;
//		} else if("LINUX".equals(serverOs.toUpperCase())) {
//			ffmpegPath += LINUX_FFMPEG_PATH;
//		}
//		
//		int index = fileChangeName.toLowerCase().lastIndexOf(".");
//		String fileName = fileChangeName.substring(0, index);
//		Runtime run = Runtime.getRuntime();
//		String command = ffmpegPath+" -i " + uploadPath + "/" + fileChangeName + " -vcodec png -vframes 1 -vf thumbnail=100 " + uploadPath + "/" + fileName + "_thumb.png";
//		try {
//			run.exec("cmd.exe chcp 65001");
//			run.exec(command);
//		} catch (Exception e) {
//			LOGGER.debug(e.getMessage());
//		}
//		return fileName + "_thumb.png";
//	}
}
