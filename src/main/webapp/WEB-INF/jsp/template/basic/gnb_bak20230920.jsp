<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: gnb.jsp
 * @Description : GNB 영역
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div id="gnb_wrap" class="gnb_wrap">
	<nav id="gnb" class="gnb">
		<% /** 메뉴 구조를 위한 LEVEL 변수 */ %>
		<c:set var="GV_GNB_LEVEL" value="1"/>
		<ul data-level="1">
			<c:forEach var="GV_GNB_ITEM" items="${GV_GNB }" varStatus="i">
				<% /** 현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
				<c:choose>
					<c:when test="${i.index eq 0}"></c:when>
					<c:when test="${GV_GNB_ITEM.menuLvl > GV_GNB_LEVEL}">
						<c:out value='<ul data-level="${GV_GNB_ITEM.menuLvl }">' escapeXml='false'/>
					</c:when>
					<c:when test="${GV_GNB_ITEM.menuLvl < GV_GNB_LEVEL}">
						<c:out value='</li>' escapeXml="false"/>
						<c:forEach begin="${GV_GNB_ITEM.menuLvl }" end="${GV_GNB_LEVEL - 1}" step="1">
							<c:out value='</ul></li>' escapeXml="false"/>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:out value='</li>' escapeXml="false"/>
					</c:otherwise>
				</c:choose>
				<c:out value='<li>' escapeXml="false"/>
				<% /** //현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
				
				<% /** 메뉴 링크 설정 */ %>
				<c:set var="GNB_LINK" value="${GV_GNB_ITEM.menuLink }"/>
				<c:choose>
					<c:when test="${GV_GNB_ITEM.progYn eq 'Y' and GV_GNB_ITEM.progPath ne null and GV_GNB_ITEM.progPath ne ''}">
						<c:choose>
							<c:when test='${GV_GNB_ITEM.progCd eq "CONTENT"}'>
								<c:set var="GNB_LINK" value="/content/${GV_GNB_ITEM.progParam }.do"/>
							</c:when>
							<c:when test='${GV_GNB_ITEM.progCd eq "BOARD"}'>
								<c:set var="GNB_LINK" value="/board/${GV_GNB_ITEM.progParam }.do"/>							
							</c:when>
							<c:when test='${GV_GNB_ITEM.progCd eq "SCHEDULE"}'>
								<c:set var="GNB_LINK" value="/schedule/${GV_GNB_ITEM.progParam }.do"/>							
							</c:when>
							<c:otherwise>
								<c:set var="GNB_LINK" value="${GV_GNB_ITEM.progPath }"/>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${GV_GNB_ITEM.menuLink ne null and GV_GNB_ITEM.menuLink ne ''}">
						<c:choose>
							<c:when test='${GV_GNB_ITEM.useSso eq "Y" }'>
								<c:set var="GNB_LINK" value="javascript:gf_goSsoUrl('${GV_GNB_ITEM.menuLink }');"/>
							</c:when>
							<c:otherwise>
								<c:set var="GNB_LINK" value="${GV_GNB_ITEM.menuLink }"/>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="GNB_LINK" value="#none"/>
					</c:otherwise>
				</c:choose>
				<a href="<c:out value="${GNB_LINK }"/>"  <c:if test='${GV_GNB_ITEM.activeYn eq "Y" }'>class="active"</c:if>>
					<c:if test='${GV_GNB_ITEM.iconSrc ne null and GV_GNB_ITEM.iconSrc ne "" }'><img src="<c:out value="${GV_GNB_ITEM.iconSrc }"/>" alt=""/></c:if>			
					<span><c:out value="${GV_GNB_ITEM.menuNm }"/></span>
				</a>
				
				<% /** 메뉴 구조를 위한 LEVEL 변수 */ %>
				<c:set var="GV_GNB_LEVEL" value="${GV_GNB_ITEM.menuLvl}"/>
				
				<% /** 마지막인경우 모두 닫는다 */ %>
				<c:if test="${i.last}">
					<c:out value='</li>' escapeXml="false"/>
					<c:if test="${GV_GNB_ITEM.menuLvl > 1 }">
						<c:forEach begin="1" end="${GV_GNB_ITEM.menuLvl - 1}" step="1">
							<c:out value="</ul></li>" escapeXml="false"/>
						</c:forEach>
					</c:if>
				</c:if>
				<% /** //마지막인경우 모두 닫는다 */ %>				
			</c:forEach>
		</ul>
	</nav>
	<script>
		var GNB;
		//전체 메뉴 HOVER , FOCUS
		$(function() {			
			GNB = $('#gnb');			
			GNB.find('>ul>li>a').on('mouseenter', function(e) {
				e.preventDefault();
				GNB.find('>ul>li>ul[data-level="2"]:visible').parent('li').removeClass('on');
				$(this).next('ul[data-level="2"]:hidden').parent('li').addClass('on');
			}).focus(function() {
				$(this).mouseover();
			}).end().mouseleave(function() {
				GNB.find('>ul>li>ul[data-level="2"]').prev('a').parent().siblings().removeClass('on');
			}).find('li').last().focusout(function() {
				$(this).mouseleave();
			});		
		});
	</script>
</div>