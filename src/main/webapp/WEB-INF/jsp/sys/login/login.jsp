<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: login.jsp
 * @Description : 사용자 로그인 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="loginVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data" action="/sys/loginAction.do">
	<form:hidden path="loginCertKey"/>
	<form:hidden path="loginId"/>
	<form:password path="loginPw" cssStyle="display:none;"/>
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>  	
   	<% /** RETURN */ %>
   	<input type="hidden" name="returnLogin" value="<c:out value='${returnLogin}' escapeXml="false"/>">
  	<% /** //RETURN */ %>  	
</form:form>
<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="loginVO" staticJavascript="false" xhtml="true" cdata="false"/>
<% /** RSA 관련 import */ %>
<script type="text/javascript" src="/js/common/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/common/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/common/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/common/rsa/rng.js"></script>
<% /** //RSA 관련 import */ %>
<script type="text/javascript" src="/js/common/gf_login.js"></script>
<script type="text/javascript" src="/js/common/gf_certification.js"></script>