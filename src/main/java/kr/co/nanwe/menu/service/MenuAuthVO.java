package kr.co.nanwe.menu.service;

import lombok.Data;

/**
 * @Class Name 		: MenuAuthVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class MenuAuthVO {
	/** 권한CD */
	private String authType;
	/** 권한CD */
	private String authCd;
	/** 메뉴CD */
	private String menuCd;
	/** 메뉴ID */
	private String menuId;
	/** 비고 */
	private String note;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
}
