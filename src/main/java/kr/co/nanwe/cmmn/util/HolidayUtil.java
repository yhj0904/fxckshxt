package kr.co.nanwe.cmmn.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ChineseCalendar;

/**
 * @Class Name 		: HolidayUtil
 * @Description 	: 공휴일관련 유틸클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class HolidayUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayUtil.class);
	
	private static final List<String> SOLAR_LIST = new ArrayList<String>(Arrays.asList("0101", "0301", "0505", "0606", "0815", "1003", "1009", "1225"));
	
	private static final List<String> LUNAR_LIST = new ArrayList<String>(Arrays.asList("0101", "0102", "0408", "0814", "0815", "0816", "1231"));
	
	public static boolean isHoliday(String date) {
		boolean isHoliday = false;
		try {
			long d = DateUtils.parseDate(date, "yyyyMMdd").getTime();
			isHoliday = isLegalHoliday(d) || isAlternative(d);
		} catch (ParseException e) {
			isHoliday = false;
			LOGGER.debug(e.getMessage());
		}
		return isHoliday;		
	}

	/**
	 * 음력날짜 구하기
	 * 
	 * @param date
	 * @return
	 */
	public static String getLunarDate(long date) {
		ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
		String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
		m = StringUtils.leftPad(m, 2, "0");
		String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
		d = StringUtils.leftPad(d, 2, "0");
		return m + d;
	}

	/**
	 * 법정휴일
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLegalHoliday(long date) {
		boolean result = false;

		String solarDate = DateFormatUtils.format(date, "MMdd");
		ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));

		String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
		m = StringUtils.leftPad(m, 2, "0");
		String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
		d = StringUtils.leftPad(d, 2, "0");

		String lunarDate = m + d;

		if (SOLAR_LIST.indexOf(solarDate) >= 0) {
			return true;
		}
		if (LUNAR_LIST.indexOf(lunarDate) >= 0) {
			return true;
		}

		return result;
	}

	/**
	 * 주말 (토,일)
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(long date) {
		boolean result = false;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);

		// SUNDAY:1 SATURDAY:7
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			result = true;
		}

		return result;
	}

	/**
	 * 대체공휴일
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isAlternative(long date) {
		boolean result = false;
		return result;
	}
}
