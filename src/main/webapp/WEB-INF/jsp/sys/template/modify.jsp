<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 템플릿관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	<div class="tab_wrap02">
		<div class="tab_btn">
			<ul>
				<li data-rel="info" class="active">
					<a href="javascript:fn_changeTab('info')">INFO</a>
				</li>
				<li data-rel="layout">
					<a href="javascript:fn_changeTab('layout')">LAYOUT</a>
				</li>
				<li data-rel="main">
					<a href="javascript:fn_changeTab('main')"><spring:message code="templateMgtVO.mainCode"/></a>
				</li>
				<li data-rel="sub">
					<a href="javascript:fn_changeTab('sub')"><spring:message code="templateMgtVO.subCode"/></a>
				</li>
				<li data-rel="login">
					<a href="javascript:fn_changeTab('login')"><spring:message code="templateMgtVO.loginCode"/></a>
				</li>
				<li data-rel="empty">
					<a href="javascript:fn_changeTab('empty')"><spring:message code="templateMgtVO.emptyCode"/></a>
				</li>
				<li data-rel="pop">
					<a href="javascript:fn_changeTab('pop')"><spring:message code="templateMgtVO.popCode"/></a>
				</li>
			</ul>
		</div>
		<div class="tab_content">
			<ul>
				<li data-rel="info" class="active">
					<table class="detail_table">
						<caption>템플릿관리 등록</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="templateCd"><spring:message code="templateMgtVO.templateCd"/></label></th>
							<td>
								<form:hidden path="templateCd"/>
								<c:out value='${templateMgtVO.templateCd }'/>
								<span class="form_error" data-path="templateCd"><form:errors path="templateCd"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="templateNm"><spring:message code="templateMgtVO.templateNm"/></label></th>
							<td>
								<form:input path="templateNm"/>
								<span class="form_error" data-path="templateNm"><form:errors path="templateNm"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="note"><spring:message code="templateMgtVO.note"/></label></th>
							<td>
								<form:input path="note"/>
								<span class="form_error" data-path="note"><form:errors path="note"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="useYn"><spring:message code="templateMgtVO.useYn" /></label></th>
							<td>
								<form:radiobutton path="useYn" value="Y" label="Y"/>
								<form:radiobutton path="useYn" value="N" label="N"/>
								<span class="form_error" data-path="useYn"><form:errors path="useYn" /></span>
							</td>
						</tr>
					</table>
				</li>
				<li data-rel="layout">
					<table class="detail_table">
						<caption>레이아웃</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="layoutHeader"><spring:message code="templateMgtVO.layoutHeader"/></label></th>
							<td>
								<form:textarea path="layoutHeader"/>
								<span class="form_error" data-path="layoutHeader"><form:errors path="layoutHeader"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="layoutGnb"><spring:message code="templateMgtVO.layoutGnb"/></label></th>
							<td>
								<form:textarea path="layoutGnb"/>
								<span class="form_error" data-path="layoutGnb"><form:errors path="layoutGnb"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="layoutFooter"><spring:message code="templateMgtVO.layoutFooter"/></label></th>
							<td>
								<form:textarea path="layoutFooter"/>
								<span class="form_error" data-path="layoutFooter"><form:errors path="layoutFooter"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="layoutCss"><spring:message code="templateMgtVO.layoutCss"/></label></th>
							<td>
								<form:textarea path="layoutCss"/>
								<span class="form_error" data-path="layoutCss"><form:errors path="layoutCss"/></span>
							</td>
						</tr>
					</table>
				</li>
				<li data-rel="main">
					<table class="detail_table">
						<caption>메인페이지</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="mainCode"><spring:message code="templateMgtVO.mainCode"/></label></th>
							<td>
								<form:textarea path="mainCode"/>
								<span class="form_error" data-path="mainCode"><form:errors path="mainCode"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="mainCss"><spring:message code="templateMgtVO.mainCss"/></label></th>
							<td>
								<form:textarea path="mainCss"/>
								<span class="form_error" data-path="mainCss"><form:errors path="mainCss"/></span>
							</td>
						</tr>
					</table>				
				</li>
				<li data-rel="sub">
					<table class="detail_table">
						<caption>메인페이지</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="subCode"><spring:message code="templateMgtVO.subCode"/></label></th>
							<td>
								<form:textarea path="subCode"/>
								<span class="form_error" data-path="subCode"><form:errors path="subCode"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="subCss"><spring:message code="templateMgtVO.subCss"/></label></th>
							<td>
								<form:textarea path="subCss"/>
								<span class="form_error" data-path="subCss"><form:errors path="subCss"/></span>
							</td>
						</tr>
					</table>				
				</li>
				<li data-rel="login">
					<table class="detail_table">
						<caption>메인페이지</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="loginCode"><spring:message code="templateMgtVO.loginCode"/></label></th>
							<td>
								<form:textarea path="loginCode"/>
								<span class="form_error" data-path="loginCode"><form:errors path="loginCode"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="loginCss"><spring:message code="templateMgtVO.loginCss"/></label></th>
							<td>
								<form:textarea path="loginCss"/>
								<span class="form_error" data-path="loginCss"><form:errors path="loginCss"/></span>
							</td>
						</tr>
					</table>				
				</li>
				<li data-rel="empty">
					<table class="detail_table">
						<caption>메인페이지</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="emptyCode"><spring:message code="templateMgtVO.emptyCode"/></label></th>
							<td>
								<form:textarea path="emptyCode"/>
								<span class="form_error" data-path="emptyCode"><form:errors path="emptyCode"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="emptyCss"><spring:message code="templateMgtVO.emptyCss"/></label></th>
							<td>
								<form:textarea path="emptyCss"/>
								<span class="form_error" data-path="emptyCss"><form:errors path="emptyCss"/></span>
							</td>
						</tr>
					</table>				
				</li>
				<li data-rel="pop">
					<table class="detail_table">
						<caption>메인페이지</caption>
						<colgroup>
				   			<col width="150"/>
				   			<col width="?"/>
				   		</colgroup>
						<tr>
							<th><label for="popCode"><spring:message code="templateMgtVO.popCode"/></label></th>
							<td>
								<form:textarea path="popCode"/>
								<span class="form_error" data-path="popCode"><form:errors path="popCode"/></span>
							</td>
						</tr>
						<tr>
							<th><label for="popCss"><spring:message code="templateMgtVO.popCss"/></label></th>
							<td>
								<form:textarea path="popCss"/>
								<span class="form_error" data-path="popCss"><form:errors path="popCss"/></span>
							</td>
						</tr>
					</table>				
				</li>
			</ul>
		</div>
	</div>
   	
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
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${templateMgtVO.templateCd}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="templateMgtVO" staticJavascript="false" xhtml="true" cdata="false"/>
<c:import url="/WEB-INF/jsp/cmmn/code_mirror.jsp"></c:import>
<script type="text/javaScript" defer="defer">
	
	var layoutHeader;
	var layoutFooter;
	var layoutGnb;
	var layoutCss;
	
	var mainCode;
	var subCode;
	var loginCode;
	var emptyCode;
	var popCode;
	
	var mainCss;
	var subCss;
	var loginCss;
	var emptyCss;
	var popCss;
	
	$(function(){
		
		layoutHeader = ComCodeMirror.edit($("#layoutHeader"));
		layoutFooter = ComCodeMirror.edit($("#layoutFooter"));
		layoutGnb = ComCodeMirror.edit($("#layoutGnb"));
		layoutCss = ComCodeMirror.edit($("#layoutCss"));
		
		mainCode = ComCodeMirror.edit($("#mainCode"));
		subCode = ComCodeMirror.edit($("#subCode"));
		loginCode = ComCodeMirror.edit($("#loginCode"));
		emptyCode = ComCodeMirror.edit($("#emptyCode"));
		popCode = ComCodeMirror.edit($("#popCode"));

		mainCss = ComCodeMirror.edit($("#mainCss"));
		subCss = ComCodeMirror.edit($("#subCss"));
		loginCss = ComCodeMirror.edit($("#loginCss"));
		emptyCss = ComCodeMirror.edit($("#emptyCss"));
		popCss = ComCodeMirror.edit($("#popCss"));
		
	});

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validateTemplateMgtVO(frm)) {
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
	
	//탭변경
	function fn_changeTab(tabId) {
		$(".tab_wrap02 .tab_btn ul li").removeClass("active");
		$(".tab_wrap02 .tab_content ul li").removeClass("active");
		$(".tab_wrap02 .tab_btn ul li[data-rel=" + tabId + "]").addClass("active");
		$(".tab_wrap02 .tab_content ul li[data-rel=" + tabId + "]").addClass("active");
		
		if(tabId == "layout"){
			layoutHeader.refresh();
			layoutFooter.refresh();
			layoutGnb.refresh();
			layoutCss.refresh();
		} else if(tabId == "main"){
			mainCode.refresh();
			mainCss.refresh();
		} else if(tabId == "sub"){
			subCode.refresh();
			subCss.refresh();
		} else if(tabId == "login"){
			loginCode.refresh();
			loginCss.refresh();
		} else if(tabId == "empty"){
			emptyCode.refresh();
			emptyCss.refresh();
		} else if(tabId == "pop"){
			popCode.refresh();
			popCss.refresh();
		}
	}
	
</script>