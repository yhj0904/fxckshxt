<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: code_view.jsp
 * @Description : 템플릿관리 백업상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
	<form:form commandName="templateCodeVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
		<form:hidden path="templateCd"/>
		<form:hidden path="seq"/>		
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
						<a href="javascript:fn_changeTab('main')"><spring:message code="templateCodeVO.mainCode"/></a>
					</li>
					<li data-rel="sub">
						<a href="javascript:fn_changeTab('sub')"><spring:message code="templateCodeVO.subCode"/></a>
					</li>
					<li data-rel="login">
						<a href="javascript:fn_changeTab('login')"><spring:message code="templateCodeVO.loginCode"/></a>
					</li>
					<li data-rel="empty">
						<a href="javascript:fn_changeTab('empty')"><spring:message code="templateCodeVO.emptyCode"/></a>
					</li>
					<li data-rel="pop">
						<a href="javascript:fn_changeTab('pop')"><spring:message code="templateCodeVO.popCode"/></a>
					</li>
				</ul>
			</div>
			<div class="tab_content">
				<ul>
					<li data-rel="info" class="active">						
						<table class="detail_table">
							<caption>템플릿 상세조회</caption>
							<colgroup>
					   			<col width="150"/>
					   			<col width="?"/>
					   		</colgroup>
					   		<tr>
								<th><spring:message code="templateCodeVO.templateCd"/></th>
								<td><c:out value="${templateCodeVO.templateCd}"/></td>
							</tr>
							<tr>
								<th><spring:message code="templateCodeVO.inptId"/></th>
								<td><c:out value="${templateCodeVO.inptId}"/></td>
							</tr>
							<tr>
								<th><spring:message code="templateCodeVO.inptIp"/></th>
								<td><c:out value="${templateCodeVO.inptIp}"/></td>
							</tr>
							<tr>
								<th><spring:message code="templateCodeVO.inptDttm"/></th>
								<td><c:out value="${templateCodeVO.inptDttm}"/></td>
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
								<th><label for="layoutHeader"><spring:message code="templateCodeVO.layoutHeader"/></label></th>
								<td>
									<form:textarea path="layoutHeader"/>
									<span class="form_error" data-path="layoutHeader"><form:errors path="layoutHeader"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="layoutGnb"><spring:message code="templateCodeVO.layoutGnb"/></label></th>
								<td>
									<form:textarea path="layoutGnb"/>
									<span class="form_error" data-path="layoutGnb"><form:errors path="layoutGnb"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="layoutFooter"><spring:message code="templateCodeVO.layoutFooter"/></label></th>
								<td>
									<form:textarea path="layoutFooter"/>
									<span class="form_error" data-path="layoutFooter"><form:errors path="layoutFooter"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="layoutCss"><spring:message code="templateCodeVO.layoutCss"/></label></th>
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
								<th><label for="mainCode"><spring:message code="templateCodeVO.mainCode"/></label></th>
								<td>
									<form:textarea path="mainCode"/>
									<span class="form_error" data-path="mainCode"><form:errors path="mainCode"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="mainCss"><spring:message code="templateCodeVO.mainCss"/></label></th>
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
								<th><label for="subCode"><spring:message code="templateCodeVO.subCode"/></label></th>
								<td>
									<form:textarea path="subCode"/>
									<span class="form_error" data-path="subCode"><form:errors path="subCode"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="subCss"><spring:message code="templateCodeVO.subCss"/></label></th>
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
								<th><label for="loginCode"><spring:message code="templateCodeVO.loginCode"/></label></th>
								<td>
									<form:textarea path="loginCode"/>
									<span class="form_error" data-path="loginCode"><form:errors path="loginCode"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="loginCss"><spring:message code="templateCodeVO.loginCss"/></label></th>
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
								<th><label for="emptyCode"><spring:message code="templateCodeVO.emptyCode"/></label></th>
								<td>
									<form:textarea path="emptyCode"/>
									<span class="form_error" data-path="emptyCode"><form:errors path="emptyCode"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="emptyCss"><spring:message code="templateCodeVO.emptyCss"/></label></th>
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
								<th><label for="popCode"><spring:message code="templateCodeVO.popCode"/></label></th>
								<td>
									<form:textarea path="popCode"/>
									<span class="form_error" data-path="popCode"><form:errors path="popCode"/></span>
								</td>
							</tr>
							<tr>
								<th><label for="popCss"><spring:message code="templateCodeVO.popCss"/></label></th>
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
		
		<% /** 이중방지 토큰 */ %>
		<double-submit:preventer/>
		
		<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	<% /** //검색조건 유지 */ %>  	
	</form:form>
	
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button restore_btn" href="javascript:fn_restoreView('<c:out value="${templateCodeVO.templateCd}" />', <c:out value="${templateCodeVO.seq}" />);"><spring:message code="button.restore" /></a>
				</li>
				<li>
					<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
				</li>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>

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
		document.detailForm.action = GV_PRESENT_PATH + "/code/list.do";
		document.detailForm.submit();
	}
	
	//복원
	function fn_restoreView(templateCd, seq) {
		var msg = confirm('<spring:message code="message.confirm.save" />');
		if(msg == true){
			fn_restoreAction(templateCd, seq);
		}
	}
	
	function fn_restoreAction(templateCd, seq){
		gf_ajax({
			url :  GV_PRESENT_PATH + "/code/modifyAction.json",
			type : "POST",
			data : {templateCd : templateCd, seq : seq},
		}).then(function(response){
			if(response){
				if(!gf_isNull(opener.parent.fn_refreshPage)){
					opener.parent.fn_refreshPage();
				}
				alert('<spring:message code="message.success" />');
				fn_closePop();
			} else {
				alert('<spring:message code="message.error" />');
			}
		});
	}
	
	function fn_closePop(){
		self.close();
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