package kr.co.nanwe.push.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.external.service.impl.ExternalMapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.push.service.PushAppUserService;
import kr.co.nanwe.push.service.PushAppUserVO;
import kr.co.nanwe.user.service.UserVO;
import kr.co.nanwe.user.service.impl.UserMapper;

/**
 * @Class Name 		: PushAppUserServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("pushAppUserService")
public class PushAppUserServiceImpl extends EgovAbstractServiceImpl implements PushAppUserService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(PushAppUserServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Mapper */
	@Resource(name="externalMapper")
    private ExternalMapper externalMapper;
	
	@Resource(name="userMapper")
    private UserMapper userMapper;

	@Resource(name="pushAppUserMapper")
    private PushAppUserMapper pushAppUserMapper;

	@Override
	@MethodDescription("앱사용자 등록여부 조회")
	public boolean selectPushAppUserExist(String userId, String token) {
		int count = pushAppUserMapper.selectPushAppUserExist(userId, token);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("앱사용자 등록")
	public int registPushAppUser(String userId, String token) {
		
		String deviceDv = "";
		String deviceId = token;
		String userIp = ClientUtil.getUserIp();
		String userOs = ClientUtil.getUserOS().toUpperCase();
		if("ANDROID".equals(userOs)) {
			deviceDv = "Android";
		} else if("IPHONE".equals(userOs)) {
			deviceDv = "iPhone";
		} else if("IPAD".equals(userOs)) {
			deviceDv = "iPad";
		} else {
			deviceDv = "SMS";
		}
		
		int result = 0;
		
		PushAppUserVO pushAppUserVO = pushAppUserMapper.selectPushAppUser(userId);
		
		//기등록되어있고 디바이스가 변경된 경우
		if(pushAppUserVO != null) {			
			
			pushAppUserVO.setDeviceDv(deviceDv);
			pushAppUserVO.setDeviceId(deviceId);
			
			pushAppUserVO.setModiId(userId);
			pushAppUserVO.setModiIp(userIp);
			
			result = pushAppUserMapper.updatePushAppUser(pushAppUserVO);
			
		} else {
			
			pushAppUserVO = new PushAppUserVO();
			
			UserVO user = null;
			String deptCd = "";
			String userNm = "";
			String userDv = "";
			String userMobile = "";
			
			//외부데이터 사용유무 체크
			if(web.isExternalUse()) {
				user = externalMapper.selectUserOne(userId);
			}
			
			if(!web.isExternalUse() || user == null) {
				user = userMapper.selectUser(userId);
			}
			
			if(user != null) {
				userNm = user.getUserNm();
				userDv = user.getUserDvcd();
				deptCd = user.getDeptCd();
				userMobile = user.getMbphNo();
			}
			
			pushAppUserVO.setUserId(userId);
			pushAppUserVO.setDeptCd(deptCd);
			pushAppUserVO.setUserNm(userNm);
			pushAppUserVO.setUserDv(userDv);
			pushAppUserVO.setUserMobile(userMobile);			
			pushAppUserVO.setDeviceDv(deviceDv);
			pushAppUserVO.setDeviceId(deviceId);
			
			pushAppUserVO.setInptId(userId);
			pushAppUserVO.setInptIp(userIp);
			
			result = pushAppUserMapper.insertPushAppUser(pushAppUserVO);
		}
		
		return result;
	}

	@Override
	@MethodDescription("앱사용자 목록 조회")
	public Map<String, Object> selectPushAppUserList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = pushAppUserMapper.selectPushAppUserTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<PushAppUserVO> list = pushAppUserMapper.selectPushAppUserList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("앱사용자 상세조회")
	public PushAppUserVO selectPushAppUser(String userId) {
		return pushAppUserMapper.selectPushAppUser(userId);
	}

	@Override
	@MethodDescription("앱사용자 등록")
	public int insertPushAppUser(PushAppUserVO pushAppUserVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		pushAppUserVO.setInptId(userId);
		pushAppUserVO.setInptIp(userIp);
		
		return pushAppUserMapper.insertPushAppUser(pushAppUserVO);
	}

	@Override
	@MethodDescription("앱사용자 수정")
	public int updatePushAppUser(PushAppUserVO pushAppUserVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		pushAppUserVO.setModiId(userId);
		pushAppUserVO.setModiIp(userIp);
		
		return pushAppUserMapper.updatePushAppUser(pushAppUserVO);
	}

	@Override
	@MethodDescription("앱사용자 삭제")
	public int deletePushAppUser(String userId) {
		return pushAppUserMapper.deletePushAppUser(userId);
	}

	@Override
	@MethodDescription("앱사용자 선택삭제")
	public int deleteCheckedPushAppUser(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			result += pushAppUserMapper.deletePushAppUser(id);
		}
		return result;
	}

	@Override
	@MethodDescription("앱사용자 엑셀목록 조회")
	public void selectPushAppUserExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {
		// 검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();		
		pushAppUserMapper.selectPushAppUserExcelList(paramMap, handler);
	}

	@Override
	public Map<String, Object> selectPushAppUserCount() {
		return pushAppUserMapper.selectPushAppUserCount();
	}
}
