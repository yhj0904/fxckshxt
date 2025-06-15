<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<table class="detail_table">
	<colgroup>
  			<col width="150"/>
  			<col width="?"/>
  		</colgroup>
	<tr>
		<th><label for="title"><spring:message code="bbsVO.title" /></label></th>
		<td><form:input path="title" /> <span class="form_error" data-path="title"><form:errors path="title" /></span></td>
	</tr>
	<tr>
		<th><label for="contents"><spring:message code="bbsVO.contents" /></label></th>
		<td><form:textarea path="contents"/> <span class="form_error" data-path="contents"><form:errors path="contents" /></span></td>
	</tr>
	<c:choose>
		<c:when test='${!empty LOGIN_USER }'>
			<tr>
				<th><label for="writer"><spring:message code="bbsVO.writer" /></label></th>
				<td>
					<c:out value="${LOGIN_USER.loginNm }"></c:out>
					<form:hidden path="writer"/> <span class="form_error" data-path="writer"><form:errors path="writer" /></span>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<th><label for="writer"><spring:message code="bbsVO.writer" /></label></th>
				<td><form:input path="writer" /> <span class="form_error" data-path="writer"><form:errors path="writer" /></span></td>
			</tr>
			<tr>
				<th><label for="pw"><spring:message code="bbsVO.pw" /></label></th>
				<td><form:password path="pw" /> <span class="form_error" data-path="pw"><form:errors path="pw" /></span></td>
			</tr>
		</c:otherwise>
	</c:choose>
	<c:if test='${bbsMgtVO.fileYn eq "Y"}'>
		<tr>
			<th><label><spring:message code="text.uploadfile" /></label></th>
			<td>
				<input type="file" name="uploadFiles" id="uploadFiles" title=""/>
				<div id="uploadList" class="upload_list"></div>
			</td>
		</tr>
	</c:if>
	<c:if test='${bbsMgtVO.supplYn eq "Y" }'>
		<c:if test='${bbsMgtVO.suppl01Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo01"><c:out value="${bbsMgtVO.suppl01Title}" /></label></th>
				<td><form:input path="bbsInfo01" /> <span class="form_error" data-path="bbsInfo01"><form:errors path="bbsInfo01" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl02Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo02"><c:out value="${bbsMgtVO.suppl02Title}" /></label></th>
				<td><form:input path="bbsInfo02" /> <span class="form_error" data-path="bbsInfo02"><form:errors path="bbsInfo02" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl03Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo03"><c:out value="${bbsMgtVO.suppl03Title}" /></label></th>
				<td><form:input path="bbsInfo03" /> <span class="form_error" data-path="bbsInfo03"><form:errors path="bbsInfo03" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl04Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo04"><c:out value="${bbsMgtVO.suppl04Title}" /></label></th>
				<td><form:input path="bbsInfo04" /> <span class="form_error" data-path="bbsInfo04"><form:errors path="bbsInfo04" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl05Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo05"><c:out value="${bbsMgtVO.suppl05Title}" /></label></th>
				<td><form:input path="bbsInfo05" /> <span class="form_error" data-path="bbsInfo05"><form:errors path="bbsInfo05" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl06Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo06"><c:out value="${bbsMgtVO.suppl06Title}" /></label></th>
				<td><form:input path="bbsInfo06" /> <span class="form_error" data-path="bbsInfo06"><form:errors path="bbsInfo06" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl07Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo07"><c:out value="${bbsMgtVO.suppl07Title}" /></label></th>
				<td><form:input path="bbsInfo07" /> <span class="form_error" data-path="bbsInfo07"><form:errors path="bbsInfo07" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl08Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo08"><c:out value="${bbsMgtVO.suppl08Title}" /></label></th>
				<td><form:input path="bbsInfo08" /> <span class="form_error" data-path="bbsInfo08"><form:errors path="bbsInfo08" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl09Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo09"><c:out value="${bbsMgtVO.suppl09Title}" /></label></th>
				<td><form:input path="bbsInfo09" /> <span class="form_error" data-path="bbsInfo09"><form:errors path="bbsInfo09" /></span></td>
			</tr>
		</c:if>
		<c:if test='${bbsMgtVO.suppl10Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo10"><c:out value="${bbsMgtVO.suppl10Title}" /></label></th>
				<td><form:input path="bbsInfo10" /> <span class="form_error" data-path="bbsInfo10"><form:errors path="bbsInfo10" /></span></td>
			</tr>
		</c:if>
	</c:if>
</table>
  	
   	
<div class="btn_wrap">
	<ul>
		<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a></li>
		<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>