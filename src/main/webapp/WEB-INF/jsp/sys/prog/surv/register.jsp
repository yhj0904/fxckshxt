<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 설문 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	<table class="detail_table">
		<caption>설문 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
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
		<%-- <tr>
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
		</tr> --%>
		<tr style="display: none;">
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
				<a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="survMgtVO" staticJavascript="false" xhtml="true" cdata="false"/>
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
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (!validateSurvMgtVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/registerAction.do";
			frm.submit();
		}
	}
	
</script>