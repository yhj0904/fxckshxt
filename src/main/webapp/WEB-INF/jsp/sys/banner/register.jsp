<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: register.jsp
 * @Description : 배너 등록 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 * @ 2023.10.02		신한나			배너 내용추가
 */
%>
<form:form modelAttribute
	<table class="detail_table">
		<caption>배너 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="bannerCd"><spring:message code="bannerVO.bannerCd" /></label></th>
			<td>
				<form:select path="bannerCd">
					<c:if test="${!empty commCd }">								
						<c:forEach var="cd" items="${commCd.list }">
							<form:option value="${cd.cd }" label="${cd.cdNm }" />
						</c:forEach>				
					</c:if>
				</form:select>
				<span class="form_error" data-path="bannerCd"><form:errors path="bannerCd" /></span>
			</td>
		</tr>
   		<tr>
			<th><label for="bannerNm"><spring:message code="bannerVO.bannerNm" /></label></th>
			<td>
				<form:input path="bannerNm" />
				<span class="form_error" data-path="bannerNm"><form:errors path="bannerNm" /></span>
			</td>
		</tr>
		<tr class="banner-content" style="display: none;">
			<th><label for="bannerCont"><spring:message code="bannerVO.bannerCont" /></label></th>
			<td>
				<form:textarea path="bannerCont"/>
				<span class="form_error" data-path="bannerCont"><form:errors path="bannerCont" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="startDttm"><spring:message code="bannerVO.startDttm" /></label></th>
			<td>
				<form:input path="startDttm" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="startDttm"><form:errors path="startDttm" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="endDttm"><spring:message code="bannerVO.endDttm" /></label></th>
			<td>
				<form:input path="endDttm" cssClass="input_date" onclick="gf_datepicker(this);"/>
				<span class="form_error" data-path="endDttm"><form:errors path="endDttm" /></span>
				
				<button class="button" type="button" onClick="setUnlimit();">무기한</button>
			</td>
		</tr>
		<tr>
			<th><label for="uploadFile"><spring:message code="text.image" /></label></th>
			<td>
				<img id="bannerFileImg" src="/images/common/no_img.png" alt="NO IMG">
   				<div>
    				<label class="file">
    					<span class="name"><spring:message code="text.file.placeholder" /></span>
    					<form:input type="file" path="uploadFile" data-img="bannerFileImg"/>
    					<span class="btn"><spring:message code="button.upload" /></span>
    				</label>
   				</div>
				<span class="form_error" data-path="uploadFile"><form:errors path="uploadFile" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="imgExplain"><spring:message code="bannerVO.imgExplain" /></label></th>
			<td>
				<form:input path="imgExplain" />
				<span class="form_error" data-path="imgExplain"><form:errors path="imgExplain" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="bannerLink"><spring:message code="bannerVO.bannerLink" /></label></th>
			<td>
				<form:input path="bannerLink" />
				<span class="form_error" data-path="bannerLink"><form:errors path="bannerLink" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="useYn"><spring:message code="bannerVO.useYn" /></label></th>
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
				<a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a>
			</li>
			<li>
				<a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a>
			</li>
		</ul>
	</div>
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="bannerVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	
	
	$(function(){
		
		//에디터사용시
		var editorArray = [{ id : "bannerCont" , useUpload : 'N' }];
		gf_initCkEditor(editorArray);
		
		//메인슬라이드 내용
		if($("#bannerCd").val() == "MAIN_SLIDE"){
			$(".banner-content").show();
		} else{
			$(".banner-content").hide();
		}
		$("#bannerCd").on("change", function(){
			if($(this).val() == "MAIN_SLIDE"){
				$(".banner-content").show();
			} else{
				$(".banner-content").hide();
			}
		});
	});

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_registerAction() {
		frm = document.detailForm;
		if (!validateBannerVO(frm)) {
			return;
		} else {
			frm.action = GV_PRESENT_PATH + "/registerAction.do";
			frm.submit();
		}
	}
	
	//무기한 - 날짜 자동 SET
	function setUnlimit(){
		$("#startDttm").val('1900-01-01');
		$("#endDttm").val('2999-12-31');
		/* alert($("#popupSdt").val().date().add(10)); */
	}
	
</script>