package kr.co.nanwe.file.service;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class Name 		: ExcelDownloadHandler
 * @Description 	: 대용량 엑셀다운로드 클래스 (OOM 방지)
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@SuppressWarnings("rawtypes")
public class ExcelDownloadHandler<T> implements ResultHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDownloadHandler.class);

	private SXSSFWorkbook wb;
	private SXSSFSheet sheet;
	private SXSSFRow row;

	private int rowNum = 0;
	private boolean sequence = false;

	private String excelName;
	private String[] columnTitles;
	private String[] columnCodes;

	public ExcelDownloadHandler(SXSSFWorkbook wb, String excelName, String[] columnTitles, String[] columnCodes, boolean sequence) {
		this.wb = wb;
		this.rowNum = 0;
		this.excelName = excelName;
		this.columnTitles = columnTitles;
		this.columnCodes = columnCodes;
		this.sequence = sequence;

		try {
			createFirstSheet(this.wb);
			createTitle();
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext resultContext) {
		HashMap<String, Object> data = (HashMap<String, Object>) resultContext.getResultObject();
		createBody(data);
	}

	/**
	 * Shhet 생성
	 */
	private SXSSFSheet createFirstSheet(Workbook workbook) {
		sheet = (SXSSFSheet) workbook.createSheet(excelName);
		return sheet;
	}

	/**
	 * Title 생성
	 */
	private void createTitle() {

		if (rowNum > 0) {
			return;
		}

		SXSSFCell cell = null;
		
		int cellIdx = 0;
		
		if (columnTitles != null && columnTitles.length > 0) {
			row = sheet.createRow(rowNum);
			if(sequence) {
				cell = row.createCell(cellIdx);
				cell.setCellValue("No");
				cellIdx++;
			}
			for (int i = 0; i < columnTitles.length; i++) {
				cell = row.createCell(cellIdx);
				cell.setCellValue(columnTitles[i]);
				cellIdx++;
			}
		}
		rowNum++;
	}

	private void createBody(HashMap<String, Object> data) {
		
		SXSSFCell cell = null;
		
		int cellIdx = 0;
		
		row = sheet.createRow(rowNum);
		
		if(sequence) {
			cell = row.createCell(cellIdx);
			cell.setCellValue(rowNum);
			cellIdx++;
		}
		
		if (columnCodes != null && columnCodes.length > 0) {
	        for (int i = 0; i < columnCodes.length; i++) {
	            cell = row.createCell(cellIdx);
	            Object value = data.get(columnCodes[i]);
	            // HTML 엔티티 변환 추가
	            String cellValue = value == null ? "" : StringEscapeUtils.unescapeHtml4(String.valueOf(value));
	            cell.setCellValue(cellValue);
	            cellIdx++;                
	        }
	    } else {
	        for (String key : data.keySet() ) {
	            cell = row.createCell(cellIdx);
	            // HTML 엔티티 변환 추가
	            String cellValue = StringEscapeUtils.unescapeHtml4(String.valueOf(data.get(key)));
	            cell.setCellValue(cellValue);
	            cellIdx++;
	        }
	    }
		rowNum++;
	}
}