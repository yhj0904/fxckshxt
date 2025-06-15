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
<div class="photo_list">
	<ul>
		<c:forEach var="item" items="${list }" varStatus="i">
			<li>
				<c:if test='${bbsMgtVO.adminUser and item.delYn ne "Y"}'>
					<div class="item_check">
						<label><input type="checkbox" name="checkRow" value="<c:out value="${item.bbsId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
					</div>
				</c:if>
				<div class="item_photo">
					<c:choose>
						<c:when test='${item.delYn eq "Y" }'>
							<img src="/images/common/no_img.png" alt="NO_IMG">
						</c:when>
						<c:otherwise>
							<!-- <a class="link" href="javascript:fn_detailView('<c:out value="${item.bbsId }"/>');" title="<spring:message code="board.detail"/>"> -->
							<a class="link" href="/board/<c:out value='${fn:toLowerCase(sCode)}/view.do?sId=${item.bbsId }'/>" title="<spring:message code="board.detail"/>">
								<img src="${item.thumbnail }" alt="<c:out value="${item.title}"/>">
							</a>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="item_cont">
					<div class="tit" data-depth="<c:out value="${item.bbsDepth }"/>">
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
									<c:if test='${item.secret eq "Y"}'>
										<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
									</c:if>
								</a>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="writer">
						<c:choose>
							<c:when test='${bbsMgtVO.nonameYn eq "Y" }'>
								<spring:message code="board.noname" />
							</c:when>
							<c:otherwise>
								<c:out value="${item.writer}"/>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="date"><c:out value='${fn:substring(item.inptDttm, 0, 10)}'/></div>
				</div>
			</li>
		</c:forEach>
		<c:if test="${empty list}">
			<li class="no_data"><spring:message code="board.noData" /></li>
		</c:if>
	</ul>
</div>
   	
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