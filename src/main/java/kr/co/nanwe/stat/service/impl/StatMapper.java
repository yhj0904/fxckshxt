package kr.co.nanwe.stat.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name 		: StatMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("statMapper")
public interface StatMapper {
	
	/** 오늘 접속한 회원 수 */
	int selectTodayLoginCount();
	
	/** 한주의 접속한 회원수 */
	List<Map<String, Object>> selectWeeklyLoginCount();
	
	/** 한주의 접속한 회원수 */
	List<Map<String, Object>> selectMonthlyLoginCount();

	/** 오늘 등록된 게시글 수 */
	int selectTodayBbsCount();
	
	/** 총 등록된 게시글 수 */
	int selectTotalBbsCount();
}
