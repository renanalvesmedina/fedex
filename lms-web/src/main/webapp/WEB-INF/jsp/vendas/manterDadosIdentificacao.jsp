<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterClientes" type="main" onPageLoad="setAbas">

	<adsm:tabGroup selectedTab="0">
		<adsm:tab onShow="myOnShow" title="listagem" id="pesq" src="/vendas/manterDadosIdentificacao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDadosIdentificacao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
<script type="text/javascript">

// chama os blockUI para cada subAba da tela de cad
blockUI();
blockUI();
blockUI();
blockUI();
showSystemMessage('processando', null, true);

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

	if (idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.setDisabledTab("cad", false);
		tabGroup.selectTab('cad',null,true);
	}else{
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');
	}
}

</script>