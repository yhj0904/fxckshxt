<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 즐겨찾기 관리
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form id="detailForm" name="detailForm" method="post" action="/bookmark/registerAction.do">
	<table class="list_table">
		<thead>
			<tr>
				<th><spring:message code="menuBookmarkVO.menuNm" /></th>
				<th><spring:message code="menuBookmarkVO.menuLink" /></th>
				<th>BOOKMARK</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${menuList }" varStatus="i">
				<tr>
					<td class="tl">
						<c:out value="${item.menuNm }"/>
					</td>
					<td class="tl">
						<c:set var="menuLink" value="${item.menuLink }"/>
						<c:choose>
							<c:when test="${item.progYn eq 'Y' and item.progPath ne null and item.progPath ne ''}">
								<c:choose>
									<c:when test='${item.progCd eq "CONTENT"}'>
										<c:set var="menuLink" value="/content/${item.progParam }.do"/>
									</c:when>
									<c:when test='${item.progCd eq "BOARD"}'>
										<c:set var="menuLink" value="/board/${item.progParam }.do"/>							
									</c:when>
									<c:when test='${item.progCd eq "SCHEDULE"}'>
										<c:set var="menuLink" value="/schedule/${item.progParam }.do"/>							
									</c:when>
									<c:otherwise>
										<c:set var="menuLink" value="${item.progPath }"/>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${item.menuLink ne null and item.menuLink ne ''}">
								<c:set var="menuLink" value="${item.menuLink }"/>
							</c:when>
						</c:choose>
						<c:out value="${menuLink }"/>
					</td>
					<td>
						<label class="check">
							<input type="checkbox" name="menuId" value="<c:out value="${item.menuId }"/>"<c:if test='${item.bookCheck > 0 }'>checked</c:if>><i></i>
						</label>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="btn_wrap">
		<ul>
			<li><a class="button save_btn" href="javascript:fn_saveAction();"><spring:message code="button.save" /></a></li>
			<li><a class="button cancel_btn" href="javascript:fn_cancelView();"><spring:message code="button.cancel" /></a></li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
	
</form>

<script type="text/javaScript" defer="defer">	

	
	//목록
	function fn_cancelView() {
		location.href = "/";
	}
	
	//등록 처리
	function fn_saveAction() {
		frm = document.detailForm;
		frm.action = "/bookmark/registerAction.do";
		frm.submit();
	}
	
</script>