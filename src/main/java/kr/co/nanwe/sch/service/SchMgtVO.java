package kr.co.nanwe.sch.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: SchMgtVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class SchMgtVO {
	/** 코드 */
	private String code;
	/** 제목 */
	private String title;
	/** 일정 상단 */
	private String header;
	/** 일정 하단 */
	private String footer;
	/** 스킨 코드 */
	private String skinCd;
	/** 스킨 코드 값 */
	private String skinCdVal;
	/** 유형 코드 */
	private String typeCd;
	/** 첨부파일 사용유무 */
	private String fileYn;
	/** 첨부파일 개수 */
	private int fileCnt;
	/** 첨부파일 최대크기 */
	private String fileSize;
	/** 허용 확장자 */
	private String fileExt;
	/** 카테고리 사용유무 */
	private String cateYn;
	/** 카테고리 */
	private String category;
	/** 추가정보 사용유무 */
	private String supplYn;
	/** 추가정보1 사용유무 */
	private String suppl01Yn;
	/** 추가정보2 사용유무 */
	private String suppl02Yn;
	/** 추가정보3 사용유무 */
	private String suppl03Yn;
	/** 추가정보4 사용유무 */
	private String suppl04Yn;
	/** 추가정보5 사용유무 */
	private String suppl05Yn;
	/** 추가정보6 사용유무 */
	private String suppl06Yn;
	/** 추가정보7 사용유무 */
	private String suppl07Yn;
	/** 추가정보8 사용유무 */
	private String suppl08Yn;
	/** 추가정보9 사용유무 */
	private String suppl09Yn;
	/** 추가정보10 사용유무 */
	private String suppl10Yn;
	/** 추가정보1 제목 */
	private String suppl01Title;
	/** 추가정보2 제목 */
	private String suppl02Title;
	/** 추가정보3 제목 */
	private String suppl03Title;
	/** 추가정보4 제목 */
	private String suppl04Title;
	/** 추가정보5 제목 */
	private String suppl05Title;
	/** 추가정보6 제목 */
	private String suppl06Title;
	/** 추가정보7 제목 */
	private String suppl07Title;
	/** 추가정보8 제목 */
	private String suppl08Title;
	/** 추가정보9 제목 */
	private String suppl09Title;
	/** 추가정보10 제목 */
	private String suppl10Title;
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
	
	/** 액션별 권한 리스트 */
	private List<SchAuthVO> listAuth;
	private List<SchAuthVO> viewAuth;
	private List<SchAuthVO> regiAuth;
	
	/** 개인별 권한 */
	private boolean adminUser; //관리자체크
	private boolean listAuthYn; //목록권한
	private boolean viewAuthYn; //상세조회권한
	private boolean regiAuthYn; //등록권한
	
	/** 카테고리 목록 */
	private List<String> categoryList;
}
