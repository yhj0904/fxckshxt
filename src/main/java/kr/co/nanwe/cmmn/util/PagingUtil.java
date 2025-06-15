package kr.co.nanwe.cmmn.util;

import kr.co.nanwe.cmmn.service.Paging;

/**
 * @Class Name 		: PagingUtil
 * @Description 	: 페이징 처리를 위한 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class PagingUtil {
	
	/**
	 * 
	 * 현재 페이지와 페이지 옵션, 전체개수를 통한 페이징 처리를 한다
	 *
	 * @param String cp, int pagePerRow, int pageSize, int totalCount
	 * @return Paging
	 * @throws 
	 */
	public static strictfp Paging getPaging(String cp, int pagePerRow, int pageSize, int totalCount) {
		
		//현재페이지
		int currentPage = 1;
		
		//OVERFLOW 방지를 위해 문자열로 받은 후 파싱
		if(StringUtil.isNull(cp)) {
			cp = "1";
		}
		try {
			currentPage = Integer.parseInt(cp);
			if (currentPage <= 0 ) {
				currentPage = 1;
			}
		} catch (NumberFormatException e) {
			currentPage = 1;
		}
		
		if (pagePerRow <= 0) {
			pagePerRow = 10;
		}
		if (pageSize <= 0) {
			pageSize = 10;
		}
		

		int startPage = ((currentPage - 1) / pageSize) * pageSize + 1;

		int endPage = startPage + pageSize - 1;

		int lastPage = totalCount / pagePerRow;
		if (totalCount % pagePerRow != 0) {
			lastPage++;
		}
		if (endPage > lastPage) {
			endPage = lastPage;
		}
		
		//현재페이지가 라스트 페이지보다 클 경우
		if(currentPage > lastPage) {
			currentPage = lastPage;
		}
		
		int beginRow = (currentPage - 1) * pagePerRow;
		if(beginRow < 0) {
			beginRow = 0;
		}

		Paging paging = new Paging();
		paging.setCurrentPage(currentPage);
		paging.setBeginRow(beginRow);
		paging.setPagePerRow(pagePerRow);
		paging.setStartPage(startPage);
		paging.setPageSize(pageSize);
		paging.setLastPage(lastPage);
		paging.setEndPage(endPage);
		paging.setTotalCount(totalCount);
		return paging;
	}
}
