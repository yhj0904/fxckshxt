package kr.co.nanwe.file.service;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @Class Name 		: ExcelFormDownload
 * @Description 	: 엑셀 업로드 양식 다운로드 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component
public class ExcelFormDownload extends AbstractView {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFormDownload.class);
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;
	
	public ExcelFormDownload() {}
	
	@Override
    protected boolean generatesDownloadContent() {
        return true;
    }

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Map<String, Object> excelInfo = (Map<String, Object>)model.get("excelInfo");
		
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("<script>");
		sbuffer.append("alert('잘못된 엑셀양식 정보입니다.');");
		sbuffer.append("history.back();");
		sbuffer.append("</script>");
		String errMsg = sbuffer.toString();
				
		if(excelInfo == null) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(errMsg);
			printWriter.flush();
			return;
		}
        
        ServletOutputStream out = null;
        XSSFWorkbook workbook = null;

		//엑셀 파일명
		String fileName =  (String) excelInfo.get("fileName");
		if(fileName == null || "".equals(fileName)) {
			fileName = "excel_form";
		}
		
		//엑셀 컬럼 정보 (리스트 조회순)
		String[] columnTitles = (String[]) excelInfo.get("columnTitles");
		
        try {
        
			//엑셀 정보 생성	
        	workbook = new XSSFWorkbook();
	        XSSFSheet worksheet = workbook.createSheet(fileName);
			
			//행수
			int rowCount = 0;
	
			// HEADER ROW
			if(columnTitles != null) {
				if(columnTitles.length > 0) {
					XSSFRow headerRow = worksheet.createRow(rowCount);
					Cell headerCell;
					for (int i = 0; i < columnTitles.length; i++) {
						headerCell = headerRow.createCell(i);
						headerCell.setCellValue(columnTitles[i]);
					}
					rowCount++;
				}			
			}
			
			response.setContentType(getContentType());
	        
	        String header = request.getHeader("User-Agent");
	        fileName = fileName.replaceAll("\r","").replaceAll("\n","");
	        if(header.contains("MSIE") || header.contains("Trident") || header.contains("Chrome")){
	        	fileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+","%20");
	            response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx;");
	        }else{
	        	fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
	            response.setHeader("Content-Disposition","attachment;filename=\""+fileName + ".xlsx\"");
	        }
      
        	out = response.getOutputStream();
            out.flush();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
        	LOGGER.debug(e.getMessage());
        } finally {
			if(out != null) {
				out.close();
			}
			if(workbook != null) {
				workbook.close();
			}
		}
	}
}
