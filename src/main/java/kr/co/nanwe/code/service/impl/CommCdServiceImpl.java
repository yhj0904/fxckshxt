package kr.co.nanwe.code.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.code.service.CommCdService;
import kr.co.nanwe.code.service.CommCdVO;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: CommCdServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("commCdService")
public class CommCdServiceImpl extends EgovAbstractServiceImpl implements CommCdService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommCdServiceImpl.class);

	@Resource(name = "commCdMapper")
	private CommCdMapper commCdMapper;

	@Override
	@MethodDescription("공통코드 목록 조회")
	public Map<String, Object> selectCommCdList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();

		//목록 조회
		List<CommCdVO> list = commCdMapper.selectCommCdList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	@Override
	@MethodDescription("공통코드 상세조회")
	public CommCdVO selectCommCd(String id) {
		CommCdVO commCdVO = commCdMapper.selectCommCd(id);
		if(commCdVO != null) {
			//해당 코드를 가진 하위코드 목록을 조회한다.
			List<CommCdVO> list = commCdMapper.selectCommCdListByHiCd(commCdVO.getCd());
			commCdVO.setList(list);
		}
		return commCdVO;
	}

	@Override
	@MethodDescription("공통코드 등록")
	public int insertCommCd(CommCdVO commCdVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		commCdVO.setInptId(userId);
		commCdVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = commCdMapper.insertCommCd(commCdVO);
		
		//등록 결과 확인
		if(result > 0) {
			return 1;
		}
		
		return 0;
	}

	@Override
	@MethodDescription("공통코드 수정")
	public int updateCommCd(CommCdVO commCdVO) {
		
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		commCdVO.setModiId(userId);
		commCdVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = commCdMapper.updateCommCd(commCdVO);
		
		//수정 결과 확인
		if(result > 0) {
			return 1;
		}
		
		return 0;
	}

	@Override
	@MethodDescription("공통코드 삭제")
	public int deleteCommCd(String id) {
		return commCdMapper.deleteCommCd(id);
	}

	@Override
	@MethodDescription("공통코드 선택 삭제")
	public int deleteCheckedCommCd(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			int deleteResult = commCdMapper.deleteCommCd(id);
			if(deleteResult > 0) {
				result++;
			}
		}
		return result;
	}

	@Override
	@MethodDescription("공통코드 중복조회")
	public boolean selectCommCdExist(String cd) {
		
		//코드가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(cd)) {
			return true;
		}
		
		int count = commCdMapper.selectCommCdExist(cd);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("권한코드목록조회")
	public List<CommCdVO> selectAuthCdList() {
		List<CommCdVO> list = commCdMapper.selectCommCdListByHiCd("AUTH");
		if(list == null) {
			list = new ArrayList<CommCdVO>();
		}
		//관리자 권한코드 추가
		CommCdVO commCdVO = new CommCdVO();
		commCdVO.setCd(CodeConfig.SYS_USER_CODE);
		commCdVO.setCdNm("관리자");
		list.add(commCdVO);
		return list;
	}

	@Override
	@MethodDescription("권한코드목록조회")
	public List<CommCdVO> selectComUserAuthCdList() {
		return commCdMapper.selectCommCdListByHiCd("AUTH");
	}
	
	@Override
	@MethodDescription("코드목록조회")
	public List<CommCdVO> selectComAuthList(String cd){
		return commCdMapper.selectCommCdListByHiCd(cd);
	}

	@Override
	@MethodDescription("코드목록조회 - 채용정보(워크넷 검색조건 코드 조회)")
	public Map<String, Object> selectWorkCommCdList(String cd) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("hiCd", cd);
		
		//목록 조회
		List<CommCdVO> depth01 = new ArrayList<CommCdVO>();
		List<CommCdVO> depth02 = new ArrayList<CommCdVO>();
		List<CommCdVO> depth03 = new ArrayList<CommCdVO>();
		
		List<CommCdVO> list = commCdMapper.selectCommCdList(paramMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for(int i = 0; i < list.size(); i++) {
			int level = list.get(i).getCdLevel();
			switch (level) {			
				case 1:
					depth01.add(list.get(i));
					break;
				case 2:
					depth02.add(list.get(i));
					break;
				case 3:
					depth03.add(list.get(i));
					break;
	
				default:
					break;
			}
			
		}
		resultMap.put("regionCd", list);
		resultMap.put("rgnDepth01", depth01);
		resultMap.put("rgnDepth02", depth02);
		resultMap.put("rgnDepth03", depth03);
		
		return resultMap;
	}

	@Override
	@MethodDescription("코드 목록 조회")
	public List<CommCdVO> selectCommCdList(String cd) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("hiCd", cd);
		
		//목록 조회
		List<CommCdVO> list = commCdMapper.selectCommCdList(paramMap);
		return list;
	}

}
