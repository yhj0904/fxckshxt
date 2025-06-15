<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: empty.jsp
 * @Description : 빈템플릿
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
	<section class="main" id="site_main">
        <!-- <div class="container-fluid bg-primary py-5 bg-header" style="margin-bottom: 90px;"> -->
        <div class="sub-bg container-fluid bg-primary py-5 bg-header">
            <div class="row py-5">
                <div class="col-12 text-center">
                    <h1 class="text-white animated zoomIn">원광대학교 일자리플러스센터는</h1>
                    <p class="sub-hd-txt text-white">전라북도내의 대학 및 지역청년의 취업지원 거점대학 역할을 합니다.</p>
                </div>
            </div>
        </div>
		<div class="sub_inner_wr">
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
							<tiles:insertAttribute name="body"/>
						</div>
					</div>
				</div>
			</div>
		</div>		
	</section>
	<% /* //CONTENT END */%>
	
	<!-- Template Javascript -->
	<script src="/js/common/main.js"></script>
	
	<% /* FOOTER START */%>
	<c:import url="/WEB-INF/jsp/template/basic/footer.jsp"></c:import>
	<% /* //FOOTER END */%>	
</div>