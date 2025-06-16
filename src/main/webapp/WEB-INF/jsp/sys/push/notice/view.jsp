<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 알림관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="noticeNo"/>
	
	<div class="grid_wrap">
		<div class="col100">
			<div class="col_title">
				<p>알림내용</p>
			</div>
			<div class="col_inner">
				<table class="detail_table">
					<caption>알림관리 상세조회</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><spring:message code="pushNoticeVO.noticeTitle"/></th>
						<td><c:out value="${pushNoticeVO.noticeTitle}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.noticeData"/></th>
						<td><c:out value="${pushNoticeVO.noticeData}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.noticeImg"/></th>
						<td><img src='<c:out value="${pushNoticeVO.noticeImg}"/>' alt=""/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.userId"/></th>
						<td><c:out value="${pushNoticeVO.userId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.userNm"/></th>
						<td><c:out value="${pushNoticeVO.userNm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.userMobile"/></th>
						<td><c:out value="${pushNoticeVO.userMobile}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.noticeDt"/></th>
						<td><c:out value="${pushNoticeVO.noticeDt}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.reservationDt"/></th>
						<td><c:out value="${pushNoticeVO.reservationDt}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.pushState"/></th>
						<td><c:out value="${pushNoticeVO.pushState}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.inptId"/></th>
						<td><c:out value="${pushNoticeVO.inptId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.inptIp"/></th>
						<td><c:out value="${pushNoticeVO.inptIp}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.inptDttm"/></th>
						<td><c:out value="${pushNoticeVO.inptDttm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.inptProg"/></th>
						<td><c:out value="${pushNoticeVO.inptProg}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.modiId"/></th>
						<td><c:out value="${pushNoticeVO.modiId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.modiIp"/></th>
						<td><c:out value="${pushNoticeVO.modiIp}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.modiDttm"/></th>
						<td><c:out value="${pushNoticeVO.modiDttm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="pushNoticeVO.modiProg"/></th>
						<td><c:out value="${pushNoticeVO.modiProg}"/></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="col100">
			<div class="col_title">
				<p>알림대상 <span class="board_total_count"><b>(<c:out value='${fn:length(pushNoticeVO.sendList)} '/>)</b></span></p>
			</div>
			<div class="col_inner">
				<div class="scroll_table_wrap">
					<table class="list_table ty2">
						<thead>
							<tr>
								<th><spring:message code="pushNoticeSendVO.userId"/></th>
								<th><spring:message code="pushNoticeSendVO.userNm"/></th>
								<th><spring:message code="pushNoticeSendVO.userMobile"/></th>
								<th><spring:message code="pushNoticeSendVO.deviceDv"/></th>
								<th><spring:message code="pushNoticeSendVO.sendDv"/></th>
								<th><spring:message code="pushNoticeSendVO.sendTime"/></th>
								<th><spring:message code="pushNoticeSendVO.sendCnt"/></th>
								<th><spring:message code="pushNoticeSendVO.rcveDv"/></th>
								<th><spring:message code="pushNoticeSendVO.rcveChk"/></th>
								<th><spring:message code="pushNoticeSendVO.rcveFailDv"/></th>
								<th><spring:message code="pushNoticeSendVO.rcveTime"/></th>
							</tr>
						</thead>
						<tbody id="sendUserList">
							<c:forEach var="item" items="${pushNoticeVO.sendList }" varStatus="i">
								<tr>
									<td><c:out value="${item.userId}"/></td>
									<td><c:out value="${item.userNm}"/></td>
									<td><c:out value="${item.userMobile}"/></td>
									<td><c:out value="${item.deviceDv}"/></td>
									<td><c:out value="${item.sendDv}"/></td>
									<td><c:out value="${item.sendTime}"/></td>
									<td><c:out value="${item.sendCnt}"/></td>
									<td><c:out value="${item.rcveDv}"/></td>
									<td><c:out value="${item.rcveChk}"/></td>
									<td><c:out value="${item.rcveFailDv}"/></td>
									<td><c:out value="${item.rcveTime}"/></td>
								</tr>
							</c:forEach>
							<c:if test="${empty pushNoticeVO.sendList}">
								<tr><td class="no_data" colspan="11"><spring:message code="board.noData" /></td></tr>
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
  	<input type="hidden" name="sId" value="<c:out value='${pushNoticeVO.noticeNo }'/>">
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