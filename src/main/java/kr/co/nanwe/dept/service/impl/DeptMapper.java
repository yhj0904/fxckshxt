package kr.co.nanwe.dept.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.dept.service.DeptVO;

/**
 * @Class Name 		: DeptMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			학과/단과대학 조회 추가
 */

@Mapper("deptMapper")
public interface DeptMapper {
		
	/** 목록 조회 */
	List<DeptVO> selectDeptList(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<DeptVO> selectColgList(Map<String, Object> paramMap);
	
	/** 목록 조회 By HI_CD */
	List<DeptVO> selectDeptListByHiCd(String hiDeptCd);
	
	/** 상세 조회 */
	DeptVO selectDept(String deptCd);
	
	/** 등록 */
	int insertDept(DeptVO deptVO);
	
	/** 수정 */
	int updateDept(DeptVO deptVO);
	
	/** 삭제 */
	int deleteDept(String id);

	/** 중복코드 조회 */
	int selectDeptCdExist(String deptCd);
	
	/** 상위부서만 조회 */
	List<DeptVO> selectOnlyColgList(Map<String, Object> paramMap);
	
}
