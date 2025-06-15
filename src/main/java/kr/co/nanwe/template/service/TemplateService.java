package kr.co.nanwe.template.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: TemplateService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface TemplateService {
	
	/** 상세 조회 */
	List<TemplateMgtVO> selectTemplateUseList();
	
	/** 목록 조회 */
	Map<String, Object> selectTemplateList(SearchVO search);
	
	/** 폼 생성 */
	TemplateMgtVO getTemplateForm();
	
	/** 복사폼 생성 */
	TemplateMgtVO selectTemplateCopyForm(String templateCd);
	
	/** 상세 조회 */
	TemplateMgtVO selectTemplate(String templateCd);
	
	/** 등록 */
	int insertTemplate(TemplateMgtVO templateVO);
	
	/** 수정 */
	int updateTemplate(TemplateMgtVO templateVO);
	
	/** 삭제 */
	int deleteTemplate(String templateCd);
	
	/** 존재하는 코드인지 체크 */
	boolean selectTemplateCdExist(String templateCd);

	/** 코드목록 조회 */
	Map<String, Object> selectTemplateCodeList(SearchVO search, String templateCd);

	/** 코드상세 조회 */
	TemplateCodeVO selectTemplateCode(String templateCd, int seq);

	/** 코드복원 */
	int updateTemplateByCode(String templateCd, int seq);

	/** 해당 템플릿 사용유무 체크 */
	int checkSiteUseTemplate(String templateCd);
	
}
