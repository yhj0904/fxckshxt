<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: back_list.jsp
 * @Description : 컨텐츠관리 백업 목록 화면
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
				<col style=""/> <!-- NO -->
				<col style=""/> <!-- 아이디 -->
				<col style=""/> <!-- 제목 -->
				<col style=""/> <!-- 등록시간 -->
			</colgroup>
			<thead>
				<tr>
					<th><spring:message code="board.no"/></th>
					<th><spring:message code="contentVO.contId"/></th>
					<th><spring:message code="contentVO.contNm"/></th>
					<th><spring:message code="contentVO.backDttm"/></th>
					<th><spring:message code="button.restore"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${list }" varStatus="i">
					<tr>
						<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
						<td><a class="link" href="javascript:fn_detailView('<c:out value="${item.contId }"/>', <c:out value="${item.seq }"/>);" title="<spring:message code="board.detail"/>"><c:out value="${item.contId}"/></a></td>
						<td><c:out value="${item.contNm}"/></td>
						<td><c:out value="${item.backDttm}"/></td>
						<td><a class="table_btn" href="javascript:fn_restoreView('<c:out value="${item.contId }"/>', <c:out value="${item.seq }"/>);"><spring:message code="button.restore"/></a></td>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>
				</c:if>
			</tbody>
	  	</table>
	  
	   	<% /** 검색조건 유지 */ %>
	   	<input type="hidden" name="contId" >
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
	function fn_detailView(contId, seq) {
		document.listForm.contId.value = contId;
		document.listForm.seq.value = seq;
		document.listForm.action = GV_PRESENT_PATH + "/back/view.do";
		document.listForm.submit();
	}
	
	//복원
	function fn_restoreView(contId, seq) {
		var msg = confirm('<spring:message code="message.confirm.save" />');
		if(msg == true){
			fn_restoreAction(contId, seq);
		}
	}
	
	function fn_restoreAction(contId, seq){
		gf_ajax({
			url :  GV_PRESENT_PATH + "/back/modifyAction.json",
			type : "POST",
			data : {contId : contId, seq : seq},
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