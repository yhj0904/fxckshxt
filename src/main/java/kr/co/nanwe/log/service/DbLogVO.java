package kr.co.nanwe.log.service;

import java.io.Serializable;

import lombok.Data;

/**
 * @Class Name 		: DbLogVO
 * @Description 	: DB 로그 VO
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class DbLogVO implements Serializable{

	private static final long serialVersionUID = -8274004534207618049L;
	
	/** 로그코드 */
	private String sysCode;
	/** SQL 아이디 */
	private String sqlId;
	/** 메소드명 */
	private String sqlMethod;
	/** 쿼리 */
	private String logSql;
	/** 로그 ID */
	private String logId;
	/** 이름 */
	private String logName;
	/** 아이피 */
	private String logIp;
	/** 로그시간 */
	private String logDttm;
	/** 순번 */
	private int logOrder;
	
}
