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
 * @Class Name 	: botton_banner.jsp
 * @Description : 하단배너 IMPORT
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
	List<BannerVO> btBanner = bannerService.selectBannerByCode("BT_BANNER");
	pageContext.setAttribute("btBanner", btBanner);
	
%>
<div class="owl-carousel vendor-carousel <c:if test="${empty btBanner }">d-none</c:if>">
	<c:forEach var="banner" items="${btBanner }" varStatus="status">
		<a href="<c:out value='${banner.bannerLink }'/>" target="_blank" class="bt_item" title="<c:out value='${banner.bannerNm }'/>">
			<img src="<c:out value='${banner.viewFile.viewUrl }'/>" alt="<c:out value='${banner.imgExplain }'/>">
		</a>
	</c:forEach>
</div>
<%-- 내용 없을 경우 --%>
<c:if test="${empty btBanner }">
	<div style="text-align: center;">등록된 내용이 없습니다.</div>
</c:if>
<%-- //내용 없을 경우 --%>
