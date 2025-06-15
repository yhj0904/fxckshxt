package kr.co.nanwe.dept.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.auth.service.impl.AuthMapper;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.dept.service.DeptService;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: DeptServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			학과/단과대학 조회 추가
 */

@Service("deptService")
public class DeptServiceImpl extends EgovAbstractServiceImpl implements DeptService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(DeptServiceImpl.class);

	@Resource(name = "deptMapper")
	private DeptMapper deptMapper;
	
	/** Mapper */
	@Resource(name="authMapper")
    private AuthMapper authMapper;

	@Override
	@MethodDescription("부서코드 목록 조회")
	public Map<String, Object> selectDeptList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();

		//목록 조회
		List<DeptVO> list = deptMapper.selectDeptList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("부서코드 상세조회")
	public DeptVO selectDept(String deptCd) {
		return deptMapper.selectDept(deptCd);
	}

	@Override
	@MethodDescription("부서코드 등록")
	public int insertDept(DeptVO deptVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		deptVO.setInptId(userId);
		deptVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = deptMapper.insertDept(deptVO);
		
		//등록 결과 확인
		if(result > 0) {
			return 1;
		}
		
		return 0;
	}

	@Override
	@MethodDescription("부서코드 수정")
	public int updateDept(DeptVO deptVO) {
		
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		deptVO.setModiId(userId);
		deptVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = deptMapper.updateDept(deptVO);
		
		//수정 결과 확인
		if(result > 0) {
			return 1;
		}
		
		return 0;
	}

	@Override
	@MethodDescription("부서코드 삭제")
	public int deleteDept(String id) {
		int result = deptMapper.deleteDept(id);
		if(result > 0) {
			//부서 권한 맵핑 삭제
			authMapper.deleteAuthDeptByDeptCd(CodeConfig.COM_USER_CODE, id);
		}
		return result;
	}

	@Override
	@MethodDescription("부서코드 선택 삭제")
	public int deleteCheckedDept(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			int deleteResult = deptMapper.deleteDept(id);
			if(deleteResult > 0) {
				//부서 권한 맵핑 삭제
				authMapper.deleteAuthDeptByDeptCd(CodeConfig.COM_USER_CODE, id);
				result++;
			}
		}
		return result;
	}

	@Override
	@MethodDescription("부서코드 중복조회")
	public boolean selectDeptCdExist(String deptCd) {
		
		//코드가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(deptCd)) {
			return true;
		}
		
		int count = deptMapper.selectDeptCdExist(deptCd);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("부서코드 목록 조회")
	public List<DeptVO> selectDeptListByUse() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("useYn", "Y");
		return deptMapper.selectDeptList(paramMap);
	}
	
	@Override
	@MethodDescription("부서코드 목록 조회")
	public List<DeptVO> selectColgListByUse(SearchVO search) {
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("useYn", "Y");
		paramMap.put("search", search);
		return deptMapper.selectColgList(paramMap);
	}
	
	@Override
	@MethodDescription("상위부서코드 목록 조회")
	public List<DeptVO> selectOnlyColgList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("useYn", "Y");
		return deptMapper.selectOnlyColgList(paramMap);
	}
	
	@Override
	@MethodDescription("상위부서코드로 하위 목록 조회")
	public List<DeptVO> selectDepByHiCd(String hiDeptCd) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("useYn", "Y");
		return deptMapper.selectDeptListByHiCd(hiDeptCd);
	}
}
