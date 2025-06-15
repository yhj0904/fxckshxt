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
<form:form commandName="pushNoticeVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="noticeNo"/>
	
	<div class="send_wrap ty2">
		<div class="send_detail">
			<div class="write_box">
				<div class="send_title">
					<label for="noticeTitle">알림제목</label>
				</div>
				<div class="send_content">
					<div class="input w_full">
						<c:out value='${pushNoticeVO.noticeTitle }'/>
					</div>
				</div>
			</div>
			<div class="write_box">
				<div class="send_title">
					<label for="noticeData">알림내용</label>
				</div>
				<div class="send_content">
					<div class="textarea w_full">
						<c:out value='${pushNoticeVO.noticeData }'/>
					</div>	
					<div class="upload">
						<img src='<c:out value="${pushNoticeVO.noticeImg}"/>' alt=""/>
					</div>
				</div>
			</div>
			<div class="write_box">
				<div class="send_title">
					<label for="userId">발송자</label>
				</div>
				<div class="send_content">
					<label for="userNm">이름</label>
					<form:input path="userNm" readonly="true"/>
					<label for="userMobile">전화번호</label>
					<form:input path="userMobile" readonly="true"/>
				</div>
			</div>
			<c:if test='${reservationDt ne null and reservationDt ne "" }'>
				<div class="write_box">
					<div class="send_title">
						<label for="reservationDt">예약전송</label>
					</div>
					<div class="send_content">					
						<div class="input">
							<c:out value='${pushNoticeVO.reservationDt }'/>
						</div>
					</div>
				</div>
			</c:if>
		</div>
		<div class="send_list">
			<div class="send_title">
				알림대상 <span>(<span><c:out value='${fn:length(pushNoticeVO.sendList)} '/></span>)</span>
			</div>
			<div class="send_content" style="border:1px solid #e8e8e8;">
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
</script>