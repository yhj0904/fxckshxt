package kr.co.nanwe.log.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.log.service.DbLogVO;
import kr.co.nanwe.log.service.SysLogService;
import kr.co.nanwe.log.service.SysLogVO;

/**
 * @Class Name 		: SysLogServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("sysLogService")
public class SysLogServiceImpl extends EgovAbstractServiceImpl implements SysLogService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SysLogServiceImpl.class);
	
	/** Config */
	@Resource(name = "web")
	private WebConfig web;

    @Resource(name="sysLogMapper")
    private SysLogMapper sysLogMapper;

	@Override
	public void insertSysLog(SysLogVO sysLog) {
		sysLogMapper.insertSysLog(sysLog);
	}

	@Override
	public void insertDbLog(DbLogVO dbLog) {
		sysLogMapper.insertDbLog(dbLog);
	}

	@Override
	public Map<String, Object> selectSysLogList(SearchVO search, String sProgram) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("sProgram", sProgram);
		
		//전체 ROW COUNT
		int totCnt = sysLogMapper.selectSysLogTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<SysLogVO> list = sysLogMapper.selectSysLogList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	public SysLogVO selectSysLog(String sysCode) {
		if(sysCode == null) {
			return null;
		}
		return sysLogMapper.selectSysLog(sysCode);
	}

	@Override
	public List<DbLogVO> selectDbLogListBySysCode(String sysCode) {
		return sysLogMapper.selectDbLogListBySysCode(sysCode);
	}

	@Override
	public void selectSysLogExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search, String sProgram) {
		// 검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("sProgram", sProgram);
		
		sysLogMapper.selectSysLogExcelList(paramMap, handler);		
	}

}
