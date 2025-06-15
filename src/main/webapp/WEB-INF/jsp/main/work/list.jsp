<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: list.jsp
 * @Description : 프로그램 목록  화면
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.16		신한나			최초생성
 */
%>
<div class="board_wrap">
	
	<% //게시글 유형 %>
	<div class="search-form-wr">
		<form name="boardSearchForm" class="board_search_form" method="post" autocomplete="off">
			<div class="search_box">
				<select name="so" title="<spring:message code="search.choose"/>">
					<option value="deptNm" <c:if test="${search.so eq 'deptNm'}">selected</c:if>>학과</option>
					<c:if test='${bbsMgtVO.nonameYn ne "Y" }'>
						<option value="writer" <c:if test="${search.so eq 'writer'}">selected</c:if>><spring:message code="bbsVO.writer"/></option>
						<option value="inptId" <c:if test="${search.so eq 'inptId'}">selected</c:if>><spring:message code="bbsVO.inptId"/></option>
					</c:if>			
				</select>
				<input type="text" name="sv" value="<c:out value='${search.sv }'/>" onkeydown="if(event.keyCode==13){fn_search();}" title="<spring:message code="search.keyword"/>">		
				<button type="button" class="search_btn" onclick="fn_search();"><spring:message code="button.search"/></button>
				<button type="button" class="reset_btn" onclick="gf_reset();"><spring:message code="button.search.reset"/></button>
			</div>
		</form>
	</div>
	<form:form id="listForm" name="listForm" method="post" autocomplete="off">
		<span class="board_total_count"><spring:message code="board.total"/> <b><c:out value='${paging.totalCount}'/></b><spring:message code="board.row"/></span>
		<% //게시글 유형 %>
		<c:if test='${bbsMgtVO.cateYn eq "Y" and !empty bbsMgtVO.categoryList }'>
			<div class="table_category">
				<ul>
					<li <c:if test="${sCate eq 'ALL' }">class="active"</c:if>><a href="javascript:fn_changeCategory('ALL');"><spring:message code="search.all"/></a></li>
					<c:forEach var="cate" items="${bbsMgtVO.categoryList }" varStatus="i">
						<li <c:if test="${sCate eq cate}">class="active"</c:if>><a href="javascript:fn_changeCategory('<c:out value='${cate }'/>');"><c:out value='${cate }'/></a></li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<% //게시글 유형 %>
		<div class="scroll">
		<table class="list_table2">
			<colgroup>
				<c:if test='${bbsMgtVO.adminUser}'>
					<col style="width: 5%;" /> <!-- CHECKBOX -->
				</c:if>
				<col class="number" style="width: 5%;" /> <!-- NO -->
				<col style="width: 17%;" /> <!-- 이름 -->
				<col class="mobile" style="width: auto;" /> <!-- 학과 -->
				<col class="mobile" style="width: 20%;" /> <!-- 작성일 -->
			</colgroup>
			<thead>
				<tr>
					<c:if test='${bbsMgtVO.adminUser}'>
						<th><label><input type="checkbox" id="checkAllRow" onclick="fn_checkAllRow();"><span class="none"><spring:message code="board.checkAll" /></span></label></th>
					</c:if>
					<th>순번</th>
					<th>이름</th>
					<th <c:if test="${!empty list}">class="mobile"</c:if>>학과</th>
					<th <c:if test="${!empty list}">class="mobile"</c:if>>입력일시</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${list }" varStatus="i">
					<tr>
						<c:if test='${bbsMgtVO.adminUser}'>
							<td class="chk-box">
								<c:choose>
									<c:when test='${item.delYn eq "Y" }'>
										&nbsp;
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="checkRow" value="<c:out value="${item.labrId }"/>" onclick="fn_checkRow();"><span class="none"><spring:message code="board.check" /></span></label>
									</c:otherwise>
								</c:choose>						
							</td>
						</c:if>
						<td><c:out value="${(paging.totalCount - (paging.currentPage -1) * paging.pageSize) - i.index}"/></td>
						<td>
							<c:choose>
								<c:when test='${item.delYn eq "Y" }'>
									<spring:message code="board.deleteboard" />
								</c:when>
								<c:otherwise>
<%--									<a class="link" href="javascript:fn_detailView('<c:out value="${item.labrId }"/>');" title="<spring:message code="board.detail"/>">--%>
<%--									<a class="link" href="javascript:fn_chkPw('<c:out value="${item.labrId }"/>');" title="<spring:message code="board.detail"/>">--%>
									<a class="link" href="/labr/view.do?sId=<c:out value="${item.labrId }"/>" title="<spring:message code="board.detail"/>">
										<c:out value="${item.userNm}" escapeXml="false" />
									</a>
									<c:if test='${item.secret eq "Y"}'>
										<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="mobile">
							<div class="tit">
								<c:out value="${item.deptNm}"/>
							</div>
						</td>
						<td class="mobile"><c:out value='${fn:substring(item.inptDttm, 0, 10)}'/></td>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr>
						<c:choose>
							<c:when test="${bbsMgtVO.adminUser}">
								<td class="no_data" colspan="5"><spring:message code="board.noData" /></td>
							</c:when>
							<c:otherwise>
								<td class="no_data" colspan="4"><spring:message code="board.noData" /></td>	
							</c:otherwise>
						</c:choose>
					</tr>
				</c:if>
			</tbody>
		</table>
		</div>
		   	
		<div class="btn_wrap">
			<ul>
<%--				<c:if test='${bbsMgtVO.adminUser or bbsMgtVO.regiAuthYn}'>--%>
					<li><a class="button register_btn" href="javascript:fn_registerView();"><spring:message code="button.register" /></a></li>
<%--				</c:if>--%>
				<c:if test='${bbsMgtVO.adminUser}'>
					<li><a class="button remove_btn" href="javascript:fn_removeCheckedRow();"><spring:message code="button.checkRemove" /></a></li>
				</c:if>
				<c:if test='${bbsMgtVO.adminUser}'>
					<li><a class="button list_btn" href="javascript:fn_excelDown();"><spring:message code="button.exceldown"/></a></li>
				</c:if>
				<c:if test='${bbsMgtVO.adminUser}'>
					<li><a class="button list_btn" href="javascript:fn_excelDownAll();"><spring:message code="button.excelDownAll"/></a></li>
				</c:if>
			</ul>
		</div>
				
		<% /** 페이징 */ %>
		<c:import url="/WEB-INF/jsp/cmmn/paging.jsp"></c:import>
		<% /** //페이징 */ %>
		<% /** 이중방지 토큰 */ %>
		<double-submit:preventer/>
	   	<% /** 검색조건 유지 */ %>
	   	<input type="hidden" name="sId">
		<input type="hidden" name="sCode" value="<c:out value='${sCode }'/>">
		<input type="hidden" name="sCate" value="<c:out value='${sCate }'/>">
	   	<input type="hidden" name="checkedSId">
	  	<c:import url="/WEB-INF/jsp/cmmn/search_cond.jsp"></c:import>
	  	<% /** //검색조건 유지 */ %>
	</form:form>
	<script type="text/javaScript" defer="defer">
	
		//검색
		function fn_search(){
			var searchFrm = document.boardSearchForm;
			document.getElementById("_search_so_").value = searchFrm.so.value;
			document.getElementById("_search_sv_").value = searchFrm.sv.value;
			gf_movePage(1);
		}
		
		//초기화
		function fn_reset() {
			document.getElementById("_search_so_").value = "";
			document.getElementById("_search_sv_").value = "";
			location.href = GV_PRESENT_PATH + "/list.do";
		}
		
		//Row 선택
		function fn_checkRow() {
			var chkCnt = 0;
			var rowArr = document.getElementsByName("checkRow");
			var rowCnt = rowArr.length;
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox" && rowArr[i].checked) chkCnt++;
			}
			if(!gf_isNull(document.getElementById("checkAllRow"))){
				if (chkCnt == rowCnt) {
					document.getElementById("checkAllRow").checked = true;
				} else {
					document.getElementById("checkAllRow").checked = false;
				}
			}
		}
		
		//Row 전체선택
		function fn_checkAllRow() {
			var rowArr = document.getElementsByName("checkRow");
			var checked = document.getElementById("checkAllRow").checked;
			if (checked) {
				for (var i = 0; i < rowArr.length; i++) {
					if (rowArr[i].type == "checkbox") rowArr[i].checked = true;
				}
			} else {
				for (var i = 0; i < rowArr.length; i++) {
					if (rowArr[i].type == "checkbox") rowArr[i].checked = false;
				}
			}
		}
		
		//목록
		function fn_listView() {
			document.detailForm.action = GV_PRESENT_PATH + "/list.do";
			document.detailForm.submit();
		}
		
		//등록
		function fn_registerView() {
			document.listForm.action = GV_PRESENT_PATH + "/register.do"; //labr/register.do
			document.listForm.submit();
		}
		
		//선택삭제
		function fn_removeCheckedRow() {
			var checkedSId = "";
			var chkCnt = 0;
			var rowArr = document.getElementsByName("checkRow");
			var rowCnt = rowArr.length;
			for (var i = 0; i < rowArr.length; i++) {
				if (rowArr[i].type == "checkbox" && rowArr[i].checked){
					if(i != 0){
						checkedSId += "|";
					}
					checkedSId += rowArr[i].value;
					chkCnt++;
				}
			}
			if(chkCnt <= 0) {
				alert('<spring:message code="message.alert.nocheck" />');
			} else {
				var msg = confirm('<spring:message code="message.confirm.remove" />');
				if(msg == true){				
					document.listForm.checkedSId.value = checkedSId;
					document.listForm.action = GV_PRESENT_PATH + "/checkRemoveAction.do";
					document.listForm.submit();
				}
			}
		}
	
		//상세보기
		function fn_detailView(sId) {
			document.listForm.sId.value = sId;
			document.listForm.action = GV_PRESENT_PATH  + "/view.do"; //labr/view.do
			document.listForm.submit();
		}
		
		//엑셀다운
		function fn_excelDown() {
		    // 선택된 체크박스의 값을 수집
		    var checkedValues = document.querySelectorAll('input[name="checkRow"]:checked');
		    if (checkedValues.length === 0) {
		        alert('다운로드할 항목을 선택해주세요.');
		        return;
		    }
		
		    // form 데이터 초기화
		    var form = document.listForm;
		    form.action = GV_PRESENT_PATH + "/excelDown.do";
		
		    // 기존에 추가된 labrIds hidden input이 있다면 제거
		    var existingIdsInput = document.getElementById('labrIds');
		    if (existingIdsInput) {
		        form.removeChild(existingIdsInput);
		    }
		
		    // 선택된 항목의 labrId를 수집하여 hidden input에 설정
		    var labrIdsInput = document.createElement('input');
		    labrIdsInput.type = 'hidden';
		    labrIdsInput.name = 'labrIds';
		    labrIdsInput.id = 'labrIds';
		    labrIdsInput.value = Array.from(checkedValues).map(input => input.value).join(',');
		    form.appendChild(labrIdsInput);
		
		    // form 제출
		    form.submit();
		}

		//엑셀다운
		function fn_excelDownAll() {
			var form = document.listForm;
			form.action = GV_PRESENT_PATH + "/excelDownAll.do";
			form.submit();
		}
	</script>
</div>
