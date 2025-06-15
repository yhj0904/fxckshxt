<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 배너 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			배너 내용추가
 */
%>
<form:form commandName="bannerVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="bannerId"/>
	
	<table class="detail_table">
		<caption>배너 상세조회</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="bannerVO.bannerId" /></th>
			<td>
				<c:out value="${bannerVO.bannerId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.bannerCd" /></th>
			<td>
				<c:out value="${bannerVO.bannerCd}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.bannerNm" /></th>
			<td>
				<c:out value="${bannerVO.bannerNm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.bannerCont" /></th>
			<td>
				<c:out value="${bannerVO.bannerCont}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.bannerLink" /></th>
			<td>
				<c:out value="${bannerVO.bannerLink}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.startDttm" /></th>
			<td>
				<c:out value="${bannerVO.startDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.endDttm" /></th>
			<td>
				<c:out value="${bannerVO.endDttm}" />
			</td>
		</tr>
		<tr>
			<th>IMAGE</th>
			<td>
				<c:choose>
   					<c:when test="${bannerVO.viewFile ne null}">
   						<img src="<c:out value='${bannerVO.viewFile.viewUrl }'/>" alt="<c:out value='${bannerVO.viewFile.oname }'/>">
   					</c:when>
   					<c:otherwise>
   						<img src="/images/common/no_img.png" alt="NO IMG">
   					</c:otherwise>
   				</c:choose>			
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.imgExplain" /></th>
			<td>
				<c:out value="${bannerVO.imgExplain}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.useYn" /></th>
			<td>
				<c:out value="${bannerVO.useYn}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.inptId" /></th>
			<td>
				<c:out value="${bannerVO.inptId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.inptIp" /></th>
			<td>
				<c:out value="${bannerVO.inptIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.inptDttm" /></th>
			<td>
				<c:out value="${bannerVO.inptDttm}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.modiId" /></th>
			<td>
				<c:out value="${bannerVO.modiId}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.modiIp" /></th>
			<td>
				<c:out value="${bannerVO.modiIp}" />
			</td>
		</tr>
		<tr>
			<th><spring:message code="bannerVO.modiDttm" /></th>
			<td>
				<c:out value="${bannerVO.modiDttm}" />
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
  	<input type="hidden" name="sId" value="<c:out value='${bannerVO.bannerId }'/>">
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
	
</script>