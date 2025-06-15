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
<div class="wrap">
	<% /* HEADER START */%>
	<c:import url="/WEB-INF/jsp/template/basic/header.jsp"></c:import>
	<% /* //HEADER END */%>
	
	<% /* CONTENT START */%>
	<section class="login" id="site_main">
		<div class="sub-bg container-fluid bg-primary py-5 bg-header">
            <div class="row py-5">
                <div class="col-12 text-center">
                    <h1 class="text-white animated zoomIn">원광대학교 일자리플러스센터는</h1>
                    <p class="sub-hd-txt text-white">전라북도내의 대학 및 지역청년의 취업지원 거점대학 역할을 합니다.</p>
                </div>
            </div>
        </div>
        <div class="sub_inner_wr">
			<c:if test='${!empty GV_MENU_INFO.sideMenu }'>
				<div class="sub_side">
					<% /* SNB START */%>
					<c:import url="/WEB-INF/jsp/cmmn/snb.jsp">
						<c:param name="GV_SNB" value="${GV_MENU_INFO.sideMenu }"></c:param>
					</c:import>
					<% /* //SNB END */%>
				</div>
			</c:if>
			<div class="sub_wrap" style="max-width: none;">
				<div class="sub_container" style="max-width: none;">
					<div class="inner">
						<%-- 현재 메뉴 노출 및 네비게이션 노출 --%>
							<c:if test='${!empty GV_MENU_INFO.presentMenu }'>
								<div class="sub_title">
									<h1><c:out value='${GV_MENU_INFO.presentMenu.menuNm }'/></h1>
									<ul id="menuNav">
										<c:forEach var="item" items="${GV_MENU_INFO.menuPath}">
											<li><a href="<c:out value='${item.menuLink }'/>" class="<c:if test="${item.menuEngNm eq 'Home' }">home</c:if>"><c:out value='${item.menuNm }'/></a></li>
										</c:forEach>
									</ul>
								</div>
							</c:if>
						<%-- 현재 메뉴 노출 및 네비게이션 노출 --%>
						<div class="sub_content">
							<div class="login_box">
								<div class="login_title">
									로그인
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
											<a href="javascript:fn_findUserId();"><spring:message code="content.login.btn.id" /></a>
										</li>
										<li>										
											<a href="javascript:fn_findUserPw();"><spring:message code="content.login.btn.pw" /></a>
										</li>
										<%-- <li>
											<a href="javascript:fn_joinView();"><spring:message code="text.join" /></a>
										</li> --%>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>		
        
	</section>
	<% /* //CONTENT END */%>
	
	<% /* FOOTER START */%>
	<c:import url="/WEB-INF/jsp/template/basic/footer.jsp"></c:import>
	<% /* //FOOTER END */%>
</div>