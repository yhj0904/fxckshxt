package kr.co.nanwe.file.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.nanwe.cmmn.util.WebUtil;

/**
 * @Class Name 		: ThumbUtil
 * @Description 	: 썸네일 유틸
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("thumbUtil")
public class ThumbUtil  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ThumbUtil.class);
	
	private static final String SEPERATOR = File.separator;
	
	//썸네일 이미지 생성
	public static void createThumbnail(FileVO fileVO, int width, int height) {
		
		// 썸네일 너비 & 높이
		int tWidth = 300;
		int tHeight = 250;
		
		if(width > 0) {
			tWidth = width;
		}
		
		if(height > 0) {
			tHeight = height; 
		}

		//원본 경로
		String path = fileVO.getFilePath() + SEPERATOR + fileVO.getFileName();
		
		//썸네일 경로
		String thumbPath = fileVO.getFilePath() + SEPERATOR + "thumb_" +fileVO.getFileName();
		
		//원본 확장지
		String ext = fileVO.getFileExt();
		
		File imageFile = new File(WebUtil.filePathBlackList(path));

		try {
			BufferedImage originalImg = ImageIO.read(imageFile);
			BufferedImage thumbnailImg = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D graphic = thumbnailImg.createGraphics();
			graphic.drawImage(originalImg, 0, 0, tWidth, tHeight, null);
			File thumbFile = new File(WebUtil.filePathBlackList(thumbPath));
			ImageIO.write(thumbnailImg, ext, thumbFile);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
