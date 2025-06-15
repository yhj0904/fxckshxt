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
 * @ 
 */
%>
<div class="wrap">
	<% /* HEADER START */%>
	<c:import url="/WEB-INF/jsp/template/{TEMPLATE_CODE}/header.jsp"></c:import>
	<% /* //HEADER END */%>
	
	<% /* CONTENT START */%>
	<section class="main" id="site_main">
		<tiles:insertAttribute name="body"/>
	</section>
	<% /* //CONTENT END */%>
	
	<% /* FOOTER START */%>
	<c:import url="/WEB-INF/jsp/template/{TEMPLATE_CODE}/footer.jsp"></c:import>
	<% /* //FOOTER END */%>	
</div>