<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 앱사용자관리 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	<table class="detail_table">
		<caption>앱사용자관리 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="userId"><spring:message code="pushAppUserVO.userId"/></label></th>
			<td>
				<form:input path="userId" readonly="true"/>
				<button type="button" class="search_btn" onclick="fn_searchUser();">검색</button>
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
				<form:input path="userNm" readonly="true"/>
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
<validator:javascript formName="pushAppUserVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (!validatePushAppUserVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/registerAction.do";
			frm.submit();
		}
	}
	
	//사용자 조회
	function fn_searchUser(){
		var _width = '800';
	    var _height = '600';
	    var _left = Math.ceil(( window.screen.width - _width )/2);
	    var _top = Math.ceil(( window.screen.width - _height )/2); 		 
	    window.open('/external/search/userList.do?searchType=one', 'user-pop', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
	}
	
	function fn_getUserInfo(data){
		//등록된 사용자인지 체크
		if(data != null){
			gf_ajax({
				url : "/sys/push/user/isIdExist.json",
				type : "POST",
				data : { userId : data.userId },
			}).then(function(response){
				var isExist = response;
				if(!isExist){
					document.detailForm.userDv.value = data.userDvcd;
					document.detailForm.userId.value = data.userId;
					document.detailForm.userNm.value = data.userNm;
					document.detailForm.deptCd.value = data.deptCd;
					document.detailForm.userMobile.value = data.mbphNo;
				} else {
					alert('<spring:message code="message.alert.existdata" />');
				}
			});
		}
	}
	
</script>