package kr.co.nanwe.cmmn.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * @Class Name 		: ClientUtil
 * @Description 	: 클라이언트 체크를 위한 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ClientUtil {
	
	public static final String FIREFOX = "Firefox";
	public static final String SAFARI = "Safari";
	public static final String CHROME = "Chrome";
	public static final String OPERA = "Opera";
	public static final String MSIE = "MSIE";
	public static final String EDGE = "Edge";
	public static final String OTHER = "Other";
	
	public static final String TYPEKEY = "type";
	public static final String VERSIONKEY = "version";

	public static String getUserIp() {
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		
		//ipv6 0:0:0:0:0:0:0:1 인경우 > localhost 로 변경
		if("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "localhost";
		}
		return ip;
	}
	
	public static String getUserDevice() {
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		String IS_MOBILE = "MOBILE";
		String IS_PC = "PC";
		String userAgent = request.getHeader("User-Agent").toUpperCase();
		if (userAgent.indexOf(IS_MOBILE) > -1) {
			return IS_MOBILE;
		} else {
			return IS_PC;
		}
	}
	
	public static HashMap<String,String> getUserBrowser() {
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		String userAgent = request.getHeader("User-Agent");
		
		HashMap<String,String> result = new HashMap<String,String>();
		Pattern pattern = null;
		Matcher matcher = null;
		
		pattern = Pattern.compile("MSIE ([0-9]{1,2}.[0-9])");
		matcher = pattern.matcher(userAgent);
		if (matcher.find())
		{
		    result.put(TYPEKEY,MSIE);
		    result.put(VERSIONKEY,matcher.group(1));
			return result;
		}
		
		if (userAgent.indexOf("Trident/7.0") > -1) {
		    result.put(TYPEKEY,MSIE);
		    result.put(VERSIONKEY,"11.0");
		    return result;
		}
		
		pattern = Pattern.compile("Edge/([0-9]{1,3}.[0-9]{1,5})");
		matcher = pattern.matcher(userAgent);
		if (matcher.find())
		{
		    result.put(TYPEKEY,EDGE);
		    result.put(VERSIONKEY,matcher.group(1));
			return result;
		}
		
		pattern = Pattern.compile("Firefox/([0-9]{1,3}.[0-9]{1,3})");
		matcher = pattern.matcher(userAgent);
		if (matcher.find())
		{
		    result.put(TYPEKEY,FIREFOX);
		    result.put(VERSIONKEY,matcher.group(1));
			return result;		    
		}

		pattern = Pattern.compile("OPR/([0-9]{1,3}.[0-9]{1,3})");
		matcher = pattern.matcher(userAgent);
		if (matcher.find())
		{
		    result.put(TYPEKEY,OPERA);
		    result.put(VERSIONKEY,matcher.group(1));
			return result;		    
		}
		
		pattern = Pattern.compile("Chrome/([0-9]{1,3}.[0-9]{1,3})");
		matcher = pattern.matcher(userAgent);
		if (matcher.find())
		{
		    result.put(TYPEKEY,CHROME);
		    result.put(VERSIONKEY,matcher.group(1));
			return result;		    
		}
		
		pattern = Pattern.compile("Version/([0-9]{1,2}.[0-9]{1,3})");
		matcher = pattern.matcher(userAgent);
		if (matcher.find())
		{
		    result.put(TYPEKEY,SAFARI);
		    result.put(VERSIONKEY,matcher.group(1));
			return result;		    
		}

	    result.put(TYPEKEY,OTHER);
	    result.put(VERSIONKEY,"0.0");
		return result;
	}
	
	public static String getUserOS() {
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		String userAgent = request.getHeader("User-Agent");
		String os = "";

		userAgent = userAgent.toLowerCase();

		if (userAgent.indexOf("windows nt 6.1") > -1) {
			os = "WINDOWS 7";
		} else if (userAgent.indexOf("windows nt 6.2") > -1 || userAgent.indexOf("windows nt 6.3") > -1) {
			os = "WINDOWS 8";
		} else if (userAgent.indexOf("windows nt 6.0") > -1) {
			os = "WINDOWS VISTA";
		} else if (userAgent.indexOf("windows nt 5.1") > -1) {
			os = "WINDOWS XP";
		} else if (userAgent.indexOf("windows nt 5.0") > -1) {
			os = "WINDOWS 2000";
		} else if (userAgent.indexOf("windows nt 4.0") > -1) {
			os = "WINDOWS NT";
		} else if (userAgent.indexOf("windows 98") > -1) {
			os = "WINDOWS 98";
		} else if (userAgent.indexOf("windows 95") > -1) {
			os = "WINDOWS 95";
		}
		// window 외
		else if (userAgent.indexOf("iphone") > -1) {
			os = "IPHONE";
		} else if (userAgent.indexOf("ipad") > -1) {
			os = "IPAD";
		} else if (userAgent.indexOf("android") > -1) {
			os = "ANDROID";
		} else if (userAgent.indexOf("mac") > -1) {
			os = "MAC";
		} else if (userAgent.indexOf("linux") > -1) {
			os = "LINUX";
		} else if (userAgent.indexOf("windows") > -1) {
			os = "WINDOWS";
		} else {
			os = "OTHER";
		}
		return os;
	}
	
	public static String getDisposition(String filename, String charSet) throws Exception {
		String encodedFilename = null;
		HashMap<String,String> result = ClientUtil.getUserBrowser();
		float version = Float.parseFloat(result.get(ClientUtil.VERSIONKEY));
		if ( ClientUtil.MSIE.equals(result.get(ClientUtil.TYPEKEY)) && version <= 8.0f ) {
			encodedFilename = "Content-Disposition: attachment; filename="+URLEncoder.encode(filename, charSet).replaceAll("\\+", "%20");
		} else if ( ClientUtil.OTHER.equals(result.get(ClientUtil.TYPEKEY)) ) {
			throw new RuntimeException("Not supported browser");
		} else {
			encodedFilename = "attachment; filename*="+charSet+"''"+URLEncoder.encode(filename, charSet);
		}
		
		return encodedFilename;
	}
	
	public static String getUserRefererPage() {
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		String referer = (String)request.getHeader("REFERER");
		String refererPage = "";
		if (StringUtil.isNull(referer)) {
			return refererPage;
		}
		
    	int index = referer.indexOf("?");
    	if (index >= 0) {
    		refererPage = referer.substring(0, index);
    	}
		return refererPage;
	}
	
	public static String getUserRefererKeyword() {
		HttpServletRequest request = RequestUtil.getCurrentRequest();
		String referer = (String)request.getHeader("REFERER");
		String refererPage = "";
		String query = "";
		String keyword = "";
		if (StringUtil.isNull(referer)) {
			return null;
		}
		int index = referer.indexOf("?");
    	if (index >= 0) {
    		refererPage = referer.substring(0, index);
    		query = referer.substring(index+1);
    	}

    	if(!StringUtil.isNull(query)) {
    		String[] params = query.split("&");
            Map<String, String> map = new HashMap<String, String>();
            for (String param : params){
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            String keyName = getSearchKeyName(refererPage);
            if(keyName != null) {
            	keyword = map.get(keyName);
            }
    	}
        
		return keyword;
	}
	
	private static String getSearchKeyName(String url) {
		
		//네이버
		if ( url.indexOf("naver.com") > -1 ) {
			return "query";
		}
		
		//다음, 네이트, 구글,  MSN, BING
		if ( url.indexOf("daum.com") > -1  
			|| url.indexOf("nate.com") > -1
			|| url.indexOf("google.com") > -1
			|| url.indexOf("google.co") > -1
			|| url.indexOf("msn.com") > -1
			|| url.indexOf("bing.com") > -1) { 
			return "query";
		}
		
		//야후
		if (url.indexOf("yahoo.com") > -1) {
			return "p";
		}
		
		//ASK, Altavista, Altherweb, Live, Najdi, Seznam, Search, Szukacz, PCHome, Kvasir, Sesam, Ozu, Mynet, Ekolay 
		if (url.indexOf("daum.com") > -1) {
			return "q";
		}
		
		//AOL, Lycos, Netscape, CNN, Mamma, Mama, Terra
		if (url.indexOf("daum.com") > -1) {
			return "query";
		}
		
		//Eniro
		if (url.indexOf("daum.com") > -1) {
			return "search_word";
		}
		
		return null;
	}
	
}