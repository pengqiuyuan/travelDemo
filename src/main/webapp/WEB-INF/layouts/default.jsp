<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>数太奇<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>

<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/static/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />

<link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>

<sitemesh:head/>
<style type="text/css">
	.head_content{margin-left:9%;}
	.member_bar{margin-right:-25%;}
	.div_footer{margin-bottom:20px;text-align:center;background:#E6E6E6;}

</style>
</head>


<body>
	
	<%@ include file="/WEB-INF/layouts/nav.jsp"%>
	<div class="container" style="width: 90%;padding-top: 20px">
		<div id="content" style="width: 95%;margin-left:50px;padding-top: 0px">
			<sitemesh:body/>
		</div>
		
	</div>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
    <script src="${ctx}/static/bootstrap/3.1.1/js/bootstrap.min.js" type="text/javascript"></script>
	
</body>
</html>