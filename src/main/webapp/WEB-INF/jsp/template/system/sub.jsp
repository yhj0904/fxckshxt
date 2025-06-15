<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: sub.jsp
 * @Description : 서브템플릿
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
	<link href="/css/template/system/sub.css" media="all" rel="stylesheet" type="text/css">
	<% /* 게시판일 경우 IMPORT */%>	
	<c:if test='${!empty bbsMgtVO and GV_BOARD_SKIN_CODE ne null and GV_BOARD_SKIN_CODE ne ""}'>
		<link href="/css/board/<c:out value='${GV_BOARD_SKIN_CODE}'/>.css" media="all" rel="stylesheet" type="text/css">
	</c:if>
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
				<c:if test='${GV_PROG_NM ne null and GV_PROG_NM ne ""}'>
					<div class="stitle">
						<c:choose>
							<c:when test='${!empty bbsMgtVO and GV_PRESENT_PATH eq "/sys/bbs"}'>
								<c:out value="${bbsMgtVO.title }"/>
							</c:when>
							<c:when test='${!empty schMgtVO and GV_PRESENT_PATH eq "/sys/sch"}'>
								<c:out value="${schMgtVO.title }"/>
							</c:when>
							<c:otherwise>
								<c:out value="${GV_PROG_NM }"/>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
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