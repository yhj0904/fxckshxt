package kr.co.nanwe.login.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.util.JsonUtil;
import kr.co.nanwe.cmmn.util.RequestUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.ViewFileVO;
import kr.co.nanwe.user.service.UserVO;

@Component("google")
public class GoogleManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleManager.class);
	
	private static final String REDIRECT_URI = "/login/sns/google.do";
	
	@Resource(name = "snsProp")
	private Properties snsProp;
	
	public String getAccessToken(HttpServletRequest request) {
		
		String snsUse = snsProp.getProperty("sns.use");
		String googleUse = snsProp.getProperty("sns.google.use");
		
		//SNS 사용중이 아니면 false
		if(!StringUtil.isEqual(snsUse, "Y") || !StringUtil.isEqual(googleUse, "Y")) {
			return null;
		}

		//KAKAO API키
		String apiKey = snsProp.getProperty("sns.google.apiKey");
		if(StringUtil.isNull(apiKey)) {
			return null;
		}
		
		String accessToken = null;
		
		String siteUrl = RequestUtil.getProtocol() + RequestUtil.getDomain();
		
		BufferedWriter bw = null;
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection con = null;
		String inputLine = null;
		
		try {
			
			String code = request.getParameter("code");
			
			String redirectURI = URLEncoder.encode(siteUrl + REDIRECT_URI, "UTF-8");
			
			String apiURL = "https://kauth.google.com/oauth/token";
			
			accessToken = "";
			
			url = new URL(apiURL);
			
			con = (HttpURLConnection) url.openConnection();
            
			con.setRequestMethod("POST");
			con.setDoOutput(true);
            
            bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + apiKey);
            sb.append("&redirect_uri="+redirectURI);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();
            
            int responseCode = con.getResponseCode();
            
            if(responseCode == 200) {
            	
            	br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String responseStr = "";							
				while ((inputLine = br.readLine()) != null) {
					responseStr += inputLine;
				}
				Map<String, Object> responseMap = JsonUtil.jsonStringToMap(responseStr);
				
				br.close();
				
				if(responseMap != null) {
					if (responseMap.get("access_token") != null) {
						accessToken = (String) responseMap.get("access_token");
					}
				}
            }
            
		} catch (IOException e) {						
			LOGGER.error(e.getMessage());						
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}		
		}
		
		return accessToken;
	}
	
	public UserVO getUserInfo(HttpServletRequest request) {		
		//ACCESS TOKEN 검증
		String accessToken = getAccessToken(request);
		if(accessToken == null) {
			return null;
		}
		return getUserInfo(accessToken);
	}
	
	public UserVO getUserInfo(String accessToken) {

		if(accessToken == null) {
			return null;
		}
		
		UserVO user = null;	
		BufferedWriter bw = null;
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection con = null;
		String inputLine = null;
		
		try {
			
			//회원정보 조회
	        String header = "Bearer " + accessToken;
			
	        String apiURL = "https://kapi.google.com/v2/user/me";
            url = new URL(apiURL);
            
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", header);
            
            int responseCode = con.getResponseCode();
            
            if(responseCode==200) {
            	
            	br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            	
	            String userStr = "";							
				while ((inputLine = br.readLine()) != null) {
					userStr += inputLine;
				}
				
				Map<String, Object> userMap = JsonUtil.jsonStringToMap(userStr);
				if(userMap != null) {
					String userId = "google@" + userMap.get("id");
					user = new UserVO();
					user.setAccessToken(accessToken);
					user.setUserId(userId);
					user.setUserNm(StringUtil.isNullToString(userMap.get("nickname")));
					user.setEmail(StringUtil.isNullToString(userMap.get("email")));
					
					//프로필이미지
					ViewFileVO viewFile = new ViewFileVO();
					viewFile.setViewUrl(StringUtil.isNullToString(userMap.get("profile_image_url")));
					user.setViewFile(viewFile);
					
					user.setSnsType("google");
					user.setSnsId(StringUtil.isNullToString(userMap.get("id")));
				}
            }
            
		} catch (IOException e) {						
			LOGGER.error(e.getMessage());						
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}		
		}
		
		return user;
	}
}
