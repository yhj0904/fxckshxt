<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: search_pop.jsp
 * @Description : 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<c:set var="selectAuthDvcd" value=""/>
<div class="pop_wrap">
	<div class="tab_wrap">
		<ul id="tabList" class="tab_list">
			<c:forEach var="item" items="${authDvcdList}" varStatus="i">
				<c:if test="${i.index eq 0 }"><c:set var="selectAuthDvcd" value="${item.authDvcd }"/></c:if>
				<li data-code="<c:out value='${item.authDvcd }'/>" <c:if test='${item.authDvcd eq selectAuthDvcd }'>class="active"</c:if>>
					<a href="javascript:fn_getTabContent('<c:out value='${item.authDvcd }'/>');"><span><c:out value='${item.authDvnm }'/></span></a>
					<div class="tab_content">
						<div class="tree_check_wrap">
							<ul class="tree_check_list" data-id="<c:out value='${item.authDvcd }'/>"></ul>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="pop_bottom_btn">	
		<div class="btn_wrap">
			<ul>
				<li>
					<a class="button register_btn" href="javascript:fn_sendData();"><spring:message code="button.register" /></a>
				</li>
				<li>
					<a class="button close_btn" href="javascript:fn_closePop();"><spring:message code="button.close" /></a>
				</li>
			</ul>
		</div>
	</div>
</div>

<script type="text/javaScript" defer="defer">
	
	$(function(){
		//부서목록 조회
		fn_searchDeptList('<c:out value="${selectAuthDvcd }"/>');
	})
	
	//탭 조회
	function fn_getTabContent(tabId) {
		var presentId = $("#sendTabList li.active").attr("data-code");
		if(presentId != tabId){
			TAB_ID = tabId;
			$("#tabList li").removeClass("active");
			$("#tabList li[data-code='"+tabId+"']").addClass("active");
			fn_searchDeptList(tabId);
		}
	}
	
	function fn_searchDeptList(authDvcd){
		gf_ajax({
			url : "/dept/getDeptList.json",
			type : "POST",
			contentType :  "application/json",
			data : {authDvcd : authDvcd},
		}).then(function(response){
			var str = "";
			if(!gf_isNull(response)){
				var treeLevel = 1;
				var treeLength = response.length;
				$.each(response, function(i, item){
					var count = i+1;
					if(i == 0) {
					} else if (item.deptLvl > treeLevel) {
						str += '<ul data-id="'+item.hiDeptCd +'">';
					} else if (item.deptLvl < treeLevel) {
						str += '</li>';
						for(var j = item.deptLvl; j <= treeLevel - 1; j++) {
							str += '</ul></li>';
						}
					} else {
						str += '</li>';
					}
					str += '<li>';
					if(item.childCnt > 0){
						str +='<a href="#open_tree" class="tree_item folder" data-id="'+item.deptCd+'"><span class="none">FILE</span></a>';
					} else {
						str +='<a href="#open_tree" class="tree_item" data-id="'+item.deptCd+'"><span class="none">FILE</span></a>';
					}
					str += '<label for="'+authDvcd+'_'+count+'">';						
					if(item.childCnt > 0){
						str += '<input type="checkbox" id="'+authDvcd+'_'+count+'" name="checkDeptRow" class="tree_check" value="" data-id="'+item.deptCd+'">';
					} else {
						str += '<input type="checkbox" id="'+authDvcd+'_'+count+'" name="checkDeptRow" class="tree_check" value="' + item.deptCd + '" data-id="'+item.deptCd+'" data-dept-nm="'+item.deptNmKor+'">';
					}
					str += item.deptNmKor + "("+ item.deptCd +")";
					str += '</label>';
					
					treeLevel =  item.deptLvl;
					
					if(count == treeLength) {
						str += '<li>';
						if(item.deptLvl > 1) {
							for(var j = 1; j <= item.deptLvl - 1; j++){
								str += '</ul></li>';
							}
						}
					}
				});
			}
			$(".tree_check_list[data-id='"+authDvcd+"']").html(str);	
		});
	};
	

	
	//트리 클릭
	$(document).on("click", ".tree_check_list .tree_item", function(){
		var treeObj = $(this).closest(".tree_check_list");
		var deptCd = $(this).attr("data-id");
		if(!$(this).hasClass("open")){
			$(this).addClass("open");	
		} else {
			$(this).removeClass("open");	
			treeObj.find("ul[data-id='"+deptCd+"']").find(".tree_item").removeClass("open");
		}
	});
	
	//트리 클릭
	$(document).on("click", ".tree_check_list .tree_check", function(){
		var treeObj = $(this).closest(".tree_check_list");
		var checked = $(this).prop("checked");
		var deptCd = $(this).attr("data-id");
		if(checked) {
			treeObj.find("ul[data-id='"+deptCd+"']").find(".tree_check").prop("checked", true);
			var itemList = $(this).closest('ul').children('li');
			var itemCnt = itemList.length;
			if(itemCnt > 0){
				var checkedCnt = 0;
				$.each(itemList, function (i, item) {
					if($(item).children("label").children(".tree_check").prop("checked")) checkedCnt++;
				});
				if(itemCnt == checkedCnt){
					var parentId = $(this).closest('ul').attr('data-id');
					treeObj.find(".tree_check[data-id='"+parentId+"']").prop("checked", true);
				}
			}
		} else {
			treeObj.find("ul[data-id='"+deptCd+"']").find(".tree_check").prop("checked", false);
			$(this).parents('ul').siblings('label').find('.tree_check').prop("checked", false);
		}
	});
	
	function fn_sendData(){
		var list = [];
		var authDvcd = $(".tab_list>li.active").attr('data-code');
		var chkCnt = 0;
		$.each($(".tree_check_list[data-id='"+authDvcd+"'] .tree_check"), function(i, item){
			if($(item).is(":checked") && !gf_isNull($(item).val())){
				var deptCd = $(item).val();
				chkCnt++;
				list.push({authDvcd : authDvcd, deptCd : deptCd, deptNm : $(item).attr("data-dept-nm")});
			}
		});
		if(chkCnt <= 0) {
			alert('<spring:message code="message.alert.nocheck" />');
		} else {
			opener.parent.fn_setDeptList(list);
			self.close();
		}
	}
	
	function fn_closePop(){
		self.close();
	}
</script>