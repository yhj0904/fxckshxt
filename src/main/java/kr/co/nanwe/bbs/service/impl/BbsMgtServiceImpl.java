package kr.co.nanwe.bbs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.bbs.service.BbsAuthVO;
import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: BbsMgtServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("bbsMgtService")
public class BbsMgtServiceImpl extends EgovAbstractServiceImpl implements BbsMgtService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BbsMgtServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "bbsMapper")
	private BbsMapper bbsMapper;

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
	@MethodDescription("게시판관리 목록 조회")
	public Map<String, Object> selectBbsMgtList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = bbsMgtMapper.selectBbsMgtTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<BbsMgtVO> list = bbsMgtMapper.selectBbsMgtList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("게시판관리 상세조회")
	public BbsMgtVO selectBbsMgt(String code) {
		BbsMgtVO bbsMgtVO = bbsMgtMapper.selectBbsMgt(code);
		if(bbsMgtVO != null) {
			//액션별 권한 리스트
			bbsMgtVO.setListAuth(bbsMgtMapper.selectBbsAuthListByAction(code, "LIST"));
			bbsMgtVO.setViewAuth(bbsMgtMapper.selectBbsAuthListByAction(code, "VIEW"));
			bbsMgtVO.setRegiAuth(bbsMgtMapper.selectBbsAuthListByAction(code, "REGI"));
			bbsMgtVO.setReplyAuth(bbsMgtMapper.selectBbsAuthListByAction(code, "REPLY"));
			bbsMgtVO.setCmntAuth(bbsMgtMapper.selectBbsAuthListByAction(code, "CMNT"));
		}
		return bbsMgtVO;
	}

	@Override
	@MethodDescription("게시판관리 등록")
	public int insertBbsMgt(BbsMgtVO bbsMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth, String[] replyAuth, String[] cmntAuth) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		bbsMgtVO.setInptId(userId);
		bbsMgtVO.setInptIp(userIp);
		
		bbsMgtVO.setCode(bbsMgtVO.getCode().toUpperCase());
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = bbsMgtMapper.insertBbsMgt(bbsMgtVO);
		
		//등록 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFiles("COM_BBS_MGT", bbsMgtVO.getCode());
			
			//권한 등록 처리				
			List<String> authCdArr = new ArrayList<String>();
			List<BbsAuthVO> bbsAuthList =  new ArrayList<BbsAuthVO>();
			
			//목록권한
			if(listAuth != null) {
				for (String authCd : listAuth) {					
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setListAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setListAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//상세보기권한
			if(viewAuth != null) {
				for (String authCd : viewAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setViewAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setViewAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//등록권한
			if(regiAuth != null) {
				for (String authCd : regiAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setRegiAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setRegiAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//답글권한
			if(replyAuth != null) {
				for (String authCd : replyAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setReplyAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setReplyAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//댓글권한
			if(cmntAuth != null) {
				for (String authCd : cmntAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setCmntAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setCmntAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			if(bbsAuthList.size() > 0) {
				for(BbsAuthVO bbsAuthVO : bbsAuthList) {
					bbsMgtMapper.updateBbsAuth(bbsAuthVO);
				}
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("게시판관리 수정")
	public int updateBbsMgt(BbsMgtVO bbsMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth, String[] replyAuth, String[] cmntAuth) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		bbsMgtVO.setModiId(userId);
		bbsMgtVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = bbsMgtMapper.updateBbsMgt(bbsMgtVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFiles("COM_BBS_MGT", bbsMgtVO.getCode());
			
			//권한 초기화
			bbsMgtMapper.resetBbsAuthByBbsCd(bbsMgtVO.getCode());
			
			//권한 등록 처리				
			List<String> authCdArr = new ArrayList<String>();
			List<BbsAuthVO> bbsAuthList =  new ArrayList<BbsAuthVO>();
			
			//목록권한
			if(listAuth != null) {
				for (String authCd : listAuth) {					
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setListAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setListAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//상세보기권한
			if(viewAuth != null) {
				for (String authCd : viewAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setViewAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setViewAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//등록권한
			if(regiAuth != null) {
				for (String authCd : regiAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setRegiAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setRegiAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//답글권한
			if(replyAuth != null) {
				for (String authCd : replyAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setReplyAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setReplyAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			//댓글권한
			if(cmntAuth != null) {
				for (String authCd : cmntAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						bbsAuthList.get(idx).setCmntAuth("Y");
					} else {
						BbsAuthVO bbsAuthVO = new BbsAuthVO();
						bbsAuthVO.setBbsCd(bbsMgtVO.getCode());
						bbsAuthVO.setInptId(userId);
						bbsAuthVO.setInptIp(userIp);
						bbsAuthVO.setModiId(userId);
						bbsAuthVO.setModiIp(userIp);
						bbsAuthVO.setAuthCd(authCd);
						bbsAuthVO.setCmntAuth("Y");						
						authCdArr.add(authCd);
						bbsAuthList.add(bbsAuthVO);
					}
				}
			}
			
			if(bbsAuthList.size() > 0) {
				for(BbsAuthVO bbsAuthVO : bbsAuthList) {
					bbsMgtMapper.updateBbsAuth(bbsAuthVO);
				}
			}
			
			//권한 체크 후 없는 경우 삭제
			bbsMgtMapper.deleteBbsAuthByNoAuth(bbsMgtVO.getCode());
		}
		
		return result;
	}

	@Override
	@MethodDescription("게시판관리 삭제")
	public int deleteBbsMgt(String code) {
		//삭제 전 VO 객체 조회
		BbsMgtVO bbsMgt = bbsMgtMapper.selectBbsMgt(code);
		int result = bbsMgtMapper.deleteBbsMgt(code);
		//첨부파일 삭제
		if(result > 0) {
			//에디터 이미지
			ckEditorUpload.deleteComEditorFiles("COM_BBS_MGT", bbsMgt.getCode());
			
			//게시글 삭제
			int bbsDeleteResult = bbsMgtMapper.deleteBbsByBbsCd(code);
			if(bbsDeleteResult > 0) {
				//게시글 첨부파일삭제
				comFileManager.removeComFileByBbsCd("bbs", code);
			}
			
			//권한 삭제
			bbsMgtMapper.deleteBbsAuthByBbsCd(code);
			
			//댓글 삭제
			bbsCmntMapper.deleteBbsCmntByBbsCd(code);
			
		}
		return result;
	}
	
	@Override
	@MethodDescription("게시판코드 중복체크")
	public boolean selectBbsMgtCodeExist(String code) {
		
		//아이디가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(code)) {
			return true;
		}
		
		int count = bbsMgtMapper.selectBbsMgtCodeExist(code);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("게시판권한확인")
	public BbsMgtVO selectBbsMgtByAuth(String code, String action) {
		
		if(StringUtil.isNull(code) || StringUtil.isNull(action)) {
			return null;
		}
		
		//게시판이 존재하지 않으면 null
		BbsMgtVO bbsMgtVO = bbsMgtMapper.selectBbsMgt(code);
		if(bbsMgtVO == null) {
			return null;
		}
		
		bbsMgtVO.setAdminUser(false); //게시판 관리자
		bbsMgtVO.setListAuthYn(false); //목록권한
		bbsMgtVO.setViewAuthYn(false); //상세조회권한
		bbsMgtVO.setRegiAuthYn(false); //등록권한
		bbsMgtVO.setReplyAuthYn(false); //답글권한
		bbsMgtVO.setCmntAuthYn(false); //댓글권한
		
		//카테고리 설정
		if("Y".equals(bbsMgtVO.getCateYn()) && !StringUtil.isNull(bbsMgtVO.getCategory())) {
			bbsMgtVO.setCategoryList(new ArrayList<String>(Arrays.asList(bbsMgtVO.getCategory().split(","))));
		}
		
		String authDvcd = "";
		String userDvcd = "";
		String workDvcd = "";
		String statDvcd = "";
		String deptCd = "";
		
		//로그인 중인지 체크
		if(SessionUtil.getLoginUser() != null) {	
			LoginVO loginVO = SessionUtil.getLoginUser();			
			authDvcd = StringUtil.isNullToString(loginVO.getLoginUserType());
			userDvcd = StringUtil.isNullToString(loginVO.getUserDvcd());
			workDvcd = StringUtil.isNullToString(loginVO.getWorkDvcd());
			statDvcd = StringUtil.isNullToString(loginVO.getStatDvcd());
			deptCd = StringUtil.isNullToString(loginVO.getDeptCd());
		} else {
			authDvcd = CodeConfig.NO_LOGIN;
			userDvcd = CodeConfig.NO_LOGIN;
			workDvcd = CodeConfig.NO_LOGIN;
			statDvcd = CodeConfig.NO_LOGIN;
		}
		
		//관리자이면 true
		if(SessionUtil.isAdmin()) {
			bbsMgtVO.setAdminUser(true); //게시판 관리자
			bbsMgtVO.setListAuthYn(true); //목록권한
			bbsMgtVO.setViewAuthYn(true); //상세조회권한
			bbsMgtVO.setRegiAuthYn(true); //등록권한
			bbsMgtVO.setReplyAuthYn(true); //답글권한
			bbsMgtVO.setCmntAuthYn(true); //댓글권한
			return bbsMgtVO;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("bbsCd", code);
		paramMap.put("authDvcd", authDvcd);
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		paramMap.put("statDvcd", statDvcd);
		paramMap.put("deptCd", deptCd);
		
		//권한체크
		BbsAuthVO bbsAuthVO = bbsMgtMapper.selectBbsAuth(paramMap);
		if(bbsAuthVO == null) {
			return null;
		}
		
		//개인별 권한
		if("Y".equals(bbsAuthVO.getListAuth())) bbsMgtVO.setListAuthYn(true); //목록권한
		if("Y".equals(bbsAuthVO.getViewAuth())) bbsMgtVO.setViewAuthYn(true); //상세조회권한
		if("Y".equals(bbsAuthVO.getRegiAuth())) bbsMgtVO.setRegiAuthYn(true); //등록권한
		if("Y".equals(bbsMgtVO.getReplyYn()) && "Y".equals(bbsAuthVO.getReplyAuth())) bbsMgtVO.setReplyAuthYn(true); //답글권한
		if("Y".equals(bbsMgtVO.getCmntYn()) && "Y".equals(bbsAuthVO.getCmntAuth())) bbsMgtVO.setCmntAuthYn(true); //댓글권한
		
		String authYn = "N";		
		switch (action.toUpperCase()) {
			case "LIST":
				authYn = bbsAuthVO.getListAuth();
				break;	
			case "VIEW":
				authYn = bbsAuthVO.getViewAuth();
				break;
			case "REGI":
				authYn = bbsAuthVO.getRegiAuth();
				return bbsMgtVO;
				//break;
			case "REPLY":				
				if(!"Y".equals(bbsMgtVO.getReplyYn())) {
					authYn = "N";
				} else {
					authYn = bbsAuthVO.getReplyAuth();
				}	
				break;
			case "CMNT":
				if(!"Y".equals(bbsMgtVO.getCmntYn())) {
					authYn = "N";
				} else {
					authYn = bbsAuthVO.getCmntAuth();
				}
				break;
			default:
				break;
		}
		
		if("Y".equals(authYn)) {
			return bbsMgtVO;
		}
		
		return null;
	}
	
}
