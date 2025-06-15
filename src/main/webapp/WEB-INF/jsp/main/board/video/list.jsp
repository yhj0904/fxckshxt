<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
<% //게시글 유형 %>
<c:if test='${bbsMgtVO.cateYn eq "Y" and !empty bbsMgtVO.categoryList }'>
	<div class="table_category">
		<ul>
			<li <c:if test="${sCate eq 'ALL' }">class="active"</c:if>><a href="javascript:fn_changeCategory('ALL');"><spring:message code="search.all"/></a></li>
			<c:forEach var="cate" items="${bbsMgtVO.categoryList }" varStatus="i">
				<li <c:if test="${sCate eq cate}">class="active"</c:if>><a href="javascript:fn_changeCategory('<c:out value='${cate }'/>');"><c:out value='${cate }'/></a></li>
			</c:forEach>
		</ul>
	</div>
</c:if>
<% //게시글 유형 %>
<table class="list_table">
	<colgroup>
		<c:if test='${bbsMgtVO.adminUser}'>
			<col style=""/> <!-- CHECKBOX -->
		</c:if>
		<col style=""/> <!-- NO -->
		<col style=""/> <!-- 제목 -->
		<col style=""/> <!-- 작성자 -->
		<col style=""/> <!-- 조회수 -->
		<col style=""/> <!--  -->
	</colgroup>
	<thead>
		<tr>
			<c:if test='${bbsMgtVO.adminUser}'>
				<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
			</c:if>
			<th><spring:message code="board.no"/></th>
			<th><spring:message code="bbsVO.title"/></th>
			<th><spring:message code="bbsVO.writer"/></th>
			<th><spring:message code="bbsVO.viewCnt"/></th>
			<th><spring:message code="bbsVO.inptDttm"/></th>
		</tr>
	</thead>
	<tbody>
		<c:if test='${bbsMgtVO.noticeYn eq "Y" and !empty noticeList }'>
			<c:forEach var="item" items="${noticeList }" varStatus="i">
				<tr>
					<c:if test='${bbsMgtVO.adminUser}'>
						<td>&nbsp;</td>
					</c:if>
					<td><spring:message code="board.notice" /></td>
					<td>
						<div class="tit">
							<!-- <a class="link" href="javascript:fn_detailView('<c:out value="${item.bbsId }"/>');" title="<spring:message code="board.detail"/>"> -->
							<a class="link" href="/board/<c:out value='${fn:toLowerCase(sCode)}/view.do?sId=${item.bbsId }'/>" title="<spring:message code="board.detail"/>">
								<c:if test='${bbsMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
									<strong>[<c:out value="${item.category}"/>]</strong>
								</c:if>
								<c:out value="${item.title}"/>
							</a>
							<c:if test='${item.secret eq "Y"}'>
								<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
							</c:if>
						</div>
					</td>
					<td>
						<c:choose>
							<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
								<spring:message code="board.noname" />
							</c:when>
							<c:otherwise>
								<c:out value="${item.writer}"/>
							</c:otherwise>
						</c:choose>
					</td>	
					<td><c:out value="${item.viewCnt}"/></td>
					<td><c:out value="${fn:substring(item.inptDttm, 0, 10)}"/></td>
				</tr>
			</c:forEach>
		</c:if>
		<c:forEach var="item" items="${list }" varStatus="i">
			<tr>
				<c:if test='${bbsMgtVO.adminUser}'>
					<td>
						<c:choose>
							<c:when test='${item.delYn eq "Y" }'>
								&nbsp;
							</c:when>
							<c:otherwise>
								<label><input type="checkbox" name="checkRow" value="<c:out value="${item.bbsId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
							</c:otherwise>
						</c:choose>						
					</td>
				</c:if>
				<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
				<td>
					<div class="tit" data-depth="<c:out value="${item.bbsDepth }"/>">
						<c:if test="${item.bbsDepth > 1}">
							<span class="ico_reply"><spring:message code="board.reply" /></span>
						</c:if>
						<c:choose>
							<c:when test='${item.delYn eq "Y" }'>
								<spring:message code="board.deleteboard" />
							</c:when>
							<c:otherwise>
								<!-- <a class="link" href="javascript:fn_detailView('<c:out value="${item.bbsId }"/>');" title="<spring:message code="board.detail"/>"> -->
								<a class="link" href="/board/<c:out value='${fn:toLowerCase(sCode)}/view.do?sId=${item.bbsId }'/>" title="<spring:message code="board.detail"/>">
									<c:if test='${bbsMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
										<strong>[<c:out value="${item.category}"/>]</strong>
									</c:if>
									<c:out value="${item.title}"/>
								</a>
								<c:if test='${item.secret eq "Y"}'>
									<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
								</c:if>
							</c:otherwise>
						</c:choose>
					</div>
				</td>
				<td>
					<c:choose>
						<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
							<spring:message code="board.noname" />
						</c:when>
						<c:otherwise>
							<c:out value="${item.writer}"/>
						</c:otherwise>
					</c:choose>
				</td>	
				<td><c:out value="${item.viewCnt}"/></td>
				<td><c:out value='${fn:substring(item.inptDttm, 0, 10)}'/></td>
			</tr>
		</c:forEach>
		<c:if test="${empty list}">
			<tr><td class="no_data" colspan="6"><spring:message code="board.noData" /></td></tr>
		</c:if>
	</tbody>
</table>
   	
<div class="btn_wrap">
	<ul>
		<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.regiAuthYn}'>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser}'>
			<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
		</c:if>
	</ul>
</div>