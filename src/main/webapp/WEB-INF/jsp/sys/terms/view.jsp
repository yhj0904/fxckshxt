<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 약관관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="termsId"/>
	
	<table class="detail_table">
		<caption>약관관리 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="termsVO.termsDvcd"/></th>
			<td><c:out value="${termsVO.termsDvcd}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.termsId"/></th>
			<td><c:out value="${termsVO.termsId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.termsNm"/></th>
			<td><c:out value="${termsVO.termsNm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.termsCont"/></th>
			<td><c:out value="${termsVO.termsCont}" escapeXml="false"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.termsDttm"/></th>
			<td><c:out value="${termsVO.termsDttm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.necessaryYn"/></th>
			<td><c:out value="${termsVO.necessaryYn}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.useYn"/></th>
			<td><c:out value="${termsVO.useYn}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.inptId"/></th>
			<td><c:out value="${termsVO.inptId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.inptIp"/></th>
			<td><c:out value="${termsVO.inptIp}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.inptDttm"/></th>
			<td><c:out value="${termsVO.inptDttm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.modiId"/></th>
			<td><c:out value="${termsVO.modiId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.modiIp"/></th>
			<td><c:out value="${termsVO.modiIp}"/></td>
		</tr>
		<tr>
			<th><spring:message code="termsVO.modiDttm"/></th>
			<td><c:out value="${termsVO.modiDttm}"/></td>
		</tr>
	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${termsVO.termsId }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
		document.detailForm.submit();
	}
	
	//수정
	function fn_modifyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/modify.do";
		document.detailForm.submit();
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}
	
</script>