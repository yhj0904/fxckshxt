<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 사용자 아이피 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="userIpVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<table class="detail_table">
		<caption>사용자 아이피 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="userId"><spring:message code="userIpVO.userId" /></label></th>
			<td>
				<form:input path="userId"/>
				<button type="button" class="search_btn" onclick="fn_getUserList();"><spring:message code="button.search"/></button>
       			<span class="form_error" data-path="userId"><form:errors path="userId"/></span>
			</td>
		</tr>
		<tr>
			<th><label for="ip"><spring:message code="userIpVO.ip" /></label></th>
			<td>
				<form:input path="ip"/>
       			<span class="form_error" data-path="ip"><form:errors path="ip"/></span>
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
<validator:javascript formName="userIpVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" defer="defer">	
	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (!validateUserIpVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/registerAction.do";
			frm.submit();
		}
	}
	
	//사용자 목록을 조회한다.
	function fn_getUserList() {
		var id = "userList";
		var url = "/sys/user/userList.json";
		var param = {};
		var columns = [
			{key:"userId", name:"아이디"},
			{key:"userNm", name:"이름"}
		];
		var callback = function(data){
			var userId = data.userId;
			document.detailForm.userId.value = userId;
		};
		gf_openSearchPop(id, url, param, columns, callback);
	};
	
</script>