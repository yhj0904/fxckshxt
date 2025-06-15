<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 일정 목록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
<% //일정 유형 %>
<c:if test='${schMgtVO.cateYn eq "Y" and !empty schMgtVO.categoryList }'>
	<div class="table_category">
		<ul>
			<li <c:if test="${sCate eq 'ALL' }">class="active"</c:if>><a href="javascript:fn_changeCategory('ALL');"><spring:message code="search.all"/></a></li>
			<c:forEach var="cate" items="${schMgtVO.categoryList }" varStatus="i">
				<li <c:if test="${sCate eq cate}">class="active"</c:if>><a href="javascript:fn_changeCategory('<c:out value='${cate }'/>');"><c:out value='${cate }'/></a></li>
			</c:forEach>
		</ul>
	</div>
</c:if>
<% //일정 유형 %>
<table class="list_table">
	<colgroup>
		<c:if test='${schMgtVO.adminUser}'>
			<col style=""/> <!-- CHECKBOX -->
		</c:if>
		<col style=""/> <!-- -->
		<col style=""/> <!-- -->
		<col style=""/> <!-- -->
		<col style=""/> <!-- -->
		<col style=""/> <!-- -->
		<col style=""/> <!-- -->
	</colgroup>
	<thead>
		<tr>
			<c:if test='${schMgtVO.adminUser}'>
				<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
			</c:if>
			<th><spring:message code="board.no"/></th>
			<th><spring:message code="schVO.title"/></th>
			<th><spring:message code="schVO.period"/></th>
			<th><spring:message code="schVO.writer"/></th>
			<th><spring:message code="schVO.viewCnt"/></th>
			<th><spring:message code="schVO.inptDttm"/></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${list }" varStatus="i">
			<tr>
				<c:if test='${schMgtVO.adminUser}'>
					<td><label><input type="checkbox" name="checkRow" value="<c:out value="${item.schId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label></td>
				</c:if>
				<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
				<td>
					<a class="link" href="javascript:fn_detailView('<c:out value="${item.schId }"/>');" title="<spring:message code="board.detail"/>">
						<c:if test='${schMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
							<strong>[<c:out value="${item.category}"/>]</strong>
						</c:if>
						<c:out value="${item.title}"/>
					</a>
				</td>
				<td>
					<c:out value="${item.startDttm}"/>
					<c:if test='${item.endDttm ne null and item.endDttm ne ""}'>~<c:out value="${item.endDttm}"/></c:if>
				</td>
				<td><c:out value="${item.writer}"/></td>	
				<td><c:out value="${item.viewCnt}"/></td>
				<td><c:out value="${item.inptDttm}"/></td>
			</tr>
		</c:forEach>
		<c:if test="${empty list}">
			<tr><td class="no_data" colspan="7"><spring:message code="board.noData" /></td></tr>
		</c:if>
	</tbody>
</table>
<div class="btn_wrap">
	<ul>
		<c:if test='${schMgtVO.adminUser or schMgtVO.regiAuthYn}'>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
		</c:if>
		<c:if test='${schMgtVO.adminUser}'>
			<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
		</c:if>
	</ul>
</div>