<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: syste.jsp
 * @Description : 시스템 관리
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="system_wrap">
	<ul>
		<li>
			<div class="">
				<p class="">에디터 임시파일 삭제</p>
				<a href="javascript:removeEditorFiles();" class="button">삭제</a>	
			</div>		
		</li>
		<li>
			<div class="">
				<p class="">웹파일 백업</p>
				<a href="javascript:backWebFiles();" class="button">백업</a>	
			</div>		
		</li>
		<li>
			<div class="">
				<p class="">첨부파일 백업</p>
				<a href="javascript:backUploadFiles();" class="button">백업</a>	
			</div>		
		</li>
		<li>
			<div class="">
				<p class="">웹에디퍼파일 백업</p>
				<a href="javascript:backEditorFiles();" class="button">백업</a>	
			</div>		
		</li>
	</ul>
</div>
<script type="text/javaScript" defer="defer">
	
	//임시 에디터파일 삭제
	function removeEditorFiles() {		
		gf_ajax({
			url : "/sys/system/removeEditorFiles.json",
			type : "POST",
		}).then(function(response){
			alert(response + "개의 파일이 삭제되었습니다.");
		});
	}
	
	//웹파일 백업
	function backWebFiles() {
		gf_ajax({
			url : "/sys/system/backWebFiles.json",
			type : "POST",
		}).then(function(response){
			if(response){
				alert("백업이 완료되었습니다.");
			} else {
				alert("백업이 실패하였습니다.");
			}					
		});
	}
	
	//첨부파일 백업
	function backUploadFiles() {
		gf_ajax({
			url : "/sys/system/backUploadFiles.json",
			type : "POST",
		}).then(function(response){
			if(response){
				alert("백업이 완료되었습니다.");
			} else {
				alert("백업이 실패하였습니다.");
			}							
		});
	}
	
	//에디터파일 백업
	function backEditorFiles() {
		gf_ajax({
			url : "/sys/system/backEditorFiles.json",
			type : "POST",
		}).then(function(response){
			if(response){
				alert("백업이 완료되었습니다.");
			} else {
				alert("백업이 실패하였습니다.");
			}						
		});
	}
	
</script>