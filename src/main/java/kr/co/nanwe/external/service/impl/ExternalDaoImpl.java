package kr.co.nanwe.external.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import kr.co.nanwe.external.service.ExternalDao;

/**
 * @Class Name 		: ExternalDaoImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Repository("externalDao")
public class ExternalDaoImpl extends EgovAbstractMapper implements ExternalDao {
	
	private final String NAMESPACE = "kr.co.nanwe.external.dao.ExternalDao.";
	@Override
	public Map<String, Object> searchOne(String sqlId, Map<String, Object> param) {
		return selectOne(NAMESPACE + sqlId, param);
	}
	@Override
	public List<Map<String, Object>> searchList(String sqlId, Map<String, Object> param) {
		return selectList(NAMESPACE + sqlId, param);
	}
	
}
