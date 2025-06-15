package kr.co.nanwe.rest.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Class Name 		: RestService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface RestService {
	String checkApiKey(HttpServletRequest request); 
}
