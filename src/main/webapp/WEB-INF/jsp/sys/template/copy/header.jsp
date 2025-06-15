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
	<div class="util_wrap">
		<div class="container">
			<div class="left_box">
				<ul>
					<li><a class="bg1" href="/"><spring:message code="text.home"/></a></li>
				</ul>
			</div>
			<div class="right_box">
				<ul>
					<c:choose>
						<c:when test='${!empty LOGIN_USER }'>
							<li><a class="bg1" href="/logout.do"><spring:message code="button.logout"/></a></li>
							<li><a class="bg2" href="/user/mypage.do"><spring:message code="button.mypage"/></a></li>
						</c:when>
						<c:otherwise>
							<li><a class="bg1" href="/login.do"><spring:message code="button.login"/></a></li>
							<li><a class="bg2" href="/join.do"><spring:message code="text.join"/></a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</div>
	<div class="logo_wrap">
		<div class="container">
			<div class="logo">
				<a href="/" title="<spring:message code="text.home" />"><img src="/images/site/logo.png" alt="<c:out value='${GV_SITE_INFO.siteNm }'/>"></a>
			</div>
			<% /* GNB START */%>
			<c:import url="/WEB-INF/jsp/template/basic/gnb.jsp"></c:import>
			<% /* //GNB END */%>
		</div>
	</div>	
</header>