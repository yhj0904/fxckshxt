<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 컨텐츠관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="contentVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="contId"/>
	
	<table class="detail_table">
		<caption>컨텐츠관리 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="contentVO.contId" /></th>
			<td>
				<c:out value="${contentVO.contId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.contNm" /></th>
			<td>
				<c:out value="${contentVO.contNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.contCont" /></th>
			<td>
				<c:out value="${contentVO.contCont}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.contEngNm" /></th>
			<td>
				<c:out value="${contentVO.contEngNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.contEngCont" /></th>
			<td>
				<c:out value="${contentVO.contEngCont}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.inptId" /></th>
			<td>
				<c:out value="${contentVO.inptId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.inptIp" /></th>
			<td>
				<c:out value="${contentVO.inptIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.inptDttm" /></th>
			<td>
				<c:out value="${contentVO.inptDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.modiId" /></th>
			<td>
				<c:out value="${contentVO.modiId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.modiIp" /></th>
			<td>
				<c:out value="${contentVO.modiIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="contentVO.modiDttm" /></th>
			<td>
				<c:out value="${contentVO.modiDttm}" />
			</td>
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
  	<input type="hidden" name="sId" value="<c:out value='${contentVO.contId }'/>">
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
	    window.open(GV_PRESENT_PATH + '/back/list.do?contId=<c:out value="${contentVO.contId }"/>', 'back-pop', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
	}
	
	function fn_refreshPage(){
		location.reload(true);
	}
	
</script>