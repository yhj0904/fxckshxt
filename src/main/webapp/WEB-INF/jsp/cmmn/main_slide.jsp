<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="kr.co.nanwe.banner.service.BannerService" %>
<%@ page import="kr.co.nanwe.banner.service.BannerVO" %>
<%@ page import="kr.co.nanwe.file.service.ViewFileVO" %>
<%
/**
 * @Class Name 	: main_slide.jsp
 * @Description : 메인슬라이드 IMPORT
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.09.26		신한나			최초생성
 */
%>
<%
	BannerService bannerService = (BannerService) webApplicationContext.getBean("bannerService");
	
	//메인배너
	List<BannerVO> mainBanner = bannerService.selectBannerByCode("MAIN_SLIDE");
	pageContext.setAttribute("mainBanner", mainBanner);
	
%>
<c:forEach var="banner" items="${mainBanner }" varStatus="status">
	<div class="carousel-item <c:if test="${status.index eq 0 }">active</c:if>" >
		<img class="w-100" src="<c:out value='${banner.viewFile.viewUrl }'/>" alt="<c:out value='${banner.imgExplain }'/>">
		<div class="carousel-caption d-flex flex-column align-items-center justify-content-center">
			<div class="p-3 slide_txt_bg" style="max-width: 900px;">
				<c:out value="${banner.bannerCont}" escapeXml="false"/>
			</div>
		</div>
	</div>
</c:forEach>

<%-- 내용 없을 경우 --%>
<c:if test="${empty mainBanner }">
	<div class="carousel-item">
		<img class="w-100" src="<c:out value='${banner.viewFile.viewUrl }'/>" alt="<c:out value='${banner.imgExplain }'/>">
		<div class="carousel-caption d-flex flex-column align-items-center justify-content-center">
			<div class="p-3 slide_txt_bg" style="max-width: 900px;">
				<h1 class="text-uppercase mb-3 animated slideInDown">등록된 내용이 없습니다.</h1>
				<!-- <a href="quote.html" class="btn btn-primary py-md-3 px-md-5 me-3 animated slideInLeft">Free Quote</a>
				<a href="" class="btn btn-outline-light py-md-3 px-md-5 animated slideInRight">Contact Us</a> -->
			</div>
		</div>
	</div>
</c:if>
<%-- //내용 없을 경우 --%>
