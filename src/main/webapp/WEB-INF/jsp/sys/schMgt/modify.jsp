<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 일정관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute="schMgtVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
	<table class="detail_table">
		<caption>일정관리 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><label for="code"><spring:message code="schMgtVO.code" /></label></th>
			<td>
				<c:out value="${schMgtVO.code }"/>
				<form:hidden path="code"/>
				<span class="form_error" data-path="code"><form:errors path="code" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="title"><spring:message code="schMgtVO.title" /></label></th>
			<td>
				<form:input path="title" />
				<span class="form_error" data-path="title"><form:errors path="title" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="header"><spring:message code="schMgtVO.header" /></label></th>
			<td>
				<form:textarea path="header"/>
				<span class="form_error" data-path="header"><form:errors path="header" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="footer"><spring:message code="schMgtVO.footer" /></label></th>
			<td>
				<form:textarea path="footer" />
				<span class="form_error" data-path="footer"><form:errors path="footer" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="typeCd"><spring:message code="schMgtVO.typeCd" /></label></th>
			<td>
				<form:select path="typeCd">
					<form:option value="YEAR" label="년도별"/>
					<form:option value="MONTH" label="월별"/>
					<form:option value="WEEK" label="주별"/>
					<form:option value="DAY" label="일별"/>
				</form:select>
				<span class="form_error" data-path="typeCd"><form:errors path="typeCd" /></span>
				
				<c:if test="${schMgtVO.code eq 'SCH01' }">
					<script type="text/javascript">
						$("#typeCd option").not(":selected").attr("disabled", "disabled");
					</script>
				</c:if>
			</td>
		</tr>
		<tr>
			<th><label for="skinCd"><spring:message code="schMgtVO.skinCd" /></label></th>
			<td>
				<form:select path="skinCd">
					<c:forEach var="cd" items="${skinCode.list }">
						<form:option value="${cd.cd}" label="${cd.cdNm}" />
					</c:forEach>
				</form:select>
				<span class="form_error" data-path="skinCd"><form:errors path="skinCd" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="fileYn"><spring:message code="schMgtVO.fileYn" /></label></th>
			<td>
				<form:radiobutton path="fileYn" value="Y" label="Y"/>
				<form:radiobutton path="fileYn" value="N" label="N"/>
				<span class="form_error" data-path="fileYn"><form:errors path="fileYn" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="fileCnt"><spring:message code="schMgtVO.fileCnt" /></label></th>
			<td>
				<form:input path="fileCnt" />
				<span class="form_error" data-path="fileCnt"><form:errors path="fileCnt" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="fileSize"><spring:message code="schMgtVO.fileSize" /></label></th>
			<td>
				<form:input path="fileSize" />
				<span class="form_error" data-path="fileSize"><form:errors path="fileSize" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="fileExt"><spring:message code="schMgtVO.fileExt" /></label></th>
			<td>
				<form:input path="fileExt" />
				<span class="form_error" data-path="fileExt"><form:errors path="fileExt" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="cateYn"><spring:message code="schMgtVO.cateYn" /></label></th>
			<td>
				<form:radiobutton path="cateYn" value="Y" label="Y"/>
				<form:radiobutton path="cateYn" value="N" label="N"/>
				<span class="form_error" data-path="cateYn"><form:errors path="cateYn" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="category"><spring:message code="schMgtVO.category" /></label></th>
			<td>
				<form:input path="category" />
				<span class="form_error" data-path="category"><form:errors path="category" /></span>
			</td>
		</tr>
		<tr>
			<th><label for="supplYn"><spring:message code="schMgtVO.supplYn"/></label></th>
			<td>
				<form:radiobutton path="supplYn" value="Y" label="Y"/>
				<form:radiobutton path="supplYn" value="N" label="N"/>
				<span class="form_error" data-path="supplYn"><form:errors path="supplYn"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl01Title"><spring:message code="schMgtVO.suppl01Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl01Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl01Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl01Title" cssClass="supplTitle" data-id="suppl01Yn"/>
				<span class="form_error" data-path="suppl01Title"><form:errors path="suppl01Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl02Title"><spring:message code="schMgtVO.suppl02Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl02Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl02Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl02Title" cssClass="supplTitle" data-id="suppl02Yn"/>
				<span class="form_error" data-path="suppl02Title"><form:errors path="suppl02Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl03Title"><spring:message code="schMgtVO.suppl03Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl03Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl03Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl03Title" cssClass="supplTitle" data-id="suppl03Yn"/>
				<span class="form_error" data-path="suppl03Title"><form:errors path="suppl03Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl04Title"><spring:message code="schMgtVO.suppl04Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl04Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl04Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl04Title" cssClass="supplTitle" data-id="suppl04Yn"/>
				<span class="form_error" data-path="suppl04Title"><form:errors path="suppl04Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl05Title"><spring:message code="schMgtVO.suppl05Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl05Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl05Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl05Title" cssClass="supplTitle" data-id="suppl05Yn"/>
				<span class="form_error" data-path="suppl05Title"><form:errors path="suppl05Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl06Title"><spring:message code="schMgtVO.suppl06Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl06Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl06Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl06Title" cssClass="supplTitle" data-id="suppl06Yn"/>
				<span class="form_error" data-path="suppl06Title"><form:errors path="suppl06Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl07Title"><spring:message code="schMgtVO.suppl07Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl07Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl07Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl07Title" cssClass="supplTitle" data-id="suppl07Yn"/>
				<span class="form_error" data-path="suppl07Title"><form:errors path="suppl07Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl08Title"><spring:message code="schMgtVO.suppl08Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl08Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl08Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl08Title" cssClass="supplTitle" data-id="suppl08Yn"/>
				<span class="form_error" data-path="suppl08Title"><form:errors path="suppl08Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl09Title"><spring:message code="schMgtVO.suppl09Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl09Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl09Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl09Title" cssClass="supplTitle" data-id="suppl09Yn"/>
				<span class="form_error" data-path="suppl09Title"><form:errors path="suppl09Title"/></span>
			</td>
		</tr>
		<tr class="suppl">
			<th><label for="suppl10Title"><spring:message code="schMgtVO.suppl10Title"/></label></th>
			<td>
				<div>
					<form:radiobutton path="suppl10Yn" cssClass="supplYn" value="Y" label="사용"/>
					<form:radiobutton path="suppl10Yn" cssClass="supplYn" value="N" label="미사용"/>
				</div>
				<form:input path="suppl10Title" cssClass="supplTitle" data-id="suppl10Yn"/>
				<span class="form_error" data-path="suppl10Title"><form:errors path="suppl10Title"/></span>
			</td>
		</tr>
	</table>
	
	<table class="detail_table">
		<caption>일정관리 권한 등록</caption>
		<colgroup>
   			<col width="150"/>
   			<col width="?"/>
   		</colgroup>
		<tr>
			<th><spring:message code="schMgtVO.listAuth" /></th>
			<td>				
				<c:if test='${!empty authList}'>
					<ul class="check_list">
						<c:forEach var="auth" items="${authList }" varStatus="i">
							<c:set var="authCheck" value="false" />
							<c:forEach var="schAuth" items="${schMgtVO.listAuth }">
						        <c:if test="${schAuth.authCd eq auth.authCd }"><c:set var="authCheck" value="true" /></c:if>
							</c:forEach>
							<li>
								<label for="listAuth_<c:out value='${i.count }'/>" class="check">
									<input type="checkbox" id="listAuth_<c:out value='${i.count }'/>" name="schListAuth" value="<c:out value="${auth.authCd }"/>" <c:if test='${authCheck }'>checked="checked"</c:if>><i></i>
									<c:out value="${auth.authNm }"/>
								</label>
							</li>
						</c:forEach>
					</ul>		
				</c:if>
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.viewAuth" /></th>
			<td>
				<c:if test='${!empty authList}'>
					<ul class="check_list">
						<c:forEach var="auth" items="${authList }" varStatus="i">
							<c:set var="authCheck" value="false" />
							<c:forEach var="schAuth" items="${schMgtVO.viewAuth }">
						        <c:if test="${schAuth.authCd eq auth.authCd }"><c:set var="authCheck" value="true" /></c:if>
							</c:forEach>
							<li>
								<label for="viewAuth_<c:out value='${i.count }'/>" class="check">
									<input type="checkbox" id="viewAuth_<c:out value='${i.count }'/>" name="schViewAuth" value="<c:out value="${auth.authCd }"/>" <c:if test='${authCheck }'>checked="checked"</c:if>><i></i>
									<c:out value="${auth.authNm }"/>
								</label>
							</li>
						</c:forEach>
					</ul>		
				</c:if>
			</td>
		</tr>
		<tr>
			<th><spring:message code="schMgtVO.regiAuth" /></th>
			<td>
				<c:if test='${!empty authList}'>
					<ul class="check_list">
						<c:forEach var="auth" items="${authList }" varStatus="i">
							<c:set var="authCheck" value="false" />
							<c:forEach var="schAuth" items="${schMgtVO.regiAuth }">
						        <c:if test="${schAuth.authCd eq auth.authCd }"><c:set var="authCheck" value="true" /></c:if>
							</c:forEach>
							<li>
								<label for="regiAuth_<c:out value='${i.count }'/>" class="check">
									<input type="checkbox" id="regiAuth_<c:out value='${i.count }'/>" name="schRegiAuth" value="<c:out value="${auth.authCd }"/>" <c:if test='${authCheck }'>checked="checked"</c:if>><i></i>
									<c:out value="${auth.authNm }"/>
								</label>
							</li>
						</c:forEach>
					</ul>		
				</c:if>
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
   	<input type="hidden" name="sId" value="<c:out value='${schMgtVO.code}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="schMgtVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="/html/ckeditor/ckeditor.js"></script> <% /** 에디터 사용시 */%>
<script type="text/javaScript" defer="defer">
	
	$(function(){
		
		//초기화면 SET
		var supplYn = '<c:out value="${schMgtVO.supplYn}"/>';
		if(supplYn == "Y"){
			$("tr.suppl").css("display", "table-row");
		} else {
			$("tr.suppl").css("display", "none");
		}
		
		$("input[name='supplYn']").click(function(){
			if($(this).val() == "Y"){
				$("tr.suppl").css("display", "table-row");
			} else {
				$("tr.suppl").css("display", "none");
			}
		})
		
		$(".supplTitle").prop("disabled", true);
		$(".supplYn").each(function(i, item){
			if($(item).is(':checked') && $(item).val() == "Y"){
				$(".supplTitle[data-id='"+$(item).attr("name")+"']").prop("disabled", false);
			}
		});
		
		$(".supplYn").click(function(){
			if($(this).is(':checked') && $(this).val() == "Y"){
				$(".supplTitle[data-id='"+$(this).attr("name")+"']").prop("disabled", false);
			} else {
				$(".supplTitle[data-id='"+$(this).attr("name")+"']").prop("disabled", true);
			}
		})
		
		//웹에디터 사용시
		var editorArray = [{ id : "header" , useUpload : "Y" },{ id : "footer" , useUpload : "Y" }];
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
		if (!validateSchMgtVO(frm)) {
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