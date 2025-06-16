<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 일정 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="schVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">

	<form:hidden path="schCd" value="${schMgtVO.code }"/>

	<table class="detail_table">
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<c:if test='${schMgtVO.cateYn eq "Y" and !empty schMgtVO.categoryList }'>
			<tr>
				<th><label for="category"><spring:message code="schVO.category" /></label></th>
				<td>
					<form:select path="category">
						<form:option value="" label="--선택--" />
						<c:forEach var="cate" items="${schMgtVO.categoryList }">
							<form:option value="${cate}" label="${cate}" />
						</c:forEach>
					</form:select>
					<span class="form_error" data-path="category"><form:errors path="category" /></span>
				</td>
			</tr>
		</c:if>
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
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
	<input type="hidden" name="sDate" value="<c:out value='${sDate }'/>">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="schVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	
	
	$(function(){
		
		//에디터사용시
		var editorArray = [{ id : "contents" }];
		gf_initCkEditor(editorArray);
		
		//첨부파일 사용시
		var fileYn = '<c:out value="${schMgtVO.fileYn}"/>';
		if(fileYn == "Y"){
			var multiOption = {
								listId : "uploadList",
								inputId : "uploadFiles",
								inputName : "uploadFiles",
								uploadCount : "<c:out value='${schMgtVO.fileCnt}'/>",
								uploadFileSize : "<c:out value='${schMgtVO.fileSize}'/>",
								uploadFileExt : "<c:out value='${schMgtVO.fileExt}'/>"
							};
			gf_initMultiSelector(multiOption);
		}
	});
	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (!validateSchVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/registerAction.do";
			frm.submit();
		}
	}
	
</script>