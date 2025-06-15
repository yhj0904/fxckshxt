package kr.co.nanwe.prog.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgSurvAnswVO;
import kr.co.nanwe.prog.service.ProgSurvItemVO;
import kr.co.nanwe.prog.service.ProgSurvMgtVO;
import kr.co.nanwe.prog.service.ProgSurvQuesVO;
import kr.co.nanwe.prog.service.ProgSurvService;
import kr.co.nanwe.prog.service.ProgVO;
import kr.co.nanwe.surv.service.SurvAnswVO;
import kr.co.nanwe.surv.service.SurvItemVO;
import kr.co.nanwe.surv.service.SurvQuesVO;
import kr.co.nanwe.surv.service.SurvTrgtVO;
import kr.co.nanwe.surv.service.impl.SurvAnswMapper;
import kr.co.nanwe.surv.service.impl.SurvItemMapper;

/**
 * @Class Name 		: ProgSurvServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.09		이가람			최초생성
 */

@Service("progSurvService")
public class ProgSurvServiceImpl extends EgovAbstractServiceImpl implements ProgSurvService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SurvServiceImpl.class);
	
		/** Web Config*/
		@Resource(name = "web")
		private WebConfig web;
		
		/** 웹에디터 */
		@Resource(name = "ckEditorUpload")
		private CkEditorUpload ckEditorUpload;
		
		/** Mapper */
		@Resource(name = "progSurvMgtMapper")
		private ProgSurvMgtMapper progSurvMgtMapper;
		
		@Resource(name = "progSurvQuesMapper")
		private ProgSurvQuesMapper progSurvQuesMapper;
		
		@Resource(name = "progSurvItemMapper")
		private ProgSurvItemMapper progSurvItemMapper;
		
		@Resource(name = "progSurvAnswMapper")
		private ProgSurvAnswMapper progSurvAnswMapper;
		
		@Resource(name = "progMapper")
		private ProgMapper progMapper;
		
		@Override
		@MethodDescription("설문 목록 조회")
		public Map<String, Object> selectSurvMgtList(SearchVO search) {
			
			//검색조건 설정
			if (search == null ) search = new SearchVO();
			Map<String, Object> paramMap = search.convertMap();
			
			//전체 ROW COUNT
			int totCnt = progSurvMgtMapper.selectSurvMgtTotCnt(paramMap);
			
			//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
			Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
			
			//시작 행과 페이지당 ROW수 조건 추가
			search.setCp(Integer.toString(paging.getCurrentPage()));
			paramMap.put("beginRow", paging.getBeginRow());
			paramMap.put("pagePerRow", paging.getPagePerRow());
			
			//목록 조회
			List<ProgSurvMgtVO> list = progSurvMgtMapper.selectSurvMgtList(paramMap);
			
			//조회결과 RETURN
			Map<String, Object> map = new HashMap<String, Object>();		
			map.put("paging", paging);
			map.put("search", search);
			map.put("list", list);
			
			return map;
		}
		
		@Override
		@MethodDescription("관리자 설문 상세 조회")
		public ProgSurvMgtVO selectSysSurvMgt(String id) {
			
			ProgSurvMgtVO ProgSurvMgtVO = progSurvMgtMapper.selectSurvMgt(id);
			
			if(ProgSurvMgtVO != null) {
				
				//설문문항 조회
				List<ProgSurvQuesVO> quesList = progSurvQuesMapper.selectSysSurvQuesList(ProgSurvMgtVO.getSurvId(), null);
				if(quesList != null && quesList.size() > 0) {
					//문항별 항목 조회
					for (int i = 0; i < quesList.size(); i++) {
						quesList.get(i).setItemList(progSurvItemMapper.selectSysSurvItemList(ProgSurvMgtVO.getSurvId(), quesList.get(i).getQuesIdx(), null));
					}
				}
				ProgSurvMgtVO.setQuesList(quesList);
			}
			return ProgSurvMgtVO;
		}

		@Override
		@MethodDescription("설문 상세 조회")
		public ProgSurvMgtVO selectSurvMgt(String id) {
			
			ProgSurvMgtVO ProgSurvMgtVO = progSurvMgtMapper.selectSurvMgt(id);
			
			if(ProgSurvMgtVO != null) {
				
				//설문문항 조회
				List<ProgSurvQuesVO> quesList = progSurvQuesMapper.selectSurvQuesList(ProgSurvMgtVO.getProgId(), null);
				if(quesList != null && quesList.size() > 0) {
					//문항별 항목 조회
					for (int i = 0; i < quesList.size(); i++) {
						quesList.get(i).setItemList(progSurvItemMapper.selectSurvItemList(ProgSurvMgtVO.getProgId(), quesList.get(i).getQuesIdx(), null));
					}
				}
				ProgSurvMgtVO.setQuesList(quesList);
			}
			return ProgSurvMgtVO;
		}

		@Override
		@MethodDescription("설문 등록")
		public int insertSurvMgt(ProgSurvMgtVO progSurvMgtVO) {
			
			//로그인정보 및 아이피
			String userId = "";		
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				userId = loginInfo.getLoginId();
			}
			String userIp = ClientUtil.getUserIp();
			
			//등록자 정보 SET
			progSurvMgtVO.setInptId(userId);
			progSurvMgtVO.setInptIp(userIp);
			
			//등록 처리 (등록된 경우 row 수 return)
			int result = progSurvMgtMapper.insertSurvMgt(progSurvMgtVO);
			
			//등록 결과 확인
			if(result > 0) {			
				//에디터 이미지 체크 후 변경
				ckEditorUpload.updateComEditorFiles("PROG_SURV", progSurvMgtVO.getSurvId());
			}
			
			return result;
		}

		@Override
		@MethodDescription("설문 수정")
		public int updateSurvMgt(ProgSurvMgtVO progSurvMgtVO) {
			
			//로그인정보 및 아이피
			String userId = "";	
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				userId = loginInfo.getLoginId();
			}
			String userIp = ClientUtil.getUserIp();
			
			//수정자 정보 SET
			progSurvMgtVO.setModiId(userId);
			progSurvMgtVO.setModiIp(userIp);
			
			//수정 처리 (수정된 경우 row 수 return)
			int result = progSurvMgtMapper.updateSurvMgt(progSurvMgtVO);
			
			//수정 결과 확인
			if(result > 0) {			
				//에디터 이미지 체크 후 변경
				ckEditorUpload.updateComEditorFiles("COM_SURV", progSurvMgtVO.getSurvId());			
			}
			
			return result;
		}

		@Override
		@MethodDescription("설문 삭제")
		public int deleteSurvMgt(String id) {
			
			System.out.println(">>>>>>>>>>>>>>>>>>>> 서비스단 : " + id + "/");
			
			int result = progSurvMgtMapper.deleteSurvMgt(id);
			//첨부파일 삭제
			if(result > 0) {			
				//에디터 이미지
				ckEditorUpload.deleteComEditorFiles("COM_SURV", id);			
				//설문문항 삭제
				progSurvQuesMapper.deleteSurvQuesBySurvId(id);
				//설문항목 삭제
				progSurvItemMapper.deleteSurvItemBySurvId(id);
				//설문응답 삭제
				//progSurvAnswMapper.deleteSurvAnswBySurvId(id);
			}
			return result;
		}

		@Override
		@MethodDescription("설문 상태 변경")
		public boolean updateSurvState(int progId, String survState) {
			
			ProgSurvMgtVO ProgSurvMgtVO = new ProgSurvMgtVO();
			
			String userId = "";	
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				userId = loginInfo.getLoginId();
			}
			String userIp = ClientUtil.getUserIp();
			
			ProgSurvMgtVO.setModiId(userId);
			ProgSurvMgtVO.setModiIp(userIp);
			
			ProgSurvMgtVO.setProgId(progId);
			ProgSurvMgtVO.setSurvState(survState);
			
			if(progSurvMgtMapper.updateSurvState(ProgSurvMgtVO) > 0) {
				return true;
			}
			
			return false;
		}

		
		@Override
		@MethodDescription("설문 문항 및 항목 등록")
		@SuppressWarnings("unchecked")
		public boolean updateSurvQuesAndItem(Map<String, Object> progSurveyMap) {
			
			System.out.println(">>>>>>>>>>>>>>>>  파라미터 : " + progSurveyMap);
			
			//로그인정보 및 아이피
			String userId = "";	
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				userId = loginInfo.getLoginId();
			}
			String userIp = ClientUtil.getUserIp();
			
			System.out.println("진입테스트1");
			
			boolean quesResult = false; //문항 등록체크
			boolean trgtResult = false; //대상 등록체크
		
			if(progSurveyMap == null) {
				return false;
			}
			
			System.out.println("진입테스트2");
			
			if(progSurveyMap.get("survId") == null) {
				return false;
			}
			
			System.out.println("진입테스트3");
			
			String survId = (String) progSurveyMap.get("survId");
			ProgSurvMgtVO ProgSurvMgtVO = progSurvMgtMapper.selectSurvMgt(survId);
			if(ProgSurvMgtVO == null) {
				return false;
			}
			
			System.out.println("진입테스트4");
			
			//문항 파라미터 체크
			if(progSurveyMap.get("rowidx") != null && progSurveyMap.get("quesFlag") != null && progSurveyMap.get("quesIdx") != null && progSurveyMap.get("quesTitle") != null && progSurveyMap.get("quesMemo") != null && progSurveyMap.get("quesType") != null && progSurveyMap.get("quesSort") != null && progSurveyMap.get("quesUseYn") != null && progSurveyMap.get("quesNcsryYn") != null) {
				quesResult = true;
			}
			
			System.out.println("진입테스트5");
			
			//문항 파라미터가 넘어온 경우
			if(quesResult) {
				
				ProgSurvQuesVO survQuesVO = null;
				ProgSurvItemVO survItemVO = null;
				
				//문항 파라미터 리스트
				List<String> rowList = (progSurveyMap.get("rowidx") instanceof List) ? (List<String>) progSurveyMap.get("rowidx"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("rowidx")));
				List<String> quesFlagList = (progSurveyMap.get("quesFlag") instanceof List) ? (List<String>) progSurveyMap.get("quesFlag"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesFlag")));
				List<String> quesIdxList = (progSurveyMap.get("quesIdx") instanceof List) ? (List<String>) progSurveyMap.get("quesIdx"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesIdx")));
				List<String> quesTitleList = (progSurveyMap.get("quesTitle") instanceof List) ? (List<String>) progSurveyMap.get("quesTitle"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesTitle")));
				List<String> quesMemoList = (progSurveyMap.get("quesMemo") instanceof List) ? (List<String>) progSurveyMap.get("quesMemo"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesMemo")));
				List<String> quesTypeList = (progSurveyMap.get("quesType") instanceof List) ? (List<String>) progSurveyMap.get("quesType"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesType")));
				List<String> quesSortList = (progSurveyMap.get("quesSort") instanceof List) ? (List<String>) progSurveyMap.get("quesSort"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesSort")));
				List<String> quesUseYnList = (progSurveyMap.get("quesUseYn") instanceof List) ? (List<String>) progSurveyMap.get("quesUseYn"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesUseYn")));
				List<String> quesNcsryYnList = (progSurveyMap.get("quesNcsryYn") instanceof List) ? (List<String>) progSurveyMap.get("quesNcsryYn"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("quesNcsryYn")));
				
				System.out.println("진입테스트6");
				
				//row size
				int rowSize = rowList.size();
				
				System.out.println(">>>>>>>>>>>> 리털항목 찍어보기 : " + rowList + "/" + quesFlagList + "/" + quesIdxList + "/" + quesTitleList + "/" + quesMemoList + "/" + quesTypeList + "/" + quesSortList + "/" + quesUseYnList + "/" + quesNcsryYnList);
				
				//row 개수와 문항 파라미터 개수가 맞는지 체크		
				if(quesFlagList.size() != rowSize || quesIdxList.size() != rowSize || quesTitleList.size() != rowSize || quesMemoList.size() != rowSize || quesTypeList.size() != rowSize || quesSortList.size() != rowSize || quesUseYnList.size() != rowSize || quesNcsryYnList.size() != rowSize) {
					return false;
				}
				
				System.out.println("진입테스트7");
				
				for (int i = 0; i < rowSize; i++) {
					
					//문항 객체 생성
					survQuesVO = new ProgSurvQuesVO();
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
							questionResult = progSurvQuesMapper.insertSurvQues(survQuesVO);
							break;
						case "U":
							questionResult = progSurvQuesMapper.updateSurvQues(survQuesVO);
							break;
						case "D":
							questionResult = progSurvQuesMapper.deleteSurvQues(survQuesVO);
							break;
						default:
							break;
					}
					
					//문항이 반영되어있으면 처리
					if(questionResult > 0) {
						if("D".equals(quesFlag)) { //삭제일 경우
							progSurvItemMapper.deleteSurvItemByQuesIdx(survId, quesIdx); //항목을 삭제한다.
						} else {
							
							//항목 파라미터 체크
							if(progSurveyMap.get(rowName+"itemFlag") == null || progSurveyMap.get(rowName+"itemIdx") == null || progSurveyMap.get(rowName+"itemTitle") == null || progSurveyMap.get(rowName+"itemMemo") == null || progSurveyMap.get(rowName+"itemPoint") == null || progSurveyMap.get(rowName+"itemEtc") == null || progSurveyMap.get(rowName+"itemUseYn") == null|| progSurveyMap.get(rowName+"itemSort") == null) {
								continue;
							}
							
							//항목 파라미터 리스트
							List<String> itemFlagList = (progSurveyMap.get(rowName+"itemFlag") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemFlag"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemFlag")));
							List<String> itemIdxList = (progSurveyMap.get(rowName+"itemIdx") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemIdx"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemIdx")));
							List<String> itemTitleList = (progSurveyMap.get(rowName+"itemTitle") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemTitle"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemTitle")));
							List<String> itemMemoList = (progSurveyMap.get(rowName+"itemMemo") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemMemo"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemMemo")));
							List<String> itemPointList = (progSurveyMap.get(rowName+"itemPoint") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemPoint"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemPoint")));
							List<String> itemEtcList = (progSurveyMap.get(rowName+"itemEtc") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemEtc"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemEtc")));
							List<String> itemUseYnList = (progSurveyMap.get(rowName+"itemUseYn") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemUseYn"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemUseYn")));
							List<String> itemSortList = (progSurveyMap.get(rowName+"itemSort") instanceof List) ? (List<String>) progSurveyMap.get(rowName+"itemSort"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(rowName+"itemSort")));
							
							//item size
							int itemSize = itemIdxList.size();
							
							//아이템 개수가 맞는지 체크		
							if(itemFlagList.size() != itemSize || itemTitleList.size() != itemSize || itemMemoList.size() != itemSize || itemPointList.size() != itemSize || itemEtcList.size() != itemSize || itemUseYnList.size() != itemSize || itemSortList.size() != itemSize) {
								continue;
							}
							
							for (int j = 0; j < itemSize; j++) {
								
								//항목 객체 생성
								survItemVO = new ProgSurvItemVO();
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
										progSurvItemMapper.insertSurvItem(survItemVO);
										break;
									case "U":
										progSurvItemMapper.updateSurvItem(survItemVO);
										break;
									case "D":
										progSurvItemMapper.deleteSurvItem(survItemVO);
										break;
									default:
										break;
								}
							}
						}
					}
				}
				
				System.out.println("진입테스트8");
			}
			
			//대상 파라미터 체크
			if(progSurveyMap.get("trgtId") != null || progSurveyMap.get("trgtCd") != null) {
				trgtResult = true;
			}
			
			//문항 파라미터가 넘어온 경우
			if(trgtResult) {
				
				List<SurvTrgtVO> list = new ArrayList<SurvTrgtVO>();		
				SurvTrgtVO survTrgtVO = null;
				
				if(progSurveyMap.get("trgtId") != null) {
					List<String> trgtIdList = (progSurveyMap.get("trgtId") instanceof List) ? (List<String>) progSurveyMap.get("trgtId"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("trgtId")));
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
				
				if(progSurveyMap.get("trgtCd") != null) {
					List<String> trgtCdList = (progSurveyMap.get("trgtCd") instanceof List) ? (List<String>) progSurveyMap.get("trgtCd"): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get("trgtCd")));
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
				
			}
			
			return true;
		}

		@Override
		@MethodDescription("사용자 설문 목록 조회")
		public List<ProgSurvMgtVO> selectSurveyList() {
			
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
			
			return progSurvMgtMapper.selectSurveyList(paramMap);
		}

		@Override
		@MethodDescription("사용자 설문 정보 조회")
		public ProgSurvMgtVO selectSurvey(@Param("progId") int progId) {
			
			// 프로그램 정보 조회
			ProgVO progVO = progMapper.selectProg(progId);
			
			System.out.println(progId);
			
			// VO 생성
			ProgSurvMgtVO progSurvMgtVO = new ProgSurvMgtVO();
			
			if(progVO != null) {
				
				progSurvMgtVO.setProgNm(progVO.getProgNm());
				
				//설문문항 조회
				List<ProgSurvQuesVO> quesList = progSurvQuesMapper.selectSurvQuesList(progVO.getProgId(), "Y");
				if(quesList != null && quesList.size() > 0) {
					//문항별 항목 조회
					for (int i = 0; i < quesList.size(); i++) {
						quesList.get(i).setItemList(progSurvItemMapper.selectSurvItemList(progVO.getProgId(), quesList.get(i).getQuesIdx(), "Y"));
					}
				}
				progSurvMgtVO.setQuesList(quesList);
			}
			
			return progSurvMgtVO;
		}

		@Override
		@MethodDescription("설문 응답 등록")
		@SuppressWarnings("unchecked")
		public Map<String, Object> insertSurveyAnswer(Map<String, Object> progSurveyMap) {
			
			System.out.println(">>>>>>>>>>>>>>>>>>>> progSurveyMap : " + progSurveyMap);

			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			boolean result = false; //등록 결과
			String errCd = "survMgtVO.fail"; //에러메시지 코드
			returnMap.put("result", result);
			returnMap.put("errCd", errCd);
			
			LoginVO loginInfo = SessionUtil.getLoginUser();
			
			if(loginInfo == null) {			
				return returnMap;
			}
			
			//로그인정보 및 아이피
			String userId = loginInfo.getLoginId();
			String userIp = ClientUtil.getUserIp();
		
			if(progSurveyMap.get("progId") == null) {
				return returnMap;
			}
			
			int progId = Integer.parseInt((String) progSurveyMap.get("progId"));
			String survId = StringUtil.isNullToString(progSurveyMap.get("survId"));
			String authDvcd = StringUtil.isNullToString(loginInfo.getLoginUserType());
			String userDvcd = StringUtil.isNullToString(loginInfo.getUserDvcd());
			String workDvcd = StringUtil.isNullToString(loginInfo.getWorkDvcd());
			String statDvcd = StringUtil.isNullToString(loginInfo.getStatDvcd());
			String deptCd = StringUtil.isNullToString(loginInfo.getDeptCd());
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("progId", progId);
			paramMap.put("survId", survId);
			paramMap.put("loginId", userId);
			paramMap.put("authDvcd", authDvcd);
			paramMap.put("userDvcd", userDvcd);
			paramMap.put("workDvcd", workDvcd);
			paramMap.put("statDvcd", statDvcd);
			paramMap.put("deptCd", deptCd);
			
			ProgVO progVO = progMapper.selectProg(progId);
			if(progVO == null) {
				return returnMap;
			}
			
			//설문문항 조회
			List<ProgSurvQuesVO> quesList = progSurvQuesMapper.selectSurvQuesList(progVO.getProgId(), "Y");
			if(quesList != null && quesList.size() > 0) {
				for (int i = 0; i < quesList.size(); i++) {
					int quesIdx = quesList.get(i).getQuesIdx();
					String quesParamName = "question_" + Integer.toString(quesIdx);
					System.out.println("객관식 답변 : " + quesParamName);
					//필수값이면서 값이 안넘어왔을경우 break;
					if("Y".equals(quesList.get(i).getNcsryYn()) && progSurveyMap.get(quesParamName) == null) {
						errCd = "ProgSurvMgtVO.error.required";
						break;
					}
					if(progSurveyMap.get(quesParamName) != null) {
						List<String> itemIdxList = (progSurveyMap.get(quesParamName) instanceof List) ? (List<String>) progSurveyMap.get(quesParamName): new ArrayList<String>(Arrays.asList((String) progSurveyMap.get(quesParamName)));
						if(itemIdxList != null && itemIdxList.size() > 0) {
							
							List<ProgSurvAnswVO> answList = new ArrayList<ProgSurvAnswVO>();
							ProgSurvAnswVO progSurvAnswVO;
							for (int j = 0; j < itemIdxList.size(); j++) {
								int itemIdx = Integer.parseInt(itemIdxList.get(j));
								String etcParamName = "etc_" + Integer.toString(quesIdx)+"_"+Integer.toString(itemIdx);
								progSurvAnswVO = new ProgSurvAnswVO();
								progSurvAnswVO.setProgId(progId);
								progSurvAnswVO.setUserId(userId);
								progSurvAnswVO.setSurvId(survId);
								progSurvAnswVO.setQuesIdx(quesIdx);
								progSurvAnswVO.setItemIdx(itemIdx);
								progSurvAnswVO.setInptId(userId);
								progSurvAnswVO.setInptIp(userIp);
								
								if(progSurveyMap.get(etcParamName) != null) {
									//서술형이면서 내용이 없는 경우 continue
									if("ETC".equals(quesList.get(i).getQuesType()) && StringUtil.isNull((String)progSurveyMap.get(etcParamName))) {
										continue;
									}
									progSurvAnswVO.setAnswContent((String)progSurveyMap.get(etcParamName));
								}
								
								answList.add(progSurvAnswVO);
							}
							
							if(answList.size() > 0) {
								Map<String, Object> answParam = new HashMap<String, Object>();
								int totalPoint = 0;
								answParam.put("answList", answList);
								System.out.println("이게 답변들이 맞나요 : " + answList);
								
								int insertResult = progSurvAnswMapper.insertSurvAnswList(answParam);
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
				progSurvAnswMapper.deleteSurvAnswByLoginId(progId, userId);
			}
			
			//최종적으로 설문여부 Y로변경
			progMapper.updateSurvYn(progVO.getProgId(), userId);
			
			returnMap.put("result", result);
			returnMap.put("errCd", errCd);
			return returnMap;
		}

		@Override
		@MethodDescription("설문 결과 조회")
		public Map<String, Object> selectSurveyResult(int progId) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			//프로그램 정보 조회
			ProgVO progVO = progMapper.selectProg(progId);
			
			//설문 VO 생성
			ProgSurvMgtVO progSurvMgtVO = new ProgSurvMgtVO();
			
			if(progSurvMgtVO != null) {			
				
				double totalPoint = 0;	//토탈 점수			
				int totalCount = 0; //응답자수
				
				//응답자 목록 조회
				List<ProgSurvAnswVO> userList = progSurvAnswMapper.selectSurvAnswUserList(progVO.getProgId());
				if(userList != null) {
					totalCount = userList.size();
				}
				
				//백분율 연산을 위한 제수 설정
				int divisor = (totalCount > 0) ? totalCount : 1;
				
				//설문결과 문항목록
				List<ProgSurvQuesVO> quesList = progSurvQuesMapper.selectSurvQuesResult(progVO.getProgId(), divisor);
				if(quesList != null && quesList.size() > 0) {				
					//문항별 항목 조회
					for (int i = 0; i < quesList.size(); i++) {
						totalPoint += quesList.get(i).getAvgPoint();
						quesList.get(i).setItemList(progSurvItemMapper.selectSurvItemResult(progVO.getProgId(), quesList.get(i).getQuesIdx(), divisor));
					}
				}
				
				progSurvMgtVO.setQuesList(quesList);
				
				progSurvMgtVO.setTotalPoint(totalPoint);
				progSurvMgtVO.setTotalCount(totalCount);
				map.put("userList", userList);
			}
			
			map.put("progSurvMgtVO", progSurvMgtVO);
			map.put("progVO", progVO);
			
			return map;
		}

		@Override
		@MethodDescription("설문 기타의견 목록 조회")
		public List<String> selectSurvAnswEtcList(int progId, int quesIdx, int itemIdx) {
			return progSurvAnswMapper.selectSurvAnswEtcList(progId, quesIdx, itemIdx);
		}

		@Override
		public Map<String, Object> selectSurveyResultByUser(int progId, String userId) {
			
			Map<String, Object> map = new HashMap<String, Object>();

			ProgVO progVO = progMapper.selectProg(progId);
			
			ProgSurvMgtVO progSurvMgtVO = new ProgSurvMgtVO();
			
			if(progVO != null) {			
				
				double totalPoint = 0;	//토탈 점수			
				
				//설문문항 조회
				List<ProgSurvQuesVO> quesList = progSurvQuesMapper.selectSurvQuesResultByUser(progVO.getProgId(), userId);
				if(quesList != null && quesList.size() > 0) {				
					//문항별 항목 조회
					for (int i = 0; i < quesList.size(); i++) {
						totalPoint += quesList.get(i).getAvgPoint();
						quesList.get(i).setAnswList(progSurvAnswMapper.selectSurvAnswListByUser(progVO.getProgId(), quesList.get(i).getQuesIdx(), userId));
					}
				}
				
				progSurvMgtVO.setQuesList(quesList);			
				progSurvMgtVO.setTotalPoint(totalPoint);
			}
			
			map.put("progVO", progVO);
			map.put("progSurvMgtVO", progSurvMgtVO);
			
			return map;
		}

		@Override
		public List<ProgSurvMgtVO> selectRecentSurvList() {
			return progSurvMgtMapper.selectRecentSurvList();
		}

		@Override
		@MethodDescription("설문지 생성")
		public int executeSurvList(Map<String, Object> progSurveyMap) {
			//프로시저
			progSurvMgtMapper.executeSurvList(progSurveyMap);
			//result 값
			int result = (int) progSurveyMap.get("result");
			
			return result;
		}
	
		@Override
		@MethodDescription("설문 결과 엑셀다운")
		public void selectSurveyResultForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search, @Param("progId") int progId) {
			
			//맵선언
			Map<String, Object> paramMap = search.convertMap();
			
			int totalCnt = progSurvItemMapper.selectSurvItemTotCntForExcel(progId);
			
			progSurvItemMapper.selectSurvItemResultForExcel(paramMap, handler, progId, totalCnt);
		}
	
}
