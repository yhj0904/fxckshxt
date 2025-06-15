package kr.co.nanwe.login.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.annotation.MethodDescription;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.service.SearchVO;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.external.service.impl.ExternalMapper;
import kr.co.nanwe.file.service.ComFileManager;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.login.service.LoginLogVO;
import kr.co.nanwe.login.service.LoginService;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: LoginServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("loginService")
public class LoginServiceImpl extends EgovAbstractServiceImpl implements LoginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource(name="loginMapper")
    private LoginMapper loginMapper;
    
    @Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** 공통파일 관리 */
	@Resource(name = "comFileManager")
	private ComFileManager comFileManager;
	
	/** 외부 데이터 */
	@Resource(name = "externalMapper")
	private ExternalMapper externalMapper;
	
	@Override
	public LoginVO selectLoginUser(String loginId, String loginPw) {

		//로그인 결과
		String loginSuccess = "N";
		
		//에러 코드
		/*
		 * ERROR_01 : 존재하지 않는 사용자
		 * ERROR_02 : 아이디 또는 비밀번호 오류
		 * ERROR_03 : 권한없음
		 * ERROR_04 : 이용중지된 사용자 로그인
		 * ERROR_05 : 탈퇴한 사용자
		 * ERROR_06 : 비밀번호 5회 오류
		 * 
		 */
		String loginErrCode = "ERROR_01"; //존재하지 않는 사용자
		LoginVO loginInfo = new LoginVO();
		
		//클라이언트 정보 조회
		String loginType = "LOGIN";
		String loginIp = ClientUtil.getUserIp();
		String loginOs = ClientUtil.getUserOS();
		String loginDevice = ClientUtil.getUserDevice();
		HashMap<String,String> browser = ClientUtil.getUserBrowser();
		String typeKey = browser.get(ClientUtil.TYPEKEY);
		String versionKey = browser.get(ClientUtil.VERSIONKEY);
		String loginBrowser = typeKey + " " + versionKey;
		String loginDttm = DateUtil.getDateTime();
		
		loginInfo.setLoginType(loginType);
		loginInfo.setLoginId(loginId);		
		loginInfo.setLoginIp(loginIp);
		loginInfo.setLoginOs(loginOs);
		loginInfo.setLoginDevice(loginDevice);
		loginInfo.setLoginBrowser(loginBrowser);
		loginInfo.setLoginDttm(loginDttm);
		
		//외부데이터 사용시 먼저 VIEW를 조회한다.
		if(web.isExternalUse()) {
			
			loginInfo.setLoginUserType(CodeConfig.EXTERANL_USER_CODE);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userId", loginId);
			paramMap.put("userPw", loginPw);
			paramMap.put("userIp", loginIp);	
			
			//유저가 존재하는지 체크
			int userCnt = externalMapper.selectUserIdCnt(loginId);
			if(userCnt > 0) {
				
				//비밀번호 오류 회수 체크
				int pwErrCnt = loginMapper.selectPwLogCnt(loginInfo);				
				if(pwErrCnt < web.getPwErrorCount()) {
					//프로시저 호출 오류 try catch
					try {
						externalMapper.executeLoginUserInfo(paramMap);
					} catch (Exception e) {
						LOGGER.debug(e.getMessage());
					}
					
					if(paramMap.get("result") != null) {
						loginSuccess = "Y";
						loginErrCode = "";
						@SuppressWarnings("unchecked")
						List<LoginVO> result = (List<LoginVO>) paramMap.get("result");
						for(LoginVO resultInfo : result) {
							loginInfo.setLoginNm(resultInfo.getLoginNm());
							loginInfo.setTelNo(resultInfo.getTelNo());
							loginInfo.setMbphNo(resultInfo.getMbphNo());
							loginInfo.setEmail(resultInfo.getEmail());
							loginInfo.setPostNo(resultInfo.getPostNo());
							loginInfo.setAddr(resultInfo.getAddr());
							loginInfo.setDetlAddr(resultInfo.getDetlAddr());
							loginInfo.setDeptCd(resultInfo.getDeptCd());
							loginInfo.setDeptNm(resultInfo.getDeptNm());
							loginInfo.setUserDvcd(resultInfo.getUserDvcd());
							loginInfo.setWorkDvcd(resultInfo.getWorkDvcd());
							loginInfo.setStatDvcd(resultInfo.getStatDvcd());
						}
					} else {
						loginErrCode = "ERROR_02"; //아이디 또는 비밀번호 오류
					}
				} else {
					loginErrCode = "ERROR_06"; //비밀번호 5회 오류
				}
			} else {
				loginErrCode = "ERROR_01"; //존재하지 않는 사용자
			}
			
			loginInfo.setLoginSuccess(loginSuccess);
			loginInfo.setLoginErrCode(loginErrCode);
		}		
		
		//외부데이터를 사용하지 않거나 로그인이 미성공이면서 존재하지 않는 사용자인 경우
		if(!web.isExternalUse() || ("N".equals(loginSuccess) && "ERROR_01".equals(loginErrCode))) {
			
			loginInfo.setLoginUserType(CodeConfig.COM_USER_CODE);
			
			//SALT KEY 조회
			String saltKey = loginMapper.selectLoginUserSaltKey(loginId);
			
			if(saltKey != null) {
				
				//비밀번호 오류 회수 체크
				int pwErrCnt = loginMapper.selectPwLogCnt(loginInfo);
				
				if(pwErrCnt < web.getPwErrorCount()) {
					
					//SALT KEY로 암호화
					loginPw = EncryptUtil.encryptMsgWithSalt(loginPw, saltKey);
					
					//조회 결과
					LoginVO resultLoginVO = loginMapper.selectLoginUser(loginId, loginPw);
					if(resultLoginVO != null) {						
						//사용가능한지 체크
						if("Y".equals(resultLoginVO.getLoginYn())) {
							
							loginInfo.setLoginNm(resultLoginVO.getLoginNm());
							loginInfo.setTelNo(resultLoginVO.getTelNo());
							loginInfo.setMbphNo(resultLoginVO.getMbphNo());
							loginInfo.setEmail(resultLoginVO.getEmail());
							loginInfo.setPostNo(resultLoginVO.getPostNo());
							loginInfo.setAddr(resultLoginVO.getAddr());
							loginInfo.setDetlAddr(resultLoginVO.getDetlAddr());
							loginInfo.setDeptCd(resultLoginVO.getDeptCd());
							loginInfo.setDeptNm(resultLoginVO.getDeptNm());
							loginInfo.setUserDvcd(CodeConfig.COM_USER_CODE);
							loginInfo.setWorkDvcd(CodeConfig.COM_USER_CODE);
							loginInfo.setStatDvcd(resultLoginVO.getStatDvcd());
							
							//권한이 관리자인경우 아이피 체크
							if(SessionUtil.isAdmin(loginInfo)) {
								//아이피 접근 가능한지 체크
								int checkIp = loginMapper.selectLoginUserIp(loginId, loginIp);
								if(checkIp > 0) {
									loginSuccess = "Y";
									loginErrCode = "";
								} else {
									loginErrCode = "ERROR_03"; //권한 없음
								}
							} else {
								loginSuccess = "Y";
								loginErrCode = "";
							}
												
							if(resultLoginVO.getFileNo() > 0 && "Y".equals(loginSuccess)) {
								ViewFileVO viewFile = comFileManager.getViewFile(resultLoginVO.getFileNo());
								if(viewFile != null) {
									loginInfo.setUserImgSrc(viewFile.getViewUrl());
								}
							}
							
						} else {
							loginErrCode = "ERROR_04"; //이용중지된 사용자 로그인
						}	
					} else {
						loginErrCode = "ERROR_02"; //아이디 또는 비밀번호 오류
					}
				} else {
					loginErrCode = "ERROR_06"; //비밀번호 5회 오류
				}			
			} else {
				loginErrCode = "ERROR_01"; //존재하지 않는 사용자
			}
			
			loginInfo.setLoginSuccess(loginSuccess);
			loginInfo.setLoginErrCode(loginErrCode);
		}
		
		return loginInfo;
	}
	
	@Override
	public LoginVO selectLoginSysAdmUser(String loginId, String loginPw) {

		//로그인 결과
		String loginSuccess = "N";
		
		//에러 코드
		/*
		 * ERROR_01 : 존재하지 않는 사용자
		 * ERROR_02 : 아이디 또는 비밀번호 오류
		 * ERROR_03 : 권한없음
		 * ERROR_04 : 이용중지된 사용자 로그인
		 * ERROR_05 : 탈퇴한 사용자
		 * ERROR_06 : 비밀번호 5회 오류
		 * 
		 */
		String loginErrCode = "ERROR_01"; //존재하지 않는 사용자
		LoginVO loginInfo = new LoginVO();
		
		//클라이언트 정보 조회
		String loginType = "LOGIN";
		String loginIp = ClientUtil.getUserIp();
		String loginOs = ClientUtil.getUserOS();
		String loginDevice = ClientUtil.getUserDevice();
		HashMap<String,String> browser = ClientUtil.getUserBrowser();
		String typeKey = browser.get(ClientUtil.TYPEKEY);
		String versionKey = browser.get(ClientUtil.VERSIONKEY);
		String loginBrowser = typeKey + " " + versionKey;
		String loginDttm = DateUtil.getDateTime();
		
		loginInfo.setLoginType(loginType);
		loginInfo.setLoginUserType(CodeConfig.COM_USER_CODE);
		loginInfo.setLoginId(loginId);		
		loginInfo.setLoginIp(loginIp);
		loginInfo.setLoginOs(loginOs);
		loginInfo.setLoginDevice(loginDevice);
		loginInfo.setLoginBrowser(loginBrowser);
		loginInfo.setLoginDttm(loginDttm);
		
		//SALT KEY 조회
		String saltKey = loginMapper.selectLoginUserSaltKey(loginId);
		
		if(saltKey != null) {
			
			//비밀번호 오류 회수 체크
			int pwErrCnt = loginMapper.selectPwLogCnt(loginInfo);
			
			if(pwErrCnt < web.getPwErrorCount()) {
				
				//SALT KEY로 암호화
				loginPw = EncryptUtil.encryptMsgWithSalt(loginPw, saltKey);				
				
				//조회 결과
				LoginVO resultLoginVO = loginMapper.selectLoginUser(loginId, loginPw);
				
				if(resultLoginVO != null) {					
					//사용가능한지 체크
					if("Y".equals(resultLoginVO.getLoginYn())) {
						
						loginInfo.setLoginNm(resultLoginVO.getLoginNm());
						loginInfo.setTelNo(resultLoginVO.getTelNo());
						loginInfo.setMbphNo(resultLoginVO.getMbphNo());
						loginInfo.setEmail(resultLoginVO.getEmail());
						loginInfo.setPostNo(resultLoginVO.getPostNo());
						loginInfo.setAddr(resultLoginVO.getAddr());
						loginInfo.setDetlAddr(resultLoginVO.getDetlAddr());
						loginInfo.setDeptCd(resultLoginVO.getDeptCd());
						loginInfo.setDeptNm(resultLoginVO.getDeptNm());
						loginInfo.setUserDvcd(CodeConfig.COM_USER_CODE);
						loginInfo.setWorkDvcd(CodeConfig.COM_USER_CODE);
						loginInfo.setStatDvcd(resultLoginVO.getStatDvcd());
						
						//관리자인지 체크
						if(SessionUtil.isAdmin(loginInfo)) {
							//아이피 접근 가능한지 체크
							int checkIp = loginMapper.selectLoginUserIp(loginId, loginIp);
							if(checkIp > 0) {
								if(resultLoginVO.getFileNo() > 0) {
									ViewFileVO viewFile = comFileManager.getViewFile(resultLoginVO.getFileNo());
									if(viewFile != null) {
										loginInfo.setUserImgSrc(viewFile.getViewUrl());
									}
								}
								loginSuccess = "Y";
								loginErrCode = "";
							} else {
								loginErrCode = "ERROR_03"; //권한 없음
							}
						} else {
							loginErrCode = "ERROR_03"; //권한 없음
						}						
					} else {
						loginErrCode = "ERROR_04"; //이용중지된 사용자 로그인
					}
				} else {
					loginErrCode = "ERROR_02"; //아이디 또는 비밀번호 오류
				}
			} else {
				loginErrCode = "ERROR_06"; //비밀번호 5회 오류
			}			
		} else {
			loginErrCode = "ERROR_01"; //존재하지 않는 사용자
		}
		
		loginInfo.setLoginSuccess(loginSuccess);
		loginInfo.setLoginErrCode(loginErrCode);
		
		return loginInfo;
	}
	
	@Override
	public int insertLoginLog(LoginVO loginVO) {
		
		/*
		 * ERROR_01 : 존재하지 않는 사용자
		 * ERROR_02 : 아이디 또는 비밀번호 오류
		 * ERROR_03 : 권한없음
		 * ERROR_04 : 이용중지된 사용자 로그인
		 * ERROR_05 : 탈퇴한 사용자
		 * ERROR_06 : 비밀번호 5회 오류
		 * 
		 */
		
		String loginErrMsg = "";
		
		if("N".equals(loginVO.getLoginSuccess())) {
			switch (loginVO.getLoginErrCode())
			{
				case "ERROR_01":
					loginErrMsg = "존재하지 않는 사용자";
					break;
				case "ERROR_02":
					loginErrMsg = "아이디 또는 비밀번호 오류";
					loginMapper.insertPwLog(loginVO);
					break;
				case "ERROR_03":
					loginErrMsg = "권한없음";
					break;
				case "ERROR_04":
					loginErrMsg = "이용중지된 사용자 로그인";
					break;
				case "ERROR_05":
					loginErrMsg = "탈퇴한 사용자";
					break;
				case "ERROR_06":
					loginErrMsg = "비밀번호 5회 오류";
					loginMapper.insertPwLog(loginVO);
					break;
				default:
					break;
			}			
		} else {
			//로그인 성공인 경우 비밀번호 로그 삭제
			loginMapper.deletePwLog(loginVO);
		}
		
		loginVO.setLoginErrMsg(loginErrMsg);
		
		//URL 정보
		loginVO.setLoginUrl(RequestUtil.getDomain());
		
		return loginMapper.insertLoginUserLog(loginVO);
	}

	@Override
	public Map<String, Object> selectLoginLogList(SearchVO search) {
		
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = loginMapper.selectLoginLogTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<LoginLogVO> list = loginMapper.selectLoginLogList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	public LoginLogVO selectLoginLog(String loginCode) {
		return loginMapper.selectLoginLog(loginCode);
	}

	@Override
	public List<LoginLogVO> selectRecentLoginLogList() {
		return loginMapper.selectRecentLoginLogList(CodeConfig.SYS_USER_CODE);
	}

	@Override
	public List<LoginLogVO> selectRecentSysLoginLogList() {
		return loginMapper.selectRecentSysLoginLogList(CodeConfig.SYS_USER_CODE);
	}

	@Override
	@MethodDescription("로그인 로그 엑셀 다운")
	public void selectLoginLogExcelList(ExcelDownloadHandler<HashMap<String, Object>> handler, SearchVO search) {
		
		// 검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		loginMapper.selectLoginLogExcelList(paramMap, handler);		
	}

	@Override
	@MethodDescription("패스워드 로그 목록")
	public Map<String, Object> selectPwLogList(SearchVO search) {
		//검색조건 설정
		if (search == null ) search = new SearchVO();
		Map<String, Object> paramMap = search.convertMap();
		
		//전체 ROW COUNT
		int totCnt = loginMapper.selectPwLogTotCnt(paramMap);
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), 10, 10, totCnt);
		
		//시작 행과 페이지당 ROW수 조건 추가
		paramMap.put("beginRow", paging.getBeginRow());
		paramMap.put("pagePerRow", paging.getPagePerRow());
		
		//목록 조회
		List<Map<String, Object>> list = loginMapper.selectPwLogList(paramMap);
		
		//조회결과 RETURN
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("paging", paging);
		map.put("search", search);
		map.put("list", list);
		
		return map;
	}

	@Override
	@MethodDescription("패스워드 오류 초기화")
	public int deletePwLog(String userType, String userId) {
		LoginVO loginVO = new LoginVO();
		loginVO.setLoginUserType(userType);
		loginVO.setLoginId(userId);
		return loginMapper.deletePwLog(loginVO);
	}

	@Override
	public int deleteCheckedPwLog(String checkedSId) {
		int result = 0;
		LoginVO loginVO = null;
		String[] arr = checkedSId.split("\\|");
		for (int i=0; i<arr.length; i++) {
			
			String type = arr[i].split("#")[0];
			String id = arr[i].split("#")[1];
			
			loginVO = new LoginVO();
			loginVO.setLoginUserType(type);
			loginVO.setLoginId(id);
			
			result += loginMapper.deletePwLog(loginVO);
		}
		return result;
	}

	@Override
	public boolean checkSysUserExist() {
		
		//관리자계정 count
		int count = loginMapper.selectSysAdminCount(CodeConfig.SYS_USER_CODE);
		if(count > 0) {
			return true;
		}		
		
		return false;
	}

	@Override
	public List<LoginLogVO> selectRecentLoginLogListByLoginId() {
		LoginVO loginVO = SessionUtil.getLoginUser();
		if(loginVO != null) {
			return loginMapper.selectRecentLoginLogListByLoginId(loginVO.getLoginUserType(), loginVO.getLoginId());
		}
		return null;
	}	
}
