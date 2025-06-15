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
 
//MOBILE CHECK
String type = request.getHeader("User-Agent").toUpperCase();
String str_os="";
if(type.indexOf("MOBILE") > -1) {
 str_os = "MOBILE";	 
} else if(type.indexOf("PHONE") > -1) {
 str_os = "PHONE";
} else if(type.indexOf("TABLET") > -1) {
 str_os = "TABLET";
} else {
 str_os = "PC"; 
}

%>
<div class="collapse navbar-collapse" id="navbarCollapse">
	
	<% /** 메뉴 구조를 위한 LEVEL 변수 */ %>
	<c:set var="GV_GNB_LEVEL" value="1"/>
	<ul class="navbar-nav mx-auto py-0" data-level="1">
		<c:forEach var="GV_GNB_ITEM" items="${GV_GNB }" varStatus="i">
				<% /** 현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
				<c:choose>
					<c:when test="${i.index eq 0}"></c:when>
					<c:when test="${GV_GNB_ITEM.menuLvl > GV_GNB_LEVEL}">
						<c:out value='<ul class="dropdown-menu m-0" data-level="${GV_GNB_ITEM.menuLvl }">' escapeXml='false'/>
					</c:when>
					<c:when test="${GV_GNB_ITEM.menuLvl < GV_GNB_LEVEL}">
						<!-- 노출되라 -->
						<c:out value='</li>' escapeXml="false"/>
						<c:forEach begin="${GV_GNB_ITEM.menuLvl }" end="${GV_GNB_LEVEL - 1}" step="1">
							<c:out value='</ul></li>' escapeXml="false"/>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:out value='</li>' escapeXml="false"/>
					</c:otherwise>
				</c:choose>
				 
				<c:choose>
					<c:when test="${GV_GNB_ITEM.childCnt > 0 && GV_GNB_ITEM.menuLvl eq 1}">
						<c:choose>
							<c:when test="${GV_GNB_ITEM.viewYn eq 'N'}">
								<c:out value='<li class="nav-item dropdown" style="display: none;">' escapeXml="false"/>
							</c:when>
							<c:otherwise>
								<c:out value='<li class="nav-item dropdown">' escapeXml="false"/>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${GV_GNB_ITEM.viewYn eq 'N'}">
								<c:out value='<li style="display: none;">' escapeXml="false"/>
							</c:when>
							<c:otherwise>
								<c:out value='<li>' escapeXml="false"/>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
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
				<c:choose>
					<c:when test="${(GV_GNB_ITEM.menuLvl eq 1) and (GV_GNB_ITEM.childCnt eq 0)}">
						<a href="<c:out value="${GNB_LINK }"/>"  class="nav-item nav-link">
							<c:if test='${GV_GNB_ITEM.iconSrc ne null and GV_GNB_ITEM.iconSrc ne "" }'><img src="<c:out value="${GV_GNB_ITEM.iconSrc }"/>" alt=""/></c:if><c:out value="${GV_GNB_ITEM.menuNm }"/>
						</a>
					</c:when>
					<c:when test="${(GV_GNB_ITEM.menuLvl eq 1) and (GV_GNB_ITEM.childCnt gt 0)}">
						<% if(!str_os.equals("PC")){ %>
							<c:set var="dropdown" value='data-bs-toggle="dropdown"'/>
						<% } %>
						<%-- <a href="<c:out value="${GNB_LINK }"/>"  class="nav-link  dropdown-toggle" data-bs-toggle="dropdown"> --%>
						<a href="<c:out value="${GNB_LINK }"/>"  class="nav-link  dropdown-toggle" <c:out value="${dropdown }" escapeXml="false" />>
							<c:if test='${GV_GNB_ITEM.iconSrc ne null and GV_GNB_ITEM.iconSrc ne "" }'><img src="<c:out value="${GV_GNB_ITEM.iconSrc }"/>" alt=""/></c:if><c:out value="${GV_GNB_ITEM.menuNm }"/>
						</a>
					</c:when>
					<c:otherwise>
						<a href="<c:out value="${GNB_LINK }"/>"  class="dropdown-item">
							<c:if test='${GV_GNB_ITEM.iconSrc ne null and GV_GNB_ITEM.iconSrc ne "" }'><img src="<c:out value="${GV_GNB_ITEM.iconSrc }"/>" alt=""/></c:if><c:out value="${GV_GNB_ITEM.menuNm }"/>
						</a>
					</c:otherwise>
				</c:choose>
				
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
	<div class="right_menu">
		<ul>
			<c:choose>
				<c:when test='${!empty LOGIN_USER }'>
					<li><a class="bg1 logout" href="/logout.do" title="<spring:message code="button.logout"/>"><i></i><span class="sound-only"><spring:message code="button.logout"/></span></a></li>
					<li><a class="bg2 mypage" href="/user/mypage.do"><i></i><span class="sound-only"><spring:message code="button.mypage"/></span></a></li>
				</c:when>
				<c:otherwise>
<%--					<li><a class="bg1 login" href="/login.do"><i></i><span class="sound-only"><spring:message code="button.login"/></span></a></li>--%>
					<%-- <li><a class="bg2 join" href="/join.do"><i></i><span class="sound-only"><spring:message code="text.join"/></span></a></li> --%>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
    <!-- <button type="button" class="btn text-primary ms-3" data-bs-toggle="modal" data-bs-target="#searchModal"><i class="fa fa-search"></i></button>
    <a href="https://htmlcodex.com/startup-company-website-template" class="btn btn-primary py-2 px-4 ms-3">Download Pro Version</a> -->
</div>