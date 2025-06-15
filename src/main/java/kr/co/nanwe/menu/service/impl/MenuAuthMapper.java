package kr.co.nanwe.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import kr.co.nanwe.menu.service.MenuAuthVO;

/**
 * @Class Name 		: MenuAuthMapper
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Mapper("menuAuthMapper")
public interface MenuAuthMapper {
	
	List<MenuAuthVO> selectMenuAuthListByMenuId(String menuId);
	
	int insertMenuAuth(MenuAuthVO menuAuthVO);
	
	int deleteMenuAuthByMenuId(String menuId);

	int deleteMenuAuthByAuthCd(String authCd);

	int deleteMenuAuthByMenuCd(String menuCd);

	int checkProgramAuth(Map<String, Object> paramMap);
	
}
