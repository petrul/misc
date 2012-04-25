<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
	String ctxtPath = request.getContextPath();
	request.setAttribute("ctxtpath", ctxtPath);
%>
<html>
<head>
	<title>choisir option</title>
	<link rel="stylesheet" type="text/css" href="${ctxtpath}/css/insert.css" />
	<script src="${ctxtpath}/js/prototype.js" type="text/javascript"></script>
	<script src="${ctxtpath}/js/scriptaculous.js" type="text/javascript"></script>
	<script src="${ctxtpath}/js/mine/insert.js" type="text/javascript"></script>

<style>
	td.hoverable-cell {
		padding:10px;
	}
	
	td.hoverable-cell:hover {
		background-color: #c5d6e7;
	}
</style>
</head>

<body>

<h3>Rendez cette histoire intéressante</h3> 
<p class="subtitle">Remplissez l'espace vide avec une des options disponibles en bas afin de rendre cette histoire intéressante</p>

<div style="margin-top:50px; width:600px; margin-left:auto; margin-right:auto;">

	<div id="story" class="story-text" style="display:none"> 
		<table><tr>
			<td width="20" valign="top" style="padding-right: 10px; color: #0066CC; font-size: 35px; font-family: 'Times New Roman',serif; font-weight: bold; text-align: left;">“</td>
			<td>
				<span id="left-side-span">${insertionResult.mainTextLeft}</span>
				<span id="fill-in-span" style="font-weight:bold; color:#0066CC;">[.........]</span>
				<span id="right-side-span">${insertionResult.mainTextRight}</span>
			</td>
			<td width="20" valign="bottom" style="padding: 0px; color: #0066CC; font-size: 36px; font-family: 'Times New Roman',serif; font-weight: bold; text-align: left">”</td>
		</tr></table>
	</div>
	
	
</div>

<script type="text/javascript" language="javascript">
  Effect.Appear('story', { duration: 1.0 });
</script>

<div style="margin-right:auto; margin-left:auto; margin-top:100px; text-align: center; ">
<form:form commandName="insertionResult" action="submit.html" onsubmit="javascript:return checkSomethingWasChosen();">
	<form:errors path="*" cssClass="errorBox" />
	
	<table style="width:800px; margin:0 auto 0 auto;"><tbody><tr>
	<td style="width:50%;" class="hoverable-cell">
		<form:radiobutton id="radio-opt1" path="chosenOption" value="${insertionResult.optionPresented1}" onfocus="javascript:leftTextSelected();" />
		<span id="fillin-txt1" onclick="javascript:leftTextSelected();">${insertionResult.optionPresented1Text}</span>
	</td>
	<td style="width:50%;" class="hoverable-cell"> 
    	<form:radiobutton id="radio-opt2" path="chosenOption" value="${insertionResult.optionPresented2}" onfocus="javascript:rightTextSelected();" />
    	<span id="fillin-txt2" onclick="javascript:return rightTextSelected();">${insertionResult.optionPresented2Text}</span>
    </td>
    </tr>
    
    <tr><td colspan="2">&#160;</td></tr>
    <tr><td colspan="2">&#160;</td></tr>
    
    <tr><td colspan="2"><input type="submit" value="Suivant" /></td></tr>
    
    </tbody></table>
	
	
	<div style="margin: 20px auto 0 auto; width:600px; text-align: right;">
	<form:textarea  rows="2" cssStyle="width:100%;" path="comment" />
	<span style="font-style: italic; color: gray; font-size: small; font-family: arial,sans-serif;">(éventuels commentaires)</span>
	</div>
	
</form:form>
</div>

</body>
</html>