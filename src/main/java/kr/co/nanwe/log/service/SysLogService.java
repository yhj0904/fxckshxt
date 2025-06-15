package kr.co.nanwe.log.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;

/**
 * @Class Name 		: SysLogService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface SysLogService {

	/** 시스템 로그 등록 */
	void insertSysLog(SysLogVO sysLog);
	
	/** DB 로그 등록 */
	void insertDbLog(DbLogVO dbLog);
	
	/** 시스템 로그 목록 */
	Map<String, Object> selectSysLogList(SearchVO search, String sProgram);
	
	/** 시스템 로그 조회 */
	SysLogVO selectSysLog(String sysCode);
	
	/** DB로그목록 조회 */
	List<DbLogVO> selectDbLogListBySysCode(String sysCode);

	/** 시스템 로그 엑셀목록 조회 */
	void selectSysLogExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search, String sProgram);

}
