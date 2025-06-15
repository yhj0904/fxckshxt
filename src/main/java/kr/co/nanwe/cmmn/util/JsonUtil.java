package kr.co.nanwe.cmmn.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStringToMap(String str) {
		Map<String, Object> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = new HashMap<String, Object>();
			map = mapper.readValue(str, HashMap.class);
		} catch (JsonGenerationException e) {
			LOGGER.error(e.getMessage());
		} catch (JsonMappingException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		return map;
	}
}
