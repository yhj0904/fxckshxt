package kr.co.nanwe.file.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.nanwe.cmmn.util.ClientUtil;
import kr.co.nanwe.cmmn.util.CryptoUtil;
import kr.co.nanwe.cmmn.util.FileUtil;
import kr.co.nanwe.cmmn.util.SessionUtil;
import kr.co.nanwe.cmmn.util.StringUtil;
import kr.co.nanwe.file.service.impl.ComEditorFilesMapper;
import kr.co.nanwe.file.service.impl.ComFilesMapper;
import kr.co.nanwe.login.service.LoginVO;

/**
 * @Class Name 		: comFileManager
 * @Description 	: 공통파일 테이블 관리 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("comFileManager")
public class ComFileManager  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComFileManager.class);
	
	private static final String BBS_CODE = "COM_BBS";
	
	private static final String SCH_CODE = "COM_SCH";
	
	private static final String PROG_CODE = "PROG_MST";
	
	private static final String DOWNLOAD_URL = "/file/download.do";
	private static final String IMAGE_VIEW_URL = "/file/imageView.do";
	
	private static final String NO_IMG_URL = "/images/common/no_img.png";
	
	/** 파일업로드 */
	@Resource(name = "fileUpload")
	private FileUpload fileUpload;
	
	/** 파일업로드 */
	@Resource(name = "FFMPEGManager")
	private FFMPEGManager FFMPEGManager;
	
	/** Mapper */
	@Resource(name = "comFilesMapper")
	private ComFilesMapper comFilesMapper;
	
	@Resource(name = "comEditorFilesMapper")
	private ComEditorFilesMapper comEditorFilesMapper;
	
	@Resource(name = "cryptoUtil")
	private CryptoUtil cryptoUtil;

	/**
	 * 공통 파일에 업로드
	 *
	 * @param MultipartFile uploadFile 
	 * @param String uploadPath : 업로드 경로
	 * @param String code : 부모 코드
	 * @param String id : 부모 아이디
	 * @return int fileNo 
	 */
	public int uploadComFile(MultipartFile uploadFile, String uploadPath, String code, Object id) {
		
		int fileNo = 0;
		
		//파일 업로드
		FileVO fileVO = fileUpload.uploadFile(uploadFile, uploadPath, 0);
		
		//업로드가 성공한 경우 DB 등록
		if(fileVO.isResult()) {
			
			String inptId = "";
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				inptId = loginInfo.getLoginId();
			}
			String inptIp = ClientUtil.getUserIp();
			
			String uid = StringUtil.isNullToString(id);
			
			ComFilesVO comFilesVO = new ComFilesVO();
			
			comFilesVO.setParentCd(code);
			comFilesVO.setParentUid(uid);
			comFilesVO.setOrgName(fileVO.getOrgName());
			comFilesVO.setFileName(fileVO.getFileName());
			comFilesVO.setFilePath(fileVO.getFilePath());
			comFilesVO.setFileExt(fileVO.getFileExt());
			comFilesVO.setFileType(fileVO.getFileType());
			comFilesVO.setFileMime(fileVO.getFileMime());
			comFilesVO.setFileSize(fileVO.getFileSize());
			comFilesVO.setInptId(inptId);
			comFilesVO.setInptIp(inptIp);
			
			int result = comFilesMapper.insertComFiles(comFilesVO);
			if (result > 0) {
				fileNo = comFilesVO.getFileNo();
				fileLog(comFilesVO.getFileNo(), fileVO.getOrgName(), "UPLOAD", code, id);
			}
		}
		
		return fileNo;
		
	}
		
	/**
	 * 공통 파일 삭제
	 *
	 * @param String code : 부모 코드
	 * @param String id : 부모 아이디
	 * @return  
	 */
	public void removeComFileByParent(String code, Object id) {
		
		String uid = id.toString();
		
		List<ComFilesVO> fileList = comFilesMapper.selectComFilesList(code, uid);

		if(fileList == null) {
			return;
		}
		if(fileList.size() == 0) {
			return;
		}
		
		for (ComFilesVO comFilesVO : fileList ) {
			
			int deleteResult = comFilesMapper.deleteComFiles(comFilesVO.getFileNo());
			if(deleteResult > 0) {
				
				FileVO fileVO = new FileVO();
				fileVO.setFilePath(comFilesVO.getFilePath());
				fileVO.setFileName(comFilesVO.getFileName());
				
				boolean remove = fileUpload.removeFile(fileVO);
				
				//삭제된 파일이 이미지이면 썸네일 체크후 삭제
				if(remove && "IMAGE".equals(comFilesVO.getFileType())) {
					fileVO.setFileName("thumb_"+comFilesVO.getFileName());					
					fileUpload.removeFile(fileVO);
				}
				
				fileLog(comFilesVO.getFileNo(), comFilesVO.getOrgName(), "REMOVE", comFilesVO.getParentCd(), comFilesVO.getParentUid());
			}
		}
	}
	
	/**
	 * 데이터 수정시 첨부파일 넘어온 값 체크 후 파일 제거
	 * 
	 * @param String code : 부모 코드
	 * @param String id : 부모 아이디
	 * @param ComFilesVO
	 * @return ViewFileVO
	 */
	public void removeComFileByViewList(String code, Object id, List<ViewFileVO> viewFiles) {

		String uid = id.toString();

		if (viewFiles == null) {
			return;
		}

		// 유효성 검사를 위해 부모코드의 파일 목록을 조회한다.
		List<ComFilesVO> fileList = comFilesMapper.selectComFilesList(code, uid);

		for (ViewFileVO viewFileVO : viewFiles) {

			boolean fileDelete = false;
			int fileIndex = 0;

			if (viewFileVO.getFno() > 0) {

				for (int i = 0; i < fileList.size(); i++) {

					if (fileList.get(i).getFileNo() == viewFileVO.getFno()) {

						fileDelete = true;
						fileIndex = i;

						int deleteResult = comFilesMapper.deleteComFiles(fileList.get(i).getFileNo());
						if (deleteResult > 0) {

							FileVO fileVO = new FileVO();
							fileVO.setFilePath(fileList.get(i).getFilePath());
							fileVO.setFileName(fileList.get(i).getFileName());
							
							boolean remove = fileUpload.removeFile(fileVO);
							
							//삭제된 파일이 이미지이면 썸네일 체크후 삭제
							if(remove && "IMAGE".equals(fileList.get(i).getFileType())) {
								fileVO.setFileName("thumb_"+fileList.get(i).getFileName());					
								fileUpload.removeFile(fileVO);
							}

							fileLog(fileList.get(i).getFileNo(), fileList.get(i).getOrgName(), "REMOVE", code, uid);
						}

						break;
					}
				}

				if (fileDelete) {
					fileList.remove(fileIndex);
				}

			}
		}
	}
	
	/**
	 * 조회된 공통파일 리스트를 ViewFileVO 로 변환한다.
	 *
	 * @param ComFilesVO
	 * @return ViewFileVO
	 */
	public List<ViewFileVO> getViewFileList(String code, String id) {
		
		List<ComFilesVO> fileList = comFilesMapper.selectComFilesList(code, id);

		if(fileList == null) {
			return null;
		}
		if(fileList.size() == 0) {
			return null;
		}
		
		List<ViewFileVO> viewList = new ArrayList<ViewFileVO>();
		
		for (ComFilesVO comFilesVO : fileList ) {
			try {
				
				//경로 및 파일명 암호화
				String fpath = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFilePath()), "UTF-8");
				String fname = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFileName()), "UTF-8");
				String fmime = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFileMime()), "UTF-8");
				
				//사이즈를 스트링으로 변환
				String size = FileUtil.getStrFileSize(comFilesVO.getFileSize());

				ViewFileVO viewFile = new ViewFileVO();
				viewFile.setFno(comFilesVO.getFileNo());
				viewFile.setOname(comFilesVO.getOrgName());
				viewFile.setFname(fname);
				viewFile.setFpath(fpath);
				viewFile.setFext(comFilesVO.getFileExt());
				viewFile.setFtype(comFilesVO.getFileType());
				viewFile.setFmime(fmime);
				viewFile.setFsize(comFilesVO.getFileSize());
				viewFile.setSize(size);
				//부가정보 추가
				viewFile.setSupplInfo1(comFilesVO.getSupplInfo1());
				
				//다운로드 URL
				String downloadUrl = DOWNLOAD_URL 
									+ "?fno=" + comFilesVO.getFileNo()
									+ "&oname=" + comFilesVO.getOrgName()
									+ "&fpath=" + fpath
									+ "&fname=" + fname;
				viewFile.setDownloadUrl(downloadUrl);
				
				//이미지 URL
				if(comFilesVO.getFileType().equals("IMAGE")) {
					String viewUrl = IMAGE_VIEW_URL 
									    + "?fpath=" + fpath
									    + "&fname=" + fname
									    + "&fmime=" + fmime;
					viewFile.setViewUrl(viewUrl);
				}
				
				viewList.add(viewFile);

			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				LOGGER.error(e.getMessage());
				continue;
			}
		}

		return viewList;
	}
	
	/**
	 * 조회된 공통파일 리스트를 ViewFileVO 로 변환한다.
	 *
	 * @param ComFilesVO
	 * @return ViewFileVO
	 */
	public ViewFileVO getViewFile(int fileNo) {
		
		ComFilesVO comFilesVO = comFilesMapper.selectComFiles(fileNo);
		
		ViewFileVO viewFile = null;
		
		if(comFilesVO != null) {
			
			try {
				
				//경로 및 파일명 암호화
				String fpath = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFilePath()), "UTF-8");
				String fname = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFileName()), "UTF-8");
				String fmime = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFileMime()), "UTF-8");
				
				//사이즈를 스트링으로 변환
				String size = FileUtil.getStrFileSize(comFilesVO.getFileSize());

				viewFile = new ViewFileVO();
				viewFile.setFno(comFilesVO.getFileNo());
				viewFile.setOname(comFilesVO.getOrgName());
				viewFile.setFname(fname);
				viewFile.setFpath(fpath);
				viewFile.setFext(comFilesVO.getFileExt());
				viewFile.setFtype(comFilesVO.getFileType());
				viewFile.setFmime(fmime);
				viewFile.setFsize(comFilesVO.getFileSize());
				viewFile.setSize(size);
				
				//다운로드 URL
				String downloadUrl = DOWNLOAD_URL 
									+ "?fno=" + comFilesVO.getFileNo()
									+ "&oname=" + comFilesVO.getOrgName()
									+ "&fpath=" + fpath
									+ "&fname=" + fname;
				viewFile.setDownloadUrl(downloadUrl);
				
				//이미지 URL
				if(comFilesVO.getFileType().equals("IMAGE")) {
					String viewUrl = IMAGE_VIEW_URL 
									    + "?fpath=" + fpath
									    + "&fname=" + fname
									    + "&fmime=" + fmime;
					viewFile.setViewUrl(viewUrl);
				}

			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				LOGGER.error(e.getMessage());
				viewFile = null;
			}
		}
		
		return viewFile;
	}
	
	/**
	 * 다운로드 로그 등록
	 */	
	public void downloadFileLog(int fno, String oname, String fpath, String fname) {

		String orgName = oname;
		String state = "DOWNLOAD";
		String code = "UNKNOWN";
		String id = "UNKNOWN";
		
		//파일 조회
		ComFilesVO comFilesVO = comFilesMapper.selectComFiles(fno);
		if(comFilesVO != null) {
			code = comFilesVO.getParentCd();
			id = comFilesVO.getParentUid();
		}
		
		fileLog(fno, orgName, state, code, id);
	}
	
	/**
	 * 파일 로그 등록
	 */	
	private void fileLog(int fileNo, String orgName, String state, String code, Object id) {
		
		String memo = "";
		
		switch (state) {
			case "UPLOAD":
				memo = "UPLOAD";
				break;
				
			case "DOWNLOAD":
				memo = "DOWNLOAD";
				break;
				
			case "REMOVE":
				memo = "REMOVE";
				break;	
			default:
				break;
		}
		
		String inptId = "";
		LoginVO loginInfo = SessionUtil.getLoginUser();
		if(loginInfo != null) {
			inptId = loginInfo.getLoginId();
		}
		String inptIp = ClientUtil.getUserIp();
		
		String uid = StringUtil.isNullToString(id);
		
		ComFilesLogVO comFilesLogVO = new ComFilesLogVO();
		comFilesLogVO.setFileNo(fileNo);
		comFilesLogVO.setParentCode(code);
		comFilesLogVO.setParentUid(uid);
		comFilesLogVO.setMemo(memo);
		comFilesLogVO.setOrgName(orgName);
		comFilesLogVO.setInptId(inptId);
		comFilesLogVO.setInptIp(inptIp);
		
		comFilesMapper.insertComFilesLog(comFilesLogVO);		
		
	}
	
	
	/**
	 * 게시판 첨부파일 업로드
	 *
	 * @param MultipartFile uploadFile 
	 * @param String uploadPath : 업로드 경로
	 * @param String id : 부모 아이디
	 * @param String bbsCd : 게시판 코드
	 * @return int fileNo 
	 */
	public int uploadComFileByBbs(MultipartFile uploadFile, String bbsType, String bbsCd, String bbsId) {
		
		int fileNo = 0;
		
		if(StringUtil.isNull(bbsCd)) {
			return 0;
		}
		
		String parentCd = getParentCdByBbsType(bbsType);
		if(parentCd == null) {
			return 0;
		}
		
		String uploadPath = fileUpload.getFileRootPath() + FileUtil.SEPERATOR + parentCd + FileUtil.SEPERATOR + bbsCd;
		
		//파일 업로드
		FileVO fileVO = fileUpload.uploadFile(uploadFile, uploadPath, 0);
		
		//업로드가 성공한 경우 DB 등록
		if(fileVO.isResult()) {
			
			if(FileUtil.VIDEO_EXT.contains(fileVO.getFileExt().toLowerCase())) {
				FileVO convertFileVO = FFMPEGManager.convertToMp4(fileVO);
				if(convertFileVO != null) {
					fileVO.setFileName(convertFileVO.getFileName());
					fileVO.setFileExt(convertFileVO.getFileExt());
					fileVO.setOrgName(convertFileVO.getOrgName());
					fileVO.setFileMime(convertFileVO.getFileMime());
					fileVO.setFileSize(convertFileVO.getFileSize());					
				}
			}
			
			String inptId = "";
			LoginVO loginInfo = SessionUtil.getLoginUser();
			if(loginInfo != null) {
				inptId = loginInfo.getLoginId();
			}
			String inptIp = ClientUtil.getUserIp();
			
			String uid = StringUtil.isNullToString(bbsId);
			
			ComFilesVO comFilesVO = new ComFilesVO();
			
			comFilesVO.setParentCd(parentCd);
			comFilesVO.setParentUid(uid);
			comFilesVO.setOrgName(fileVO.getOrgName());
			comFilesVO.setFileName(fileVO.getFileName());
			comFilesVO.setFilePath(fileVO.getFilePath());
			comFilesVO.setFileExt(fileVO.getFileExt());
			comFilesVO.setFileType(fileVO.getFileType());
			comFilesVO.setFileMime(fileVO.getFileMime());
			comFilesVO.setFileSize(fileVO.getFileSize());
			comFilesVO.setInptId(inptId);
			comFilesVO.setInptIp(inptIp);
			
			//게시판 코드
			comFilesVO.setSupplInfo1(bbsCd);
			
			int result = comFilesMapper.insertComFiles(comFilesVO);
			if (result > 0) {
				fileNo = comFilesVO.getFileNo();
				fileLog(comFilesVO.getFileNo(), fileVO.getOrgName(), "UPLOAD", parentCd, bbsId);
				
				//파일이 이미지인경우 썸네일 생성
				if("IMAGE".equals(fileVO.getFileType())) {
					ThumbUtil.createThumbnail(fileVO, 0, 0);
				}
			}
		}
		
		return fileNo;
		
	}
	
	/**
	 * 게시판삭제시 해당 게시글 첨부파일 삭제
	 *
	 * @param String code : 부모 코드
	 * @param String id : 부모 아이디
	 * @return  
	 */
	public void removeComFileByBbsCd(String bbsType, String bbsCd) {
		
		String parentCd = getParentCdByBbsType(bbsType);
		if(parentCd == null) {
			return;
		}
		
		int deleteResult = comFilesMapper.deleteComFilesByBbsCd(parentCd, bbsCd);
	
		if(deleteResult > 0) {
			//첨부파일삭제
			String path = fileUpload.getFileRootPath() + FileUtil.SEPERATOR + parentCd + FileUtil.SEPERATOR + bbsCd;		
			fileUpload.removeFolder(path);
			
			//에디터파일은 삭제시 리스트조회후 처리해야하므로 임시파일로 변경한다.
			comEditorFilesMapper.resetcomEditorFilesByBbsCd(parentCd, bbsCd);
		}
	}

	/**
	 * 게시판 썸네일 조회
	 *
	 * @param ComFilesVO
	 * @return ViewFileVO
	 */
	public String getThumbnail(String code, String id) {
		
		String thumbnail = NO_IMG_URL;
		
		ComFilesVO comFilesVO = comFilesMapper.selectThumbnail(code, id);

		if(comFilesVO != null) {
			try {
				
				//경로 및 파일명 암호화
				String fpath = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFilePath()), "UTF-8");
				String fname = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFileName()), "UTF-8");
				String fmime = URLEncoder.encode(cryptoUtil.encrypt(comFilesVO.getFileMime()), "UTF-8");
								
				String viewUrl = IMAGE_VIEW_URL 
					    + "?fpath=" + fpath
					    + "&fname=" + fname
					    + "&fmime=" + fmime;
				
				
				thumbnail = viewUrl;

			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				LOGGER.error(e.getMessage());
			}
		}

		return thumbnail;
	}
	
	/**
	 * 이미지 URL 리턴
	 *
	 * @param ComFilesVO
	 * @return ViewFileVO
	 */
	public String getImageSrc(String imgPath, String imgName, String imgMime) {
		
		String imageSrc = "";
		
		if(StringUtil.isNull(imgPath) || StringUtil.isNull(imgName)|| StringUtil.isNull(imgMime)) {
			return null;
		}
		
		try {			
			//경로 및 파일명 암호화
			String fpath = URLEncoder.encode(cryptoUtil.encrypt(imgPath), "UTF-8");
			String fname = URLEncoder.encode(cryptoUtil.encrypt(imgName), "UTF-8");
			String fmime = URLEncoder.encode(cryptoUtil.encrypt(imgMime), "UTF-8");
						
			imageSrc = IMAGE_VIEW_URL + "?fpath=" + fpath + "&fname=" + fname + "&fmime=" + fmime;

		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			LOGGER.error(e.getMessage());
			imageSrc = null;
		}
		
		return imageSrc;
	}
	
	private String getParentCdByBbsType(String bbsType) {
		if(bbsType == null) {
			return null;
		}
		bbsType = bbsType.toLowerCase();
		String parentCd = "";		
		if("bbs".equals(bbsType)) {
			parentCd = BBS_CODE;
		} else if("sch".equals(bbsType)) {
			parentCd = SCH_CODE;
		} else if("prog".equals(bbsType)) {
			parentCd = PROG_CODE;
		} else {
			parentCd = null;
		}
		return parentCd;
	}
}
