<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 권한 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="grid_wrap">
	<div class="col30">
		<div class="col_inner">
			<div id="btn_wrap1" class="btn_wrap">
				<ul>
					<li>
						<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
					</li>
				</ul>
			</div>
			<div class="tree_wrap">
				<ul class="tree_list">
					<c:forEach var="item" items="${authList }">
						<li><a href="#open_tree" class="tree_item" data-id="<c:out value='${item.authCd }' />"><c:out value='${item.authNm }'/>(<c:out value='${item.authCd }' />)</a></li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<div class="col70">
		<div class="col_inner">
			<div id="btn_wrap2" class="btn_wrap">
				<ul>
					<li>
						<a class="button save_btn" href="javascript:fn_registerAction();"><spring:message code="button.save" /></a>
					</li>
					<li>
						<a class="button remove_btn" href="javascript:fn_removeAction();"><spring:message code="button.remove" /></a>
					</li>
				</ul>
			</div>
<form:form modelAttribute
			
				<input type="hidden" name="registerFlag" value="C"/>
			
				<table class="detail_table">
					<caption>권한 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><label for="authCd"><spring:message code="authVO.authCd" /></label></th>
						<td>
							<form:input path="authCd" readonly="true"/>
							<span class="form_error" data-path="authCd"><form:errors path="authCd" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="authNm"><spring:message code="authVO.authNm" /></label></th>
						<td>
							<form:input path="authNm"/>
							<span class="form_error" data-path="authNm"><form:errors path="authNm" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="note"><spring:message code="authVO.note" /></label></th>
						<td>
							<form:input path="note"/>
							<span class="form_error" data-path="note"><form:errors path="note" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="useYn"><spring:message code="menuVO.useYn" /></label></th>
						<td>
							<form:radiobutton path="useYn" value="Y" label="Y"/>
							<form:radiobutton path="useYn" value="N" label="N"/>
							<span class="form_error" data-path="useYn"><form:errors path="useYn"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="authMap">MAP LIST</label></th>
						<td>
							<a href="javascript:fn_addMapList();" class="button"><spring:message code="button.add"/></a>
							<div class="scroll_table_wrap">
								<table class="list_table ty2">
									<colgroup>
										<col style="width:10%;"/>
										<col style="width:18%;"/>
										<col style="width:18%;"/>
										<col style="width:18%;"/>
										<col style="width:18%;"/>
										<col style="width:18%;"/>
									</colgroup>
									<thead>
										<tr>
											<th class="tc">No.</th>
											<th class="tc"><spring:message code="authMapVO.authDvcd" /></th>
											<th class="tc"><spring:message code="authMapVO.userDvcd" /></th>
											<th class="tc"><spring:message code="authMapVO.workDvcd" /></th>
											<th class="tc"><spring:message code="authMapVO.statDvcd" /></th>
											<th class="tc"><spring:message code="button.remove" /></th>
										</tr>
									</thead>
									<tbody id="mapList"></tbody>
							  	</table>
							</div>
						</td>
					</tr>
					
					<tr>
						<th><label for="">부서</label></th>
						<td>
							<a href="javascript:fn_addDeptList();" class="button"><spring:message code="button.add"/></a>
							<div class="scroll_table_wrap">
								<table class="list_table ty2">
									<colgroup>
										<col style="width:10%;"/>
										<col style="width:15%;"/>
										<col style="width:30%;"/>
										<col style="width:15%;"/>
										<col style="width:10%;"/>
									</colgroup>
									<thead>
										<tr>
											<th class="tc">No.</th>
											<th class="tc"><spring:message code="authDeptVO.authDvcd" /></th>
											<th class="tc"><spring:message code="authDeptVO.deptCd" /></th>
											<th class="tc"><spring:message code="authDeptVO.deptNm" /></th>
											<th class="tc"><spring:message code="button.remove" /></th>
										</tr>
									</thead>
									<tbody id="deptList"></tbody>
							  	</table>
							</div>
						</td>
					</tr>
				</table>
				
			   	<% /** 이중방지 토큰 */ %>
				<double-submit:preventer/>		
			</form:form>
		</div>
	</div>
</div>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="authVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" defer="defer">

	var mapListCount = 0;
	var mapListId = 0;

	//권한분류 목록
	var authDvcdList = {
		<c:forEach var="authDvcd" items="${authDvcdList}" varStatus="i">
			<c:if test="${i.index > 0}">,</c:if>
			<c:out value='${authDvcd.authDvcd}'/> : {authDvcd : "<c:out value='${authDvcd.authDvcd}'/>", authDvnm : "<c:out value='${authDvcd.authDvnm}'/>"}
		</c:forEach>
	};

	//데이터 변경여부 확인 변수
	var is_change = false;
	
	//현재 권한 저장 후 같은 권한인 경우 조회 생략
	var present_auth_cd = '<c:out value="${authVO.authCd}"/>';
	var present_auth_info = null;
	
	$(document).ready(function(){
		
		$("input,select,textarea,checkbox,radio").change(function(){
			is_change = true;
		});
		
		if(gf_isNull(present_auth_cd)){
			present_auth_info = null;
			present_auth_cd = "0";
			$(".tree_item[data-id='0']").addClass("present");
			fn_resetInput();
		} else {
			$(".tree_item[data-id='"+present_auth_cd+"']").addClass("present");	
			fn_resetInput();
			document.detailForm.authCd.value = '<c:out value="${authVO.authCd}"/>';
			document.detailForm.authNm.value = '<c:out value="${authVO.authNm}"/>';
			document.detailForm.note.value = '<c:out value="${authVO.note}"/>';
			
			$('#detailForm input[name="useYn"]').filter('[value="<c:out value="${authVO.useYn}"/>"]').prop("checked", true);
		}
	});
	
	//트리 구조 처리
	$(".tree_item").click(function(){
		//값이 변경된경우 confirm
		if (is_change || $(".tree_item[data-id='tempCreateCode']").length > 0){
			var msg = confirm('<spring:message code="message.confirm.nosave" />');
			if(msg == false){
				return false;
			}
		}
		$(".tree_item[data-id='tempCreateCode']").remove();
		
		var cd = $(this).attr("data-id");		
		$(".tree_item").removeClass("present");
		$(this).addClass("present");
		
		//ROOT인 경우 초기화
		if(cd == "0"){
			present_auth_info = null;
			present_auth_cd = "0";
			fn_resetInput();
		} else {
			fn_getAuthInfo(cd);
		}
	});
	
	//코드 정보를 조회한다.
	function fn_getAuthInfo(cd){		
		if(present_auth_cd != cd) {
			mapListCount = 0;
			mapListId = 0;
			present_auth_cd = cd;
			
			gf_ajax({
				url : "/auth/view.json",
				type : "POST",
				data : { cd : cd },
			}).then(function(response){
				present_auth_info = response;
				
				//FLAG
				document.detailForm.registerFlag.value = "U";
				
				//초기화
				fn_resetInput();
				
				//조회 결과 SET
				document.detailForm.authCd.value = response.authCd;
				document.detailForm.authNm.value = response.authNm;
				document.detailForm.note.value = response.note;
				
				$('#detailForm input[name="useYn"]').filter("[value="+response.useYn+"]").prop("checked", true);
	
				var str = "";
				if(response.mapList != null){
					str = "";
					$.each(response.mapList, function(i, item){
						mapListCount++;
						mapListId++;
						str += '<tr data-id="'+mapListId+'">';
						str += '<td class="tc">';
						str += '<span class="cnt">'+mapListCount+'</span>';
						str += '<input type="hidden" name="authDvcd" value="'+item.authDvcd+'"/>';
						str += '<input type="hidden" name="userDvcd" value="'+item.userDvcd+'"/>';
						str += '<input type="hidden" name="userDvnm" value="'+item.userDvnm+'"/>';
						str += '<input type="hidden" name="workDvcd" value="'+item.workDvcd+'"/>';
						str += '<input type="hidden" name="workDvnm" value="'+item.workDvnm+'"/>';
						str += '<input type="hidden" name="statDvcd" value="'+item.statDvcd+'"/>';
						str += '<input type="hidden" name="statDvnm" value="'+item.statDvnm+'"/>';
						str += '</td>';
						str += '<td class="tc">'+authDvcdList[item.authDvcd].authDvnm+'</td>';
						str += '<td class="tc">'+item.userDvnm+'</td>';
						str += '<td class="tc">'+item.workDvnm+'</td>';
						str += '<td class="tc">'+item.statDvnm+'</td>';
						str += '<td class="tc"><a href="javascript:fn_removeMapList(\''+mapListId+'\');" class="table_btn"><spring:message code="button.remove" /></a></td>'
						str += '</tr>';
					});
					$("#mapList").html(str);
				}
				
				if(response.deptList != null){
					str = "";
					var count = 0;
					$.each(response.deptList, function(i, item){
						count++;
						str += '<tr data-id="'+item.authDvcd+'_'+item.deptCd+'">';
						str += '<td class="tc">';
						str += '<span class="cnt">'+count+'</span>';
						str += '<input type="hidden" name="deptDvcd" value="'+item.authDvcd+'"/>';
						str += '<input type="hidden" name="deptCd" value="'+item.deptCd+'"/>';
						str += '<input type="hidden" name="deptNm" value="'+item.deptNm+'"/>';
						str += '</td>';
						str += '<td class="tc">'+authDvcdList[item.authDvcd].authDvnm+'</td>';
						str += '<td class="tc">'+item.deptCd+'</td>';
						str += '<td class="tc">'+item.deptNm+'</td>';
						str += '<td class="tc"><a href="javascript:fn_removeDeptList(\''+item.authDvcd+'_'+item.deptCd+'\');" class="table_btn"><spring:message code="button.remove" /></a></td>'
						str += '</tr>';
					});
					$("#deptList").html(str);
				}
				
				is_change = false;
			});	
		}
	};
	
	//등록
	function fn_registerView() {
		
		//값이 변경된경우 confirm
		if (is_change || $(".tree_item[data-id='tempCreateCode']").length > 0){
			var msg = confirm('<spring:message code="message.confirm.nosave" />');
			if(msg == false){
				return false;
			}
		}

		mapListCount = 0;
		mapListId = 0;
		
		$(".tree_item[data-id='tempCreateCode']").remove();
		
		//FLAG
		document.detailForm.registerFlag.value = "C";
		
		//초기화
		fn_resetInput();
		
		//html append
		var str = "";
		str += '<ul><li>';
		str += '<a href="#open_tree" class="tree_item" data-id="tempCreateCode">';
		str += 'NEW';
		str += '</a>';
		str += '</li></ul>';
		$(".tree_list").append(str);
		$(".tree_item").removeClass('present');
		$(".tree_item[data-id='tempCreateCode']").addClass('present');
		
		//코드 입력
		$('#detailForm input[name="authCd"]').prop("readonly", false);
		
		$("#authMapPopBtn").attr("data-id", "tempCreateCode");
		
		present_auth_cd = "0";
		present_auth_info = null;
		
		is_change = false;
		
		document.detailForm.authCd.focus();
	}

	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (frm.registerFlag.value != "C" && frm.registerFlag.value != "U"){
			return;
		} else {
			if (!validateAuthVO(frm)) {
				return;
			} else {	
				var msg = confirm('<spring:message code="message.confirm.save" />');
				if(msg == true){					
					if (frm.registerFlag.value == "C") {
						gf_ajax({
							url : "/auth/isCdExist.json",
							type : "POST",
							data : { cd : frm.authCd.value },
						}).then(function(response){
							var isExist = response;
							if(!isExist){
								frm.action = GV_PRESENT_PATH + "/registerAction.do";
								frm.submit();
							} else {
								alert('<spring:message code="message.alert.existdata" />');
								frm.authCd.focus()
							}
						});		
					} else if (frm.registerFlag.value == "U"){						
						frm.action = GV_PRESENT_PATH + "/modifyAction.do";
						frm.submit();						
					} else {						
						frm.action = "";
						return;						
					}
				}			
			}
		}
	}
	
	//삭제
	function fn_removeAction() {
		if (document.detailForm.registerFlag.value != "U"){
			alert('<spring:message code="message.alert.nosave" />');
			return;
		}		
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}
	
	//값 초기화
	function fn_resetInput(){		
		document.detailForm.authCd.value = '';
		document.detailForm.authNm.value = '';
		document.detailForm.note.value = '';
		
		$('#detailForm input[name="useYn"]').removeAttr('checked');
		
		$('#detailForm input[name="authCd"]').prop("readonly", true);
		
		$(".form_error").html("");
		
		$("#mapList").html("");
		$("#authMapPopBtn").attr("data-id", "");
	}
	
	function fn_addMapList() {
		mapListCount++;
		mapListId++;
		var options = "";
		var authDvcd = "";
		$.each(authDvcdList, function(key, value){
			if(gf_isNull(authDvcd)) authDvcd = key;
			options += '<option value="'+value.authDvcd+'">'+value.authDvnm+'</option>';
		});	
		var str = "";
		str += '<tr data-id="'+mapListId+'">';
		str += '<td class="tc">';
		str += '<span class="cnt">'+mapListCount+'</span>';
		str += '<input type="hidden" name="authDvcd" class="authDvcd" value=""/>';
		str += '<input type="hidden" name="userDvcd" class="userDvcd" value=""/>';
		str += '<input type="hidden" name="userDvnm" class="userDvnm" value=""/>';
		str += '<input type="hidden" name="workDvcd" class="workDvcd" value=""/>';
		str += '<input type="hidden" name="workDvnm" class="workDvnm" value=""/>';
		str += '<input type="hidden" name="statDvcd" class="statDvcd" value=""/>';
		str += '<input type="hidden" name="statDvnm" class="statDvnm" value=""/>';
		str += '</td>';
		str += '<td class="tc">';
		str += '<select class="selectAuthDvcd changeAuth">';
		str += options;
		str += '</select>'
		str += '</td>';
		str += '<td class="tc">';
		str += '<select class="selectUserDvcd changeAuth"></select>';
		str += '</td>';
		str += '<td class="tc">';
		str += '<select class="selectWorkDvcd changeAuth"></select>';
		str += '</td>';
		str += '<td class="tc">';
		str += '<select class="selectStatDvcd changeAuth"></select>';
		str += '</td>';
		str += '<td class="tc"><a href="javascript:fn_removeMapList(\''+mapListId+'\');" class="table_btn"><spring:message code="button.remove" /></a></td>'
		str += '</tr>';
		$("#mapList").append(str);
		fn_changeSelectAuth(mapListId);
	}
	
	$(document).on("change", ".changeAuth", function(){
		var id = $(this).closest('tr').attr('data-id');
		fn_changeSelectAuth(id);
	});
	
	function fn_changeSelectAuth(id){
		
		var parentObj = $("#mapList tr[data-id='"+id+"']");
		var authDvcd = parentObj.find(".authDvcd").val();
		var userDvcd = parentObj.find(".userDvcd").val();
		var workDvcd = parentObj.find(".workDvcd").val();
		var statDvcd = parentObj.find(".statDvcd").val();
		
		var selectAuthDvcd = parentObj.find(".selectAuthDvcd").val();
		var selectUserDvcd = parentObj.find(".selectUserDvcd").val();
		var selectWorkDvcd = parentObj.find(".selectWorkDvcd").val();
		var selectStatDvcd = parentObj.find(".selectStatDvcd").val();
		
		var type = "";
		var options = "";
		if(authDvcd != selectAuthDvcd){
			gf_ajax({
				url : "/auth/authList.json",
				type : "POST",
				contentType :  "application/json",
				data : {type : "USER_DVCD", authDvcd : selectAuthDvcd},
			}).then(function(response){
				parentObj.find(".authDvcd").val(selectAuthDvcd);
				parentObj.find(".userDvcd").val("");
				parentObj.find(".userDvnm").val("");
				parentObj.find(".workDvcd").val("");
				parentObj.find(".workDvnm").val("");
				parentObj.find(".statDvcd").val("");
				parentObj.find(".statDvnm").val("");
				parentObj.find(".selectStatDvcd").html("");
				parentObj.find(".selectWorkDvcd").html("");
				options += '<option value="" selected>--선택--</option>';
				options += '<option value="ALL">전체</option>';
				if(response != null){
					$.each(response, function(i, item){
						options += '<option value="'+item.userDvcd+'">'+item.userDvnm+'</option>';
					});
				}
				parentObj.find(".selectUserDvcd").html(options);
			});	
		} else if(userDvcd != selectUserDvcd){
			if(selectUserDvcd != "ALL"){
				gf_ajax({
					url : "/auth/authList.json",
					type : "POST",
					contentType :  "application/json",
					data : {type : "WORK_DVCD", authDvcd : selectAuthDvcd, userDvcd : selectUserDvcd},
				}).then(function(response){
					parentObj.find(".userDvcd").val(selectUserDvcd);
					if(!gf_isNull(selectUserDvcd)){
						parentObj.find(".userDvnm").val(parentObj.find("select.selectUserDvcd option:selected").text());
					} else {
						parentObj.find(".userDvnm").val("");
					}
					parentObj.find(".workDvcd").val("");
					parentObj.find(".workDvnm").val("");
					parentObj.find(".statDvcd").val("");
					parentObj.find(".statDvnm").val("");
					parentObj.find(".selectStatDvcd").html("");
					options += '<option value="" selected>--선택--</option>';
					options += '<option value="ALL">전체</option>';
					if(response != null){
						$.each(response, function(i, item){
							options += '<option value="'+item.workDvcd+'">'+item.workDvnm+'</option>';
						});
					}
					parentObj.find(".selectWorkDvcd").html(options);
				});
			} else {
				parentObj.find(".userDvcd").val("ALL");
				parentObj.find(".userDvnm").val("전체");
				parentObj.find(".workDvcd").val("ALL");
				parentObj.find(".workDvnm").val("전체");
				parentObj.find(".statDvcd").val("ALL");
				parentObj.find(".statDvnm").val("전체");
				parentObj.find(".selectWorkDvcd").html("");
				parentObj.find(".selectStatDvcd").html("");
			}
		} else if(workDvcd != selectWorkDvcd){
			if(selectWorkDvcd != "ALL"){
				gf_ajax({
					url : "/auth/authList.json",
					type : "POST",
					contentType :  "application/json",
					data : {type : "STAT_DVCD", authDvcd : selectAuthDvcd, userDvcd : selectUserDvcd, workDvcd : selectWorkDvcd},
				}).then(function(response){
					parentObj.find(".workDvcd").val(selectWorkDvcd);				
					if(!gf_isNull(selectWorkDvcd)){
						parentObj.find(".workDvnm").val(parentObj.find("select.selectWorkDvcd option:selected").text());
					} else {
						parentObj.find(".workDvnm").val("");
					}
					options += '<option value="" selected>--선택--</option>';
					options += '<option value="ALL">전체</option>';
					if(response != null){
						$.each(response, function(i, item){
							options += '<option value="'+item.statDvcd+'">'+item.statDvnm+'</option>';
						});
					}
					parentObj.find(".selectStatDvcd").html(options);
				});
			} else {
				parentObj.find(".workDvcd").val("ALL");
				parentObj.find(".workDvnm").val("전체");
				parentObj.find(".statDvcd").val("ALL");
				parentObj.find(".statDvnm").val("전체");
				parentObj.find(".selectStatDvcd").html("");
			}
		} else if(statDvcd != selectStatDvcd){ 
			if(selectStatDvcd != "ALL"){
				parentObj.find(".statDvcd").val(selectStatDvcd);				
				if(!gf_isNull(selectStatDvcd)){
					parentObj.find(".statDvnm").val(parentObj.find("select.selectStatDvcd option:selected").text());
				} else {
					parentObj.find(".statDvnm").val("");
				}
			} else {
				parentObj.find(".statDvcd").val("ALL");
				parentObj.find(".statDvnm").val("전체");
			}
		}
	}
	
	function fn_removeMapList(id) {
		mapListCount = 0;
		$("#mapList tr[data-id='"+id+"']").remove();
		$.each($("#mapList tr"), function(i, item){
			mapListCount++;
			$(item).find(".cnt").text(mapListCount);
		});
	}
	
	function fn_addDeptList(){
		var _width = '800';
	    var _height = '600';
	    var _left = Math.ceil(( window.screen.width - _width )/2);
	    var _top = Math.ceil(( window.screen.width - _height )/2); 		 
	    window.open('/sys/auth/deptList.do', 'auth-pop', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
	}
	
	function fn_setDeptList(list){
		var str = "";
		if(list != null){
			var count = $("#deptList tr").length;
			$.each(list, function(i, item){
				count++;
				str += '<tr data-id="'+item.authDvcd+'_'+item.deptCd+'">';
				str += '<td class="tc">';
				str += '<span class="cnt">'+count+'</span>';
				str += '<input type="hidden" name="deptDvcd" value="'+item.authDvcd+'"/>';
				str += '<input type="hidden" name="deptCd" value="'+item.deptCd+'"/>';
				str += '<input type="hidden" name="deptNm" value="'+item.deptNm+'"/>';
				str += '</td>';
				str += '<td class="tc">'+authDvcdList[item.authDvcd].authDvnm+'</td>';
				str += '<td class="tc">'+item.deptCd+'</td>';
				str += '<td class="tc">'+item.deptNm+'</td>';
				str += '<td class="tc"><a href="javascript:fn_removeDeptList(\''+item.authDvcd+'_'+item.deptCd+'\');" class="table_btn"><spring:message code="button.remove" /></a></td>'
				str += '</tr>';
			});
		}
		$("#deptList").append(str);
	}
	
	function fn_removeDeptList(id) {
		var count = 0;
		$("#deptList tr[data-id='"+id+"']").remove();
		$.each($("#deptList tr"), function(i, item){
			count++;
			$(item).find(".cnt").text(count);
		});
	}
	
</script>