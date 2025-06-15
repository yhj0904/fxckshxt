package kr.co.nanwe.sch.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.CalendarUtil;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.sch.service.SchMgtVO;
import kr.co.nanwe.sch.service.SchService;
import kr.co.nanwe.sch.service.SchVO;

/**
 * @Class Name 		: SchServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.23		신한나			VIEW 페이지 조회 수정
 */

@Service("schService")
public class SchServiceImpl extends EgovAbstractServiceImpl implements SchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SchServiceImpl.class);
	
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
	@MethodDescription("일정 목록 조회")
	public Map<String, Object> selectSchList(SchMgtVO schMgtVO, SearchVO search, String category) {
		
		//검색조건 설정		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("schCd", schMgtVO.getCode());
		
		//카테고리 체크
		if(!StringUtil.isNull(category) && !"ALL".equals(category)) {
			paramMap.put("category", category);
		}
		
		//전체 ROW COUNT
		int totCnt = schMapper.selectSchTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<SchVO> list = schMapper.selectSchList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	@MethodDescription("일정 목록 조회")
	public Map<String, Object> selectSchCalendar(SchMgtVO schMgtVO, SearchVO search, String category, String date, String option) {
		
		//검색조건 설정		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("schCd", schMgtVO.getCode());
		
		//카테고리 체크
		if(!StringUtil.isNull(category) && !"ALL".equals(category)) {
			paramMap.put("category", category);
		}
		
		//달력 유형 (default 년도별)
		Map<String, Object> calendar = new HashMap<String, Object>();
		String typeCd = schMgtVO.getTypeCd();
		if(typeCd != null) {
			typeCd = typeCd.toLowerCase();
		}
		
		switch (typeCd) {
			case "year":
				calendar = CalendarUtil.getYear(date, option);
				break;				
			case "month":
				calendar = CalendarUtil.getMonth(date, option);
				break;				
			case "week":
				calendar = CalendarUtil.getWeek(date, option);
				break;				
			case "day":
				calendar = CalendarUtil.getDay(date, option);
				break;
			default:
				typeCd = "list";
				break;
		}
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();
		if("list".equals(typeCd)) {
			//전체 ROW COUNT
			int totCnt = schMapper.selectSchTotCnt(paramMap);
			
			//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
			Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
			
			//시작 행과 페이지당 ROW수 조건 추가
			search.setCp(Integer.toString(paging.getCurrentPage()));
			paramMap.put("beginRow", paging.getBeginRow());
			paramMap.put("pagePerRow", paging.getPagePerRow());
			
			//목록 조회
			List<SchVO> list = schMapper.selectSchList(paramMap);
			
			map.put("paging", paging);
			map.put("search", search);
			map.put("list", list);
		} else {
			paramMap.put("typeCd", typeCd);
			if(calendar.get("list") != null) {
				List<Map<String, Object>> list = (List<Map<String, Object>>)calendar.get("list");
				for (int i=0; i<list.size(); i++) {
					if(StringUtil.isEqual(schMgtVO.getCode().toLowerCase(), "sch01")){
						
						List<SchVO> schList = schMapper.selectProgSchListByCalendar(paramMap, list.get(i));
						list.get(i).put("schList", schList);
					} else {
						List<SchVO> schList = schMapper.selectSchListByCalendar(paramMap, list.get(i));
						list.get(i).put("schList", schList);
					}
				}
			} else {
				List<SchVO> schList = schMapper.selectSchListByCalendar(paramMap, calendar);
				calendar.put("schList", schList);	
			}
			map.put("calendar", calendar);
			map.put("sDate", calendar.get("sDate"));
		}
		
		return map;
	}

	@Override
	@MethodDescription("일정 상세조회")
	public SchVO selectSch(SchMgtVO schMgtVO, String schId) {
		SchVO schVO = schMapper.selectSch(schMgtVO.getCode(), schId);
		if(schVO != null) {
			//첨부파일 목록 조회
			List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_SCH", schVO.getSchId());
			schVO.setViewFiles(viewFiles);
		}
		return schVO;
	}

	@Override
	@MethodDescription("일정 등록")
	public int insertSch(SchMgtVO schMgtVO, SchVO schVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		String userNm = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
			userNm = loginInfo.getLoginNm();
	
			//로그인된 경우 작성자 이름 설정
			schVO.setWriter(userNm);
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		schVO.setInptId(userId);
		schVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = schMapper.insertSch(schVO);
		
		//등록 결과 확인
		if(result > 0) {			
			//첨부파일 업로드
			List<MultipartFile> uploadList = schVO.getUploadFiles();
			if(uploadList != null && uploadList.size() > 0) {
				for (MultipartFile uploadFile : uploadList) {
					//첨부파일 업로드
					comFileManager.uploadComFileByBbs(uploadFile, "sch", schVO.getSchCd(), schVO.getSchId());
				}
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("일정 수정")
	public int updateSch(SchMgtVO schMgtVO, SchVO schVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		schVO.setModiId(userId);
		schVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = schMapper.updateSch(schVO);
		
		//수정 결과 확인
		if(result > 0) {
			//파일목록 체크
			List<MultipartFile> uploadList = schVO.getUploadFiles();
			if(uploadList != null && uploadList.size() > 0) {
				for (MultipartFile uploadFile : uploadList) {
					comFileManager.uploadComFileByBbs(uploadFile, "sch", schVO.getSchCd(), schVO.getSchId());
				}
			}
			
			//기등록된 파일 목록중 삭제된 목록을 체크후 제거
			comFileManager.removeComFileByViewList("COM_SCH", schVO.getSchId(), schVO.getViewFiles());
			
		}		
		return result;
	}

	@Override
	@MethodDescription("일정 삭제")
	public int deleteSch(SchMgtVO schMgtVO, String schId) {		
		//삭제 전 VO 객체 조회
		SchVO schVO = schMapper.selectSch(schMgtVO.getCode(), schId);
		if(schVO == null) {
			return 0;
		}
		
		int result = schMapper.deleteSch(schMgtVO.getCode(), schId);		
		//첨부파일 삭제
		if(result > 0) {
			//첨부파일
			comFileManager.removeComFileByParent("COM_SCH", schId);
		}
		return result;
	}

	@Override
	@MethodDescription("일정 선택 삭제")
	public int deleteCheckedSch(SchMgtVO schMgtVO, String checkedSId) {		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			
			String id = idArr[i];
			//삭제 전 VO 객체 조회
			SchVO schVO = schMapper.selectSch(schMgtVO.getCode(), id);
			if(schVO == null) {
				continue;
			}
			
			int deleteResult = schMapper.deleteSch(schMgtVO.getCode(), id);	
			if(deleteResult > 0) {
				result++;
				//첨부파일
				comFileManager.removeComFileByParent("COM_SCH", id);
			}
		}
		return result;
	}

	@Override
	public int updateSchViewCnt(String schCd, String schId) {
		return schMapper.updateSchViewCnt(schCd, schId);
	}

	@Override
	public Map<String, Object> selectSchByAuth(SchMgtVO schMgtVO, String schId, String action) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		String errMsgCd = "message.error.deleteboard"; //삭제일정 메시지 코드
		
		//일정 조회
		SchVO schVO = schMapper.selectSch(schMgtVO.getCode(), schId);
		if(schVO != null) {
			
			//현재 로그인 정보
			LoginVO loginVO = SessionUtil.getLoginUser();
			
			//로그인 체크
			if(loginVO != null) {
				
				errMsgCd = "message.error.auth"; //권한없음				
				
				if(schMgtVO.isAdminUser()) { //관리자이면 true
					result = true;
				} else if(loginVO.getLoginId().equals(schVO.getInptId())) { //내가쓴글이면 true
					result = true;
				} else if("VIEW".equals(action)) { //상세조회인경우
					result = true;
				}
				
			} else {
				errMsgCd = "message.error.auth"; //권한없음
				
				if("VIEW".equals(action)) { //상세조회인경우
					result = true;
				}
			}
			
			if(result) {
				//첨부파일 목록 조회
				List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_SCH", schVO.getSchId());
				schVO.setViewFiles(viewFiles);
				map.put("schVO", schVO);
			}
		}
		
		map.put("result", result);
		map.put("errMsgCd", errMsgCd);
		
		return map;
	}


	@Override
	public List<SchVO> selectRecentSchListByParam(Map<String, Object> param) {
		if(param == null) {
			return null;
		}
		if(param.get("schCd") == null) {
			return null;
		}
		return schMapper.selectRecentSchListByParam(param);
	}	
}
