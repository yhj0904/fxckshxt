<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: index.jsp
 * @Description : 메인페이지
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<c:if test='${!empty GV_SITE_INFO}'>
	<c:import url="/WEB-INF/jsp/main/site/${GV_SITE_INFO.viewCd }.jsp"></c:import>
</c:if>