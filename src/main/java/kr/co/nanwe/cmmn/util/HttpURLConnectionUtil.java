package kr.co.nanwe.cmmn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @Class Name 		: HttpURLConnectionUtil
 * @Description 	: URL CONNECTION
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.24		신한나			최초생성
 */

public class HttpURLConnectionUtil {
	public static HttpURLConnection openHttpUrlConnection(String url) throws Exception {
		if (url == null || "".equals(url))
			return null;
		if (url.startsWith("https://"))
			return openHttpsUrlConnection(url);
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		return conn;
	}

	public static HttpURLConnection openHttpsUrlConnection(String url) throws Exception {
		if (url == null || "".equals(url))
			return null;
		if (url.startsWith("http://"))
			return openHttpUrlConnection(url);
		
		TrustManager[] trustAllCerts = { (TrustManager) new Object() };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		URL _url = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) _url.openConnection();
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		return conn;
	}

	public static int getResponseCode(HttpURLConnection conn) throws Exception {
		return conn.getResponseCode();
	}

	public static String getResponseString(HttpURLConnection conn) {
		String responseStr = null;
		BufferedReader br = null;
		InputStreamReader ir = null;
		try {
			ir = new InputStreamReader(conn.getInputStream(), "UTF-8");
			br = new BufferedReader(ir);
			StringBuffer responseBody = new StringBuffer();
			String output;
			while ((output = br.readLine()) != null)
				responseBody.append(output);
			responseStr = responseBody.toString();
		} catch (Exception e) {
			responseStr = null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException iOException) {
				}
			if (ir != null)
				try {
					ir.close();
				} catch (IOException iOException) {
				}
		}
		return responseStr;
	}

	public static JSONObject getResponseJSONObject(HttpURLConnection conn) {
		JSONObject jsonObject = null;
		String jsonStr = getResponseString(conn);
		if (jsonStr != null && !"".equals(jsonStr)) {
			JSONParser parser = new JSONParser();
			try {
				jsonObject = (JSONObject) parser.parse(jsonStr);
			} catch (ParseException e) {
				jsonObject = null;
			}
		}
		return jsonObject;
	}

	public static void disconnect(HttpURLConnection conn) {
		if (conn != null)
			conn.disconnect();
	}
}
