package kr.co.nanwe.popup.service;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: PopupVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class PopupVO {
	/** 사이트코드 */
	private String siteCd;
	/** ID */
	private String popId;
	/** 팝업 유형 */
	private String popType;
	/** 팝업 이름 */
	private String popNm;
	/** 내용 */
	private String popCont;
	/** 팝업 시작일 */
	private String startDttm;
	/** 팝업 종료일 */
	private String endDttm;
	/** 파일 SEQ */
	private int fileNo;
	/** 대체텍스트 */
	private String imgExplain;
	/** 링크 */
	private String popLink;
	/** 너비 */
	private String popWidth;
	/** 높이 */
	private String popHeight;
	/** 사용유무 */
	private String useYn;
	/**  */
	private String inptId;
	/**  */
	private String inptIp;
	/**  */
	private String inptDttm;
	/**  */
	private String modiId;
	/**  */
	private String modiIp;
	/**  */
	private String modiDttm;
	
	private MultipartFile uploadFile;
	private ViewFileVO viewFile;
}
