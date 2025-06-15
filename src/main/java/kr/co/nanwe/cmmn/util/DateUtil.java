package kr.co.nanwe.cmmn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class Name 		: DateUtil
 * @Description 	: 날짜관련 유틸클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class DateUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	
	/**
	 * 현재날짜를 YYYY-MM-DD 형식으로 만들어 리턴
	 */
	
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String formatDate = sdf.format(cal.getTime());
		return formatDate;
	}

	/**
	 * 현재날짜를 지정된 포맷으로 만들어 리턴
	 */
	public static String getDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		String formatDate = sdf.format(cal.getTime());
		return formatDate;
	}
	
	/**
	 * 입력받은 Date를 YYYY-MM-DD 형식의 String 으로 만들어 리턴
	 */
	public static String dateToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = sdf.format(d);
		return formatDate;
	}

	/**
	 * 입력받은 Date를 지정된 포맷으로 만들어 리턴
	 */
	public static String dateToString(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String formatDate = sdf.format(d);
		return formatDate;
	}

	/**
	 * 입력받은 String을 Date형으로 만들어 리턴
	 */
	public static Date stringToDate(String d, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(d);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			date = null;
		}
		return date;
	}

	/**
	 * 입력받은 String을 다른 Format으로변경
	 */
	public static String convertFormat(String d, String prevFormat, String format) {
		Date date = stringToDate(d, prevFormat);
		return dateToString(date, format);
	}

	/**
	 * 현재날짜를 YYYY-MM-DD HH:MM:SS 형식으로 만들어 리턴
	 */
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String formatDate = sdf.format(cal.getTime());
		return formatDate;
	}
	
	/**
	 * 입력받은 String을  YYYY-MM-DD HH:MM:SS 형식으로 만들어 리턴
	 */
	public static String getDateTime(String d, String format) {
		Date date = stringToDate(d, format);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatDate = sdf.format(date.getTime());
		return formatDate;
	}

	/**
	 * 오늘 날짜의 년도 리턴
	 */
	public static int getDateYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 해당 날짜의 년도 리턴
	 */
	public static int getDateYear(String d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		int year = 0;
		
		try {
			
			Date date = sdf.parse(d);
			Calendar cal = Calendar.getInstance() ;
		    cal.setTime(date);
			year = cal.get(Calendar.YEAR);
			
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			year = 0;
		}
		
		return year;
	}
	
	/**
	 * 오늘 날짜의 월 리턴
	 */
	public static int getDateMonth() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		return month;
	}
	
	/**
	 * 해당 날짜의 월 리턴
	 */
	public static int getDateMonth(String d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		int month = 0;
		
		try {
			
			Date date = sdf.parse(d);
			Calendar cal = Calendar.getInstance() ;
		    cal.setTime(date);
		    month = cal.get(Calendar.MONTH) + 1;
			
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			month = 0;
		}
		
		return month;
	}
	
	/**
	 * 오늘 날짜의 일 리턴
	 */
	public static int getDateDay() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	/**
	 * 해당 날짜의 일 리턴
	 */
	public static int getDateDay(String d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		int day = 0;
		
		try {
			
			Date date = sdf.parse(d);
			Calendar cal = Calendar.getInstance() ;
		    cal.setTime(date);
		    day = cal.get(Calendar.DAY_OF_MONTH);
			
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			day = 0;
		}
		
		return day;
	}
	
	/**
	 * 오늘 날짜의 요일 리턴 (한글)
	 */
	public static String getKorDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return convertDayOfWeekToKor(dayOfWeek);
	}
	
	/**
	 * 해당 날짜의 요일 리턴 (한글)
	 */
	public static String getKorDayOfWeek(String d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		String DAY_OF_WEEK = null;
		
		try {
			
			Date date = sdf.parse(d);
			Calendar cal = Calendar.getInstance() ;
		    cal.setTime(date);
		    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		    DAY_OF_WEEK = convertDayOfWeekToKor(dayOfWeek);
			
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			DAY_OF_WEEK =  "";
		}
		
		return DAY_OF_WEEK;
	}
	
	/**
	 * 오늘 날짜의 요일 리턴 (영어)
	 */
	public static String getEngDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return convertDayOfWeekToEng(dayOfWeek);
	}
	
	/**
	 * 해당 날짜의 요일 리턴 (한글)
	 */
	public static String getEngDayOfWeek(String d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		String DAY_OF_WEEK = null;
		
		try {
			
			Date date = sdf.parse(d);
			Calendar cal = Calendar.getInstance() ;
		    cal.setTime(date);
		    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		    DAY_OF_WEEK = convertDayOfWeekToEng(dayOfWeek);
			
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			DAY_OF_WEEK =  "";
		}
		
		return DAY_OF_WEEK;
	}
	
	private static String convertDayOfWeekToKor(int dayOfWeek) {
		String kor = "";
		switch(dayOfWeek) {
			case 1:
				kor = "일";
			case 2:
				kor = "월";
			case 3:
				kor = "화";
			case 4:
				kor = "수";
			case 5:
				kor = "목";
			case 6:
				kor = "금";
			case 7:
				kor = "토";
		}
		return kor;
	};
	
	private static String convertDayOfWeekToEng(int dayOfWeek) {
		String eng = "";
		switch(dayOfWeek) {
			case 1:
				eng = "SUN";
			case 2:
				eng = "MON";
			case 3:
				eng = "TUE";
			case 4:
				eng = "WED";
			case 5:
				eng = "THU";
			case 6:
				eng = "FRI";
			case 7:
				eng = "SAT";
		}
		return eng;
	};
		
	/**
	 * 두 날짜의 일수 차이를 구함
	 */
	public static strictfp int getDiffDay(String startDate, String endDate) {
		int diffDay = 0;
		
		Date start = null;
		Date end = null;

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.KOREA);

		try {
			start = df.parse(startDate);
			end = df.parse(endDate);
			long diff = start.getTime() - end.getTime();
		    long diffDays = diff / (24 * 60 * 60 * 1000);
			diffDay = (int) diffDays;
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			diffDay = -1;
		}
		return diffDay;
	}
	
	/**
	 * 해당 날짜가 포함된 월의 마지막 일을 구함
	 */
	public static int getMonthLastDay(String d, String format) {
		Date date = stringToDate(d, format);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static String getMonthToEng(int month) {
		String monthEng = "";
		switch (month) {
		case 1:
			monthEng = "JANUARY";
			break;
		case 2:
			monthEng = "FEBURARY";
			break;
		case 3:
			monthEng = "MARCH";
			break;
		case 4:
			monthEng = "APRIL";
			break;
		case 5:
			monthEng = "MAY";
			break;
		case 6:
			monthEng = "JUNE";
			break;
		case 7:
			monthEng = "JULY";
			break;
		case 8:
			monthEng = "AUGUST";
			break;
		case 9:
			monthEng = "SEPTEMBER";
			break;
		case 10:
			monthEng = "OCTOBER";
			break;
		case 11:
			monthEng = "NOVEMBER";
			break;
		case 12:
			monthEng = "DECEMBER";
			break;
		}
		return monthEng;
	}
	
	public static String getDayOfWeekKor(int dayNum) {
		String dayOfWeek = "";
		switch(dayNum){
	        case 1:
	        	dayOfWeek = "일";
	            break ;
	        case 2:
	        	dayOfWeek = "월";
	            break ;
	        case 3:
	        	dayOfWeek = "화";
	            break ;
	        case 4:
	        	dayOfWeek = "수";
	            break ;
	        case 5:
	        	dayOfWeek = "목";
	            break ;
	        case 6:
	        	dayOfWeek = "금";
	            break ;
	        case 7:
	        	dayOfWeek = "토";
	            break ;
		}
		return dayOfWeek;
	}
	
	public static String getDayOfWeekEng(int dayNum) {
		String dayOfWeek = "";
		switch(dayNum){
	        case 1:
	        	dayOfWeek = "SUN";
	            break ;
	        case 2:
	        	dayOfWeek = "MON";
	            break ;
	        case 3:
	        	dayOfWeek = "TUE";
	            break ;
	        case 4:
	        	dayOfWeek = "WED";
	            break ;
	        case 5:
	        	dayOfWeek = "THU";
	            break ;
	        case 6:
	        	dayOfWeek = "FRI";
	            break ;
	        case 7:
	        	dayOfWeek = "SAT";
	            break ;
		}
		return dayOfWeek;
	}
}
