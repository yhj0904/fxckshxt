<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 템플릿관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="templateMgtVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<table class="detail_table">
		<caption>템플릿 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
   		<tr>
			<th><spring:message code="templateMgtVO.templateCd"/></th>
			<td><c:out value="${templateMgtVO.templateCd}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.templateNm"/></th>
			<td><c:out value="${templateMgtVO.templateNm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.note"/></th>
			<td><c:out value="${templateMgtVO.note}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.useYn"/></th>
			<td><c:out value="${templateMgtVO.useYn}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.inptId"/></th>
			<td><c:out value="${templateMgtVO.inptId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.inptIp"/></th>
			<td><c:out value="${templateMgtVO.inptIp}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.inptDttm"/></th>
			<td><c:out value="${templateMgtVO.inptDttm}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.modiId"/></th>
			<td><c:out value="${templateMgtVO.modiId}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.modiIp"/></th>
			<td><c:out value="${templateMgtVO.modiIp}"/></td>
		</tr>
		<tr>
			<th><spring:message code="templateMgtVO.modiDttm"/></th>
			<td><c:out value="${templateMgtVO.modiDttm}"/></td>
		</tr>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a>
			</li>
			<li>
				<a class="button restore_btn" href="javascript:fn_restoreView();"><spring:message code="button.restore" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${templateMgtVO.templateCd }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
		document.detailForm.submit();
	}
	
	//수정
	function fn_modifyView() {
		document.detailForm.action = GV_PRESENT_PATH + "/modify.do";
		document.detailForm.submit();
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}
	
	//백업 목록
	function fn_restoreView(){
		var _width = '800';
	    var _height = '600';
	    var _left = Math.ceil(( window.screen.width - _width )/2);
	    var _top = Math.ceil(( window.screen.width - _height )/2); 		 
	    window.open(GV_PRESENT_PATH + '/code/list.do?templateCd=<c:out value="${templateMgtVO.templateCd }"/>', 'back-pop', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
	}
	
	function fn_refreshPage(){
		location.reload(true);
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