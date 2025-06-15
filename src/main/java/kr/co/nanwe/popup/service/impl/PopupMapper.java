package kr.co.nanwe.popup.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.popup.service.PopupVO;

/**
 * @Class Name 		: PopupMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("popupMapper")
public interface PopupMapper {
	
	/** 전체 Row 개수 */
	int selectPopupTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<PopupVO> selectPopupList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	PopupVO selectPopup(String popId);
	
	/** 등록 */
	int insertPopup(PopupVO popupVO);
	
	/** 수정 */
	int updatePopup(PopupVO popupVO);
	
	/** 삭제 */
	int deletePopup(String popId);
	
	/** 파일 수정 */
	int updatePopupFile(PopupVO popupVO);
	
	/** 메인 팝업 목록 조회 */
	List<PopupVO> selectPopupByMain(String siteCd);

}
