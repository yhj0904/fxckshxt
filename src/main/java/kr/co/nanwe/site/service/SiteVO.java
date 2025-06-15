package kr.co.nanwe.site.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: SiteVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class SiteVO {
	/** 코드 */
	private String siteCd;
	/** 뷰코드 */
	private String viewCd;
	/** 사이트명 */
	private String siteNm;
	/** 사이트영문명 */
	private String siteEngNm;
	/** 사이트주소 */
	private String siteAddr;
	/** 사이트영문주소 */
	private String siteEngAddr;
	/** 전화번호 */
	private String telNo;
	/** 팩스번호 */
	private String faxNo;
	/** 추가정보1 */
	private String siteInfo01;
	/** 추가정보2 */
	private String siteInfo02;
	/** 추가정보3 */
	private String siteInfo03;
	/** 추가정보4 */
	private String siteInfo04;
	/** 추가정보5 */
	private String siteInfo05;
	/** 추가정보6 */
	private String siteInfo06;
	/** 추가정보7 */
	private String siteInfo07;
	/** 추가정보8 */
	private String siteInfo08;
	/** 추가정보9 */
	private String siteInfo09;
	/** 추가정보10 */
	private String siteInfo10;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일지 */
	private String inptDttm;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
	/** 관리자접속가능여부 */
	private String sysAccessYn;
	/** 사용유무 */
	private String useYn;
	/** 메타태그 */
	private String siteMeta;
	/** 로그인접속여부 */
	private String loginYn;
	
	/** 도메인 */
	private String domain;
	
	/** 도메인 리스트 */
	private List<DomainVO> domainList;
	
	/** 템플릿코드 */
	private String templateCd;
	
	/** 로고아이콘 */
	private int siteLogo;
	private MultipartFile uploadFile;
	private ViewFileVO viewFile;
	
	/** 코드 */
	private String indexCode;
	private String indexCss;
	
}
