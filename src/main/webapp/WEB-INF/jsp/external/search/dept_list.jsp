<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: dept_list.jsp
 * @Description : 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="pop_wrap">
	<form:form id="listForm" name="listForm" method="post" autocomplete="off">
		<div class="tree_check_wrap">
			<% /** 트리 구조를 위한 LEVEL 변수 */ %>
			<c:set var="treeLevel" value="1"/>
			<ul class="tree_check_list">
				<c:forEach var="item" items="${list }" varStatus="i">
					<% /** 현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
					<c:choose>
						<c:when test="${i.index eq 0}"></c:when>
						<c:when test="${item.deptLvl > treeLevel}">
							<c:out value='<ul data-id="${item.hiDeptCd }">' escapeXml='false'/>
						</c:when>
						<c:when test="${item.deptLvl < treeLevel}">
							<c:out value='</li>' escapeXml="false"/>
							<c:forEach begin="${item.deptLvl }" end="${treeLevel - 1}" step="1">
								<c:out value='</ul></li>' escapeXml="false"/>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:out value='</li>' escapeXml="false"/>
						</c:otherwise>
					</c:choose>
					<c:out value='<li>' escapeXml="false"/>
					<% /** //현재 LEVEL과 비교하여 리스트 태그 생성*/ %>
					
					<a href="#open_tree" class="tree_item <c:if test='${item.childCnt > 0 }'>folder</c:if>" data-id="<c:out value='${item.deptCd }' />"><span class="none">FILE</span></a>
					<label for="tree_<c:out value='${i.count }'/>">
						<input type="checkbox" id="tree_<c:out value='${i.count }'/>" name="checkDeptRow" class="tree_check" value="<c:if test='${item.childCnt eq 0 }'><c:out value='${item.deptCd }' /></c:if>" data-id="<c:out value='${item.deptCd }' />">
						<c:out value='${item.deptNmKor }' />(<c:out value='${item.deptCd }' />)
					</label>
					
					<% /** 트리 구조를 위한 LEVEL 변수 */ %>
					<c:set var="treeLevel" value="${item.deptLvl}"/>
					
					<% /** 마지막인경우 모두 닫는다 */ %>
					<c:if test="${i.last}">
						<c:out value='</li>' escapeXml="false"/>
						<c:if test="${item.deptLvl > 1 }">
							<c:forEach begin="1" end="${item.deptLvl - 1}" step="1">
								<c:out value="</ul></li>" escapeXml="false"/>
							</c:forEach>
						</c:if>
					</c:if>
					<% /** //마지막인경우 모두 닫는다 */ %>
				</c:forEach>
			</ul>
	  	</div>
	</form:form>
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button register_btn" href="javascript:fn_registUserList();"><spring:message code="button.register" /></a>
				</li>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>

<script type="text/javaScript" defer="defer">

	function fn_sendData(){
		var params = [];
		var chkCnt = 0;
		var rowArr = document.getElementsByName("checkDeptRow");
		var rowCnt = rowArr.length;
		for (var i = 0; i < rowArr.length; i++) {
			if (rowArr[i].type == "checkbox" && rowArr[i].checked && !gf_isNull(rowArr[i].value)){
				params.push(rowArr[i].value);
				chkCnt++;
			}
		}
		if(chkCnt <= 0) {
			alert('<spring:message code="message.alert.nocheck" />');
		} else {
			opener.parent.fn_setDeptList(params);
			self.close();
		}
	}
	
	function fn_closePop(){
		self.close();
	}

	$(document).on("click", ".tree_item", function(){
		var deptCd = $(this).attr("data-id");
		if(!$(this).hasClass("open")){
			$(this).addClass("open");	
		} else {
			$(this).removeClass("open");	
			$(".tree_check_list ul[data-id='"+deptCd+"']").find(".tree_item").removeClass("open");
		}
	});
	
	$(document).on("click", ".tree_check", function(){
		var checked = $(this).prop("checked");
		var deptCd = $(this).attr("data-id");
		if(checked) {
			$(".tree_check_list ul[data-id='"+deptCd+"']").find(".tree_check").prop("checked", true);
			var itemList = $(this).closest('ul').children('li');
			var itemCnt = itemList.length;
			if(itemCnt > 0){
				var checkedCnt = 0;
				$.each(itemList, function (i, item) {
					if($(item).children("label").children(".tree_check").prop("checked")) checkedCnt++;
				});
				if(itemCnt == checkedCnt){
					var parentId = $(this).closest('ul').attr('data-id');
					$(".tree_check_list").find(".tree_check[data-id='"+parentId+"']").prop("checked", true);
				}
			}
		} else {
			$(".tree_check_list ul[data-id='"+deptCd+"']").find(".tree_check").prop("checked", false);
			$(this).parents('ul').siblings('label').find('.tree_check').prop("checked", false);
		}
	});
	
	
</script>