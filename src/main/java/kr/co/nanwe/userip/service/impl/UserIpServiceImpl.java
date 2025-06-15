package kr.co.nanwe.userip.service.impl;

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
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.userip.service.UserIpService;
import kr.co.nanwe.userip.service.UserIpVO;

/**
 * @Class Name 		: UserIpServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("userIpService")
public class UserIpServiceImpl extends EgovAbstractServiceImpl implements UserIpService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(UserIpServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	/** Mapper */
	@Resource(name = "userIpMapper")
	private UserIpMapper userIpMapper;

	@Override
	@MethodDescription("사용자 아이피 목록 조회")
	public Map<String, Object> selectUserIpList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = userIpMapper.selectUserIpTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<UserIpVO> list = userIpMapper.selectUserIpList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("사용자 아이피 상세조회")
	public UserIpVO selectUserIp(String id, String ip) {
		return userIpMapper.selectUserIp(id, ip);
	}

	@Override
	@MethodDescription("사용자 아이피 등록")
	public int insertUserIp(UserIpVO userIpVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		userIpVO.setInptId(userId);
		userIpVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		return userIpMapper.insertUserIp(userIpVO);
	}

	@Override
	@MethodDescription("사용자 아이피 수정")
	public int updateUserIp(UserIpVO userIpVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		userIpVO.setModiId(userId);
		userIpVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		return userIpMapper.updateUserIp(userIpVO);
	}

	@Override
	@MethodDescription("사용자 아이피 삭제")
	public int deleteUserIp(String id, String ip) {
		return userIpMapper.deleteUserIp(id, ip);
	}

	@Override
	@MethodDescription("사용자 아이피 선택 삭제")
	public int deleteCheckedUserIp(String checkedSId) {
		int result = 0;
		String[] arr = checkedSId.split("\\|");
		for (int i=0; i<arr.length; i++) {
			String id = arr[i].split("#")[0];
			String ip = arr[i].split("#")[1];
			int deleteResult = userIpMapper.deleteUserIp(id, ip);
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
			}
		}
		return result;
	}

	@Override
	@MethodDescription("사용자 아이피  중복체크")
	public boolean selectUserIpExist(String id, String ip) {		
		int count = userIpMapper.selectUserIpExist(id, ip);
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	@MethodDescription("사용자 아이피 엑셀 다운")
	public void selectUserIpExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {

		// 검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		userIpMapper.selectUserIpExcelList(paramMap, handler);
	}
	
}
