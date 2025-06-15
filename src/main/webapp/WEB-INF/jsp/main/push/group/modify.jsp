<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 그룹관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="sub_title">
	주소록 관리		
</div>
<div class="sub_content">
	<form:form commandName="pushGrpMstVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
		<form:hidden path="grpCd"/>
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
		
		<div>
			<table class="detail_table">
				<caption>그룹관리 등록</caption>
				<colgroup>
		   			<col width="150"/>
		   			<col width="?"/>
		   		</colgroup>
				<tr>
					<th><label for="grpNm"><spring:message code="pushGrpMstVO.grpNm"/></label></th>
					<td>
						<form:input path="grpNm"/>
						<span class="form_error" data-path="grpNm"><form:errors path="grpNm"/></span>
					</td>
				</tr>
				<tr>
					<th><label for="note"><spring:message code="pushGrpMstVO.note"/></label></th>
					<td>
						<form:input path="note"/>
						<span class="form_error" data-path="note"><form:errors path="note"/></span>
					</td>
				</tr>
		  	</table>
		</div>
		<div>
			<div class="btn_wrap">
				<ul>
					<li>
						<a class="button save_btn" href="javascript:fn_searchPop();">그룹원 등록</a>
					</li>
					<li>
						<a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a>
					</li>
				</ul>
			</div>
			<span class="board_total_count"><spring:message code="board.total"/> <b id="sendUserCnt"><c:out value='${fn:length(pushGrpMstVO.memList)} '/></b><spring:message code="board.row"/></span>
			<div class="scroll_table_wrap">
				<table class="list_table ty2">
					<thead>
						<tr>
							<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
							<th><spring:message code="board.no"/></th>
							<th><spring:message code="pushNoticeSendVO.userId"/></th>
							<th><spring:message code="pushNoticeSendVO.userNm"/></th>
							<th>삭제</th>
						</tr>
					</thead>
					<tbody id="sendUserList">
						<c:forEach var="item" items="${pushGrpMstVO.memList }" varStatus="i">
							<tr data-id="<c:out value="${item.grpMembId }"/>">
								<td>
									<label><input type="checkbox" name="checkRow" value="<c:out value="${item.grpMembId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
									<input type="hidden" class="grpMembId" name="memList[<c:out value='${i.index }'/>].grpMembId" value="<c:out value="${item.grpMembId }"/>">
									<input type="hidden" class="grpMembNm" name="memList[<c:out value='${i.index }'/>].grpMembNm" value="<c:out value="${item.grpMembNm }"/>">
									<input type="hidden" class="grpMembDv" name="memList[<c:out value='${i.index }'/>].grpMembDv" value="<c:out value="${item.grpMembDv }"/>">
									<input type="hidden" class="grpMembMobile" name="memList[<c:out value='${i.index }'/>].grpMembMobile" value="<c:out value="${item.grpMembMobile }"/>">
								</td>
								<td><c:out value="${i.count}"/></td>
								<td><c:out value="${item.grpMembId}"/></td>
								<td><c:out value="${item.grpMembNm}"/></td>
								<td></td>
							</tr>
						</c:forEach>
						<c:if test="${empty pushGrpMstVO.memList}">
							<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>
						</c:if>
					</tbody>
			  	</table>
			</div>
		</div>
		
		<% /** 이중방지 토큰 */ %>
		<double-submit:preventer/>
	  	
	   	<% /** 검색조건 유지 */ %>
	   	<input type="hidden" name="sId" value="<c:out value='${pushGrpMstVO.grpCd}'/>">
		<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	<% /** //검색조건 유지 */ %>  	
	</form:form>
</div>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="pushGrpMstVO" staticJavascript="false" xhtml="true" cdata="false"/>
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
		if (!validatePushGrpMstVO(frm)) {
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
				jsonData['grpMembId'] = $("#sendUserList tr[data-id='"+userId+"']").find(".grpMembId").val();
				jsonData['grpMembNm'] = $("#sendUserList tr[data-id='"+userId+"']").find(".grpMembNm").val();
				jsonData['grpMembDv'] = $("#sendUserList tr[data-id='"+userId+"']").find(".grpMembDv").val();
				jsonData['grpMembMobile'] = $("#sendUserList tr[data-id='"+userId+"']").find(".grpMembMobile").val();
				arr.push(jsonData);
			}
		}
		if(chkCnt > 0) {
			var str = "";
			var sendUserCnt = arr.length;
			if(arr.length > 0){
				$.each(arr, function(i, item){
					var iCount = i+1;
					str += '<tr data-id="'+item.grpMembId+'">';
					str += '<td>';
					str += '<label><input type="checkbox" name="checkRow" value="'+item.grpMembId+'" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>';
					str += '<input type="hidden" class="grpMembId" name="memList['+i+'].grpMembId" value="'+item.grpMembId +'">';
					str += '<input type="hidden" class="grpMembNm" name="memList['+i+'].grpMembNm" value="'+item.grpMembNm +'">';
					str += '<input type="hidden" class="grpMembDv" name="memList['+i+'].grpMembDv" value="'+item.grpMembDv +'">';
					str += '<input type="hidden" class="grpMembMobile" name="memList['+i+'].grpMembMobile" value="'+item.grpMembMobile +'">';
					str += '</td>';
					str += '<td>'+iCount+'</td>';
					str += '<td>'+item.grpMembId+'</td>';
					str += '<td>'+item.grpMembNm+'</td>';
					str += '<td></td>';
					str += '</tr>';
				});
			} else {
				str = '<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>';
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
			cnt = $("#sendUserList .grpMembId").length;
			var arr = [];
			if(cnt > 0){
				for(var i=0; i<cnt; i++){
					var jsonData = {}
					jsonData['grpMembId'] = $("#sendUserList .grpMembId").eq(i).val();
					jsonData['grpMembNm'] = $("#sendUserList .grpMembNm").eq(i).val();
					jsonData['grpMembDv'] = $("#sendUserList .grpMembDv").eq(i).val();
					jsonData['grpMembMobile'] = $("#sendUserList .grpMembMobile").eq(i).val();
					arr.push(jsonData);
				}
			}
			
			$.each(data, function(i, item){
				if($("#sendUserList .grpMembId[value='"+item.userId+"']").length == 0){
					var jsonData = {};
					jsonData['grpMembId'] = item.userId;
					jsonData['grpMembNm'] = item.userNm;
					jsonData['grpMembDv'] = item.userDvcd;
					jsonData['grpMembMobile'] = item.mbphNo;
					arr.push(jsonData);
				}
			});
			
			var sendUserCnt = arr.length;
			if(arr.length > 0){
				$.each(arr, function(i, item){
					var iCount = i+1;
					str += '<tr data-id="'+item.grpMembId+'">';
					str += '<td>';
					str += '<label><input type="checkbox" name="checkRow" value="'+item.grpMembId+'" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>';
					str += '<input type="hidden" class="grpMembId" name="memList['+i+'].grpMembId" value="'+item.grpMembId +'">';
					str += '<input type="hidden" class="grpMembNm" name="memList['+i+'].grpMembNm" value="'+item.grpMembNm +'">';
					str += '<input type="hidden" class="grpMembDv" name="memList['+i+'].grpMembDv" value="'+item.grpMembDv +'">';
					str += '<input type="hidden" class="grpMembMobile" name="memList['+i+'].grpMembMobile" value="'+item.grpMembMobile +'">';
					str += '</td>';
					str += '<td>'+iCount+'</td>';
					str += '<td>'+item.grpMembId+'</td>';
					str += '<td>'+item.grpMembNm+'</td>';
					str += '<td></td>';
					str += '</tr>';
				});
			} else {
				str = '<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>';
			}
			$("#sendUserCnt").text(sendUserCnt);
			$("#sendUserList").html(str);
		}
	}
	
</script>