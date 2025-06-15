package kr.co.nanwe.file.web;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.util.CryptoUtil;
import kr.co.nanwe.cmmn.util.MessageUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.cmmn.util.WebUtil;
import kr.co.nanwe.file.service.CkEditorUpload;
import kr.co.nanwe.file.service.FileUpload;
import kr.co.nanwe.file.service.FileVO;
import kr.co.nanwe.file.service.ImageView;
import kr.co.nanwe.file.service.VideoView;

/**
 * @Class Name 		: FileController
 * @Description 	: 파일 업로드 / 다운로드 컨트롤러
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Controller
public class FileController {
	
	/** Config */
	@Resource(name = "web")
	private WebConfig web;
	
	/** Message Source */
	@Resource(name = "messageUtil")
	private MessageUtil messageUtil;
	
	/** AES 암복호화 */
	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;
	
	@Resource(name = "imageView")
	private ImageView imageView;
	
	@Resource(name = "videoView")
	private VideoView videoView;
	
	@Resource(name = "fileUpload")
	private FileUpload fileUpload;
	
	@Resource(name = "ckEditorUpload")
	private CkEditorUpload ckEditorUpload;
	
	/**
	 * 첨부파일을 다운로드한다
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/file/download.do")
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response
								, @RequestParam(value = "fno", defaultValue="0") int fno
								, @RequestParam(value = "oname") String oname
								, @RequestParam(value = "fpath") String fpath
								, @RequestParam(value = "fname") String fname) throws Exception {

		if (StringUtil.isNull(oname) || StringUtil.isNull(fpath) || StringUtil.isNull(fname)) {
			return new ModelAndView("fileDownload", "fileMap", null);
		}
		
		String filePath = cryptoUtil.decrypt(StringUtil.isNullToString(fpath));
		String fileName = cryptoUtil.decrypt(StringUtil.isNullToString(fname));

		if (filePath.indexOf("..") >= 0) 
			return new ModelAndView("fileDownload", "fileMap", null);
		if (fileName.indexOf("..") >= 0)
			return new ModelAndView("fileDownload", "fileMap", null);

		String ext = "";
		if (fileName.lastIndexOf(".") > 0) {
			ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		}
		if (ext == null) {
			return new ModelAndView("fileDownload", "fileMap", null);
		}
		
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("fileNo", fno);
		fileMap.put("filePath", filePath);
		fileMap.put("fileName", fileName);
		fileMap.put("orgName", oname);
		return new ModelAndView("fileDownload", "fileMap", fileMap);
	}
	
	/**
	 * 이미지 파일 미리보기
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/file/imageView.do", method = RequestMethod.GET)
	public void imageView(HttpServletRequest request, HttpServletResponse response
						, @RequestParam(value = "fpath", required = false) String fpath
						, @RequestParam(value = "fname", required = false) String fname
						, @RequestParam(value = "fmime", required = false) String fmime) throws Exception {

		if (StringUtil.isNull(fpath) || StringUtil.isNull(fname) || StringUtil.isNull(fmime)) {
			imageView.viewNoImg(response);
			return;
		}

		String filePath = cryptoUtil.decrypt(StringUtil.isNullToString(fpath));
		String fileName = cryptoUtil.decrypt(StringUtil.isNullToString(fname));
		String fileMime = cryptoUtil.decrypt(StringUtil.isNullToString(fmime));

		if (filePath.indexOf("..") >= 0) {
			imageView.viewNoImg(response);
			return;
		}

		String fileExt = "";
		if (fileName.lastIndexOf(".") > 0) {
			fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		}
		if (fileExt == null) {
			imageView.viewNoImg(response);
			return;
		}
		
		imageView.viewFile(response, filePath, fileName, fileMime ,fileExt);
	}
	
	/**
	 * 동영상 파일
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/file/videoView.do", method = RequestMethod.GET)
	public void videoView(HttpServletRequest request, HttpServletResponse response
						, @RequestParam(value = "fpath", required = false) String fpath
						, @RequestParam(value = "fname", required = false) String fname
						, @RequestParam(value = "fmime", required = false) String fmime) throws Exception {

		if (StringUtil.isNull(fpath) || StringUtil.isNull(fname) || StringUtil.isNull(fmime)) {
			return;
		}

		String filePath = cryptoUtil.decrypt(StringUtil.isNullToString(fpath));
		String fileName = cryptoUtil.decrypt(StringUtil.isNullToString(fname));
		String fileMime = cryptoUtil.decrypt(StringUtil.isNullToString(fmime));

		if (filePath.indexOf("..") >= 0) {
			return;
		}

		String fileExt = "";
		if (fileName.lastIndexOf(".") > 0) {
			fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		}
		if (fileExt == null) {
			return;
		}
		
		videoView.viewFile(response, filePath, fileName, fileMime ,fileExt);
	}
	
	/**
	 * 엑셀 업로드를 위한 양식을 다운받는다.
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value="/file/excelFormDown.do")
    public ModelAndView excelFormDown(HttpServletRequest request, HttpServletResponse response
    								,@RequestParam(value="fileName", defaultValue="excel_form") String fileName
    								,@RequestParam(value="columnTitle", required=false) String[] columnTitles) throws Exception {
		
		//엑셀 정보
		Map<String, Object> excelInfo = new HashMap<String, Object>();

        excelInfo.put("fileName", fileName);
        excelInfo.put("columnTitles", columnTitles);
		
        return new ModelAndView("excelFormDownload", "excelInfo", excelInfo);
    }
	
	/**
	 * 웹에디터 (CKEDITOR) 파일 업로드
	 * @param 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "/file/ckImageUpload.do")
	public void ckImageUpload( HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {
		
		String resultMessage = "";
		String callback = WebUtil.clearXSSMinimum(request.getParameter("CKEditorFuncNum"));

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		StringBuffer sbuffer = new StringBuffer();
		PrintWriter printWriter = response.getWriter();

		FileVO file = ckEditorUpload.uploadCkImage(upload);
		if(!file.isResult()) {			
			resultMessage = messageUtil.getMessage("message.error.file");
			sbuffer.append("<script>window.parent.CKEDITOR.tools.callFunction(");
			sbuffer.append(callback);
			sbuffer.append(", '',  '" + resultMessage + "');");
			sbuffer.append("</script>");			
			
		} else {
			resultMessage = messageUtil.getMessage("message.success");
			
			//DB 파일 업로드 처리
			int fileNo = ckEditorUpload.insertComEditorFiles(file);			
			
			String filePath = file.getFilePath();
			String fileName = file.getFileName();
			String fileMime = file.getFileMime();
			
			String fpath = URLEncoder.encode(cryptoUtil.encrypt(filePath), "UTF-8");
			String fname = URLEncoder.encode(cryptoUtil.encrypt(fileName), "UTF-8");
			String fmime = URLEncoder.encode(cryptoUtil.encrypt(fileMime), "UTF-8");
			
			
			String fileUrl = request.getContextPath()
							    + "/file/imageView.do?"
							    + "fpath=" + fpath
							    + "&fname=" + fname
							    + "&fmime=" + fmime;

			sbuffer.append("<script>window.parent.CKEDITOR.tools.callFunction(");
			sbuffer.append(callback);
			sbuffer.append(", '");
			sbuffer.append(fileUrl);
			sbuffer.append("' , '" + resultMessage);
			sbuffer.append("');");
			sbuffer.append("window.parent.gf_callbackCkUpload("+fileNo+");");
			sbuffer.append("</script>");
		}
		
		printWriter.println(sbuffer.toString());
		printWriter.flush();
		
	}
}