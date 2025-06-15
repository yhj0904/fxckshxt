package kr.co.nanwe.cmmn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.nanwe.file.service.FileUpload;

public class CompressUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUpload.class);

	private static final String SEPERATOR = File.separator;

	public static boolean compress(String path, String outputPath, String outputFileName) {

		boolean isChk = false;
		
		path = WebUtil.filePathBlackList(path);
		outputPath = WebUtil.filePathBlackList(outputPath);

		File file = new File(path);
		
		if(!file.isFile() && !file.isDirectory()) {
			return false;
		}

		int pos = outputFileName.lastIndexOf(".") == -1 ? outputFileName.length() : outputFileName.lastIndexOf(".");
		
		if (!outputFileName.substring(pos).equalsIgnoreCase(".zip")) {
			outputFileName += ".zip";
		}
		
		String fPath = WebUtil.filePathBlackList(outputPath + SEPERATOR + outputFileName);
		
		FileOutputStream fos = null;

		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(new File(fPath));
			zos = new ZipOutputStream(fos);
			searchDirectory(file, file.getPath(), zos);
			isChk = true;
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}

			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return isChk;
	}

	private static void searchDirectory(File file, String root, ZipOutputStream zos) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				searchDirectory(f, root, zos);
			}
		} else {
			compressZip(file, root, zos);
		}
	}

	private static void compressZip(File file, String root, ZipOutputStream zos) {
		FileInputStream fis = null;
		String zipName = file.getPath().replace(root + SEPERATOR, "");
		try {
			fis = new FileInputStream(file);
			ZipEntry zipentry = new ZipEntry(zipName);

			zos.putNextEntry(zipentry);
			int length = (int) file.length();
			byte[] buffer = new byte[length];

			fis.read(buffer, 0, length);
			zos.write(buffer, 0, length);
			zos.closeEntry();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
	}
}
