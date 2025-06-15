package kr.co.nanwe.auth.service;

import lombok.Data;

/**
 * @Class Name 		: UserAuthVO
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
@Data
public class UserAuthVO {
	private String authDvcd;
	private String userDvcd;
	private String userDvnm;
	private String workDvcd;
	private String workDvnm;
	private String statDvcd;
	private String statDvnm;
	private boolean checked;
}
