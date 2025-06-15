<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: code_view.jsp
 * @Description : 템플릿관리 백업상세조회 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
	<div class="tab_wrap02">
		<div class="tab_btn">
			<ul>
				<li data-rel="main" class="active">
					<a href="javascript:fn_changeTab('main')"><spring:message code="templateMgtVO.mainCode"/></a>
				</li>
				<li data-rel="sub">
					<a href="javascript:fn_changeTab('sub')"><spring:message code="templateMgtVO.subCode"/></a>
				</li>
				<li data-rel="login">
					<a href="javascript:fn_changeTab('login')"><spring:message code="templateMgtVO.loginCode"/></a>
				</li>
				<li data-rel="empty">
					<a href="javascript:fn_changeTab('empty')"><spring:message code="templateMgtVO.emptyCode"/></a>
				</li>
				<li data-rel="pop">
					<a href="javascript:fn_changeTab('pop')"><spring:message code="templateMgtVO.popCode"/></a>
				</li>
			</ul>
		</div>
		<div class="tab_content">
			<iframe id="previewFrame" src="/sys/template/previewFrame.do?sId=<c:out value='${sId }'/>&type=<c:out value='${type }'/>" style="width:100%;height:100%;"></iframe>
		</div>
	</div>
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>
<script type="text/javaScript" defer="defer">
	function fn_closePop(){
		self.close();
	}
	
	//탭변경
	function fn_changeTab(tabId) {
		$(".tab_wrap02 .tab_btn ul li").removeClass("active");
		$(".tab_wrap02 .tab_btn ul li[data-rel=" + tabId + "]").addClass("active");
		var url = "/sys/template/previewFrame.do?sId=<c:out value='${sId }'/>";
		url += "&type="+tabId;
		document.getElementById("previewFrame").src = url;		
	}
</script>