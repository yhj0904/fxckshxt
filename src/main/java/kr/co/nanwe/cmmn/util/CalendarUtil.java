package kr.co.nanwe.cmmn.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Class Name 		: Calendar
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class CalendarUtil {
	
	public static Map<String, Object> getYear(String date, String option) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		//입력날짜
		if(StringUtil.isNull(date)) {
			date = DateUtil.getDate("yyyyMMdd");
		}
		date = date.replaceAll("-", "").replaceAll("\\.", "");
		
		//오늘날짜
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String today = simpleDateFormat.format(calendar.getTime());
		returnMap.put("today", today);
		
		//입력날짜를 Date로 변환
		Date nDate;
		if(!StringUtil.isNull(date)) {
			nDate = DateUtil.stringToDate(date, "yyyyMMdd");
		} else {
			nDate = new Date();
		}
		
		//날짜 변환이 안된경우 return
		if(nDate == null) {
			return null;
		}
		
		//옵션값에 따라 날짜를 변경한다.
		if(!StringUtil.isNull(option)) {
			option = option.toUpperCase();
		} else {
			option = "";
		}
		
		calendar.setTime(nDate);
		if (option.equals("PREV")) {
			calendar.add(Calendar.YEAR, -1);
		} else if (option.equals("NEXT")) {
			calendar.add(Calendar.YEAR, 1);
		}
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		//변경된 날짜
		String sDate = simpleDateFormat.format(calendar.getTime());
		returnMap.put("sDate", sDate);

		//년도
		int year = calendar.get(Calendar.YEAR);
		String sYear = Integer.toString(year);
		returnMap.put("sYear", year);
		
		//달력 리스트
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i=0; i<12; i++) {
			
			Map<String, Object> row = new HashMap<String, Object>();
			
			//월
			int month = i+1;
			String sMonth = Integer.toString(month);
			if (month < 10) {
				sMonth = "0" + sMonth;
			}
			
			//해당 월의 마지막일
			int endDay = calendar.getActualMaximum(Calendar.DATE);
			String sEndday = Integer.toString(endDay);
			if (endDay < 10) {
				sEndday = "0" + sEndday;
			}

			String startDate = sYear+sMonth+"01";
			String endDate = sYear+sMonth+sEndday;
			
			row.put("year", year);
			row.put("month", month);
			row.put("beginDay", 1);
			row.put("endDay", endDay);
			row.put("startDate", startDate);
			row.put("endDate", endDate);
			
			//row 추가
			list.add(row);
			
		}
		returnMap.put("list", list);
		return returnMap;
	}
	
	public static Map<String, Object> getMonth(String date, String option) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		//입력날짜
		if(StringUtil.isNull(date)) {
			date = DateUtil.getDate("yyyyMMdd");
		}
		date = date.replaceAll("-", "").replaceAll("\\.", "");
		
		//오늘날짜
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String today = simpleDateFormat.format(calendar.getTime());
		returnMap.put("today", today);
		
		//입력날짜를 Date로 변환
		Date nDate;
		if(!StringUtil.isNull(date)) {
			nDate = DateUtil.stringToDate(date, "yyyyMMdd");
		} else {
			nDate = new Date();
		}
		
		//날짜 변환이 안된경우 return
		if(nDate == null) {
			return null;
		}
		
		//옵션값에 따라 날짜를 변경한다.
		if(!StringUtil.isNull(option)) {
			option = option.toUpperCase();
		} else {
			option = "";
		}
		
		calendar.setTime(nDate);
		if (option.equals("PREV")) {
			calendar.add(Calendar.MONTH, -1);
		} else if (option.equals("NEXT")) {
			calendar.add(Calendar.MONTH, 1);
		}
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		//변경된 날짜
		String sDate = simpleDateFormat.format(calendar.getTime());
		returnMap.put("sDate", sDate);

		//년도
		int year = calendar.get(Calendar.YEAR);
		String sYear = Integer.toString(year);
		returnMap.put("sYear", year);

		//월
		int month = calendar.get(Calendar.MONTH) + 1;
		String sMonth = Integer.toString(month);
		if (month < 10) {
			sMonth = "0" + sMonth;
		}
		returnMap.put("sMonth", month);
		
		//해당 월의 마지막일
		int endDay = calendar.getActualMaximum(Calendar.DATE);
		String sEndday = Integer.toString(endDay);
		if (endDay < 10) {
			sEndday = "0" + sEndday;
		}
		
		String startDate = sYear+sMonth+"01";
		String endDate = sYear+sMonth+sEndday;
		
		returnMap.put("beginDay", 1);
		returnMap.put("endDay", endDay);
		returnMap.put("startDate", startDate);
		returnMap.put("endDate", endDate);
		
		// 1일의 요일
		int firstWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		// 이전달 마지막 일
		Calendar prevMonth = Calendar.getInstance();
		prevMonth.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		int prevEndDay = prevMonth.getActualMaximum(Calendar.DATE);
		
		//달력계산
		int listSize = (firstWeek - 1) + endDay;
		if (listSize / 7 != 0) {
			listSize = listSize + (7 - (listSize % 7));
		}		
		int beginSpace = prevEndDay - (firstWeek - 2);
		int endSpace = 1;
		
		//요일
		int dayNum = 1;
		
		//달력 리스트
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < listSize; i++) {			
			Map<String, Object> row = new HashMap<String, Object>();
			
			int iYear = calendar.get(Calendar.YEAR);
			int iMonth = 0;
			int iDay = 0;
				
			//휴일 체크
			String background = "";
			
			if (i < (firstWeek - 1)) {
				iMonth = calendar.get(Calendar.MONTH);
				if (iMonth < 1) {
					iMonth = 12;
					iYear--;
				}
				iDay = beginSpace;
				beginSpace++;
				background = "not";
			} else if (i < ((firstWeek - 1) + endDay)) {				
				iMonth = calendar.get(Calendar.MONTH) + 1;
				iDay = (i + 1) - (firstWeek - 1);
			} else {				
				iMonth = calendar.get(Calendar.MONTH) + 2;
				if (iMonth > 12) {
					iMonth = 1;
					iYear++;
				}
				iDay = endSpace;
				endSpace++;
				background = "not";
			}
			
			String strYear = Integer.toString(iYear);
			String strMonth = Integer.toString(iMonth);
			if(iMonth < 10) {
				strMonth = "0" + strMonth;
			}			
			String strDay = Integer.toString(iDay);
			if(iDay < 10) {
				strDay = "0" + strDay;
			}
			
			String strDate = strYear+strMonth+strDay;
			
			if(!"not".equals(background)) {
				if(dayNum == 1) {
					background = "sun";
				} else if(HolidayUtil.isHoliday(strDate)){
					background = "hol";
				} else if(dayNum == 7) {
					background = "sat";				
				} 
			}
			
			row.put("year", iYear);
			row.put("month", iMonth);
			row.put("day", iDay);
			row.put("dayNum", dayNum);
			row.put("date", strDate);
			row.put("background", background);
			
			//row 추가
			list.add(row);
			
			dayNum++;
			if(dayNum > 7) {
				dayNum = 1;
			}
		}
		returnMap.put("list", list);
		return returnMap;
		
	}
	
	public static Map<String, Object> getWeek(String date, String option) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		//입력날짜
		if(StringUtil.isNull(date)) {
			date = DateUtil.getDate("yyyyMMdd");
		}
		date = date.replaceAll("-", "").replaceAll("\\.", "");
		
		//오늘날짜
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String today = simpleDateFormat.format(calendar.getTime());
		returnMap.put("today", today);
		
		//입력날짜를 Date로 변환
		Date nDate;
		if(!StringUtil.isNull(date)) {
			nDate = DateUtil.stringToDate(date, "yyyyMMdd");
		} else {
			nDate = new Date();
		}
		
		//날짜 변환이 안된경우 return
		if(nDate == null) {
			return null;
		}
		
		//옵션값에 따라 날짜를 변경한다.
		if(!StringUtil.isNull(option)) {
			option = option.toUpperCase();
		} else {
			option = "";
		}
		
		Calendar monCal = Calendar.getInstance();
		monCal.setTime(nDate);
		Calendar sunCal = Calendar.getInstance();
		sunCal.setTime(nDate);
		if (option.equals("PREV")) {
			monCal.add(Calendar.WEEK_OF_MONTH, -1);
			sunCal.add(Calendar.WEEK_OF_MONTH, -1);					
		} else if (option.equals("NEXT")) {
			monCal.add(Calendar.WEEK_OF_MONTH, 1);
			sunCal.add(Calendar.WEEK_OF_MONTH, 1);;
		}
		monCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		sunCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		sunCal.add(Calendar.DATE, 7);
		
		//변경된 날짜
		String sDate = simpleDateFormat.format(monCal.getTime());
		returnMap.put("sDate", sDate);

		//년도
		int year = monCal.get(Calendar.YEAR);
		String sYear = Integer.toString(year);
		returnMap.put("sYear", year);
		
		//월
		int month = monCal.get(Calendar.MONTH) + 1;
		String sMonth = Integer.toString(month);
		if (month < 10) {
			sMonth = "0" + sMonth;
		}
		returnMap.put("sMonth", month);
		
		//일
		int day = monCal.get(Calendar.YEAR);
		String sDay = Integer.toString(day);
		if (day < 10) {
			sDay = "0" + sDay;
		}
		
		//해당주차 날짜
		int endYear = sunCal.get(Calendar.YEAR);
		String sEndYear = Integer.toString(endYear);
		
		int endMonth = sunCal.get(Calendar.MONTH)+1;
		String sEndMonth = Integer.toString(endMonth);
		if (endMonth < 10) {
			sEndMonth = "0" + sEndMonth;
		}
		
		int endDay = sunCal.get(Calendar.DAY_OF_MONTH);
		String sEndDay = Integer.toString(endDay);
		if (endDay < 10) {
			sEndDay = "0" + sEndDay;
		}
		
		String startDate = sYear+sMonth+sDay;
		String endDate = sEndYear+sEndMonth+sEndDay;
		
		returnMap.put("startDate", startDate);
		returnMap.put("endDate", endDate);
		
		//주
		int week = monCal.get(Calendar.WEEK_OF_MONTH);
		returnMap.put("sWeek", week);
				
		//달력 리스트
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (monCal.compareTo(sunCal) != 1) {			
			
			Map<String, Object> row = new HashMap<String, Object>();
			
			int iYear = monCal.get(Calendar.YEAR);
			int iMonth = monCal.get(Calendar.MONTH)+1;
			int iDay = monCal.get(Calendar.DAY_OF_MONTH);
			int dayNum = monCal.get(Calendar.DAY_OF_WEEK);
			
			String strYear = Integer.toString(iYear);
			String strMonth = Integer.toString(iMonth);
			if(iMonth < 10) {
				strMonth = "0" + strMonth;
			}			
			String strDay = Integer.toString(iDay);
			if(iDay < 10) {
				strDay = "0" + strDay;
			}
			
			String strDate = strYear+strMonth+strDay;
			
			row.put("year", iYear);
			row.put("month", iMonth);
			row.put("day", iDay);
			row.put("dayNum", dayNum);
			row.put("dayKor", DateUtil.getDayOfWeekKor(dayNum));
			row.put("dayEng", DateUtil.getDayOfWeekEng(dayNum));
			row.put("date", strDate);
			
			//row 추가
			list.add(row);
			
			monCal.add(Calendar.DATE, 1);
		}
		returnMap.put("list", list);
		return returnMap;		
	}
	
	public static Map<String, Object> getDay(String date, String option) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		//입력날짜
		if(StringUtil.isNull(date)) {
			date = DateUtil.getDate("yyyyMMdd");
		}
		date = date.replaceAll("-", "").replaceAll("\\.", "");
		
		//오늘날짜
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String today = simpleDateFormat.format(calendar.getTime());
		returnMap.put("today", today);
		
		//입력날짜를 Date로 변환
		Date nDate;
		if(!StringUtil.isNull(date)) {
			nDate = DateUtil.stringToDate(date, "yyyyMMdd");
		} else {
			nDate = new Date();
		}
		
		//날짜 변환이 안된경우 return
		if(nDate == null) {
			return null;
		}
		
		//옵션값에 따라 날짜를 변경한다.
		if(!StringUtil.isNull(option)) {
			option = option.toUpperCase();
		} else {
			option = "";
		}
		
		calendar.setTime(nDate);
		if (option.equals("PREV")) {
			calendar.add(Calendar.DATE, -1);
		} else if (option.equals("NEXT")) {
			calendar.add(Calendar.DATE, 1);
		}
		
		//변경된 날짜
		String sDate = simpleDateFormat.format(calendar.getTime());
		returnMap.put("sDate", sDate);

		//년도
		int year = calendar.get(Calendar.YEAR);
		returnMap.put("sYear", year);
		
		//월
		int month = calendar.get(Calendar.MONTH) + 1;
		returnMap.put("sMonth", month);
		
		//일
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		returnMap.put("sDay", day);
		
		//주
		int week = calendar.get(Calendar.WEEK_OF_MONTH);
		returnMap.put("sWeek", week);
		
		//요일
		int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
		returnMap.put("dayNum", dayNum);
		returnMap.put("dayKor", DateUtil.getDayOfWeekKor(dayNum));
		returnMap.put("dayEng", DateUtil.getDayOfWeekEng(dayNum));
		
		return returnMap;	
	}
}
