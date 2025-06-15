<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<c:set var="path" value=""/>
<c:set var="tel1" value="010"/>
<c:set var="tel2" value=""/>
<c:set var="tel3" value=""/>

<c:if test="${param.path ne null and param.path ne ''}">
	<c:set var="path" value="${param.path }"/>
</c:if>

<c:if test="${param.value ne null and param.value ne ''}">
	<c:set var="tel" value="${fn:split(param.value,'-')}" />
	<c:forEach var="telNum" items="${tel}" varStatus="i">
		<c:if test="${i.index == 0}"><c:set var="tel1" value="${telNum }"/></c:if>
		<c:if test="${i.index == 1}"><c:set var="tel2" value="${telNum }"/></c:if>
		<c:if test="${i.index == 2}"><c:set var="tel3" value="${telNum }"/></c:if>
	</c:forEach>
</c:if>

<select class="tel1" data-path="<c:out value='${path }'/>">
	<option value="010" <c:if test='${tel1 eq "010" }'>selected</c:if>>010</option>
	<option value="011" <c:if test='${tel1 eq "011" }'>selected</c:if>>011</option>
	<option value="016" <c:if test='${tel1 eq "016" }'>selected</c:if>>016</option>
	<option value="017" <c:if test='${tel1 eq "017" }'>selected</c:if>>017</option>
	<option value="018" <c:if test='${tel1 eq "018" }'>selected</c:if>>018</option>
	<option value="019" <c:if test='${tel1 eq "019" }'>selected</c:if>>019</option>
</select>
<span class="hipen">-</span>
<input type="text" class="tel2" maxlength="4" title="전화번호 앞번호" value="<c:out value='${tel2 }'/>" data-path="<c:out value='${path }'/>">
<span class="hipen">-</span>
<input type="text" class="tel3" maxlength="4" title="전화번호 뒷번호" value="<c:out value='${tel3 }'/>" data-path="<c:out value='${path }'/>">