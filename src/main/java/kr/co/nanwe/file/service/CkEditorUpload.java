package kr.co.nanwe.file.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.cmmn.util.CryptoUtil;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cmmn.util.WebUtil;
import kr.co.nanwe.file.service.impl.ComEditorFilesMapper;

/**
 * @Class Name 		: CkEditorUpload
 * @Description 	: 웹에디터 (CKEDITOR) 업로드 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("ckEditorUpload")
public class CkEditorUpload  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CkEditorUpload.class);
	
	private static final String BBS_CODE = "COM_BBS";
	
	private static final String SCH_CODE = "COM_SCH";
	
	private static final String PROG_CODE = "PROG_MST";
	
	public static final String SEPERATOR = File.separator;
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;
	
	/** Mapper */
	@Resource(name = "comEditorFilesMapper")
	private ComEditorFilesMapper comEditorFilesMapper;
	
	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;
	
	/**
	 * 파일업로드 후 FileVO 객체를 반환한다
	 *
	 * @param MultipartFile uploadFile : 
	 * @param String uploadPath : 업로드 경로
	 * @param int limitSize : 제한 사이즈 (바이트)
	 * @return FileVO
	 */
	public FileVO uploadCkImage(MultipartFile uploadFile) {
		
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
			//파일 유형이 이미지가 아닌 경우
			if (!FileUtil.getFileType(uploadFile).equals("IMAGE")) {
				return fileVO;
			}
			
			//에디터 파일 업로드 경로
			String year = DateUtil.getDate("yyyy"); //년도
			String date = DateUtil.getDate("yyyyMMdd"); //오늘날짜
			String uploadRootPath = serverProp.getProperty("server.editor.file.path");
			String uploadPath = WebUtil.filePathBlackList(uploadRootPath + SEPERATOR + year + SEPERATOR + date);
			File folder = new File(uploadPath);
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
			if(fileSize > FileUtil.MAX_FILE_SIZE) {
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
	
	public int insertComEditorFiles(FileVO fileVO) {
		int fileNo = 0;
		ComEditorFilesVO comEditorFileVO = new ComEditorFilesVO();
		comEditorFileVO.setOrgName(fileVO.getOrgName());
		comEditorFileVO.setFileName(fileVO.getFileName());
		comEditorFileVO.setFilePath(fileVO.getFilePath());
		comEditorFileVO.setFileExt(fileVO.getFileExt());
		comEditorFileVO.setFileType(fileVO.getFileType());
		comEditorFileVO.setFileMime(fileVO.getFileMime());
		comEditorFileVO.setFileSize(fileVO.getFileSize());
		int result = comEditorFilesMapper.insertComEditorFiles(comEditorFileVO);
		if (result > 0) {
			fileNo = comEditorFileVO.getFileNo();
		}
		return fileNo;
	}	
	
	public int updateComEditorFiles(String code, Object id) {
		int result = 0;
		String uid = (String) id;
		String[] strArr = RequestUtil.getCurrentRequest().getParameterValues("_EDITOR_FILE_NO_");
		if(strArr != null) {
			for (String str : strArr) {
				int fileNo = Integer.parseInt(str);
				if (fileNo > 0) {
					comEditorFilesMapper.updateComEditorFiles(code, uid, fileNo);
				}
			}
		}
		return result;
	}
	
	public void deleteComEditorFiles(String code, Object id) {
		String uid = (String) id;
		List<ComEditorFilesVO> fileList = comEditorFilesMapper.selectComEditorFilesByParent(code, uid);		
		if(fileList != null) {			
			for (ComEditorFilesVO comEditorFilesVO : fileList) {
				int deleteResult = comEditorFilesMapper.deleteComEditorFiles(comEditorFilesVO.getFileNo());
				if(deleteResult > 0) {
					
					String filePath = WebUtil.filePathBlackList(comEditorFilesVO.getFilePath() + SEPERATOR + comEditorFilesVO.getFileName());
					
					//파일 존재 유무 확인 후 파일이 있는 경우
					if(FileUtil.isFileExist(filePath)) {
						//파일 삭제
						File destFile = new File(filePath);
						destFile.delete();
					}
				}
			}
		}
	}
	
	public synchronized int deleteTempEditorFiles() {
		int count = 0;
		List<ComEditorFilesVO> fileList = comEditorFilesMapper.selectComEditorFilesByNoParent();		
		if(fileList != null) {			
			for (ComEditorFilesVO comEditorFilesVO : fileList) {
				String filePath = WebUtil.filePathBlackList(comEditorFilesVO.getFilePath() + SEPERATOR + comEditorFilesVO.getFileName());					
				//파일 존재 유무 확인 후 파일이 있는 경우
				if(FileUtil.isFileExist(filePath)) {
					//파일 삭제
					File destFile = new File(filePath);
					destFile.delete();
					count++;
				}
			}
			comEditorFilesMapper.deleteComEditorFilesByNoParent();
		}
		return count;
	}
	
	/**
	 * 글 삭제시 해당 에디터 파일 삭제
	 *
	 * @param String content : 웹에디터 본문 내용
	 * @return int : 삭제 개수
	 */
	public int deleteCkImage(String content) {

		int count = 0;
		
		//Null 일경우 return;
		if (StringUtil.isNull(content)) {
			return count;
		}
		
		//에디터 파일 업로드 경로
		String uploadRootPath = serverProp.getProperty("server.editor.file.path");

		// 이미지 태그가 매칭 되는걸 찾는다.
		Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String src = matcher.group(1);
			if (!StringUtil.isNull(src)) {
				
				MultiValueMap<String, String> map = RequestUtil.getParamters(src);
				
				if(map != null) {
					try {
						
						String fpath = cryptoUtil.decrypt(StringUtil.isNullToString(map.get("fpath")));
						String fname = cryptoUtil.decrypt(StringUtil.isNullToString(map.get("fname")));
						String fmime = cryptoUtil.decrypt(StringUtil.isNullToString(map.get("fmime")));
						
						if (!StringUtil.isNull(fpath) && !StringUtil.isNull(fname) && !StringUtil.isNull(fmime)) {
							
							String filePath = WebUtil.filePathBlackList(fpath + SEPERATOR + fname);
							
							//만일 파일 경로가 에디터 경로가 아닌 경우 continue
							if(!filePath.contains(uploadRootPath)) {
								continue;
							}
							
							//파일 존재 유무 확인 후 파일이 없는 경우
							if(!FileUtil.isFileExist(filePath)) {
								continue;
							}
							
							//파일 삭제
							File destFile = new File(filePath);
							if(destFile.delete()) {
								count ++;
							}							
						}
						
					} catch (UnsupportedEncodingException | GeneralSecurityException e) {
						LOGGER.error(e.getMessage());
					}
					
				}
			}
		}
		return count;
	}
	
	public int updateComEditorFilesByBbs(String bbsType, String bbsCd, String bbsId) {
		
		String parentCd = getParentCdByBbsType(bbsType);
		if(parentCd == null) {
			return 0;
		}
		
		int result = 0;
		String[] strArr = RequestUtil.getCurrentRequest().getParameterValues("_EDITOR_FILE_NO_");
		if(strArr != null) {
			for (String str : strArr) {
				int fileNo = Integer.parseInt(str);
				if (fileNo > 0) {
					comEditorFilesMapper.updateComEditorFilesByBbs(parentCd, bbsId, fileNo, bbsCd);
				}
			}
		}
		return result;
	}
	
	private String getParentCdByBbsType(String bbsType) {
		if(bbsType == null) {
			return null;
		}
		bbsType = bbsType.toLowerCase();
		String parentCd = "";		
		if("bbs".equals(bbsType)) {
			parentCd = BBS_CODE;
		} else if("sch".equals(bbsType)) {
			parentCd = SCH_CODE;
		} else if("prog".equals(bbsType)) {
			parentCd = PROG_CODE;
		} else {
			parentCd = null;
		}
		return parentCd;
	}
}
