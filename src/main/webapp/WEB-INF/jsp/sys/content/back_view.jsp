<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: back_view.jsp
 * @Description : 컨텐츠관리 백업상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
<form:form modelAttribute
		
		<form:hidden path="contId"/>
		<form:hidden path="seq"/>
		
		<table class="detail_table">
			<caption>컨텐츠관리 백업상세조회</caption>
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
			<tr>
				<th><spring:message code="contentVO.backDttm" /></th>
				<td>
					<c:out value="${contentVO.backDttm}" />
				</td>
			</tr>
		</table>
		
		<% /** 이중방지 토큰 */ %>
		<double-submit:preventer/>
		
		<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	<% /** //검색조건 유지 */ %>  	
	</form:form>
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button restore_btn" href="javascript:fn_restoreView('<c:out value="${contentVO.contId}" />', <c:out value="${contentVO.seq}" />);"><spring:message code="button.restore" /></a>
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

<script type="text/javaScript" defer="defer">

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/back/list.do";
		document.detailForm.submit();
	}
	
	//복원
	function fn_restoreView(contId, seq) {
		var msg = confirm('<spring:message code="message.confirm.save" />');
		if(msg == true){
			fn_restoreAction(contId, seq);
		}
	}
	
	function fn_restoreAction(contId, seq){
		gf_ajax({
			url :  GV_PRESENT_PATH + "/back/modifyAction.json",
			type : "POST",
			data : {contId : contId, seq : seq},
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
	
</script>