<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTRTPorCliente" type="main" onPageLoad="setAbas">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/vendas/manterTrtCliente" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/vendas/manterTrtCliente" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>

<script type="text/javascript">

// Bloqueia a tela devido ao tempo de carga do combo de Munícpios da aba de detalhamento.
blockUI();

//Exibe o loader.
showSystemMessage('processando', null, true);

//Remove o loader.
function waitLoad() {
	if (getBlockUISemaphore().value == 0) {
		var doc = getMessageDocument(document);
		var messageSign = doc.getElementById("message.div");
		if (messageSign) {
			if (messageSign.systemMsg == true) {
				messageSign.systemMsg = false;
				messageSign.style.visibility = "hidden";
			}
			cancelAnimation(messageSign, doc);
			clearInterval(messageSign.timerId);
			messageSign.timerId = null; // remove o timerId do elemento
		}
		return;
	}
	setTimeout('waitLoad()', 100);
}

waitLoad();


function setAbas(){

	var url = new URL(document.location.href);

	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;

	if ( idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.selectTab('cad',null,true);
	}else{
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');
	}
}

</script>