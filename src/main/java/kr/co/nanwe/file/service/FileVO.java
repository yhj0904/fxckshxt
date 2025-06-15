package kr.co.nanwe.file.service;

import java.io.File;

import lombok.Data;

/**
 * @Class Name 		: FileVO
 * @Description 	: FileVO
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public @Data class FileVO {
	
	/** 파일 업로드 결과 */
	private boolean result;
	
	/** 원래이름 */
	private String orgName;
	
	/** 변경된 이름 */
	private String fileName;
	
	/** 업로드 경로 */
	private String filePath;
	
	/** 확장자 */
	private String fileExt;
	
	/** 파일 유형 */
	private String fileType;
	
	/** 파일 유형 */
	private String fileMime;
	
	/** 파일 사이즈 */
	private int fileSize;
	
	/** 파일 */
	private File file;
	
}
