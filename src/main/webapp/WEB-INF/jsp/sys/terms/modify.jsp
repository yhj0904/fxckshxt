<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 약관관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form commandName="termsVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<form:hidden path="termsId"/>
	<table class="detail_table">
		<caption>약관관리 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="termsDvcd"><spring:message code="termsVO.termsDvcd" /></label></th>
			<td>
				<form:input path="termsDvcd" />
				<span class="form_error" data-path="termsDvcd"><form:errors path="termsDvcd" /></span>
			</td>
		</tr>
   		<tr>
			<th><label for="termsNm"><spring:message code="termsVO.termsNm" /></label></th>
			<td>
				<form:input path="termsNm" />
				<span class="form_error" data-path="termsNm"><form:errors path="termsNm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="termsCont"><spring:message code="termsVO.termsCont" /></label></th>
			<td>
				<form:textarea path="termsCont" />
				<span class="form_error" data-path="termsCont"><form:errors path="termsCont" /></span>
			</td>
		</tr>
   		<tr>
			<th><label for="termsDttm"><spring:message code="termsVO.termsDttm" /></label></th>
			<td>
				<form:input path="termsDttm" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="termsDttm"><form:errors path="termsDttm" /></span>
			</td>
		</tr>
   		<tr>
			<th><label for="termsOrd"><spring:message code="termsVO.termsOrd" /></label></th>
			<td>
				<form:input path="termsOrd"/>
				<span class="form_error" data-path="termsOrd"><form:errors path="termsOrd" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="necessaryYn"><spring:message code="termsVO.necessaryYn" /></label></th>
			<td>
				<form:radiobutton path="necessaryYn" value="Y" label="Y"/>
				<form:radiobutton path="necessaryYn" value="N" label="N"/>
				<span class="form_error" data-path="necessaryYn"><form:errors path="necessaryYn" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="useYn"><spring:message code="termsVO.useYn" /></label></th>
			<td>
				<form:radiobutton path="useYn" value="Y" label="Y"/>
				<form:radiobutton path="useYn" value="N" label="N"/>
				<span class="form_error" data-path="useYn"><form:errors path="useYn" /></span>
			</td>
		</tr>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${termsVO.termsId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="termsVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	
	var termsCont;
	
	$(function(){
		//웹에디터 사용시
		var editorArray = [{ id : "termsCont" }];
		gf_initCkEditor(editorArray);
	});

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validateTermsVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/modifyAction.do";
			frm.submit();
		}
		
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}

	//취소
	function fn_detailView() {
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
</script>