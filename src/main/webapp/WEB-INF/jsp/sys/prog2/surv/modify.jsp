<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 설문 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form commandName="progSurvMgtVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<table class="detail_table">
		<caption>설문 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="survId"><spring:message code="survMgtVO.survId"/></label></th>
			<td>
				<c:out value="${progSurvMgtVO.survId }"/>
				<form:hidden path="survId"/>
				<span class="form_error" data-path="survId"><form:errors path="survId"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="survTitle"><spring:message code="survMgtVO.survTitle"/></label></th>
			<td>
				<form:input path="survTitle"/>
				<span class="form_error" data-path="survTitle"><form:errors path="survTitle"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="survMemo"><spring:message code="survMgtVO.survMemo"/></label></th>
			<td>
				<form:textarea path="survMemo"/>
				<span class="form_error" data-path="survMemo"><form:errors path="survMemo"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="survDate1"><spring:message code="survMgtVO.survDate1"/></label></th>
			<td>
				<form:input path="survDate1" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="survDate1"><form:errors path="survDate1"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="survDate2"><spring:message code="survMgtVO.survDate2"/></label></th>
			<td>
				<form:input path="survDate2" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="survDate2"><form:errors path="survDate2"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="useYn"><spring:message code="popupVO.useYn" /></label></th>
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
   	<input type="hidden" name="sId" value="<c:out value='${progSurvMgtVO.survId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="progSurvMgtVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	
	$(function(){
		//웹에디터 사용시
		var editorArray = [{ id : "survMemo" , useUpload : "Y" }];
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
		if (!validateProgSurvMgtVO(frm)) {
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