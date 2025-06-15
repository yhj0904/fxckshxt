package kr.co.nanwe.prog.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
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
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
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
import kr.co.nanwe.prog.service.ProgSearchVO;
import kr.co.nanwe.prog.service.ProgService;
import kr.co.nanwe.prog.service.ProgUserVO;
import kr.co.nanwe.prog.service.ProgVO;

/**
 * @Class Name 		: ProgServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */

@Service("progService")
public class ProgServiceImpl extends EgovAbstractServiceImpl implements ProgService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProgServiceImpl.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	@Resource(name = "progMapper")
	private ProgMapper progMapper;

	@Resource(name = "progSurvItemMapper")
	private ProgSurvItemMapper progSurvItemMapper;

	@Resource(name = "progSurvQuesMapper")
	private ProgSurvQuesMapper progSurvQuesMapper;
	
	/** 웹에디터 */
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	/** 파일업로드 */
	@Resource(name = "fileUpload")
	private FileUpload fileUpload;
	
	@Override
	@MethodDescription("프로그램 ID 검색")
	public String selectProgId() {
		return progMapper.selectProgId();
	}

	@Override
	@MethodDescription("프로그램 목록 조회")
	public Map<String, Object> selectProgList(ProgSearchVO search) {

		//검색조건 설정		
		if (search == null ) search = new ProgSearchVO();
		Map<String, Object> paramMap = search.convertMap();
		//paramMap.put("bbsCd", bbsMgtVO.getCode());

		//전체 ROW COUNT
		int totCnt = progMapper.selectProgTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
				
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<ProgVO> list = progMapper.selectProgList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("프로그램 등록")
	public int insertProg(ProgVO progVO) {

		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		progVO.setInptId(userId);
		progVO.setInptIp(userIp);
		
		if(progVO.getRm() != null || !"".equals(progVO.getRm())) {
			progVO.setRm(HtmlConvertorUtil.changeLineSeparatorToBr(progVO.getRm()));	
		}
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = progMapper.insertProg(progVO);
		
		//등록 결과 확인
		if(result > 0) {
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFilesByBbs("prog", "PROG", String.valueOf(progVO.getProgId()));
			
			if(FileUtil.isFile(progVO.getUploadFile())) {				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "PROG" + FileUtil.SEPERATOR + "PROG_THUMB";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(progVO.getUploadFile(), uploadPath, "PROG_MST", progVO.getProgId());
				if(fileNo > 0) {
					progVO.setProgFileNo(fileNo);
					progMapper.updateProgThumbFile(progVO);
				}
			}
			
			//첨부파일 업로드 - 프로그램 첨부파일
			List<MultipartFile> uploadList = progVO.getUploadFiles();
			if(uploadList != null && uploadList.size() > 0) {
				for (MultipartFile uploadFile01 : uploadList) {
					//첨부파일 업로드
					comFileManager.uploadComFileByBbs(uploadFile01, "prog", "PROG", String.valueOf(progVO.getProgId()));
				}
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("프로그램 상세조회")
	public ProgVO selectProg(int progId) {
		ProgVO progVO = progMapper.selectProg(progId);
		if(progVO != null) {
			//프로그램 썸네일 파일 조회
			if(progVO.getProgFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(progVO.getProgFileNo());
				progVO.setViewFile(viewFile);
			}
			
			//프로그램 첨부파일 목록 조회
			List<ViewFileVO> oriViewFiles = comFileManager.getViewFileList("PROG_MST", String.valueOf(progVO.getProgId()));
			List<ViewFileVO> viewFiles = new ArrayList<ViewFileVO>();
			if(oriViewFiles != null) {
				for(int i=0; i < oriViewFiles.size(); i++) {
					String info = oriViewFiles.get(i).getSupplInfo1();
					if("PROG".equals(info)) {
						viewFiles.add(oriViewFiles.get(i));
					}
				}
				progVO.setViewFiles(viewFiles);
			}
		}
		return progVO;
	}
	
	@Override
	@MethodDescription("프로그램 상세조회")
	public ProgVO selectProgUpBfView(int progId) {
		ProgVO progVO = progMapper.selectProgUpBfView(progId);
		
		if(progVO != null) {
			//프로그램 썸네일 파일 조회
			if(progVO.getProgFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(progVO.getProgFileNo());
				progVO.setViewFile(viewFile);
			}
			
			//프로그램 첨부파일 목록 조회
			List<ViewFileVO> oriViewFiles = comFileManager.getViewFileList("PROG_MST", String.valueOf(progVO.getProgId()));
			List<ViewFileVO> viewFiles = new ArrayList<ViewFileVO>();
			if(oriViewFiles != null) {
				for(int i=0; i < oriViewFiles.size(); i++) {
					String info = oriViewFiles.get(i).getSupplInfo1();
					if("PROG".equals(info)) {
						viewFiles.add(oriViewFiles.get(i));
					}
				}
				progVO.setViewFiles(viewFiles);
			}
		}
		return progVO;
	}

	@Override
	@MethodDescription("프로그램 삭제")
	public int deleteProg(int progId) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//삭제 전 VO 객체 조회
		ProgVO prog = progMapper.selectProg(progId);
		if(prog == null) {
			return 0;
		}
		
		int result = 0;
		//신청자 / 설문 있는지 확인
		int applCnt = progMapper.selectProgApplUserTotCnt(progId);
		int survItemCnt = progSurvItemMapper.selectProgSurvItemCnt(String.valueOf(progId));
		int survQueCnt = progSurvQuesMapper.selectProgSurvQuesCnt(String.valueOf(progId));
		
		if(applCnt > 0 || survItemCnt > 0 || survQueCnt > 0) {
			prog.setDelId(userId);
			prog.setDelIp(userIp);
			
			//부모글 보존을 위해 상태만 삭제상태로 변경한다.
			result = progMapper.deleteProgToDelete(prog);
		} else {
			result = progMapper.deleteProg(progId);
			//삭제상태인 부모게시글을 조회 후 모두 삭제한다.
		}
		
		if(result > 0) {
			//에디터 이미지
			ckEditorUpload.deleteComEditorFiles("PROG_MST", String.valueOf(prog.getProgId()));
			//첨부파일
			comFileManager.removeComFileByParent("PROG_MST",  String.valueOf(progId));
			
			//설문지 / 신청자 내역 삭제 로직 추가
		}
		
		return result;
	}
	
	@Override
	@MethodDescription("프로그램 선택 삭제")
	public int deleteCheckedProg(String checkedSId) {
		
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
			ProgVO prog = progMapper.selectProg(Integer.valueOf(id));
			if(prog == null) {
				continue;
			}
			
			int deleteResult = 0;
			
			//답글이 있는지 체크
			//신청자 / 설문 있는지 확인
			int applCnt = progMapper.selectProgApplUserTotCnt(Integer.valueOf(id));
			int survItemCnt = progSurvItemMapper.selectProgSurvItemCnt(id);
			int survQueCnt = progSurvQuesMapper.selectProgSurvQuesCnt(id);
			
			if(applCnt > 0 || survItemCnt > 0 || survQueCnt > 0) {
				prog.setDelId(userId);
				prog.setDelIp(userIp);
				
				//부모글 보존을 위해 상태만 삭제상태로 변경한다.
				deleteResult = progMapper.deleteProgToDelete(prog);
			} else {
				deleteResult = progMapper.deleteProg(Integer.valueOf(id));
				//삭제상태인 부모게시글을 조회 후 모두 삭제한다.
			}
			
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//에디터 이미지
				ckEditorUpload.deleteComEditorFiles("PROG_MST", String.valueOf(prog.getProgId()));
				//첨부파일
				comFileManager.removeComFileByParent("PROG_MST", id);
			}
		}
		return result;
	}

	@Override
	@MethodDescription("프로그램 수정")
	public int updateProg(ProgVO progVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		progVO.setModiId(userId);
		progVO.setModiIp(userIp);
				
		progVO.setRm(HtmlConvertorUtil.changeLineSeparatorToBr(progVO.getRm()));
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = progMapper.updateProg(progVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			if(FileUtil.isFile(progVO.getUploadFile())) {

				//기존 이미지가 있는 경우 삭제				
				if(progVO.getFileNo() > 0) {
					comFileManager.removeComFileByParent("PROG_MST", progVO.getProgId());
				}
				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "PROG" + FileUtil.SEPERATOR + "PROG_THUMB";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(progVO.getUploadFile(), uploadPath, "PROG_MST", progVO.getProgId());
				if(fileNo > 0) {
					progVO.setProgFileNo(fileNo);
					progMapper.updateProgThumbFile(progVO);
				}
			}
			
			//에디터 이미지 체크 후 변경
			ckEditorUpload.updateComEditorFilesByBbs("prog", "PROG", String.valueOf(progVO.getProgId()));

			//첨부파일 업로드 - 프로그램 첨부파일
			List<MultipartFile> uploadList = progVO.getUploadFiles();
			if(uploadList != null && uploadList.size() > 0) {
				for (MultipartFile uploadFile01 : uploadList) {
					//첨부파일 업로드
					comFileManager.uploadComFileByBbs(uploadFile01, "prog", "PROG", String.valueOf(progVO.getProgId()));
				}
			}
			
			//기등록된 파일 목록중 삭제된 목록을 체크후 제거
			comFileManager.removeComFileByViewList("PROG_MST", String.valueOf(progVO.getProgId()), progVO.getViewFiles());
			
		}
		return result;
	}

	@Override
	@MethodDescription("사용자 프로그램 목록 조회")
	public Map<String, Object> selectProgUserViewList(ProgSearchVO search) {
		//검색조건 설정		
		if (search == null ) search = new ProgSearchVO();
		Map<String, Object> paramMap = search.convertMap();

		//전체 ROW COUNT
		int totCnt = progMapper.selectProgUserViewTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 12, 10, totCnt);
				
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<ProgVO> list = progMapper.selectProgUserViewList(paramMap);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				//첨부파일 목록 조회
				if(list.get(i).getProgFileNo() != 0) {
					ViewFileVO viewFile = comFileManager.getViewFile(list.get(i).getProgFileNo());
					list.get(i).setViewFile(viewFile);
				}
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
	@MethodDescription("사용자 프로그램 상세조회")
	public ProgVO selectProgUserView(int progId) {
		ProgVO progVO = progMapper.selectProgUserView(progId);
		if(progVO != null) {
			//프로그램 썸네일 파일 조회
			if(progVO.getProgFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(progVO.getProgFileNo());
				progVO.setViewFile(viewFile);
			}
			
			//프로그램 첨부파일 목록 조회
			List<ViewFileVO> oriViewFiles = comFileManager.getViewFileList("PROG_MST", String.valueOf(progVO.getProgId()));
			List<ViewFileVO> viewFiles = new ArrayList<ViewFileVO>();
			if(oriViewFiles != null) {
				for(int i=0; i < oriViewFiles.size(); i++) {
					String info = oriViewFiles.get(i).getSupplInfo1();
					if("PROG".equals(info)) {
						viewFiles.add(oriViewFiles.get(i));
					}
				}
			}
			progVO.setViewFiles(viewFiles);
		}
		return progVO;
	}

	@Override
	@MethodDescription("사용자 프로그램 신청")
	public int insertProgUser(ProgUserVO progUserVO) {
		return progMapper.insertProgUser(progUserVO);
	}
	
	//@Override
	//@MethodDescription("사용자 프로그램 신청 전 중복접수 검사")
	//public int selectApplDuplCnt(ProgUserVO progUserVO) {
	//	return progMapper.selectMyProgTotCnt(progUserVO.getProgId(), progUserVO.getUserId());
	//}

	@Override
	@MethodDescription("프로그램 신청자 목록 조회")
	public Map<String, Object> selectProgApplUserList(ProgSearchVO search, int progId) {
		
		//검색조건 설정		
		if (search == null ) search = new ProgSearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("progId", progId);
		
		//전체 ROW COUNT
		int totCnt = progMapper.selectProgApplUserTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
				
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<Map> list = progMapper.selectProgApplUserList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	public List<ProgVO> selectProgUserMainList(Map<String, Object> paramMap) {
		if(paramMap == null) {
			return null;
		}
		
		//목록조회
		List<ProgVO> proglist = progMapper.selectProgUserMainList(paramMap);
		System.out.println("test"+proglist);
		if(proglist != null && proglist.size() > 0) {
			for (int i = 0; i < proglist.size(); i++) {
				//첨부파일 목록 조회
				if(proglist.get(i).getProgFileNo() != 0) {
					ViewFileVO viewFile = comFileManager.getViewFile(proglist.get(i).getProgFileNo());
					proglist.get(i).setViewFile(viewFile);
				}
			}
		}
		
		return proglist;
	}

	@Override
	@MethodDescription("프로그램 신청자 상세 조회")
	public Map<String, Object> selectProgApplUser(int progId, String userId) {
		
		//반환
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//데이터 조회
		Map<String, Object> map = progMapper.selectProgApplUser(progId, userId);
		if(map != null) {
			resultMap.put("progUser", map);
		}
		
		return resultMap;
	}
	
	@Override
	@MethodDescription("프로그램 신청자 상세 조회")
	public Map<String, Object> selectProgApplChkUser(int progId, String userId) {
		return progMapper.selectProgApplUser(progId, userId);
	}

	@Override
	@MethodDescription("프로그램 수강생 선택 삭제")
	public int deleteCheckedProgApplUser(int progId, String checkedSId) {
		String[] idArr = checkedSId.split("\\|");
		int result = 0;
		int resultCnt = 0;
		
		for (int i=0; i<idArr.length; i++) {
			String seq = idArr[i];
			result = progMapper.deleteProgApplUser(progId, seq);
			if(result > 0) {
				resultCnt++;
			} else if (result == 0) {
				result = progMapper.deleteProgApplPublicUser(progId, seq);
				if(result > 0) {
					resultCnt++;
				}
			}
		}
		return resultCnt;
	}

	@Override
	@MethodDescription("프로그램 수강생 이수/미이수 처리")
	public int updateProgComplUser(int progId, String complCd, String checkedSId) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//MAP
		Map<String, Object> param = new HashMap<String, Object>(); 
		
		String[] idArr = checkedSId.split("\\|");
		int result = 0;
		int resultCnt = 0;
		
		for (int i=0; i<idArr.length; i++) {
			//userId
			String seq = idArr[i];
			
			param.clear();
			param.put("modiId", userId);
			param.put("modiIp", userIp);
			param.put("userId", seq);
			param.put("progId", progId);
			param.put("complCd", complCd);
			
			result = progMapper.updateProgComplUser(param);
			if(result > 0) {
				resultCnt++;
			} else if (result == 0) {
				result = progMapper.updateProgComplPublicUser(param);
				if(result > 0) {
					resultCnt++;
				}
			}
		}
		return resultCnt;
	}

	@Override
	@MethodDescription("사용자 프로그램 신청내역 조회")
	public Map<String, Object> selectMyProgList(ProgSearchVO search) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		} else {
			return null;
		}
		
		//검색조건 설정		
		if (search == null ) search = new ProgSearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("userId", userId);
		
		//전체 ROW COUNT
		int totCnt = progMapper.selectMyProgTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 20, 10, totCnt);
				
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		List<Map> list = progMapper.selectMyProgList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("프로그램 신청인원 조회 ")
	public int selectApplUserTotCnt(int progId) {
		return progMapper.selectProgApplUserTotCnt(progId);
	}

	@Override
	
	public int updateProgUser(int progId, String progReqstCd) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		} else {
			return 0;
		}
		String userIp = ClientUtil.getUserIp();
		
		//PARAM
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progId", progId);
		paramMap.put("userId", userId);
		paramMap.put("modiId", userId);
		paramMap.put("modiIp", userIp);
		paramMap.put("progReqstCd", progReqstCd);
		
		int result = progMapper.updateProgUser(paramMap);
		//if(result > 0) {}
		return result;
	}

	@Override
	@MethodDescription("사용자 프로그램 신청 취소")
	public int updateProgCancelUser(String checkedSId, String progReqstCd) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		} else {
			return 0;
		}
		String userIp = ClientUtil.getUserIp();
		
		
		String[] idArr = checkedSId.split("\\|");
		int result = 0;
		int resultCnt = 0;
		
		//PARAM
		Map<String, Object> param = new HashMap<String, Object>();
		//LOGGER.debug("=============== idArr >>>>>>>>>>>>> " + idArr);
		for (int i=0; i<idArr.length; i++) {
			//progId
			String seq = idArr[i];
			if(StringUtil.isNull(seq)) {
				continue;
			}
			//LOGGER.debug("=============== seq >>>>>>>>>>>>> " + seq);
			//LOGGER.debug("=============== seq >>>>>>>>>>>>> " + Integer.valueOf(seq));
			
			Map<String, Object> user = progMapper.selectProgApplUser(Integer.valueOf(seq), userId);		
			if(user != null && !user.isEmpty()) {

				param.clear();
				param.put("modiId", userId);
				param.put("modiIp", userIp);
				param.put("userId", userId);
				param.put("progId", Integer.valueOf(seq));
				param.put("progReqstCd", progReqstCd);
				
				result = progMapper.updateProgUser(param);
				if(result > 0) {
					resultCnt++;
				}
			}
		}
		return resultCnt;
	}
	
	@Override
	@MethodDescription("생산제품리스트 excel 다운로드")
	public void selectExcelProgApplUserList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search, int progId) {
		
		//맵선언
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("progId", progId);
		
		progMapper.selectExcelProgApplUserList(paramMap, handler);
	}

}
