<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: main.jsp
 * @Description : 메인템플릿
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<!DOCTYPE html>
<html lang="<c:out value='${GV_LANGUAGE }'/>">
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
	<link href="/css/template/system/layout.css" media="all" rel="stylesheet" type="text/css">	
	<link href="/css/template/system/main.css" media="all" rel="stylesheet" type="text/css">
	<% /* 공통 JS */%>
	<c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>
</head>
<body>

	<% /* TOP */%>
	<c:import url="/WEB-INF/jsp/cmmn/top.jsp"></c:import>
	<% /* //TOP */%>

	<div class="wrap">
	
		<% /* HEADER START */%>
		<c:import url="/WEB-INF/jsp/template/system/header.jsp"></c:import>
		<% /* //HEADER END */%>
		
		<% /* CONTENT START */%>
		<section class="main">
			<div id="site_main">
				<tiles:insertAttribute name="body"/>
			</div>
		</section>
		<% /* //CONTENT END */%>
		
		<% /* FOOTER START */%>
		<c:import url="/WEB-INF/jsp/template/system/footer.jsp"></c:import>
		<% /* //FOOTER END */%>
	
	</div>

	<% /* BOTTOM */%>
	<c:import url="/WEB-INF/jsp/cmmn/bottom.jsp"></c:import>
	<% /* //BOTTOM */%>
</body>
</html>