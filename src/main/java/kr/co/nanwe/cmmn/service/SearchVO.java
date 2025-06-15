package kr.co.nanwe.cmmn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @Class Name 		: SearchVO
 * @Description 	: 검색용 VO
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data 
public class SearchVO {
	
	/** 현재페이지 */
	private String cp;
	
	/** 검색조건 */
	private String so;
	
	/** 검색어 */
	private String sv;
	
	/** 시작일 */
	private String sd;
	
	/** 종료일 */
	private String ed;
	
	/** 정렬컬럼 */
	private String oc;
	
	/** 정렬방향 */
	private String ob;
	
	/** 체크박스 id 저장 */
	private List<String> labrIds;
	
	/** SEARCH VO를 맵형태로 변환 */
	public Map<String, Object> convertMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("searchOption", so);
		paramMap.put("searchValue", sv);
		paramMap.put("startDate", sd);
		paramMap.put("endDate", ed);
		paramMap.put("orderColumn",oc);
		paramMap.put("orderBy", ob);
		paramMap.put("labrIds", labrIds);
		return paramMap;
	}
	
}
