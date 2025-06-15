<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="java.util.List" %>
<%@ page import="kr.co.nanwe.file.service.ComFileManager" %>
<%@ page import="kr.co.nanwe.file.service.ViewFileVO" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>
<%
ComFileManager comFileManager = (ComFileManager)webApplicationContext.getBean("comFileManager");
CommCdService commCdService = (CommCdService) webApplicationContext.getBean("commCdService");

/**
 * @Class Name 	: list.jsp
 * @Description : wk100 게시판 목록
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.24		신한나			최초생성
 */ 
 
%>
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
				<c:set var="bbsId" value="${item.bbsId}" />
				<%
					String bbsId = (String) pageContext.getAttribute("bbsId");
					List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_BBS", bbsId);
					pageContext.setAttribute("viewFiles", viewFiles);
				%>
					<c:choose>
						<c:when test='${item.delYn eq "Y" }'>
							<img src="/images/common/no_img.png" alt="NO_IMG">
						</c:when>
						<c:otherwise>
							<a class="link" href="javascript:fn_detailView('<c:out value="${item.bbsId }"/>');" title="<spring:message code="board.detail"/>">
								<c:choose>
									<c:when test="${empty viewFiles}">
										<div class="thumbBack" style="background-image: url('/images/common/no_img.png');">
										</div>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${empty viewFiles[0].viewUrl}">
												<div class="thumbBack" style="background-color: #f1f1f1;">
											</c:when>
											<c:otherwise>
												<div class="thumbBack" style="background-image: url(<c:out value='${viewFiles[0].viewUrl} '/>)">
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${empty viewFiles[1].viewUrl and empty viewFiles[1].fpath}">
												<img class="thumbLogo" src="/images/common/no_img.png">
											</c:when>
											<c:otherwise>
												<img class="thumbLogo" src="<c:out value="${viewFiles[1].viewUrl} "/><c:out value="${viewFiles[1].fpath} "/>">
											</c:otherwise>
										</c:choose>
										</div>
									</c:otherwise>
								</c:choose>
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
								<a class="link" href="javascript:fn_detailView('<c:out value="${item.bbsId }"/>');" title="<spring:message code="board.detail"/>">
									<c:if test='${bbsMgtVO.cateYn eq "Y" and item.category ne null and item.category ne ""}'>
										<strong>[<c:out value="${item.category}"/>]</strong>
									</c:if>
									<c:set var="bbsInfo01" value="${item.bbsInfo01}" />
										<%
											String bbsInfo01 = (String) pageContext.getAttribute("bbsInfo01");
											CommCdVO bbsInfo01CdVO = commCdService.selectCommCd(bbsInfo01);
											pageContext.setAttribute("bbsInfo01CdVO", bbsInfo01CdVO);
										%>
									<h4 class="start_cate" id="<c:out value="${bbsInfo01CdVO.cd}"/>"><c:out value="${bbsInfo01CdVO.cdNm}"/></h4>
									<h2 class="start_tit"><c:out value="${item.title}"/></h2>
									<c:if test='${item.secret eq "Y"}'>
										<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
									</c:if>
								</a>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="writer hashtagDiv">
						<p class="hashtag_p"><c:out value="${item.bbsInfo05}"/></p>
					</div>
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