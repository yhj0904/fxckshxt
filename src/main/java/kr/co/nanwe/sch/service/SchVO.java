package kr.co.nanwe.sch.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: SchVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class SchVO {
	/** 아이디 */
	private String schId;
	/** 일정코드 */
	private String schCd;
	/** 제목 */
	private String title;
	/** 내용 */
	private String contents;
	/** 시작일 */
	private String startDttm;
	/** 종료일 */
	private String endDttm;
	/** 작성자 */
	private String writer;
	/** 비밀번호 */
	private String pw;
	/** 카테고리 */
	private String category;
	/** 순서 */
	private int schSeq;
	/** 조회수 */
	private int viewCnt;
	/** 부가정보1 */
	private String schInfo01;
	/** 부가정보2 */
	private String schInfo02;
	/** 부가정보3 */
	private String schInfo03;
	/** 부가정보4 */
	private String schInfo04;
	/** 부가정보5 */
	private String schInfo05;
	/** 부가정보6 */
	private String schInfo06;
	/** 부가정보7 */
	private String schInfo07;
	/** 부가정보8 */
	private String schInfo08;
	/** 부가정보9 */
	private String schInfo09;
	/** 부가정보10 */
	private String schInfo10;
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
	
	/** 첨부파일 목록 */
	private List<ViewFileVO> viewFiles;
	
	/** 업로드 파일 */
	private List<MultipartFile> uploadFiles;
	
	private String startYear;
	private String startMonth;
	private String startDay;
	private String endYear;
	private String endMonth;
	private String endDay;
}
