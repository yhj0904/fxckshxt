<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: footer.jsp
 * @Description : 하단 FOOTER 영역
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 
 */
%>
<footer class="footer">
	<div class="bottom_link">
		<div class="container">
			<ul>
	        </ul>
		</div>
	</div>
	<div class="address">
		<div class="container">
			<ul>
				<li><c:out value='${GV_SITE_INFO.siteAddr }'/></li>
				<li><c:out value='${GV_SITE_INFO.telNo }'/></li>
				<li><c:out value='${GV_SITE_INFO.faxNo }'/></li>
			</ul>
			<p class="copy">copyright © <strong><c:out value='${GV_SITE_INFO.siteEngNm }'/></strong> all rights reserved.</p>		 
			<div class="logo">
				<img src="/images/site/logo.png" alt="<c:out value='${GV_SITE_INFO.siteNm }'/>">
			</div>
		</div>
     </div>
</footer>