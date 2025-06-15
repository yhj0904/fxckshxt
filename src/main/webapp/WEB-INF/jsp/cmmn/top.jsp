<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: top.jsp
 * @Description : 상단 IMPORT
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<!--[if lt IE 10]>
<div class="ie_error">
	<p><spring:message code="message.error.ie" /><a href="https://support.microsoft.com/ko-kr/help/17621/internet-explorer-downloads"><spring:message code="button.download" /></a></p>
</div>
<![endif]-->
<div class="skip_navigation">
	<ul>
		<li><a href="#site_main"><spring:message code="skipNagivation.main" /></a></li>
	</ul>
</div>
<noscript>
	<div class="noscript"><spring:message code="message.noscript" /></div>
</noscript>