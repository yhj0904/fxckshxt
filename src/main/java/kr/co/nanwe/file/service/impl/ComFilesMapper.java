package kr.co.nanwe.file.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ComFilesLogVO;
import kr.co.nanwe.file.service.ComFilesVO;

/**
 * @Class Name 		: ComFilesMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("comFilesMapper")
public interface ComFilesMapper {
	
	/** 상세 조회 */	
	ComFilesVO selectComFiles(int fileNo);
	
	/** 등록 */
	int insertComFiles(ComFilesVO comFilesVO);
	
	/** 삭제 */
	int deleteComFiles(int fileNo);

	/** 부모코드로 목록 조회 */
	List<ComFilesVO> selectComFilesList(@Param("code") String code, @Param("id") String id);
	
	/** 부모코드로 목록 삭제 */
	int deleteComFilesList(@Param("code") String code, @Param("id") String id);
	
	/** 로그 등록 */
	int insertComFilesLog(ComFilesLogVO comFilesLogVO);
	
	/** 게시판코드로 목록 삭제 */
	int deleteComFilesByBbsCd(@Param("code") String code, @Param("bbsCd") String bbsCd);
	
	/** 썸네일 조회 */
	ComFilesVO selectThumbnail(@Param("code") String code, @Param("id") String id);	
	
}
