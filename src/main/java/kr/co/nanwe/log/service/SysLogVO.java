package kr.co.nanwe.log.service;

import java.io.Serializable;

import lombok.Data;

/**
 * @Class Name 		: SysLogVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SysLogVO implements Serializable{


	private static final long serialVersionUID = 540569951549295059L;
	
	/** 로그코드 */
	private String sysCode;
	/** 프로그램 코드 */
	private String programCd;
	/** 프로그램명 */
	private String programNm;
	/** 프로그램 정보 코드 */
	private String infoCd;
	/** 프로그램 정보명 */
	private String infoNm;
	/** 프로그램 URI */
	private String programUri;
	/** 클래스명 */
	private String className;
	/** 메소드명 */
	private String methodName;
	/** 메소드설명 */
	private String methodDesc;
	/** 프로세스 코드 */
	private String processCode;
	/** 프로세스 시간 */
	private String processTime;
	/** 로그 ID */
	private String logId;
	/** 이름 */
	private String logName;
	/** 아이피 */
	private String logIp;
	/** OS */
	private String logOs;
	/** DEVICE */
	private String logDevice;
	/** BROWSER */
	private String logBrowser;
	/** URL */
	private String logUrl;
	/** 로그 시간 */
	private String logDttm;
	/** 로그 파라미터 */
	private String logParam;
	/** 에러여부 */
	private String errYn;
	/** 에러메시지 */
	private String errMsg;
	/** 개인정보여부 */
	private String privacyYn;
	
}