package kr.co.nanwe.popup.service.impl;

import java.util.HashMap;
import java.util.Iterator;
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
import kr.co.nanwe.cmmn.util.CookieUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.popup.service.PopupService;
import kr.co.nanwe.popup.service.PopupVO;

/**
 * @Class Name 		: PopupServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("popupService")
public class PopupServiceImpl extends EgovAbstractServiceImpl implements PopupService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(PopupServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	/** Mapper */
	@Resource(name="popupMapper")
    private PopupMapper popupMapper;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	/** 웹에디터 */
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;

	@MethodDescription("팝업 목록 조회")
	public Map<String, Object> selectPopupList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = popupMapper.selectPopupTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<PopupVO> list = popupMapper.selectPopupList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("팝업 상세조회")
	public PopupVO selectPopup(String id) {
		PopupVO popupVO = popupMapper.selectPopup(id);
		if(popupVO != null) {
			//이미지 파일 조회
			if(popupVO.getFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(popupVO.getFileNo());
				popupVO.setViewFile(viewFile);
			}
		}
		return popupVO;
	}

	@Override
	@MethodDescription("팝업 등록")
	public int insertPopup(PopupVO popupVO) {
		
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		popupVO.setInptId(userId);
		popupVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = popupMapper.insertPopup(popupVO);
		
		//등록 결과 확인
		if(result > 0) {			
			//팝업 유형이 이미지일 경우
			if(StringUtil.isEqual(popupVO.getPopType(), "IMAGE")) {
				
				if(FileUtil.isFile(popupVO.getUploadFile())) {				
					//첨부파일 업로드 경로
					String rootPath = web.getFileRootPath();
					String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_POPUP";
					
					//첨부파일 업로드
					int fileNo = comFileManager.uploadComFile(popupVO.getUploadFile(), uploadPath, "COM_POPUP", popupVO.getPopId());
					if(fileNo > 0) {
						popupVO.setFileNo(fileNo);
						popupMapper.updatePopupFile(popupVO);
					}
				}
				
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("팝업 수정")
	public int updatePopup(PopupVO popupVO) {
		
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		popupVO.setModiId(userId);
		popupVO.setModiIp(userIp);
		
		//수정처리전 원본 데이터
		PopupVO oriPopupVO = popupMapper.selectPopup(popupVO.getPopId());
		if(oriPopupVO == null) {
			return 0;
		}
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = popupMapper.updatePopup(popupVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			//팝업 유형이 이미지일 경우
			if(StringUtil.isEqual(popupVO.getPopType(), "IMAGE")) {
				
				popupVO.setFileNo(oriPopupVO.getFileNo());
				
				if(FileUtil.isFile(popupVO.getUploadFile())) {
					
					//기존 이미지가 있는 경우 삭제				
					if(oriPopupVO.getFileNo() > 0) {
						comFileManager.removeComFileByParent("COM_POPUP", popupVO.getPopId());
					}
					
					//첨부파일 업로드 경로
					String rootPath = web.getFileRootPath();
					String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_POPUP";
					
					//첨부파일 업로드
					int fileNo = comFileManager.uploadComFile(popupVO.getUploadFile(), uploadPath, "COM_POPUP", popupVO.getPopId());
					if(fileNo > 0) {
						popupVO.setFileNo(fileNo);						
					}
				}
				
				popupMapper.updatePopupFile(popupVO);
				
			} else {
				
				//기존 이미지가 있는 경우 삭제
				if(oriPopupVO.getFileNo() > 0) {
					comFileManager.removeComFileByParent("COM_POPUP", popupVO.getPopId());
				}
				
				popupMapper.updatePopupFile(popupVO);
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("팝업 삭제")
	public int deletePopup(String id) {
		//삭제 전 VO 객체 조회
		PopupVO popup = popupMapper.selectPopup(id);
		int result = popupMapper.deletePopup(id);
		//첨부파일 삭제
		if(result > 0) {
			//에디터 이미지
			ckEditorUpload.deleteCkImage(popup.getPopCont());
			//첨부파일
			comFileManager.removeComFileByParent("COM_POPUP", id);
		}
		return result;
	}

	@Override
	@MethodDescription("팝업 선택 삭제")
	public int deleteCheckedPopup(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			//삭제 전 VO 객체 조회
			PopupVO popup = popupMapper.selectPopup(id);
			int deleteResult = popupMapper.deletePopup(id);
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//에디터 이미지
				ckEditorUpload.deleteCkImage(popup.getPopCont());
				//첨부파일
				comFileManager.removeComFileByParent("COM_POPUP", id);
			}
		}
		return result;
	}

	@Override
	@MethodDescription("팝업 메인목록 조회")
	public List<PopupVO> selectPopupByMain(String siteCd) {
		List<PopupVO> list = popupMapper.selectPopupByMain(siteCd);
		if(list != null) {
			for ( Iterator<PopupVO> it = list.iterator(); it.hasNext();) {
				PopupVO popup = it.next();
				if (CookieUtil.getCookie(popup.getPopId()) != null) {
					it.remove();
				} else {
					popup.setViewFile(comFileManager.getViewFile(popup.getFileNo()));
				}
			}
		}		
		return list;
	}
}
