<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 앱사용자관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="pushAppUserVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<table class="detail_table">
		<caption>앱사용자관리 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="userId"><spring:message code="pushAppUserVO.userId"/></label></th>
			<td>
				<form:hidden path="userId"/>
				<c:out value="${pushAppUserVO.userId }"/>
				<span class="form_error" data-path="userId"><form:errors path="userId"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="deptCd"><spring:message code="pushAppUserVO.deptCd"/></label></th>
			<td>
				<form:input path="deptCd" readonly="true"/>
				<span class="form_error" data-path="deptCd"><form:errors path="deptCd"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="userNm"><spring:message code="pushAppUserVO.userNm"/></label></th>
			<td>
				<form:hidden path="userNm"/>
				<c:out value="${pushAppUserVO.userNm }"/>
				<span class="form_error" data-path="userNm"><form:errors path="userNm"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="userDv"><spring:message code="pushAppUserVO.userDv"/></label></th>
			<td>
				<form:input path="userDv" readonly="true"/>
				<span class="form_error" data-path="userDv"><form:errors path="userDv"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="deviceDv"><spring:message code="pushAppUserVO.deviceDv"/></label></th>
			<td>
				<form:select path="deviceDv">					
					<form:option value="SMS" label="SMS"/>
					<form:option value="Android" label="Android"/>
					<form:option value="iPhone" label="iPhone"/>
					<form:option value="iPad" label="iPad"/>
				</form:select>
				<span class="form_error" data-path="deviceDv"><form:errors path="deviceDv"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="deviceId"><spring:message code="pushAppUserVO.deviceId"/></label></th>
			<td>
				<form:input path="deviceId"/>
				<span class="form_error" data-path="deviceId"><form:errors path="deviceId"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="userMobile"><spring:message code="pushAppUserVO.userMobile"/></label></th>
			<td>
				<form:input path="userMobile"/>
				<span class="form_error" data-path="userMobile"><form:errors path="userMobile"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="adminDv"><spring:message code="pushAppUserVO.adminDv" /></label></th>
			<td>
				<form:radiobutton path="adminDv" value="Y" label="Y"/>
				<form:radiobutton path="adminDv" value="N" label="N"/>
				<span class="form_error" data-path="adminDv"><form:errors path="adminDv"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="note"><spring:message code="pushAppUserVO.note"/></label></th>
			<td>
				<form:input path="note"/>
				<span class="form_error" data-path="note"><form:errors path="note"/></span>
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
   	<input type="hidden" name="sId" value="<c:out value='${pushAppUserVO.userId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="pushAppUserVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validatePushAppUserVO(frm)) {
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