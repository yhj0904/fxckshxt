<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: modify.jsp
 * @Description : 컨텐츠관리 수정 화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<form:form modelAttribute
	<ul class="sub_ul">
		<li>
			<div class="sub_tit">
				<p>사이트정보</p>
			</div>
			<div class="sub_cont">
				<table class="detail_table">
					<caption>사이트 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><label for="siteCd"><spring:message code="siteVO.siteCd"/></label></th>
						<td>
							<form:hidden path="siteCd"/>
							<c:out value='${siteVO.siteCd }'/>
							<span class="form_error" data-path="siteCd"><form:errors path="siteCd"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteNm"><spring:message code="siteVO.siteNm"/></label></th>
						<td>
							<form:input path="siteNm"/>
							<span class="form_error" data-path="siteNm"><form:errors path="siteNm"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteEngNm"><spring:message code="siteVO.siteEngNm"/></label></th>
						<td>
							<form:input path="siteEngNm"/>
							<span class="form_error" data-path="siteEngNm"><form:errors path="siteEngNm"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="sysAccessYn"><spring:message code="siteVO.sysAccessYn" /></label></th>
						<td>
							<form:radiobutton path="sysAccessYn" value="Y" label="Y"/>
							<form:radiobutton path="sysAccessYn" value="N" label="N"/>
							<span class="form_error" data-path="sysAccessYn"><form:errors path="sysAccessYn" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="loginYn"><spring:message code="siteVO.loginYn" /></label></th>
						<td>
							<form:radiobutton path="loginYn" value="Y" label="Y"/>
							<form:radiobutton path="loginYn" value="N" label="N"/>
							<span class="form_error" data-path="loginYn"><form:errors path="loginYn" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="useYn"><spring:message code="siteVO.useYn" /></label></th>
						<td>
							<form:radiobutton path="useYn" value="Y" label="Y"/>
							<form:radiobutton path="useYn" value="N" label="N"/>
							<span class="form_error" data-path="useYn"><form:errors path="useYn" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="templateCd"><spring:message code="siteVO.templateCd"/></label></th>
						<td>
							<form:select path="templateCd">
								<form:option value="" label="--선택--" />
								<c:forEach var="template" items="${templateList }">
									<form:option value="${template.templateCd}" label="${template.templateNm}" />
								</c:forEach>
							</form:select>
							<span class="form_error" data-path="templateCd"><form:errors path="templateCd"/></span>
						</td>
					</tr>
				</table>
			</div>
		</li>
		<li>
			<div class="sub_tit">
				<p>도메인정보</p>
			</div>
			<div class="sub_cont">
				<div class="btn_wrap">
					<ul>
						<li>
							<input type="text" id="inputDomain" placeholder="도메인 입력"/>
							<a class="button" href="javascript:fn_addDomain();"><spring:message code="button.add" /></a>
						</li>
					</ul>
				</div>
				<div class="scroll_table_wrap">
					<table class="list_table ty2">
						<thead>
							<tr>
								<th><spring:message code="board.no"/></th>
								<th><spring:message code="domainVO.domain"/></th>
								<th><spring:message code="domainVO.defaultYn"/></th>
								<th><spring:message code="domainVO.useYn"/></th>
								<th><spring:message code="button.remove"/></th>
							</tr>
						</thead>
						<tbody id="domainList">
							<c:forEach var="item" items="${siteVO.domainList }" varStatus="i">
								<tr data-id="<c:out value='${item.domain }'/>">
									<td><c:out value='${i.count }'/></td>
									<td>
										<input type="hidden" class="domain" name="domainList[<c:out value='${i.index }'/>].domain" value="<c:out value="${item.domain }"/>">
										<c:out value='${item.domain }'/>
									</td>
									<td>
										<label>
											<input type="radio" class="defaultYn" name="domainList[<c:out value='${i.index }'/>].defaultYn" value="Y" <c:if test='${item.defaultYn eq "Y" }'>checked="checked"</c:if>>
											&nbsp;Y
										</label>
										<label>
											<input type="radio" class="defaultYn" name="domainList[<c:out value='${i.index }'/>].defaultYn" value="N" <c:if test='${item.defaultYn eq "N" }'>checked="checked"</c:if>>
											&nbsp;N
										</label>
									</td>
									<td>
										<label>
											<input type="radio" class="useYn" name="domainList[<c:out value='${i.index }'/>].useYn" value="Y" <c:if test='${item.useYn eq "Y" }'>checked="checked"</c:if>>
											&nbsp;Y
										</label>
										<label>
											<input type="radio" class="useYn" name="domainList[<c:out value='${i.index }'/>].useYn" value="N" <c:if test='${item.useYn eq "N" }'>checked="checked"</c:if>>
											&nbsp;N
										</label>
									</td>
									<td><a class="table_btn" href="javascript:fn_removeDomain('<c:out value="${item.domain }"/>');"><spring:message code="button.remove"/></a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty siteVO.domainList}">
								<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>
							</c:if>
						</tbody>
				  	</table>
				</div>
			</div>
		</li>
		
		<li>
			<div class="sub_tit">
				<p>추가정보</p>
			</div>
			<div class="sub_cont">
				<table class="detail_table">
					<caption>사이트 등록</caption>
					<colgroup>
			   			<col width="150"/>
			   			<col width="?"/>
			   		</colgroup>
					<tr>
						<th><label for="siteAddr"><spring:message code="siteVO.siteAddr"/></label></th>
						<td>
							<form:input path="siteAddr"/>
							<span class="form_error" data-path="siteAddr"><form:errors path="siteAddr"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteEngAddr"><spring:message code="siteVO.siteEngAddr"/></label></th>
						<td>
							<form:input path="siteEngAddr"/>
							<span class="form_error" data-path="siteEngAddr"><form:errors path="siteEngAddr"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="telNo"><spring:message code="siteVO.telNo"/></label></th>
						<td>
							<form:input path="telNo"/>
							<span class="form_error" data-path="telNo"><form:errors path="telNo"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="faxNo"><spring:message code="siteVO.faxNo"/></label></th>
						<td>
							<form:input path="faxNo"/>
							<span class="form_error" data-path="faxNo"><form:errors path="faxNo"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="uploadFile"><spring:message code="siteVO.siteLogo" /></label></th>
						<td>
							<c:choose>
			   					<c:when test="${siteVO.viewFile ne null}">
			   						<img id="siteLogoFileImg" src="<c:out value='${siteVO.viewFile.viewUrl }'/>" alt="<c:out value='${siteVO.viewFile.oname }'/>">
			   					</c:when>
			   					<c:otherwise>
			   						<img id="siteLogoFileImg" src="/images/common/no_img.png" alt="NO IMG">
			   					</c:otherwise>
			   				</c:choose>
			   				<div>
			    				<label class="file">
			    					<c:choose>
					   					<c:when test="${siteVO.viewFile ne null}">
					   						<span class="name"><c:out value='${siteVO.viewFile.oname }'/></span>
					   					</c:when>
					   					<c:otherwise>
					   						<span class="name"><spring:message code="text.file.placeholder" /></span>
					   					</c:otherwise>
					   				</c:choose>    					
			    					<form:input type="file" path="uploadFile" data-img="siteLogoFileImg"/>
			    					<span class="btn"><spring:message code="button.upload" /></span>
			    				</label>
			   				</div>
							<span class="form_error" data-path="uploadFile"><form:errors path="uploadFile" /></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteMeta"><spring:message code="siteVO.siteMeta"/></label></th>
						<td>
							<form:textarea path="siteMeta"/>
							<span class="form_error" data-path="siteMeta"><form:errors path="siteMeta"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo01"><spring:message code="siteVO.siteInfo01"/></label></th>
						<td>
							<form:input path="siteInfo01"/>
							<span class="form_error" data-path="siteInfo01"><form:errors path="siteInfo01"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo02"><spring:message code="siteVO.siteInfo02"/></label></th>
						<td>
							<form:input path="siteInfo02"/>
							<span class="form_error" data-path="siteInfo02"><form:errors path="siteInfo02"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo03"><spring:message code="siteVO.siteInfo03"/></label></th>
						<td>
							<form:input path="siteInfo03"/>
							<span class="form_error" data-path="siteInfo03"><form:errors path="siteInfo03"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo04"><spring:message code="siteVO.siteInfo04"/></label></th>
						<td>
							<form:input path="siteInfo04"/>
							<span class="form_error" data-path="siteInfo04"><form:errors path="siteInfo04"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo05"><spring:message code="siteVO.siteInfo05"/></label></th>
						<td>
							<form:input path="siteInfo05"/>
							<span class="form_error" data-path="siteInfo05"><form:errors path="siteInfo05"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo06"><spring:message code="siteVO.siteInfo06"/></label></th>
						<td>
							<form:input path="siteInfo06"/>
							<span class="form_error" data-path="siteInfo06"><form:errors path="siteInfo06"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo07"><spring:message code="siteVO.siteInfo07"/></label></th>
						<td>
							<form:input path="siteInfo07"/>
							<span class="form_error" data-path="siteInfo07"><form:errors path="siteInfo07"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo08"><spring:message code="siteVO.siteInfo08"/></label></th>
						<td>
							<form:input path="siteInfo08"/>
							<span class="form_error" data-path="siteInfo08"><form:errors path="siteInfo08"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo09"><spring:message code="siteVO.siteInfo09"/></label></th>
						<td>
							<form:input path="siteInfo09"/>
							<span class="form_error" data-path="siteInfo09"><form:errors path="siteInfo09"/></span>
						</td>
					</tr>
					<tr>
						<th><label for="siteInfo10"><spring:message code="siteVO.siteInfo10"/></label></th>
						<td>
							<form:input path="siteInfo10"/>
							<span class="form_error" data-path="siteInfo10"><form:errors path="siteInfo10"/></span>
						</td>
					</tr>
				</table>
			</div>
		</li>	
	</ul>
   	
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
   	<input type="hidden" name="sId" value="<c:out value='${siteVO.siteCd}'/>">
	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
  	<% /** //검색조건 유지 */ %>  	
</form:form>

<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<validator:javascript formName="siteVO" staticJavascript="false" xhtml="true" cdata="false"/>
<c:import url="/WEB-INF/jsp/cmmn/code_mirror.jsp"></c:import>
<script type="text/javaScript" defer="defer">

	var siteMeta;
	
	$(function(){		
		siteMeta = ComCodeMirror.edit($("#siteMeta"));	
	});
	
	//목록
	function fn_listView() {
		document.detailForm.action = GV_PRESENT_PATH + "/list.do";
		document.detailForm.submit();
	}
	
	//수정 처리
	function fn_modifyAction() {
		frm = document.detailForm;
		if (!validateSiteVO(frm)) {
			return;
		} else {
			//도메인 개수 체크
			var domainCnt = $("#domainList .domain").length;
			if(domainCnt == 0){
				alert("최소 한개의 도메인을 등록해주세요.");
				return;
			}
			
			//사용유무 체크
			var useDomainCnt = 0;
			var useDomainArr = $("#domainList .useYn:checked");
			$.each(useDomainArr, function(i, item){
				if($(item).val() == "Y"){
					useDomainCnt++;
				}
			});
			
			if(useDomainCnt == 0){
				alert("최소 한개 이상의 사용도메인이 있어야합니다.");
				return;					
			}
			
			//대표 도메인 체크
			var defaultDomainCnt = 0;
			var defaultDomainArr = $("#domainList .defaultYn:checked");
			$.each(defaultDomainArr, function(i, item){
				if($(item).val() == "Y"){
					defaultDomainCnt++;
				}
			});
			
			if(defaultDomainCnt == 0){
				alert("대표 도메인을 설정해주세요.");
				return;					
			} else if(defaultDomainCnt > 1){
				alert("대표 도메인은 한개 이상 설정하실 수 없습니다.");
				return;	
			}
			
			frm.action = GV_PRESENT_PATH + "/modifyAction.do";
			frm.submit();
		}		
	}
	
	//도메인추가
	function fn_addDomain(){
		var domain = $("#inputDomain").val();
		if(gf_isNull(domain)){
			alert("도메인을 입력해주세요.");
			$("#inputDomain").focus();
			return;
		}
		
		gf_ajax({
			url : "/sys/site/isDomainExist.json",
			type : "POST",
			data : {domain : domain},
		}).then(function(response){
			if(!response){
				if(!fn_checkDuplicateDomain()){
					fn_setDomainList(domain);
					$("#inputDomain").val("");
				} else {
					alert('<spring:message code="message.alert.existdata"/>');	
					$("#inputDomain").focus();
				}
			} else {
				alert('<spring:message code="message.alert.existdata"/>');	
				$("#inputDomain").focus();
			}
		});
	}
	
	//도메인 삭제
	function fn_removeDomain(domain){
		$("#domainList tr[data-id='"+domain+"']").remove();
		fn_setDomainList();
	}
	
	function fn_checkDuplicateDomain(){
		var result = false;
		
		return result;
	}
	
	function fn_setDomainList(domain){
		
		var str = "";
		
		//기존 데이터
		var cnt = 0;
		cnt = $("#domainList .domain").length;
		var arr = [];
		if(cnt > 0){
			for(var i=0; i<cnt; i++){
				var jsonData = {}
				jsonData['domain'] = $("#domainList tr").eq(i).find('.domain').val();
				jsonData['defaultYn'] = $("#domainList tr").eq(i).find('.defaultYn:checked').val();
				jsonData['useYn'] = $("#domainList tr").eq(i).find('.useYn:checked').val();
				arr.push(jsonData);
			}
		}
		
		if(!gf_isNull(domain)){
			var jsonData = {}
			jsonData['domain'] = domain;
			jsonData['defaultYn'] = 'N';
			jsonData['useYn'] = 'Y';
			arr.push(jsonData);
		}
		
		var domainCnt = arr.length;
		if(domainCnt > 0){
			$.each(arr, function(i, item){
				var iCount = i+1;
				str += '<tr data-id="'+item.domain+'">';
				str += '<td>'+iCount+'</td>';
				str += '<td>';
				str += '	<input type="hidden" class="domain" name="domainList['+i+'].domain" value="'+item.domain+'"/>';
				str += item.domain;
				str += '</td>';
				str += '<td>';
				str += '	<label>';
				if (item.defaultYn == "Y"){
					str += '	<input type="radio" class="defaultYn" name="domainList['+i+'].defaultYn" value="Y" checked="checked">';
				} else {
					str += '	<input type="radio" class="defaultYn" name="domainList['+i+'].defaultYn" value="Y">';
				}					
				str += '		&nbsp;Y';
				str += '	</label>';
				str += '	<label>';
				if (item.defaultYn == "N"){
					str += '	<input type="radio" class="defaultYn" name="domainList['+i+'].defaultYn" value="N" checked="checked">';
				} else {
					str += '	<input type="radio" class="defaultYn" name="domainList['+i+'].defaultYn" value="N">';
				}
				str += '		&nbsp;N';
				str += '	</label>';
				str += '</td>';
				str += '<td>';
				str += '	<label>';
				if (item.useYn == "Y"){
					str += '	<input type="radio" class="useYn" name="domainList['+i+'].useYn" value="Y" checked="checked">';
				} else {
					str += '	<input type="radio" class="useYn" name="domainList['+i+'].useYn" value="Y">';
				}				
				str += '		&nbsp;Y';
				str += '	</label>';
				str += '	<label>';
				if (item.useYn == "N"){
					str += '	<input type="radio" class="useYn" name="domainList['+i+'].useYn" value="N" checked="checked">';
				} else {
					str += '	<input type="radio" class="useYn" name="domainList['+i+'].useYn" value="N">';
				}
				str += '		&nbsp;N';
				str += '	</label>';
				str += '</td>';
				str += '<td><a class="table_btn" href="javascript:fn_removeDomain(\''+item.domain+'\');"><spring:message code="button.remove"/></a></td>';
				str += '</tr>';
			});
		} else {
			str = '<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>';
		}
		$("#domainList").html(str);
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