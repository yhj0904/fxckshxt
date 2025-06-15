<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 부서코드 목록 화면
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
			<form:form id="listForm" name="listForm" method="post" autocomplete="off">
				<div class="tree_wrap">
					<% /** 트리 구조를 위한 LEVEL 변수 */ %>
					<c:set var="treeLevel" value="1"/>
					<ul class="tree_list">
						<li>
							<a href="#open_tree" class="tree_item folder open" data-id="0">부서코드</a>
							<ul>
								<c:forEach var="item" items="${list }" varStatus="i">
								
									<% /** 현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
									<c:choose>
										<c:when test="${i.index eq 0}"></c:when>
										<c:when test="${item.deptLvl > treeLevel}">
											<c:out value='<ul>' escapeXml='false'/>
										</c:when>
										<c:when test="${item.deptLvl < treeLevel}">
											<c:out value='</li>' escapeXml="false"/>
											<c:forEach begin="${item.deptLvl }" end="${treeLevel - 1}" step="1">
												<c:out value='</ul></li>' escapeXml="false"/>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:out value='</li>' escapeXml="false"/>
										</c:otherwise>
									</c:choose>
									<c:out value='<li>' escapeXml="false"/>
									<% /** //현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
									
									<a href="#open_tree" class="tree_item<c:if test='${item.childCnt > 0 }'> folder open</c:if>" data-id="<c:out value='${item.deptCd }' />">
										<c:out value='${item.deptNmKor }' />(<c:out value='${item.deptCd }' />)
									</a>
									
									<% /** 트리 구조를 위한 LEVEL 변수 */ %>
									<c:set var="treeLevel" value="${item.deptLvl}"/>
									
									<% /** 마지막인경우 모두 닫는다 */ %>
									<c:if test="${i.last}">
										<c:out value='</li>' escapeXml="false"/>
										<c:if test="${item.deptLvl > 1 }">
											<c:forEach begin="1" end="${item.deptLvl - 1}" step="1">
												<c:out value="</ul></li>" escapeXml="false"/>
											</c:forEach>
										</c:if>
									</c:if>
									<% /** //마지막인경우 모두 닫는다 */ %>
								</c:forEach>
							</ul>
						</li>
					</ul>
			  	</div>
			</form:form>
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
			<form:form commandName="deptVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
			
				<input type="hidden" name="registerFlag" value="C"/>
			
				<table class="detail_table">
					<caption>부서코드 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
			   		<tr>
						<th><label for="deptCd"><spring:message code="deptVO.deptCd"/></label></th>
						<td>
							<form:input path="deptCd" readonly="true"/>
							<span class="form_error" data-path="deptCd"><form:errors path="deptCd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="hiDeptCd"><spring:message code="deptVO.hiDeptCd"/></label></th>
						<td>
							<form:input path="hiDeptCd" readonly="true"/>
							<span class="form_error" data-path="hiDeptCd"><form:errors path="hiDeptCd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="deptDvcd"><spring:message code="deptVO.deptDvcd"/></label></th>
						<td>
							<form:input path="deptDvcd"/>
							<span class="form_error" data-path="deptDvcd"><form:errors path="deptDvcd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="deptNmKor"><spring:message code="deptVO.deptNmKor"/></label></th>
						<td>
							<form:input path="deptNmKor"/>
							<span class="form_error" data-path="deptNmKor"><form:errors path="deptNmKor"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="deptNmEng"><spring:message code="deptVO.deptNmEng"/></label></th>
						<td>
							<form:input path="deptNmEng"/>
							<span class="form_error" data-path="deptNmEng"><form:errors path="deptNmEng"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="telNo"><spring:message code="deptVO.telNo"/></label></th>
						<td>
							<form:input path="telNo"/>
							<span class="form_error" data-path="telNo"><form:errors path="telNo"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="deptLvl"><spring:message code="deptVO.deptLvl"/></label></th>
						<td>
							<form:input path="deptLvl" readonly="true"/>
							<span class="form_error" data-path="deptLvl"><form:errors path="deptLvl"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="oldDeptCd"><spring:message code="deptVO.oldDeptCd"/></label></th>
						<td>
							<form:input path="oldDeptCd"/>
							<span class="form_error" data-path="oldDeptCd"><form:errors path="oldDeptCd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="useYn"><spring:message code="deptVO.useYn"/></label></th>
						<td>
							<form:radiobutton path="useYn" value="Y" label="Y"/>
							<form:radiobutton path="useYn" value="N" label="N"/>
							<span class="form_error" data-path="useYn"><form:errors path="useYn"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="sortOrd"><spring:message code="deptVO.sortOrd"/></label></th>
						<td>
							<form:input path="sortOrd"/>
							<span class="form_error" data-path="sortOrd"><form:errors path="sortOrd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="note"><spring:message code="deptVO.note"/></label></th>
						<td>
							<form:input path="note"/>
							<span class="form_error" data-path="note"><form:errors path="note"/></span>
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
<validator:javascript formName="deptVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" defer="defer">

	//데이터 변경여부 확인 변수
	var is_change = false;
	
	//현재 코드 저장 후 같은 코드인 경우 조회 생략
	var present_dept_cd = '<c:out value="${deptVO.deptCd}"/>';
	var present_dept_cd_info = null;
	
	$(document).ready(function(){
		$("input,select,textarea,checkbox,radio").change(function(){
			is_change = true;
		});
		
		if(gf_isNull(present_dept_cd)){
			present_dept_cd_info = null;
			present_dept_cd = "0";
			$(".tree_item[data-id='0']").addClass("present");
			fn_resetInput();
		} else {
			$(".tree_item[data-id='"+present_dept_cd+"']").addClass("present");
			fn_resetInput();
			
			document.detailForm.deptCd.value = '<c:out value="${deptVO.deptCd}"/>';
			document.detailForm.hiDeptCd.value = '<c:out value="${deptVO.hiDeptCd}"/>';
			document.detailForm.deptDvcd.value = '<c:out value="${deptVO.deptDvcd}"/>';
			document.detailForm.deptNmKor.value = '<c:out value="${deptVO.deptNmKor}"/>';
			document.detailForm.deptNmEng.value = '<c:out value="${deptVO.deptNmEng}"/>';
			document.detailForm.telNo.value = '<c:out value="${deptVO.telNo}"/>';
			document.detailForm.deptLvl.value = <c:out value="${deptVO.deptLvl}"/>;
			document.detailForm.oldDeptCd.value = '<c:out value="${deptVO.oldDeptCd}"/>';
			document.detailForm.sortOrd.value = <c:out value="${deptVO.sortOrd}"/>;
			document.detailForm.note.value = '<c:out value="${deptVO.note}"/>';
			
			$('#detailForm input[name="useYn"]').filter('[value="<c:out value="${deptVO.useYn}"/>"]').prop("checked", true);
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
		
		var deptCd = $(this).attr("data-id");		
		$(".tree_item").removeClass("present");
		$(this).addClass("present");
		
		//ROOT인 경우 초기화
		if(deptCd == "0"){
			present_dept_cd_info = null;
			present_dept_cd = "0";
			fn_resetInput();
		} else {
			fn_getDeptInfo(deptCd);
		}
	});
	
	//코드 정보를 조회한다.
	function fn_getDeptInfo(deptCd){		
		if(present_dept_cd != deptCd) {			
			present_dept_cd = deptCd;
			gf_ajax({
				url : "/dept/view.json",
				type : "POST",
				data : { deptCd : deptCd },
			}).then(function(response){
				present_dept_cd_info = response;
				
				//FLAG
				document.detailForm.registerFlag.value = "U";
				
				//초기화
				fn_resetInput();
				
				//조회 결과 SET
				document.detailForm.deptCd.value = response.deptCd;
				document.detailForm.hiDeptCd.value = response.hiDeptCd;
				document.detailForm.deptDvcd.value = response.deptDvcd;
				document.detailForm.deptNmKor.value = response.deptNmKor;
				document.detailForm.deptNmEng.value = response.deptNmEng;
				document.detailForm.telNo.value = response.telNo;
				document.detailForm.deptLvl.value = response.deptLvl;
				document.detailForm.oldDeptCd.value = response.oldDeptCd;
				document.detailForm.sortOrd.value = response.sortOrd;
				document.detailForm.note.value = response.note;
				
				$('#detailForm input[name="useYn"]').filter("[value="+response.useYn+"]").prop("checked", true);
									
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
		$(".tree_item[data-id='tempCreateCode']").remove();
		
		//FLAG
		document.detailForm.registerFlag.value = "C";
		
		//초기화
		fn_resetInput();
		
		//현재 선택된 트리의 정보를 가져와 하위메뉴로 등록된다.
		var str = "";
		var parentObj = null;
		if(present_dept_cd_info != null && present_dept_cd != "0"){			
			document.detailForm.deptLvl.value = present_dept_cd_info.deptLvl + 1;
			document.detailForm.hiDeptCd.value = present_dept_cd_info.dpetCd;
			parentObj = $(".tree_item[data-id='"+present_dept_cd_info.deptCd+"']");
		} else {			
			document.detailForm.deptLvl.value = 1;
			document.detailForm.hiDeptCd.value = null;
			parentObj = $(".tree_item[data-id='0']");
		}
		
		if(parentObj.next("ul").length > 0){
			str += '<li>';
			str += '<a href="#open_tree" class="tree_item" data-id="tempCreateCode">';
			str += 'NEW';
			str += '</a>';
			str += '</li>';
			parentObj.next("ul").append(str);
		} else {
			str += '<ul><li>';
			str += '<a href="#open_tree" class="tree_item" data-id="tempCreateCode">';
			str += 'NEW';
			str += '</a>';
			str += '</li></ul>';
			parentObj.parent("li").append(str);
		}
		$(".tree_item").removeClass('present');
		$(".tree_item[data-id='tempCreateCode']").addClass('present');
		
		//코드 입력
		$('#detailForm input[name="deptCd"]').prop("readonly", false);
		
		present_dept_cd = "0";
		present_dept_cd_info = null;
		
		is_change = false;
		
		document.detailForm.deptCd.focus();
	}

	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (frm.registerFlag.value != "C" && frm.registerFlag.value != "U"){
			return;
		} else {
			if (!validateDeptVO(frm)) {
				return;
			} else {	
				var msg = confirm('<spring:message code="message.confirm.save" />');
				if(msg == true){					
					if (frm.registerFlag.value == "C"){	
						gf_ajax({
							url : "/dept/isCdExist.json",
							type : "POST",
							data : { deptCd : frm.deptCd.value },
						}).then(function(response){
							var isExist = response;
							if(!isExist){
								frm.action = GV_PRESENT_PATH + "/registerAction.do";
								frm.submit();
							} else {
								alert('<spring:message code="message.alert.existdata" />');
								frm.deptCd.focus()
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
		if(present_dept_cd_info != null && present_dept_cd != "0"){
			if(!gf_isNull(present_dept_cd_info.list) && present_dept_cd_info.list.length > 0){
				alert('<spring:message code="message.alert.remove.parent" />');
				return
			}
		}		
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
		document.detailForm.deptCd.value = "";
		document.detailForm.hiDeptCd.value = "";
		document.detailForm.deptDvcd.value = "";
		document.detailForm.deptNmKor.value = "";
		document.detailForm.deptNmEng.value = "";
		document.detailForm.telNo.value = "";
		document.detailForm.deptLvl.value = 0;
		document.detailForm.oldDeptCd.value = "";
		document.detailForm.useYn.value = "";
		document.detailForm.sortOrd.value = 0;
		document.detailForm.note.value = "";
		
		$('#detailForm input[name="useYn"]').removeAttr('checked');
		
		$('#detailForm input[name="deptCd"]').prop("readonly", true);
		$(".form_error").html("");
	}
	
</script>