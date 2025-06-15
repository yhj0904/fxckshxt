package kr.co.nanwe.cnsler.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.bbs.service.impl.BbsMapper;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.CalendarUtil;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.HtmlConvertorUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cnsler.service.CnslerService;
import kr.co.nanwe.cnsler.service.CnslerVO;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.prog.service.ProgSearchVO;
import kr.co.nanwe.sch.service.SchVO;

/**
 * @Class Name 		: BbsServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("cnslerService")
public class CnslerServiceImpl extends EgovAbstractServiceImpl implements CnslerService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BbsServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "cnslerMapper")
	private CnslerMapper cnslerMapper;
	
	/** 상담원 관리 START */
	
	@Override
	@MethodDescription("상담원 목록 조회")
	public Map<String, Object> selectCnslerList(SearchVO search) {
		
		//검색조건 설정		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = cnslerMapper.selectCnslerTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<CnslerVO> list = cnslerMapper.selectCnslerList(paramMap); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	@Override
	@MethodDescription("상담원 상세 조회")
	public CnslerVO selectCnsler(String cnslerId) {
		
		CnslerVO cnslerVO = cnslerMapper.selectCnsler(cnslerId);
		
		
		return cnslerVO; 
	}
	
	@Override
	@MethodDescription("상담원 등록")
	public int insertCnsler(CnslerVO cnslerVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
		
		//로그인된 경우 작성자 이름 설정
		cnslerVO.setInptId(userId);
		
		
		//등록자 정보 SET
		cnslerVO.setInptId(userId);
		cnslerVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = cnslerMapper.insertCnsler(cnslerVO);
		
		
		return result;
	}
	
	@Override
	@MethodDescription("상담원 수정")
	public int updateCnsler(CnslerVO cnslerVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		cnslerVO.setModiId(userId);
		cnslerVO.setModiIp(userIp);
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = cnslerMapper.updateCnsler(cnslerVO);
		
		return result;
	}
	
	@Override
	@MethodDescription("상담원 삭제")
	public int deleteCnsler(String cnslerId) {
		
		//삭제 전 VO 객체 조회
		CnslerVO cnsler = cnslerMapper.selectCnsler(cnslerId);
		if(cnsler == null) {
			return 0;
		}
		
		int result = 0;
		
		result = cnslerMapper.deleteCnsler(cnslerId);
		
		return result;
	}
	
	@Override
	@MethodDescription("상담원 선택삭제")
	public int deleteCheckCnsler(String checkedSId) {
		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			//삭제 전 VO 객체 조회
			CnslerVO cnsler = cnslerMapper.selectCnsler(id);
			if(cnsler == null) {
				continue;
			}
			
			int deleteResult = 0;
			
			deleteResult = cnslerMapper.deleteCnsler(id);
			
			if(deleteResult > 0) {
				result++;
			}
		}
		
		return result;
	}
	
	@Override
	@MethodDescription("상담원 부서등록")
	public int insertCnslerColg(CnslerVO cnslerVO, String colg_values ,String dept_values, String cnslerId) {
		
		//입력 전  데이터 삭제
		int deleteResult = cnslerMapper.deleteCnslerDept(cnslerId);
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		int result = 0;
		String[] colgArr = colg_values.split("\\,");
		String[] deptArr = dept_values.split("\\,");
		for (int i=0; i<colgArr.length; i++) {
			String colg_id = colgArr[i];
			String dept_id = deptArr[i];
			
			cnslerVO.setCnslerId(cnslerId);
			cnslerVO.setCnslColgCd(colg_id);
			cnslerVO.setCnslDeptCd(dept_id);
			cnslerVO.setInptId(userId);
			cnslerVO.setInptIp(userIp);
			
			System.out.println(cnslerVO);
			
			result = cnslerMapper.insertCnslerColg(cnslerVO);
		}
		
		return result;
	}
	
	@Override
	@MethodDescription("상담원 단과목록 조회")
	public List<CnslerVO> selectColgByCnsler(String cnslerId) {
		
		//목록조회
		List<CnslerVO> list = cnslerMapper.selectColgByCnsler(cnslerId);
		
		return list;
	}
	
	@Override
	@MethodDescription("상담원 부서목록 조회")
	public List<CnslerVO> selectDeptByCnsler(String cnslerId) {
		
		//목록조회
		List<CnslerVO> list = cnslerMapper.selectDeptByCnsler(cnslerId);
		
		return list;
	}
	
	/** 상담원 관리 END */
	/** 상담 관리 START */
	@Override
	@MethodDescription("사용자 프로그램 신청내역 조회")
	public Map<String, Object> selectCnslList(SearchVO search) {
		
		//검색조건 설정		
		if (search == null ) search = new ProgSearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = cnslerMapper.selectCnslTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
				
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<Map> list = cnslerMapper.selectCnslList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	@Override
	@MethodDescription("상담원 상세 조회")
	public CnslerVO selectCnsl(String cnslId) {
		
		CnslerVO cnslerVO = cnslerMapper.selectCnsl(cnslId);
		
		
		return cnslerVO; 
	}

	@Override
	@MethodDescription("상담원 상세 조회")
	public Map<String, Object> selectCnslSecret(String cnslId, String pw) {

		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		String errMsgCd = "message.error.deleteboard"; //삭제게시물 메시지 코드

		// 상담글 조회
		CnslerVO cnslerVO = cnslerMapper.selectCnsl(cnslId);

		if(cnslerVO != null) {

			if(StringUtil.isNull(cnslerVO.getPw())) {
				cnslerVO.setPw("NO_PW");
			}

			errMsgCd = "message.error.auth"; //권한없음

			//현재 로그인 정보
			String loginId = "";
			LoginVO loginVO = SessionUtil.getLoginUser();
			if(loginVO != null) {
				loginId = loginVO.getLoginId();
			}

			if(!StringUtil.isNull(cnslerVO.getInptId()) && loginId.equals(cnslerVO.getInptId())) { //내가쓴글이면 true
				result = true;
			} else if (!StringUtil.isNull(cnslerVO.getPw())) { //비밀글인 경우)
				if (StringUtil.isEqual(cnslerVO.getPw(), pw)) {
					result = true;
				} else {
					errMsgCd = CodeConfig.NO_LOGIN; //에러코드를 비회원으로 하여 비밀번호 검증
				}
			}
		}

		map.put("cnslerVO", cnslerVO);
		map.put("result", result);
		map.put("errMsgCd", errMsgCd);

		return map;
	}
	
	@Override
	@MethodDescription("나의 상담신청내역 조회")
	public Map<String, Object> selectMyCnslList(SearchVO search, String userId) {
		
		//검색조건 설정		
		if (search == null ) search = new ProgSearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("userId", userId);
		
		//전체 ROW COUNT
		int totCnt = cnslerMapper.selectMyCnslTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
				
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<Map> list = cnslerMapper.selectMyCnslList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	
	@Override
	@MethodDescription("나의 상담신청내역 조회")
	public List<CnslerVO> selectCnslerByColg(SearchVO search, String cnslColgCd, String schDt) {
		
		// 맵선언
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("cnslColgCd", cnslColgCd);
		paramMap.put("schDt", schDt);
		
		List<CnslerVO> list = cnslerMapper.selectCnslerByColg(paramMap);
		
		return list;
	}
	
	@Override
	@MethodDescription("상담 등록")
	public int insertCnsl(CnslerVO cnslerVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();

		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		} else {
			userId = "NO_LOING";
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		cnslerVO.setUserId(userId);
		cnslerVO.setInptId(userId);
		cnslerVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = cnslerMapper.insertCnsl(cnslerVO);
		
		
		return result;
	}
	
	/** 상담 등록시 시간체크 */
	@Override
	@MethodDescription("상담가능시간 조회")
	public List<CnslerVO> selectCnslerTmList(CnslerVO cnslerVO) {
		
		List<CnslerVO> list = cnslerMapper.selectCnslerTmList(cnslerVO);
		System.out.println(">>>>>>>>>>>>>>>>서비스단 : " + list);
		
		return list;
	}
	
	/** 상담 수정 */
	public int updateCnsl(CnslerVO cnslerVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		cnslerVO.setModiId(userId);
		cnslerVO.setModiIp(userIp);
		
		int result = 0;
		//수정처리
		result = cnslerMapper.updateCnsl(cnslerVO);
		return result;
	}
	
	@Override
	@MethodDescription("상담 선택삭제")
	public int deleteCheckCnsl(String checkedSId) {
		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			//삭제 전 VO 객체 조회
			CnslerVO cnsler = cnslerMapper.selectCnsl(id);
			if(cnsler == null) {
				continue;
			}
			
			int deleteResult = 0;
			
			deleteResult = cnslerMapper.deleteCnsl(id);
			
			if(deleteResult > 0) {
				result++;
			}
		}
		
		return result;
	}
	
	@Override
	@MethodDescription("생산제품리스트 excel 다운로드")
	public void selectCnslListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler) {
		cnslerMapper.selectCnslListForExcel(handler);
	}
	
	/** 상담 관리 END */
	/** 상담일정 관리 START */
	
	/** 상담원별 상담일정 조회 */
	@Override
	@MethodDescription("상담원 일정 조회")
	public Map<String, Object> selectCnslerSchList(SearchVO search, String cnslerId, String date, String option) {
		
		//검색조건 설정		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//달력설정
		Map<String, Object> calendar = new HashMap<String, Object>();
		String typeCd = "month";
		
		calendar = CalendarUtil.getMonth(date, option);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();
		if("list".equals(typeCd)) {
			//전체 ROW COUNT
			int totCnt = cnslerMapper.selectCnslerSchTotCnt(paramMap);
			
			//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
			Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
			
			//시작 행과 페이지당 ROW수 조건 추가
			search.setCp(Integer.toString(paging.getCurrentPage()));
			paramMap.put("beginRow", paging.getBeginRow());
			paramMap.put("pagePerRow", paging.getPagePerRow());
			
			//목록 조회
			List<Map> list = cnslerMapper.selectCnslerSchList(paramMap);
			
			map.put("paging", paging);
			map.put("search", search);
			map.put("list", list);
		} else {
			if(calendar.get("list") != null) {
				List<Map<String, Object>> list = (List<Map<String, Object>>)calendar.get("list");
				for (int i=0; i<list.size(); i++) {
					List<CnslerVO> schList = cnslerMapper.selectCnslerSchListByCalendar(paramMap, list.get(i), cnslerId);
					list.get(i).put("schList", schList);
				}
			} else {
				List<CnslerVO> schList = cnslerMapper.selectCnslerSchListByCalendar(paramMap, calendar, cnslerId);
				calendar.put("schList", schList);	
			}
			map.put("calendar", calendar);
			map.put("sDate", calendar.get("sDate"));
		}
		
		return map;
	}
	
	/** 상담원 일정 등록 */
	@Override
	@MethodDescription("상담원 일정등록")
	public int insertCnslerSch(CnslerVO cnslerVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		cnslerVO.setInptId(userId);
		cnslerVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = cnslerMapper.insertCnslerSch(cnslerVO);
		
		
		return result;
	}
	
	/** 상담일정 등록시 날짜중복체크 */
	public int selectCnslerSchDupl(CnslerVO cnslerVO) {
		
		int result = 0;
		result = cnslerMapper.selectCnslerSchDupl(cnslerVO);
		
		return result;
	}
	
	/** 상담일정 1개조회 */
	public CnslerVO selectCnslerSchDetail(CnslerVO cnslerVO) {
		
		//단건 조회
		cnslerVO = cnslerMapper.selectCnslerSchDetail(cnslerVO);
		
		
		return cnslerVO;
	}
	
	/** 상담일정 삭제 */
	public int deleteCnslerSch(CnslerVO cnslerVO) {
		int result = 0;
		//삭제처리
		result = cnslerMapper.deleteCnslerSch(cnslerVO);
		return result;
	}
	
	/** 상담일정 수정 */
	public int updateCnslerSch(CnslerVO cnslerVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		
		userId = loginInfo.getLoginId();
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		cnslerVO.setInptId(userId);
		cnslerVO.setInptIp(userIp);
		
		int result = 0;
		//수정처리
		result = cnslerMapper.updateCnslerSch(cnslerVO);
		return result;
	}
	
	/** 상담일정 수정 */
	public int updateCnslStat(CnslerVO cnslerVO, String checkedSId) {
		
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			//수정 전 VO 객체 조회
			CnslerVO cnsler = cnslerMapper.selectCnsl(id);
			if(cnsler == null) {
				continue;
			}
			
			cnslerVO.setCnslId(id);
			
			int updateResult = 0;
			updateResult = cnslerMapper.updateCnslStat(cnslerVO);
			
			if(updateResult > 0) {
				result++;
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("학과 조회")
	public List<CnslerVO> selectCnslerDeptCd(Map<String, Object> params) {
		return cnslerMapper.selectCnslerDeptCd(params);
	}
	
	
	/** 상담일정 관리 END */
}
