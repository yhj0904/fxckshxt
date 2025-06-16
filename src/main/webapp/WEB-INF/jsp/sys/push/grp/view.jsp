<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 그룹관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="grpCd"/>
	<div class="grid_wrap">
		<div class="col50">
			<div class="col_title">
				<p>그룹정보</p>
			</div>
			<div class="col_inner">
				<table class="detail_table">
					<caption>그룹관리 상세조회</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><spring:message code="pushGrpMstVO.grpCd"/></th>
						<td><c:out value="${pushGrpMstVO.grpCd}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.grpCreUser"/></th>
						<td><c:out value="${pushGrpMstVO.grpCreUser}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.grpNm"/></th>
						<td><c:out value="${pushGrpMstVO.grpNm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.grpPer"/></th>
						<td><c:out value="${pushGrpMstVO.grpPer}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.note"/></th>
						<td><c:out value="${pushGrpMstVO.note}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.inptId"/></th>
						<td><c:out value="${pushGrpMstVO.inptId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.inptIp"/></th>
						<td><c:out value="${pushGrpMstVO.inptIp}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.inptDttm"/></th>
						<td><c:out value="${pushGrpMstVO.inptDttm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.inptProg"/></th>
						<td><c:out value="${pushGrpMstVO.inptProg}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.modiId"/></th>
						<td><c:out value="${pushGrpMstVO.modiId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.modiIp"/></th>
						<td><c:out value="${pushGrpMstVO.modiIp}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.modiDttm"/></th>
						<td><c:out value="${pushGrpMstVO.modiDttm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushGrpMstVO.modiProg"/></th>
						<td><c:out value="${pushGrpMstVO.modiProg}"/></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="col50">
			<div class="col_title">
				<p>그룹원목록 <span class="board_total_count"><b>(<c:out value='${fn:length(pushGrpMstVO.memList)} '/>)</b></span></p>
			</div>
			<div class="col_inner">
				<div class="scroll_table_wrap">
					<table class="list_table ty2">
						<thead>
							<tr>
								<th><spring:message code="board.no"/></th>
								<th><spring:message code="pushNoticeSendVO.userId"/></th>
								<th><spring:message code="pushNoticeSendVO.userNm"/></th>
							</tr>
						</thead>
						<tbody id="sendUserList">
							<c:forEach var="item" items="${pushGrpMstVO.memList }" varStatus="i">
								<tr data-id="<c:out value="${item.grpMembId }"/>">
									<td><c:out value="${i.count}"/></td>
									<td><c:out value="${item.grpMembId}"/></td>
									<td><c:out value="${item.grpMembNm}"/></td>
								</tr>
							</c:forEach>
							<c:if test="${empty pushGrpMstVO.memList}">
								<tr><td class="no_data" colspan="3"><spring:message code="board.noData" /></td></tr>
							</c:if>
						</tbody>
				  	</table>
				</div>
			</div>
		</div>
	</div>
   	
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
  	<input type="hidden" name="sId" value="<c:out value='${pushGrpMstVO.grpCd }'/>">
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