<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ page import="kr.co.nanwe.cmmn.util.DateUtil" %>
<%
/**
 * @Class Name 	: js.jsp
 * @Description : 공통 JS
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<script type="text/javascript" src="/js/common/jquery-1.8.2.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="/lib/wow/wow.min.js"></script>
<script src="/lib/easing/easing.min.js"></script>
<script src="/lib/waypoints/waypoints.min.js"></script>
<script src="/lib/counterup/counterup.min.js"></script>
<script src="/lib/owlcarousel/owl.carousel.min.js"></script>

<script src="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.1/xlsx.full.min.js"></script>
<link rel="stylesheet" href="https://uicdn.toast.com/grid/latest/tui-grid.css" />
<script type="text/javascript" src="https://uicdn.toast.com/tui.pagination/v3.4.0/tui-pagination.js"></script>
<script src="https://uicdn.toast.com/grid/latest/tui-grid.js"></script>

<script>
	var GV_TODAY_DATE = "<%=DateUtil.getDate("yyyy.MM.dd")%>";
	var GV_TODAY_YEAR = "<%=DateUtil.getDate("yyyy")%>";
	var GV_TODAY_MONTH = "<%=DateUtil.getDate("MM")%>";
	var GV_TODAY_DAY = "<%=DateUtil.getDate("dd")%>";
	var GV_LANGUAGE = '<c:out value="${GV_LANGUAGE}"/>';
	var GV_PRESENT_PATH = '<c:out value="${GV_PRESENT_PATH}"/>';
	var GV_MESSAGE = {
		ALERT 	: '<spring:message code="message.alert"/>',
		CONFIRM	: '<spring:message code="button.confirm"/>',
		YES 	: '<spring:message code="button.yes"/>',
		NO 		: '<spring:message code="button.no"/>',
		OK	 	: '<spring:message code="button.confirm"/>',
		CANCEL 	: '<spring:message code="button.cancel"/>',
		CLOSE 	: '<spring:message code="button.close"/>',
		DATA_ING : '<spring:message code="message.data.ing"/>',
	}
	var CERTIFICATION_MSG = {
		EMPTY 	: '<spring:message code="certification.alert.certKey"/>',			
		RESEND 	: '<spring:message code="certification.resend"/>',
		CERT 	: '<spring:message code="certification.button"/>',
		TIME 	: '<spring:message code="certification.error.time"/>',	
		SUCCESS	: '<spring:message code="certification.success"/>',		
	};
	$(document).ready(function() {		
		if(GV_LANGUAGE == ''){
			GV_LANGUAGE = 'en'
		}
	});
</script>
<script type="text/javascript" src="/js/common/jquery-ui.min.js"></script>
<script type="text/javascript" src="/js/common/gf_util.js"></script>
<script type="text/javascript" src="/js/common/gf_field.js"></script>
<script type="text/javascript" src="/js/common/gf_layer.js"></script>
<script type="text/javascript" src="/js/common/gf_searchPop.js"></script>
<script type="text/javascript" src="/js/common/gf_form.js"></script>
<script type="text/javascript" src="/js/common/gf_ajax.js"></script>
<!--[if lt IE 10]>
<script type="text/javascript" src="/js/common/html/html5.js"></script>
<script>
	// Internet Explorer 버전 체크
	var IEVersionCheck = function() {
		var word;
		var version = "N/A";
		var agent = navigator.userAgent.toLowerCase();
		var name = navigator.appName;
		// IE old version ( IE 10 or Lower )
		if (name == "Microsoft Internet Explorer")
			word = "msie ";
		else {
			// IE 11
			if (agent.search("trident") > -1)
				word = "trident/.*rv:";
			// IE 12  ( Microsoft Edge )
			else if (agent.search("edge/") > -1)
				word = "edge/";
		}
		
		var reg = new RegExp(word + "([0-9]{1,})(\\.{0,}[0-9]{0,1})");
		if (reg.exec(agent) != null)
			version = RegExp.$1 + RegExp.$2;
		//return version;
		if (version < 10) {
			if (confirm("<spring:message code="message.error.ie" />")) {
				location.href = "https://support.microsoft.com/ko-kr/help/17621/internet-explorer-downloads";
			}
		}
	};
	IEVersionCheck();
</script>
<![endif]-->