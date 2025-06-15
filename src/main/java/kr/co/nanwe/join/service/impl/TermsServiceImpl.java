package kr.co.nanwe.join.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.join.service.JoinVO;
import kr.co.nanwe.join.service.TermsService;
import kr.co.nanwe.join.service.TermsUserVO;
import kr.co.nanwe.join.service.TermsVO;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: TermsServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("termsService")
public class TermsServiceImpl extends EgovAbstractServiceImpl implements TermsService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(TermsServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Mapper */
	@Resource(name="termsMapper")
    private TermsMapper termsMapper;

	@Override
	@MethodDescription("약관 목록조회")
	public Map<String, Object> selectTermsList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = termsMapper.selectTermsTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<TermsVO> list = termsMapper.selectTermsList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("약관 상세조회")
	public TermsVO selectTerms(String termsId) {
		TermsVO termsVO = termsMapper.selectTerms(termsId);
		if(termsVO != null) {
			termsVO.setUserList(termsMapper.selectTermsUserList(termsId));
		}
		return termsVO;
	}

	@Override
	@MethodDescription("약관 등록")
	public int insertTerms(TermsVO termsVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		termsVO.setInptId(userId);
		termsVO.setInptIp(userIp);		
		return termsMapper.insertTerms(termsVO);
	}

	@Override
	@MethodDescription("약관 수정")
	public int updateTerms(TermsVO termsVO) {
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		termsVO.setModiId(userId);
		termsVO.setModiIp(userIp);		
		return termsMapper.updateTerms(termsVO);
	}

	@Override
	@MethodDescription("약관 삭제")
	public int deleteTerms(String termsId) {
		int result = termsMapper.deleteTerms(termsId);
		//첨부파일 삭제
		if(result > 0) {
			//해당 약관 동의 목록 삭제
			termsMapper.deleteTermsUserList(termsId);
		}
		return result;
	}

	@Override
	@MethodDescription("약관 선택삭제")
	public int deleteCheckedTerms(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String termsId = idArr[i];
			int deleteResult = termsMapper.deleteTerms(termsId);
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//해당 약관 동의 목록 삭제
				termsMapper.deleteTermsUserList(termsId);
			}
		}
		return result;
	}

	@Override
	@MethodDescription("약관 사용목록 조회")
	public List<TermsVO> selectTermsListByUse() {
		return termsMapper.selectTermsListByUse();
	}

	@Override
	@MethodDescription("약관 체크여부 검증")
	public boolean checkTermsList(List<TermsVO> termList) {
		boolean result = false;		
		
		//사용중인 약관 목록 조회
		List<TermsVO> useList = termsMapper.selectTermsListByUse();
		
		//등록된 약관이 있는경우
		if(useList != null && useList.size() > 0) {			
			
			//파라미터로 넘어온 약관이 있는경우 두 약관 목록을 비교
			if(termList != null && termList.size() > 0) {
				List<String> termsIdList = new ArrayList<String>(); 
				for(TermsVO termsVO : useList) {
					if(StringUtil.isEqual(termsVO.getNecessaryYn(), "Y")) {
						termsIdList.add(termsVO.getTermsId());
					}
				}
				
				//필수약관 목록
				if(termsIdList.size() > 0) {
					boolean checked = true;
					for(TermsVO termsVO : termList) {
						for(int i=termsIdList.size()-1; i>=0; i--) {
							if(termsIdList.get(i).equals(termsVO.getTermsId())) {
								if(termsVO.getChecked() == 1) {
									termsIdList.remove(i);
								} else {
									checked = false;
								}
								break;
							}
						}
						if(!checked) {
							break;
						}
					}
					result = checked;
				} else { //필수약관이 없는 경우
					result = true;
				}
			}
			
		} else { //약관이 없는 경우
			result = true;
		}
		
		return result;
	}

	@Override
	public int insertTermsJoinUser(JoinVO joinVO) {
		int result = 0;
		List<TermsVO> termList = joinVO.getTermList();
		if(termList != null && termList.size() > 0) {
			for(TermsVO termsVO : termList) {
				String agreeYn = "N";
				if(termsVO.getChecked() > 0) {
					agreeYn = "Y";
				}
				TermsUserVO termsUserVO = new TermsUserVO();
				termsUserVO.setUserType(CodeConfig.COM_USER_CODE);
				termsUserVO.setUserId(joinVO.getUserId());
				termsUserVO.setUserNm(joinVO.getUserNm());
				termsUserVO.setAgreeYn(agreeYn);
				termsUserVO.setTermsDvcd(termsVO.getTermsDvcd());
				termsUserVO.setTermsId(termsVO.getTermsId());
				termsUserVO.setInptId(joinVO.getUserId());
				termsUserVO.setInptIp(ClientUtil.getUserIp());
				result += termsMapper.insertTermsUser(termsUserVO);
			}
		}
		return result;
	}
}
