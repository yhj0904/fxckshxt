package kr.co.nanwe.cmmn.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @Class Name 		: HtmlConvertorUtil
 * @Description 	: HTML 태그 변환 유틸클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class HtmlConvertorUtil {
	
	public static String changeHtmlTag(String txt) {
		if (StringUtil.isNull(txt)) {
			return txt;
		}
		String returnTxt = "";
		returnTxt = txt.replaceAll("&lt", "<").replaceAll("&gt", ">");
		return returnTxt;
	}
	
	public static String changeLineSeparatorToBr(String txt) {
		if (StringUtil.isNull(txt)) {
			return txt;
		}
		String returnTxt = "";
		returnTxt = txt.replaceAll(System.getProperty("line.separator"), "<br/>");
		return returnTxt;
	}
	
	public static String changeBrToLineSeparator(String txt) {
		if (StringUtil.isNull(txt)) {
			return txt;
		}
		String returnTxt = txt;
		returnTxt = returnTxt.replaceAll("<br/>", System.getProperty("line.separator"));
		returnTxt = returnTxt.replaceAll("<br />", System.getProperty("line.separator"));
		returnTxt = returnTxt.replaceAll("<br>", System.getProperty("line.separator"));
		return returnTxt;
	}
	
	public static String removeLineSeparator(String txt) {
		if (StringUtil.isNull(txt)) {
			return txt;
		}
		String returnTxt = "";
		returnTxt = txt.replaceAll(System.getProperty("line.separator"), "");
		return returnTxt;
	}
	
	//HTML 태그 제거
	public static String removeTag(String html) {
		return removeTag(html, HtmlTagPatterns.values());
	}
	
	public static String removeTagWithoutWhiteSpace(String html) {
		return removeTagWithoutWhiteSpace(html, HtmlTagPatterns2.values());
	}

	private static String removeTag(String html, HtmlTagPatterns... patterns) {
		if (StringUtil.isNull(html) || ArrayUtils.isEmpty(patterns)) {
			return html;
		}

		for (HtmlTagPatterns tagPattern : patterns) {
			html = tagPattern.getPattern().matcher(html).replaceAll("");
		}

		return html;
	}
	
	private static String removeTagWithoutWhiteSpace(String html, HtmlTagPatterns2... patterns) {
		if (StringUtil.isNull(html) || ArrayUtils.isEmpty(patterns)) {
			return html;
		}

		for (HtmlTagPatterns2 tagPattern : patterns) {
			html = tagPattern.getPattern().matcher(html).replaceAll("");
		}

		return html;
	}
	
	private enum HtmlTagPatterns {
		
		SCRIPTS("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL), 
		STYLE("<style[^>]*>.*</style>", Pattern.DOTALL),
		TAGS("<([a-zA-Z!-:]+[^>]+|[a-zA-Z!-:]+)>"),
		N_TAGS("<\\w+\\s+[^<]*\\s*>"),
		ENTITY_REFS("&[^;]+;"),
		WHITESPACE("\\s\\s+");

		private Pattern pattern;

		private HtmlTagPatterns(String regex) {
			this.pattern = Pattern.compile(regex);
		}

		private HtmlTagPatterns(String regex, int flags) {
			this.pattern = Pattern.compile(regex, flags);
		}

		public Pattern getPattern() {
			return pattern;
		}
	}
	
	private enum HtmlTagPatterns2 {
		
		SCRIPTS("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL), 
		STYLE("<style[^>]*>.*</style>", Pattern.DOTALL),
		TAGS("<([a-zA-Z!-:]+[^>]+|[a-zA-Z!-:]+)>"),
		N_TAGS("<\\w+\\s+[^<]*\\s*>"),
		ENTITY_REFS("&[^;]+;");

		private Pattern pattern;

		private HtmlTagPatterns2(String regex) {
			this.pattern = Pattern.compile(regex);
		}

		private HtmlTagPatterns2(String regex, int flags) {
			this.pattern = Pattern.compile(regex, flags);
		}

		public Pattern getPattern() {
			return pattern;
		}
	}
}
