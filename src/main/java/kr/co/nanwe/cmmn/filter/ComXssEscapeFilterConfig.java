package kr.co.nanwe.cmmn.filter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilterRule;
import com.navercorp.lucy.security.xss.servletfilter.defender.Defender;

import kr.co.nanwe.cmmn.bean.ProgramControllerScan;

public class ComXssEscapeFilterConfig  {

	private static final String DEFAULT_FILTER_RULE_FILENAME = "lucy-xss-servlet-filter-rule.xml";

	private static final Logger LOGGER = LoggerFactory.getLogger(ComXssEscapeFilter.class);
	
	private Map<String, Map<String, XssEscapeFilterRule>> urlRuleSetMap = new HashMap<String, Map<String, XssEscapeFilterRule>>();
	private Map<String, XssEscapeFilterRule> globalParamRuleMap = new HashMap<String, XssEscapeFilterRule>();
	private Map<String, Defender> defenderMap = new HashMap<String, Defender>();
	private Defender defaultDefender = null;

	/**
	 * Default Constructor
	 */
	public ComXssEscapeFilterConfig() throws IllegalStateException {
		this(DEFAULT_FILTER_RULE_FILENAME);
	}

	/**
	 * Constructor
	 *
	 * @param filename String
	 */
	public ComXssEscapeFilterConfig(String filename) throws IllegalStateException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			Element rootElement = builder.parse(is).getDocumentElement();

			// defender 설정
			addDefenders(rootElement);
			
			// defaultDefender 설정
			addDefaultInfo(rootElement);

			// globalParam 설정
			addGlobalParams(rootElement);

			// urlRule 설정
			addUrlRuleSet(rootElement);

		} catch (Exception e) {
			String message = String.format("Cannot parse the RequestParam configuration file [%s].", filename);
			throw new IllegalStateException(message, e);
		}
	}

	/**
	 * @param rootElement Element
	 * @return void
	 */
	private void addDefaultInfo(Element rootElement) {
		NodeList nodeList = rootElement.getElementsByTagName("default");
		if (nodeList.getLength() > 0) {
			Element element = (Element)nodeList.item(0);
			addDefaultInfoItems(element);
		}
	}

	/**
	 * @param element Element
	 * @return void
	 */
	private void addDefaultInfoItems(Element element) {
		NodeList nodeList = element.getElementsByTagName("defender");
		if (nodeList.getLength() > 0) {
			defaultDefender = defenderMap.get(nodeList.item(0).getTextContent());
			
			if (defaultDefender == null) {
				LOGGER.error("Error config 'Default defender': Not found '" + nodeList.item(0).getTextContent() + "'");
			}
		}
	}

	/**
	 * @param rootElement Element
	 * @return void
	 */
	private void addGlobalParams(Element rootElement) {
		NodeList nodeList = rootElement.getElementsByTagName("global");
		if (nodeList.getLength() > 0) {
			Element params = (Element)nodeList.item(0);
			NodeList paramNodeList = params.getElementsByTagName("params");
			
			if (paramNodeList.getLength() > 0) {
				globalParamRuleMap = createRequestParamRuleMap((Element)nodeList.item(0));
			}			
		}
	}

	/**
	 * @param rootElement Element
	 * @return void
	 */
	private void addUrlRuleSet(Element rootElement) {
		NodeList nodeList = rootElement.getElementsByTagName("url-rule");
		for (int i = 0; nodeList.getLength() > 0 && i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			addUrlRule(element);
		}
	}

	/**
	 * @param element Element
	 * @return void
	 */
	private void addUrlRule(Element element) {
		
		Map<String, XssEscapeFilterRule> paramRuleMap = null;
		NodeList params = element.getElementsByTagName("params");
		if (params.getLength() > 0) {
			paramRuleMap = createRequestParamRuleMap((Element)params.item(0));
		}
		
		if(paramRuleMap != null) {
			NodeList urlNodeList = element.getElementsByTagName("program");
			if (urlNodeList.getLength() > 0) {
				addUrlRuleByProgram((Element) urlNodeList.item(0), paramRuleMap);		
			} else {
				urlNodeList = element.getElementsByTagName("urls");
				if (urlNodeList.getLength() > 0) {
					addUrlRuleByList((Element) urlNodeList.item(0), paramRuleMap);		
				} else {			
					urlNodeList = element.getElementsByTagName("url");
					if (urlNodeList.getLength() > 0) {
						addUrlRuleByOne((Element) urlNodeList.item(0), paramRuleMap);
					}
				}
			}
		}
	}
	
	/**
	 * @param Element urlElement
	 * @param Map<String, XssEscapeFilterRule> paramRuleMap
	 * @return void
	 */
	private void addUrlRuleByProgram(Element urlElement, Map<String, XssEscapeFilterRule> paramRuleMap) {		
		String programCd = urlElement.getTextContent();		
		List<Map<String, Object>> programList = ProgramControllerScan.getProgramInfoList(programCd);
		if(programList != null) {
			for(Map<String, Object> map : programList) {
				if(map.get("uri") != null) {
					urlRuleSetMap.put((String)map.get("uri"), paramRuleMap);
				}
			}
		}
		
	}
	
	/**
	 * @param Element urlElement
	 * @param Map<String, XssEscapeFilterRule> paramRuleMap
	 * @return void
	 */
	private void addUrlRuleByOne(Element urlElement, Map<String, XssEscapeFilterRule> paramRuleMap) {		
		String url = urlElement.getTextContent();		
		// url이 disable인지 확인, disable 이라면 param 정보를 가질 필요가 없이 그대로 빠져나가면 된다.
		if (addUrlDisableRule(url, urlElement)) {
			return;
		}
		urlRuleSetMap.put(url, paramRuleMap);	
	}

	/**
	 * @param Element urlElement
	 * @param Map<String, XssEscapeFilterRule> paramRuleMap
	 * @return void
	 */
	private void addUrlRuleByList(Element urlElement, Map<String, XssEscapeFilterRule> paramRuleMap) {
		NodeList nodeList = urlElement.getElementsByTagName("url");
		for (int i = 0; nodeList.getLength() > 0 && i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			addUrlRuleByOne(element, paramRuleMap);
		}
	}

	/**
	 * @param url String
	 * @param nodeList NodeList
	 * @return boolean
	 */
	private boolean addUrlDisableRule(String url, Element element) {
		Map<String, XssEscapeFilterRule> paramRuleMap = null;
		boolean result = false;
		
		if (!url.isEmpty()) {
			boolean disable = StringUtils.equalsIgnoreCase(element.getAttribute("disable"), "true") ? true : false;
			paramRuleMap = createRequestParamRuleMap(url, disable);
			
			if (paramRuleMap != null) {
				urlRuleSetMap.put(url, paramRuleMap);
				result = true;
			}
		}
		
		return result;
	}

	/**
	 * @param element Element
	 * @return Map<String, XssEscapeFilterRule>
	 */
	private Map<String, XssEscapeFilterRule> createRequestParamRuleMap(Element element) {
		Map<String, XssEscapeFilterRule> urlRuleMap = new HashMap<String, XssEscapeFilterRule>();

		NodeList nodeList = element.getElementsByTagName("param");
		for (int i = 0; nodeList.getLength() > 0 && i < nodeList.getLength(); i++) {
			Element eachElement = (Element)nodeList.item(i);
			String name = eachElement.getAttribute("name");
			boolean useDefender = StringUtils.equalsIgnoreCase(eachElement.getAttribute("useDefender"), "false") ? false : true;
			boolean usePrefix = StringUtils.equalsIgnoreCase(eachElement.getAttribute("usePrefix"), "true") ? true : false;
			Defender defender = null;

			NodeList defenderNodeList = eachElement.getElementsByTagName("defender");
			if (defenderNodeList.getLength() > 0) {
				defender = defenderMap.get(defenderNodeList.item(0).getTextContent());
				
				if (defender == null) {
					LOGGER.error("Error config 'param defender': Not found '" + nodeList.item(0).getTextContent() + "'");
				}
			} else {
				defender = defaultDefender;
			}

			XssEscapeFilterRule urlRule = new XssEscapeFilterRule();
			urlRule.setName(name);
			urlRule.setUseDefender(useDefender);
			urlRule.setDefender(defender);
			urlRule.setUsePrefix(usePrefix);

			urlRuleMap.put(name, urlRule);
		}

		return urlRuleMap;
	}

	/**
	 * @param url String
	 * @param disable boolean
	 * @return Map<String, XssEscapeFilterRule>
	 */
	private Map<String, XssEscapeFilterRule> createRequestParamRuleMap(String url, boolean disable) {
		if (!disable) {
			return null;
		}
		
		Map<String, XssEscapeFilterRule> urlRuleMap = new HashMap<String, XssEscapeFilterRule>();
		XssEscapeFilterRule urlRule = new XssEscapeFilterRule();
		urlRule.setName(url);
		urlRule.setUseDefender(false);
		urlRuleMap.put(url, urlRule);
		
		return urlRuleMap;
	}

	/**
	 * @param rootElement Element
	 * @return void
	 */
	private void addDefenders(Element rootElement) {
		NodeList nodeList = rootElement.getElementsByTagName("defenders");

		if (nodeList.getLength() > 0) {
			Element element = (Element)nodeList.item(0);
			addDefender(element);
		}
	}

	/**
	 * @param element Element
	 * @return void
	 */
	private void addDefender(Element element) {
		NodeList nodeList = element.getElementsByTagName("defender");
		for (int i = 0; nodeList.getLength() > 0 && i < nodeList.getLength(); i++) {
			Element eachElement = (Element)nodeList.item(i);
			String name = getTagContent(eachElement, "name");
			String clazz = getTagContent(eachElement, "class");
			String[] args = getInitParams(eachElement);
			addDefender(name, clazz, args);
		}
	}

	/**
	 * @param name String
	 * @param clazz String
	 * @param args String[]
	 * @return void
	 */
	private void addDefender(String name, String clazz, String[] args) {
		// TODO 필수 파라미터의 검증은 향후 DTD나 XSL등 XML 정합성체크에 맡겨야함
		if (StringUtils.isBlank(name) || StringUtils.isBlank(clazz)) {
			String message = String.format("The defender's name('%s') or clazz('%s') is empty. This defender is ignored", name, clazz);
			LOGGER.warn(message);
			return;
		}
		try {
			Defender defender = (Defender)Class.forName(clazz.trim()).newInstance();
			defender.init(args);
			defenderMap.put(name, defender);
		} catch (InstantiationException e) {
			rethrow(name, clazz, e);
		} catch (IllegalAccessException e) {
			rethrow(name, clazz, e);
		} catch (ClassNotFoundException e) {
			rethrow(name, clazz, e);

		}
	}

	/**
	 * @param name String
	 * @param clazz String
	 * @param e Exception
	 * @return void
	 */
	private void rethrow(String name, String clazz, Exception e) {
		String message = String.format("Fail to add defender: name=%s, class=%s", name, clazz);
		throw new IllegalStateException(message, e);
	}

	/**
	 * @param eachElement Element
	 * @return String[]
	 */
	private String[] getInitParams(Element eachElement) {
		NodeList initParamNodeList = eachElement.getElementsByTagName("init-param");
		if (initParamNodeList.getLength() == 0) {
			return new String[]{};
		}
		Element paramValueElement = (Element)initParamNodeList.item(0);
		NodeList paramValueNodeList = paramValueElement.getElementsByTagName("param-value");
	
		String[] args = new String[paramValueNodeList.getLength()];
		for (int j = 0; paramValueNodeList.getLength() > 0 && j < paramValueNodeList.getLength(); j++) {
			args[j] = paramValueNodeList.item(j).getTextContent();
		}
		return args;
	}

	/**
	 * @param eachElement Element
	 * @param tagName String
	 * @return String
	 */
	private String getTagContent(Element eachElement, String tagName) {
		NodeList nodeList = eachElement.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		}
		return "";
	}

	/**
	 * @param url String
	 * @param paramName String
	 * @return XssEscapeFilterRule
	 */
	public XssEscapeFilterRule getUrlParamRule(String url, String paramName) {
		
		if(url != null) {
			if(url.startsWith("/board/")) {
				url = "/board/{path}/list.do";
			} else if(url.startsWith("/schedule/")) {
				url = "/schedule/{path}/list.do";
			}
		}
		
		Map<String, XssEscapeFilterRule> urlParamRuleMap = urlRuleSetMap.get(url);
		XssEscapeFilterRule paramRule = null;
		
		if (urlParamRuleMap == null) {
			paramRule = checkGlobalParamRule(paramName);
		} else {
			//param rule 확인
			paramRule = checkParamRule(urlParamRuleMap, url, paramName);
		}
		
		return paramRule;
	}

	/**
	 * @param paramName String
	 * @return XssEscapeFilterRule
	 */
	private XssEscapeFilterRule checkGlobalParamRule(String paramName) {
		XssEscapeFilterRule paramRule = globalParamRuleMap.get(paramName);
		
		// paramRule이 null이면 prefix 확인
		if (paramRule == null) {
			paramRule = checkPrefixParameter(paramName, null, globalParamRuleMap);
		}
		
		return paramRule;
	}

	/**
	 * @param urlParamRuleMap Map<String, XssEscapeFilterRule>
	 * @param url String
	 * @param paramName String
	 * @return XssEscapeFilterRule
	 */
	private XssEscapeFilterRule checkParamRule(Map<String, XssEscapeFilterRule> urlParamRuleMap, String url, String paramName) {
		XssEscapeFilterRule paramRule = urlParamRuleMap.get(paramName);
		
		if (paramRule == null) {
			// url 전체 disable 설정되었는지 확인
			paramRule = checkDisableUrl(url, paramRule, urlParamRuleMap);
			
			// prefix 설정이 적용된 파라메터인지 확인 필요
			paramRule = checkPrefixParameter(paramName, paramRule, urlParamRuleMap);
			
			if (paramRule == null) {
				paramRule = globalParamRuleMap.get(paramName);
			}
		}
		return paramRule;
	}

	/**
	 * @param url String
	 * @param paramRule XssEscapeFilterRule
	 * @param urlParamRuleMap Map<String, XssEscapeFilterRule>
	 * @return XssEscapeFilterRule
	 */
	private XssEscapeFilterRule checkDisableUrl(String url, XssEscapeFilterRule paramRule, Map<String, XssEscapeFilterRule> urlParamRuleMap) {
		if (paramRule != null) {
			return paramRule;
		}
		
		if (urlParamRuleMap.containsKey(url) && !(urlParamRuleMap.get(url).isUseDefender())) {
			return urlParamRuleMap.get(url);
		}
		return paramRule;
	}

	/**
	 * @param paramName String
	 * @param paramRule XssEscapeFilterRule
	 * @param urlParamRuleMap Map<String, XssEscapeFilterRule>
	 * @return XssEscapeFilterRule
	 */
	private XssEscapeFilterRule checkPrefixParameter(String paramName, XssEscapeFilterRule paramRule, Map<String, XssEscapeFilterRule> urlParamRuleMap) {
		if (paramRule != null || paramName == null) {
			return paramRule;
		}
		
		Set<Entry<String, XssEscapeFilterRule>> entries = urlParamRuleMap.entrySet();
		for (Entry<String, XssEscapeFilterRule> entry : entries) {
			if (entry.getValue().isUsePrefix() && paramName.startsWith(entry.getKey())) {
				return urlParamRuleMap.get(entry.getKey());
			} 
		}
		return paramRule;
	}

	/**
	 * @return Map<String, Defender>
	 */
	public Map<String, Defender> getDefenderMap() {
		return defenderMap;
	}

	/**
	 * @return Defender
	 */
	public Defender getDefaultDefender() {
		return defaultDefender;
	}
}