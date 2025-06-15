<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: meta.jsp
 * @Description : 공통 메타태그
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<meta http-equiv="Content-Language" content="<c:out value='${GV_LANGUAGE }'/>">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<c:choose>
	<c:when test='${!empty GV_SITE_INFO }'>
		<c:choose>
			<c:when test='${GV_SITE_INFO.siteEngNm ne null and GV_SITE_INFO.siteEngNm ne "" and GV_LANGUAGE eq "en" }'>
				<meta http-equiv="title" content="<c:out value='${GV_SITE_INFO.siteEngNm }'/>">
				<meta http-equiv="description" content="<c:out value='${GV_SITE_INFO.siteEngNm }'/>">
				<meta http-equiv="keywords" content="<c:out value='${GV_SITE_INFO.siteEngNm }'/>">
			</c:when>
			<c:otherwise>
				<meta http-equiv="title" content="<c:out value='${GV_SITE_INFO.siteNm }'/>">
				<meta http-equiv="description" content="<c:out value='${GV_SITE_INFO.siteNm }'/>">
				<meta http-equiv="keywords" content="<c:out value='${GV_SITE_INFO.siteNm }'/>">
			</c:otherwise>
		</c:choose>
		<c:if test='${GV_SITE_INFO.siteMeta ne null and GV_SITE_INFO.siteMeta ne ""}'>
			<c:out value='${GV_SITE_INFO.siteMeta }' escapeXml="false"/>
		</c:if>
	</c:when>
	<c:otherwise>
		<meta http-equiv="title" content="<spring:message code="content.site.info.title" />">
		<meta http-equiv="description" content="<spring:message code="content.site.info.title" />">
		<meta http-equiv="keywords" content="<spring:message code="content.site.info.title" />">		
	</c:otherwise>
</c:choose>
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<meta name="format-detection" content="telephone=no, address=no, email=no">
<link rel="shortcut icon" href="/images/site/favicon.ico">