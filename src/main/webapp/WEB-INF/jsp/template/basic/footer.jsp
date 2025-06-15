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
				<li><a href="https://www.wku.ac.kr/privacy/int_privacy.html" target="_blank">개인정보처리방침</a></li>
				<li><a href="https://www.wku.ac.kr/privacy/%ec%a0%95%eb%b3%b4%eb%b3%b4%ed%98%b8%ec%84%a0%ec%96%b8%eb%ac%b8.html" target="_blank">정보보호선언문</a></li>
				<li><a href="https://www.wku.ac.kr/privacy/int_emailaddr.html" target="_blank">이메일무단수집거부</a></li>
	        </ul>
		</div>
	</div>
	<div class="address">
		<div class="container">
			<div class="logo">
				<img src="/images/site/btm_logo.png" alt="<c:out value='${GV_SITE_INFO.siteNm }'/>">
			</div>
			<div class="footer_info">
				<p class="site"><c:out value="${GV_SITE_INFO.siteEngNm }"/><br/><c:out value=" ${GV_SITE_INFO.siteInfo03 }"/></p>
				<ul>
					<li><c:out value='${GV_SITE_INFO.siteInfo02 }'/></li>
					<li><c:out value='${GV_SITE_INFO.siteAddr }'/></li>
					<li><c:out value='TEL : ${GV_SITE_INFO.telNo }'/></li>
					<li><c:out value='FAX : ${GV_SITE_INFO.faxNo }'/></li>
					<li><c:out value='Email : ${GV_SITE_INFO.siteInfo01 }'/></li>
				</ul>
				<p class="copy">COPYRIGHT © 2017 <c:out value='${GV_SITE_INFO.siteEngNm }'/>. ALL RIGHTS RESERVED.</p>
			</div>
		</div>
     </div>
</footer>