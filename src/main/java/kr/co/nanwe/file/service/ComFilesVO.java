package kr.co.nanwe.file.service;

import lombok.Data;

/**
 * @Class Name 		: ComFilesVO
 * @Description 	: ComFilesVO
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public @Data class ComFilesVO {
	
	/** SEQ */
	private int fileNo;
	
	/** 부모 코드 */
	private String parentCd;
	
	/** 부모 ID */
	private String parentUid;
	
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
	
	/** 아이디 */
	private String inptId;
	
	/** 아이피 */
	private String inptIp;
	
	/** 등록시간 */
	private String inptDttm;
	
	/** 부가정보1 */
	private String supplInfo1;
	
	/** 부가정보2 */
	private String supplInfo2;
	
	/** 부가정보3 */
	private String supplInfo3;
	
	/** 부가정보4 */
	private String supplInfo4;
	
	/** 부가정보5 */
	private String supplInfo5;
}
