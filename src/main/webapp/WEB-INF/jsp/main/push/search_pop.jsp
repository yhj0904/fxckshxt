<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: search_pop.jsp
 * @Description : 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
	<div class="tab_wrap">
		<ul id="sendTabList" class="tab_list">
			<li data-code="DEPT" class="active">
				<a href="javascript:fn_getTabContent('DEPT');"><span>부서 / 학과</span></a>
				<div class="tab_content">
					<div class="tree_check_wrap">
						<ul id="deptTreeList" class="tree_check_list">
						</ul>
					</div>
				</div>
			</li>
			<li data-code="PROF">	
				<a href="javascript:fn_getTabContent('PROF');"><span>교직원</span></a>
				<div class="tab_content">		
					<form name="search_form" method="post" autocomplete="off">			
						<div class="search_form">
							<div class="search_row">
								<label>소속</label>
								<select name="deptCd">
									<option value="">--전체--</option>
									<c:forEach var="childDept" items="${deptList }">
										<option value="<c:out value='${childDept.deptCd }'/>"><c:out value='${childDept.deptNmKor }'/></option>
									</c:forEach>
								</select>
							</div>
							<div class="search_row">
								<label>아이디 / 이름</label>
								<input type="text" name="searchValue"/>
							</div>
							<div class="search_bottom">
								<button type="button" class="button" onclick="fn_searchUserList('PROF');"><spring:message code="button.search"/></button>
							</div>
						</div>
					</form>
					<span class="board_total_count"><spring:message code="board.total"/> <b>0</b><spring:message code="board.row"/></span>
					<div class="scroll_table_wrap">
						<table class="list_table">
							<thead>
								<tr>
									<th><label><input type="checkbox" class="checkAllRow" onclick="fn_checkAllRow(this);" data-code="PROF"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
									<th><spring:message code="board.no"/></th>
									<th><spring:message code="userVO.userId"/></th>
									<th><spring:message code="userVO.userNm"/></th>
									<th><spring:message code="userVO.deptNm"/></th>
									<th><spring:message code="userVO.mbphNo"/></th>
								</tr>
							</thead>
							<tbody>
								<tr><td class="no_data" colspan="6"><spring:message code="board.noData" /></td></tr>
							</tbody>
					  	</table>
					</div>
				</div>
			</li>
			<li data-code="STU">
				<a href="javascript:fn_getTabContent('STU');"><span>학생</span></a>
				<div class="tab_content">		
					<form name="search_form" method="post" autocomplete="off">			
						<div class="search_form">
							<div class="search_row">
								<label>소속</label>
								<select name="deptCd">
									<option value="">--전체--</option>
									<c:forEach var="childDept" items="${deptList }">
										<option value="<c:out value='${childDept.deptCd }'/>"><c:out value='${childDept.deptNmKor }'/></option>
									</c:forEach>
								</select>
							</div>
							<div class="search_row">
								<label>아이디 / 이름</label>
								<input type="text" name="searchValue"/>
							</div>
							<div class="search_bottom">
								<button type="button" class="button" onclick="fn_searchUserList('STU');"><spring:message code="button.search"/></button>
							</div>
						</div>
					</form>
					<span class="board_total_count"><spring:message code="board.total"/> <b>0</b><spring:message code="board.row"/></span>
					<div class="scroll_table_wrap">
						<table class="list_table">
							<thead>
								<tr>
									<th><label><input type="checkbox" class="checkAllRow" onclick="fn_checkAllRow(this);" data-code="STU"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
									<th><spring:message code="board.no"/></th>
									<th><spring:message code="userVO.userId"/></th>
									<th><spring:message code="userVO.userNm"/></th>
									<th><spring:message code="userVO.deptNm"/></th>
									<th><spring:message code="userVO.mbphNo"/></th>
								</tr>
							</thead>
							<tbody>
								<tr><td class="no_data" colspan="6"><spring:message code="board.noData" /></td></tr>
							</tbody>
					  	</table>
					</div>
				</div>
			</li>
			<li data-code="GRP">
				<a href="javascript:fn_getTabContent('GRP');"><span>주소록</span></a>
				<div class="tab_content">
					<span class="board_total_count"><spring:message code="board.total"/> <b>0</b><spring:message code="board.row"/></span>
					<div class="scroll_table_wrap">
						<table class="list_table">
							<thead>
								<tr>
									<th><label><input type="checkbox" class="checkAllRow" onclick="fn_checkAllRow(this);" data-code="GRP"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
									<th><spring:message code="board.no"/></th>
									<th><spring:message code="pushGrpMstVO.grpNm"/></th>
									<th><spring:message code="pushGrpMstVO.memCnt"/></th>
								</tr>
							</thead>
							<tbody>
								<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>
							</tbody>
					  	</table>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button register_btn" href="javascript:fn_sendData();"><spring:message code="button.register" /></a>
				</li>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>

<script type="text/javaScript" defer="defer">
	
	var TAB_ID = "";
	
	$(function(){
		TAB_ID = "DEPT";
		//부서목록 조회
		fn_searchDeptList();
	})
	
	function fn_sendData(){
		if (TAB_ID == "DEPT") {
			fn_getDeptUserList();
		} else if (TAB_ID == "GRP") {
			fn_getGrpUserList();
		} else {
			fn_getUserList();
		}
	}
	
	function fn_closePop(){
		self.close();
	}
	
	//탭 조회
	function fn_getTabContent(tabId) {
		var presentId = $("#sendTabList li.active").attr("data-code");
		if(presentId != tabId){
			TAB_ID = tabId;
			$("#sendTabList li").removeClass("active");
			$("#sendTabList li[data-code='"+tabId+"']").addClass("active");
			if(TAB_ID == "GRP"){
				//주소록 조회
				fn_searchGrpList();
			}
		}
	}
	
	//조직도 조회
	function fn_searchDeptList(){
		gf_ajax({
			url : "/external/search/deptList.json",
			type : "POST",
		}).then(function(response){
			var str = "";
			if(!gf_isNull(response)){
				var treeLevel = 1;
				var treeLength = response.length;
				$.each(response, function(i, item){
					var count = i+1;
					if(i == 0) {
					} else if (item.deptLvl > treeLevel) {
						str += '<ul data-id="'+item.hiDeptCd +'">';
					} else if (item.deptLvl < treeLevel) {
						str += '</li>';
						for(var j = item.deptLvl; j <= treeLevel - 1; j++) {
							str += '</ul></li>';
						}
					} else {
						str += '</li>';
					}
					str += '<li>';
					if(item.childCnt > 0){
						str +='<a href="#open_tree" class="tree_item folder" data-id="'+item.deptCd+'"><span class="none">FILE</span></a>';
					} else {
						str +='<a href="#open_tree" class="tree_item" data-id="'+item.deptCd+'"><span class="none">FILE</span></a>';
					}
					str += '<label for="tree_'+count+'">';						
					if(item.childCnt > 0){
						str += '<input type="checkbox" id="tree_'+count+'" name="checkDeptRow" class="tree_check" value="" data-id="'+item.deptCd+'">';
					} else {
						str += '<input type="checkbox" id="tree_'+count+'" name="checkDeptRow" class="tree_check" value="' + item.deptCd + '" data-id="'+item.deptCd+'">';
					}
					str += item.deptNmKor + "("+ item.deptCd +")";
					str += '</label>';
					
					treeLevel =  item.deptLvl;
					
					if(count == treeLength) {
						str += '<li>';
						if(item.deptLvl > 1) {
							for(var j = 1; j <= item.deptLvl - 1; j++){
								str += '</ul></li>';
							}
						}
					}
				});
			}
			$("#deptTreeList").html(str);	
		});
	}
	
	//트리 클릭
	$(document).on("click", "#deptTreeList .tree_item", function(){
		var deptCd = $(this).attr("data-id");
		if(!$(this).hasClass("open")){
			$(this).addClass("open");	
		} else {
			$(this).removeClass("open");	
			$("#deptTreeList ul[data-id='"+deptCd+"']").find(".tree_item").removeClass("open");
		}
	});
	
	//트리 클릭
	$(document).on("click", "#deptTreeList .tree_check", function(){
		var checked = $(this).prop("checked");
		var deptCd = $(this).attr("data-id");
		if(checked) {
			$("#deptTreeList ul[data-id='"+deptCd+"']").find(".tree_check").prop("checked", true);
			var itemList = $(this).closest('ul').children('li');
			var itemCnt = itemList.length;
			if(itemCnt > 0){
				var checkedCnt = 0;
				$.each(itemList, function (i, item) {
					if($(item).children("label").children(".tree_check").prop("checked")) checkedCnt++;
				});
				if(itemCnt == checkedCnt){
					var parentId = $(this).closest('ul').attr('data-id');
					$("#deptTreeList").find(".tree_check[data-id='"+parentId+"']").prop("checked", true);
				}
			}
		} else {
			$("#deptTreeList ul[data-id='"+deptCd+"']").find(".tree_check").prop("checked", false);
			$(this).parents('ul').siblings('label').find('.tree_check').prop("checked", false);
		}
	});
	
	//부서별 사용자 목록 조회
	function fn_getDeptUserList(){
		var params = [];
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkDeptRow");
		var rowCnt = rowArr.length;
		for (var i = 0; i < rowArr.length; i++) {
			if (rowArr[i].type == "checkbox" && rowArr[i].checked && !gf_isNull(rowArr[i].value)){
				params.push(rowArr[i].value);
				chkCnt++;
			}
		}
		if(chkCnt <= 0) {
			alert('<spring:message code="message.alert.nocheck" />');
		} else {
			gf_ajax({
				url : "/external/search/deptUserList.json",
				type : "POST",
				data : { deptCd : params },
			}).then(function(response){
				opener.parent.fn_setUserList(response);
				self.close();
			});
		}
	}
	
	//사용자 조회
	function fn_searchUserList(code){		
		var params = {}
		params['userDvcd'] = code;
		var deptCd = $("#sendTabList li[data-code='"+code+"']").find("form[name='search_form']").find("select[name='deptCd']").val();
		params['deptCd'] = deptCd;
		var searchValue = $("#sendTabList li[data-code='"+code+"']").find("form[name='search_form']").find("input[name='searchValue']").val();
		params['searchValue'] = searchValue;
		
		gf_ajax({
			url : "/external/search/userList.json",
			type : "POST",
			data : params,
		}).then(function(response){
			var cnt = 0;
			var str = "";
			if(response != null && response.length > 0){
				cnt = response.length;
				$.each(response, function(i, item){
					var iCount = i+1;
					str += '<tr>';
					str += '<td>';
					str += '<label>';
					str += '<input type="checkbox" name="checkRow" value="'+item.userId+'" onclick="fn_checkRow(this);" data-code="'+code+'"><span class="none"><spring:message code="board.check" /></span>';
					str += '<input type="hidden" class="userId" value="'+item.userId+'"/>';
					str += '<input type="hidden" class="userNm" value="'+item.userNm+'"/>';
					str += '<input type="hidden" class="mbphNo" value="'+item.mbphNo+'"/>';
					str += '<input type="hidden" class="userDvcd" value="'+item.userDvcd+'"/>';
					str += '</label>';
					str += '</td>';
					str += '<td>'+iCount+'</td>';
					str += '<td>'+item.userId+'</td>';
					str += '<td>'+item.userNm+'</td>';
					str += '<td>'+item.deptNm+'</td>';
					str += '<td>'+item.mbphNo+'</td>';
					str += '</tr>';
				});
			} else {
				str += '<tr><td class="no_data" colspan="6"><spring:message code="board.noData" /></td></tr>';
			}
			$("#sendTabList li[data-code='"+code+"']").find(".board_total_count").find("b").html(cnt);
			$("#sendTabList li[data-code='"+code+"']").find(".list_table").find("tbody").html(str);
		});
	}
	
	//사용자 row 선택
	function fn_checkRow(obj) {
		var code = $(obj).attr("data-code");
		var chkCnt = 0;
		var rowArr = $("input[type='checkbox'][name='checkRow'][data-code='"+code+"']");
		var rowCnt = rowArr.length;
		$.each(rowArr, function(i, item){
			if($(item).prop("checked")){
				chkCnt++;
			}
		});
		if (chkCnt == rowCnt) {
			$("input.checkAllRow[data-code='"+code+"']").prop("checked", true);
		} else {
			$("input.checkAllRow[data-code='"+code+"']").prop("checked", false);
		}
	}
	
	//사용자 Row 전체선택
	function fn_checkAllRow(obj) {
		var code = $(obj).attr("data-code");
		var rowArr = $("input[type='checkbox'][name='checkRow'][data-code='"+code+"']");
		var checked = $(obj).prop("checked");
		$.each(rowArr, function(i, item){
			$(item).prop("checked", checked);
		});
	}
	
	//사용자 목록 조회
	function fn_getUserList(){
		var result = [];
		var code = TAB_ID;
		var params = [];
		var chkCnt = 0;		
		var rowArr = $("input[type='checkbox'][name='checkRow'][data-code='"+code+"']");
		var rowCnt = rowArr.length;
		$.each(rowArr, function(i, item){
			if($(item).prop("checked")){
				var parentObj = $(item).closest('label');
				var user = {};
				user['userId'] = parentObj.find(".userId").val();
				user['userNm'] = parentObj.find(".userNm").val();
				user['mbphNo'] = parentObj.find(".mbphNo").val();
				user['userDvcd'] = parentObj.find(".userDvcd").val();
				result.push(user);
				chkCnt++;
			}
		});
		if(chkCnt <= 0) {
			alert('<spring:message code="message.alert.nocheck" />');
		} else {
			opener.parent.fn_setUserList(result);
			self.close();
		}
	}
	
	function fn_searchGrpList(){
		gf_ajax({
			url : "/push/search/grp/list.json",
			type : "POST",
		}).then(function(response){
			var cnt = 0;
			var str = "";
			if(response != null && response.length > 0){
				cnt = response.length;
				$.each(response, function(i, item){
					var iCount = i+1;
					str += '<tr>';
					str += '<td>';
					str += '<label>';
					str += '<input type="checkbox" name="checkRow" value="'+item.grpCd+'" onclick="fn_checkRow(this);" data-code="GRP"><span class="none"><spring:message code="board.check" /></span>';
					str += '</label>';
					str += '</td>';
					str += '<td>'+iCount+'</td>';
					str += '<td>'+item.grpNm+'</td>';
					str += '<td>'+item.memCnt+'</td>';
					str += '</tr>';
				});
			} else {
				str += '<tr><td class="no_data" colspan="6"><spring:message code="board.noData" /></td></tr>';
			}
			$("#sendTabList li[data-code='GRP']").find(".board_total_count").find("b").html(cnt);
			$("#sendTabList li[data-code='GRP']").find(".list_table").find("tbody").html(str);
		});
	}
	
	//사용자 목록 조회
	function fn_getGrpUserList(){
		var params = [];
		var chkCnt = 0;		
		var rowArr = $("input[type='checkbox'][name='checkRow'][data-code='GRP']");
		var rowCnt = rowArr.length;
		$.each(rowArr, function(i, item){
			if($(item).prop("checked")){
				params.push($(item).val());
				chkCnt ++;
			}
		});
		if(chkCnt <= 0) {
			alert('<spring:message code="message.alert.nocheck" />');
		} else {
			gf_ajax({
				url : "/push/search/grp/memListByCd.json",
				type : "POST",
				data : { grpCd : params },
			}).then(function(response){
				var userList = [];
				if(response != null && response.length > 0){
					cnt = response.length;
					$.each(response, function(i, item){
						var user = {};
						user['userId'] = item.grpMembId;
						user['userNm'] = item.grpMembNm;
						user['mbphNo'] = item.grpMembMobile;
						user['userDvcd'] = item.grpMembDv;
						userList.push(user);
					})
				}
				opener.parent.fn_setUserList(userList);
				self.close();
			});
		}
	}
	
</script>