<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>List of users who have taken this test</title>
	<style>
		
		table#crlist tr {
			border-bottom-width: 1px;
			border-bottom-style: solid;
			border-bottom-color: black;
		}
		
		table#crlist tr.odd {
			background-color: #ddd;
		}
		table#crlist tr.even {
			background-color: #fff;
		}
	</style>
</head>
<body>
	<h3>List of users who have taken this test</h3>
	
	<div>
		<c:forEach var="u" items="${user_list}">
			<a href="list.html?login=${u.login}">${u.login}</a> | 
		</c:forEach>
	</div>
</body>
</html>