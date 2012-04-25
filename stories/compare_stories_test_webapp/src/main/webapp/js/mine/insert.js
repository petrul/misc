var userMayLeavePageWithoutWarning = false;

function warnUserAboutLeavingThePage() {
	if (! userMayLeavePageWithoutWarning) {
		return 'Vous ne pouvez pas revenir en arrière ou rafraîchir la page pendant ce test.' +
			' Si vous le faites, le test sera abandonné.';
	}
}

window.onbeforeunload = warnUserAboutLeavingThePage;


function copyInnerHTML(from, to) {
	
	var mytarget = $(to);
	mytarget.hide();
	var mytext = document.getElementById(from).innerHTML;
	mytarget.update(mytext) ;
	Effect.Appear(to, { duration: 0.4 });
}

function leftTextSelected() {
	copyInnerHTML('fillin-txt1', 'fill-in-span');
	document.getElementById('radio-opt1').checked = true;
	userMayLeavePageWithoutWarning = true;
	return ;
}

function rightTextSelected() {
	copyInnerHTML('fillin-txt2', 'fill-in-span');
	document.getElementById('radio-opt2').checked = true;
	userMayLeavePageWithoutWarning = true;
	return ;
}

function checkSomethingWasChosen() {
	if  (!(
			document.getElementById('radio-opt1').checked || 
			document.getElementById('radio-opt2').checked)
			) 
	{
		alert('Vous devez choisir une des deux alternatives');
		return false;
	}
	userMayLeavePageWithoutWarning = true;
	return true;
}