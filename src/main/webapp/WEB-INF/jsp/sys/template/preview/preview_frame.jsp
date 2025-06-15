<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: preview.jsp
 * @Description : 템플릿 미리보기
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
	<title><spring:message code="content.site.info.title" /></title>
	<% /* 공통 메타 태그 */%>
	<c:import url="/WEB-INF/jsp/cmmn/meta.jsp"></c:import>	
	<% /* 공통 CSS */%>
	<c:import url="/WEB-INF/jsp/cmmn/css.jsp"></c:import>
	<% /* 템플릿 CSS */%>
	<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/layout.css" media="all" rel="stylesheet" type="text/css">
	<c:choose>
		<c:when test='${type eq "MAIN" }'>
			<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/main.css" media="all" rel="stylesheet" type="text/css">		
		</c:when>
		<c:when test='${type eq "SUB" }'>
			<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/sub.css" media="all" rel="stylesheet" type="text/css">
		</c:when>
		<c:when test='${type eq "LOGIN" }'>
			<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/login.css" media="all" rel="stylesheet" type="text/css">		
		</c:when>
		<c:when test='${type eq "EMPTY" }'>
			<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/empty.css" media="all" rel="stylesheet" type="text/css">		
		</c:when>
		<c:when test='${type eq "POP" }'>
			<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/pop.css" media="all" rel="stylesheet" type="text/css">
		</c:when>
	</c:choose>
	<link href="/css/template/<c:out value='${GV_TEMPLATE_CODE}'/>/main.css" media="all" rel="stylesheet" type="text/css">
	<% /* 공통 JS */%>
	<c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>
</head>
<body <c:if test='${type eq "POP"}'> style="background:#f9f9f9;"</c:if>>
	<tiles:putAttribute name="body" value="PREVIEW PAGE"/>
	<c:choose>
		<c:when test='${type eq "MAIN" }'>
		
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
			
		</c:when>
		
		<c:when test='${type eq "SUB" }'>
			
			<% /* TOP */%>
			<c:import url="/WEB-INF/jsp/cmmn/top.jsp"></c:import>
			<% /* //TOP */%>
		
			<% /* TEMPLATE IMPORT */%>
			<c:if test='${GV_TEMPLATE_CODE ne null and GV_TEMPLATE_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/template/${GV_TEMPLATE_CODE}/sub.jsp"></c:import>
			</c:if>
			<% /* //TEMPLATE IMPORT */%>
			
			<% /* BOTTOM */%>
			<c:import url="/WEB-INF/jsp/cmmn/bottom.jsp"></c:import>
			<% /* //BOTTOM */%>
			
		</c:when>
		
		<c:when test='${type eq "LOGIN" }'>
		
			<% /* TOP */%>
			<c:import url="/WEB-INF/jsp/cmmn/top.jsp"></c:import>
			<% /* //TOP */%>
			
			<% /* TEMPLATE IMPORT */%>
			<c:if test='${GV_TEMPLATE_CODE ne null and GV_TEMPLATE_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/template/${GV_TEMPLATE_CODE}/login.jsp"></c:import>
			</c:if>
			<% /* //TEMPLATE IMPORT */%>
			
			<% /* BOTTOM */%>
			<c:import url="/WEB-INF/jsp/cmmn/bottom.jsp"></c:import>
			<% /* //BOTTOM */%>
			
		</c:when>
		
		<c:when test='${type eq "EMPTY" }'>
		
			<% /* TEMPLATE IMPORT */%>
			<c:if test='${GV_TEMPLATE_CODE ne null and GV_TEMPLATE_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/template/${GV_TEMPLATE_CODE}/empty.jsp"></c:import>
			</c:if>
			<% /* //TEMPLATE IMPORT */%>
			
		</c:when>
		
		<c:when test='${type eq "POP" }'>
		
			<% /* TEMPLATE IMPORT */%>
			<c:if test='${GV_TEMPLATE_CODE ne null and GV_TEMPLATE_CODE ne ""}'>
				<c:import url="/WEB-INF/jsp/template/${GV_TEMPLATE_CODE}/pop.jsp"></c:import>
			</c:if>
			<% /* //TEMPLATE IMPORT */%>
			
		</c:when>
	</c:choose>
</body>
</html>