<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: header.jsp
 * @Description : 상단 HEADER 영역
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<header class="header">
	<div class="inner_wrap">
		<div class="logo">
			<a href="/sys.do" title="<spring:message code="text.home" />">ADMINISTRATOR</a>
		</div>
		<% /* 언어변경 */%>
		<div class="change_locale">
			<div class="dropdown">
				<span><spring:message code="locale.${GV_LANGUAGE }" /></span>
				<button type="button"><span class="none">OPEN</span></button>
				<ul>
					<c:if test='${GV_LANGUAGE ne "ko" or GV_LANGUAGE eq null or GV_LANGUAGE eq ""}'>
						<li><a href="javascript:gf_changeLocale('ko')"><spring:message code="locale.ko" /></a></li>
					</c:if>
					<c:if test='${GV_LANGUAGE ne "en"}'>
						<li><a href="javascript:gf_changeLocale('en')"><spring:message code="locale.en" /></a></li>
					</c:if>
				</ul>
			</div>
		</div>
		<% /* GNB START */%>
		<c:import url="/WEB-INF/jsp/template/system/gnb.jsp"></c:import>
		<% /* //GNB END */%>
	</div>
	
</header>