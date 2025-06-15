package kr.co.nanwe.cmmn.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
	
	private static final String PROP_PATH = "egovframework/prop/";
	
	private static HashMap<String, Properties> propses;

	public static String get(String prop, String key) {
		if (load(prop) == true) {
			return propses.get(prop).getProperty(key);
		} else {
			return "";
		}
	}

	/**
	 * Properties 가 적재되어 있는지 확인 적재되어 있지 않다면 적재함. properties 파일이 없을 경우 false return.
	 *
	 * @param prop
	 * @return
	 */
	private static boolean load(String prop) {

		// 초기화
		if (propses == null) {
			init();
		}

		if (propses.containsKey(prop)) {
			return true;
		} else {
			try {
				StringBuilder path = new StringBuilder();
				path.append(PROP_PATH).append(prop).append(".properties");
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path.toString());
				Properties props = new Properties();
				props.load(is);

				propses.put(prop, props);

				is.close();

				return true;
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
				return false;
			}
		}
	}

	/**
	 * 초기화
	 */
	private static void init() {
		propses = new HashMap<String, Properties>();
	}
}