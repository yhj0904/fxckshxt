<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/session.jspf" %>
<%@ include file="/WEB-INF/jsp/include/context.jspf" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="double-submit" uri="http://www.egovframe.go.kr/tags/double-submit/jsp" %>
<%@ page import="kr.co.nanwe.code.service.CommCdService" %>
<%@ page import="kr.co.nanwe.code.service.CommCdVO" %>
<%@ page import="kr.co.nanwe.file.service.ViewFileVO" %>
<%@ page import="kr.co.nanwe.file.service.ComFileManager" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : wk100 게시판 상세화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.24		신한나			최초생성
 */ 

	CommCdService commCdService = (CommCdService) webApplicationContext.getBean("commCdService");
	ComFileManager comFileManager = (ComFileManager)webApplicationContext.getBean("comFileManager");
	
%>
<div class="aply_view_wrap">
	<div class="view_title_img">
		<c:set var="bbsId" value="${bbsVO.bbsId}" />		
		<%
			String bbsId = (String) pageContext.getAttribute("bbsId");
			List<ViewFileVO> viewFiles = comFileManager.getViewFileList("COM_BBS", bbsId);
			pageContext.setAttribute("viewFiles", viewFiles);
		%>	
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
	</div>

	<div class="detail_div">
		<c:set var="bbsInfo01" value="${bbsVO.bbsInfo01}" />
		<%
			String bbsInfo01 = (String) pageContext.getAttribute("bbsInfo01");
			CommCdVO bbsInfo01CdVO = commCdService.selectCommCd(bbsInfo01);
			pageContext.setAttribute("bbsInfo01CdVO", bbsInfo01CdVO);
		%>
		<div class="detail_titBox">
			<span class="detail_status" id="<c:out value="${bbsInfo01CdVO.cd}"/>"><c:out value="${bbsInfo01CdVO.cdNm}"/></span>
			<div class="detail_titBoxRight">
				<p class="detail_tit"><c:out value="${bbsVO.title}" /></p>
				<p class="detail_hash"><c:out value="${bbsVO.bbsInfo05}"/></p>
			</div>
		</div>	
			
			
		<h2></h2>
		<div class="item_cont_info">
			<dl>
				<dt>대표자</dt><dd><c:out value="${bbsVO.bbsInfo02}"/><c:if test="${empty bbsVO.bbsInfo02}">-</c:if></dd>
				<dt>연락처</dt><dd><c:out value="${bbsVO.bbsInfo03}"/><c:if test="${empty bbsVO.bbsInfo03}">-</c:if></dd>
				<dt>이메일</dt><dd><c:out value="${bbsVO.bbsInfo04}"/><c:if test="${empty bbsVO.bbsInfo04}">-</c:if></dd>
				<dt>홈페이지</dt><dd><a href="<c:out value="${bbsVO.bbsInfo06}"/>" target="_blank"><c:out value="${bbsVO.bbsInfo06}"/></a><c:if test="${empty bbsVO.bbsInfo06}">-</c:if></dd>
				<dt>SNS</dt><dd><a href="<c:out value="${bbsVO.bbsInfo07}"/>" target="_blank"><c:out value="${bbsVO.bbsInfo07}"/></a><c:if test="${empty bbsVO.bbsInfo07}">-</c:if></dd>
				<dt>서비스소개</dt><dd><c:out value="${bbsVO.contents}" escapeXml="false"/></dd>
			</dl>
		</div>
	</div>

</div>
  	
<div class="btn_wrap">
	<ul>
		<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.regiAuthYn}'>
			<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser or bbsVO.inptId eq LOGIN_USER.loginId}'>
			<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.replyAuthYn}'>
			<li><a class="button reply_btn" href="javascript:fn_replyView();"><spring:message code="button.reply" /></a></li>
		</c:if>
		<c:if test='${bbsMgtVO.adminUser or bbsVO.inptId eq LOGIN_USER.loginId}'>
			<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li>
		</c:if>
		<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
	</ul>
</div>