package kr.co.nanwe.bbs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.bbs.service.LaborVO;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.MaskingUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: BbsServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("bbsService")
public class BbsServiceImpl extends EgovAbstractServiceImpl implements BbsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BbsServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "bbsMapper")
	private BbsMapper bbsMapper;

	@Resource(name = "labrMapper")
	private LabrMapper labrMapper;
	
	@Resource(name = "bbsMgtMapper")
	private BbsMgtMapper bbsMgtMapper;

	@Resource(name = "bbsCmntMapper")
	private BbsCmntMapper bbsCmntMapper;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	/** 파일업로드 */
	@Resource(name = "fileUpload")
	private FileUpload fileUpload;
	
	/** 웹에디터 */
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;

	@Override
	@MethodDescription("게시글 목록 조회")
	public Map<String, Object> selectBbsList(BbsMgtVO bbsMgtVO, SearchVO search, String category) {
		
		//검색조건 설정		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("bbsCd", bbsMgtVO.getCode());
		
		//카테고리 체크
		if(!StringUtil.isNull(category) && !"ALL".equals(category)) {
			paramMap.put("category", category);
		}
		
		//전체 ROW COUNT
		int totCnt = bbsMapper.selectBbsTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), bbsMgtVO.getRowCnt(), bbsMgtVO.getPageCnt(), totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//익명글 체크
		if(!StringUtil.isNull(bbsMgtVO.getNonameYn()) && !"ALL".equals(bbsMgtVO.getNonameYn())) {
			paramMap.put("nonameYn", bbsMgtVO.getNonameYn());
		}
		
		//목록 조회
		List<BbsVO> list = bbsMapper.selectBbsList(paramMap);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				//첨부파일 목록 조회
				String thumbnail = comFileManager.getThumbnail("COM_BBS", list.get(i).getBbsId());
				list.get(i).setThumbnail(thumbnail);
			}
		}
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("게시글 상세조회")
	public BbsVO selectBbs(BbsMgtVO bbsMgtVO, String bbsId) {
		BbsVO bbsVO = bbsMapper.selectBbs(bbsMgtVO.getCode(), bbsId);
		if(bbsVO != null) {
			//첨부파일 목록 조회
			List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_BBS", bbsVO.getBbsId());
			bbsVO.setViewFiles(viewFiles);
			
			//비밀번호 초기화
			bbsVO.setPw(null);
		}
		return bbsVO;
	}

	@Override
	@MethodDescription("게시글 등록")
	public int insertBbs(BbsMgtVO bbsMgtVO, BbsVO bbsVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		String userNm = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
			userNm = loginInfo.getLoginNm();
	
			//로그인된 경우 작성자 이름 설정
			bbsVO.setWriter(userNm);
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		bbsVO.setInptId(userId);
		bbsVO.setInptIp(userIp);
		
		//에디터 미사용시 줄바꿈 치환
		if("N".equals(bbsMgtVO.getEditorYn())) {
			bbsVO.setContents(HtmlConvertorUtil.changeLineSeparatorToBr(bbsVO.getContents()));
		}
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = bbsMapper.insertBbs(bbsVO);
		
		//등록 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFilesByBbs("bbs", bbsVO.getBbsCd(), bbsVO.getBbsId());
			
			//첨부파일 업로드
			List<MultipartFile> uploadList = bbsVO.getUploadFiles();
			if(uploadList != null && uploadList.size() > 0) {
				for (MultipartFile uploadFile : uploadList) {
					//첨부파일 업로드
					comFileManager.uploadComFileByBbs(uploadFile, "bbs", bbsVO.getBbsCd(), bbsVO.getBbsId());
				}
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("게시글 수정")
	public int updateBbs(BbsMgtVO bbsMgtVO, BbsVO bbsVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		bbsVO.setModiId(userId);
		bbsVO.setModiIp(userIp);
		
		//에디터 미사용시 줄바꿈 치환
		if("N".equals(bbsMgtVO.getEditorYn())) {
			bbsVO.setContents(HtmlConvertorUtil.changeLineSeparatorToBr(bbsVO.getContents()));
		}
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = bbsMapper.updateBbs(bbsVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFilesByBbs("bbs", bbsVO.getBbsCd(), bbsVO.getBbsId());

			//파일목록 체크
			List<MultipartFile> uploadList = bbsVO.getUploadFiles();
			if(uploadList != null && uploadList.size() > 0) {
				for (MultipartFile uploadFile : uploadList) {
					comFileManager.uploadComFileByBbs(uploadFile, "bbs", bbsVO.getBbsCd(), bbsVO.getBbsId());
				}
			}
			
			//기등록된 파일 목록중 삭제된 목록을 체크후 제거
			comFileManager.removeComFileByViewList("COM_BBS", bbsVO.getBbsId(), bbsVO.getViewFiles());
			
		}
		
		return result;
	}

	@Override
	@MethodDescription("게시글 삭제")
	public int deleteBbs(BbsMgtVO bbsMgtVO, String bbsId) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//삭제 전 VO 객체 조회
		BbsVO bbs = bbsMapper.selectBbs(bbsMgtVO.getCode(), bbsId);
		if(bbs == null) {
			return 0;
		}
		
		int result = 0;
		
		//삭제되지 않은 답글이 있는지 체크
		int childCnt = bbsMapper.selectChildBbsCnt(bbsMgtVO.getCode(), bbsId);
		if(childCnt > 0) {
			bbs.setDelId(userId);
			bbs.setDelIp(userIp);
			//부모글 보존을 위해 상태만 삭제상태로 변경한다.
			result = bbsMapper.updateBbsToDelete(bbs);
		} else {
			result = bbsMapper.deleteBbs(bbsMgtVO.getCode(), bbsId);
			//삭제상태인 부모게시글을 조회 후 모두 삭제한다.
			if(result > 0) {
				bbsMapper.deleteBbsByGroupId(bbsMgtVO.getCode(), bbs.getGroupId());
			}
		}
		
		//첨부파일 삭제
		if(result > 0) {
			//에디터 이미지
			ckEditorUpload.deleteComEditorFiles("COM_BBS", bbs.getBbsId());
			//첨부파일
			comFileManager.removeComFileByParent("COM_BBS", bbsId);
			//댓글 삭제
			bbsCmntMapper.deleteBbsCmntByBbsId(bbsMgtVO.getCode(), bbsId);
		}
		return result;
	}

	@Override
	@MethodDescription("게시글 선택 삭제")
	public int deleteCheckedBbs(BbsMgtVO bbsMgtVO, String checkedSId) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			//삭제 전 VO 객체 조회
			BbsVO bbs = bbsMapper.selectBbs(bbsMgtVO.getCode(), id);
			if(bbs == null) {
				continue;
			}
			
			int deleteResult = 0;
			
			//답글이 있는지 체크
			int childCnt = bbsMapper.selectChildBbsCnt(bbsMgtVO.getCode(), id);
			if(childCnt > 0) {
				bbs.setDelId(userId);
				bbs.setDelIp(userIp);
				deleteResult = bbsMapper.updateBbsToDelete(bbs);
			} else {
				deleteResult = bbsMapper.deleteBbs(bbsMgtVO.getCode(), id);
				//삭제상태인 그룹게시글을 조회 후 모두 삭제한다.
				if(result > 0) {
					bbsMapper.deleteBbsByGroupId(bbsMgtVO.getCode(), bbs.getGroupId());
				}
				
			}
			
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//에디터 이미지
				ckEditorUpload.deleteComEditorFiles("COM_BBS", bbs.getBbsId());
				//첨부파일
				comFileManager.removeComFileByParent("COM_BBS", id);
				//댓글 삭제
				bbsCmntMapper.deleteBbsCmntByBbsId(bbsMgtVO.getCode(), bbs.getBbsId());
			}
		}
		return result;
	}

	@Override
	public int updateBbsViewCnt(String bbsCd, String bbsId) {
		return bbsMapper.updateBbsViewCnt(bbsCd, bbsId);
	}

	@Override
	@MethodDescription("공지사항 목록 조회")
	public List<BbsVO> selectBbsNoticeList(BbsMgtVO bbsMgtVO) {		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("bbsCd", bbsMgtVO.getCode());
		paramMap.put("beginRow", 0);
		paramMap.put("pagePerRow", bbsMgtVO.getNoticeRowCnt());
		
		//익명글 체크
		if(!StringUtil.isNull(bbsMgtVO.getNonameYn()) && !"ALL".equals(bbsMgtVO.getNonameYn())) {
			paramMap.put("nonameYn", bbsMgtVO.getNonameYn());
		}
		return bbsMapper.selectBbsNoticeList(paramMap);
	}

	@Override
	@MethodDescription("이전글 다음글 조회")
	public Map<String, Object> selectNearBbs(BbsMgtVO bbsMgtVO, BbsVO bbsVO, String category) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("bbsCd", bbsVO.getBbsCd());
		paramMap.put("bbsId", bbsVO.getBbsId());	
		paramMap.put("bbsSeq", bbsVO.getBbsSeq());	
		
		//카테고리 체크
		if(!StringUtil.isNull(category) && !"ALL".equals(category)) {
			paramMap.put("category", category);
		}
		
		//익명글 체크
		if(!StringUtil.isNull(bbsMgtVO.getNonameYn()) && !"ALL".equals(bbsMgtVO.getNonameYn())) {
			paramMap.put("nonameYn", bbsMgtVO.getNonameYn());
		}
		
		BbsVO prevBbs = bbsMapper.selectPrevBbs(paramMap);
		BbsVO nextBbs = bbsMapper.selectNextBbs(paramMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prevBbs", prevBbs);
		map.put("nextBbs", nextBbs);
		
		return map;
	}

	@Override
	public Map<String, Object> selectBbsByAuth(BbsMgtVO bbsMgtVO, String bbsId, String action, String bbsPw) {
		
		action = action.toUpperCase();
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		String errMsgCd = "message.error.deleteboard"; //삭제게시물 메시지 코드
		
		//게시글 조회
		BbsVO bbsVO = bbsMapper.selectBbs(bbsMgtVO.getCode(), bbsId);
		
		if(bbsVO != null && !"Y".equals(bbsVO.getDelYn())) {

			if(StringUtil.isNull(bbsVO.getPw())) {
				bbsVO.setPw("NO_PW");
			}
			
			errMsgCd = "message.error.auth"; //권한없음
			
			//현재 로그인 정보
			String loginId = "";
			LoginVO loginVO = SessionUtil.getLoginUser();
			if(loginVO != null) {
				loginId = loginVO.getLoginId();
			}
			
			if(bbsMgtVO.isAdminUser()) { //관리자이면 true
				result = true;
			} else if(!StringUtil.isNull(bbsVO.getInptId()) && loginId.equals(bbsVO.getInptId())) { //내가쓴글이면 true
				result = true;
			} else if("VIEW".equals(action)) { //상세조회인 경우
				if(!"Y".equals(bbsVO.getSecret())) { //비밀글이 아닌 경우
					result = true;
				} else { //비밀글인 경우
					//먼저 비회원 작성글이면서 비밀번호가 맞는지 체크
//					if(loginVO == null && StringUtil.isNull(bbsVO.getInptId())){
					if(loginVO == null){
						if(StringUtil.isEqual(bbsVO.getPw(), bbsPw)) {
							result = true;
						} else {
							errMsgCd = CodeConfig.NO_LOGIN; //에러코드를 비회원으로 하여 비밀번호 검증
						}
					}		
					//비회원 작성글이 아니면서 부모글이 있는 경우
//					if(!result && !StringUtil.isNull(bbsVO.getParentId())) {
//						BbsVO groupBbsVO = bbsMapper.selectBbs(bbsMgtVO.getCode(), bbsVO.getGroupId());
//						if(groupBbsVO != null) {
//							if(!StringUtil.isNull(groupBbsVO.getInptId()) && loginId.equals(groupBbsVO.getInptId())) { //상위 게시글이 내가쓴글이면
//								result = true;
//							} else if(loginVO == null && StringUtil.isNull(groupBbsVO.getInptId())){ //상위게시글이 비회원 작성글이며 비로그인인 경우
//								if(groupBbsVO.getPw() != null && bbsPw != null && StringUtil.isEqual(groupBbsVO.getPw(), bbsPw)) {
//									result = true;
//								} else {
//									errMsgCd = CodeConfig.NO_LOGIN; //에러코드를 비회원으로 하여 비밀번호 검증
//								}
//							}
//						}
//					}
				}		
			} else if(loginVO == null && StringUtil.isNull(bbsVO.getInptId())){ //비회원이면서 비회원 작성글인 경우
				if(StringUtil.isEqual(bbsVO.getPw(), bbsPw)) {
					result = true;
				} else {
					errMsgCd = CodeConfig.NO_LOGIN; //에러코드를 비회원으로 하여 비밀번호 검증
				}
			}
			
			if(result) {
				//첨부파일 목록 조회
				List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_BBS", bbsVO.getBbsId());
				bbsVO.setViewFiles(viewFiles);
				
				//비밀번호 초기화
				bbsVO.setPw(null);
				map.put("bbsVO", bbsVO);
			}
		}
		
		map.put("result", result);
		map.put("errMsgCd", errMsgCd);
		
		return map;
	}

	@Override
	public List<BbsVO> selectRecentBbsList() {
		return bbsMapper.selectRecentBbsList();
	}

	@Override
	public List<BbsVO> selectRecentBbsListByParam(Map<String, Object> param) {
		if(param == null) {
			return null;
		}
		if(param.get("bbsCd") == null) {
			return null;
		}
		List<BbsVO> bbsList = bbsMapper.selectRecentBbsListByParam(param);
		if(bbsList != null) {
			for (int i = 0; i < bbsList.size(); i++) {
				String contents = bbsList.get(i).getContents();
				if(!StringUtil.isNull(contents)) {
					contents = HtmlConvertorUtil.removeLineSeparator(contents);
					contents = HtmlConvertorUtil.changeBrToLineSeparator(contents);
					contents = HtmlConvertorUtil.removeTagWithoutWhiteSpace(contents);
					contents = HtmlConvertorUtil.changeLineSeparatorToBr(contents);
					//html 태그 제거
					bbsList.get(i).setContents(contents);
				}
				
				//첨부파일
				List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_BBS", bbsList.get(i).getBbsId());
				if(viewFiles != null && !viewFiles.isEmpty()) {
					bbsList.get(i).setViewFiles(viewFiles);	
				}
			}
		}
		return bbsList;
	}
	
	@Override
	@MethodDescription("wk인력풀 등록")
	public int insertLabor(BbsMgtVO bbsMgtVO, LaborVO laborVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		String userNm = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
			userNm = loginInfo.getLoginNm();
	
			//로그인된 경우 작성자 이름 설정
			//bbsVO.setWriter(userNm);
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		laborVO.setInptId(userId);
		laborVO.setInptIp(userIp);
		
		//에디터 미사용시 줄바꿈 치환
		if("N".equals(bbsMgtVO.getEditorYn())) {
			laborVO.setLicense(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getLicense()));
			
			laborVO.setLangScore(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getLangScore()));
			laborVO.setExtAct(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getExtAct()));
			laborVO.setWhCompany(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getWhCompany()));
			laborVO.setWhQuestions(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getWhQuestions()));
			laborVO.setWhProgram02(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getWhProgram02()));
		}
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = labrMapper.insertLabor(laborVO);
		
		//등록 결과 확인
		//if(result > 0) {
		//	
		//	//에디터 이미지 체크 후 변경
		//	ckEditorUpload.updateComEditorFilesByBbs("bbs", bbsVO.getBbsCd(), bbsVO.getBbsId());
		//	
		//	//첨부파일 업로드
		//	List<MultipartFile> uploadList = bbsVO.getUploadFiles();
		//	if(uploadList != null && uploadList.size() > 0) {
		//		for (MultipartFile uploadFile : uploadList) {
		//			//첨부파일 업로드
		//			comFileManager.uploadComFileByBbs(uploadFile, "bbs", bbsVO.getBbsCd(), bbsVO.getBbsId());
		//		}
		//	}
		//}
		
		return result;
	}
	
	@Override
	@MethodDescription("wk인력풀 상세조회")
	public LaborVO selectLabr(BbsMgtVO bbsMgtVO, String labrId) {
		LaborVO laborVO = labrMapper.selectLabr(labrId);
		
		if(laborVO != null) {
			if(laborVO.getWhLocal() != null) {
				laborVO.setWhLocalList(new ArrayList<String>(Arrays.asList(laborVO.getWhLocal().split(","))));
			}
			
			if(laborVO.getWhProgram01() != null) {
				laborVO.setWhProgList01(new ArrayList<String>(Arrays.asList(laborVO.getWhProgram01().split(","))));
			}
		}
		
		return laborVO;
	}
	
	@Override
	public Map<String, Object> selectLabrByAuth(BbsMgtVO bbsMgtVO, String labrId, String action, String pw) {
		
		action = action.toUpperCase();
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		String errMsgCd = "message.error.deleteboard"; //삭제게시물 메시지 코드
		
		//게시글 조회
		LaborVO laborVO = labrMapper.selectLabr(labrId);
		
		if(laborVO != null && !"Y".equals(laborVO.getDelYn())) {

			if(StringUtil.isNull(laborVO.getPw())) {
				laborVO.setPw("NO_PW");
			}
			
			errMsgCd = "message.error.auth"; //권한없음
			
			//현재 로그인 정보
			String loginId = "";
			LoginVO loginVO = SessionUtil.getLoginUser();
			if(loginVO != null) {
				loginId = loginVO.getLoginId();
			}
			
			if(bbsMgtVO.isAdminUser()) { //관리자이면 true
				result = true;
			} else if(!StringUtil.isNull(laborVO.getInptId()) && loginId.equals(laborVO.getInptId())) { //내가쓴글이면 true
				result = true;
			} else if("VIEW".equals(action)) { //상세조회인 경우
				if(!"Y".equals(laborVO.getSecret())) { //비밀글이 아닌 경우
					result = true;
				} else { //비밀글인 경우
					//먼저 비회원 작성글이면서 비밀번호가 맞는지 체크
					if (loginVO == null) {
						if (StringUtil.isEqual(laborVO.getPw(), pw)) {
							result = true;
						} else {
							errMsgCd = CodeConfig.NO_LOGIN; //에러코드를 비회원으로 하여 비밀번호 검증
						}
					}
				}
			}
			// 비회원 수정, 삭제 권한 추가
			else if ("MODI".equals(action)) {
				result = true;
			} else if ("REMOVE".equals(action)) {
				result = true;
			}
			
			if(result) {
				if(laborVO != null) {
					if(laborVO.getWhLocal() != null) {
						laborVO.setWhLocalList(new ArrayList<String>(Arrays.asList(laborVO.getWhLocal().split(","))));
					}
					
					if(laborVO.getWhProgram01() != null) {
						laborVO.setWhProgList01(new ArrayList<String>(Arrays.asList(laborVO.getWhProgram01().split(","))));
					}
					
				}
				map.put("laborVO", laborVO);
			}
		}
		
		map.put("result", result);
		map.put("errMsgCd", errMsgCd);
		
		return map;
	}
	
	@Override
	@MethodDescription("게시글 수정")
	public int updateLabr(BbsMgtVO bbsMgtVO, LaborVO laborVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		laborVO.setModiId(userId);
		laborVO.setModiIp(userIp);
		
		//에디터 미사용시 줄바꿈 치환
		if("N".equals(bbsMgtVO.getEditorYn())) {
			laborVO.setLicense(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getLicense()));
			
			laborVO.setLangScore(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getLangScore()));
			laborVO.setExtAct(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getExtAct()));
			laborVO.setWhCompany(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getWhCompany()));
			laborVO.setWhQuestions(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getWhQuestions()));
			laborVO.setWhProgram02(HtmlConvertorUtil.changeLineSeparatorToBr(laborVO.getWhProgram02()));
		}
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = labrMapper.updateLabr(laborVO);
		
		return result;
	}

	@Override
	@MethodDescription("wk인력풀 삭제")
	public int deleteLabr(BbsMgtVO bbsMgtVO, String labrId) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//게시글 조회
		LaborVO labor = labrMapper.selectLabr(labrId);
		if(labor == null) {
			return 0;
		}
		
		int result = 0;
		
		//삭제되지 않은 답글이 있는지 체크
		//int childCnt = bbsMapper.selectChildBbsCnt(bbsMgtVO.getCode(), bbsId);
		//if(childCnt > 0) {
		//	bbs.setDelId(userId);
		//	bbs.setDelIp(userIp);
		//	//부모글 보존을 위해 상태만 삭제상태로 변경한다.
		//	result = bbsMapper.updateBbsToDelete(bbs);
		//} else {
			result = labrMapper.deleteLabr(labrId);
		//	//삭제상태인 부모게시글을 조회 후 모두 삭제한다.
		//	if(result > 0) {
		//		bbsMapper.deleteBbsByGroupId(bbsMgtVO.getCode(), bbs.getGroupId());
		//	}
		//}
		
		//첨부파일 삭제
		//if(result > 0) {
		//	//에디터 이미지
		//	ckEditorUpload.deleteComEditorFiles("COM_BBS", bbs.getBbsId());
		//	//첨부파일
		//	comFileManager.removeComFileByParent("COM_BBS", bbsId);
		//	//댓글 삭제
		//	bbsCmntMapper.deleteBbsCmntByBbsId(bbsMgtVO.getCode(), bbsId);
		//}
		return result;
	}
	
	@Override
	@MethodDescription("wk인력풀 선택 삭제")
	public int deleteCheckedLabr(BbsMgtVO bbsMgtVO, String checkedSId) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			//삭제 전 VO 객체 조회
			LaborVO labor = labrMapper.selectLabr(id);
			if(labor == null) {
				continue;
			}
			
			int deleteResult = 0;
			
			//답글이 있는지 체크
			//int childCnt = bbsMapper.selectChildBbsCnt(bbsMgtVO.getCode(), id);
			//if(childCnt > 0) {
			//	bbs.setDelId(userId);
			//	bbs.setDelIp(userIp);
			//	deleteResult = bbsMapper.updateBbsToDelete(bbs);
			//} else {
				deleteResult = labrMapper.deleteLabr(id);
				//삭제상태인 그룹게시글을 조회 후 모두 삭제한다.
				//if(result > 0) {
				//	bbsMapper.deleteBbsByGroupId(bbsMgtVO.getCode(), bbs.getGroupId());
				//}
				
			//}
			
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//에디터 이미지
				//ckEditorUpload.deleteComEditorFiles("COM_BBS", bbs.getBbsId());
				//첨부파일
				//comFileManager.removeComFileByParent("COM_BBS", id);
				//댓글 삭제
				//bbsCmntMapper.deleteBbsCmntByBbsId(bbsMgtVO.getCode(), bbs.getBbsId());
			}
		}
		return result;
	}

	@Override
	@MethodDescription("게시글 목록 조회")
	public Map<String, Object> selectLaborList(BbsMgtVO bbsMgtVO, SearchVO search, String category) {
		
		//검색조건 설정		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//카테고리 체크
		if(!StringUtil.isNull(category) && !"ALL".equals(category)) {
			paramMap.put("category", category);
		}
		
		//전체 ROW COUNT
		int totCnt = labrMapper.selectLaborTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), bbsMgtVO.getRowCnt(), bbsMgtVO.getPageCnt(), totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//익명글 체크
		//if(!StringUtil.isNull(bbsMgtVO.getNonameYn()) && !"ALL".equals(bbsMgtVO.getNonameYn())) {
		//	paramMap.put("nonameYn", bbsMgtVO.getNonameYn());
		//}
		
		//목록 조회
		List<LaborVO> list = labrMapper.selectLaborList(paramMap);
//		마스킹처리로직
//		if(list != null && list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				//이름 마스킹 처리
//				if(!bbsMgtVO.isAdminUser()) {
//					list.get(i).setUserNm(MaskingUtil.getMaskedName(list.get(i).getUserNm()));;
//				}
//			}
//		}
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	@Override
	@MethodDescription("사용자 선택 excel 다운로드")
	public void selectLaborListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {
		
		//맵선언
		Map<String, Object> paramMap = search.convertMap();
		
		labrMapper.selectLaborListForExcel(paramMap, handler);
	}

	@Override
	@MethodDescription("사용자 전체 excel 다운로드")
	public void selectLaborListForExcelAll(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {

		//맵선언
		Map<String, Object> paramMap = search.convertMap();

		labrMapper.selectLaborListForExcelAll(paramMap, handler);
	}

}
