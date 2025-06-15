package kr.co.nanwe.site.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.site.service.DomainVO;

/**
 * @Class Name 		: DomainMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("domainMapper")
public interface DomainMapper {

	/** 도메인 중복체크 */
	int selectDomainExist(String domain);

	/** 도메인 등록 */
	int insertDomain(DomainVO domainVO);

	/** 도메인 목록 */
	List<DomainVO> selectDomainList(String siteCd);

	/** 도메인 삭제 */
	int deleteDomainBySiteCd(String siteCd);

}
