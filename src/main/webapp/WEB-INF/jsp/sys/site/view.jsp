<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 사이트관리 상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="siteVO" id="detailForm" name="detailForm" method="post" autocomplete="off">
	
	<form:hidden path="siteCd"/>
	
	<div class="grid_wrap">
		<div class="col50">
			<div class="col_title">
				<p>사이트정보</p>
			</div>
			<div class="col_inner">
				<table class="detail_table">
					<caption>사이트 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><spring:message code="siteVO.siteCd"/></th>
						<td><c:out value="${siteVO.siteCd}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteNm"/></th>
						<td><c:out value="${siteVO.siteNm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteEngNm"/></th>
						<td><c:out value="${siteVO.siteEngNm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteAddr"/></th>
						<td><c:out value="${siteVO.siteAddr}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteEngAddr"/></th>
						<td><c:out value="${siteVO.siteEngAddr}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.telNo"/></th>
						<td><c:out value="${siteVO.telNo}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.faxNo"/></th>
						<td><c:out value="${siteVO.faxNo}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteLogo"/></th>
						<td><img src="<c:out value="${siteVO.viewFile.viewUrl}"/>"></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.sysAccessYn"/></th>
						<td><c:out value="${siteVO.sysAccessYn}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.loginYn"/></th>
						<td><c:out value="${siteVO.loginYn}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.useYn"/></th>
						<td><c:out value="${siteVO.useYn}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.templateCd"/></th>
						<td><c:out value="${siteVO.templateCd}"/></td>
					</tr>					
					<tr>
						<th><spring:message code="siteVO.siteMeta"/></th>
						<td><pre><c:out value="${siteVO.siteMeta}"/></pre></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo01"/></th>
						<td><c:out value="${siteVO.siteInfo01}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo02"/></th>
						<td><c:out value="${siteVO.siteInfo02}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo03"/></th>
						<td><c:out value="${siteVO.siteInfo03}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo04"/></th>
						<td><c:out value="${siteVO.siteInfo04}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo05"/></th>
						<td><c:out value="${siteVO.siteInfo05}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo06"/></th>
						<td><c:out value="${siteVO.siteInfo06}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo07"/></th>
						<td><c:out value="${siteVO.siteInfo07}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo08"/></th>
						<td><c:out value="${siteVO.siteInfo08}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo09"/></th>
						<td><c:out value="${siteVO.siteInfo09}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.siteInfo10"/></th>
						<td><c:out value="${siteVO.siteInfo10}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.inptId"/></th>
						<td><c:out value="${siteVO.inptId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.inptIp"/></th>
						<td><c:out value="${siteVO.inptIp}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.inptDttm"/></th>
						<td><c:out value="${siteVO.inptDttm}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.modiId"/></th>
						<td><c:out value="${siteVO.modiId}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.modiIp"/></th>
						<td><c:out value="${siteVO.modiIp}"/></td>
					</tr>
					<tr>
						<th><spring:message code="siteVO.modiDttm"/></th>
						<td><c:out value="${siteVO.modiDttm}"/></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="col50">
			<div class="col_title">
				<p>도메인정보</p>
			</div>
			<div class="col_inner">
				<div class="scroll_table_wrap">
					<table class="list_table ty2">
						<thead>
							<tr>
								<th><spring:message code="board.no"/></th>
								<th><spring:message code="domainVO.domain"/></th>
								<th><spring:message code="domainVO.defaultYn"/></th>
								<th><spring:message code="domainVO.useYn"/></th>
							</tr>
						</thead>
						<tbody id="domainList">
							<c:forEach var="item" items="${siteVO.domainList }" varStatus="i">
								<tr data-id="<c:out value='${item.domain }'/>">
									<td><c:out value='${i.count }'/></td>
									<td><c:out value='${item.domain }'/></td>
									<td><c:out value='${item.defaultYn }'/></td>
									<td><c:out value='${item.useYn }'/></td>
								</tr>
							</c:forEach>
							<c:if test="${empty siteVO.domainList}">
								<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>
							</c:if>
						</tbody>
				  	</table>
				</div>
			</div>
		</div>
	</div>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button code_btn" href="javascript:fn_codeView();">코드수정</a>
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
  	<input type="hidden" name="sId" value="<c:out value='${siteVO.siteCd }'/>">
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
	
	//수정
	function fn_codeView() {
		document.detailForm.action = GV_PRESENT_PATH + "/code.do";
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