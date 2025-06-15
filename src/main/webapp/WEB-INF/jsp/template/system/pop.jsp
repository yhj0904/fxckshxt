<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: pop.jsp
 * @Description : 팝업템플릿
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<head>
	<c:choose>
		<c:when test='${!empty GV_SITE_INFO }'>			
			<c:choose>
				<c:when test='${GV_SITE_INFO.siteEngNm ne null and GV_SITE_INFO.siteEngNm ne "" and GV_LANGUAGE eq "en" }'>
					<title><c:out value='${GV_SITE_INFO.siteEngNm }'/></title>
				</c:when>
				<c:otherwise>
					<title><c:out value='${GV_SITE_INFO.siteNm }'/></title>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<title><spring:message code="content.site.info.title" /></title>
		</c:otherwise>
	</c:choose>	
	<% /* 공통 메타 태그 */%>
	<c:import url="/WEB-INF/jsp/cmmn/meta.jsp"></c:import>	
	<% /* 공통 CSS */%>
	<c:import url="/WEB-INF/jsp/cmmn/css.jsp"></c:import>
	<% /* 공통 JS */%>
	<c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>
	<link href="/css/template/system/layout.css" media="all" rel="stylesheet" type="text/css">	
	<link href="/css/template/system/main.css" media="all" rel="stylesheet" type="text/css">
</head>
<body style="background:#f9f9f9;">	
	<div class="wrap">
		<div style="padding:10px;">
			<tiles:insertAttribute name="body"/>
		</div>
	</div>
</body>
</html>