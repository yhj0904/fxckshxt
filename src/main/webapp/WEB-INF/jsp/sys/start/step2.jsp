<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: step2.jsp
 * @Description : 최초 사이트 등록
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="sys_start" style="padding:50px;">
	<div class="start_title">
		<p>사이트 최초 등록 화면입니다.</p>
	</div>
	<div class="start_content">
		<form:form modelAttribute="siteVO" id="detailForm" name="detailForm" method="post" autocomplete="off" enctype="multipart/form-data">
			<form:hidden path="siteCd"/>
			<form:hidden path="useYn"/>
			<form:hidden path="loginYn"/>
			<form:hidden path="sysAccessYn"/>
			
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
								<th><label for="siteNm"><spring:message code="siteVO.siteNm"/></label></th>
								<td>
									<form:input path="siteNm"/>
									<span class="form_error" data-path="siteNm"><form:errors path="siteNm"/></span>
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
			</ul>
		   	
		   	<div class="btn_wrap">
				<ul>
					<li>
						<a class="button register_btn" href="javascript:fn_registerAction();"><spring:message code="button.register" /></a>
					</li>
				</ul>
			</div>
			
		   	<% /** 이중방지 토큰 */ %>
			<double-submit:preventer/>
			
		</form:form>
	</div>
	
	<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
	<validator:javascript formName="siteVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" defer="defer">	
		
		//등록 처리
		function fn_registerAction() {
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
				
				frm.action = "/sys/start/step2Action.do";
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
		
	</script>
</div>