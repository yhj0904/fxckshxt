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
	<c:choose>
		<c:when test='${GV_TEMPLATE_CODE eq "system"}'>
			<c:import url="/WEB-INF/jsp/cmmn/css.jsp"></c:import>
		</c:when>
		<c:otherwise>
			<c:import url="/WEB-INF/jsp/cmmn/user_css.jsp"></c:import>
		</c:otherwise>
	</c:choose>
	<% /* 템플릿 CSS */%>
	<c:if test='${GV_TEMPLATE_CODE ne null and GV_TEMPLATE_CODE ne ""}'>
		<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/layout.css" media="all" rel="stylesheet" type="text/css">	
		<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/main.css" media="all" rel="stylesheet" type="text/css">
	</c:if>
	<c:if test='${!empty GV_SITE_INFO}'>
		<link href="/css/site/<c:out value='${GV_SITE_INFO.viewCd}'/>.css" media="all" rel="stylesheet" type="text/css">
	</c:if>
	<% /* 공통 JS */%>
	<c:choose>
		<c:when test='${GV_TEMPLATE_CODE eq "system"}'>
			<c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>
		</c:when>
		<c:otherwise>
			<c:import url="/WEB-INF/jsp/cmmn/user_js.jsp"></c:import>
		</c:otherwise>
	</c:choose>
</head>
<body>
	<% /* TOP */%>
	<c:import url="/WEB-INF/jsp/cmmn/top.jsp"></c:import>
	<% /* //TOP */%>
	
	<% /* POPUP IMPORT */%>
	<c:import url="/WEB-INF/jsp/main/popup.jsp"></c:import>
	<% /* //POPUP IMPORT */%>
		
	<% /* TEMPLATE IMPORT */%>
	<c:if test='${GV_TEMPLATE_CODE ne null and GV_TEMPLATE_CODE ne ""}'>
		<c:import url="/WEB-INF/jsp/template/${GV_TEMPLATE_CODE}/main.jsp"></c:import>
	</c:if>
	<% /* //TEMPLATE IMPORT */%>
	
	<% /* BOTTOM */%>
	<c:import url="/WEB-INF/jsp/cmmn/bottom.jsp"></c:import>
	<% /* //BOTTOM */%>
</body>
</html>