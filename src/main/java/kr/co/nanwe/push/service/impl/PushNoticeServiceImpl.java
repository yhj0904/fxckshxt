package kr.co.nanwe.push.service.impl;

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
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.external.service.impl.ExternalMapper;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.push.service.PushNoticeSendVO;
import kr.co.nanwe.push.service.PushNoticeService;
import kr.co.nanwe.push.service.PushNoticeVO;
import kr.co.nanwe.user.service.UserVO;
import kr.co.nanwe.user.service.impl.UserMapper;

/**
 * @Class Name 		: PushNoticeServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("pushNoticeService")
public class PushNoticeServiceImpl extends EgovAbstractServiceImpl implements PushNoticeService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(PushNoticeServiceImpl.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	/** Mapper */
	@Resource(name="externalMapper")
    private ExternalMapper externalMapper;
	
	@Resource(name="userMapper")
    private UserMapper userMapper;
	
	@Resource(name="pushNoticeMapper")
    private PushNoticeMapper pushNoticeMapper;
	
	@Resource(name="pushNoticeSendMapper")
    private PushNoticeSendMapper pushNoticeSendMapper;

	@Override
	@MethodDescription("알림전송 목록")
	public Map<String, Object> selectPushNoticeList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = pushNoticeMapper.selectPushNoticeTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<PushNoticeVO> list = pushNoticeMapper.selectPushNoticeList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("알림전송 조회")
	public PushNoticeVO selectPushNotice(int noticeNo) {
		PushNoticeVO pushNoticeVO = pushNoticeMapper.selectPushNotice(noticeNo);
		if(pushNoticeVO != null) {
			//전송 목록 조회
			List<PushNoticeSendVO> sendList = pushNoticeSendMapper.selectPushNoticeSendList(noticeNo);
			pushNoticeVO.setSendList(sendList);
		}
		return pushNoticeVO;
	}

	@Override
	@MethodDescription("알림전송 등록")
	public int insertPushNotice(PushNoticeVO pushNoticeVO) {
		
		//로그인정보 및 아이피
		String userId = "";
		String userNm = "";
		String userMobile = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
			userNm = loginInfo.getLoginNm();
			userMobile = loginInfo.getMbphNo();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		pushNoticeVO.setInptId(userId);
		pushNoticeVO.setInptIp(userIp);
		
		//알림 작성자		
		//기본값은 등록자와 동일하게
		String pushUserId = userId;
		String pushUserNm = userNm;
		String pushUserMobile = userMobile;
		
		//등록자와 작성자가 같지 않을 경우
		if(!StringUtil.isNull(pushNoticeVO.getUserId()) && !userId.equals(StringUtil.isNullToString(pushNoticeVO.getUserId()))) {
			UserVO user = null;			
			//외부데이터 사용유무 체크
			if(web.isExternalUse()) {
				user = externalMapper.selectUserOne(userId);
			}
			
			if(!web.isExternalUse() || user == null) {
				user = userMapper.selectUser(userId);
			}
			
			if(user != null) {
				pushUserId = user.getUserId();
				pushUserNm = user.getUserNm();
				pushUserMobile = user.getMbphNo();
			}
		}
		
		//작성자 정보 SET
		pushNoticeVO.setUserId(pushUserId);
		pushNoticeVO.setUserNm(pushUserNm);
		pushNoticeVO.setUserMobile(pushUserMobile);
		
		//예약전송인 경우
		if("Y".equals(pushNoticeVO.getReservationYn())) {
			//예약일 설정
			String reservationDt = "";
			String reservationYYYYMMDD = pushNoticeVO.getReservationYYYYMMDD();
			if(!StringUtil.isNull(reservationYYYYMMDD)) {
				reservationDt = reservationYYYYMMDD;
				pushNoticeVO.setReservationDt(reservationDt);
			}
		}
		
		int result = pushNoticeMapper.insertPushNotice(pushNoticeVO);
		if(result > 0) {
			
			//전체건수
			int totalCnt = 0;
			//SMS 건수
			int smsCnt = 0;
			//PUSH 건수
			int pushCnt = 0;
			
			//전송대상 등록
			if(pushNoticeVO.getSendList() != null) {
				for (PushNoticeSendVO pushNoticeSendVO : pushNoticeVO.getSendList()) {
					//전송구분					
					pushNoticeSendVO.setNoticeNo(pushNoticeVO.getNoticeNo());
					pushNoticeSendVO.setInptId(userId);
					pushNoticeSendVO.setInptIp(userIp);
					int sendResult = pushNoticeSendMapper.insertPushNoticeSend(pushNoticeSendVO);
					if(sendResult > 0) {
						totalCnt++;
						String sendDv = pushNoticeSendVO.getSendDv();
						if(sendDv != null) {
							if(sendDv.equals("PUSH")) {
								pushCnt++;
							} else if(sendDv.equals("SMS")) {
								smsCnt++;
							}
						}
					}					
				}
			}
			
			//이미지 파일이 업로드된 경우
			String noticeImg = null;
			if(FileUtil.isFile(pushNoticeVO.getUploadFile()) && "IMAGE".equals(FileUtil.getFileType(pushNoticeVO.getUploadFile()))) {
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "PUSH_NOTICE";				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(pushNoticeVO.getUploadFile(), uploadPath, "PUSH_NOTICE", pushNoticeVO.getNoticeNo());
				if(fileNo > 0) {
					ViewFileVO viewFile = comFileManager.getViewFile(fileNo);
					if(viewFile != null) {
						String serverDomain = RequestUtil.getProtocol() + RequestUtil.getDomain();
						noticeImg = serverDomain + viewFile.getViewUrl();
					}
				}
			}
			
			pushNoticeVO.setTotalCnt(totalCnt);
			pushNoticeVO.setSmsCnt(smsCnt);
			pushNoticeVO.setPushCnt(pushCnt);
			pushNoticeVO.setNoticeImg(noticeImg);
			pushNoticeMapper.updatePushNotice(pushNoticeVO);
		}
		
		return result;
	}
	
	@Override
	@MethodDescription("알림전송 삭제")
	public int deletePushNotice(int noticeNo) {
		int result = 0;
		//기존 데이터
		PushNoticeVO pushNoticeVO = pushNoticeMapper.selectPushNotice(noticeNo);
		if(pushNoticeVO != null) {
			result = pushNoticeMapper.deletePushNotice(noticeNo);
			if(result > 0) {
				pushNoticeSendMapper.deletePushNoticeSendList(noticeNo);
				//첨부파일
				comFileManager.removeComFileByParent("PUSH_NOTICE", noticeNo);
			}
		}
		
		return result;
	}

	@Override
	@MethodDescription("알림전송 선택삭제")
	public int deleteCheckedPushNotice(String checkedSId) {
		int cnt = 0;
		String[] idArr = checkedSId.split("\\|");
		for (int i=0; i<idArr.length; i++) {
			String id = idArr[i];
			int noticeNo = Integer.parseInt(id);
			PushNoticeVO pushNoticeVO = pushNoticeMapper.selectPushNotice(noticeNo);
			if(pushNoticeVO != null) {
				int result = pushNoticeMapper.deletePushNotice(noticeNo);
				if(result > 0) {
					pushNoticeSendMapper.deletePushNoticeSendList(noticeNo);
					//첨부파일
					comFileManager.removeComFileByParent("PUSH_NOTICE", noticeNo);
					cnt++;
				}
			}
		}
		return cnt;
	}

	@Override
	@MethodDescription("알림전송 엑셀 목록 조회")
	public void selectPushNoticeExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {
		// 검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();		
		pushNoticeMapper.selectPushNoticeExcelList(paramMap, handler);		
	}
	


	@Override
	@MethodDescription("알림전송 목록")
	public Map<String, Object> selectPushNoticeListByLoginUser(SearchVO search) {
		
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		paramMap.put("userId", loginVO.getLoginId());
		
		//전체 ROW COUNT
		int totCnt = pushNoticeMapper.selectPushNoticeTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<PushNoticeVO> list = pushNoticeMapper.selectPushNoticeList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("알림전송 조회")
	public PushNoticeVO selectPushNoticeByLoginUser(int noticeNo) {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		
		PushNoticeVO pushNoticeVO = pushNoticeMapper.selectPushNoticeByUser(loginVO.getLoginId(), noticeNo);
		if(pushNoticeVO != null) {
			//전송 목록 조회
			List<PushNoticeSendVO> sendList = pushNoticeSendMapper.selectPushNoticeSendList(noticeNo);
			pushNoticeVO.setSendList(sendList);
		}
		return pushNoticeVO;
	}

	@Override
	public int selectPushNoticeSendCountByState(String sendType) {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return 0;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!SessionUtil.isAdmin()) {
			paramMap.put("userId", loginVO.getLoginId());
		}
		paramMap.put("searchType", "sendCount");
		paramMap.put("sendType", sendType);
		
		return pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
	}
	
	@Override
	public Map<String, Object> selectPushNoticeSendCountByDate(String date) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			returnMap.put("count1", 0);
			returnMap.put("count2", 0);
			returnMap.put("count3", 0);			
			return returnMap;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!SessionUtil.isAdmin()) {
			paramMap.put("userId", loginVO.getLoginId());
		}
		paramMap.put("searchType", "calendar");
		paramMap.put("date", date);
		
		//발송
		paramMap.put("state", "1");
		int count1 = pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
		
		//대기
		paramMap.put("state", "2");
		int count2 = pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
		
		//예약
		paramMap.put("state", "3");
		int count3 = pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
		
		returnMap.put("count1", count1);
		returnMap.put("count2", count2);
		returnMap.put("count3", count3);
		
		return returnMap;
	}
	
	@Override
	public Map<String, Object> selectPushNoticeSendCountByMonthProgressBar() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			returnMap.put("allCount", 0);
			returnMap.put("pushCount", 0);
			returnMap.put("smsCount", 0);
			returnMap.put("pushPercent", "0%");
			returnMap.put("smsPercent", "0%");
			return returnMap;
		}
				
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!SessionUtil.isAdmin()) {
			paramMap.put("userId", loginVO.getLoginId());
		}
		
		paramMap.put("searchType", "monthProgressBar");
		
		//전체
		int allCount = pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
		
		//push
		paramMap.put("sendDv", "PUSH");
		int pushCount = pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
		
		//sms
		paramMap.put("sendDv", "SMS");
		int smsCount = pushNoticeSendMapper.selectPushNoticeSendCountByParam(paramMap);
		
		//퍼센트 계산
		String pushPercent = "0%";
		String smsPercent = "0%";
		if(allCount > 0) {
			pushPercent = Math.round((double) pushCount / (double) allCount * 100.0) + "%";
			smsPercent = Math.round((double) smsCount / (double) allCount * 100.0) + "%";
		}
		
		returnMap.put("allCount", allCount);
		returnMap.put("pushCount", pushCount);
		returnMap.put("smsCount", smsCount);
		returnMap.put("pushPercent", pushPercent);
		returnMap.put("smsPercent", smsPercent);
		
		return returnMap;
	}
	

	
	@Override
	public Map<String, Object> selectPushNoticeSendCountByMonthGraph() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String userId = "";
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO != null) {
			userId = loginVO.getLoginId();
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!SessionUtil.isAdmin()) {
			paramMap.put("userId", userId);
		}
		
		paramMap.put("sendDv", "PUSH");
		List<Map<String, Object>> push = pushNoticeSendMapper.selectPushNoticeSendCountByMonth(paramMap);
		
		paramMap.put("sendDv", "SMS");
		List<Map<String, Object>> sms = pushNoticeSendMapper.selectPushNoticeSendCountByMonth(paramMap);
		
		returnMap.put("push", push);
		returnMap.put("sms", sms);
		
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> selectRecentPushNoticeList() {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO == null) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(!SessionUtil.isAdmin()) {
			paramMap.put("userId", loginVO.getLoginId());
		}
		return pushNoticeMapper.selectRecentPushNoticeList(paramMap);
	}
}
