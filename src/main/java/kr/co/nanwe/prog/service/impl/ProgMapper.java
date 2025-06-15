package kr.co.nanwe.prog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.bbs.service.BbsVO;
import kr.co.nanwe.file.service.ExcelDownloadHandler;
import kr.co.nanwe.prog.service.ProgUserVO;
import kr.co.nanwe.prog.service.ProgVO;

/**
 * @Class Name 		: ProgMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */

@Mapper("progMapper")
public interface ProgMapper {
	
	/** 전체 Row 개수 */
	int selectProgTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 */
	List<ProgVO> selectProgList(Map<String, Object> paramMap);
	
	/** 상세 조회 */
	ProgVO selectProg(@Param("progId") int progId);
	
	/** 상세 조회 - UPDATE BEFORE */
	ProgVO selectProgUpBfView(@Param("progId") int progId);
	
	/** 등록 */
	int insertProg(ProgVO progVO);
	
	/** 수정 */
	int updateProg(ProgVO progVO);
	
	/** 삭제 */
	int deleteProg(@Param("progId") int progId);
	
	/** 삭제상태로 변경 */
	int deleteProgToDelete(ProgVO progVO);
	
	/** 프로그램ID 조회 */
	String selectProgId();
	
	/** 프로그램 첨부파일 수정 */
	int updateProgThumbFile(ProgVO progVO);
	
	/** 전체 Row 개수 - 사용자 */
	int selectProgUserViewTotCnt(Map<String, Object> paramMap);
	
	/** 목록 조회 - 사용자 */
	List<ProgVO> selectProgUserViewList(Map<String, Object> paramMap);
	
	/** 상세 조회 - 사용자 */
	ProgVO selectProgUserView(@Param("progId") int progId);
	
	/** 프로그램 신청 - 사용자 */
	int insertProgUser(ProgUserVO progUserVO);
	
	/** 프로그램 신청자 관리 - 전체 Row 개수 */
	int selectProgApplUserTotCnt(@Param("progId") int progId);
	
	/** 프로그램 신청자 관리 - 전체 Row 개수 */
	int selectProgApplUserTotCnt(Map<String, Object> paramMap);
	
	/** 프로그램 신청자 관리 */
	List<Map> selectProgApplUserList(Map<String, Object> paramMap);
	
	/** 프로그램 신청자 삭제 */
	int deleteProgApplUser(@Param("progId") int progId, @Param("userId") String userId);

	/** 프로그램 비회원 신청자 삭제 */
	int deleteProgApplPublicUser(@Param("progId") int progId, @Param("userId") String userId);
	
	/** 프로그램 신청자 상세조회 */
	Map<String, Object> selectProgApplUser(@Param("progId") int progId, @Param("userId") String userId);
	
	/** 프로그램 수강생 이수 / 미이수 처리 */
	int updateProgComplUser(Map<String, Object> paramMap);

	/** 프로그램 비회원 수강생 이수 / 미이수 처리 */
	int updateProgComplPublicUser(Map<String, Object> paramMap);
	
	/** 사용자 프로그램 신청 내역 ROW 개수 */
	int selectMyProgTotCnt(@Param("progId") int progId, @Param("userId") String userId);
	
	/** 사용자 프로그램 신청 내역 ROW 개수 */
	int selectMyProgTotCnt(Map<String, Object> paramMap);
	
	/** 사용자 프로그램 신청 내역 */
	List<Map> selectMyProgList(Map<String, Object> paramMap);
	
	/** 사용자 프로그램 신청 취소 / 재신청 */
	int updateProgUser(Map<String, Object> paramMap);
	
	/** 목록 조회 - 메인화면*/
	List<ProgVO> selectProgUserMainList(Map<String, Object> paramMap);
	
	/** 설문완료 처리 */
	int updateSurvYn(@Param("progId") int progId, @Param("userId") String userId);
	
	/** 신청자 엑셀 다운로드 */
	void selectExcelProgApplUserList(Map<String, Object> paramMap, ExcelDownloadHandler<HashMap<String, Object>> handler);
}
