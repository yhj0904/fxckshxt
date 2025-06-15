<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: sub.jsp
 * @Description : 서브템플릿
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
	<section class="main" id="site_main">
		<c:if test='${!empty GV_MENU_INFO.sideMenu }'>
			<div class="sub_side">
				<% /* SNB START */%>
				<c:import url="/WEB-INF/jsp/cmmn/snb.jsp">
					<c:param name="GV_SNB" value="${GV_MENU_INFO.sideMenu }"></c:param>
				</c:import>
				<% /* //SNB END */%>
			</div>
		</c:if>
		<div class="sub_wrap">
			<c:if test='${!empty GV_MENU_INFO.presentMenu }'>
				<div class="sub_title">
					<div class="container">
						<div class="inner">
							<p><c:out value='${GV_MENU_INFO.presentMenu.menuNm }'/></p>
							<ul>
								<c:forEach var="item" items="${GV_MENU_INFO.menuPath}">
									<li><a href="<c:out value='${item.menuLink }'/>"><c:out value='${item.menuNm }'/></a></li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</c:if>
			<div class="sub_content">
				<div class="container">
					<div class="inner">
						<tiles:insertAttribute name="body"/>
					</div>
				</div>
			</div>
		</div>
	</section>
	<% /* //CONTENT END */%>
	
	<% /* FOOTER START */%>
	<c:import url="/WEB-INF/jsp/template/{TEMPLATE_CODE}/footer.jsp"></c:import>
	<% /* //FOOTER END */%>	
</div>