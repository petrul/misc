<%@ page contentType="text/html; charset=utf-8"
%><%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" 
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
			<title>Test histoires - <decorator:title default="" /></title>
			<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css" />
			<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=utf-8">

			<decorator:head />
	</head>
	<body>
		<div id="toolbar">
			Bonjour ${sessionScope.user.login}! | 
			<a href="<%= request.getContextPath() %>/">Accueil</a> |
			<a href="<%= request.getContextPath() %>/login/logout.html">Logout</a>
			
			<span style="position:absolute; right:0; width:50px; margin:0;" >
				${progress}
			</span>
		</div>
		<decorator:body />
	</body>
</html>
<!-- $Id: main-decorator.jsp 112 2008-12-08 17:30:02Z dimulesc $ -->