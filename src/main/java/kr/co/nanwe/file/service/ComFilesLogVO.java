package kr.co.nanwe.file.service;

import lombok.Data;

/**
 * @Class Name 		: ComFilesLogVO
 * @Description 	: ComFilesLogVO
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public @Data class ComFilesLogVO {
	
	/** 부모 코드 */
	private String parentCode;
	/** 부모 ID */
	private String parentUid;
	/** SEQ */
	private int fileNo;
	/** 파일명 */
	private String orgName;
	/** 메모 */
	private String memo;
	/** 아이디 */
	private String inptId;
	/** 아이피 */
	private String inptIp;
	/** 등록시간 */
	private String inptDttm;
}
