package kr.co.nanwe.banner.service;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: BannerVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			배너내용 추가
 */

@Data
public class BannerVO {
	/** ID */
	private String bannerId;
	/** 배너 코드 */
	private String bannerCd;
	/** 배너 이름 */
	private String bannerNm;
	/** 배너 내용 */
	private String bannerCont;
	/** 배너 링크 */
	private String bannerLink;
	/** 배너 시작일 */
	private String startDttm;
	/** 배너 종료일 */
	private String endDttm;
	/** 파일 SEQ */
	private int fileNo;
	/** 대체텍스트 */
	private String imgExplain;
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
