<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: login.jsp
 * @Description : 로그인 템플릿
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
	<link href="/css/template/system/login.css" media="all" rel="stylesheet" type="text/css">
	<% /* 공통 JS */%>
	<c:import url="/WEB-INF/jsp/cmmn/js.jsp"></c:import>
	<% /* 로그인 메시지 선언 */%>
	<script>
		var CERTIFICATION_MSG = {
			EMPTY 	: '<spring:message code="certification.alert.certKey"/>',			
			RESEND 	: '<spring:message code="certification.resend"/>',
			CERT 	: '<spring:message code="certification.button"/>',
			TIME 	: '<spring:message code="certification.error.time"/>',		
		};
	</script>
</head>
<body>
	<% /* TOP */%>
	<c:import url="/WEB-INF/jsp/cmmn/top.jsp"></c:import>
	<% /* //TOP */%>
	
	<div class="wrap">
		<% /* CONTENT START */%>
		<section class="login">
			<div id="site_main">
				<div class="login_box">
					<div class="login_title">
						<img src="/images/common/sys_logo.png" alt=""/>
						<p>LOGIN</p>
					</div>
					<div class="login_input">
						<label for="loginId"><span class="none">ID</span></label>
						<input type="text" id="inputId" onkeydown="if(event.keyCode==13){fn_loginAction();}" placeholder="<spring:message code="content.login.placeholder.id"/>" autocomplete="off">
				   	</div>
					<div class="login_input">
						<label for="loginPw"><span class="none">PW</span></label>
						<input type="password" id="inputPw" onkeydown="if(event.keyCode==13){fn_loginAction();}" placeholder="<spring:message code="content.login.placeholder.pw"/>" autocomplete="off">
					</div>
					<div class="login_save_id">
						<label for="saveId" class="check">
							<input id="saveId" type="checkbox" value="1"><i></i><spring:message code="content.login.btn.saveid" />
						</label>
					</div>
				   	<div class="login_btn">
						<ul>
							<li>
								<a class="button" href="javascript:fn_loginAction();"><spring:message code="content.login.btn.login" /></a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</section>
		<% /* //CONTENT END */%>
	</div>
	
	<% /* LOGIN FORM */%>
	<tiles:insertAttribute name="body"/>
	<% /* LOGIN FORM */%>
	
	<% /* BOTTOM */%>
	<c:import url="/WEB-INF/jsp/cmmn/bottom.jsp"></c:import>
	<% /* //BOTTOM */%>
</body>
</html>