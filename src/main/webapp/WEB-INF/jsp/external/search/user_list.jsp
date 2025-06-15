<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: user_list.jsp
 * @Description : 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
	<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
		<div class="search_box">
			<select name="searchOption" title="<spring:message code="search.choose"/>">
				<option value="userId" <c:if test="${searchOption eq 'userId'}">selected</c:if>><spring:message code="userVO.userId"/></option>
				<option value="userNm" <c:if test="${searchOption eq 'userNm'}">selected</c:if>><spring:message code="userVO.userNm"/></option>
			</select>
			<input type="text" name="searchValue" value="<c:out value='${searchValue }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
			<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
		</div>
	</form>
	<form:form id="listForm" name="listForm" method="post" autocomplete="off">	
		<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${totCnt}'/></b><spring:message code="board.row"/></span>
		<table class="list_table">
			<thead>
				<tr>
					<c:if test='${searchType ne "one"}'>
						<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
					</c:if>
					<th><spring:message code="userVO.userId"/></th>
					<th><spring:message code="userVO.userNm"/></th>
					<th><spring:message code="userVO.deptNm"/></th>
					<th><spring:message code="userVO.mbphNo"/></th>					
					<c:if test='${searchType eq "one"}'>
						<th>Select</th>		
					</c:if>					
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${list }" varStatus="i">
					<tr>						
						<c:if test='${searchType ne "one"}'>
							<td>
								<label data-id="<c:out value='${item.userId }'/>">
									<input type="checkbox" name="checkRow" value="<c:out value="${i.index }"/>" onclick="fn_checkRow();">
									<input type="hidden" class="userId" value="<c:out value='${item.userId }'/>">
									<input type="hidden" class="userNm" value="<c:out value='${item.userNm }'/>">
									<input type="hidden" class="deptCd" value="<c:out value='${item.deptCd }'/>">
									<input type="hidden" class="deptNm" value="<c:out value='${item.deptNm }'/>">
									<input type="hidden" class="mbphNo" value="<c:out value='${item.mbphNo }'/>">
									<input type="hidden" class="userDvcd" value="<c:out value='${item.userDvcd }'/>">
									<input type="hidden" class="workDvcd" value="<c:out value='${item.workDvcd }'/>">
									<input type="hidden" class="statDvcd" value="<c:out value='${item.statDvcd }'/>">
									<span class="none"><spring:message code="board.check" /></span>
								</label>
							</td>
						</c:if>
						<td><c:out value="${item.userId}"/></td>
						<td><c:out value="${item.userNm}"/></td>
						<td><c:out value="${item.deptNm}"/></td>
						<td><c:out value="${item.mbphNo}"/></td>
						<c:if test='${searchType eq "one"}'>
							<td>
								<a href="javascript:fn_selectUser('<c:out value='${item.userId }'/>')">선택</a>
								<label data-id="<c:out value='${item.userId }'/>" style="display:none;">
									<input type="hidden" class="userId" value="<c:out value='${item.userId }'/>">
									<input type="hidden" class="userNm" value="<c:out value='${item.userNm }'/>">
									<input type="hidden" class="deptCd" value="<c:out value='${item.deptCd }'/>">
									<input type="hidden" class="deptNm" value="<c:out value='${item.deptNm }'/>">
									<input type="hidden" class="mbphNo" value="<c:out value='${item.mbphNo }'/>">
									<input type="hidden" class="userDvcd" value="<c:out value='${item.userDvcd }'/>">
									<input type="hidden" class="workDvcd" value="<c:out value='${item.workDvcd }'/>">
									<input type="hidden" class="statDvcd" value="<c:out value='${item.statDvcd }'/>">
								</label>
							</td>
						</c:if>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>
				</c:if>
			</tbody>
	  	</table>
	</form:form>
	<div class="pop_bottom_btn">
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button register_btn" href="javascript:fn_checkUserList();"><spring:message code="button.register" /></a>
				</li>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>

<script type="text/javaScript" defer="defer">
	
	//검색
	function fn_search(){
		document.boardSearchForm.submit();
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
	
	//등록
	function fn_selectUser(id) {
		var parentObj = $("label[data-id='"+id+"']");
		var jsonData = {};				
		jsonData['userId'] = parentObj.find(".userId").val();
		jsonData['userNm'] = parentObj.find(".userNm").val();
		jsonData['deptCd'] = parentObj.find(".deptCd").val();
		jsonData['deptNm'] = parentObj.find(".deptNm").val();
		jsonData['mbphNo'] = parentObj.find(".mbphNo").val();
		jsonData['userDvcd'] = parentObj.find(".userDvcd").val();
		jsonData['statDvcd'] = parentObj.find(".statDvcd").val();
		jsonData['workDvcd'] = parentObj.find(".workDvcd").val();
		opener.parent.fn_getUserInfo(jsonData);
		self.close();
	}
	
	//등록
	function fn_checkUserList() {
		var formData = new Array();
		var checkedSId = "";
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkRow");
		var rowCnt = rowArr.length;
		for (var i = 0; i < rowArr.length; i++) {
			if (rowArr[i].type == "checkbox" && rowArr[i].checked){
				var parentObj = $(rowArr[i]).parents("label");
				var jsonData = {};				
				jsonData['userId'] = parentObj.find(".userId").val();
				jsonData['userNm'] = parentObj.find(".userNm").val();
				jsonData['deptCd'] = parentObj.find(".deptCd").val();
				jsonData['deptNm'] = parentObj.find(".deptNm").val();
				jsonData['mbphNo'] = parentObj.find(".mbphNo").val();
				jsonData['userDvcd'] = parentObj.find(".userDvcd").val();
				jsonData['statDvcd'] = parentObj.find(".statDvcd").val();
				jsonData['workDvcd'] = parentObj.find(".workDvcd").val();
				formData.push(jsonData);
				chkCnt++;
			}
		}
		opener.parent.fn_getUserList(formData);
		self.close();
	}
	
	function fn_closePop(){
		self.close();
	}
</script>