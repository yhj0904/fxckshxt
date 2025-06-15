package kr.co.nanwe.file.service;

import lombok.Data;

/**
 * @Class Name 		: ViewFileVO
 * @Description 	: ViewFileVO
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public @Data class ViewFileVO {

	/** SEQ */
	private int fno;
	
	/** 원래이름 */
	private String oname;
	
	/** 변경된 이름 */
	private String fname;
	
	/** 업로드 경로 */
	private String fpath;
	
	/** 확장자 */
	private String fext;
	
	/** 파일 유형 */
	private String ftype;
	
	/** 파일 유형 */
	private String fmime;
	
	/** 파일 사이즈 */
	private int fsize;
	
	/** 파일 사이즈 */
	private String size;
	
	/** 다운로드 URL */
	private String downloadUrl;
	
	/** 이미지인 경우 VIEW URL */
	private String viewUrl;

	/** 부가정보1 */
	private String supplInfo1;
}
