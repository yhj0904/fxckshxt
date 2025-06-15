<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
	<title>ERROR</title>
	<link href="/css/common/common.css" media="all" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="error_page">
		<div class="error_wrap">
			<div class="error_box">
				<div class="error_tit">
					<p>ERROR 400</p>
				</div>
				<div class="error_cont">
					<p class="txt1"><spring:message code="page.error400.txt1"/></p>
					<p class="txt2"><spring:message code="page.error400.txt2"/></p>
				</div>
				<div class="error_btn">
					<a class="btn1" href="javascript:location.replace('/');"><spring:message code="page.error.button01"/></a>
					<a class="btn2" href="javascript:history.go(-1);"><spring:message code="page.error.button02"/></a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>