<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 팝업 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	
	<form:hidden path="popId"/>
	
	<table class="detail_table">
		<caption>팝업 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
   		<tr>
			<th><spring:message code="popupVO.siteCd" /></th>
			<td>
				<c:out value="${popupVO.siteCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.popId" /></th>
			<td>
				<c:out value="${popupVO.popId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.popType" /></th>
			<td>
				<c:out value="${popupVO.popType}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.popNm" /></th>
			<td>
				<c:out value="${popupVO.popNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.startDttm" /></th>
			<td>
				<c:out value="${popupVO.startDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.endDttm" /></th>
			<td>
				<c:out value="${popupVO.endDttm}" />
			</td>
		</tr>
		<tr data-pop-type="HTML">
			<th><spring:message code="popupVO.popCont" /></th>
			<td>
				<c:out value="${popupVO.popCont}" escapeXml="false"/>
			</td>
		</tr>
		<tr data-pop-type="IMAGE">
			<th>IMAGE</th>
			<td>
				<c:choose>
   					<c:when test="${popupVO.viewFile ne null}">
   						<img src="<c:out value='${popupVO.viewFile.viewUrl }'/>" alt="<c:out value='${popupVO.viewFile.oname }'/>">
   					</c:when>
   					<c:otherwise>
   						<img src="/images/common/no_img.png" alt="NO IMG">
   					</c:otherwise>
   				</c:choose>			
			</td>
		</tr>
		<tr data-pop-type="IMAGE">
			<th><spring:message code="popupVO.imgExplain" /></th>
			<td>
				<c:out value="${popupVO.imgExplain}" />
			</td>
		</tr>
		<tr data-pop-type="IMAGE">
			<th><spring:message code="popupVO.popLink" /></th>
			<td>
				<c:out value="${popupVO.popLink}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.popWidth" /></th>
			<td>
				<c:out value="${popupVO.popWidth}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.popHeight" /></th>
			<td>
				<c:out value="${popupVO.popHeight}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.useYn" /></th>
			<td>
				<c:out value="${popupVO.useYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.inptId" /></th>
			<td>
				<c:out value="${popupVO.inptId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.inptIp" /></th>
			<td>
				<c:out value="${popupVO.inptIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.inptDttm" /></th>
			<td>
				<c:out value="${popupVO.inptDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.modiId" /></th>
			<td>
				<c:out value="${popupVO.modiId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.modiIp" /></th>
			<td>
				<c:out value="${popupVO.modiIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="popupVO.modiDttm" /></th>
			<td>
				<c:out value="${popupVO.modiDttm}" />
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
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
   	<% /** 검색조건 유지 */ %>
  	<input type="hidden" name="sId" value="<c:out value='${popupVO.popId }'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javaScript" defer="defer">

	$(function(){
		//초기화면 SET
		var popType = '<c:out value="${popupVO.popType}"/>';
		if(popType == "HTML"){
			$("tr[data-pop-type='HTML']").css("display", "table-row");
			$("tr[data-pop-type='IMAGE']").css("display", "none");
		} else {
			$("tr[data-pop-type='IMAGE']").css("display", "table-row");
			$("tr[data-pop-type='HTML']").css("display", "none");
		}
	});

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
	
</script>