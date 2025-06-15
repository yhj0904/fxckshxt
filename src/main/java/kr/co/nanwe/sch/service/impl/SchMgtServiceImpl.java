package kr.co.nanwe.sch.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.sch.service.SchAuthVO;
import kr.co.nanwe.sch.service.SchMgtService;
import kr.co.nanwe.sch.service.SchMgtVO;

/**
 * @Class Name 		: SchMgtServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("schMgtService")
public class SchMgtServiceImpl extends EgovAbstractServiceImpl implements SchMgtService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SchMgtServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "schMapper")
	private SchMapper schMapper;

	@Resource(name = "schMgtMapper")
	private SchMgtMapper schMgtMapper;
	
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
	@MethodDescription("일정관리 목록 조회")
	public Map<String, Object> selectSchMgtList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = schMgtMapper.selectSchMgtTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<SchMgtVO> list = schMgtMapper.selectSchMgtList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("일정관리 상세조회")
	public SchMgtVO selectSchMgt(String code) {
		SchMgtVO schMgtVO = schMgtMapper.selectSchMgt(code);
		if(schMgtVO != null) {
			//액션별 권한 리스트
			schMgtVO.setListAuth(schMgtMapper.selectSchAuthListByAction(code, "LIST"));
			schMgtVO.setViewAuth(schMgtMapper.selectSchAuthListByAction(code, "VIEW"));
			schMgtVO.setRegiAuth(schMgtMapper.selectSchAuthListByAction(code, "REGI"));
		}
		return schMgtVO;
	}

	@Override
	@MethodDescription("일정관리 등록")
	public int insertSchMgt(SchMgtVO schMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		schMgtVO.setInptId(userId);
		schMgtVO.setInptIp(userIp);
		
		schMgtVO.setCode(schMgtVO.getCode().toUpperCase());
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = schMgtMapper.insertSchMgt(schMgtVO);
		
		//등록 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFiles("COM_SCH_MGT", schMgtVO.getCode());
			
			//권한 등록 처리				
			List<String> authCdArr = new ArrayList<String>();
			List<SchAuthVO> schAuthList =  new ArrayList<SchAuthVO>();
			
			//목록권한
			if(listAuth != null) {
				for (String authCd : listAuth) {					
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						schAuthList.get(idx).setListAuth("Y");
					} else {
						SchAuthVO schAuthVO = new SchAuthVO();
						schAuthVO.setSchCd(schMgtVO.getCode());
						schAuthVO.setInptId(userId);
						schAuthVO.setInptIp(userIp);
						schAuthVO.setModiId(userId);
						schAuthVO.setModiIp(userIp);
						schAuthVO.setAuthCd(authCd);
						schAuthVO.setListAuth("Y");						
						authCdArr.add(authCd);
						schAuthList.add(schAuthVO);
					}
				}
			}
			
			//상세보기권한
			if(viewAuth != null) {
				for (String authCd : viewAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						schAuthList.get(idx).setViewAuth("Y");
					} else {
						SchAuthVO schAuthVO = new SchAuthVO();
						schAuthVO.setSchCd(schMgtVO.getCode());
						schAuthVO.setInptId(userId);
						schAuthVO.setInptIp(userIp);
						schAuthVO.setModiId(userId);
						schAuthVO.setModiIp(userIp);
						schAuthVO.setAuthCd(authCd);
						schAuthVO.setViewAuth("Y");						
						authCdArr.add(authCd);
						schAuthList.add(schAuthVO);
					}
				}
			}
			
			//등록권한
			if(regiAuth != null) {
				for (String authCd : regiAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						schAuthList.get(idx).setRegiAuth("Y");
					} else {
						SchAuthVO schAuthVO = new SchAuthVO();
						schAuthVO.setSchCd(schMgtVO.getCode());
						schAuthVO.setInptId(userId);
						schAuthVO.setInptIp(userIp);
						schAuthVO.setModiId(userId);
						schAuthVO.setModiIp(userIp);
						schAuthVO.setAuthCd(authCd);
						schAuthVO.setRegiAuth("Y");						
						authCdArr.add(authCd);
						schAuthList.add(schAuthVO);
					}
				}
			}
			
			if(schAuthList.size() > 0) {
				for(SchAuthVO schAuthVO : schAuthList) {
					schMgtMapper.updateSchAuth(schAuthVO);
				}
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("일정관리 수정")
	public int updateSchMgt(SchMgtVO schMgtVO, String[] listAuth, String[] viewAuth, String[] regiAuth) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		schMgtVO.setModiId(userId);
		schMgtVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = schMgtMapper.updateSchMgt(schMgtVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFiles("COM_SCH_MGT", schMgtVO.getCode());
			
			//권한 초기화
			schMgtMapper.resetSchAuthBySchCd(schMgtVO.getCode());
			
			//권한 등록 처리				
			List<String> authCdArr = new ArrayList<String>();
			List<SchAuthVO> schAuthList =  new ArrayList<SchAuthVO>();
			
			//목록권한
			if(listAuth != null) {
				for (String authCd : listAuth) {					
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						schAuthList.get(idx).setListAuth("Y");
					} else {
						SchAuthVO schAuthVO = new SchAuthVO();
						schAuthVO.setSchCd(schMgtVO.getCode());
						schAuthVO.setInptId(userId);
						schAuthVO.setInptIp(userIp);
						schAuthVO.setModiId(userId);
						schAuthVO.setModiIp(userIp);
						schAuthVO.setAuthCd(authCd);
						schAuthVO.setListAuth("Y");						
						authCdArr.add(authCd);
						schAuthList.add(schAuthVO);
					}
				}
			}
			
			//상세보기권한
			if(viewAuth != null) {
				for (String authCd : viewAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						schAuthList.get(idx).setViewAuth("Y");
					} else {
						SchAuthVO schAuthVO = new SchAuthVO();
						schAuthVO.setSchCd(schMgtVO.getCode());
						schAuthVO.setInptId(userId);
						schAuthVO.setInptIp(userIp);
						schAuthVO.setModiId(userId);
						schAuthVO.setModiIp(userIp);
						schAuthVO.setAuthCd(authCd);
						schAuthVO.setViewAuth("Y");						
						authCdArr.add(authCd);
						schAuthList.add(schAuthVO);
					}
				}
			}
			
			//등록권한
			if(regiAuth != null) {
				for (String authCd : regiAuth) {
					if(authCdArr.contains(authCd)) {
						int idx = authCdArr.indexOf(authCd);
						schAuthList.get(idx).setRegiAuth("Y");
					} else {
						SchAuthVO schAuthVO = new SchAuthVO();
						schAuthVO.setSchCd(schMgtVO.getCode());
						schAuthVO.setInptId(userId);
						schAuthVO.setInptIp(userIp);
						schAuthVO.setModiId(userId);
						schAuthVO.setModiIp(userIp);
						schAuthVO.setAuthCd(authCd);
						schAuthVO.setRegiAuth("Y");						
						authCdArr.add(authCd);
						schAuthList.add(schAuthVO);
					}
				}
			}
			
			if(schAuthList.size() > 0) {
				for(SchAuthVO schAuthVO : schAuthList) {
					schMgtMapper.updateSchAuth(schAuthVO);
				}
			}
			
			//권한 체크 후 없는 경우 삭제
			schMgtMapper.deleteSchAuthByNoAuth(schMgtVO.getCode());
		}
		
		return result;
	}

	@Override
	@MethodDescription("일정관리 삭제")
	public int deleteSchMgt(String code) {
		//삭제 전 VO 객체 조회
		SchMgtVO schMgt = schMgtMapper.selectSchMgt(code);
		int result = schMgtMapper.deleteSchMgt(code);
		//첨부파일 삭제
		if(result > 0) {
			//에디터 이미지
			ckEditorUpload.deleteComEditorFiles("COM_SCH_MGT", schMgt.getCode());
			
			//게시글 삭제
			int schDeleteResult = schMgtMapper.deleteSchBySchCd(code);
			if(schDeleteResult > 0) {
				//게시글 첨부파일삭제
				comFileManager.removeComFileByBbsCd("sch", code);
			}			
			//권한 삭제
			schMgtMapper.deleteSchAuthBySchCd(code);
			
		}
		return result;
	}
	
	@Override
	@MethodDescription("일정코드 중복체크")
	public boolean selectSchMgtCodeExist(String code) {
		
		//아이디가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(code)) {
			return true;
		}
		
		int count = schMgtMapper.selectSchMgtCodeExist(code);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	@MethodDescription("일정권한확인")
	public SchMgtVO selectSchMgtByAuth(String code, String action) {
		
		if(StringUtil.isNull(code) || StringUtil.isNull(action)) {
			return null;
		}
		
		//일정이 존재하지 않으면 null
		SchMgtVO schMgtVO = schMgtMapper.selectSchMgt(code);
		if(schMgtVO == null) {
			return null;
		}
		
		schMgtVO.setAdminUser(false); //일정 관리자
		schMgtVO.setListAuthYn(false); //목록권한
		schMgtVO.setViewAuthYn(false); //상세조회권한
		schMgtVO.setRegiAuthYn(false); //등록권한
		
		//카테고리 설정
		if("Y".equals(schMgtVO.getCateYn()) && !StringUtil.isNull(schMgtVO.getCategory())) {
			schMgtVO.setCategoryList(new ArrayList<String>(Arrays.asList(schMgtVO.getCategory().split(","))));
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
			schMgtVO.setAdminUser(true); //일정 관리자
			schMgtVO.setListAuthYn(true); //목록권한
			schMgtVO.setViewAuthYn(true); //상세조회권한
			schMgtVO.setRegiAuthYn(true); //등록권한
			return schMgtVO;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("schCd", code);
		paramMap.put("authDvcd", authDvcd);
		paramMap.put("userDvcd", userDvcd);
		paramMap.put("workDvcd", workDvcd);
		paramMap.put("statDvcd", statDvcd);
		paramMap.put("deptCd", deptCd);
		
		//권한체크
		SchAuthVO schAuthVO = schMgtMapper.selectSchAuth(paramMap);
		if(schAuthVO == null) {
			return null;
		}
		
		//개인별 권한
		if("Y".equals(schAuthVO.getListAuth())) schMgtVO.setListAuthYn(true); //목록권한
		if("Y".equals(schAuthVO.getViewAuth())) schMgtVO.setViewAuthYn(true); //상세조회권한
		if("Y".equals(schAuthVO.getRegiAuth())) schMgtVO.setRegiAuthYn(true); //등록권한
		
		String authYn = "N";		
		switch (action.toUpperCase()) {
			case "LIST":
				authYn = schAuthVO.getListAuth();
				break;	
			case "VIEW":
				authYn = schAuthVO.getViewAuth();
				break;
			case "REGI":
				authYn = schAuthVO.getRegiAuth();
				break;
			default:
				break;
		}
		
		if("Y".equals(authYn)) {
			return schMgtVO;
		}
		
		return null;
	}
	
}
