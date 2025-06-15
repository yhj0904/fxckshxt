package kr.co.nanwe.file.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.WebUtil;

/**
 * @Class Name 		: ImageView
 * @Description 	: 이미지 미리보기 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("imageView")
public class ImageView {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(FileDownload.class);
	
	/** Buffer size */
	private static final int BUFFER_SIZE = 8192;

	public static final String SEPERATOR = File.separator;
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;

	public void viewFile(HttpServletResponse response, String filePath, String fileName, String fileMime, String fileExt) throws Exception {
		
		if (!FileUtil.IMAGE_EXT.contains(fileExt)) {
			viewNoImg(response);
			return;
		}

		String mimeType = fileMime;
		String downFileName = filePath + SEPERATOR + fileName;

		File file = new File(WebUtil.filePathBlackList(downFileName));

		if (!file.exists()) {
			viewNoImg(response);
			return;
		}

		if (!file.isFile()) {
			viewNoImg(response);
			return;
		}

		byte[] b = new byte[BUFFER_SIZE];

		if (mimeType == null) {
			mimeType = "application/octet-stream;";
		}

		response.setContentType(WebUtil.removeCRLF(mimeType));
		response.setHeader("Content-Disposition", "filename=image;");

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(response.getOutputStream());

			int read = 0;

			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		} finally {
			if(outs != null) {
				outs.close();
			}
			if(fin != null) {
				fin.close();
			}			
		}
	}
	
	public void viewNoImg(HttpServletResponse response) throws Exception {
		String rootPath = serverProp.getProperty("server.path");
		String mimeType = "image/png";
		String downFileName = rootPath + SEPERATOR + "images/common/no_img.png";

		File file = new File(WebUtil.filePathBlackList(downFileName));

		byte[] b = new byte[BUFFER_SIZE];

		response.setContentType(WebUtil.removeCRLF(mimeType));
		response.setHeader("Content-Disposition", "filename=image;");

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(response.getOutputStream());

			int read = 0;

			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		} finally {
			fin.close();
			outs.close();
		}
	}
}
