package kr.co.nanwe.push.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.push.service.PushGrpMembVO;
import kr.co.nanwe.push.service.PushGrpMstVO;
import kr.co.nanwe.push.service.PushGrpService;

/**
 * @Class Name 		: PushGrpServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("pushGrpService")
public class PushGrpServiceImpl extends EgovAbstractServiceImpl implements PushGrpService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(PushGrpServiceImpl.class);
	
	@Resource(name="pushGrpMstMapper")
    private PushGrpMstMapper pushGrpMstMapper;
	
	@Resource(name="pushGrpMembMapper")
    private PushGrpMembMapper pushGrpMembMapper;
	
	@Override
	@MethodDescription("그룹 목록조회")
	public Map<String, Object> selectPushGrpMstList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = pushGrpMstMapper.selectPushGrpMstTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<PushGrpMstVO> list = pushGrpMstMapper.selectPushGrpMstList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("그룹 상세조회")
	public PushGrpMstVO selectPushGrpMst(String grpCd) {
		PushGrpMstVO pushGrpMstVO = pushGrpMstMapper.selectPushGrpMst(grpCd);
		if(pushGrpMstVO != null) {
			List<PushGrpMembVO> memList = pushGrpMembMapper.selectPushGrpMemListByGrpCd(grpCd);
			pushGrpMstVO.setMemList(memList);
		}
		return pushGrpMstVO;
	}

	@Override
	@MethodDescription("그룹 등록")
	public int insertPushGrpMst(PushGrpMstVO pushGrpMstVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		pushGrpMstVO.setGrpCreUser(userId);
		pushGrpMstVO.setInptId(userId);
		pushGrpMstVO.setInptIp(userIp);
		
		int result = pushGrpMstMapper.insertPushGrpMst(pushGrpMstVO);
		if(result > 0) {
			//그룹원 등록			
			if(pushGrpMstVO.getMemList() != null) {
				for(PushGrpMembVO pushGrpMembVO : pushGrpMstVO.getMemList()) {
					pushGrpMembVO.setGrpCd(pushGrpMstVO.getGrpCd());
					pushGrpMembVO.setInptId(userId);
					pushGrpMembVO.setInptIp(userIp);
					pushGrpMembMapper.insertPushGrpMemb(pushGrpMembVO);
				}				
			}
		}
		return result;
	}

	@Override
	@MethodDescription("그룹 수정")
	public int updatePushGrpMst(PushGrpMstVO pushGrpMstVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		pushGrpMstVO.setModiId(userId);
		pushGrpMstVO.setModiIp(userIp);
		
		int result = pushGrpMstMapper.updatePushGrpMst(pushGrpMstVO);
		if(result > 0) {
			//그룹원 삭제후 재등록
			pushGrpMembMapper.deletePushGrpMembByGrpCd(pushGrpMstVO.getGrpCd());
			
			if(pushGrpMstVO.getMemList() != null) {
				for(PushGrpMembVO pushGrpMembVO : pushGrpMstVO.getMemList()) {
					pushGrpMembVO.setGrpCd(pushGrpMstVO.getGrpCd());
					pushGrpMembVO.setInptId(userId);
					pushGrpMembVO.setInptIp(userIp);
					pushGrpMembMapper.insertPushGrpMemb(pushGrpMembVO);
				}				
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("그룹 삭제")
	public int deletePushGrpMst(String grpCd) {
		int result = pushGrpMstMapper.deletePushGrpMst(grpCd);
		if(result > 0) {
			//그룹원 삭제
			pushGrpMembMapper.deletePushGrpMembByGrpCd(grpCd);
		}
		return result;
	}

	@Override
	@MethodDescription("그룹 선택삭제")
	public int deleteCheckedPushGrpMst(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String grpCd = idArr[i];
			int delResult = pushGrpMstMapper.deletePushGrpMst(grpCd);
			if(delResult > 0) {
				//그룹원 삭제
				pushGrpMembMapper.deletePushGrpMembByGrpCd(grpCd);
				result++;
			}
		}
		return result;
	}

	@Override
	@MethodDescription("사용자 그룹원 목록 (코드목록)")
	public List<PushGrpMembVO> selectPushGrpMemListByGrpCdList(List<String> grpCdList) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("grpCd", grpCdList);
		return pushGrpMembMapper.selectPushGrpMemListByGrpCdList(paramMap);
	}


	@Override
	@MethodDescription("로그인 사용자 그룹 전체 목록 ")
	public List<PushGrpMstVO> selectPushGrpMstAllListByLoginUser() {
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		return pushGrpMstMapper.selectPushGrpMstAllListByUser(loginVO.getLoginId());
	}
	
	@Override
	@MethodDescription("로그인 사용자 그룹 목록")
	public Map<String, Object> selectPushGrpMstListByLoginUser(SearchVO search) {
		
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("grpCreUser", loginVO.getLoginId());
		
		//전체 ROW COUNT
		int totCnt = pushGrpMstMapper.selectPushGrpMstTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<PushGrpMstVO> list = pushGrpMstMapper.selectPushGrpMstList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("로그인 사용자 그룹 상세조회")
	public PushGrpMstVO selectPushGrpMstByLoginUser(String grpCd) {
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		
		return pushGrpMstMapper.selectPushGrpMstByUser(loginVO.getLoginId(), grpCd);
	}

	@Override
	@MethodDescription("로그인 사용자 그룹 선택삭제")
	public int deleteCheckedPushGrpMstByLoginUser(String checkedSId) {
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return 0;
		}
		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String grpCd = idArr[i];
			int delResult = pushGrpMstMapper.deletePushGrpMstByLoginUser(loginVO.getLoginId(), grpCd);
			if(delResult > 0) {
				//그룹원 삭제
				pushGrpMembMapper.deletePushGrpMembByGrpCd(grpCd);
				result++;
			}
		}
		return result;
	}
	
	@Override
	@MethodDescription("로그인 사용자 그룹원 목록")
	public List<PushGrpMembVO> selectPushGrpMemListByLoginUser(String grpCd) {
		
		//로그인 정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		
		PushGrpMstVO pushGrpMstVO = pushGrpMstMapper.selectPushGrpMstByUser(loginVO.getLoginId(), grpCd);
		if(pushGrpMstVO != null) {
			return pushGrpMembMapper.selectPushGrpMemListByGrpCd(grpCd);
		}
		return null;
	}
}
