<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<html>
<head><title>Erreur</title></head>

<body>

<h3>Erreur</h3>
<a href="../">Accueil</a>
<%
StringWriter sw = new StringWriter();

Exception e = (Exception) request.getAttribute("exception");
e.printStackTrace(new PrintWriter(sw));
String stacktrace = sw.toString();
log("general error page caught exception", e);
%>
<hr/>
<pre >
<%= stacktrace %>
</pre>
<hr/>
</body>
</html>