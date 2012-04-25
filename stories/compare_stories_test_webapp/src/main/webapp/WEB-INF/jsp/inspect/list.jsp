<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Story comparison results for ${param['login']}</title>
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
	<h3>Comparison results for [${param['login']}]</h3>
	<form method="get">
		Login : <input type="text" name="login">
		<input type="submit" value="Inspecter" /> 
	</form>
	
	<table id="crlist">
	<c:forEach var="res" items="${inspection_list}" varStatus="rowCounter">
	
		<c:choose>
          <c:when test="${rowCounter.count % 2 == 0}">
            <c:set var="rowStyle" scope="page" value="odd"/>
          </c:when>
          <c:otherwise>
            <c:set var="rowStyle" scope="page" value="even"/>
          </c:otherwise>
        </c:choose>
        
		<c:choose>
          <c:when test="${res.optionPresented1 == res.chosenOption}">
            <c:set var="leftOptionStyle" scope="page" value="font-weight:bold;color:#0066CC;"/>
            <c:set var="rightOptionStyle" scope="page" value="font-weight:normal;"/>
          </c:when>
          <c:otherwise>
          	<c:set var="leftOptionStyle" scope="page" value="font-weight:normal;"/>
            <c:set var="rightOptionStyle" scope="page" value="font-weight:bold;color:#0066CC;"/>
          </c:otherwise>
        </c:choose>
        
		<tr class="${rowStyle}">
			<td>${res.story.id}. ${res.story.main}</td>
			<td style="${leftOptionStyle}">[${res.optionPresented1}] ${res.optionPresented1Text}</td>
			<td>${res.chosenOption}</td>
			<td style="${rightOptionStyle}">[${res.optionPresented2}] ${res.optionPresented2Text}</td>
		</tr>
	</c:forEach>
	</table>
</body>
</html>