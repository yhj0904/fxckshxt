<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 일정 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<table class="detail_table">
	<colgroup>
  			<col width="150"/>
  			<col width="?"/>
  		</colgroup>
	<tr>
		<th><label for="title"><spring:message code="schVO.title" /></label></th>
		<td><form:input path="title" /> <span class="form_error" data-path="title"><form:errors path="title" /></span></td>
	</tr>
	<tr>
		<th><label for="contents"><spring:message code="schVO.contents" /></label></th>
		<td><form:textarea path="contents"/> <span class="form_error" data-path="contents"><form:errors path="contents" /></span></td>
	</tr>
	<tr>
		<th><label for="startDttm"><spring:message code="schVO.startDttm" /></label></th>
		<td>
			<form:input path="startDttm" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>
			<span class="form_error" data-path="startDttm"><form:errors path="startDttm" /></span>
		</td>
	</tr>
	<tr>
		<th><label for="endDttm"><spring:message code="schVO.endDttm" /></label></th>
		<td>
			<form:input path="endDttm" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>
			<span class="form_error" data-path="endDttm"><form:errors path="endDttm" /></span>
		</td>
	</tr>
	<c:if test='${schMgtVO.supplYn eq "Y" }'>
		<c:if test='${schMgtVO.suppl01Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo01"><c:out value="${schMgtVO.suppl01Title}" /></label></th>
				<td><form:input path="schInfo01" /> <span class="form_error" data-path="schInfo01"><form:errors path="schInfo01" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl02Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo02"><c:out value="${schMgtVO.suppl02Title}" /></label></th>
				<td><form:input path="schInfo02" /> <span class="form_error" data-path="schInfo02"><form:errors path="schInfo02" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl03Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo03"><c:out value="${schMgtVO.suppl03Title}" /></label></th>
				<td><form:input path="schInfo03" /> <span class="form_error" data-path="schInfo03"><form:errors path="schInfo03" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl04Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo04"><c:out value="${schMgtVO.suppl04Title}" /></label></th>
				<td><form:input path="schInfo04" /> <span class="form_error" data-path="schInfo04"><form:errors path="schInfo04" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl05Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo05"><c:out value="${schMgtVO.suppl05Title}" /></label></th>
				<td><form:input path="schInfo05" /> <span class="form_error" data-path="schInfo05"><form:errors path="schInfo05" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl06Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo06"><c:out value="${schMgtVO.suppl06Title}" /></label></th>
				<td><form:input path="schInfo06" /> <span class="form_error" data-path="schInfo06"><form:errors path="schInfo06" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl07Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo07"><c:out value="${schMgtVO.suppl07Title}" /></label></th>
				<td><form:input path="schInfo07" /> <span class="form_error" data-path="schInfo07"><form:errors path="schInfo07" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl08Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo08"><c:out value="${schMgtVO.suppl08Title}" /></label></th>
				<td><form:input path="schInfo08" /> <span class="form_error" data-path="schInfo08"><form:errors path="schInfo08" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl09Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo09"><c:out value="${schMgtVO.suppl09Title}" /></label></th>
				<td><form:input path="schInfo09" /> <span class="form_error" data-path="schInfo09"><form:errors path="schInfo09" /></span></td>
			</tr>
		</c:if>
		<c:if test='${schMgtVO.suppl10Yn eq "Y" }'>
			<tr>
				<th><label for="schInfo10"><c:out value="${schMgtVO.suppl10Title}" /></label></th>
				<td><form:input path="schInfo10" /> <span class="form_error" data-path="schInfo10"><form:errors path="schInfo10" /></span></td>
			</tr>
		</c:if>
	</c:if>
	<c:choose>
		<c:when test='${!empty LOGIN_USER }'>
			<tr>
				<th><label for="writer"><spring:message code="schVO.writer" /></label></th>
				<td>
					<c:out value="${LOGIN_USER.loginNm }"></c:out>
					<form:hidden path="writer"/> <span class="form_error" data-path="writer"><form:errors path="writer" /></span>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<th><label for="writer"><spring:message code="schVO.writer" /></label></th>
				<td><form:input path="writer" /> <span class="form_error" data-path="writer"><form:errors path="writer" /></span></td>
			</tr>
			<tr>
				<th><label for="pw"><spring:message code="schVO.pw" /></label></th>
				<td><form:password path="pw" /> <span class="form_error" data-path="pw"><form:errors path="pw" /></span></td>
			</tr>
		</c:otherwise>
	</c:choose>
	<c:if test='${schMgtVO.fileYn eq "Y"}'>
		<tr>
			<th><label><spring:message code="text.uploadfile" /></label></th>
			<td>
				<input type="file" id="uploadFiles" title=""/>
				<c:if test="${!empty schVO.viewFiles }">
					<ul class="file_list">
						<c:forEach var="viewFile" items="${schVO.viewFiles }" varStatus="vi">
							<li>
								<input type="hidden" id="viewFile_<c:out value="${vi.index }"/>" name="viewFiles[<c:out value="${vi.index }"/>].fno" class="viewFiles" value="0" data-val="<c:out value="${viewFile.fno }"/>"/>
								<a href="javascript:gf_download('<c:out value="${viewFile.oname }"/>','<c:out value="${viewFile.fpath }"/>','<c:out value="${viewFile.fname }"/>', <c:out value="${viewFile.fno }"/>);" title="<spring:message code="button.download" />">
									<c:out value="${viewFile.oname }"/>
								</a>
								<button type="button" data-id="viewFile_<c:out value="${vi.index }"/>" value="0" onclick="gf_removeUpload(this);">Delete</button>
							</li>
						</c:forEach>
					</ul>
				</c:if>
				<div id="uploadList" class="upload_list"></div>
			</td>
		</tr>
	</c:if>
</table>
  	
<div class="btn_wrap">
	<ul>
		<li><a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a></li>
		<li><a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a></li>
		<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>