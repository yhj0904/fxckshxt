package kr.co.nanwe.content.service;

import lombok.Data;

/**
 * @Class Name 		: ContentVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class ContentVO {
	/** 아이디 */
	private String contId;
	/** 제목 */
	private String contNm;
	/** 내용 */
	private String contCont;
	/** 영문 제목 */
	private String contEngNm;
	/** 영문 내용 */
	private String contEngCont;
	/** 등록자 아이디 */
	private String inptId;
	/** 등록자 아이피 */
	private String inptIp;
	/** 등록시간 */
	private String inptDttm;
	/** 수정자 아이디 */
	private String modiId;
	/** 수정자 아이피 */
	private String modiIp;
	/** 수정시간 */
	private String modiDttm;
	
	/** SEQ */
	private int seq;	
	/** 백업시간 */
	private String backDttm;
}
