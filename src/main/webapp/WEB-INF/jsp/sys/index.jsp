<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%
/**
 * @Class Name 	: index.jsp
 * @Description :  관리자 메인페이지
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */
%>
<div class="grid_wrap">
	<div class="row">
		<!-- <div class="col75">
			<div class="main_box">
				<div class="graph_box">
					<ul>
						<li><a href="javascript:fn_initGraph('weekly');">WEEKLY</a></li>
						<li><a href="javascript:fn_initGraph('monthly');">MONTHLY</a></li>
					</ul>
					<div id="mainGraph"></div>
				</div>
			</div>
		</div> -->
		<div class="col25">
			<div class="main_box ty2">
				<div class="count_box">
					<ul>
						<li>
							<p><spring:message code="content.sys.todayLoginCount"/></p>
							<span class="count"><c:out value='${statMap.todayLoginCount }'/></span>
						</li>
						<li>
							<p><spring:message code="content.sys.todayBbsCount"/></p>
							<span class="count"><c:out value='${statMap.todayBbsCount }'/></span>
						</li>
						<li>
							<p><spring:message code="content.sys.totalBbsCount"/></p>
							<span class="count"><c:out value='${statMap.totalBbsCount }'/></span>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col40">
			<div class="main_box">
				<div class="top"><spring:message code="content.sys.recentLogin"/></div>
				<div class="cont">
					<table class="list_table ty2">
						<colgroup>
							<col style=""/> <!-- ID -->
							<col style=""/> <!-- 이름 -->
							<col style=""/> <!-- 아이피 -->
							<col style=""/> <!-- 로그인 시각 -->
						</colgroup>
						<thead>
							<tr>
								<th><spring:message code="loginLogVO.loginId"/></th>
								<th><spring:message code="loginLogVO.loginNm"/></th>
								<th><spring:message code="loginLogVO.loginIp"/></th>
								<th><spring:message code="loginLogVO.loginDttm"/></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="item" items="${loginLogLst }" varStatus="i">
							<tr>
								<td><a href="/sys/log/login/view.do?sId=<c:out value="${item.loginCode}"/>"><c:out value="${item.loginId}"/></a></td>
								<td><c:out value="${item.loginNm}"/></td>
								<td><c:out value="${item.loginIp}"/></td>
								<td><c:out value="${fn:substring(item.loginDttm, 0, 16)}"/></td>
							</tr>
						</c:forEach>
						<c:if test="${empty loginLogLst}">
							<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>
						</c:if>
						</tbody>
				  	</table>
				  </div>
			 </div>
		</div>
		<div class="col40">
			<div class="main_box">
				<div class="top"><spring:message code="content.sys.recentSysLogin"/></div>
				<div class="cont">
					<table class="list_table ty2">
						<colgroup>
							<col style=""/> <!-- ID -->
							<col style=""/> <!-- 이름 -->
							<col style=""/> <!-- 아이피 -->
							<col style=""/> <!-- 로그인 시각 -->
						</colgroup>
						<thead>
							<tr>
								<th><spring:message code="loginLogVO.loginId"/></th>
								<th><spring:message code="loginLogVO.loginNm"/></th>
								<th><spring:message code="loginLogVO.loginIp"/></th>
								<th><spring:message code="loginLogVO.loginDttm"/></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="item" items="${sysLoginLogList }" varStatus="i">
							<tr>
								<td><a href="/sys/log/login/view.do?sId=<c:out value="${item.loginCode}"/>"><c:out value="${item.loginId}"/></a></td>
								<td><c:out value="${item.loginNm}"/></td>
								<td><c:out value="${item.loginIp}"/></td>
								<td><c:out value="${fn:substring(item.loginDttm, 0, 16)}"/></td>
							</tr>
						</c:forEach>
						<c:if test="${empty sysLoginLogList}">
							<tr><td class="no_data" colspan="4"><spring:message code="board.noData" /></td></tr>
						</c:if>
						</tbody>
				  	</table>
				  </div>
			 </div>
		</div>
		<div class="col20">
			<div class="main_box">
				<div class="top"><spring:message code="content.sys.currentLoginUser"/></div>
				<div class="cont">
					<div class="scroll_box current_user_list">
						<div>
							<span class="count"><spring:message code="content.sys.currentLoginCount" arguments="${loginCount }"/></span>
							<a class="ico_refresh" href="javascript:fn_getLoginList();"><span class="none"><spring:message code="button.refresh" /></span></a>
						</div>
						<ul id="loginList">
							<c:forEach var="item" items="${loginList }" varStatus="i">
								<li class="item">
									<div class="user">
										<p><c:out value="${item.loginNm}"/>(<c:out value="${item.loginId}"/>)</p>
									</div>
									<div class="info">
										<span class="ip"><c:out value="${item.loginIp}"/></span>
										<span class="time"><c:out value="${fn:substring(item.loginDttm, 11, 16)}"/></span>
									</div>
									<c:if test="${LOGIN_USER.loginId ne item.loginId }">
										<a class="ico_logout" href="javascript:fn_logoutUser('<c:out value="${item.sessionKey}"/>')"><span class="none"><spring:message code="button.logout" /></span></a>
									</c:if>
								</li>
							</c:forEach>
							<c:if test="${empty loginList}">
								<li class="no_list"><spring:message code="content.sys.message.noLogin" /><li>
							</c:if>
						</ul>
					</div>
				</div>
			 </div>
		</div>
	</div>
	<div class="row">
		<div class="col65">
			<div class="main_box">
				<div class="top"><spring:message code="content.sys.recentBoard"/></div>
				<div class="cont">
					<table class="list_table ty2">
						<colgroup>
							<col style=""/> <!-- 제목 -->
							<col style=""/> <!-- 작성자 -->
							<col style=""/> <!-- 조회수 -->
							<col style=""/> <!--  -->
							<col style=""/> <!--  -->
						</colgroup>
						<thead>
							<tr>
								<th><spring:message code="bbsVO.title"/></th>
								<th><spring:message code="bbsVO.writer"/></th>
								<th><spring:message code="bbsVO.viewCnt"/></th>
								<th><spring:message code="bbsVO.inptId"/></th>
								<th><spring:message code="bbsVO.inptDttm"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${bbsList }" varStatus="i">
								<tr>
									<td>
										<div class="tit">
											[<c:out value="${item.bbsTitle}"/>]
											<c:if test="${item.bbsDepth > 1}">
												<span class="ico_reply"><spring:message code="board.reply" /></span>
											</c:if>
											<a href="/sys/bbs/view.do?sCode=<c:out value="${item.bbsCd}"/>&sId=<c:out value="${item.bbsId}"/>" title="<spring:message code="board.detail"/>">
												<c:out value="${item.title}"/>
											</a>
											<c:if test='${item.secret eq "Y"}'>
												<span class="ico_secret"><span class="none"><spring:message code="board.secret" /></span></span>
											</c:if>
										</div>
									</td>
									<td><c:out value="${item.writer}"/></td>	
									<td><c:out value="${item.viewCnt}"/></td>
									<td><c:out value="${item.inptId}"/></td>
									<td><c:out value="${fn:substring(item.inptDttm, 0, 16)}"/></td>
								</tr>
							</c:forEach>
							<c:if test="${empty bbsList}">
								<tr><td class="no_data" colspan="5"><spring:message code="board.noData" /></td></tr>
							</c:if>
						</tbody>
				  	</table>
				  </div>
			 </div>
		</div>
		<div class="col35">
			<div class="main_box">
				<div class="top"><spring:message code="content.sys.currentSurvey"/></div>
				<div class="cont">
					<table class="list_table ty2">
						<colgroup>
							<col style=""/> <!-- 설문제목 -->
							<col style=""/> <!-- 시작일자 -->
							<col style=""/> <!-- 종료일자 -->
						</colgroup>
						<thead>
							<tr>
								<th><spring:message code="survMgtVO.survTitle"/></th>
								<th><spring:message code="survMgtVO.survDate1"/></th>
								<th><spring:message code="survMgtVO.survDate2"/></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="item" items="${list }" varStatus="i">
							<tr>
								<td><a href="/sys/surv/view.do?sId=<c:out value="${item.survId }"/>" title="<spring:message code="board.detail"/>"><c:out value="${item.survTitle}"/></a></td>
								<td><c:out value="${item.survDate1}"/></td>
								<td><c:out value="${item.survDate2}"/></td>
							</tr>
						</c:forEach>
						<c:if test="${empty list}">
							<tr><td class="no_data" colspan="3"><spring:message code="board.noData" /></td></tr>
						</c:if>
						</tbody>
				  	</table>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet" type="text/css" href="/css/common/chart/Chart.min.css" />
<script type="text/javascript" src="/js/common/chart/Chart.min.js"></script>
<script type="text/javascript" src="/js/common/chart/Chart.bundle.min.js"></script>
<script type="text/javascript" src="/js/common/chart/Chart.util.js"></script>
<script type="text/javascript" src="/js/common/jquery.counterup.min.js"></script>
<script type="text/javascript" src="/js/common/jquery.waypoints.min.js"></script>
<script type="text/javascript" src="/js/common/jquery.transit.min.js"></script>
<script type="text/javaScript" defer="defer">

$(function(){		
		//count box 카운트
		$('.count_box .count').counterUp({
			delay: 10,
			time: 700
		});
		
		fn_initGraph("weekly");
	});
	
	//강제 로그아웃
	function fn_logoutUser(sessionKey){		
		var msg = confirm('<spring:message code="content.sys.message.logoutUser" />');
		if(msg == true){
			gf_ajax({
				url : "/sys/logoutUser.json",
				type : "POST",
				dataType : "json",
				data : { sessionKey : sessionKey },
			}).then(function(response){
				fn_getLoginList();
			});
		}
	}
	
	//현재 로그인 목록 조회
	function fn_getLoginList(){
		gf_ajax({
			url : "/sys/loginList.json",
			type : "POST",
		}).then(function(response){
			if(response.result){
				var str = "";
				if(response.loginList.length > 0){
					$.each(response.loginList, function (i, item) {
						str += '<li class="item">';
						str += '	<div class="user">';
						str += '		<p>'+item.loginNm+'('+item.loginId+')</p>';
						str += '	</div>';
						str += '	<div class="info">';
						str += '		<span class="ip">'+item.loginIp+'</span>';
						str += '		<span class="time">'+item.loginDttm.substring(11, 16)+'</span>';
						str += '	</div>';
						if(item.loginId != '<c:out value="${LOGIN_USER.loginId}"/>'){
							str += '	<a class="ico_logout" href="javascript:fn_logoutUser(\''+item.loginId+'\')"><span class="none"><spring:message code="button.logout" /></span></a>';
						}
						str += '</li>';
					});						
				} else {
					str = '<li class="no_list"><spring:message code="content.sys.message.noLogin" /><li>';
				}
				$("#loginList").html(str);
				$("#loginCount").html(response.loginCount);
			} else {
				var str = '<li class="no_list"><spring:message code="content.sys.message.noLogin" /><li>';
				$("#loginList").html(str);
				$("#loginCount").html("0");
			}				
		});
	}
	
	function fn_initGraph(type){
		
		$('#mainGraph').html('');
		$('#mainGraph').html('<canvas id="mainCanvas"></canvas>')
		
		var weeklyLabel = "일주일간 로그인";
		var weeklyLabels = [<c:forEach var='item' items='${statMap.weeklyLoginCount }' varStatus='i'><c:if test='${i.index ne 0}'>,</c:if>'<c:out value="${item.CUR_DATE}"/>'</c:forEach>];
		var weeklyDatas = [<c:forEach var='item' items='${statMap.weeklyLoginCount }' varStatus='i'><c:if test='${i.index ne 0}'>,</c:if><c:out value="${item.LOGIN_COUNT}"/></c:forEach>];
		
		var monthlyLabel = GV_TODAY_MONTH+"월 로그인";
		var monthlyLabels = [<c:forEach var='item' items='${statMap.monthlyLoginCount }' varStatus='i'><c:if test='${i.index ne 0}'>,</c:if>'<c:out value="${item.CUR_DATE}"/>'</c:forEach>];
		var monthlyDatas = [<c:forEach var='item' items='${statMap.monthlyLoginCount }' varStatus='i'><c:if test='${i.index ne 0}'>,</c:if><c:out value="${item.LOGIN_COUNT}"/></c:forEach>];
		
		var chartLabel;
		var chartLabels;
		var chartDatas;
		
		if(type == "weekly"){
			chartLabel = weeklyLabel;
			chartLabels = weeklyLabels;
			chartDatas = weeklyDatas;
		} else if(type == "monthly"){
			chartLabel = monthlyLabel;
			chartLabels = monthlyLabels;
			chartDatas = monthlyDatas;
		}
		
		var mainCanvas = document.getElementById('mainCanvas').getContext('2d');
		
		window.myLine = new Chart(mainCanvas, {
			type: 'line',
			data: {
				labels: chartLabels,
				datasets: [
					{
						label: chartLabel,
						data: chartDatas,
						fill: false
					}
				]
			},
			options: {
				responsive: true,
				tooltips: {
					mode: 'index',
					intersect: false,
				},
				hover: {
					mode: 'nearest',
					intersect: true
				}
			},
			scales: {
	            xAxes: [{
	            	gridLines: {
	                    color: "rgba(0, 0, 0, 0)",
	                },
	            	ticks: {
	                    min: 0,
	                    stepSize: 1
	                } 
	            }],
	            yAxes: [{
	                gridLines: {
	                    color: "rgba(0, 0, 0, 0)",
	                },
	            	ticks: {
	                    min: 0,
	                    stepSize: 1
	                } 
	            }]
	        }
		});
	}
	
</script>