package kr.co.nanwe.cmmn.scheduler;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
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
	
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;
	
	/**
	 * 임시 파일 삭제 스케줄러
	 */
	/*
	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
	public void deleteTempFiles() {
		try {
			// 임시 파일 삭제 로직
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId("TEMP");
			List<FileVO> fileList = fileMngService.selectFileInfs(fileVO);
			
			for (FileVO file : fileList) {
				File physicalFile = new File(file.getFileStreCours() + file.getStreFileNm());
				if (physicalFile.exists()) {
					physicalFile.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * 사용자 데이터 보관 기간 관리 스케줄러
	 */
	/*
	@Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
	public void manageUserDataRetention() {
		try {
			// 사용자 데이터 보관 기간 관리 로직
			int retentionMonths = web.getUserDataRetentionMonths();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -retentionMonths);
			Date retentionDate = cal.getTime();
			
			// 보관 기간이 지난 사용자 데이터 삭제
			userService.deleteExpiredUserData(retentionDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}