package kr.co.nanwe.cmmn.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.StringUtil;

/**
 * @Class Name 		: WebConfig
 * @Description 	: 전역변수 및 메소드를 선언한 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("web")
public class WebConfig {
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;

	/** server.properties */
	@Resource(name = "apiProp")
	private Properties apiProp;

	/** 데이터 처리후 결과 VIEW */
	private static final String SUCCESS_VIEW = "cmmn/result_success";
	
	/** 데이터 처리후 결과 VIEW */
	private static final String ERROR_VIEW = "cmmn/result_error";
	
	/** 로그인 후 결과 VIEW */
	private static final String LOGIN_VIEW = "cmmn/result_login";
	
	/** 권한 에러 */
	private static final String ERROR_AUTH_VIEW = "error/errorAuth";
	
	/** 템플릿 확장자 (메인 확장자) */
	private static final String _MAIN = ".main";
	
	/** 템플릿 확장자 (서브 확장자) */
	private static final String _SUB = ".sub";
	
	/** 템플릿 확장자 (로그인 확장자) */
	private static final String _LOGIN = ".login";
	
	/** 템플릿 확장자 (팝업) */
	private static final String _POP = ".pop";
	
	/** 템플릿 확장자 (빈화면) */
	private static final String _EMPTY = ".empty";
	
	/** 언어 */
	private static final List<String> KOREAN = new ArrayList<String>(Arrays.asList("ko", "ko_kr", "ko_KR"));
	
	/**
	 * JSP로 리턴하는 메소드
	 *
	 * @param jspPath : JSP 경로
	 * @return String
	 */
	public String returnJsp (String jspPath) {
		return jspPath;
	}
	
	/**
	 * JSP로 리턴하는 메소드
	 *
	 * @param viewName : view 파일명
	 * @return String
	 */
	public String returnView(String viewName) {
		String path = viewName;		
		String view = path + _SUB;
		return view;
	}
	
	/**
	 * JSP로 리턴하는 메소드
	 *
	 * @param viewPath : view 경로
	 * @param viewName : view 파일명
	 * @return String
	 */
	public String returnView(String viewPath, String viewName) {
		String path = viewPath + viewName;
		String view = path + _SUB;
		
		System.out.println("##############view="+view);
		System.out.println("##############view="+view);
		System.out.println("##############view="+view);
		System.out.println("##############view="+view);
		System.out.println("##############view="+view);
		return view;
	}
	
	/**
	 * JSP로 리턴하는 메소드
	 *
	 * @param viewPath : view 경로
	 * @param viewName : view 파일명
	 * @param template : template (MAIN, SUB, EMPTY)
	 * @return String
	 */
	public String returnView(String viewPath, String viewName, String template) {
		
		//대문자로 변경
		if(StringUtil.isNull(template)) {
			template = "SUB";
		}
		
		if(template != null && !"".equals(template)) {
			template = template.toUpperCase();
		}
		
		String path = viewPath + viewName;
		String view = path;
		
		switch (template) {
			case "MAIN":
				view += _MAIN;
				break;
			case "SUB":
				view += _SUB;
				break;
			case "LOGIN":
				view += _LOGIN;				
				break;
			case "EMPTY":
				view += _EMPTY;				
				break;
			case "POP":
				view += _POP;				
				break;
			default:
				view += "." + template;
				break;
		}
		
		return view;
	}
	
	/**
	 * 데이터 처리 성공시 결과화면 리턴
	 * @param 
	 * @return String
	 */
	public String returnSuccess() {
		return SUCCESS_VIEW;
	}
	
	/**
	 * 데이터 처리 실패시 결과화면 리턴
	 * @param 
	 * @return String
	 */
	public String returnError() {
		return ERROR_VIEW;
	}
	
	/**
	 * 로그인 후 결과화면 리턴
	 * @param 
	 * @return String
	 */
	public String returnLogin() {
		return LOGIN_VIEW;
	}
	
	/**
	 * 권한에러
	 * @param 
	 * @return String
	 */
	public String returnErrorAuth() {
		return ERROR_AUTH_VIEW;
	}
	
	/**
	 * 리다이렉트 처리
	 * @param redirectPath
	 * @return String
	 */
	public String redirect(Model model, String redirectPath) {
		if(model != null) {
			model.asMap().clear();
		}
		return "redirect:" + redirectPath;
	}
	
	/**
	 * 포워드 처리
	 * @param forwardPath
	 * @return String
	 */
	public String forward(String forwardPath) {
		return "forward:" + forwardPath;
	}
	
	/**
	 * 현재 언어를 가져온다
	 * @param 
	 * @return String
	 */
	public String getLanguage() {
		//언어 체크
		String language = "en";
		
		if(LocaleContextHolder.getLocale() != null) {
			language = LocaleContextHolder.getLocale().toString();
			language = language.toLowerCase().trim();
			if(language.equals("")) {
				language = "en";
			}
		}
		
		//설정된 언어가 한국어인경우
		if (KOREAN.contains(language)) {
			language = "ko";
		} else {
			language = "en";
		}
		return language;
	}
	
	/**
	 * 동시접속 가능한지를 체크
	 * @param 
	 * @return boolean
	 */
	public boolean isMultiLogin() {
		String multi = serverProp.getProperty("server.login.multi");
		if("Y".equals(multi)) {
			return true;
		}		
		return false;
	}
	
	/**
	 * 비밀번호 오류 회수
	 * @param 
	 * @return int
	 */
	public int getPwErrorCount() {
		int pwCount = 0;
		String count = serverProp.getProperty("server.login.pw.count");
		if(!StringUtil.isNull(count)) {
			try {
				pwCount = Integer.parseInt(count);
				if (pwCount <= 1 ) {
					pwCount = 0;
				}
			} catch (NumberFormatException e) {
				pwCount = 0;
			}
		}
		return pwCount;
	}
	
	
	/**
	 * 서버의 ROOT 절대경로를 가져온다
	 * @param 
	 * @return String
	 */
	public String getRootPath() {
		return serverProp.getProperty("server.path");
	}
	
	
	/**
	 * 서버의 VIEW(JSP) 절대경로를 가져온다
	 * @param 
	 * @return String
	 */
	public String getJspPath() {
		String rootPath = serverProp.getProperty("server.path");
		return rootPath + FileUtil.SEPERATOR+"WEB-INF"+FileUtil.SEPERATOR+"jsp";
	}
	
	/**
	 * 서버의 파일업로드 절대경로를 가져온다
	 * @param 
	 * @return String
	 */
	public String getFileRootPath() {
		return serverProp.getProperty("server.file.path");
	}
	
	/**
	 * 서버의 TEMP 절대경로를 가져온다
	 * @param 
	 * @return String
	 */
	public String getTempPath() {
		return serverProp.getProperty("server.temp.path");
	}
	
	/**
	 * 2차인증 사용유무
	 * @param 
	 * @return boolean
	 */
	public boolean isCertUse() {
		String useCert = serverProp.getProperty("server.cert.use");
		if("Y".equals(useCert)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 2차인증 방식
	 * @param 
	 * @return String
	 */
	public String getCertType() {
		String useCert = serverProp.getProperty("server.cert.use");
		if("Y".equals(useCert)) {
			String certType = serverProp.getProperty("server.cert.type");
			if(!StringUtil.isNull(certType)) {
				return certType;
			}
		}
		return null;
	}
	
	/**
	 * SSO 사용유무
	 * @param 
	 * @return boolean
	 */
	public boolean isSsoUse() {
		String useSso = serverProp.getProperty("server.sso.use");
		if("Y".equals(useSso)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 외부데이터 사용유무
	 * @param 
	 * @return boolean
	 */
	public boolean isExternalUse() {
		String useExternal = serverProp.getProperty("server.external.use");
		if("Y".equals(useExternal)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 서버 PROP
	 * @param 
	 * @return String
	 */
	public String getServerProp(String code) {
		if(StringUtil.isNull(code)) {
			return null;
		}
		String value = serverProp.getProperty(code);
		if(!StringUtil.isNull(value)) {
			return value;
		}
		return null;
	}
	
	/**
	 * API PROP
	 * @param 
	 * @return String
	 */
	public String getApiProp(String code) {
		if(StringUtil.isNull(code)) {
			return null;
		}
		String value = apiProp.getProperty(code);
		if(!StringUtil.isNull(value)) {
			return value;
		}
		return null;
	}
	
}
