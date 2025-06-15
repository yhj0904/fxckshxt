<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 컨텐츠관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="content_wrap">
	<c:choose>
		<c:when test='${contentVO.contEngCont ne null and contentVO.contEngCont ne "" and GV_LANGUAGE eq "en" }'><c:out value="${contentVO.contEngCont }" escapeXml="false"/></c:when>
		<c:otherwise><c:out value="${contentVO.contCont }" escapeXml="false"/></c:otherwise>
	</c:choose>
</div>