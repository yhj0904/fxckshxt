<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 알림관리 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="pushNoticeVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<div class="send_wrap">
		<div class="send_detail">
			<div class="write_box">
				<div class="send_title">
					<label for="noticeTitle">알림제목</label>
				</div>
				<div class="send_content">
					<form:input path="noticeTitle" cssClass="w_full"/>
					<span class="form_error" data-path="noticeTitle"><form:errors path="noticeTitle"/></span>
				</div>
			</div>
			<div class="write_box">
				<div class="send_title">
					<label for="noticeData">알림내용</label>
					<span class="right"><span id="noticeDataCnt">0/1000</span> byte</span>
				</div>
				<div class="send_content">				
					<form:textarea path="noticeData" onkeyup="fn_checkByte(this);"/>
					<span class="form_error" data-path="noticeData"><form:errors path="noticeData"/></span>
					<div class="upload">
						<label class="file">
	    					<span class="name"><spring:message code="text.file.placeholder" /></span>
	    					<form:input type="file" path="uploadFile"/>
	    					<span class="btn">이미지 첨부</span>
	    				</label>
						<span class="form_error" data-path="uploadFile"><form:errors path="uploadFile" /></span>
					</div>
				</div>
			</div>
			<div class="write_box">
				<div class="send_title">
					<label for="userId">발송자</label>
				</div>
				<div class="send_content">
					<c:choose>
						<c:when test='${!GV_IS_ADMIN }'>
							<form:input path="userId" readonly="true" value="${LOGIN_USER.loginId }"/>
							<button type="button" class="search_btn" onclick="fn_searchUser();">검색</button>
							<span class="form_error" data-path="userId"><form:errors path="userId"/></span>									
						</c:when>
						<c:otherwise>
							<form:hidden path="userId"/>
							<label for="userNm">이름</label>
							<form:input path="userNm" readonly="true"/>
							<label for="userMobile">전화번호</label>
							<form:input path="userMobile" readonly="true"/>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="write_box">
				<div class="send_title">
					<label for="reservationDt">예약전송</label>
					<div class="right">
						<form:radiobutton path="reservationYn" value="Y" label="설정"/>
						<form:radiobutton path="reservationYn" value="N" label="미설정"/>
					</div>
				</div>
				<div class="send_content">
					
					<div id="inputReservation" class="mt_10">
						<form:input path="reservationYYYYMMDD" cssClass="input_date" onclick="gf_datepicker(this);"/>
						<form:select path="reservationHour">
							 <c:forEach var="i" begin="0" end="23" step="1">
								<form:option value="${i }" label="${i }"/>
							</c:forEach>
						</form:select>시
						<form:select path="reservationMinute">
							 <c:forEach var="i" begin="0" end="59" step="1">
								<form:option value="${i }" label="${i }"/>
							</c:forEach>
						</form:select>분
					</div>
					<span class="form_error" data-path="reservationDt"><form:errors path="reservationDt"/></span>
				</div>
			</div>
		</div>
		<div class="send_list">
			<div class="send_title">
				알림대상 <span>(<span id="sendUserCnt"><c:out value='${fn:length(pushNoticeVO.sendList)} '/></span>)</span>
				<div class="right">
					<a class="button add_btn" href="javascript:fn_searchPop();">알림대상 추가</a>
					<a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a>
				</div>
			</div>
			<div class="send_content" style="border:1px solid #e8e8e8;">
				<div class="scroll_table_wrap">
					<table class="list_table ty2">
						<thead>
							<tr>
								<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
								<th><spring:message code="pushNoticeSendVO.userId"/></th>
								<th><spring:message code="pushNoticeSendVO.userNm"/></th>
								<th>삭제</th>
							</tr>
						</thead>
						<tbody id="sendUserList">
							<c:forEach var="item" items="${pushNoticeVO.sendList }" varStatus="i">
								<tr data-id="<c:out value="${item.userId }"/>">
									<td>
										<label><input type="checkbox" name="checkRow" value="<c:out value="${item.userId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
										<input type="hidden" class="sendUserId" name="sendList[<c:out value='${i.index }'/>].userId" value="<c:out value="${item.userId }"/>">
										<input type="hidden" class="sendUserNm" name="sendList[<c:out value='${i.index }'/>].userNm" value="<c:out value="${item.userNm }"/>">
										<input type="hidden" class="sendUserMobile" name="sendList[<c:out value='${i.index }'/>].userMobile" value="<c:out value="${item.userMobile }"/>">
									</td>
									<td><c:out value="${item.userId}"/></td>
									<td><c:out value="${item.userNm}"/></td>
									<td><a href="javascript:fn_removeUser('<c:out value="${item.userId}"/>')" class="table_btn"><spring:message code="button.remove" /></a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty pushNoticeVO.sendList}">
								<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>
							</c:if>
						</tbody>
				  	</table>
				</div>
			</div>
		</div>
	</div>
	   	
   	<div class="send_btn">
		<a class="button" href="javascript:fn_registerAction();">전송</a>
	</div>
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="pushNoticeVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" defer="defer">
	
	$(function(){
		var reservationYn = '<c:out value="${pushNoticeVO.reservationYn}"/>';
		if(reservationYn == 'Y'){
			$("#inputReservation").css("display", "block");
		} else {
			$("#inputReservation").css("display", "none");
		};
		
		$("input[name='reservationYn']").click(function(){
			if($(this).val() == "Y"){
				$("#inputReservation").css("display", "block");
			} else {
				$("#inputReservation").css("display", "none");
			}
		});
	})

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (!validatePushNoticeVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/sendAction.do";
			frm.submit();
		}
	}
	
	//바이트 체크
	function fn_checkByte(obj) {
		gf_checkByte(obj, 1000, $("#noticeDataCnt"));
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
		if(data != null){
			document.detailForm.userId.value = data.userId;
			document.detailForm.userMobile.value = data.mbphNo;
		}
	}
	
	//사용자 조회
	function fn_searchPop(){
		var _width = '800';
	    var _height = '600';
	    var _left = Math.ceil(( window.screen.width - _width )/2);
	    var _top = Math.ceil(( window.screen.width - _height )/2); 		 
	    window.open('/push/search.do', 'user-pop', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
	}
	
	//Row 선택
	function fn_checkRow() {
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkRow");
		var rowCnt = rowArr.length;
		for (var i = 0; i < rowArr.length; i++) {
			if (rowArr[i].type == "checkbox" && rowArr[i].checked) chkCnt++;
		}
		if (chkCnt == rowCnt) {
			document.getElementById("checkAllRow").checked = true;
		} else {
			document.getElementById("checkAllRow").checked = false;
		}
	}
	
	//Row 전체선택
	function fn_checkAllRow() {
		var rowArr = document.getElementsByName("checkRow");
		var checked = document.getElementById("checkAllRow").checked;
		if (checked) {
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox") rowArr[i].checked = true;
			}
		} else {
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox") rowArr[i].checked = false;
			}
		}
	}
	
	//선택삭제
	function fn_removeCheckedRow() {
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkRow");
		var rowCnt = rowArr.length;
		var arr = [];
		for (var i = 0; i < rowArr.length; i++) {
			var userId = rowArr[i].value;
			if (rowArr[i].type == "checkbox" && rowArr[i].checked){
				chkCnt++;
			} else {
				var jsonData = {}
				jsonData['userId'] = $("#sendUserList tr[data-id='"+userId+"']").find(".sendUserId").val();
				jsonData['userNm'] = $("#sendUserList tr[data-id='"+userId+"']").find(".sendUserNm").val();
				jsonData['userMobile'] = $("#sendUserList tr[data-id='"+userId+"']").find(".sendUserMobile").val();
				arr.push(jsonData);
			}
		}
		if(chkCnt > 0) {
			var str = "";
			var sendUserCnt = arr.length;
			if(arr.length > 0){
				$.each(arr, function(i, item){
					str += '<tr data-id="'+item.userId+'">';
					str += '<td>';
					str += '<label><input type="checkbox" name="checkRow" value="'+item.userId+'" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>';
					str += '<input type="hidden" class="sendUserId" name="sendList['+i+'].userId" value="'+item.userId +'">';
					str += '<input type="hidden" class="sendUserNm" name="sendList['+i+'].userNm" value="'+item.userNm +'">';
					str += '<input type="hidden" class="sendUserMobile" name="sendList['+i+'].userMobile" value="'+item.userMobile +'">';
					str += '</td>';
					str += '<td>'+item.userId+'</td>';
					str += '<td>'+item.userNm+'</td>';
					str += '<td><a href="javascript:fn_removeUser(\''+item.userId+'\')" class="table_btn"><spring:message code="button.remove" /></a></td>';
					str += '</tr>';
				});
			} else {
				str = '<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>';
			}
			$("#sendUserCnt").text(sendUserCnt);
			$("#sendUserList").html(str);
			$('#checkAllRow').removeAttr('checked');
		} else {
			alert('<spring:message code="message.alert.nocheck" />');
		}
	}
	
	function fn_setUserList(data){
		var str = "";
		if(data != null){
			//기존 데이터
			var cnt = 0;
			cnt = $("#sendUserList .sendUserId").length;
			var arr = [];
			if(cnt > 0){
				for(var i=0; i<cnt; i++){
					var jsonData = {}
					jsonData['userId'] = $("#sendUserList .sendUserId").eq(i).val();
					jsonData['userNm'] = $("#sendUserList .sendUserNm").eq(i).val();
					jsonData['userMobile'] = $("#sendUserList .sendUserMobile").eq(i).val();
					arr.push(jsonData);
				}
			}
			
			$.each(data, function(i, item){
				if($("#sendUserList .sendUserId[value='"+item.userId+"']").length == 0){
					var jsonData = {};
					jsonData['userId'] = item.userId;
					jsonData['userNm'] = item.userNm;
					jsonData['userMobile'] = item.mbphNo;
					arr.push(jsonData);
				}
			});
			
			var sendUserCnt = arr.length;
			if(arr.length > 0){
				$.each(arr, function(i, item){
					str += '<tr data-id="'+item.userId+'">';
					str += '<td>';
					str += '<label><input type="checkbox" name="checkRow" value="'+item.userId+'" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>';
					str += '<input type="hidden" class="sendUserId" name="sendList['+i+'].userId" value="'+item.userId +'">';
					str += '<input type="hidden" class="sendUserNm" name="sendList['+i+'].userNm" value="'+item.userNm +'">';
					str += '<input type="hidden" class="sendUserMobile" name="sendList['+i+'].userMobile" value="'+item.userMobile +'">';
					str += '</td>';
					str += '<td>'+item.userId+'</td>';
					str += '<td>'+item.userNm+'</td>';
					str += '<td><a href="javascript:fn_removeUser(\''+item.userId+'\')" class="table_btn"><spring:message code="button.remove" /></a></td>';
					str += '</tr>';
				});
			} else {
				str = '<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>';
			}
			$("#sendUserCnt").text(sendUserCnt);
			$("#sendUserList").html(str);
		}
	}
	
	function fn_removeUser(sUserId){
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkRow");
		var rowCnt = rowArr.length;
		var arr = [];
		for (var i = 0; i < rowArr.length; i++) {
			var userId = rowArr[i].value;
			if (userId != sUserId){
				var jsonData = {}
				jsonData['userId'] = $("#sendUserList tr[data-id='"+userId+"']").find(".sendUserId").val();
				jsonData['userNm'] = $("#sendUserList tr[data-id='"+userId+"']").find(".sendUserNm").val();
				jsonData['userMobile'] = $("#sendUserList tr[data-id='"+userId+"']").find(".sendUserMobile").val();
				arr.push(jsonData);
			}
		}
		var str = "";
		var sendUserCnt = arr.length;
		if(arr.length > 0){
			$.each(arr, function(i, item){
				str += '<tr data-id="'+item.userId+'">';
				str += '<td>';
				str += '<label><input type="checkbox" name="checkRow" value="'+item.userId+'" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>';
				str += '<input type="hidden" class="sendUserId" name="sendList['+i+'].userId" value="'+item.userId +'">';
				str += '<input type="hidden" class="sendUserNm" name="sendList['+i+'].userNm" value="'+item.userNm +'">';
				str += '<input type="hidden" class="sendUserMobile" name="sendList['+i+'].userMobile" value="'+item.userMobile +'">';
				str += '</td>';
				str += '<td>'+item.userId+'</td>';
				str += '<td>'+item.userNm+'</td>';
				str += '<td><a href="javascript:fn_removeUser(\''+item.userId+'\')" class="table_btn"><spring:message code="button.remove" /></a></td>';
				str += '</tr>';
			});
		} else {
			str = '<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>';
		}
		$("#sendUserCnt").text(sendUserCnt);
		$("#sendUserList").html(str);
	}
	
</script>