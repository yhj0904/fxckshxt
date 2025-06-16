<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 프로그램 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.11		신한나			최초생성
 */
%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="nowDate"><fmt:formatDate value="${now}" pattern="MM" /></c:set> 

<form:form modelAttribute="progVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	
	<form:hidden path="progId"/>
	<form:hidden path="semstrCd"/>
	
	<div class="prog-tb">
	   	<table class="detail_table">
			<colgroup>
	   			<col width="150"/>
	   			<col width="?"/>
	   			<col width="150"/>
	   			<col width="?"/>
	   		</colgroup>
	   		<tbody>
	   			<tr>
	   				<th><label for="progNm"><spring:message code="progVO.progNm" /></label></th>
	   				<td colspan="3">
	   					<input id="progNm" name="progNm" class="w_full" type="text" value="${progVO.progNm }">
						<span class="form_error" data-path="progNm"><form:errors path="progNm" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="statusCd"><spring:message code="progVO.statusCd" /></label></th>
	   				<td colspan="3">
	   					<form:select path="statusCd">
							<form:option value="" label="--선택--" />
							<c:forEach var="progStatus" items="${progStatus }">
								<form:option value="${progStatus.cd}" label="${progStatus.cdNm}" />
							</c:forEach>
						</form:select>
	   					<span class="form_error" data-path="statusCd"><form:errors path="statusCd" /></span>
	   				</td>
	   			</tr>
	   			<tr>
	   				<th><label for="progCareerCd"><spring:message code="progVO.progCareerCd" /></label></th>
	   				<td colspan="3">
	   					<form:select path="progCareerCd">
							<form:option value="" label="--선택--" />
							<c:forEach var="progCareer" items="${progCareer }">
								<form:option value="${progCareer.cd}" label="${progCareer.cdNm}" />
							</c:forEach>
						</form:select>
	   					<span class="form_error" data-path="progCareerCd"><form:errors path="progCareerCd" /></span>
	   				</td>
	   			</tr>
	   			<tr>
	   				<th><label for="progTypeCd"><spring:message code="progVO.progTypeCd" /></label></th>
	   				<td colspan="3">
						<c:forEach var="item" items="${progType }" varStatus="i">
							<label class="radio" for="progTypeCd<c:out value='${i.count }'/>">
								<form:radiobutton path="progTypeCd" value="${item.cd }"/><i></i><c:out value='${item.cdNm }'/>
							</label>
						</c:forEach>
						<span class="form_error" data-path="progTypeCd"><form:errors path="progTypeCd" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="progMthCd"><spring:message code="progVO.progMthCd" /></label></th>
	   				<td>
						<form:select path="progMthCd">
							<form:option value="" label="--선택--" />
							<c:forEach var="progMth" items="${progMth }">
								<form:option value="${progMth.cd}" label="${progMth.cdNm}" />
							</c:forEach>
						</form:select>
						<span class="form_error" data-path="progMthCd"><form:errors path="progMthCd" /></span>
					</td>
					<th><label for="reqstNmpr"><spring:message code="progVO.reqstNmpr" /></label></th>
	   				<td>
						<input type="number" id="reqstNmpr" name="reqstNmpr" value="${progVO.reqstNmpr }" />
						<span class="form_error" data-path="reqstNmpr"><form:errors path="reqstNmpr" /></span>
					</td>
	   			</tr>
	   			
	   			<tr class="mr-info mr-info01">
	   				<th><label for="progPlace"><spring:message code="progVO.progPlace" /></label></th>
	   				<td colspan="3">
						<form:input path="progPlace" cssClass="w_full" />
						<span class="form_error" data-path="progPlace"><form:errors path="progPlace" /></span>
					</td>
				</tr>
				<tr class="mr-info mr-info02">
	   				<th><label for="progContactCd"><spring:message code="progVO.progContactCd" /></label></th>
	   				<td>
						<c:forEach var="item" items="${progContact }" varStatus="i">
							<label class="radio" for="progContactCd<c:out value='${i.count }'/>">
								<form:radiobutton path="progContactCd" value="${item.cd }"/><i></i><c:out value='${item.cdNm }'/>
							</label>
						</c:forEach>
						<span class="form_error" data-path="progContactCd"><form:errors path="progContactCd" /></span>
					</td>
				</tr>
				<tr class="mr-info mr-info03">
	   				<th><label for="progUrl"><spring:message code="progVO.progUrl" /></label></th>
	   				<td colspan="3">
						<form:input path="progUrl" cssClass="w_full" />
						<span class="form_error" data-path="progUrl"><form:errors path="progUrl" /></span>
					</td>
				</tr>
				
	   			<tr>
	   				<th><label for="reqstSdt"><spring:message code="progVO.reqstSdt" /></label></th>
	   				<td>
						<form:input path="reqstSdt" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>
						<span class="form_error" data-path="reqstSdt"><form:errors path="reqstSdt" /></span>
					</td>
					<th><label for="reqstEdt"><spring:message code="progVO.reqstEdt" /></label></th>
	   				<td>
						<form:input path="reqstEdt" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>
						<span class="form_error" data-path="reqstEdt"><form:errors path="reqstEdt" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="progSdt"><spring:message code="progVO.progSdt" /></label></th>
	   				<td>
						<form:input path="progSdt" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>
						<span class="form_error" data-path="progSdt"><form:errors path="progSdt" /></span>
					</td>
					<th><label for="progEdt"><spring:message code="progVO.progEdt" /></label></th>
	   				<td>
						<form:input path="progEdt" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>
						<span class="form_error" data-path="progEdt"><form:errors path="progEdt" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="progStm"><spring:message code="progVO.progStm" /></label></th>
	   				<td>
	   					<fmt:parseDate var="start" value="${progVO.progStm}" pattern="HHmm" />
	   					<fmt:formatDate pattern="HH:mm" value="${start}" var="stm" />
						<form:input path="progStm" cssClass="input_time" value="${stm }" />
						<span class="form_error" data-path="progStm"><form:errors path="progStm" /></span>
					</td>
					<th><label for="progEtm"><spring:message code="progVO.progEtm" /></label></th>
	   				<td>
	   					<fmt:parseDate var="end" value="${progVO.progEtm}" pattern="HHmm" />
	   					<fmt:formatDate pattern="HH:mm" value="${end}" var="etm" />
						<form:input path="progEtm" cssClass="input_time" value="${etm }" />
						<span class="form_error" data-path="progEtm"><form:errors path="progEtm" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="surveyYn"><spring:message code="progVO.surveyYn" /></label></th>
	   				<td>
						<label class="radio" for="surveyYn1">
							<form:radiobutton path="surveyYn" value="Y"/><i></i>Y
						</label>
						<label class="radio" for="surveyYn2">
							<form:radiobutton path="surveyYn" value="N"/><i></i>N
						</label>
						<span class="form_error" data-path="progStm"><form:errors path="surveyYn" /></span>
					</td>
					<th><label for="reqstYn"><spring:message code="progVO.reqstYn" /></label></th>
	   				<td>
						<label class="radio" for="reqstYn1">
							<form:radiobutton path="reqstYn" value="Y"/><i></i>Y
						</label>
						<label class="radio" for="reqstYn2">
							<form:radiobutton path="reqstYn" value="N"/><i></i>N
						</label>
						<span class="form_error" data-path="reqstYn"><form:errors path="reqstYn" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="surveySdt"><spring:message code="progVO.surveySdt" /></label></th>
	   				<td>
						<form:input path="surveySdt" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>	
						<span class="form_error" data-path="surveySdt"><form:errors path="surveySdt" /></span>
					</td>
					<th><label for="surveyEdt"><spring:message code="progVO.surveyEdt" /></label></th>
	   				<td>
						<form:input path="surveyEdt" cssClass="input_date" onclick="gf_datepicker(this, {dateFormat: 'yymmdd'});"/>	
						<span class="form_error" data-path="surveyEdt"><form:errors path="surveyEdt" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="rm"><spring:message code="progVO.rm" /></label></th>
	   				<td colspan="3">
						<form:textarea path="rm"/>	
						<span class="form_error" data-path="rm"><form:errors path="rm" /></span>
					</td>
	   			</tr>
	   		</tbody>
	   	</table>
   	</div>
   	<div class="prog-tb">
   		<h1>교육 담당자 정보</h1>
	   	<table class="detail_table">
			<colgroup>
	   			<col width="150"/>
	   			<col width="?"/>
	   			<col width="150"/>
	   			<col width="?"/>
	   		</colgroup>
	   		<tbody>
	   			<tr>
	   				<th><label for="eduOrgNm"><spring:message code="progVO.eduOrgNm" /></label></th>
	   				<td>
						<form:input path="eduOrgNm"/>	
						<span class="form_error" data-path="eduOrgNm"><form:errors path="eduOrgNm" /></span>
					</td>
					<th><label for="eduMngNm"><spring:message code="progVO.eduMngNm" /></label></th>
	   				<td>
						<form:input path="eduMngNm"/>	
						<span class="form_error" data-path="eduMngNm"><form:errors path="eduMngNm" /></span>
					</td>
	   			</tr>
	   			<tr>
	   				<th><label for="eduTelNo"><spring:message code="progVO.eduTelNo" /></label></th>
	   				<td>
						<form:input path="eduTelNo"/>	
						<span class="form_error" data-path="eduTelNo"><form:errors path="eduTelNo" /></span>
					</td>
					<th><label for="eduEmail"><spring:message code="progVO.eduEmail" /></label></th>
	   				<td>
						<form:input path="eduEmail"/>	
						<span class="form_error" data-path="eduEmail"><form:errors path="eduEmail" /></span>
					</td>
	   			</tr>
	   		</tbody>
	   	</table>
   	</div>
 	<div class="prog-tb">
 		<table class="detail_table">
			<colgroup>
	   			<col width="150"/>
	   			<col width="?"/>
	   		</colgroup>
	   		<tbody>
	   			<tr>
	   				<th><label for="progSumry"><spring:message code="progVO.progSumry" /></label></th>
   					<td><form:textarea path="progSumry"/></td>
   				</tr>
	   			<tr>
	   				<th><label for="progFileNo"><spring:message code="progVO.progFileNo" /></label></th>
	   				<td>
	   				${viewFile }
	   					<c:choose>
		   					<c:when test="${progVO.viewFile ne null}">
		   						<div style="width: 30%;">
		   							<img id="userFileImg" src="<c:out value='${progVO.viewFile.viewUrl }'/>" alt="<c:out value='${progVO.viewFile.oname }'/>">
		   						</div>
		   					</c:when>
		   					<c:otherwise>
		   						<div style="width: 30%;">
		   							<img id="userFileImg" src="/images/common/no_img.png" alt="NO IMG">
		   						</div>
		   					</c:otherwise>
		   				</c:choose>
	   					<label class="file">
	    					<c:choose>
			   					<c:when test="${progVO.viewFile ne null}">
			   						<span class="name"><c:out value='${progVO.viewFile.oname }'/></span>
			   					</c:when>
			   					<c:otherwise>
			   						<span class="name"><spring:message code="text.file.placeholder" /></span>
			   					</c:otherwise>
			   				</c:choose>    					
	    					<form:input type="file" path="uploadFile" data-img="userFileImg"/>
	    					<span class="btn"><spring:message code="button.upload" /></span>
	    				</label>
	   				</td>
	   			</tr>
	   			<tr>
	   				<th><label for="fileNo"><spring:message code="progVO.fileNo" /></label></th>
	   				<td>
						<input type="file" id="uploadFiles" title=""/>
						<c:if test="${!empty progVO.viewFiles }">
							<ul class="file_list">
								<c:forEach var="viewFile" items="${progVO.viewFiles }" varStatus="vi">
									<li>
										<input type="hidden" id="viewFile_<c:out value="${vi.index }"/>" name="viewFiles[<c:out value="${vi.index }"/>].fno" class="viewFiles" value="0" data-val="<c:out value="${viewFile.fno }"/>"/>
										<a href="javascript:gf_download('<c:out value="${viewFile.oname }"/>','<c:out value="${viewFile.fpath }"/>','<c:out value="${viewFile.fname }"/>', <c:out value="${viewFile.fno }"/>);" title="<spring:message code="button.download" />">
											<c:out value="${viewFile.oname }"/>
										</a>
										<button type="button" data-id="viewFile_<c:out value="${vi.index }"/>" value="0" onclick="gf_removeUpload(this);">Delete</button>
									</li>
								</c:forEach>
							</ul>
						</c:if>
						<div id="uploadList" class="upload_list"></div>
					</td>
	   			</tr>
   			</tbody>
   		</table>
   	</div>
   	<div class="btn_wrap">
		<ul>
			<li><a class="button modify_btn" href="javascript:fn_modifyAction();"><spring:message code="button.modify" /></a></li>
			<li><a class="button list_btn" href="javascript:fn_listView();"><spring:message code="button.list" /></a></li>
		</ul>
	</div>
	
   	<% /** 이중방지 토큰 */ %>
	<double-submit:preventer/>
  	
   	<% /** 검색조건 유지 */ %>
   	<input type="hidden" name="sId" value="">
	<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
	<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="progVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">	
	
	$(function(){
		
		//학기 Cd SET
		//fn_setSemstrCd();
		
		//에디터 - 프로그램 설명
		var editorArray = [{ id : "progSumry" , useUpload : 'Y' }];
		gf_initCkEditor(editorArray);
	
		
		//첨부파일 사용시
		var multiOption = {
							listId : "uploadList",
							inputId : "uploadFiles",
							inputName : "uploadFiles",
							uploadCount : "10",
							uploadFileSize : "900000000",
							uploadFileExt : ""
						};
		gf_initMultiSelector(multiOption);
		
		//교육방법 선택 시 - 추가항목 DATA SET
		var prgMthCd = '<c:out value="${progVO.progMthCd}"/>';
		if(prgMthCd == "PROG_MTH_010"){
			$("tr.mr-info01").show();
		} else if(prgMthCd == "PROG_MTH_020"){
			$("tr.mr-info02").show();
		} else if(prgMthCd == "PROG_MTH_030"){
			$("tr.mr-info03").show();
		}
		
		//교육방법 선택 시 - 추가항목
		$("#progMthCd").on("change", function(){
			var prgMthCd = $(this).val();
			if(prgMthCd == "PROG_MTH_010"){
				$("tr.mr-info").hide();
				$("tr.mr-info02 input").prop('checked', false);
				$("tr.mr-info03 input").val('');
				$("tr.mr-info01").show();
			} else if(prgMthCd == "PROG_MTH_020"){
				$("tr.mr-info").hide();
				$("tr.mr-info01 input").val('');
				$("tr.mr-info02").show();
			} else if(prgMthCd == "PROG_MTH_030"){
				$("tr.mr-info").hide();
				$("tr.mr-info02 input").prop('checked', false);
				$("tr.mr-info03 input").val('');
				$("tr.mr-info01").show();
			}
		});
		
		//프로그램 접근방식 - 온라인
		var progContactCd = '<c:out value="${progVO.progContactCd}"/>';
		if(prgMthCd == "PROG_MTH_020" && progContactCd == "PROG_CONTACT_010"){
			$("tr.mr-info03").show();
		} else{
			$("tr.mr-info03").hide();
		}
		
		//프로그램 접근방식 - 온라인
		$("input[name='progContactCd']").on("click", function(){
			var progContactCd = $(this).val();
			if(progContactCd == "PROG_CONTACT_010"){
				$("tr.mr-info03").show();
			} else{
				$("tr.mr-info03").hide();
				$("tr.mr-info03 input").val('');
			}
		});
		
		//설문시작일자 SET
		$("#progSdt").on("propertychange change keyup paste input", function(){
			var progSdt = $(this).val();
			console.log(progSdt);
			$("#surveySdt").val(progSdt);
		});
		
		//설문종료일자 SET
		$("#progEdt").on("propertychange change keyup paste input", function(){
			var progEdt = $(this).val();
			console.log(progEdt);
			$("#surveyEdt").val(progEdt);
		});
		
		//시간 설정
		$('#progStm, #progEtm').timepicker({
			timeFormat: 'HH:mm',
			interval: 10,
			minTime: '09',
			maxTime: '23:00pm',
			startTime: '09:00',
			dynamic: false,
			dropdown: true,
			scrollbar: true
		});
		
	});
	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//등록 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validateProgVO(frm)) {
			return;
		} else if(gf_nvl(frm.progMthCd.value, "") == "PROG_MTH_010" && gf_isNull(frm.progPlace.value)){
			gf_alert("교육장소를 입력해주세요.");
			return;
		} else if(gf_nvl(frm.progMthCd.value, "") == "PROG_MTH_020" && gf_isNull(frm.progContactCd.value)){
			gf_alert("프로그램 접근방식을 선택해주세요.");
			return;
		} else if(gf_nvl(frm.progMthCd.value, "") == "PROG_MTH_020" && gf_nvl(frm.progContactCd.value) == "PROG_CONTACT_010" 
						&& gf_isNull(frm.progUrl.value)){
			gf_alert("교육장소를 입력해주세요.");
			return;
		} /*else if(gf_isNull(frm.uploadFile.value)){
			gf_alert("프로그램 이미지파일을 선택해주세요.");
			return;
		} */
		else if(CKEDITOR.instances.progSumry.getData() == '' || CKEDITOR.instances.progSumry.getData().length == 0){	
       	    gf_alert("프로그램 섦명을 입력해주세요.");
       	    return false;	        
		} else {
			//시작시간
			if(!gf_isNull(frm.progStm.value)){    
				frm.progStm.value = frm.progStm.value.replaceAll(":", "");
			}
			//종료시간
			if(!gf_isNull(frm.progEtm.value)){    
				frm.progEtm.value = frm.progEtm.value.replaceAll(":", "");
			}
			
			frm.action = GV_PRESENT_PATH + "/modifyAction.do";
			frm.submit();
		}
	}
	
	//학기정보 SET
	function fn_setSemstrCd(){
		//현재 날짜
		let nw = '<c:out value="${nowDate }" />';
		//학기 비교
		if(Number(03) <= Number(nw) && Number(nw) <= Number(08)){
			$("input[name='semstrCd']").val("10");
		} else{
			$("input[name='semstrCd']").val("20");
		}
		
	}
	
</script>