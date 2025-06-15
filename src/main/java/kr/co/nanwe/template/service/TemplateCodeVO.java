package kr.co.nanwe.template.service;

import lombok.Data;

/**
 * @Class Name 		: TemplateCodeVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class TemplateCodeVO {
	/** 템플릿코드 */
	private String templateCd;
	/** 순번 */
	private String seq;
	/** 메인페이지 */
	private String mainCode;
	/** 서브페이지 */
	private String subCode;
	/** 로그인페이지 */
	private String loginCode;
	/** 빈페이지 */
	private String emptyCode;
	/** 팝업페이지 */
	private String popCode;
	/** HEADER */
	private String layoutHeader;
	/** FOOTER */
	private String layoutFooter;
	/** GNB */
	private String layoutGnb;
	/** 메인CSS */
	private String mainCss;
	/** 서브CSS */
	private String subCss;
	/** 로그인CSS */
	private String loginCss;
	/** 빈CSS */
	private String emptyCss;
	/** 팝업CSS */
	private String popCss;
	/** 레이아웃CSS */
	private String layoutCss;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
}
