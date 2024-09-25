<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="consultarControleCargas" type="main"
	onPageLoad="setAbas">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq"
			src="/carregamento/consultarControleCargas" cmd="list" />
		<adsm:tab title="detalhamento" id="cad"
			src="/carregamento/consultarControleCargas" cmd="cad" disabled="true" />
		<adsm:tab title="trechos" id="trechos"
			src="/carregamento/consultarControleCargas" cmd="trechos"
			disabled="true" />
		<adsm:tab title="pontosParadaTitulo" id="pontosParada"
			src="/carregamento/consultarControleCargas" cmd="pontosParada"
			disabled="true" />
		<adsm:tab title="postosPassagemTitulo" id="postosPassagem"
			src="/carregamento/consultarControleCargas" cmd="postosPassagem"
			boxWidth="117" disabled="true" />
		<!-- adsm:tab title="adiantamentos" id="adiantamentos" src="/carregamento/consultarControleCargas" cmd="adiantamentos" disabled="true" / -->
		<adsm:tab title="adiantamentos" id="adiantamentoTrecho"
			src="/carregamento/consultarControleCargas" cmd="adiantamentoTrecho"
			disabled="true" />
		<adsm:tab title="trechosCorporativo" id="trechoCorporativo"
			src="/carregamento/consultarControleCargas" cmd="trechoCorporativo"
			disabled="true" />
	</adsm:tabGroup>
</adsm:window>

<script>
	function setAbas() {
		var url = new URL(document.location.href);
		var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
		if (idProcessoWorkflow != undefined && idProcessoWorkflow != "") {
			tabGroup.setDisabledTab("pesq", true);
			tabGroup.setDisabledTab("cad", false);
			tabGroup.selectTab('cad', null, true);
		} else {
			tabGroup.setDisabledTab("pesq", false);
			tabGroup.selectTab('pesq');
		}
	}
</script>