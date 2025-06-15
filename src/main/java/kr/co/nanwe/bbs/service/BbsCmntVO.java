package kr.co.nanwe.bbs.service;

import lombok.Data;

/**
 * @Class Name 		: BbsCmntVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class BbsCmntVO {
	/** 아이디 */
	private String cmntId;
	/** 게시글ID */
	private String bbsId;
	/** 게시판CD */
	private String bbsCd;
	/** 댓글 */
	private String cmntTxt;
	/** 작성자 */
	private String writer;
	/** 비밀번호 */
	private String pw;
	/** 순서 */
	private int cmntSeq;
	/**  */
	private String inptId;
	/**  */
	private String inptIp;
	/**  */
	private String inptDttm;
}
