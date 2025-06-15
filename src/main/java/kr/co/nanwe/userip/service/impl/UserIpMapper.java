package kr.co.nanwe.userip.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.userip.service.UserIpVO;

/**
 * @Class Name 		: UserIpMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("userIpMapper")
public interface UserIpMapper {
	
	/** 전체 Row 개수 */
	int selectUserIpTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<UserIpVO> selectUserIpList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	UserIpVO selectUserIp(@Param("userId") String userId, @Param("ip") String ip);
	
	/** 등록 */
	int insertUserIp(UserIpVO userIpVO);
	
	/** 수정 */
	int updateUserIp(UserIpVO userIpVO);
	
	/** 삭제 */
	int deleteUserIp(@Param("userId") String userId, @Param("ip") String ip);

	/** 엑셀 목록 조회 (*필수 return void)*/
	void selectUserIpExcelList(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);
	
	/** 존재하는 아이디 & 아이피 인지 체크 */
	int selectUserIpExist(@Param("userId") String userId, @Param("ip") String ip);

	/** 아이피 삭제 */
	int deleteUserIpByUserId(String userId);
	
}
