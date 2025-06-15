<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: gnb.jsp
 * @Description : 좌측 GNB 영역
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<button id="gnb_btn" class="gnb_btn" type="button">
	<span class="none"><spring:message code="button.menu" /></span>
	<span class="icon_bar"></span>
	<span class="icon_bar"></span>
	<span class="icon_bar"></span>
</button>
<div id="gnb_wrap" class="gnb_wrap open">
	<div class="info">
		<c:choose>
			<c:when test='${LOGIN_USER.userImgSrc ne null and LOGIN_USER.userImgSrc ne ""}'>
				<img src="<c:out value='${LOGIN_USER.userImgSrc }'/>" alt="Login User Img">
			</c:when>
			<c:otherwise>
				<img src="/images/common/no_user.png" alt="Login User Img">
			</c:otherwise>
		</c:choose>
		<span><c:out value="${LOGIN_USER.loginNm }"/></span>
		<ul>
			<li><a href="/sys/logout.do" class="button"><spring:message code="button.logout" /></a></li>
		</ul>
	</div>
	<nav id="gnb" class="gnb custom_scroll">
		<% /** 메뉴 구조를 위한 LEVEL 변수 */ %>
		<c:set var="GV_GNB_LEVEL" value="1"/>
		<ul data-level="1">
			<li>
				<a href="/sys.do"><span><spring:message code="text.home" /></span></a>
			</li>
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
					<c:when test="${GV_GNB_ITEM.childCnt > 0 }">
						<c:set var="GNB_LINK" value="#openMenu"/>
					</c:when>
					<c:otherwise>
						<c:set var="GNB_LINK" value="#none"/>
					</c:otherwise>
				</c:choose>
				<a href="<c:out value="${GNB_LINK }"/>"><span><c:out value="${GV_GNB_ITEM.menuNm }"/></span></a>
				
				<c:if test="${GV_GNB_ITEM.childCnt > 0 }">
					<button type="button" class="folder_btn"><span class="none">open</span></button>
				</c:if>
				
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
		<script>
			$("#gnb .folder_btn, #gnb a[href=#openMenu]").click(function(){
				if(!$(this).closest('li').children('button').hasClass("open")){
					$(this).closest('li').children('button').addClass("open")
					$(this).closest('li').children('button').find('span').html("close")
				} else {
					$(this).closest('li').children('button').removeClass("open")
					$(this).closest('li').children('button').find('span').html("open")
				}
			});
			$("#gnb_btn").click(function(){
				if(!$("#gnb_wrap").is(':animated')){					
					if(!$("#gnb_wrap").hasClass("open")){			    	
						$('#gnb_wrap').show("slide", {direction : "left"}, 'fast');
						$('#gnb_wrap').addClass("open");
						$('section.main').removeClass("full");
						$('footer.footer').removeClass("full");
					} else {
						$('#gnb_wrap').hide("slide", {direction : "left"}, 'fast');
						$('#gnb_wrap').removeClass("open");
						$('section.main').addClass("full");
						$('footer.footer').addClass("full");
					}
				}
			});
		</script>
	</nav>
</div>