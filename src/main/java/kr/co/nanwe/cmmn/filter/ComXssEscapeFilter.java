package kr.co.nanwe.cmmn.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilterRule;

/**
 * @Class Name 		: ComXssEsscapeFilter
 * @Description 	: Xss 공격 방지 필터
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public final class ComXssEscapeFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComXssEscapeFilter.class);

	private static ComXssEscapeFilter xssEscapeFilter;
	private static ComXssEscapeFilterConfig config;

	static {
		try {
			xssEscapeFilter = new ComXssEscapeFilter();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Default Constructor
	 */
	private ComXssEscapeFilter() {
		config = new ComXssEscapeFilterConfig();
	}

	/**
	 * @return XssEscapeFilter
	 */
	public static ComXssEscapeFilter getInstance() {
		return xssEscapeFilter;
	}

	/**
	 * @param url String
	 * @param paramName String
	 * @param value String
	 * @return String
	 */
	public String doFilter(String url, String paramName, String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}

		XssEscapeFilterRule urlRule = config.getUrlParamRule(url, paramName);
		if (urlRule == null) {
			// Default defender 적용
			return config.getDefaultDefender().doFilter(value);
		} 

		if (!urlRule.isUseDefender()) {
			log(url, paramName, value);
			return value;
		}

		return urlRule.getDefender().doFilter(value);
	}

	/**
	 * @param url String
	 * @param paramName String
	 * @param value String
	 * @return void
	 */
	private void log(String url, String paramName, String value) {
		if (LOGGER.isDebugEnabled()) {
			//LOGGER.debug("Do not filtered Parameter. Request url: " + url + ", Parameter name: " + paramName + ", Parameter value: " + value);
			LOGGER.debug("Do not filtered Parameter. Request url: " + url + ", Parameter name: " + paramName);
		}
	}
}
