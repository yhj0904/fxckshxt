<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
/**
 * @Class Name 	: result_success.jsp
 * @Description : 데이터 처리 성공 결과 처리 페이지 (결과메시지 출력)
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<%	
	//출력 메시지
	String resultMsg = "";
	if(request.getAttribute("resultMsg") != null && !"".equals(request.getAttribute("resultMsg"))){
		resultMsg = (String) request.getAttribute("resultMsg");
	}
	
	//리다이렉트 URL
	String redirectUrl = "/";
	if(request.getAttribute("redirectUrl") != null && !"".equals(request.getAttribute("redirectUrl"))){
		redirectUrl = (String) request.getAttribute("redirectUrl");
	}
	
	//리다이렉트 METHOD
	String redirectMethod = "POST";
	if(request.getAttribute("redirectMethod") != null && !"".equals(request.getAttribute("redirectMethod"))){
		redirectMethod = (String) request.getAttribute("redirectMethod");
		//POST 또는 GET이 아닌경우
		if(!redirectMethod.toUpperCase().equals("POST") && !redirectMethod.toUpperCase().equals("GET")){
			redirectMethod = "POST";
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
	<% /* 공통 JS */%>
	<c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>		
</head>
<body>
	<noscript>
		<div class="noscript"><spring:message code="message.noscript" /></div>
	</noscript>
	<script>
		var resultMsg = "<%=resultMsg%>";
	
		window.onload = function(){		
			if(resultMsg == null || resultMsg == "" || resultMsg == "undefined"){
				resultMsg = "<spring:message code='message.success' />";
			}
			alert(resultMsg);
			document.getElementById("resultForm").submit();
		}
		
	</script>
	<form id="resultForm" name="resultForm" method="post" action="<%=redirectUrl %>">
		<c:if test="${sId ne null }">
			<input type="hidden" name="sId" value="<c:out value='${sId }'/>">
		</c:if>
		<c:if test="${search ne null and !empty search}">
  			<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	</c:if>
	  	<%
		//추가 파라미터가 있는경우 PARAMETER 추가 (model)
		if(request.getAttribute("resultParam") != null){
				
			@SuppressWarnings("unchecked")
			Map<String, Object> resultParam = (HashMap<String, Object>) request.getAttribute("resultParam");
			
			for ( String resultParamkey : resultParam.keySet() ) {
			%>
			<input type="hidden" name="<%=resultParamkey %>" value="<%=resultParam.get(resultParamkey)%>">
			<%
			}
		}
	  	%>
	</form>
</body>
</html>