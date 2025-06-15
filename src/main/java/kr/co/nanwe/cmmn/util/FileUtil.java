package kr.co.nanwe.cmmn.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Class Name 		: FileUtil
 * @Description 	: 파일 관련 유틸 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class FileUtil {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	/** 최대 첨부파일 크기 */
	public static final int MAX_FILE_SIZE = 1000 * 1024 * 1024;
	
	/** 확장자 거부 리스트 */
	public static final List<String> DENY_EXT = new ArrayList<String>(Arrays.asList("jsp", "java", "js", "xml", "php", "sql", "htm", "asp", "txt", "php3", "cgi", "exe", "inc", "htaccess", "html"));
	
	/** 오디오 파일 확장자 리스트 */
	public static final List<String> AUDIO_EXT = new ArrayList<String>(Arrays.asList("mp3", "ogg", "wma", "wav", "flac", "mid", "ac3", "aac", "ra"));

	/** 동영상 파일 확장자 리스트 */
	public static final List<String> VIDEO_EXT = new ArrayList<String>(Arrays.asList("avi", "mpg", "mpeg", "asf", "asx", "wmv", "mov", "mp4", "swf", "flv", "mkv"));

	/** 이미지 파일 확장자 리스트 */
	public static final List<String> IMAGE_EXT = new ArrayList<String>(Arrays.asList("jpg", "jpeg", "gif", "png", "bmp", "ico"));

	/** 문서파일 확장자 리스트 */
	public static final List<String> DOC_EXT = new ArrayList<String>(Arrays.asList("doc", "docx", "dotx", "docm", "rtf", "hwp", "hwt", "pdf", "ppt", "pptx", "xls", "xlsx", "hwpx"));

	/** 압축파일 확장자 리스트 */
	public static final List<String> ZIP_EXT = new ArrayList<String>(Arrays.asList("egg", "alz", "zip", "rar", "7z", "cab", "tar", "tgz", "ace"));
	
	public static final String SEPERATOR = File.separator;
	
	
	/**
	 * 파일유효성 검사
	 *
	 * @param MultipartFile
	 * @return boolean
	 */
	public static boolean isFile(MultipartFile file) {
		
		//Null 인경우 false
		if(file == null) {
			return false;
		}
		
		//파일 사이즈 체크
		if(getFileSize(file) <= 0) {
			return false;
		}
		
		//파일명 체크
		if(StringUtil.isNull(getFileName(file))) {
			return false;
		}	
		
		return true;
	}
	
	/**
	 * 파일이 존재하는 지 체크
	 *
	 * @param String path
	 * @return boolean
	 */
	public static boolean isFileExist(String path) {
		
		//Null 인경우 false
		if(StringUtil.isNull(path)) {
			return false;
		}
		
		File file = new File(WebUtil.filePathBlackList(path));
		boolean isExist = file.exists();
		
		if(isExist) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 파일명 리턴
	 *
	 * @param MultipartFile
	 * @return String
	 */
	public static String getFileName(MultipartFile file) {
		String fileName = "";
		//Null 인경우 공백 리턴
		if(file == null) {
			return "";
		}
		fileName = file.getOriginalFilename();
		if(!StringUtil.isNull(fileName)) {
			int index = fileName.lastIndexOf(".");
			if(index != -1) {
				String name = fileName.substring(0, index);
				String extension = fileName.substring(index + 1);
				fileName = name + "." + extension;
			}
		}
		return fileName; 
	}
	
	/**
	 * 파일명 리턴 (확장자 제외 파일명 리턴)
	 *
	 * @param MultipartFile
	 * @return String
	 */
	public static String getFileNameNotExt(MultipartFile file) {
		String fileName = "";
		//Null 인경우 공백 리턴
		if(file == null) {
			return "";
		}
		fileName = file.getOriginalFilename();
		if(!StringUtil.isNull(fileName)) {
			int index = fileName.lastIndexOf(".");
			if(index != -1) {
				fileName = fileName.substring(0, index);
			}
		}
		return fileName; 
	}
	
	/**
	 * 파일 확장자 리턴
	 *
	 * @param MultipartFile
	 * @return String
	 */
	public static String getFileExt(MultipartFile file) {
		String fileExt = "";
		//Null 인경우 공백 리턴
		if(file == null) {
			return "";
		}
		String fileName = file.getOriginalFilename();
		if(!StringUtil.isNull(fileName)) {
			int index = fileName.lastIndexOf(".");
			if(index != -1) {
				fileExt = fileName.substring(index + 1);
			}
		}
		
		return fileExt; 
	}
	
	/**
	 * 파일 유형 리턴
	 *
	 * @param MultipartFile
	 * @return String
	 */
	public static String getFileType(MultipartFile file) {
		
		String extension = getFileExt(file);
		
		//확장자가 공백 인경우
		if(StringUtil.isNull(extension)) {
			return "";
		}
		
		//거부된 확장자 인경우 공백 리턴
		if (DENY_EXT.contains(extension)) {
			return "";
		}

		String fileType = "";
		if (AUDIO_EXT.contains(extension)) {
			fileType =  "AUDIO";
		} else if (VIDEO_EXT.contains(extension)) {
			fileType =  "VIDEO";
		} else if (IMAGE_EXT.contains(extension)) {
			fileType =  "IMAGE";
		} else if (DOC_EXT.contains(extension)) {
			fileType =  "DOC";
		} else if (ZIP_EXT.contains(extension)) {
			fileType =  "ZIP";
		}
		return fileType;
	}
	
	/**
	 * 파일 MIME 유형 리턴
	 *
	 * @param MultipartFile
	 * @return String
	 */
	public static String getFileMimeType(MultipartFile file) {
		if(!isFile(file)) {
			return "";
		}
		return file.getContentType();
	}
	
	/**
	 * 파일 사이즈 리턴
	 *
	 * @param MultipartFile
	 * @return int (단위 byte)
	 */
	public static int getFileSize(MultipartFile file) {
		//Null 인경우 0 으로 리턴
		if(file == null) {
			return 0;
		}
		return (int) file.getSize();
	}
	
	/**
	 * 파일 사이즈 단위 계산
	 *
	 * @param int (byte)
	 * @return String
	 */
	public static strictfp String getStrFileSize(int size) {

		String gubn[] = {"Byte", "KB", "MB", "GB"};
		int gubnKey = 0;
		double changeSize = (double) 0;
		long fileSize = 0;

		fileSize = (long) size;
		for (int x = 0; (fileSize / (long) 1024) > 0; x++, fileSize /= (long) 1024) {
			gubnKey = x;
			changeSize = (double) fileSize;
		}
		return changeSize + gubn[gubnKey];
	}
	
	/**
	 * 파일명 UUID 생성
	 *
	 * @param ext : 확장자
	 * @return String
	 */
	public static String getChangedName(String ext) {
		String changedName = "";
		String today = DateUtil.getDate("yyyyMMddHHmmss");
		String randomId = UUID.randomUUID().toString().replaceAll("-", "");
		changedName =  today + "_" + randomId;
		if(!StringUtil.isNull(ext)) {
			changedName += "." + ext;
		}	
		return changedName;
	}
	
	/**
	 * 폴더가 존재하는지 체크
	 *
	 * @param path : 경로
	 * @return String
	 */
	public static boolean isFolder(String path) {			
		//Null 인경우 false
		if(StringUtil.isNull(path)) {
			return false;
		}
		
		File file = new File(WebUtil.filePathBlackList(path));
		boolean isFolder = file.isDirectory();
		
		if(isFolder) {
			return true;
		} else {
			return false;
		}
	}
	
}