package kr.co.nanwe.join.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: TermsVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class TermsVO {

	/** 약관구분 */
	private String termsDvcd;
	/** 약관코드 */
	private String termsId;
	/** 약관명 */
	private String termsNm;
	/** 약관내용 */
	private String termsCont;
	/** 약관시행일 */
	private String termsDttm;
	/** 필수여부 */
	private String necessaryYn;
	/** 사용여부 */
	private String useYn;
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
	/** 수정일시 */
	private int termsOrd;
	
	/** 체크여부 */
	private int checked;
	
	/** 사용자 목록 */
	private List<TermsUserVO> userList;
	
}
