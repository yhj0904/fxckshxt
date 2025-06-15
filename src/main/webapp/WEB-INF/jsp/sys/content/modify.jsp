<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 컨텐츠관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form commandName="contentVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<table class="detail_table">
		<caption>컨텐츠관리 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="contId"><spring:message code="contentVO.contId" /></label></th>
			<td>
				<c:out value="${contentVO.contId }"/>
				<form:hidden path="contId"/>
				<span class="form_error" data-path="contId"><form:errors path="contId" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="contNm"><spring:message code="contentVO.contNm" /></label></th>
			<td>
				<form:input path="contNm" />
				<span class="form_error" data-path="contNm"><form:errors path="contNm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="contCont"><spring:message code="contentVO.contCont" /></label></th>
			<td>
				<form:textarea path="contCont" />
				<span class="form_error" data-path="contCont"><form:errors path="contCont" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="contEngNm"><spring:message code="contentVO.contEngNm" /></label></th>
			<td>
				<form:input path="contEngNm" />
				<span class="form_error" data-path="contEngNm"><form:errors path="contEngNm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="contEngCont"><spring:message code="contentVO.contEngCont" /></label></th>
			<td>
				<form:textarea path="contEngCont" />
				<span class="form_error" data-path="contEngCont"><form:errors path="contEngCont" /></span>
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
   	<input type="hidden" name="sId" value="<c:out value='${contentVO.contId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="contentVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	
	$(function(){
		//웹에디터 사용시
		var editorArray = [{ id : "contCont" , useUpload : "Y" },{ id : "contEngCont" , useUpload : "Y" }];
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
		if (!validateContentVO(frm)) {
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