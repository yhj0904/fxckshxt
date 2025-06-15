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
 * @ 
 */
%>
<div class="wrap">
	<% /* HEADER START */%>
	<c:import url="/WEB-INF/jsp/template/{TEMPLATE_CODE}/header.jsp"></c:import>
	<% /* //HEADER END */%>
		
	<% /* CONTENT START */%>
	<section class="login" id="site_main">
		<div class="login_box">
			<div class="login_title">
				<img src="/images/site/login_title.png" alt="LOGIN">
			</div>
			<div class="login_input">
				<label for="loginId"><span class="none">ID</span></label>
				<input type="text" id="inputId" onkeydown="if(event.keyCode==13){fn_loginAction();}" placeholder="<spring:message code="content.login.placeholder.id"/>" autocomplete="off">
		   	</div>
			<div class="login_input">
				<label for="loginPw"><span class="none">PW</span></label>
				<input type="password" id="inputPw" onkeydown="if(event.keyCode==13){fn_loginAction();}" placeholder="<spring:message code="content.login.placeholder.pw"/>" autocomplete="off">
			</div>
		   	<div class="login_btn">
				<a class="button" href="javascript:fn_loginAction();"><spring:message code="content.login.btn.login" /></a>	
			</div>			
			<div class="user_btn">
				<ul>
					<li>
						<a href="javascript:fn_joinView();"><spring:message code="text.join" /></a>
					</li>
					<li>
						<a href="javascript:fn_findUserId();"><spring:message code="content.login.btn.id" /></a> / <a href="javascript:fn_findUserPw();"><spring:message code="content.login.btn.pw" /></a>
					</li>
				</ul>
			</div>
		</div>
	</section>
	<% /* //CONTENT END */%>
	
	<% /* FOOTER START */%>
	<c:import url="/WEB-INF/jsp/template/{TEMPLATE_CODE}/footer.jsp"></c:import>
	<% /* //FOOTER END */%>	
</div>