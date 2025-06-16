package kr.co.nanwe.user.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.annotation.PrivacyLog;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.MailUtil;
import kr.co.nanwe.cmmn.util.MaskingUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.SendSmsUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.external.service.impl.ExternalMapper;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.join.service.impl.TermsMapper;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.menu.service.impl.MenuBookmarkMapper;
import kr.co.nanwe.user.service.UserService;
import kr.co.nanwe.user.service.UserVO;
import kr.co.nanwe.userip.service.impl.UserIpMapper;

/**
 * @Class Name 		: UserServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.18		신한나			필드 값 조정 및 기능 추가
 */

@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;

	@Resource(name = "userMapper")
	private UserMapper userMapper;

	@Resource(name = "userIpMapper")
	private UserIpMapper userIpMapper;

	@Resource(name = "termsMapper")
	private TermsMapper termsMapper;
	
	@Resource(name = "menuBookmarkMapper")
	private MenuBookmarkMapper menuBookmarkMapper;
	
	/** 외부 데이터 */
	@Resource(name = "externalMapper")
	private ExternalMapper externalMapper;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	@Resource(name = "mailUtil")
	private MailUtil mailUtil;
	
	@Resource(name = "sendSmsUtil")
	private SendSmsUtil sendSmsUtil;

	@Override
	@PrivacyLog
	@MethodDescription("사용자 목록 조회")
	public Map<String, Object> selectUserList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = userMapper.selectUserTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<UserVO> list = userMapper.selectUserList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 상세조회")
	public UserVO selectUser(String id) {
		UserVO userVO = userMapper.selectUser(id);
		if(userVO != null) {
			//이미지 파일 조회
			if(userVO.getFileNo() != 0) {
				ViewFileVO viewFile = comFileManager.getViewFile(userVO.getFileNo());
				userVO.setViewFile(viewFile);
			}
		}
		return userVO;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 등록")
	public int insertUser(UserVO userVO) {
		
		//로그인정보 및 아이피
		String userId = "";		
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//등록자 정보 SET
		userVO.setInptId(userId);
		userVO.setInptIp(userIp);
		
		//패스워드 암호화
		String saltKey = EncryptUtil.getSalt();
		String password = EncryptUtil.encryptMsgWithSalt(userVO.getPassword(), saltKey);
		userVO.setSaltKey(saltKey);
		userVO.setPassword(password);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = userMapper.insertUser(userVO);
		
		//등록 결과 확인
		if(result > 0) {
			if(FileUtil.isFile(userVO.getUploadFile())) {				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_USER";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(userVO.getUploadFile(), uploadPath, "COM_USER", userVO.getUserId());
				if(fileNo > 0) {
					userVO.setFileNo(fileNo);
					userMapper.updateUserFile(userVO);
				}
			}
		}
		
		return result;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 수정")
	public int updateUser(UserVO userVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		//수정자 정보 SET
		userVO.setModiId(userId);
		userVO.setModiIp(userIp);
		
		//패스워드 암호화
		if(!StringUtil.isNull(userVO.getPassword())) {
			String saltKey = EncryptUtil.getSalt();
			String password = EncryptUtil.encryptMsgWithSalt(userVO.getPassword(), saltKey);
			userVO.setSaltKey(saltKey);
			userVO.setPassword(password);
		}
		
		//수정처리전 원본 데이터
		UserVO oriuserVO = userMapper.selectUser(userVO.getUserId());
		if(oriuserVO == null) {
			return 0;
		}
		
		//수정 처리 (수정된 경우 row 수 return)
		int result = userMapper.updateUser(userVO);
		
		//수정 결과 확인
		if(result > 0) {
			
			if(FileUtil.isFile(userVO.getUploadFile())) {
				
				//기존 이미지가 있는 경우 삭제				
				if(oriuserVO.getFileNo() > 0) {
					comFileManager.removeComFileByParent("COM_USER", userVO.getUserId());
				}
				
				//첨부파일 업로드 경로
				String rootPath = web.getFileRootPath();
				String uploadPath = rootPath + FileUtil.SEPERATOR + "COM_USER";
				
				//첨부파일 업로드
				int fileNo = comFileManager.uploadComFile(userVO.getUploadFile(), uploadPath, "COM_USER", userVO.getUserId());
				if(fileNo > 0) {
					userVO.setFileNo(fileNo);
					userMapper.updateUserFile(userVO);				
				}
			}
		}
		
		return result;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 삭제")
	public int deleteUser(UserVO userVO) {
		
		//로그인정보 및 아이피
		String userId = "";	
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			userId = loginInfo.getLoginId();
		}
		String userIp = ClientUtil.getUserIp();
		
		int result = 0;
		
		//개인정보보관기간이 있는 경우
		int saveMonth = Integer.parseInt(StringUtil.isNullToString(web.getServerProp("server.user.save.month")));
		if(saveMonth > 0) {
			userVO.setDelId(userId);
			userVO.setDelIp(userIp);
			userVO.setDelYn("Y");
			result = userMapper.updateUserDeleteState(userVO);
		} else {
			result = userMapper.deleteUser(userVO.getUserId());
			if(result > 0) {
				//약관삭제
				termsMapper.deleteTermsUser(CodeConfig.COM_USER_CODE, userVO.getUserId());
				//첨부파일 삭제
				comFileManager.removeComFileByParent("COM_USER", userVO.getUserId());
				//즐겨찾기 삭제
				menuBookmarkMapper.deleteMenuBookmarkByUserId(userVO.getUserId());
				//아이피 삭제
				userIpMapper.deleteUserIpByUserId(userVO.getUserId());
			}
		}
		return result;
	}
	
	@Override
	@MethodDescription("사용자 아이디 존재여부 확인")
	public boolean selectUserIdExist(String id) {
		
		//아이디가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(id)) {
			return true;
		}
		
		//사용불가능한 아이디 정의
		List<String> denyList = new ArrayList<String>(Arrays.asList("admin", "adm", "root", "system"));
		
		// 불가능한 아이디일 경우 true로 return
		if (denyList.contains(id.toLowerCase())) {
			return true;
		}
		
		int count = userMapper.selectUserIdExist(id);
		if(count > 0) {
			return true;
		}
		
		//외부데이터 사용시 아이디 중복체크 추가
		if(web.isExternalUse()) {
			count = externalMapper.selectUserIdCnt(id);
			if(count > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	@MethodDescription("관리자 아이디 존재여부 확인")
	public boolean selectUserIdExistBySysUser(String id) {
		
		//아이디가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(id)) {
			return true;
		}
		
		int count = userMapper.selectUserIdExist(id);
		if(count > 0) {
			return true;
		}
		
		//외부데이터 사용시 아이디 중복체크 추가
		if(web.isExternalUse()) {
			count = externalMapper.selectUserIdCnt(id);
			if(count > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 목록 조회")
	public List<UserVO> selectUserListByUse() {
		return userMapper.selectUserListByUse();
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 아이디 찾기")
	public Map<String, Object> findUserId(Map<String, Object> map) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		returnMap.put("result", result);
		
		String findType = web.getServerProp("server.find.type");
		if(findType == null) {
			return returnMap;
		}	
		findType = findType.toUpperCase();	
		
		Map<String, Object> user = null;
		if(map != null) {
			//외부데이터 사용시
			if(web.isExternalUse()) {
				user = externalMapper.findUserId(map);
			}
			
			//외부데이터를 사용하지 않거나 외부사용자가 아닌 경우
			if(!web.isExternalUse() || (web.isExternalUse() && user == null)) {
				user = userMapper.findUserId(map);
			}
		}
		
		if(user != null) {
			result = true;
			returnMap.put("result", result);
			String id = (String) user.get("id");
			returnMap.put("id", MaskingUtil.getMaskedId(id));
			if(user.get("phone") != null && (findType.equals("BOTH") || findType.equals("PHONE"))) {
				String phone = (String) user.get("phone");
				returnMap.put("phone", MaskingUtil.getMaskedPhone(phone));
			}
			if(user.get("email") != null && (findType.equals("BOTH") || findType.equals("EMAIL"))) {
				String email = (String) user.get("email");
				returnMap.put("email", MaskingUtil.getMaskedEmail(email));
			}			
			returnMap.put("findType", findType);
		}
		
		return returnMap;
	}
	
	@Override
	@PrivacyLog
	@MethodDescription("사용자 아이디 찾기 발송")
	public boolean sendFindUserId(Map<String, Object> map) {
		
		boolean result = false;
		
		String findType = web.getServerProp("server.find.type");
		if(findType == null) {
			return false;
		}
		findType = findType.toUpperCase();
		
		if(map.get("sendType") == null) {
			return false;
		}
		
		String sendType = ((String) map.get("sendType")).toUpperCase();
		
		if(!"BOTH".equals(findType) && !findType.equals(sendType)) {
			return false;
		}
		
		Map<String, Object> user = null;
		if(map != null) {
			//외부데이터 사용시
			if(web.isExternalUse()) {
				user = externalMapper.findUserId(map);
			}
			
			//외부데이터를 사용하지 않거나 외부사용자가 아닌 경우
			if(!web.isExternalUse() || (web.isExternalUse() && user == null)) {
				user = userMapper.findUserId(map);
			}
		}
		
		if(user != null) {
			String id = (String) user.get("id");
			if("PHONE".equals(sendType) && user.get("phone") != null) {
				String phone =  (String) user.get("phone");
				result = sendSmsUtil.sendSms(messageUtil.getMessage("findUser.result.id", new String[] {id}), phone);
			} else if("EMAIL".equals(sendType) && user.get("email") != null) {
				String email =  (String) user.get("email");
				result = mailUtil.sendMail(email, "아이디 찾기 결과", messageUtil.getMessage("findUser.result.id", new String[] {id}));
			}
		}
				
		return result;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 비밀번호 찾기")
	public Map<String, Object> findUserPw(Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean result = false;
		returnMap.put("result", result);
		
		String findType = web.getServerProp("server.find.type");
		if(findType == null) {
			return returnMap;
		}	
		findType = findType.toUpperCase();
		
		if(map.get("sendType") == null) {
			return returnMap;
		}
		
		String sendType = ((String) map.get("sendType")).toUpperCase();
		
		if(!"BOTH".equals(findType) && !findType.equals(sendType)) {
			return returnMap;
		}
		
		String userType = "";
		Map<String, Object> user = null;
		if(map != null) {
			//외부데이터 사용시
			if(web.isExternalUse()) {
				user = externalMapper.findUserPw(map);
				userType = CodeConfig.EXTERANL_USER_CODE;
			}
			
			//외부데이터를 사용하지 않거나 외부사용자가 아닌 경우
			if(!web.isExternalUse() || (web.isExternalUse() && user == null)) {
				user = userMapper.findUserPw(map);
				userType = CodeConfig.COM_USER_CODE;
			}
		}
		
		if(user != null) {
			returnMap.put("result", true);
			returnMap.put("userType", userType);
			returnMap.put("id",  user.get("id"));
			
			if("PHONE".equals(sendType) && user.get("phone") != null) {
				returnMap.put("phone", user.get("phone"));
			} else if("EMAIL".equals(sendType) && user.get("email") != null) {
				returnMap.put("email", user.get("email"));
			}
			
			returnMap.put("sendType",  sendType);
		}
		
		return returnMap;
	}
	

	@Override
	@PrivacyLog
	@MethodDescription("사용자 비밀번호 변경")
	public int updateUserPassword(String userId, String password) {		
		UserVO userVO = userMapper.selectUser(userId);
		if(userVO != null) {
			String saltKey = EncryptUtil.getSalt();
			String userPw = EncryptUtil.encryptMsgWithSalt(password, saltKey);
			userVO.setPassword(userPw);
			userVO.setSaltKey(saltKey);			
			return userMapper.updateUserPassword(userVO);
		}
		return 0;		
	}

	@Override
	public void deleteUserListByAfterSaveMonth(int saveMonth) {
		List<UserVO> userList = userMapper.selectUserListByAfterSaveMonth(saveMonth);
		if(userList != null && userList.size() > 0) {
			for (UserVO userVO : userList) {
				String userId = userVO.getUserId();
				int result = userMapper.deleteUser(userId);
				if(result > 0) {
					//약관삭제
					termsMapper.deleteTermsUser(CodeConfig.COM_USER_CODE, userId);
					//첨부파일 삭제
					comFileManager.removeComFileByParent("COM_USER", userId);
					//즐겨찾기 삭제
					menuBookmarkMapper.deleteMenuBookmarkByUserId(userId);
					//아이피 삭제
					userIpMapper.deleteUserIpByUserId(userId);
				}
			}
		}
	}
	
	@Override
	@PrivacyLog
	@MethodDescription("탈퇴사용자 목록 조회")
	public Map<String, Object> selectDelUserList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//탈퇴여부
		paramMap.put("delYn", "Y");
		
		//전체 ROW COUNT
		int totCnt = userMapper.selectUserTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		search.setCp(Integer.toString(paging.getCurrentPage()));
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<UserVO> list = userMapper.selectUserList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@PrivacyLog
	@MethodDescription("탈퇴사용자 상태 변경")
	public int updateUserDelState(String userId, String deleteState) {
		
		if(StringUtil.isNull(userId) || StringUtil.isNull(deleteState)) {
			return 0;
		}
		
		int result = 0;		
		switch (deleteState.toLowerCase()) {
			case "remove":
				result = userMapper.deleteUser(userId);
				if(result > 0) {
					//약관삭제
					termsMapper.deleteTermsUser(CodeConfig.COM_USER_CODE, userId);
					//첨부파일 삭제
					comFileManager.removeComFileByParent("COM_USER", userId);
					//즐겨찾기 삭제
					menuBookmarkMapper.deleteMenuBookmarkByUserId(userId);
					//아이피 삭제
					userIpMapper.deleteUserIpByUserId(userId);
				}
				break;
			case "recover":
				UserVO userVO = new UserVO();
				userVO.setUserId(userId);
				userVO.setDelYn("N");
				result = userMapper.updateUserDeleteState(userVO);
				break;
			default:
				break;
		}
		
		return result;
	}

	@Override
	@PrivacyLog
	@MethodDescription("사용자 정보 조회")
	public UserVO selectMaskingUserByLoginUser(LoginVO loginVO) {
		
		UserVO userVO = null;
		
		//외부사용자인 경우
		if(StringUtil.isEqual(loginVO.getLoginUserType(), CodeConfig.EXTERANL_USER_CODE)) {
			userVO = externalMapper.selectUserOne(loginVO.getLoginId());
		} else {
			userVO = userMapper.selectUser(loginVO.getLoginId());
			if(userVO != null) {		
				//이미지 파일 조회
				if(userVO.getFileNo() != 0) {
					ViewFileVO viewFile = comFileManager.getViewFile(userVO.getFileNo());
					userVO.setViewFile(viewFile);
				}
			}
		}
		
		//사용자 정보를 마스킹
		if(userVO != null) {			
			if(userVO.getUserId() != null) {
				userVO.setUserId(MaskingUtil.getMaskedId(userVO.getUserId()));
			}
			if(userVO.getTelNo() != null) {
				userVO.setTelNo(MaskingUtil.getMaskedTel(userVO.getTelNo()));
			}
			if(userVO.getMbphNo() != null) {
				userVO.setMbphNo(MaskingUtil.getMaskedPhone(userVO.getMbphNo()));
			}
			if(userVO.getEmail() != null) {
				userVO.setEmail(MaskingUtil.getMaskedEmail(userVO.getEmail()));
			}	
		}
		
		return userVO;
	}
	
	@Override
	@MethodDescription("사용자 휴대폰 존재여부 확인")
	public boolean selectUserPhoneExist(String userId, String mbphNo) {
		
		//아이디가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(mbphNo)) {
			return true;
		}
		
		int count = userMapper.selectUserPhoneExist(userId, mbphNo);
		if(count > 0) {
			return true;
		}
		
		//외부데이터 사용시 아이디 중복체크 추가
		if(web.isExternalUse()) {
			count = externalMapper.selectUserPhoneExist(mbphNo);
			if(count > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	@MethodDescription("사용자 이메일 존재여부 확인")
	public boolean selectUserEmailExist(String userId, String email) {
		
		//아이디가 공백인 경우 존재하는 거로 간주
		if(StringUtil.isNull(email)) {
			return true;
		}
		
		int count = userMapper.selectUserEmailExist(userId, email);
		if(count > 0) {
			return true;
		}
		
		//외부데이터 사용시 아이디 중복체크 추가
		if(web.isExternalUse()) {
			count = externalMapper.selectUserEmailExist(email);
			if(count > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	@PrivacyLog
	@MethodDescription("상담원 가능 사용자 목록 조회")
	public List<UserVO> selectCnslerUserList() {
		return userMapper.selectCnslerUserList();
	}
	
	@Override
	@MethodDescription("사용자 전체 excel 다운로드")
	public void selectUserListForExcel(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {
		
		//맵선언
		Map<String, Object> paramMap = search.convertMap();
		
		userMapper.selectUserListForExcel(paramMap, handler);
	}
}
