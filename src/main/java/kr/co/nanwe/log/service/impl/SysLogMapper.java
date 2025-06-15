package kr.co.nanwe.log.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.log.service.DbLogVO;
import kr.co.nanwe.log.service.SysLogVO;

/**
 * @Class Name 		: SysLogMapper.java
 * @Description 	: SysLogMapper Class
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("sysLogMapper")
public interface SysLogMapper {

	/** 시스템 로그 등록 */
	void insertSysLog(SysLogVO sysLog);
	
	/** DB 로그 등록 */
	void insertDbLog(DbLogVO dbLog);
	
	/** 시스템 로그 카운트 */
	int selectSysLogTotCnt(Map<String, Object> paramMap);
	
	/** 시스템 로그 목록 */
	List<SysLogVO> selectSysLogList(Map<String, Object> paramMap);
	
	/** 시스템 로그 상세조회 */
	SysLogVO selectSysLog(String sysCode);
	
	/** DB로그목록 조회 */
	List<DbLogVO> selectDbLogListBySysCode(String sysCode);
	
	/** 엑셀 목록 조회 (*필수 return void) */
	void selectSysLogExcelList(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);
	
}
