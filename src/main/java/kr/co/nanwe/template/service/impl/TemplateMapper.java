package kr.co.nanwe.template.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.template.service.TemplateCodeVO;
import kr.co.nanwe.template.service.TemplateMgtVO;

/**
 * @Class Name 		: TemplateMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("templateMapper")
public interface TemplateMapper {
	
	/** 템플릿 사용 목록 조회*/
	List<TemplateMgtVO> selectTemplateUseList();
	
	/** 전체 Row 개수 */
	int selectTemplateTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<TemplateMgtVO> selectTemplateList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	TemplateMgtVO selectTemplate(String templateCd);
	
	/** 등록 */
	int insertTemplate(TemplateMgtVO templateVO);
	
	/** 수정 */
	int updateTemplate(TemplateMgtVO templateVO);
	
	/** 삭제 */
	int deleteTemplate(String templateCd);
	
	/** 중복체크 */
	int selectTemplateCdExist(String templateCd);

	/** 템플릿 코드 목록 개수 */
	int selectTemplateCodeTotCnt(Map<String, Object> paramMap);

	/** 템플릿 코드 목록 조회 */
	List<TemplateCodeVO> selectTemplateCodeList(Map<String, Object> paramMap);
	
	/** 템플릿 코드 상세조회 */
	TemplateCodeVO selectTemplateCode(@Param("templateCd") String templateCd, @Param("seq") int seq);

	/** 템플릿 코드 등록 */
	int insertTemplateCode(TemplateCodeVO templateCodeVO);

	/** 템플릿 코드 삭제 */
	int deleteTemplateCode(@Param("templateCd") String templateCd, @Param("seq") int seq);
	
	/** 템플릿 코드 목록 삭제 */
	int deleteTemplateCodeByTemplateCd(String templateCd);

	/** 해당 템플릿 사용유무 체크 */
	int checkSiteUseTemplate(String templateCd);
	
}
