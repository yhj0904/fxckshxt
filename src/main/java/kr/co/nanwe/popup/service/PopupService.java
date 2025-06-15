package kr.co.nanwe.popup.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: PopupService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public interface PopupService {

	/** 목록 조회 */
	Map<String, Object> selectPopupList(SearchVO search);
	
	/** 상세 조회 */
	PopupVO selectPopup(String id);
	
	/** 등록 */
	int insertPopup(PopupVO popupVO);
	
	/** 수정 */
	int updatePopup(PopupVO popupVO);
	
	/** 삭제 */
	int deletePopup(String id);
	
	/** 선택 삭제 */
	int deleteCheckedPopup(String checkedSId);
	
	/** 메인 팝업 목록 조회 */
	List<PopupVO> selectPopupByMain(String siteCd);
}
