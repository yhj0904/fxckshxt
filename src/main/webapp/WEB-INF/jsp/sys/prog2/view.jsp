<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: view.jsp
 * @Description : 프로그램 상세 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */
%>

<form:form modelAttribute
	
	<form:hidden path="progId"/>
	<form:hidden path="semstrCd"/>
	<form:hidden path="statusCd"/>
	
	<div class="prog-tb">
	   	<table class="detail_table">
   			<colgroup>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   		</colgroup>
	   		<tbody>
	   			<tr>
	   				<th><spring:message code="progVO.progNm" /></th>
	   				<td colspan="3"><c:out value="${progVO.progNm}" /></td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.statusCd" /></th>
	   				<td colspan="3">
	   					<c:out value="${progVO.statusCdNm}" />
	   				</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.progCareerCd" /></th>
	   				<td colspan="3">
	   					<c:out value="${progVO.progCareerCdNm}" />
	   				</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.progTypeCd" /></th>
	   				<td colspan="3">
						<c:out value="${progVO.progTypeCdNm}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.progMthCd" /></th>
	   				<td>
						<c:out value="${progVO.progMthCdNm}" />
					</td>
					<th><spring:message code="progVO.applNmpr" /> / <spring:message code="progVO.reqstNmpr" /></th>
	   				<td>
						<c:out value="${progVO.applNmpr}" /> / <c:out value="${progVO.reqstNmpr}" />
					</td>
	   			</tr>
	   			<c:if test="${!empty progVO.progPlace}">
		   			<tr>
		   				<th><spring:message code="progVO.progPlace" /></th>
		   				<td>
							<c:out value="${progVO.progPlace}" />
						</td>
					</tr>
				</c:if>
				<c:if test="${!empty progVO.progContactCdNm and !empty progVO.progContactCd}">
					<tr>
		   				<th><spring:message code="progVO.progContactCd" /></th>
		   				<td>
							<c:out value="${progVO.progContactCdNm}" />
						</td>
					</tr>
				</c:if>
				<c:if test="${!empty progVO.progUrl}">
					<tr>
		   				<th><spring:message code="progVO.progUrl" /></th>
		   				<td>
							<c:out value="${progVO.progUrl}" />
						</td>
					</tr>
				</c:if>
	   			<tr>
	   				<th><spring:message code="progVO.reqstSdt" /></th>
	   				<td>
						<c:out value="${progVO.reqstSdt}" />
					</td>
					<th><spring:message code="progVO.reqstEdt" /></th>
	   				<td>
						<c:out value="${progVO.reqstEdt}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.progSdt" /></th>
	   				<td>
						<c:out value="${progVO.progSdt}" />
					</td>
					<th><spring:message code="progVO.progEdt" /></th>
	   				<td>
						<c:out value="${progVO.progEdt}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.progStm" /></th>
	   				<td>
						<c:out value="${progVO.progStm}" />
					</td>
					<th><spring:message code="progVO.progEtm" /></th>
	   				<td>
						<c:out value="${progVO.progEtm}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.surveyYn" /></th>
	   				<td>
						<c:out value="${progVO.surveyYn}" />
						
						<c:if test="${progVO.surveyYn eq 'Y'}">
							<ul class="survey-cr" style="display: block;">
								<li><button type="button" class="button" onClick="fn_createSurv();">설문지생성</button></li>
								<li><button type="button" class="button" onClick="fn_viewSurv();">미리보기</button></li>
							</ul>
						</c:if>
					</td>
					<th><spring:message code="progVO.reqstYn" /></th>
	   				<td>
						<c:out value="${progVO.reqstYn}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.surveySdt" /></th>
	   				<td>
						<c:out value="${progVO.surveySdt}" />
					</td>
					<th><spring:message code="progVO.surveyEdt" /></th>
	   				<td>
						<c:out value="${progVO.surveyEdt}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.surveyNmpr" /></th>
	   				<td>
						<c:out value="${progVO.surveyNmpr}" />
					</td>
					<th><spring:message code="progVO.surveyScore" /></th>
	   				<td>
						<c:out value="${progVO.surveyScore}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.rm" /></th>
	   				<td colspan="3">
						<c:out value="${progVO.rm}" escapeXml="false" />
					</td>
	   			</tr>
	   		</tbody>
	   	</table>
   	</div>
   	<div class="prog-tb">
   		<h1>교육 담당자 정보</h1>
	   	<table class="detail_table">
			<colgroup>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   		</colgroup>
	   		<tbody>
	   			<tr>
	   				<th><spring:message code="progVO.eduOrgNm" /></th>
	   				<td>
						<c:out value="${progVO.eduOrgNm}" />
					</td>
					<th><spring:message code="progVO.eduMngNm" /></th>
	   				<td>
						<c:out value="${progVO.eduMngNm}" />
					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.eduTelNo" /></th>
	   				<td>
						<c:out value="${progVO.eduTelNo}" />
					</td>
					<th><spring:message code="progVO.eduEmail" /></th>
	   				<td>
						<c:out value="${progVO.eduEmail}" />
					</td>
	   			</tr>
	   		</tbody>
	   	</table>
   	</div>
 	<div class="prog-tb">
 		<table class="detail_table">
			<colgroup>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   			<col style="width: 12%;"/>
	   			<col style="width: 38%;"/>
	   		</colgroup>
	   		<tbody>
	   			<tr>
	   				<th><spring:message code="progVO.progSumry" /></th>
   					<td colspan="3"><div class="content"><c:out value="${progVO.progSumry}" escapeXml="false" /></div></td>
   				</tr>
   				<c:if test="${!empty progVO.viewFile }">
		   			<tr>
		   				<th><spring:message code="progVO.progFileNo" /></th>
		   				<td colspan="3">
		   					<div style="width: 30%;">
		   						<img src="${progVO.viewFile.viewUrl}" alt="<c:out value="${progVO.viewFile.oname }"/>" />
		   					</div>
		   					<ul class="file_list">
		   						<li>
		   							<a href="javascript:gf_download('<c:out value="${progVO.viewFile.oname }"/>','<c:out value="${progVO.viewFile.fpath }"/>','<c:out value="${progVO.viewFile.fname }"/>', <c:out value="${progVO.viewFile.fno }"/>);" title="<spring:message code="button.download" />">
		   								<c:out value="${progVO.viewFile.oname }"/>
									</a>
		   						</li>
		   					</ul>
		   				</td>
		   			</tr>
		   		</c:if>
   				<c:if test="${!empty progVO.viewFiles }">
		   			<tr>
		   				<th><spring:message code="progVO.fileNo" /></th>
		   				<td colspan="3">
			   				<ul class="file_list">
			   					<c:forEach var="viewFile" items="${progVO.viewFiles }">
									<li>
										<a href="javascript:gf_download('<c:out value="${viewFile.oname }"/>','<c:out value="${viewFile.fpath }"/>','<c:out value="${viewFile.fname }"/>', <c:out value="${viewFile.fno }"/>);" title="<spring:message code="button.download" />">
											<c:out value="${viewFile.oname }"/>
										</a>
									</li>
								</c:forEach>
							</ul>
	   					</td>
		   			</tr>
		   		</c:if>
		   		<tr>
	   				<th><spring:message code="progVO.inptId" /></th>
	   				<td>
		   				<c:out value="${progVO.inptId }"/>
   					</td>
   					<th><spring:message code="progVO.inptDttm" /></th>
	   				<td>
		   				<c:out value="${progVO.inptDttm }"/>
   					</td>
	   			</tr>
	   			<tr>
	   				<th><spring:message code="progVO.modiId" /></th>
	   				<td>
		   				<c:out value="${progVO.modiId }"/>
   					</td>
   					<th><spring:message code="progVO.modiDttm" /></th>
	   				<td>
		   				<c:out value="${progVO.modiDttm }"/>
   					</td>
	   			</tr>
   			</tbody>
   		</table>
   	</div>
   	<div class="btn_wrap">
		<ul>
			<c:if test='${loginInfo.statDvcd eq "SYS_ADM" }'>
				<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
				<li><a class="button modify_btn" href="javascript:fn_modifyView();"><spring:message code="button.modify" /></a></li>
				<li><a class="button remove_btn" href="javascript:fn_removeView();"><spring:message code="button.remove" /></a></li>
			</c:if>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="<c:out value='${progVO.progId }'/>">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="progVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	

	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록
	function fn_registerView() {
		document.detailForm.action = GV_PRESENT_PATH + "/register.do";
		document.detailForm.submit();
	}
	
	//수정
	function fn_modifyView() {
		document.detailForm.sId.value = document.detailForm.progId.value;
		document.detailForm.action = GV_PRESENT_PATH + "/modify.do";
		document.detailForm.submit();
	}
	
	//삭제
	function fn_removeView() {
		var msg = gf_confirm('<spring:message code="message.confirm.remove" />', function(e){
			if(e == "Y"){
				document.detailForm.sId.value = document.detailForm.progId.value;
				document.detailForm.action = GV_PRESENT_PATH + "/removeAction.do";
				document.detailForm.submit();
			}
		});
	}

	//설문지 생성
	function fn_createSurv() {
		gf_ajax({
			url : "/prog_surv/crtSurv.json",
			type : "POST",
			dataType : "json",
			data : { 
				progId: "<c:out value='${progVO.progId}'/>"
			},
		}).then(function(response){	
			
			//RESULT SET
			var data = gf_nvl(response, "");
			if(gf_nvl(data.result, "") == 0){
				gf_alert("설문지 생성이 완료되었습니다.");
			} else if(gf_nvl(response, "") == -1){
				gf_alert("다시 생성바랍니다.");
			}
			
		});
	}
	
	//설문지 미리보기
	function fn_viewSurv() {
		var url = "/sys/prog_surv/prev_view.do?progId="+"<c:out value='${progVO.progId}' />";
		var width = 800;
		var height = 900;
		var popupX = (window.screen.width / 2) - (width / 2);
		var popupY = (window.screen.width / 2) - (height / 2);
		window.open(url, '', 'status=no, width='+width+', height='+height+', left='+popupX+', top='+popupY+', screenX='+popupX+', screenY='+popupY+', scrollbars=yes')
	}
	
</script>