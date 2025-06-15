package kr.co.nanwe.surv.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.surv.service.SurvAnswVO;
import kr.co.nanwe.surv.service.SurvItemVO;
import kr.co.nanwe.surv.service.SurvMgtVO;
import kr.co.nanwe.surv.service.SurvQuesVO;
import kr.co.nanwe.surv.service.SurvService;
import kr.co.nanwe.surv.service.SurvTrgtVO;

/**
 * @Class Name 		: SurvServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("survService")
public class SurvServiceImpl extends EgovAbstractServiceImpl implements SurvService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SurvServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** 웹에디터 */
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;
	
	/** Mapper */
	@Resource(name = "survMgtMapper")
	private SurvMgtMapper survMgtMapper;
	
	@Resource(name = "survQuesMapper")
	private SurvQuesMapper survQuesMapper;
	
	@Resource(name = "survItemMapper")
	private SurvItemMapper survItemMapper;
	
	@Resource(name = "survAnswMapper")
	private SurvAnswMapper survAnswMapper;
	
	@Resource(name = "survTrgtMapper")
	private SurvTrgtMapper survTrgtMapper;

	@Override
	@MethodDescription("설문 목록 조회")
	public Map<String, Object> selectSurvMgtList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = survMgtMapper.selectSurvMgtTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<SurvMgtVO> list = survMgtMapper.selectSurvMgtList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("설문 상세 조회")
	public SurvMgtVO selectSurvMgt(String id) {
		
		SurvMgtVO survMgtVO = survMgtMapper.selectSurvMgt(id);
		
		if(survMgtVO != null) {
			
			//설문대상 조회
			List<SurvTrgtVO> trgtList = survTrgtMapper.selectSurvTrgtList(survMgtVO.getSurvId());
			survMgtVO.setTrgtList(trgtList);
			
			//설문문항 조회
			List<SurvQuesVO> quesList = survQuesMapper.selectSurvQuesList(survMgtVO.getSurvId(), null);
			if(quesList != null && quesList.size() > 0) {
				//문항별 항목 조회
				for (int i = 0; i < quesList.size(); i++) {
					quesList.get(i).setItemList(survItemMapper.selectSurvItemList(survMgtVO.getSurvId(), quesList.get(i).getQuesIdx(), null));
				}
			}
			survMgtVO.setQuesList(quesList);
		}
		return survMgtVO;
	}

	@Override
	@MethodDescription("설문 등록")
	public int insertSurvMgt(SurvMgtVO survMgtVO) {
		
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		survMgtVO.setInptId(userId);
		survMgtVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = survMgtMapper.insertSurvMgt(survMgtVO);
		
		//등록 결과 확인
		if(result > 0) {			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFiles("COM_SURV", survMgtVO.getSurvId());
		}
		
		return result;
	}

	@Override
	@MethodDescription("설문 수정")
	public int updateSurvMgt(SurvMgtVO survMgtVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		survMgtVO.setModiId(userId);
		survMgtVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = survMgtMapper.updateSurvMgt(survMgtVO);
		
		//수정 결과 확인
		if(result > 0) {			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFiles("COM_SURV", survMgtVO.getSurvId());			
		}
		
		return result;
	}

	@Override
	@MethodDescription("설문 삭제")
	public int deleteSurvMgt(String id) {
		int result = survMgtMapper.deleteSurvMgt(id);
		//첨부파일 삭제
		if(result > 0) {			
			//에디터 이미지
			ckEditorUpload.deleteComEditorFiles("COM_SURV", id);			
			//설문대상 삭제
			survTrgtMapper.deleteSurvTrgtBySurvId(id);
			//설문문항 삭제
			survQuesMapper.deleteSurvQuesBySurvId(id);
			//설문항목 삭제
			survItemMapper.deleteSurvItemBySurvId(id);
			//설문응답 삭제
			survAnswMapper.deleteSurvAnswBySurvId(id);
		}
		return result;
	}

	@Override
	@MethodDescription("설문 상태 변경")
	public boolean updateSurvState(String survId, String survState) {
		
		SurvMgtVO survMgtVO = new SurvMgtVO();
		
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		survMgtVO.setModiId(userId);
		survMgtVO.setModiIp(userIp);
		
		survMgtVO.setSurvId(survId);
		survMgtVO.setSurvState(survState);
		
		if(survMgtMapper.updateSurvState(survMgtVO) > 0) {
			return true;
		}
		
		return false;
	}

	
	@Override
	@MethodDescription("설문 문항 및 항목 등록")
	@SuppressWarnings("unchecked")
	public boolean updateSurvQuesAndItem(Map<String, Object> surveyMap) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		boolean quesResult = false; //문항 등록체크
		boolean trgtResult = false; //대상 등록체크
	
		if(surveyMap == null) {
			return false;
		}
		
		if(surveyMap.get("survId") == null) {
			return false;
		}
		
		String survId = (String) surveyMap.get("survId");
		SurvMgtVO survMgtVO = survMgtMapper.selectSurvMgt(survId);
		if(survMgtVO == null) {
			return false;
		}
		
		//문항 파라미터 체크
		if(surveyMap.get("rowidx") != null && surveyMap.get("quesFlag") != null && surveyMap.get("quesIdx") != null && surveyMap.get("quesTitle") != null && surveyMap.get("quesMemo") != null && surveyMap.get("quesType") != null && surveyMap.get("quesSort") != null && surveyMap.get("quesUseYn") != null && surveyMap.get("quesNcsryYn") != null) {
			quesResult = true;
		}
		
		//문항 파라미터가 넘어온 경우
		if(quesResult) {
			
			SurvQuesVO survQuesVO = null;
			SurvItemVO survItemVO = null;
			
			//문항 파라미터 리스트
			List<String> rowList = (surveyMap.get("rowidx") instanceof List) ? (List<String>) surveyMap.get("rowidx"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("rowidx")));
			List<String> quesFlagList = (surveyMap.get("quesFlag") instanceof List) ? (List<String>) surveyMap.get("quesFlag"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesFlag")));
			List<String> quesIdxList = (surveyMap.get("quesIdx") instanceof List) ? (List<String>) surveyMap.get("quesIdx"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesIdx")));
			List<String> quesTitleList = (surveyMap.get("quesTitle") instanceof List) ? (List<String>) surveyMap.get("quesTitle"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesTitle")));
			List<String> quesMemoList = (surveyMap.get("quesMemo") instanceof List) ? (List<String>) surveyMap.get("quesMemo"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesMemo")));
			List<String> quesTypeList = (surveyMap.get("quesType") instanceof List) ? (List<String>) surveyMap.get("quesType"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesType")));
			List<String> quesSortList = (surveyMap.get("quesSort") instanceof List) ? (List<String>) surveyMap.get("quesSort"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesSort")));
			List<String> quesUseYnList = (surveyMap.get("quesUseYn") instanceof List) ? (List<String>) surveyMap.get("quesUseYn"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesUseYn")));
			List<String> quesNcsryYnList = (surveyMap.get("quesNcsryYn") instanceof List) ? (List<String>) surveyMap.get("quesNcsryYn"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("quesNcsryYn")));
			
			//row size
			int rowSize = rowList.size();
			
			//row 개수와 문항 파라미터 개수가 맞는지 체크		
			if(quesFlagList.size() != rowSize || quesIdxList.size() != rowSize || quesTitleList.size() != rowSize || quesMemoList.size() != rowSize || quesTypeList.size() != rowSize || quesSortList.size() != rowSize || quesUseYnList.size() != rowSize || quesNcsryYnList.size() != rowSize) {
				return false;
			}
			
			for (int i = 0; i < rowSize; i++) {
				
				//문항 객체 생성
				survQuesVO = new SurvQuesVO();
				survQuesVO.setSurvId(survId);
				survQuesVO.setInptId(userId);
				survQuesVO.setInptIp(userIp);
				survQuesVO.setModiId(userId);
				survQuesVO.setModiIp(userIp);
				
				//문항 DB RESULT
				int questionResult = 0;
				
				String rowName = "row"+rowList.get(i)+"_";
				String quesFlag = quesFlagList.get(i);
				String qIdx = quesIdxList.get(i);
				int quesIdx = 0;
				
				//수정 또는 삭제인 경우 설정
				if(!"I".equals(quesFlag) && !"0".equals(qIdx)) {
					quesIdx = Integer.parseInt(qIdx);
					survQuesVO.setQuesIdx(quesIdx);
				}
				
				survQuesVO.setQuesTitle(quesTitleList.get(i));
				survQuesVO.setQuesMemo(quesMemoList.get(i));
				survQuesVO.setQuesType(quesTypeList.get(i));
				survQuesVO.setQuesSort(Integer.parseInt(quesSortList.get(i)));
				survQuesVO.setUseYn(quesUseYnList.get(i));
				survQuesVO.setNcsryYn(quesNcsryYnList.get(i));
				
				//FLAG에 따른 DB 처리
				switch (quesFlag) {
					case "I":
						questionResult = survQuesMapper.insertSurvQues(survQuesVO);
						break;
					case "U":
						questionResult = survQuesMapper.updateSurvQues(survQuesVO);
						break;
					case "D":
						questionResult = survQuesMapper.deleteSurvQues(survQuesVO);
						break;
					default:
						break;
				}
				
				//문항이 반영되어있으면 처리
				if(questionResult > 0) {
					if("D".equals(quesFlag)) { //삭제일 경우
						survItemMapper.deleteSurvItemByQuesIdx(survId, quesIdx); //항목을 삭제한다.
					} else {
						
						//항목 파라미터 체크
						if(surveyMap.get(rowName+"itemFlag") == null || surveyMap.get(rowName+"itemIdx") == null || surveyMap.get(rowName+"itemTitle") == null || surveyMap.get(rowName+"itemMemo") == null || surveyMap.get(rowName+"itemPoint") == null || surveyMap.get(rowName+"itemEtc") == null || surveyMap.get(rowName+"itemUseYn") == null|| surveyMap.get(rowName+"itemSort") == null) {
							continue;
						}
						
						//항목 파라미터 리스트
						List<String> itemFlagList = (surveyMap.get(rowName+"itemFlag") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemFlag"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemFlag")));
						List<String> itemIdxList = (surveyMap.get(rowName+"itemIdx") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemIdx"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemIdx")));
						List<String> itemTitleList = (surveyMap.get(rowName+"itemTitle") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemTitle"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemTitle")));
						List<String> itemMemoList = (surveyMap.get(rowName+"itemMemo") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemMemo"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemMemo")));
						List<String> itemPointList = (surveyMap.get(rowName+"itemPoint") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemPoint"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemPoint")));
						List<String> itemEtcList = (surveyMap.get(rowName+"itemEtc") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemEtc"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemEtc")));
						List<String> itemUseYnList = (surveyMap.get(rowName+"itemUseYn") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemUseYn"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemUseYn")));
						List<String> itemSortList = (surveyMap.get(rowName+"itemSort") instanceof List) ? (List<String>) surveyMap.get(rowName+"itemSort"): new ArrayList<String>(Arrays.asList((String) surveyMap.get(rowName+"itemSort")));
						
						//item size
						int itemSize = itemIdxList.size();
						
						//아이템 개수가 맞는지 체크		
						if(itemFlagList.size() != itemSize || itemTitleList.size() != itemSize || itemMemoList.size() != itemSize || itemPointList.size() != itemSize || itemEtcList.size() != itemSize || itemUseYnList.size() != itemSize || itemSortList.size() != itemSize) {
							continue;
						}
						
						for (int j = 0; j < itemSize; j++) {
							
							//항목 객체 생성
							survItemVO = new SurvItemVO();
							survItemVO.setSurvId(survId);
							survItemVO.setQuesIdx(survQuesVO.getQuesIdx());
							survItemVO.setInptId(userId);
							survItemVO.setInptIp(userIp);
							survItemVO.setModiId(userId);
							survItemVO.setModiIp(userIp);
							
							String itemFlag = itemFlagList.get(j);
							String iIdx = itemIdxList.get(j);
							int itemIdx = 0;
							
							//수정 또는 삭제인 경우 설정
							if(!"I".equals(itemFlag) && !"0".equals(iIdx)) {
								itemIdx = Integer.parseInt(iIdx);
								survItemVO.setItemIdx(itemIdx);
							}
							
							survItemVO.setItemTitle(itemTitleList.get(j));
							survItemVO.setItemMemo(itemMemoList.get(j));
							survItemVO.setItemPoint(Integer.parseInt(itemPointList.get(j)));
							survItemVO.setItemEtc(itemEtcList.get(j));
							survItemVO.setUseYn(itemUseYnList.get(j));
							survItemVO.setItemSort(Integer.parseInt(itemSortList.get(j)));
							
							//FLAG에 따른 DB 처리
							switch (itemFlag) {
								case "I":
									survItemMapper.insertSurvItem(survItemVO);
									break;
								case "U":
									survItemMapper.updateSurvItem(survItemVO);
									break;
								case "D":
									survItemMapper.deleteSurvItem(survItemVO);
									break;
								default:
									break;
							}
						}
					}
				}
			}
		}
		
		//전체 삭제 후 타겟 정보 재등록
		survTrgtMapper.deleteSurvTrgtBySurvId(survId);
		
		//대상 파라미터 체크
		if(surveyMap.get("trgtId") != null || surveyMap.get("trgtCd") != null) {
			trgtResult = true;
		}
		
		//문항 파라미터가 넘어온 경우
		if(trgtResult) {
			
			List<SurvTrgtVO> list = new ArrayList<SurvTrgtVO>();		
			SurvTrgtVO survTrgtVO = null;
			
			if(surveyMap.get("trgtId") != null) {
				List<String> trgtIdList = (surveyMap.get("trgtId") instanceof List) ? (List<String>) surveyMap.get("trgtId"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("trgtId")));
				if(trgtIdList.size() > 0) {
					for(String trgtId : trgtIdList) {
						survTrgtVO = new SurvTrgtVO();
						survTrgtVO.setSurvId(survId);
						survTrgtVO.setTrgtType("TRGT_ID");
						survTrgtVO.setTrgtId(trgtId);
						survTrgtVO.setInptId(userId);
						survTrgtVO.setInptIp(userIp);
						list.add(survTrgtVO);
					}
				}
			}
			
			if(surveyMap.get("trgtCd") != null) {
				List<String> trgtCdList = (surveyMap.get("trgtCd") instanceof List) ? (List<String>) surveyMap.get("trgtCd"): new ArrayList<String>(Arrays.asList((String) surveyMap.get("trgtCd")));
				if(trgtCdList.size() > 0) {
					for(String trgtCd : trgtCdList) {
						survTrgtVO = new SurvTrgtVO();
						survTrgtVO.setSurvId(survId);
						survTrgtVO.setTrgtType("TRGT_CD");
						survTrgtVO.setTrgtCd(trgtCd);
						survTrgtVO.setInptId(userId);
						survTrgtVO.setInptIp(userIp);
						list.add(survTrgtVO);					
					}
				}
			}
			
			if(list.size() > 0) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("trgtList", list);
				survTrgtMapper.insertSurvTrgt(paramMap);
			}
			
		}
		
		return true;
	}

	@Override
	@MethodDescription("사용자 설문 목록 조회")
	public List<SurvMgtVO> selectSurveyList() {
		
		//비로그인 경우 return
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			return null;
		}
		
		String loginId = StringUtil.isNullToString(loginInfo.getLoginId());
		String authDvcd = StringUtil.isNullToString(loginInfo.getLoginUserType());
		String userDvcd = StringUtil.isNullToString(loginInfo.getUserDvcd());
		String workDvcd = StringUtil.isNullToString(loginInfo.getWorkDvcd());
		String statDvcd = StringUtil.isNullToString(loginInfo.getStatDvcd());
		String deptCd = StringUtil.isNullToString(loginInfo.getDeptCd());
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginId", loginId);
		paramMap.put("authDvcd", authDvcd);
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		paramMap.put("statDvcd", statDvcd);
		paramMap.put("deptCd", deptCd);
		
		return survMgtMapper.selectSurveyList(paramMap);
	}

	@Override
	@MethodDescription("사용자 설문 정보 조회")
	public SurvMgtVO selectSurvey(String survId) {
		
		//비로그인 경우 return
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {
			return null;
		}
				
		String loginId = StringUtil.isNullToString(loginInfo.getLoginId());
		String authDvcd = StringUtil.isNullToString(loginInfo.getLoginUserType());
		String userDvcd = StringUtil.isNullToString(loginInfo.getUserDvcd());
		String workDvcd = StringUtil.isNullToString(loginInfo.getWorkDvcd());
		String statDvcd = StringUtil.isNullToString(loginInfo.getStatDvcd());
		String deptCd = StringUtil.isNullToString(loginInfo.getDeptCd());
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("survId", survId);
		paramMap.put("loginId", loginId);
		paramMap.put("authDvcd", authDvcd);
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		paramMap.put("statDvcd", statDvcd);
		paramMap.put("deptCd", deptCd);
		
		SurvMgtVO survMgtVO = survMgtMapper.selectSurvey(paramMap);
		
		if(survMgtVO != null) {
			
			//설문문항 조회
			List<SurvQuesVO> quesList = survQuesMapper.selectSurvQuesList(survMgtVO.getSurvId(), "Y");
			if(quesList != null && quesList.size() > 0) {
				//문항별 항목 조회
				for (int i = 0; i < quesList.size(); i++) {
					quesList.get(i).setItemList(survItemMapper.selectSurvItemList(survMgtVO.getSurvId(), quesList.get(i).getQuesIdx(), "Y"));
				}
			}
			survMgtVO.setQuesList(quesList);
		}
		
		return survMgtVO;
	}

	@Override
	@MethodDescription("설문 응답 등록")
	@SuppressWarnings("unchecked")
	public Map<String, Object> insertSurveyAnswer(Map<String, Object> surveyMap) {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		boolean result = false; //등록 결과
		String errCd = "survMgtVO.error"; //에러메시지 코드
		returnMap.put("result", result);
		returnMap.put("errCd", errCd);
		
		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo == null) {			
			return returnMap;
		}
		
		
		//로그인정보 및 아이피
		String userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
	
		if(surveyMap == null) {
			return returnMap;
		}
		
		if(surveyMap.get("survId") == null) {
			return returnMap;
		}
		
		String survId = (String) surveyMap.get("survId");
		String authDvcd = StringUtil.isNullToString(loginInfo.getLoginUserType());
		String userDvcd = StringUtil.isNullToString(loginInfo.getUserDvcd());
		String workDvcd = StringUtil.isNullToString(loginInfo.getWorkDvcd());
		String statDvcd = StringUtil.isNullToString(loginInfo.getStatDvcd());
		String deptCd = StringUtil.isNullToString(loginInfo.getDeptCd());
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("survId", survId);
		paramMap.put("loginId", userId);
		paramMap.put("authDvcd", authDvcd);
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		paramMap.put("statDvcd", statDvcd);
		paramMap.put("deptCd", deptCd);
		
		SurvMgtVO survMgtVO = survMgtMapper.selectSurvey(paramMap);
		if(survMgtVO == null) {
			return returnMap;
		}
		
		//설문문항 조회
		List<SurvQuesVO> quesList = survQuesMapper.selectSurvQuesList(survMgtVO.getSurvId(), "Y");
		if(quesList != null && quesList.size() > 0) {
			for (int i = 0; i < quesList.size(); i++) {
				int quesIdx = quesList.get(i).getQuesIdx();
				String quesParamName = "question_" + Integer.toString(quesIdx);
				//필수값이면서 값이 안넘어왔을경우 break;
				if("Y".equals(quesList.get(i).getNcsryYn()) && surveyMap.get(quesParamName) == null) {
					errCd = "survMgtVO.error.required";
					break;
				}
				if(surveyMap.get(quesParamName) != null) {
					List<String> itemIdxList = (surveyMap.get(quesParamName) instanceof List) ? (List<String>) surveyMap.get(quesParamName): new ArrayList<String>(Arrays.asList((String) surveyMap.get(quesParamName)));
					if(itemIdxList != null && itemIdxList.size() > 0) {
						
						List<SurvAnswVO> answList = new ArrayList<SurvAnswVO>();
						SurvAnswVO survAnswVO;
						for (int j = 0; j < itemIdxList.size(); j++) {
							int itemIdx = Integer.parseInt(itemIdxList.get(j));
							String etcParamName = "etc_" + Integer.toString(quesIdx)+"_"+Integer.toString(itemIdx);
							survAnswVO = new SurvAnswVO();
							survAnswVO.setSurvId(survId);
							survAnswVO.setQuesIdx(quesIdx);
							survAnswVO.setItemIdx(itemIdx);
							survAnswVO.setInptId(userId);
							survAnswVO.setInptIp(userIp);
							
							if(surveyMap.get(etcParamName) != null) {
								//서술형이면서 내용이 없는 경우 continue
								if("ETC".equals(quesList.get(i).getQuesType()) && StringUtil.isNull((String)surveyMap.get(etcParamName))) {
									continue;
								}
								survAnswVO.setAnswContent((String)surveyMap.get(etcParamName));
							}
							
							answList.add(survAnswVO);
						}
						
						if(answList.size() > 0) {
							Map<String, Object> answParam = new HashMap<String, Object>();
							answParam.put("answList", answList);
							int insertResult = survAnswMapper.insertSurvAnswList(answParam);
							if(insertResult > 0) {
								result = true;
							} else {
								errCd = "message.error.create";
							}
						}
					}
				}
			}
		}
		
		//오류 발생시 해당 응답 데이터 rollback
		if(!result) {
			survAnswMapper.deleteSurvAnswByLoginId(survId, userId);
		}
		
		returnMap.put("result", result);
		returnMap.put("errCd", errCd);
		return returnMap;
	}

	@Override
	@MethodDescription("설문 결과 조회")
	public Map<String, Object> selectSurveyResult(String id) {
		
		Map<String, Object> map = new HashMap<String, Object>();

		SurvMgtVO survMgtVO = survMgtMapper.selectSurvMgt(id);
		
		if(survMgtVO != null) {			
			
			double totalPoint = 0;	//토탈 점수			
			int totalCount = 0; //응답자수
			
			//응답자 목록 조회
			List<SurvAnswVO> userList = survAnswMapper.selectSurvAnswUserList(survMgtVO.getSurvId());
			if(userList != null) {
				totalCount = userList.size();
			}
			
			//백분율 연산을 위한 제수 설정
			int divisor = (totalCount > 0) ? totalCount : 1;
			
			//설문결과 문항목록
			List<SurvQuesVO> quesList = survQuesMapper.selectSurvQuesResult(survMgtVO.getSurvId(), divisor);
			if(quesList != null && quesList.size() > 0) {				
				//문항별 항목 조회
				for (int i = 0; i < quesList.size(); i++) {
					totalPoint += quesList.get(i).getAvgPoint();
					quesList.get(i).setItemList(survItemMapper.selectSurvItemResult(survMgtVO.getSurvId(), quesList.get(i).getQuesIdx(), divisor));
				}
			}
			survMgtVO.setQuesList(quesList);
			
			survMgtVO.setTotalPoint(totalPoint);
			survMgtVO.setTotalCount(totalCount);
			map.put("userList", userList);
		}
		
		map.put("survMgtVO", survMgtVO);
		
		return map;
	}

	@Override
	@MethodDescription("설문 기타의견 목록 조회")
	public List<String> selectSurvAnswEtcList(String survId, int quesIdx, int itemIdx) {
		return survAnswMapper.selectSurvAnswEtcList(survId, quesIdx, itemIdx);
	}

	@Override
	public Map<String, Object> selectSurveyResultByUser(String survId, String userId) {
		
		Map<String, Object> map = new HashMap<String, Object>();

		SurvMgtVO survMgtVO = survMgtMapper.selectSurvMgt(survId);
		
		if(survMgtVO != null) {			
			
			double totalPoint = 0;	//토탈 점수			
			
			//설문문항 조회
			List<SurvQuesVO> quesList = survQuesMapper.selectSurvQuesResultByUser(survMgtVO.getSurvId(), userId);
			if(quesList != null && quesList.size() > 0) {				
				//문항별 항목 조회
				for (int i = 0; i < quesList.size(); i++) {
					totalPoint += quesList.get(i).getAvgPoint();
					quesList.get(i).setAnswList(survAnswMapper.selectSurvAnswListByUser(survMgtVO.getSurvId(), quesList.get(i).getQuesIdx(), userId));
				}
			}
			
			survMgtVO.setQuesList(quesList);			
			survMgtVO.setTotalPoint(totalPoint);
		}
		
		map.put("survMgtVO", survMgtVO);
		
		return map;
	}

	@Override
	public List<SurvMgtVO> selectRecentSurvList() {
		return survMgtMapper.selectRecentSurvList();
	}
	
}
