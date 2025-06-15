package kr.co.nanwe.auth.service;

import java.util.List;

import lombok.Data;

/**
 * @Class Name 		: AuthVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class AuthVO {
	/** 권한코드 */
	private String authCd;
	/** 권한명 */
	private String authNm;
	/** 사용여부 */
	private String useYn;
	/** 비고 */
	private String note;
	/** 입력ID */
	private String inptId;
	/** 입력IP */
	private String inptIp;
	/** 입력일시 */
	private String inptDttm;
	/** 수정ID */
	private String modiId;
	/** 수정IP */
	private String modiIp;
	/** 수정일시 */
	private String modiDttm;
	
	private List<AuthMapVO> mapList;
	
	private List<AuthDeptVO> deptList;
	
	private String[] authDvcd;
	private String[] userDvcd;
	private String[] userDvnm;
	private String[] workDvcd;
	private String[] workDvnm;
	private String[] statDvcd;
	private String[] statDvnm;
	private String[] deptDvcd;
	private String[] deptCd;
	private String[] deptNm;
}
