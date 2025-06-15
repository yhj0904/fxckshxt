package kr.co.nanwe.menu.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: MenuVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Data
public class MenuVO {
	
	/** 메뉴CD */
	private String menuCd;
	/** 메뉴ID */
	private String menuId;
	/** 상위메뉴ID */
	private String hiMenuId;
	/** 메뉴명 */
	private String menuNm;
	/** 메뉴약어명 */
	private String menuAbbrNm;
	/** 메뉴영문명 */
	private String menuEngNm;
	/** 메뉴영문약어명 */
	private String menuEngAbbrNm;
	/** 메뉴설명 */
	private String menuDesc;
	/** 메뉴레벨 */
	private int menuLvl;
	/** 메뉴링크 */
	private String menuLink;
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
	/** 사용여부 */
	private String useYn;
	/** 노출여부 */
	private String viewYn;
	/** SSO */
	private String useSso;
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
	/** 메뉴아이콘 */
	private int fileNo;
	private MultipartFile uploadFile;
	private ViewFileVO viewFile;
	
	/** 하위 목록 개수 */
	private int childCnt;
	/** 하위 목록 */
	private List<MenuVO> list;
	/** 권한 목록 */
	private List<MenuAuthVO> authList;	
	
	private String imgName;
	private String imgPath;
	private String imgMime;
	private String iconSrc;
	
	private String activeYn;
	
}
