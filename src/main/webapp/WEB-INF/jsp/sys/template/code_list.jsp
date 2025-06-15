<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: code_list.jsp
 * @Description : 템플릿관리 백업 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
	<form:form id="listForm" name="listForm" method="post" autocomplete="off">
		<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
		<table class="list_table">
			<colgroup>
				<col style=""/>
				<col style=""/>
				<col style=""/>
				<col style=""/>
			</colgroup>
			<thead>
				<tr>
					<th>No.</th>
					<th><spring:message code="templateMgtVO.templateCd"/></th>
					<th><spring:message code="templateMgtVO.inptDttm"/></th>
					<th><spring:message code="button.restore"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${list }" varStatus="i">
					<tr>
						<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
						<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.templateCd }"/>', <c:out value="${item.seq }"/>);" title="<spring:message code="board.detail"/>"><c:out value="${item.templateCd}"/></a></td>
						<td><c:out value="${item.inptDttm}"/></td>
						<td><a class="table_btn" href="javascript:fn_restoreView('<c:out value="${item.templateCd }"/>', <c:out value="${item.seq }"/>);"><spring:message code="button.restore"/></a></td>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>
				</c:if>
			</tbody>
	  	</table>
	  
	   	<% /** 검색조건 유지 */ %>
	   	<input type="hidden" name="templateCd" >
	   	<input type="hidden" name="seq" >
		<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	<% /** //검색조건 유지 */ %>
	   	
		   	
		<% /** 페이징 */ %>
		<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
		<% /** //페이징 */ %>
	</form:form>
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>


<script type="text/javaScript" defer="defer">

	//상세보기
	function fn_detailView(templateCd, seq) {
		document.listForm.templateCd.value = templateCd;
		document.listForm.seq.value = seq;
		document.listForm.action = GV_PRESENT_PATH + "/code/view.do";
		document.listForm.submit();
	}
	
	//복원
	function fn_restoreView(templateCd, seq) {
		var msg = confirm('<spring:message code="message.confirm.save" />');
		if(msg == true){
			fn_restoreAction(templateCd, seq);
		}
	}
	
	function fn_restoreAction(templateCd, seq){
		gf_ajax({
			url :  GV_PRESENT_PATH + "/code/modifyAction.json",
			type : "POST",
			data : {templateCd : templateCd, seq : seq},
		}).then(function(response){
			if(response){
				if(!gf_isNull(opener.parent.fn_refreshPage)){
					opener.parent.fn_refreshPage();
				}
				alert('<spring:message code="message.success" />');
				fn_closePop();
			} else {
				alert('<spring:message code="message.error" />');
			}
		});
	}
	
	function fn_closePop(){
		self.close();
	}
</script>