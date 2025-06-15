package kr.co.nanwe.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cmmn.util.WebUtil;

/**
 * @Class Name 		: FileUpload
 * @Description 	: 파일 업로드 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("fileUpload")
public class FileUpload  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUpload.class);
	
	private static final String SEPERATOR = File.separator;
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;	
	
	/**
	 * 서버의 파일업로드 절대경로를 가져온다
	 * @param 
	 * @return String
	 */
	public String getFileRootPath() {
		return serverProp.getProperty("server.file.path");
	}
	
	/**
	 * 파일업로드 후 FileVO 객체를 반환한다
	 *
	 * @param MultipartFile uploadFile : 
	 * @param String uploadPath : 업로드 경로
	 * @param int limitSize : 제한 사이즈 (바이트)
	 * @return FileVO
	 */
	public FileVO uploadFile(MultipartFile uploadFile, String uploadPath, int limitSize) {
		
		//결과
		boolean result = false;

		//return 객체 생성
		FileVO fileVO = new FileVO();
		fileVO.setResult(false);

		try {
			
			//파일이 아닌경우
			if(!FileUtil.isFile(uploadFile)) {
				return fileVO;
			}
			
			//업로드 경로가 없는 경우
			if(StringUtil.isNull(uploadPath)) {
				return fileVO;
			}
			
			//폴더가 없는 경우 생성
			File folder = new File(WebUtil.filePathBlackList(uploadPath));
			if(!folder.isDirectory()){
				folder.mkdirs();
			}
			 
			//파일 정보			
			String orgName = FileUtil.getFileName(uploadFile);
			String filePath = WebUtil.filePathBlackList(uploadPath);
			String fileExt = FileUtil.getFileExt(uploadFile);
			String changedName = FileUtil.getChangedName(fileExt);
			String fileType = FileUtil.getFileType(uploadFile);
			String fileMime = FileUtil.getFileMimeType(uploadFile);
			int fileSize = FileUtil.getFileSize(uploadFile);
			
			fileVO.setOrgName(orgName);
			fileVO.setFilePath(filePath);
			fileVO.setFileExt(fileExt);		
			fileVO.setFileType(fileType);
			fileVO.setFileMime(fileMime);
			fileVO.setFileName(changedName);
			fileVO.setFileSize(fileSize);
			
			//limit size 체크
			if(limitSize == 0) {
				limitSize = FileUtil.MAX_FILE_SIZE;
			} else {
				limitSize = limitSize * 1024;
			}
			if(fileSize > limitSize) {
				return fileVO;
			}
			
			//거부된 확장자인지 체크
			if(FileUtil.DENY_EXT.contains(fileExt)){
				return fileVO;
			}
			
			String path = WebUtil.filePathBlackList(filePath + SEPERATOR + changedName);
			File file = new File(path);
			uploadFile.transferTo(file);
			
			//업로드 결과를 확인한다.
			if(FileUtil.isFileExist(path)) {
				result = true;
				fileVO.setFile(file);
			}
			
			fileVO.setResult(result);
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		
		//결과반환		
		return fileVO;
	}
	
	/**
	 * 파일제거후 결과를 리턴
	 *
	 * @param FileVO
	 * @return boolean
	 */
	public boolean removeFile(FileVO fileVO) {
		
		//FileVO 객체가 Null 일경우 false
		if(fileVO == null) {
			return false;
		}
		
		String filePath = fileVO.getFilePath();
		String fileName = fileVO.getFileName();
		
		//경로 또는 이름이 Null 일경우
		if(StringUtil.isNull(filePath) || StringUtil.isNull(fileName)) {
			return false;
		}
		
		String path = WebUtil.filePathBlackList(filePath + SEPERATOR + fileName);
		
		//파일 존재 유무 확인 후 파일이 없는 경우
		if(!FileUtil.isFileExist(path)) {
			return true;
		}
		
		//파일 삭제
		File destFile = new File(path);
		
		return destFile.delete();
	}
	
	/**
	 * 폴더삭제
	 *
	 * @param String path
	 * @return boolean
	 */
	public boolean removeFolder(String path) {
		boolean result = true;
		File folder = new File(WebUtil.filePathBlackList(path));
		if(folder.exists()) {
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
		return result;
	}
	
	/**
	 * 폴더삭제
	 *
	 * @param String path
	 * @return boolean
	 */
	public boolean copyFile(String oldPath, String newPath) {
		boolean result = false;
		File oldFile = new File(WebUtil.filePathBlackList(oldPath));
		if (oldFile.exists()) {			
			FileInputStream input = null;
			FileOutputStream output = null;			
			try {
				File newFile = new File(WebUtil.filePathBlackList(newPath));
				input = new FileInputStream(oldFile);
				output = new FileOutputStream(newFile);

				int readBuffer = 0;
				byte[] buffer = new byte[512];
				while ((readBuffer = input.read(buffer)) != -1) {
					output.write(buffer, 0, readBuffer);
				}
				result = true;
			} catch (IOException e) {
				LOGGER.debug(e.getMessage());
				result = false;
			} finally {
				if(output != null) {
					try {
						output.close();
					} catch (IOException e) {
						LOGGER.debug(e.getMessage());
					}
				}
				if(input != null) {
					try {
						input.close();
					} catch (IOException e) {
						LOGGER.debug(e.getMessage());
					}
				}
			}
		}
		return result;
	}
}
