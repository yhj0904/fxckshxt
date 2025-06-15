package kr.co.nanwe.bbs.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsCmntService;
import kr.co.nanwe.bbs.service.BbsCmntVO;
import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsMgtVO;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: BoardCmntController
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RequestMapping(value = "/board/cmnt")
@Program(code="BOARD_COMMENT", name="게시글 댓글")
@RestController
public class BoardCmntController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(BbsCmntController.class);
		
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Service */
	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	/** Service */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;
	
	/** Service */
	@Resource(name = "bbsCmntService")
	private BbsCmntService bbsCmntService;
	
	/**
	 * 게시판 댓글 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/list.json")
	public Map<String, Object> list(HttpServletRequest request
									, @RequestParam(value = "currentPage", defaultValue="1") String currentPage
									, @RequestParam(value = "bbsCd", defaultValue="") String bbsCd
									, @RequestParam(value = "bbsId", defaultValue="") String bbsId){
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		
		//게시판 정보 및 권한 체크
		BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(bbsCd, "VIEW");
		if(bbsMgtVO != null) {
			result = true;
			map = bbsCmntService.selectBbsCmntList(bbsMgtVO, bbsId, currentPage);
		}
		
		map.put("result", result);
		return map;
	}
	
	/**
	 * 게시판 댓글 등록
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/registerAction.json")
	public Map<String, Object> registerAction(HttpServletRequest request, @ModelAttribute BbsCmntVO bbsCmntVO){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean result = false;
		
		if(bbsCmntVO != null) {			
			//게시판 정보 및 권한 체크
			BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(bbsCmntVO.getBbsCd(), "CMNT");
			if(bbsMgtVO != null) {
				int insertResult = bbsCmntService.insertBbsCmnt(bbsMgtVO, bbsCmntVO);
				if(insertResult > 0) {
					result = true;
					map = bbsCmntService.selectBbsCmntList(bbsMgtVO, bbsCmntVO.getBbsId(), "1");
				}
			}
		}		
		map.put("result", result);
		return map;
	}
	
	/**
	 * 게시판 댓글 삭제
	 * @param 
	 * @return
	 * @exception 
	 */	
	@RequestMapping(value = "/removeAction.json")
	public Map<String, Object> removeAction(HttpServletRequest request, @ModelAttribute BbsCmntVO bbsCmntVO
											, @RequestParam(value = "currentPage", defaultValue="1") String currentPage){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean result = false;
		
		if(bbsCmntVO != null) {
			//게시판 정보 및 권한 체크
			BbsMgtVO bbsMgtVO = bbsMgtService.selectBbsMgtByAuth(bbsCmntVO.getBbsCd(), "CMNT");
			if(bbsMgtVO != null) {				
				
				//로그인 정보
				LoginVO loginVO = SessionUtil.getLoginUser();				
				//로그인인 경우
				if(loginVO != null) {					
					BbsCmntVO oriBbsCmntVO = bbsCmntService.selectBbsCmnt(bbsMgtVO, bbsCmntVO.getBbsId(), bbsCmntVO.getCmntId());					
					//관리자인지 또는 등록자 아이디와 로그인정보가 맞는지 확인
					if(SessionUtil.isAdmin() || loginVO.getLoginId().equals(oriBbsCmntVO.getInptId())){
						int insertResult = bbsCmntService.deleteBbsCmnt(bbsMgtVO, bbsCmntVO);
						if(insertResult > 0) {
							result = true;
							map = bbsCmntService.selectBbsCmntList(bbsMgtVO, bbsCmntVO.getBbsId(), currentPage);
						}
					}
				}
			}
		}
		map.put("result", result);
		return map;
	}
}
