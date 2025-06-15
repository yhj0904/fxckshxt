<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ taglib prefix="double-submit" uri="http://www.egovframe.go.kr/tags/double-submit/jsp" %>
<%@ page import="kr.co.nanwe.cmmn.util.DateUtil" %>
<%@ page import="kr.co.nanwe.bbs.service.BbsService" %>
<%@ page import="kr.co.nanwe.bbs.service.BbsVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : wk100 게시판 등록화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.24		신한나			최초생성
 */ 
%>
<table class="detail_table">
	<colgroup>
 		<col width="150"/>
 		<col width="?"/>
 	</colgroup>
	<c:if test='${bbsMgtVO.cateYn eq "Y" and !empty bbsMgtVO.categoryList }'>
		<tr>
			<th><label for="category"><spring:message code="bbsVO.category" /></label></th>
			<td>
				<form:select path="category">
					<form:option value="" label="--선택--" />
					<c:forEach var="cate" items="${bbsMgtVO.categoryList }">
						<form:option value="${cate}" label="${cate}" />
					</c:forEach>
				</form:select>
				<span class="form_error" data-path="category"><form:errors path="category" /></span>
			</td>
		</tr>
	</c:if>
	<tr>
		<th><label for="title"><spring:message code="bbsVO.title" /></label></th>
		<td><form:input path="title" cssClass="w_full"/> <span class="form_error" data-path="title"><form:errors path="title" /></span></td>
	</tr>
	<tr>
		<th><label for="contents"><spring:message code="bbsVO.contents" /></label></th>
		<td><form:textarea path="contents"/> <span class="form_error" data-path="contents"><form:errors path="contents" /></span></td>
	</tr>
	<c:if test='${bbsMgtVO.supplYn eq "Y" }'>
	
		<c:if test='${bbsMgtVO.suppl01Yn eq "Y" }'>
			<tr>
				<th><label for="bbsInfo01"><c:out value="${bbsMgtVO.suppl01Title}" /></label></th>
				<td>
					<form:select path="bbsInfo01" id="comboAll" >
						<form:option value="" label="기업 카테고리를 선택해주세요." />
						<c:forEach var="cate" items="${companyCode.list }">
							<form:option value="${cate.cd }" label="${cate.cdNm }" />
						</c:forEach>
					</form:select><span class="form_error" data-path="bbsInfo01"><form:errors path="bbsInfo01" /></span>
				</td>
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
	<c:choose>
		<c:when test='${!empty LOGIN_USER }'>
			<tr>
				<th><label for="writer"><spring:message code="bbsVO.writer" /></label></th>
				<td>
					<c:out value="${LOGIN_USER.loginNm }"></c:out>
					<form:hidden path="writer" /> <span class="form_error" data-path="writer"><form:errors path="writer" /></span>
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
	<c:if test='${bbsMgtVO.noticeYn eq "Y" and bbsMgtVO.adminUser}'>
		<tr>
			<th><label for="notice"><spring:message code="bbsVO.notice" /></label></th>
			<td>
				<form:radiobutton path="notice" value="Y" label="Y"/>
				<form:radiobutton path="notice" value="N" label="N"/>
				<span class="form_error" data-path="notice"><form:errors path="notice" /></span>
			</td>
		</tr>
	</c:if>
	<c:if test='${bbsMgtVO.secretYn eq "Y"}'>
		<tr>
			<th><label for="secret"><spring:message code="bbsVO.secret" /></label></th>
			<td>
				<form:radiobutton path="secret" value="Y" label="Y"/>
				<form:radiobutton path="secret" value="N" label="N"/>
				<span class="form_error" data-path="secret"><form:errors path="secret" /></span>
			</td>
		</tr>
	</c:if>
	<c:if test='${bbsMgtVO.fileYn eq "Y"}'>
		<tr>
			<th><label><spring:message code="text.uploadfile" /></label></th>
			<td>
				<input type="file" id="uploadFiles" title=""/>
				<div id="uploadList" class="upload_list"></div>
			</td>
		</tr>
	</c:if>
</table>
   	
<div class="btn_wrap">
	<ul>
		<li><a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a></li>
		<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>