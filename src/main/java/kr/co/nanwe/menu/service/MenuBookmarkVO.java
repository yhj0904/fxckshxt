package kr.co.nanwe.menu.service;

import lombok.Data;

/**
 * @Class Name 		: MenuBookmarkVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class MenuBookmarkVO {
	
	/** 메뉴CD */
	private String menuCd;
	/** 메뉴ID */
	private String menuId;
	/** 상위메뉴ID */
	private String hiMenuId;
	/** 메뉴명 */
	private String menuNm;
	/** 메뉴링크 */
	private String menuLink;
	/** 메뉴레벨 */
	private int menuLvl;
	/** 프로그램여부 */
	private String progYn;
	/** 프로그램코드 */
	private String progCd;
	/** 프로그램경로 */
	private String progPath;
	/** 파라미터 */
	private String progParam;
	/** 정렬순서 */
	private int sortOrd;
	/** 입력ID */
	private String userId;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 즐겨찾기 여부 */
	private int bookCheck;
	/** SSO */
	private String useSso;
	
}
