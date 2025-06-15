package kr.co.nanwe.stat.service;

import java.util.Map;

/**
 * @Class Name 		: StatService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface StatService {

	/** 포털관리자 메인 통계 */
	Map<String, Object> selectSysMainStat();	

}
