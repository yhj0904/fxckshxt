package kr.co.nanwe.cmmn.util;

import java.text.DecimalFormat;

/**
 * @Class Name 		: StringUtil
 * @Description 	: 문자열 관련 유틸클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class StringUtil {
	
	public static final String EMPTY = "";

	/**
	 * 공백 또는 null 인지 체크
	 */
	public static boolean isNull(String str) {
		if (str != null) {
			if (!str.equals("") && !str.trim().equals("")) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	public static String isNullToString(Object object) {
		String string = "";

		if (object != null) {
			string = object.toString().trim();
		}

		return string;
	}
	
	/**
	 * 문자열 비교
	 */
	public static boolean isEqual(String str, String compareStr) {
		if(str == null) {
			str = "";
		}
		if(compareStr == null) {
			compareStr = "";
		}
		if(str.trim().equals(compareStr.trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 가격등의 값을 3자리 마다 comma(,)로 분리하여 리턴
	 */
	public static String numToDecimal(int num) {
		DecimalFormat df = new DecimalFormat("###,###");
		String format = df.format(num);
		return format;
	}
	
	/**
	 * 입력받은 String 사이즈 체크
	 */
	public static int getStringLength(String str) {
		
		if(isNull(str)) {
			return 0;
		} else {
			return str.length();
		}
	}
	
	/**
	 * 문자열 길이를 해당 사이즈보다 길 경우 자름
	 */
	public static String getStringCut(String str, int size){
		
		if(size == 0) {
			size = 1000;
		}
		
		if(isNull(str)) {
			return "";
		} else {
			if (str.length() > size) {
				return str.substring(0, size - 3) + "...";
			} else {
				return str;
			}
		}
	}
	
	/**
	 * String을 format에 맞추어 출력 getFormatedText("01012345678","###-####-####") - 010-1234-5678 로 출력
	 */
	public static String getFormatedText(String str, String format) {
		
		String rtn;
		
		int start, i, j, len;
		
		int tCount, fCount;

		tCount = str.length();
		fCount = format.length();

		rtn = "";

		if (isNull(str)) {
			return rtn;
		}

		for (i = 0; i < tCount; ++i) {
			if (!str.substring(i, i + 1).equals("-"))
				rtn = rtn + str.substring(i, i + 1);
		}

		str = rtn;
		tCount = str.length();

		// 포멧에서 #의 count
		len = 0;
		for (j = 0; j < fCount; ++j) {
			if (format.substring(j, j + 1).equals("#"))
				++len;
		}
		
		// str의 길이가 len보다 작으면 앞에 0를 붙인다.
		if (tCount < len) {
			for (i = 0; i < (len - tCount); ++i) {
				str = '0' + str;
			}
			tCount = len;
		}

		rtn = "";
		start = 0;
		for (i = 0; i < tCount; ++i) {
			for (j = start; j < fCount; ++j) {
				if (format.substring(j, j + 1).equals("#")) {
					rtn = rtn + str.substring(i, i + 1);
					start = start + 1;
					break;
				} else {
					rtn = rtn + format.substring(j, j + 1);
					start = start + 1;
				}
			}
		}
		return rtn + format.substring(start);
	}
	
	public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
	
	public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
	public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }
	
	public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
	
	public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }
	
	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String substringBeforeLast(String str, String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}
	
	public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }
	
	public static String underScoreToCamel(String str) {
		if(StringUtil.isNull(str)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char currentChar = str.charAt(i);
			if (currentChar == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}
			}
		}
		return result.toString();
	}
	
	public static String camelToUpperUnderscore(String str) {
		if(StringUtil.isNull(str)) {
			return "";
		}
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		return str.replaceAll(regex, replacement).toUpperCase();
	}
	
	public static String camelToLowerUnderscore(String str) {
		if(StringUtil.isNull(str)) {
			return "";
		}
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		return str.replaceAll(regex, replacement).toLowerCase();
	}
	
	public static String getExtension(String str) {
		if(StringUtil.isNull(str)) {
			return "";
		}
		String ext = "";
		int pos = str.lastIndexOf(".");
		if(pos != -1) {
			ext = str.substring(pos + 1);
		}
		return ext;
	}
	
	public static String getStrReplaceAll(String str, String regex, String replacement) {
		if(StringUtil.isNull(str)) {
			return "";
		}
		return str.replaceAll(regex, replacement);
	}
}
