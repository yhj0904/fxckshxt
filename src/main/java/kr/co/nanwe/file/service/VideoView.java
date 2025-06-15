package kr.co.nanwe.file.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.util.WebUtil;

/**
 * @Class Name 		: VideoView
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("videoView")
public class VideoView {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(VideoView.class);
	
	/** Buffer size */
	private static final int BUFFER_SIZE = 8192;

	public static final String SEPERATOR = File.separator;
	
	/** server.properties */
	@Resource(name = "serverProp")
	private Properties serverProp;

	public void viewFile(HttpServletResponse response, String filePath, String fileName, String fileMime, String fileExt) throws Exception {
		
		if (fileExt == null || !"mp4".equals(fileExt)) {
			return;
		}

		String mimeType = fileMime;
		String downFileName = filePath + SEPERATOR + fileName;

		File file = new File(WebUtil.filePathBlackList(downFileName));

		if (!file.exists()) {
			return;
		}

		if (!file.isFile()) {
			return;
		}

		byte[] b = new byte[BUFFER_SIZE];

		if (mimeType == null) {
			mimeType = "video/mp4";
		}

		response.setContentType(WebUtil.removeCRLF(mimeType));
		response.setHeader("Content-Disposition", "filename=mp4;");

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
}
