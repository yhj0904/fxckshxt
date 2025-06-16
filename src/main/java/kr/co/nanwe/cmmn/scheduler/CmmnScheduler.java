package kr.co.nanwe.cmmn.scheduler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.user.service.UserService;

/**
 * @Class Name 		: CmmnScheduler
 * @Description 	: 공통 스케줄러
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component
public class CmmnScheduler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmmnScheduler.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** 웹에디터 */
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;
	
	/** 웹에디터 */
	@Resource(name = "userService")
	private UserService userService;
	
	public void executeJob() throws Exception {	
		LOGGER.info("** CmmnScheduler Execute **");
		
		//임시 웹에디터 삭제
		ckEditorUpload.deleteTempEditorFiles();
		
		//개인정보보관기간이 있는 경우 날짜 비교하여 삭제
		int saveMonth = Integer.parseInt(StringUtil.isNullToString(web.getServerProp("server.user.save.month")));
		if(saveMonth > 0) {
			userService.deleteUserListByAfterSaveMonth(saveMonth);		
		}		
		
	}
}