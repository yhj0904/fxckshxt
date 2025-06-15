package kr.co.nanwe.stat.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.stat.service.StatService;

/**
 * @Class Name 		: StatServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("statService")
public class StatServiceImpl extends EgovAbstractServiceImpl implements StatService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(StatServiceImpl.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;

	/** Mapper */
	@Resource(name = "statMapper")
	private StatMapper statMapper;

	@Override
	public Map<String, Object> selectSysMainStat() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//오늘 로그인 수
		int todayLoginCount = statMapper.selectTodayLoginCount();
		map.put("todayLoginCount", todayLoginCount);
		
		//이번주 로그인 수
		List<Map<String, Object>> weeklyLoginCount = statMapper.selectWeeklyLoginCount();
		map.put("weeklyLoginCount", weeklyLoginCount);
		
		//번 월 로그인 수
		List<Map<String, Object>> monthlyLoginCount = statMapper.selectMonthlyLoginCount();
		map.put("monthlyLoginCount", monthlyLoginCount);
				
		//오늘 등록된 게시글 수
		int todayBbsCount = statMapper.selectTodayBbsCount();
		map.put("todayBbsCount", todayBbsCount);
				
		//총 게시글 수
		int totalBbsCount = statMapper.selectTotalBbsCount();
		map.put("totalBbsCount", totalBbsCount);
		
		return map;
	}
	
}
