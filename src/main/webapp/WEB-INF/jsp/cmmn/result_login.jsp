<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="java.net.URLDecoder" %>
<%
/**
 * @Class Name 	: result_login.jsp
 * @Description : 로그인 처리 후 결과 처리 페이지
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<%
	//리다이렉트 파라미터
	Map<String, Object> parameterMap = new HashMap<String, Object>();
	String returnLogin = "/";
	String actionUrl = "/";

	if (request.getAttribute("returnLogin") != null && !"".equals(request.getAttribute("returnLogin"))) {
		returnLogin = (String) request.getAttribute("returnLogin");		
		try {
			returnLogin = URLDecoder.decode(returnLogin, "UTF-8");
			if (returnLogin.indexOf("?") > -1) {
				actionUrl = returnLogin.split("\\?")[0];
				String params = returnLogin.split("\\?")[1];
				String[] paramArr = params.split("&");
				if (paramArr != null && paramArr.length > 0) {
					for (String str : paramArr) {
						if (str.indexOf("=") > -1 && str.split("=").length == 2) {
							parameterMap.put(str.split("=")[0], str.split("=")[1]);
						}
					}
				}
			} else {
				actionUrl = returnLogin;
			}
		} catch (UnsupportedEncodingException e) {
			actionUrl = "/";
		}
	}
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
</head>
<body>
	<noscript>
		<div class="noscript"><spring:message code="message.noscript" /></div>
	</noscript>
	<form id="loginReturnForm" method="post" action="<%=actionUrl%>">
		<%
		if(parameterMap != null){		
			for ( String paramKey : parameterMap.keySet() ) {
			%><input type="hidden" name="<%=paramKey %>" value="<%=parameterMap.get(paramKey)%>"><%
			}
		}
	  	%>		
	</form>
	<script>
		window.onload = function(){
			document.getElementById("loginReturnForm").submit();
		}
	</script>
</body>
</html>