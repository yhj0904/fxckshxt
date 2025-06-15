package kr.co.nanwe.bbs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.bbs.service.BbsCmntService;
import kr.co.nanwe.bbs.service.BbsCmntVO;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: BbsCmntServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("bbsCmntService")
public class BbsCmntServiceImpl extends EgovAbstractServiceImpl implements BbsCmntService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BbsCmntServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "bbsCmntMapper")
	private BbsCmntMapper bbsCmntMapper;

	@Override
	@MethodDescription("댓글 목록 조회")
	public Map<String, Object> selectBbsCmntList(BbsMgtVO bbsMgtVO, String bbsId, String currentPage) {
		
		//검색조건 설정
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("bbsCd", bbsMgtVO.getCode());
		paramMap.put("bbsId", bbsId);
		
		//전체 ROW COUNT
		int totCnt = bbsCmntMapper.selectBbsCmntTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(currentPage, 20, 5, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<BbsCmntVO> list = bbsCmntMapper.selectBbsCmntList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("댓글 상세 조회")
	public BbsCmntVO selectBbsCmnt(BbsMgtVO bbsMgtVO, String bbsId, String cmntId) {
		return bbsCmntMapper.selectBbsCmnt(bbsMgtVO.getCode(), bbsId, cmntId);
	}

	@Override
	@MethodDescription("댓글 등록")
	public int insertBbsCmnt(BbsMgtVO bbsMgtVO, BbsCmntVO bbsCmntVO) {
		//로그인정보
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO != null) {
			bbsCmntVO.setInptId(loginVO.getLoginId());
			bbsCmntVO.setWriter(loginVO.getLoginNm());
		} else {
			//비회원인 경우
			if(StringUtil.isNull(bbsCmntVO.getWriter()) || StringUtil.isNull(bbsCmntVO.getPw())) {
				return 0;
			}
		}
		bbsCmntVO.setInptIp(ClientUtil.getUserIp());
		return bbsCmntMapper.insertBbsCmnt(bbsCmntVO);
	}

	@Override
	@MethodDescription("댓글 삭제")
	public int deleteBbsCmnt(BbsMgtVO bbsMgtVO, BbsCmntVO bbsCmntVO) {
		return bbsCmntMapper.deleteBbsCmnt(bbsMgtVO.getCode(), bbsCmntVO.getBbsId(), bbsCmntVO.getCmntId());
	}
}
