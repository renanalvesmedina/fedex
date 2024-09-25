<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterRecibosReembolsoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/carregamento/manterEquipes" idProperty="idReciboReembolsoProcesso" height="30">
	
		<adsm:hidden property="processoSinistro.idProcessoSinistro" serializable="true"/>
		<adsm:textbox label="numeroProcesso" property="processoSinistro.nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />

	</adsm:form>
	<adsm:grid property="reciboReembolsoProcesso" 
				idProperty="idReciboReembolsoProcesso" 
				selectionMode="check" 
				gridHeight="200" 
				unique="true"
				service="lms.seguros.manterRecibosReembolsoAction.findPaginatedRecibos"
				rowCountService="lms.seguros.manterRecibosReembolsoAction.getRowCountRecibos"
				defaultOrder="nrRecibo"
				rows="14"
				>
		<adsm:gridColumn property="nrReciboReembolso"  title="reciboReembolso" width="300" align="right" dataType="integer"/>
		<adsm:gridColumn property="dtReembolso"        title="data"  width="200" align="center" dataType="JTDate" />
		<adsm:gridColumn property="sgSimboloReembolso" title="valor" width="60" align="left"  dataType="text"/>
		<adsm:gridColumn property="vlReembolso"        title=""      width="150" align="right"  dataType="currency"/>		
		<adsm:buttonBar>
			<adsm:button caption="excluir" buttonType="removeButton" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	var tabGroup = getTabGroup(this.document);

	document.getElementById("processoSinistro.idProcessoSinistro").masterLink="true";
	document.getElementById("processoSinistro.nrProcessoSinistro").masterLink="true";
	
	function onRemoveButtonClick() {
		reciboReembolsoProcessoGridDef.removeByIds('lms.seguros.manterRecibosReembolsoAction.removeByIds', 'remove');
	}
	
	function remove_cb(data, error) {
		if (error!=undefined)
			alert(error);
	
		atualizaValoresProcessoSinistro();
		var data = buildFormBeanFromForm(document.forms[0]);
		reciboReembolsoProcessoGridDef.executeSearch(data);
	}
	
	// atualiza os valore na tela de processo de sinistro
	function atualizaValoresProcessoSinistro() {
		dialogArguments.window.findAtualizaValoresDetalhamento();
	}
	
	
	function pageLoad_cb() {
		onPageLoad_cb();
	
		if (dialogArguments.document.forms[0].elements["idProcessoSinistro"])
			setElementValue("processoSinistro.idProcessoSinistro", dialogArguments.document.forms[0].elements["idProcessoSinistro"].value);
	
		if (dialogArguments.document.forms[0].elements["nrProcessoSinistro"])
			setElementValue("processoSinistro.nrProcessoSinistro", dialogArguments.document.forms[0].elements["nrProcessoSinistro"].value);
	
		executeSearch();			

	}	
	
	var firstTime = true;
	
	function onTabShow() {		
		
	}
	
	function executeSearch() {
		var fb = buildFormBeanFromForm(document.forms[0]);
		reciboReembolsoProcessoGridDef.executeSearch(fb);
	}	


</script>
