<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: paging.jsp
 * @Description : 공통 페이징
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<c:if test="${paging ne null and paging.lastPage ne 1}">
	<div class="paging">
		<c:if test="${paging.currentPage ne 1 and paging.currentPage ne null and paging.currentPage ne ''}">
	        <a href="javascript:gf_movePage(1);" title="<spring:message code="paging.first" />" class="page_btn page_first"><img src="/images/common/btn/paging_first.png"></a>
	    </c:if>
		<c:if test="${paging.startPage>1}">
	        <a href="javascript:gf_movePage(<c:out value='${paging.startPage-paging.pageSize }'/>)" title="<spring:message code="paging.prev" />" class="page_btn page_prev">
	        	<img src="/images/common/btn/paging_prev.png" alt="<spring:message code="paging.prev" />">
	        </a>
	    </c:if>
	    <c:forEach var="i" begin="${paging.startPage}" end="${paging.endPage}" step="1">
	    	<c:choose>
	    		<c:when test="${paging.currentPage eq i }">
	    			<a href="javascript:gf_movePage(${i });" title="(<spring:message code="paging.current" />)<spring:message code="paging.move" arguments="${i }"/>" class="page_btn page_num current_page">
	    				<span>${i }</span>
	    			</a>
	    		</c:when>
	    		<c:otherwise>
	    			<a href="javascript:gf_movePage(${i });" title="<spring:message code="paging.move" arguments="${i }"/>" class="page_btn page_num">
	    				<span>${i }</span>
	    			</a>
	    		</c:otherwise>
	    	</c:choose>
	    </c:forEach>
	    <c:if test="${paging.endPage ne paging.lastPage}">
	        <a href="javascript:gf_movePage(<c:out value='${paging.startPage+paging.pageSize }'/>);" title="<spring:message code="paging.next" />" class="page_btn page_next">
	        	<img src="/images/common/btn/paging_next.png" alt="<spring:message code="paging.next" />">
	        </a>
	    </c:if>
	    <c:if test="${paging.currentPage ne paging.lastPage and paging.lastPage ne 0}">
	        <a href="javascript:gf_movePage(<c:out value='${paging.lastPage }'/>);" title="<spring:message code="paging.last" />" class="page_btn page_last" >
	        	<img src="/images/common/btn/paging_last.png" alt="<spring:message code="paging.last" />">
	        </a>
	    </c:if>
	</div>
</c:if>