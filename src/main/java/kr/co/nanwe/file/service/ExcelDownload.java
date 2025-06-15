package kr.co.nanwe.file.service;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.nanwe.cmmn.util.StringUtil;

/**
 * @Class Name 		: ExcelDownload
 * @Description 	: 엑셀다운로드 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ExcelDownload {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDownload.class);
	
	public static void excelDownload(HttpServletRequest request, HttpServletResponse response, String excelName, SXSSFWorkbook wb) throws IOException {
		
		if(wb == null) {
			return;
		}
		
		if(StringUtil.isNull(excelName)) {
			excelName = "excel";
		}
		
		SXSSFWorkbook workbook = wb;
		ServletOutputStream out = null;
		
		try {

			// Response 헤더 설정
			String header = request.getHeader("User-Agent");
			excelName = excelName.replaceAll("\r", "").replaceAll("\n", "");
			if (header.contains("MSIE") || header.contains("Trident") || header.contains("Chrome")) {
				excelName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
				response.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xlsx;");
			} else {
				excelName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + excelName + ".xlsx\"");
			}

			// 엑셀 파일 출력
			out = response.getOutputStream();
			out.flush();
			workbook.write(out);
			out.flush();

		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
			if (workbook != null) {
				try {
					workbook.dispose();
				} catch (Exception e) {
					workbook.close();
				} finally {
					workbook.close();
				}
			}
		}
	}
	
}
