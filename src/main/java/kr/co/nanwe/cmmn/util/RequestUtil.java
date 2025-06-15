package kr.co.nanwe.cmmn.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Class Name 		: RequestUtil
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class RequestUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);
	
	public static final List<String> PASS_PARAMS =  new ArrayList<String>(Arrays.asList("LOGINPW", "PASSWORD", "PASSWORDCHECK", "SALTKEY"));
	
	//LUCY WEB FILTER 파라미터
	public static final List<String> DENIED_PARAM_LIST = new ArrayList<String>(Arrays.asList(
		"returnLogin","content","contCont","contEngCont","contents","survMemo","header","footer","popCont","siteMeta","indexCode","indexCss"
		,"mainCode","subCode","loginCode","emptyCode","popCode","layoutHeader","layoutFooter","layoutGnb","mainCss","subCss","loginCss","emptyCss","popCss","layoutCss"
	));
	
	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest hsr = sra.getRequest();
		return hsr;
	}
	
	public static HttpServletResponse getCurrentResponse() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletResponse hsr = sra.getResponse();
		return hsr;
	}
	
	public static String getRequestURL() {
		HttpServletRequest request = getCurrentRequest();
		String str = request.getRequestURL().toString();
		return str;
	}
	
	public static String getDomain() {
		HttpServletRequest request = getCurrentRequest();
		String str = "";
		String url = request.getRequestURL().toString();
		Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
		Matcher mc = urlPattern.matcher(url);
		if (mc.matches()) {
			str = mc.group(2);
		}
		
		String port = getPort();
		if(!StringUtil.isNull(port)) {
			str += port;
		}
		
		return str;
	}
	
	public static String getDomainByUrl(String url) {
		if(url == null) {
			return "";
		}		
		url = url.replaceAll("http://", "").replaceAll("https://", "");
		int idx = url.indexOf("/");
		if(idx > 0) {
			url = url.substring(0, idx);
		}		
		return url;
	}
	
	public static String getPort() {
		HttpServletRequest request = getCurrentRequest();
		String str = "";
		int port = request.getServerPort();
		if(port != 80) {
			str = ":" + port;
		}
		return str;
	}
	
	public static String getRealPath() {
		HttpServletRequest request = getCurrentRequest();
		String str = request.getServletContext().getRealPath("");
		return str;
	}
	
	public static String getProtocol() {
		HttpServletRequest request = getCurrentRequest();
		return request.isSecure() ? "https://" : "http://";
	}
	
	public static String getURI() {
		HttpServletRequest request = getCurrentRequest();
		String str = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		return str;
	}
	
	public static String getRequestURI() {
		HttpServletRequest request = getCurrentRequest();
		String str = request.getRequestURI().toString();
		return str;
	}
	
    public static Map<String, String> getQueryMap(String url) {    	
    	if (url == null) {
    		return null;
    	}
    	
    	int pos1 = url.indexOf("?");
    	if (pos1 >= 0) {
    		url = url.substring(pos1+1);
    	}
    	
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
	
	public static MultiValueMap<String, String> getParamters(String url) {

		MultiValueMap<String, String> map = null;

		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
		}

		// amp; 치환
		url = url.replaceAll("&amp;", "&");

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
		map = uriComponentsBuilder.build().getQueryParams();

		return map;
	}
	
	public static boolean isRestRequest(HttpServletRequest request) {
		if(request.getAttribute("IS_REST_REQUEST") != null && "true".equals(request.getAttribute("IS_REST_REQUEST"))){
			return true;
		}
		String uri = RequestUtil.getRequestURI();
		if(uri != null) {
			String ext = StringUtil.getExtension(uri);
			if((uri.startsWith("/api/") && !"do".equals(ext)) || "json".equals(ext)) {
				return true;
			}
		}
		return false;
	}
}
