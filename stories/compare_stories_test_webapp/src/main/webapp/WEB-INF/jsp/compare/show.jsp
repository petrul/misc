<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
	String ctxtPath = request.getContextPath();
	request.setAttribute("ctxtpath", ctxtPath);
%>
<html>
<head>
	<title>Comparaison</title>
	<link rel="stylesheet" type="text/css" href="${ctxtpath}/css/compare.css" />
	<script src="${ctxtpath}/js/prototype.js" type="text/javascript"></script>
	<script src="${ctxtpath}/js/scriptaculous.js" type="text/javascript"></script>

	<style>
	  div.slider { width:500px; margin:10px 0; background-color:#ccc; height:5px; position: relative; }
	  div.slider div.handle { width:10px; height:15px; background-color:#f00; cursor:move; position: absolute; }
	</style>

</head>
<body>
<h3>Comparez l'intérêt des deux histoires suivantes</h3> 
<p class="subtitle">Estimez le niveau relatif d'intérêt en positionnant le slider proportionnellement plutôt vers la gauche ou plutôt vers la droite</p>

<div style="margin-top:50px; width:900px; margin-left:auto; margin-right:auto;">
<table id="stories-side-by-side"><tr><td width="50%">
	<div id="story-left" class="story-text" style="display:none"> 
		<table><tr>
			<td width="20" valign="top" style="padding-right: 10px; color: #0066CC; font-size: 35px; font-family: 'Times New Roman',serif; font-weight: bold; text-align: left;">“</td>
			<td>${comparisonResult.storyLeft.text}</td>
<!--			<td width="20" valign="bottom" style="padding: 0px; color: #0066CC; font-size: 36px; font-family: 'Times New Roman',serif; font-weight: bold; text-align: left">”</td>-->
		</tr></table>
	</div>
	</td><td width="50%">
	<div id="story-right" class="story-text" style="display:none">
		<table><tr>
			<td width="20" valign="top" style="padding-right: 10px; color: #0066CC; font-size: 35px; font-family: 'Times New Roman',serif; font-weight: bold; text-align: left;">“</td>
			<td>${comparisonResult.storyRight.text}</td>
<!--			<td width="20" valign="bottom" style="padding: 0px; color: #0066CC; font-size: 36px; font-family: 'Times New Roman',serif; font-weight: bold; text-align: left">”</td>-->
		</tr></table>
	</div>
</td></tr></table>
</div>

<script type="text/javascript" language="javascript">
  Effect.Appear('story-left', { duration: 1.0 });
  Effect.Appear('story-right', { duration: 1.0 });
</script>

<div style="width:100%; margin-top:100px;">
	<div id="compare-slider" class="slider" style="margin-left:auto;margin-right:auto;">
	    <div id="compare-slider-handle" class="handle"></div>
	</div>
</div>

<div style="margin-right:auto; margin-left:auto; margin-top:100px; text-align: center;">
<form:form commandName="comparisonResult" action="submit.html">
	<form:errors path="*" cssClass="errorBox" />
	<form:hidden path="comparisonValue" id="comparison-value" />
	<form:hidden path="" id="comparison-value" />
	<input type="submit" value="Suivant"/>
</form:form>
</div>

<div id="debug-console">
</div>

<script type="text/javascript" language="javascript">
(function() {
	var leftStoryDiv = $('story-left');
	var rightStoryDiv = $('story-right');
	var debugConsoleDiv = $('debug-console');
	var comparisonValueInput = $('comparison-value');

	var s2 = new Control.Slider('compare-slider-handle', 'compare-slider', {
	  axis:'horizontal',
	  range: $R(0, 100),
	  sliderValue: 50,
	});
	
	s2.options.onChange = function(value) {
		comparisonValueInput.setValue(parseInt(value));
		setColors(value);
	};

	function getRed(value) {
		return parseInt(-255 / 50 * value + 255); 
	}
	
	function getGreen(value) {
		return parseInt( (102 - 255) / 50 * value + 255); 
	}

	function getBlue(value) {
		return parseInt( (204 - 255) / 50 * value + 255); 
	}

	function setColors(value) {
	  var thickness = parseInt((value - 50));
	  var left_thickness, right_thickness;
	  
	  if (thickness > 0) {
		  right_thickness = thickness;
		  left_thickness = 0;
	  } else {
		  right_thickness = 0;
		  left_thickness = -thickness;
	  }
	  leftStoryDiv.setStyle({ backgroundColor: "rgb(" + getRed(left_thickness)  + " , " + getGreen(left_thickness) + ", " + getBlue(left_thickness) + ")" });
	  rightStoryDiv.setStyle({ backgroundColor: "rgb(" + getRed(right_thickness)  + " , " + getGreen(right_thickness) + ", " + getBlue(right_thickness) + ")" });
	}
	
	s2.options.onSlide = function(value) {
		setColors(value);
	};
	
})();
	</script>


</body>
</html>