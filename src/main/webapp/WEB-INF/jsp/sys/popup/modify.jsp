<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 팝업 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="popupVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">

   	<form:hidden path="popId"/>
	
	<table class="detail_table">
		<caption>팝업 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="siteCd"><spring:message code="popupVO.siteCd" /></label></th>
			<td>
				<form:select path="siteCd">
					<form:option value="" label="--선택--" />
					<c:if test="${!empty siteList }">							
						<c:forEach var="site" items="${siteList }">
							<form:option value="${site.siteCd }" label="${site.siteNm }" />
						</c:forEach>				
					</c:if>
				</form:select>
			</td>
		</tr>
		<tr>
			<th><label for="popNm"><spring:message code="popupVO.popNm" /></label></th>
			<td>
				<form:input path="popNm" />
				<span class="form_error" data-path="popNm"><form:errors path="popNm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="startDttm"><spring:message code="popupVO.startDttm" /></label></th>
			<td>
				<form:input path="startDttm" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="startDttm"><form:errors path="startDttm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="endDttm"><spring:message code="popupVO.endDttm" /></label></th>
			<td>
				<form:input path="endDttm" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="endDttm"><form:errors path="endDttm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="popType"><spring:message code="popupVO.popType" /></label></th>
			<td>
				<form:radiobutton path="popType" value="IMAGE" label="IMAGE"/>
				<form:radiobutton path="popType" value="HTML" label="HTML"/>
				<span class="form_error" data-path="popType"><form:errors path="popType" /></span>
			</td>
		</tr>
		<tr data-pop-type="HTML">
			<th><label for="popCont"><spring:message code="popupVO.popCont" /></label></th>
			<td>
				<form:textarea path="popCont" />
				<span class="form_error" data-path="popCont"><form:errors path="popCont" /></span>
			</td>
		</tr>
		<tr data-pop-type="IMAGE">
			<th><label for="uploadFile"><spring:message code="text.image" /></label></th>
			<td>
				<c:choose>
   					<c:when test="${popupVO.viewFile ne null}">
   						<img id="popupFileImg" src="<c:out value='${popupVO.viewFile.viewUrl }'/>" alt="<c:out value='${popupVO.viewFile.oname }'/>">
   					</c:when>
   					<c:otherwise>
   						<img id="popupFileImg" src="/images/common/no_img.png" alt="NO IMG">
   					</c:otherwise>
   				</c:choose>
   				<div>
    				<label class="file">
    					<c:choose>
		   					<c:when test="${popupVO.viewFile ne null}">
		   						<span class="name"><c:out value='${popupVO.viewFile.oname }'/></span>
		   					</c:when>
		   					<c:otherwise>
		   						<span class="name"><spring:message code="text.file.placeholder" /></span>
		   					</c:otherwise>
		   				</c:choose>    					
    					<form:input type="file" path="uploadFile" data-img="popupFileImg"/>
    					<span class="btn"><spring:message code="button.upload" /></span>
    				</label>
   				</div>
				<span class="form_error" data-path="uploadFile"><form:errors path="uploadFile" /></span>
			</td>
		</tr>
		<tr data-pop-type="IMAGE">
			<th><label for="imgExplain"><spring:message code="popupVO.imgExplain" /></label></th>
			<td>
				<form:input path="imgExplain" />
				<span class="form_error" data-path="imgExplain"><form:errors path="imgExplain" /></span>
			</td>
		</tr>
		<tr data-pop-type="IMAGE">
			<th><label for="popLink"><spring:message code="popupVO.popLink" /></label></th>
			<td>
				<form:input path="popLink" />
				<span class="form_error" data-path="popLink"><form:errors path="popLink" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="popWidth"><spring:message code="popupVO.popWidth" /></label></th>
			<td>
				<form:input path="popWidth" />
				<span class="form_error" data-path="popWidth"><form:errors path="popWidth" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="popHeight"><spring:message code="popupVO.popHeight" /></label></th>
			<td>
				<form:input path="popHeight" />
				<span class="form_error" data-path="popHeight"><form:errors path="popHeight" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="useYn"><spring:message code="popupVO.useYn" /></label></th>
			<td>
				<form:radiobutton path="useYn" value="Y" label="Y"/>
				<form:radiobutton path="useYn" value="N" label="N"/>
				<span class="form_error" data-path="useYn"><form:errors path="useYn" /></span>
			</td>
		</tr>
  	</table>
   	
   	<div class="btn_wrap">
		<ul>
			<li>
				<a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a>
			</li>
			<li>
				<a class="button cancel_btn" href="javascript:fn_detailView();"><spring:message code="button.cancel" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${popupVO.popId}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="popupVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	
	$(function(){
		//초기화면 SET
		var popType = '<c:out value="${popupVO.popType}"/>';
		if(popType == "HTML"){
			$("tr[data-pop-type='HTML']").css("display", "table-row");
			$("tr[data-pop-type='IMAGE']").css("display", "none");
		} else {
			$("input[name='popType'][value='IMAGE']").prop("checked" , true);
			$("tr[data-pop-type='IMAGE']").css("display", "table-row");
			$("tr[data-pop-type='HTML']").css("display", "none");
		}
		
		$("input[name='popType']").click(function(){
			if($(this).val() == "HTML"){				
				$("tr[data-pop-type='HTML']").css("display", "table-row");
				$("tr[data-pop-type='IMAGE']").css("display", "none");
			} else {
				$("tr[data-pop-type='IMAGE']").css("display", "table-row");
				$("tr[data-pop-type='HTML']").css("display", "none");
			}
		})
		
		//웹에디터 사용시
		var editorArray = [{ id : "popCont" , useUpload : "Y" }];
		gf_initCkEditor(editorArray);
	});

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validatePopupVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/modifyAction.do";
			frm.submit();
		}
		
	}
	
	//삭제
	function fn_removeView() {
		var msg = confirm('<spring:message code="message.confirm.remove" />');
		if(msg == true){
			document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
			document.detailForm.submit();
		}
	}

	//취소
	function fn_detailView() {
		document.detailForm.action = GV_PRESENT_PATH + "/view.do";
		document.detailForm.submit();
	}
	
</script>