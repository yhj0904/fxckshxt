package kr.co.nanwe.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.auth.service.AuthDeptVO;
import kr.co.nanwe.auth.service.AuthMapVO;
import kr.co.nanwe.auth.service.AuthService;
import kr.co.nanwe.auth.service.AuthVO;
import kr.co.nanwe.bbs.service.impl.BbsMgtMapper;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.impl.CommCdMapper;
import kr.co.nanwe.external.service.impl.ExternalMapper;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.menu.service.impl.MenuAuthMapper;
import kr.co.nanwe.sch.service.impl.SchMgtMapper;

/**
 * @Class Name 		: AuthServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("authService")
public class AuthServiceImpl extends EgovAbstractServiceImpl implements AuthService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Mapper */
	@Resource(name="authMapper")
    private AuthMapper authMapper;
	
	/** Mapper */
	@Resource(name="commCdMapper")
    private CommCdMapper commCdMapper;
	
	/** Mapper */
	@Resource(name="externalMapper")
    private ExternalMapper externalMapper;
	
	/** Mapper */
	@Resource(name="menuAuthMapper")
    private MenuAuthMapper menuAuthMapper;
	
	/** Mapper */
	@Resource(name="bbsMgtMapper")
    private BbsMgtMapper bbsMgtMapper;
	
	/** Mapper */
	@Resource(name="schMgtMapper")
    private SchMgtMapper schMgtMapper;

	@Override
	@MethodDescription("권한 목록 조회")
	public List<AuthVO> selectAuthList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return authMapper.selectAuthList(paramMap);
	}

	@Override
	@MethodDescription("권한 사용목록 조회")
	public List<AuthVO> selectAuthUseList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("useYn", "Y");
		List<AuthVO> list = authMapper.selectAuthList(paramMap);
		if(list == null) {
			list = new ArrayList<AuthVO>();
		}
		
		//전체 권한을 추가
		AuthVO authVO = new AuthVO();
		authVO.setAuthCd("ALL");
		authVO.setAuthNm("전체");
		list.add(0, authVO);
		
		//비로그인 권한
		authVO = new AuthVO();
		authVO.setAuthCd(CodeConfig.NO_LOGIN);
		authVO.setAuthNm("비회원");
		list.add(1, authVO);
		
		return list;
	}

	@Override
	@MethodDescription("권한 상세 조회")
	public AuthVO selectAuth(String authCd) {
		AuthVO auth = authMapper.selectAuth(authCd);
		if(auth != null) {
			//AUTH MAP 조회
			List<AuthMapVO> mapList = authMapper.selectAuthMapList(authCd);
			auth.setMapList(mapList);
			
			//DEPT MAP 조회
			List<AuthDeptVO> deptList = authMapper.selectAuthDeptList(authCd);
			auth.setDeptList(deptList);
		}
		return auth;
	}

	@Override
	@MethodDescription("권한맵 목록 조회")
	public List<AuthMapVO> selectAuthMapList(String authCd) {
		return authMapper.selectAuthMapList(authCd);
	}

	@Override
	@MethodDescription("권한코드 중복체크")
	public boolean selectAuthCdExist(String authCd) {
		//코드가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(authCd)) {
			return true;
		}
		
		//ALL 은 전체 , NO_LOGIN은 비회원
		if("ALL".equals(authCd) || CodeConfig.NO_LOGIN.equals(authCd)) {
			return true;
		}
		
		int count = authMapper.selectAuthCdExist(authCd);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("권한 등록")
	public int insertAuth(AuthVO authVO) {
		
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		authVO.setInptId(userId);
		authVO.setInptIp(userIp);
		
		int result = authMapper.insertAuth(authVO);
		if(result > 0) {
			String authCd = authVO.getAuthCd();
			//권한 맵핑 등록			
			if(authVO.getAuthDvcd() != null && authVO.getUserDvcd() != null && authVO.getUserDvnm() != null && authVO.getWorkDvcd() != null && authVO.getWorkDvnm() != null && authVO.getStatDvcd() != null && authVO.getStatDvnm() != null) {
				String[] authDvcdArr = authVO.getAuthDvcd();
				String[] userDvcdArr = authVO.getUserDvcd();
				String[] userDvnmArr = authVO.getUserDvnm();
				String[] workDvcdArr = authVO.getWorkDvcd();
				String[] workDvnmArr = authVO.getWorkDvnm();
				String[] statDvcdArr = authVO.getStatDvcd();
				String[] statDvnmArr = authVO.getStatDvnm();
				int arrCnt = authDvcdArr.length;
				if(userDvcdArr.length == arrCnt && userDvnmArr.length == arrCnt && workDvcdArr.length == arrCnt && workDvnmArr.length == arrCnt && statDvcdArr.length == arrCnt && statDvnmArr.length == arrCnt) {
					for (int i = 0; i < arrCnt; i++) {
						if(StringUtil.isNull(authDvcdArr[i]) || StringUtil.isNull(userDvcdArr[i]) || StringUtil.isNull(workDvcdArr[i]) || StringUtil.isNull(statDvcdArr[i])) {
							continue;
						}
						AuthMapVO authMapVO = new AuthMapVO();
						authMapVO.setAuthCd(authCd);
						authMapVO.setAuthDvcd(authDvcdArr[i]);
						authMapVO.setUserDvcd(userDvcdArr[i]);
						authMapVO.setUserDvnm(userDvnmArr[i]);
						authMapVO.setWorkDvcd(workDvcdArr[i]);
						authMapVO.setWorkDvnm(workDvnmArr[i]);
						authMapVO.setStatDvcd(statDvcdArr[i]);
						authMapVO.setStatDvnm(statDvnmArr[i]);
						authMapVO.setInptId(userId);
						authMapVO.setInptIp(userIp);
						authMapper.insertAuthMap(authMapVO);
					}
				}
			}
			
			//부서 맵핑 등록
			if(authVO.getDeptDvcd() != null && authVO.getDeptCd() != null && authVO.getDeptNm() != null) {
				String[] deptDvcdArr = authVO.getDeptDvcd();
				String[] deptCdArr = authVO.getDeptCd();
				String[] deptNmArr = authVO.getDeptNm();
				int arrCnt = deptDvcdArr.length;
				if(deptCdArr.length == arrCnt && deptNmArr.length == arrCnt) {
					for (int i = 0; i < arrCnt; i++) {
						if(StringUtil.isNull(deptDvcdArr[i]) || StringUtil.isNull(deptCdArr[i]) || StringUtil.isNull(deptNmArr[i])) {
							continue;
						}
						AuthDeptVO authDeptVO = new AuthDeptVO();
						authDeptVO.setAuthCd(authCd);
						authDeptVO.setAuthDvcd(deptDvcdArr[i]);
						authDeptVO.setDeptCd(deptCdArr[i]);
						authDeptVO.setDeptNm(deptNmArr[i]);
						authDeptVO.setInptId(userId);
						authDeptVO.setInptIp(userIp);
						authMapper.insertAuthDept(authDeptVO);
					}
				}
			}
			
		}		
		return result;
	}

	@Override
	@MethodDescription("권한 수정")
	public int updateAuth(AuthVO authVO) {
		
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		authVO.setModiId(userId);
		authVO.setModiIp(userIp);
		
		int result = authMapper.updateAuth(authVO);
		if(result > 0) {			
			String authCd = authVO.getAuthCd();
			
			//기등록된 권한 맵핑 삭제
			authMapper.deleteAuthMapByAuthCd(authCd);
			authMapper.deleteAuthDeptByAuthCd(authCd);
			
			//권한 맵핑 등록			
			if(authVO.getAuthDvcd() != null && authVO.getUserDvcd() != null && authVO.getUserDvnm() != null && authVO.getWorkDvcd() != null && authVO.getWorkDvnm() != null && authVO.getStatDvcd() != null && authVO.getStatDvnm() != null) {
				String[] authDvcdArr = authVO.getAuthDvcd();
				String[] userDvcdArr = authVO.getUserDvcd();
				String[] userDvnmArr = authVO.getUserDvnm();
				String[] workDvcdArr = authVO.getWorkDvcd();
				String[] workDvnmArr = authVO.getWorkDvnm();
				String[] statDvcdArr = authVO.getStatDvcd();
				String[] statDvnmArr = authVO.getStatDvnm();
				int arrCnt = authDvcdArr.length;
				if(userDvcdArr.length == arrCnt && userDvnmArr.length == arrCnt && workDvcdArr.length == arrCnt && workDvnmArr.length == arrCnt && statDvcdArr.length == arrCnt && statDvnmArr.length == arrCnt) {
					for (int i = 0; i < arrCnt; i++) {
						if(StringUtil.isNull(authDvcdArr[i]) || StringUtil.isNull(userDvcdArr[i]) || StringUtil.isNull(workDvcdArr[i]) || StringUtil.isNull(statDvcdArr[i])) {
							continue;
						}
						AuthMapVO authMapVO = new AuthMapVO();
						authMapVO.setAuthCd(authCd);
						authMapVO.setAuthDvcd(authDvcdArr[i]);
						authMapVO.setUserDvcd(userDvcdArr[i]);
						authMapVO.setUserDvnm(userDvnmArr[i]);
						authMapVO.setWorkDvcd(workDvcdArr[i]);
						authMapVO.setWorkDvnm(workDvnmArr[i]);
						authMapVO.setStatDvcd(statDvcdArr[i]);
						authMapVO.setStatDvnm(statDvnmArr[i]);
						authMapper.insertAuthMap(authMapVO);
					}
				}
			}
			
			//부서 맵핑 등록
			if(authVO.getDeptDvcd() != null && authVO.getDeptCd() != null && authVO.getDeptNm() != null) {
				String[] deptDvcdArr = authVO.getDeptDvcd();
				String[] deptCdArr = authVO.getDeptCd();
				String[] deptNmArr = authVO.getDeptNm();
				int arrCnt = deptDvcdArr.length;
				if(deptCdArr.length == arrCnt && deptNmArr.length == arrCnt) {
					for (int i = 0; i < arrCnt; i++) {
						if(StringUtil.isNull(deptDvcdArr[i]) || StringUtil.isNull(deptCdArr[i]) || StringUtil.isNull(deptNmArr[i])) {
							continue;
						}
						AuthDeptVO authDeptVO = new AuthDeptVO();
						authDeptVO.setAuthCd(authCd);
						authDeptVO.setAuthDvcd(deptDvcdArr[i]);
						authDeptVO.setDeptCd(deptCdArr[i]);
						authDeptVO.setDeptNm(deptNmArr[i]);
						authDeptVO.setInptId(userId);
						authDeptVO.setInptIp(userIp);
						authMapper.insertAuthDept(authDeptVO);
					}
				}
			}
		}		
		return result;
	}

	@Override
	@MethodDescription("권한 삭제")
	public int deleteAuth(String authCd) {
		int result = authMapper.deleteAuth(authCd);
		if(result > 0) {
			//메뉴권한 삭제
			menuAuthMapper.deleteMenuAuthByAuthCd(authCd);			
			//게시판권한 삭제
			bbsMgtMapper.deleteBbsAuthByAuthCd(authCd);
			//일정권한 삭제
			schMgtMapper.deleteSchAuthByAuthCd(authCd);
			//권한맵핑 삭제
			authMapper.deleteAuthMapByAuthCd(authCd);
			//부서권한맵핑 삭제
			authMapper.deleteAuthDeptByAuthCd(authCd);
		}
		return result;
	}
	
}
