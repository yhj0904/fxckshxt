package kr.co.nanwe.user.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.nanwe.cmmn.config.CodeConfig;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.DateUtil;
import kr.co.nanwe.cmmn.util.EncryptUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.TempPasswordUtil;
import kr.co.nanwe.login.service.LoginVO;
import kr.co.nanwe.user.service.SnsUserService;
import kr.co.nanwe.user.service.UserVO;

/**
 * @Class Name 		: SnsUserServiceImpl
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Service("snsUserService")
public class SnsUserServiceImpl extends EgovAbstractServiceImpl implements SnsUserService {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(SnsUserServiceImpl.class);
	
	/** Web Config */
	@Resource(name = "web")
	private WebConfig web;

	@Resource(name = "userMapper")
	private UserMapper userMapper;

	@Resource(name = "snsUserMapper")
	private SnsUserMapper snsUserMapper;

	@Override
	public boolean checkSnsUserExist(UserVO userVO) {
		int count = snsUserMapper.checkSnsUserExist(userVO);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public LoginVO selectLoginSnsUser(UserVO userVO) {
		
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
		String loginType = "SNS";
		String loginIp = ClientUtil.getUserIp();
		String loginOs = ClientUtil.getUserOS();
		String loginDevice = ClientUtil.getUserDevice();
		HashMap<String,String> browser = ClientUtil.getUserBrowser();
		String typeKey = browser.get(ClientUtil.TYPEKEY);
		String versionKey = browser.get(ClientUtil.VERSIONKEY);
		String loginBrowser = typeKey + " " + versionKey;
		String loginDttm = DateUtil.getDateTime();
		
		loginInfo.setLoginType(loginType);
		loginInfo.setLoginId(userVO.getUserId());		
		loginInfo.setLoginIp(loginIp);
		loginInfo.setLoginOs(loginOs);
		loginInfo.setLoginDevice(loginDevice);
		loginInfo.setLoginBrowser(loginBrowser);
		loginInfo.setLoginDttm(loginDttm);
		loginInfo.setSnsType(userVO.getSnsType());
		loginInfo.setSnsId(userVO.getSnsId());
		loginInfo.setLoginUserType(CodeConfig.COM_USER_CODE);
		
		//조회 결과
		LoginVO resultLoginVO = snsUserMapper.selectLoginSnsUser(userVO);
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
				loginInfo.setUserImgSrc(userVO.getViewFile().getViewUrl());
				
				loginSuccess = "Y";
				loginErrCode = "";
				
			} else {
				loginErrCode = "ERROR_04"; //이용중지된 사용자 로그인
			}	
		}
		
		loginInfo.setLoginSuccess(loginSuccess);
		loginInfo.setLoginErrCode(loginErrCode);
		
		return loginInfo;
	}

	@Override
	public int insertSnsUser(UserVO userVO) {
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
		TempPasswordUtil tempPassword = new TempPasswordUtil();
		String password = tempPassword.getTempPassword();
		String saltKey = EncryptUtil.getSalt();
		password = EncryptUtil.encryptMsgWithSalt(password, saltKey);
		userVO.setSaltKey(saltKey);
		userVO.setPassword(password);
		
		//등록 처리 (등록된 경우 row 수 return)
		int result = userMapper.insertUser(userVO);
		if(result > 0) {
			snsUserMapper.insertSnsUser(userVO);
		}
		return result;
	}
}
