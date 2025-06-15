package kr.co.nanwe.dept.service;

import java.util.List;
import java.util.Map;

import kr.co.nanwe.cmmn.service.SearchVO;

/**
 * @Class Name 		: DeptService
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */


public interface DeptService {
	
	/** 목록 조회 */
	Map<String, Object> selectDeptList(SearchVO search);
	
	/** 상세 조회 */
	DeptVO selectDept(String deptCd);
	
	/** 등록 */
	int insertDept(DeptVO deptVO);
	
	/** 수정 */
	int updateDept(DeptVO deptVO);
	
	/** 삭제 */
	int deleteDept(String deptCd);
	
	/** 선택 삭제 */
	int deleteCheckedDept(String checkedSId);

	/** 존재하는 코드인지 체크 */
	boolean selectDeptCdExist(String deptCd);
	
	/** 목록 조회 */
	List<DeptVO> selectDeptListByUse();

	/** 목록 조회 */
	List<DeptVO> selectColgListByUse(SearchVO search);
	
	/** 상위부서만 목록조회 */
	List<DeptVO> selectOnlyColgList();
	
	/** 하위부서 상위코드로 목록조회 */
	List<DeptVO> selectDepByHiCd(String hiDeptCd);

}
