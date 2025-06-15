<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<c:set var="path" value=""/>
<c:set var="tel1" value="02"/>
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
	<option value="02"  <c:if test='${tel1 eq "02" }'>selected</c:if>>02</option> 	
	<option value="031" <c:if test='${tel1 eq "031" }'>selected</c:if>>031</option>
	<option value="032" <c:if test='${tel1 eq "032" }'>selected</c:if>>032</option>
	<option value="033" <c:if test='${tel1 eq "033" }'>selected</c:if>>033</option>
	<option value="041" <c:if test='${tel1 eq "041" }'>selected</c:if>>041</option>
	<option value="042" <c:if test='${tel1 eq "042" }'>selected</c:if>>042</option>
	<option value="043" <c:if test='${tel1 eq "043" }'>selected</c:if>>043</option>
	<option value="051" <c:if test='${tel1 eq "051" }'>selected</c:if>>051</option>
	<option value="052" <c:if test='${tel1 eq "052" }'>selected</c:if>>052</option>
	<option value="053" <c:if test='${tel1 eq "053" }'>selected</c:if>>053</option>
	<option value="054" <c:if test='${tel1 eq "054" }'>selected</c:if>>054</option>
	<option value="055" <c:if test='${tel1 eq "055" }'>selected</c:if>>055</option>
	<option value="061" <c:if test='${tel1 eq "061" }'>selected</c:if>>061</option>
	<option value="062" <c:if test='${tel1 eq "062" }'>selected</c:if>>062</option>
	<option value="063" <c:if test='${tel1 eq "063" }'>selected</c:if>>063</option>
	<option value="064" <c:if test='${tel1 eq "064" }'>selected</c:if>>064</option>
</select>
<span class="hipen">-</span>
<input type="text" class="tel2" maxlength="4" title="전화번호 앞번호" value="<c:out value='${tel2 }'/>" data-path="<c:out value='${path }'/>">
<span class="hipen">-</span>
<input type="text" class="tel3" maxlength="4" title="전화번호 뒷번호" value="<c:out value='${tel3 }'/>" data-path="<c:out value='${path }'/>">