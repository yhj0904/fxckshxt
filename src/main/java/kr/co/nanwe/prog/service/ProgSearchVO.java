package kr.co.nanwe.prog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: ProgSearchVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.12		신한나			최초생성
 */
@Data
public class ProgSearchVO extends SearchVO {
	/** 개설년도 */
	private String search_year;
	/** 개설학기 */
	private String search_semstrCd;
	
	public Map<String, Object> convertMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("searchOption", super.getSo());
		paramMap.put("searchValue", super.getSv());
		paramMap.put("startDate", super.getSd());
		paramMap.put("endDate", super.getEd());
		paramMap.put("orderColumn",super.getOc());
		paramMap.put("orderBy", super.getOb());
		/** 코드 */
		paramMap.put("year", this.search_year);
		/** 사업및 내용 */
		paramMap.put("semstrCd", this.search_semstrCd);
		
		return paramMap;
	}
}
