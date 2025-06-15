package kr.co.nanwe.external.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name 		: ExternalDao
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface ExternalDao {
	
	Map<String, Object> searchOne(String sqlId, Map<String, Object> param);
	
	List<Map<String, Object>> searchList(String sqlId, Map<String, Object> param);
}