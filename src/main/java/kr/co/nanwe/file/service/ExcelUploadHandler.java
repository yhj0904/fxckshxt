package kr.co.nanwe.file.service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellReference;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.XMLReader;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @Class Name 		: ExcelUploadHandler
 * @Description 	: 엑셀 업로드 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ExcelUploadHandler implements SheetContentsHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUploadHandler.class);

	private int currentCol = -1;
	//private int currRowNum = 0;

	String filePath = "";

	private List<List<String>> rows = new ArrayList<List<String>>();
	private List<String> row = new ArrayList<String>();
	private List<String> header = new ArrayList<String>();

	public static ExcelUploadHandler readExcel(File file) throws Exception {

		ExcelUploadHandler sheetHandler = new ExcelUploadHandler();
		
		OPCPackage opc = null;
		InputStream inputStream = null;

		try {
			
			opc = OPCPackage.open(file);
			XSSFReader xssfReader = new XSSFReader(opc);
			
			StylesTable styles = xssfReader.getStylesTable();

			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);

			inputStream = xssfReader.getSheetsData().next();

			InputSource inputSource = new InputSource(inputStream);
			
			ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, sheetHandler, false);

			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			saxFactory.setNamespaceAware(true);
			SAXParser saxParser = saxFactory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(handle);
			xmlReader.parse(inputSource);

			xmlReader.parse(inputSource);

		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
			if(opc != null) {
				opc.close();
			}
		}

		return sheetHandler;

	}

	public List<List<String>> getRows() {
		return rows;
	}

	@Override
	public void startRow(int arg0) {
		this.currentCol = -1;
		//this.currRowNum = arg0;
	}

	@Override
	public void cell(String columnName, String value, XSSFComment var3) {
		int iCol = (new CellReference(columnName)).getCol();
		int emptyCol = iCol - currentCol - 1;

		for (int i = 0; i < emptyCol; i++) {
			row.add("");
		}
		currentCol = iCol;
		row.add(value);
	}

	@Override
	public void headerFooter(String arg0, boolean arg1, String arg2) {
		//
	}

	@Override
	public void endRow(int rowNum) {
		if (rowNum == 0) {
			header = new ArrayList<String>(row);
		} else {
			if (row.size() < header.size()) {
				for (int i = row.size(); i < header.size(); i++) {
					row.add("");
				}
			}
			rows.add(new ArrayList<String>(row));
		}
		row.clear();
	}
}