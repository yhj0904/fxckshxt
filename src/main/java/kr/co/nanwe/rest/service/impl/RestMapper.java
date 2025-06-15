package kr.co.nanwe.rest.service.impl;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name 		: RestMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("restMapper")
public interface RestMapper {
	
	int checkApiKey(@Param("apiKey") String apiKey);
	
	int checkApiDomain(@Param("apiKey") String apiKey, @Param("domain") String domain);
}
