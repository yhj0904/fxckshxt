package kr.co.nanwe.external.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.dept.service.DeptVO;
import kr.co.nanwe.external.service.ExternalDao;
import kr.co.nanwe.external.service.ExternalService;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: ExternalServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("externalService")
public class ExternalServiceImpl extends EgovAbstractServiceImpl implements ExternalService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ExternalServiceImpl.class);
	
	/** Mapper */
	@Resource(name="externalMapper")
    private ExternalMapper externalMapper;
	
	/** Dao */
	@Resource(name="externalDao")
    private ExternalDao externalDao;

	@Override
	public Map<String, Object> selectUserList(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		int totCnt = externalMapper.selectUserTotCnt(paramMap);
		map.put("totCnt", totCnt);
		List<UserVO> list = externalMapper.selectUserList(paramMap);
		map.put("list", list);
		return map;
	}

	@Override
	public Map<String, Object> selectDeptList(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<DeptVO> list = externalMapper.selectDeptList(paramMap);
		map.put("list", list);
		return map;
	}

	@Override
	public int insertPushAppUserFromViewUser() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		paramMap.put("userId", userId);
		paramMap.put("userIp", userIp);
		
		return externalMapper.insertPushAppUserFromViewUser(paramMap);
	}

	@Override
	public List<UserVO> selectUserListByDeptList(List<String> deptCdList) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deptCd", deptCdList);
		return externalMapper.selectUserListByDeptList(paramMap);
	}

	@Override
	public Map<String, Object> selectChildDeptList(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<DeptVO> list = externalMapper.selectChildDeptList(paramMap);
		map.put("list", list);
		return map;
	}

	@Override
	public List<Map<String, Object>> selectUserDvcdList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return externalMapper.selectUserDvcdList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectWorkDvcdList(String userDvcd) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userDvcd", userDvcd);
		return externalMapper.selectWorkDvcdList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectStatDvcdList(String userDvcd, String workDvcd) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		return externalMapper.selectStatDvcdList(paramMap);
	}
	
	@Override
	public Map<String, Object> searchOne(String sqlId) {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		if(sqlId == null) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("session", loginVO);		
		return externalDao.searchOne(sqlId, paramMap);
	}
	
	@Override
	public Map<String, Object> searchOne(String sqlId, Object param) {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		if(sqlId == null) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(param != null) {
			paramMap.put("param", param);
		}
		paramMap.put("session", loginVO);		
		return externalDao.searchOne(sqlId, paramMap);
	}

	@Override
	public List<Map<String, Object>> searchList(String sqlId) {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		if(sqlId == null) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("session", loginVO);
		return externalDao.searchList(sqlId, paramMap);
	}

	@Override
	public List<Map<String, Object>> searchList(String sqlId, Object param) {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		if(sqlId == null) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(param != null) {
			paramMap.put("param", param);
		}
		paramMap.put("session", loginVO);
		return externalDao.searchList(sqlId, paramMap);
	}

	@Override
	public int updateUserPassword(String userId, String password) {
		UserVO userVO = externalMapper.selectUserOne(userId);
		if(userVO != null) {
			return externalMapper.updateUserPw(userId, password);
		}
		return 0;
	}
}
