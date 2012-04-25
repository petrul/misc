<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>Login</title>
</head>
<body>
<table style="width:800px;margin-left:auto;margin-right:auto; "><tr><td style="text-align:center;">

<h3>Vos coordonnées</h3>

<form:form commandName="user" action="submit.html">
<table style="margin-left:auto;margin-right:auto;font-family: tahoma,arial; font-size: small;">
	<tr><td align="right">nom </td><td><form:input path="login" autocomplete="off"/></td><td><b>*</b></td></tr>
	<tr><td align="right">occupation </td><td><form:input path="occupation" /></td><td></td></tr>
	
	<tr><td align="right">email </td><td><form:input path="email" /></td><td></td></tr>
	<tr><td align="right">commentaire </td><td><form:textarea path="comment" /></td><td></td></tr>
	<tr><td colspan="3" style="padding-top:20px;"><input type="submit" value="Commencer le test" /></td></tr>
</table>
</form:form>
</td></tr></table>
</body>
</html>