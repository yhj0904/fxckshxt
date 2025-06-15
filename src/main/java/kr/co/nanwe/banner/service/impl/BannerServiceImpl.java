package kr.co.nanwe.banner.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.banner.service.BannerService;
import kr.co.nanwe.banner.service.BannerVO;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: BannerServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("bannerService")
public class BannerServiceImpl extends EgovAbstractServiceImpl implements BannerService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(BannerServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	/** Mapper */
	@Resource(name="bannerMapper")
    private BannerMapper bannerMapper;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	@MethodDescription("배너 목록 조회")
	public Map<String, Object> selectBannerList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = bannerMapper.selectBannerTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<BannerVO> list = bannerMapper.selectBannerList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("배너 상세조회")
	public BannerVO selectBanner(String id) {
		BannerVO bannerVO = bannerMapper.selectBanner(id);
		if(bannerVO != null) {
			//이미지 파일 조회
			if(bannerVO.getFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(bannerVO.getFileNo());
				bannerVO.setViewFile(viewFile);
			}
		}
		return bannerVO;
	}

	@Override
	@MethodDescription("배너 등록")
	public int insertBanner(BannerVO bannerVO) {
		
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		bannerVO.setInptId(userId);
		bannerVO.setInptIp(userIp);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = bannerMapper.insertBanner(bannerVO);
		
		//등록 결과 확인
		if(result > 0) {			
			if(FileUtil.isFile(bannerVO.getUploadFile())) {				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_BANNER";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(bannerVO.getUploadFile(), uploadPath, "COM_BANNER", bannerVO.getBannerId());
				if(fileNo > 0) {
					bannerVO.setFileNo(fileNo);
					bannerMapper.updateBannerFile(bannerVO);
				}
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("배너 수정")
	public int updateBanner(BannerVO bannerVO) {
		
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		bannerVO.setModiId(userId);
		bannerVO.setModiIp(userIp);
		
		//수정처리전 원본 데이터
		BannerVO oriBannerVO = bannerMapper.selectBanner(bannerVO.getBannerId());
		if(oriBannerVO == null) {
			return 0;
		}
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = bannerMapper.updateBanner(bannerVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			bannerVO.setFileNo(oriBannerVO.getFileNo());
			
			if(FileUtil.isFile(bannerVO.getUploadFile())) {
				
				//기존 이미지가 있는 경우 삭제				
				if(oriBannerVO.getFileNo() > 0) {
					comFileManager.removeComFileByParent("COM_BANNER", bannerVO.getBannerId());
				}
				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_BANNER";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(bannerVO.getUploadFile(), uploadPath, "COM_BANNER", bannerVO.getBannerId());
				if(fileNo > 0) {
					bannerVO.setFileNo(fileNo);						
				}
			}
			
			bannerMapper.updateBannerFile(bannerVO);
		}
		
		return result;
	}

	@Override
	@MethodDescription("배너 삭제")
	public int deleteBanner(String id) {
		int result = bannerMapper.deleteBanner(id);
		//첨부파일 삭제
		if(result > 0) {
			//첨부파일
			comFileManager.removeComFileByParent("COM_BANNER", id);			
		}
		return result;
	}

	@Override
	@MethodDescription("배너 선택 삭제")
	public int deleteCheckedBanner(String checkedSId) {
		int result = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			int deleteResult = bannerMapper.deleteBanner(id);
			//첨부파일 삭제
			if(deleteResult > 0) {
				result++;
				//첨부파일
				comFileManager.removeComFileByParent("COM_BANNER", id);
			}
		}
		return result;
	}

	@Override
	@MethodDescription("배너 코드목록 조회")
	public List<BannerVO> selectBannerByCode(String code) {
		List<BannerVO> list = bannerMapper.selectBannerByCode(code);
		for(BannerVO banner : list) {
			banner.setViewFile(comFileManager.getViewFile(banner.getFileNo()));
		}
		
		return list;
	}

}
