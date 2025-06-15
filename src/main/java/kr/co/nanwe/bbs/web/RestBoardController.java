package kr.co.nanwe.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.co.nanwe.bbs.service.LaborVO;
import org.springframework.web.bind.annotation.*;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.nanwe.bbs.service.BbsMgtService;
import kr.co.nanwe.bbs.service.BbsService;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.MessageUtil;

/**
 * @Class Name 		: RestBoardController
 * @Description 	: RestController
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@RestController
public class RestBoardController {
		
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
	
	/**
	 * 최근 게시물 목록 조회
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/getRecentBoardList.json", method = RequestMethod.POST)
	public List<BbsVO> getRecentBoardList(HttpServletRequest request, @RequestBody Map<String, Object> param) {
		return bbsService.selectRecentBbsListByParam(param);
	}
		
}
