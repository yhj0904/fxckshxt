package kr.co.nanwe.content.service.impl;

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
import kr.co.nanwe.content.service.ContentService;
import kr.co.nanwe.content.service.ContentVO;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: ComContentServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("contentService")
public class ContentServiceImpl extends EgovAbstractServiceImpl implements ContentService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "contentMapper")
	private ContentMapper contentMapper;
	
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
	@MethodDescription("컨텐츠 목록조회")
	public Map<String, Object> selectContentList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = contentMapper.selectContentTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<ContentVO> list = contentMapper.selectContentList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("컨텐츠 상세조회")
	public ContentVO selectContent(String contId) {
		return contentMapper.selectContent(contId);
	}

	@Override
	@MethodDescription("컨텐츠 등록")
	public int insertContent(ContentVO contentVO) {
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		contentVO.setInptId(userId);
		contentVO.setInptIp(userIp);
		
		contentVO.setContId(contentVO.getContId().toUpperCase());
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = contentMapper.insertContent(contentVO);
		if(result > 0) {
			ckEditorUpload.updateComEditorFiles("COM_CONTENT", contentVO.getContId());
		}
		
		return result;
	}

	@Override
	@MethodDescription("컨텐츠 수정")
	public int updateContent(ContentVO contentVO) {
		//로그인정보 및 아이피
		String userId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		contentVO.setModiId(userId);
		contentVO.setModiIp(userIp);
		
		//이전 컨텐츠
		ContentVO preContentVO = contentMapper.selectContent(contentVO.getContId());
		
		//수정 처리 (수정된 경우 row 수 return)	
		int result = contentMapper.updateContent(contentVO);
		if(result > 0) {
			//수정된 경우 이전 컨텐츠 백업
			contentMapper.insertContentBack(preContentVO);
			
			ckEditorUpload.updateComEditorFiles("COM_CONTENT", contentVO.getContId());
		}
		
		return result;
	}

	@Override
	@MethodDescription("컨텐츠 삭제")
	public int deleteContent(String contId) {
		//삭제 전 VO 객체 조회
		ContentVO content = contentMapper.selectContent(contId);
		int result = contentMapper.deleteContent(contId);
		//첨부파일 삭제
		if(result > 0) {
			//백업목록 삭제
			contentMapper.deleteContentBackByContId(contId);
			//에디터 이미지
			ckEditorUpload.deleteComEditorFiles("COM_CONTENT", content.getContId());
		}
		return result;
	}

	@Override
	@MethodDescription("컨텐츠 선택삭제")
	public int deleteCheckedContent(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String contId = idArr[i];
			//삭제 전 VO 객체 조회
			ContentVO content = contentMapper.selectContent(contId);
			int deleteResult = contentMapper.deleteContent(contId);
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//백업목록 삭제
				contentMapper.deleteContentBackByContId(contId);
				//에디터 이미지
				ckEditorUpload.deleteComEditorFiles("COM_CONTENT", content.getContId());
			}
		}
		return result;
	}
	
	@Override
	@MethodDescription("컨텐츠 백업 목록조회")
	public Map<String, Object> selectContentBackList(SearchVO search, String contId) {
		
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("contId", contId);
		
		//전체 ROW COUNT
		int totCnt = contentMapper.selectContentBackTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<ContentVO> list = contentMapper.selectContentBackList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}
	


	@Override
	@MethodDescription("컨텐츠 백업 상세조회")
	public ContentVO selectContentBack(String contId, int seq) {
		return contentMapper.selectContentBack(contId, seq);
	}
	
	@Override
	@MethodDescription("컨텐츠 백업 복원")
	public int updateContentByBack(String contId, int seq) {
		
		int result = 0;
		
		//원복전 데이터
		ContentVO contentVO = contentMapper.selectContent(contId);
		
		//복원데이터
		ContentVO backContentVO = contentMapper.selectContentBack(contId, seq);
		
		if(contentVO != null && backContentVO != null) {
			
			//로그인정보 및 아이피
			String userId = "";
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				userId = loginInfo.getLoginId();
			}
			String userIp = ClientUtil.getUserIp();
			
			//수정자 정보 SET
			backContentVO.setModiId(userId);
			backContentVO.setModiIp(userIp);
			
			//수정 처리 (수정된 경우 row 수 return)	
			result = contentMapper.updateContent(backContentVO);
			if(result > 0) {				
				//원복전 컨텐츠 백업
				contentMapper.insertContentBack(contentVO);
				//복원된 컨텐츠는 삭제
				contentMapper.deleteContentBack(contId, seq);
			}
		}
		return result;
	}
	
}
