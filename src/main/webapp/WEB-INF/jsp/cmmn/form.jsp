<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
/**
 * @Class Name 	: form.jsp
 * @Description : 공통 폼 태그
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<% /** 언어변환폼 */ %>
<form id="cmmmnLocaleForm" name="cmmmnLocaleForm" method="post">
	<input type="hidden" name="language">
</form>
<% /** 다운로드 폼 */ %>
<form id="cmmmnDownloadForm" name="cmmmnDownloadForm" method="post" action="/file/download.do">
	<input type="hidden" name="fno" value="">
	<input type="hidden" name="oname" value="">
   	<input type="hidden" name="fpath" value="">
   	<input type="hidden" name="fname" value="">
</form>