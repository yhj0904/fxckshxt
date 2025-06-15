package kr.co.nanwe.login.service.impl;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.login.service.CertificationVO;

/**
 * @Class Name 		: CertificationMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("certificationMapper")
public interface CertificationMapper {
	
	/** 인증로그 등록 */
	int insertCertification(CertificationVO certificationVO);

	/** 인증로그 성공 */
	int updateCertification(CertificationVO certificationVO);

	/** 시간 체크 */
	int selectCertificationSecond(@Param("certification") CertificationVO certificationVO, @Param("certTime") long certTime);
	
}
