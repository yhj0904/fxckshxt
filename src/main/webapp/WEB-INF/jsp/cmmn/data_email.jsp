<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<c:set var="etcFlag" value="true"/>
<c:set var="path" value=""/>
<c:set var="id" value=""/>
<c:set var="domain" value=""/>
<c:set var="etc" value=""/>

<c:if test="${param.path ne null and param.path ne ''}">
	<c:set var="path" value="${param.path }"/>
</c:if>

<c:if test="${param.value ne null and param.value ne ''}">
	<c:set var="email" value="${fn:split(param.value,'@')}" />
	<c:forEach var="emailArr" items="${email}" varStatus="i">
		<c:if test="${i.index == 0}"><c:set var="id" value="${emailArr }"/></c:if>
		<c:if test="${i.index == 1}">
			<c:set var="domain" value="${emailArr }"/>
		</c:if>
	</c:forEach>
</c:if>
<input type="text" class="id" value="<c:out value='${id }'/>" data-path="<c:out value='${path }'/>">
<span class="hipen">@</span>
<select class="domain" data-path="<c:out value='${path }'/>">
	<option value="" <c:if test='${domain eq null or domain eq "" }'><c:set var="etcFlag" value="false"/>selected</c:if>>--선택--</option>
	<option value="naver.com" <c:if test='${domain eq "naver.com" }'><c:set var="etcFlag" value="false"/>selected</c:if>>naver.com</option>
	<option value="gmail.com" <c:if test='${domain eq "gmail.com" }'><c:set var="etcFlag" value="false"/>selected</c:if>>gmail.com</option>
	<option value="nate.com" <c:if test='${domain eq "nate.com" }'><c:set var="etcFlag" value="false"/>selected</c:if>>nate.com</option>
	<option value="yahoo.co.kr" <c:if test='${domain eq "yahoo.co.kr" }'><c:set var="etcFlag" value="false"/>selected</c:if>>yahoo.co.kr</option>
	<option value="daum.net" <c:if test='${domain eq "daum.net" }'><c:set var="etcFlag" value="false"/>selected</c:if>>daum.net</option>
	<option value="etc" <c:if test='${etcFlag }'>selected</c:if>>직접입력</option>
</select>
<c:choose>
	<c:when test='${etcFlag }'>
		<input type="text" class="etc" value="<c:out value='${domain }'/>" data-path="<c:out value='${path }'/>">
	</c:when>
	<c:otherwise>
		<input type="text" class="etc" value="" data-path="<c:out value='${path }'/>" style="display:none;">
	</c:otherwise>
</c:choose>
