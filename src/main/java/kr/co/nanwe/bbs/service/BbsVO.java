package kr.co.nanwe.bbs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.file.service.ViewFileVO;
import lombok.Data;

/**
 * @Class Name 		: BbsVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class BbsVO {
	/** 아이디 */
	private String bbsId;
	/** 게시판코드 */
	private String bbsCd;
	/** 게시판제목 */
	private String bbsTitle;
	/** 제목 */
	private String title;
	/** 내용 */
	private String contents;
	/** 작성자 */
	private String writer;
	/** 비밀번호 */
	private String pw;
	/** 카테고리 */
	private String category;
	/** 공지글여부 */
	private String notice;
	/** 비밀글여부 */
	private String secret;
	/** DEPTH */
	private int bbsDepth;
	/** 순서 */
	private int bbsSeq;
	/** 그룹ID */
	private String groupId;
	/** 부모글ID */
	private String parentId;
	/** 조회수 */
	private int viewCnt;
	/** 부가정보 1 */
	private String bbsInfo01;
	/** 부가정보 2 */
	private String bbsInfo02;
	/** 부가정보 3 */
	private String bbsInfo03;
	/** 부가정보 4 */
	private String bbsInfo04;
	/** 부가정보 5 */
	private String bbsInfo05;
	/** 부가정보 6 */
	private String bbsInfo06;
	/** 부가정보 7 */
	private String bbsInfo07;
	/** 부가정보 8 */
	private String bbsInfo08;
	/** 부가정보 9 */
	private String bbsInfo09;
	/** 부가정보 10 */
	private String bbsInfo10;
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
	/**  */
	private String delYn;
	/**  */
	private String delId;
	/**  */
	private String delIp;
	/**  */
	private String delDttm;
	
	/** 썸네일 */
	private String thumbnail;
	
	/** 첨부파일 목록 */
	private List<ViewFileVO> viewFiles;
	
	/** 업로드 파일 */
	private List<MultipartFile> uploadFiles;
}
