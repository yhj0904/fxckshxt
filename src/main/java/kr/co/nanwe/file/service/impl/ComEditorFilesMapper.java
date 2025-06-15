package kr.co.nanwe.file.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ComEditorFilesVO;

/**
 * @Class Name 		: ComEditorFilesMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("comEditorFilesMapper")
public interface ComEditorFilesMapper {	
	
	int insertComEditorFiles(ComEditorFilesVO comEditorFilesVO);

	int updateComEditorFiles(@Param("parentCd") String parentCd, @Param("parentUid") String parentUid, @Param("fileNo") int fileNo);
	
	List<ComEditorFilesVO> selectComEditorFilesByParent(@Param("parentCd") String parentCd, @Param("parentUid") String parentUid);
	
	List<ComEditorFilesVO> selectComEditorFilesByNoParent();

	int deleteComEditorFiles(int fileNo);	

	int deleteComEditorFilesByNoParent();

	int updateComEditorFilesByBbs(@Param("parentCd") String parentCd, @Param("parentUid") String parentUid, @Param("fileNo") int fileNo, @Param("bbsCd") String bbsCd);
	
	int resetcomEditorFilesByBbsCd(@Param("parentCd") String parentCd, @Param("bbsCd") String bbsCd);
}
