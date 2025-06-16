<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 공통코드 목록 화면
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
							<a href="#open_tree" class="tree_item folder open" data-id="0">공통코드</a>
							<ul>
								<c:forEach var="item" items="${list }" varStatus="i">
								
									<% /** 현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
									<c:choose>
										<c:when test="${i.index eq 0}"></c:when>
										<c:when test="${item.cdLevel > treeLevel}">
											<c:out value='<ul>' escapeXml='false'/>
										</c:when>
										<c:when test="${item.cdLevel < treeLevel}">
											<c:out value='</li>' escapeXml="false"/>
											<c:forEach begin="${item.cdLevel }" end="${treeLevel - 1}" step="1">
												<c:out value='</ul></li>' escapeXml="false"/>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:out value='</li>' escapeXml="false"/>
										</c:otherwise>
									</c:choose>
									<c:out value='<li>' escapeXml="false"/>
									<% /** //현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
									
									<a href="#open_tree" class="tree_item<c:if test='${item.childCnt > 0 }'> folder open</c:if>" data-id="<c:out value='${item.cd }' />">
										<c:out value='${item.cdNm }' />(<c:out value='${item.cd }' />)
									</a>
									
									<% /** 트리 구조를 위한 LEVEL 변수 */ %>
									<c:set var="treeLevel" value="${item.cdLevel}"/>
									
									<% /** 마지막인경우 모두 닫는다 */ %>
									<c:if test="${i.last}">
										<c:out value='</li>' escapeXml="false"/>
										<c:if test="${item.cdLevel > 1 }">
											<c:forEach begin="1" end="${item.cdLevel - 1}" step="1">
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
<form:form modelAttribute
			
				<input type="hidden" name="registerFlag" value="C"/>
			
				<table class="detail_table">
					<caption>공통코드 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><label for="cd"><spring:message code="commCdVO.cd" /></label></th>
						<td>
							<form:input path="cd"/>
			       			<span class="form_error" data-path="cd"><form:errors path="cd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="hiCd"><spring:message code="commCdVO.hiCd" /></label></th>
						<td>
							<form:input path="hiCd" readonly="true"/>
							<span class="form_error" data-path="hiCd"><form:errors path="hiCd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="cdNm"><spring:message code="commCdVO.cdNm" /></label></th>
						<td>
							<form:input path="cdNm" cssClass="w_full"/>
							<span class="form_error" data-path="cdNm"><form:errors path="cdNm"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="cdAbbrNm"><spring:message code="commCdVO.cdAbbrNm" /></label></th>
						<td>
							<form:input path="cdAbbrNm"/>
							<span class="form_error" data-path="cdAbbrNm"><form:errors path="cdAbbrNm"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="cdEngNm"><spring:message code="commCdVO.cdEngNm" /></label></th>
						<td>
							<form:input path="cdEngNm" cssClass="w_full"/>
							<span class="form_error" data-path="cdEngNm"><form:errors path="cdEngNm"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="cdEngAbbrNm"><spring:message code="commCdVO.cdEngAbbrNm" /></label></th>
						<td>
							<form:input path="cdEngAbbrNm"/>
							<span class="form_error" data-path="cdEngAbbrNm"><form:errors path="cdEngAbbrNm"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="sortOrd"><spring:message code="commCdVO.sortOrd" /></label></th>
						<td>
							<form:input path="sortOrd"/>
							<span class="form_error" data-path="sortOrd"><form:errors path="sortOrd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="note"><spring:message code="commCdVO.note" /></label></th>
						<td>
							<form:textarea path="note"/>
							<span class="form_error" data-path="note"><form:errors path="note"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="useYn"><spring:message code="commCdVO.useYn" /></label></th>
						<td>
							<form:radiobutton path="useYn" value="Y" label="Y"/>
							<form:radiobutton path="useYn" value="N" label="N"/>
							<span class="form_error" data-path="useYn"><form:errors path="useYn"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="editPermYn"><spring:message code="commCdVO.editPermYn" /></label></th>
						<td>
							<form:radiobutton path="editPermYn" value="Y" label="Y"/>
							<form:radiobutton path="editPermYn" value="N" label="N"/>
							<span class="form_error" data-path="editPermYn"><form:errors path="editPermYn"/></span>
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
<validator:javascript formName="commCdVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" defer="defer">

	//데이터 변경여부 확인 변수
	var is_change = false;
	
	//현재 코드 저장 후 같은 코드인 경우 조회 생략
	var present_comm_cd = '<c:out value="${commCdVO.cd}"/>';
	var present_comm_cd_info = null;
	
	$(document).ready(function(){
		$("input,select,textarea,checkbox,radio").change(function(){
			is_change = true;
		});
		
		if(gf_isNull(present_comm_cd)){
			present_comm_cd_info = null;
			present_comm_cd = "0";
			$(".tree_item[data-id='0']").addClass("present");
			fn_resetInput();
		} else {
			$(".tree_item[data-id='"+present_comm_cd+"']").addClass("present");
			fn_resetInput();
			document.detailForm.cd.value = '<c:out value="${commCdVO.cd}"/>';
			document.detailForm.hiCd.value = '<c:out value="${commCdVO.hiCd}"/>';
			document.detailForm.cdNm.value = '<c:out value="${commCdVO.cdNm}"/>';
			document.detailForm.cdAbbrNm.value = '<c:out value="${commCdVO.cdAbbrNm}"/>';
			document.detailForm.cdEngNm.value = '<c:out value="${commCdVO.cdEngNm}"/>';
			document.detailForm.cdEngAbbrNm.value = '<c:out value="${commCdVO.cdEngAbbrNm}"/>';
			document.detailForm.sortOrd.value = '<c:out value="${commCdVO.sortOrd}"/>';
			document.detailForm.note.value = '<c:out value="${commCdVO.note}"/>';	
			$('#detailForm input[name="useYn"]').filter('[value="<c:out value="${commCdVO.useYn}"/>"]').prop("checked", true);
			$('#detailForm input[name="editPermYn"]').filter('[value="<c:out value="${commCdVO.editPermYn}"/>"]').prop("checked", true);
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
			present_comm_cd_info = null;
			present_comm_cd = "0";
			fn_resetInput();
		} else {
			fn_getCommCdInfo(cd);
		}
	});
	
	//코드 정보를 조회한다.
	function fn_getCommCdInfo(cd){		
		if(present_comm_cd != cd) {			
			present_comm_cd = cd;
			gf_ajax({
				url : "/commCd/view.json",
				type : "POST",
				data : { cd : cd },
			}).then(function(response){
				present_comm_cd_info = response;
				
				//FLAG
				document.detailForm.registerFlag.value = "U";
				
				//초기화
				fn_resetInput();
				
				//조회 결과 SET
				document.detailForm.cd.value = response.cd;
				document.detailForm.hiCd.value = response.hiCd;
				document.detailForm.cdNm.value = response.cdNm;
				document.detailForm.cdAbbrNm.value = response.cdAbbrNm;
				document.detailForm.cdEngNm.value = response.cdEngNm;
				document.detailForm.cdEngAbbrNm.value = response.cdEngAbbrNm;
				document.detailForm.sortOrd.value = response.sortOrd;
				document.detailForm.note.value = response.note;
				
				$('#detailForm input[name="useYn"]').filter("[value="+response.useYn+"]").prop("checked", true);
				$('#detailForm input[name="editPermYn"]').filter("[value="+response.editPermYn+"]").prop("checked", true);
				
				//편집허용불가일 경우
				if(response.editPermYn == "N"){
					$('#detailForm input[name="cdNm"]').prop("readonly", true);
					$('#detailForm input[name="cdAbbrNm"]').prop("readonly", true);
					$('#detailForm input[name="cdEngNm"]').prop("readonly", true);
					$('#detailForm input[name="cdEngAbbrNm"]').prop("readonly", true);
					$('#detailForm input[name="sortOrd"]').prop("readonly", true);
					$('#detailForm input[name="note"]').prop("readonly", true);
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
		$(".tree_item[data-id='tempCreateCode']").remove();
		
		//FLAG
		document.detailForm.registerFlag.value = "C";
		
		//초기화
		fn_resetInput();
		
		//현재 선택된 트리의 정보를 가져와 하위메뉴로 등록된다.
		var str = "";
		var parentObj = null;
		if(present_comm_cd_info != null && present_comm_cd != "0"){			
			document.detailForm.hiCd.value = present_comm_cd_info.cd;
			parentObj = $(".tree_item[data-id='"+present_comm_cd_info.cd+"']");
		} else {			
			document.detailForm.hiCd.value = null;
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
		$('#detailForm input[name="cd"]').prop("readonly", false);
		
		present_comm_cd = "0";
		present_comm_cd_info = null;
		
		is_change = false;
		
		document.detailForm.cd.focus();
	}

	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (frm.registerFlag.value != "C" && frm.registerFlag.value != "U"){
			return;
		} else {
			if (!validateCommCdVO(frm)) {
				return;
			} else {	
				var msg = confirm('<spring:message code="message.confirm.save" />');
				if(msg == true){					
					if (frm.registerFlag.value == "C"){	
						gf_ajax({
							url : "/commCd/isCdExist.json",
							type : "POST",
							data : { cd : frm.cd.value },
						}).then(function(response){
							var isExist = response;
							if(!isExist){
								frm.action = GV_PRESENT_PATH + "/registerAction.do";
								frm.submit();
							} else {
								alert('<spring:message code="message.alert.existdata" />');
								frm.cd.focus()
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
		if(present_comm_cd_info != null && present_comm_cd != "0"){			
			if(present_comm_cd_info.list.length > 0){
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
		document.detailForm.cd.value = "";
		document.detailForm.cdNm.value = "";		
		document.detailForm.hiCd.value = "";
		document.detailForm.cdAbbrNm.value = "";
		document.detailForm.cdEngNm.value = "";
		document.detailForm.cdEngAbbrNm.value = "";
		document.detailForm.sortOrd.value = 0;
		document.detailForm.note.value = "";
		
		$('#detailForm input[name="useYn"]').removeAttr('checked');
		$('#detailForm input[name="editPermYn"]').removeAttr('checked');
		
		$('#detailForm input[name="cd"]').prop("readonly", true);
		$('#detailForm input[name="cdNm"]').prop("readonly", false);
		$('#detailForm input[name="cdAbbrNm"]').prop("readonly", false);
		$('#detailForm input[name="cdEngNm"]').prop("readonly", false);
		$('#detailForm input[name="cdEngAbbrNm"]').prop("readonly", false);
		$('#detailForm input[name="sortOrd"]').prop("readonly", false);
		$('#detailForm input[name="note"]').prop("readonly", false);
		$(".form_error").html("");
	}
	
</script>