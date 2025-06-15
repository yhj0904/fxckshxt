package kr.co.nanwe.cmmn.service;

import lombok.Data;

/**
 * @Class Name 		: Paging
 * @Description 	: 페이징용 VO
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class Paging {
	
	/** 현재페이지 */
	private int currentPage;
	
	/** 시작 ROW */
	private int beginRow;
	
	/** 페이지당 ROW 수 */
	private int pagePerRow;
	
	/** 시작페이지번호 */
	private int startPage;
	
	/** 페이징 사이즈 */
	private int pageSize;
	
	/** 끝페이지번호 */
	private int endPage;
	
	/** 총페이지개수 */
	private int lastPage;
	
	/** 총ROW수 */
	private int totalCount;
	
}
