<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.indenizacoes.incluirReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/incluirReciboIndenizacaoSelecaoDocumentosServico">
		<adsm:hidden property="masterId" serializable="true"/>
		<adsm:hidden property="idProcessoSinistro" serializable="true"/>
		<adsm:hidden property="tpBeneficiarioIndenizacao" serializable="true"/>
		<adsm:hidden property="idCliente" serializable="true"/>
		<adsm:textbox label="numeroRIM" property="nrReciboIndenizacao" dataType="integer"  disabled="true" serializable="false" labelWidth="15%" width="85%" maxLength="8"/>
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="integer" disabled="true" serializable="false" labelWidth="15%" width="85%" maxLength="15"/>
	</adsm:form>

	<adsm:grid  property="doctoServico" 
				idProperty="idDoctoServico" 
				service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedSelecaoDocumentos"
				rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountSelecaoDocumentos"
				selectionMode="check" 
				unique="true" 
				scrollBars="horizontal" 
				onRowClick="onRowClick"
				gridHeight="280" >

		<adsm:gridColumn property="tpDocumentoServico" isDomain="true" title="documentoServico" dataType="text"  width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="sgFilialOrigem"     dataType="text" title="" width="40" />
			<adsm:gridColumn property="nrDocumentoServico" dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="sgFilialDestino" title="destino" width="60"/>
		<adsm:gridColumn property="nmRemetente"    title="remetente" width="200"/>
		<adsm:gridColumn property="nmDestinatario" title="destinatario" width="200"/>

		<adsm:buttonBar>
			<adsm:button caption="adicionar" id="adicionar" onclick="adicionarDocumento();" buttonType="removeButton" />
  		    <adsm:button caption="fechar"    id="fechar"    onclick="window.close();"       disabled="false"/>
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script>

	function pageLoad_cb() {
		onPageLoad_cb();
		setMasterLinkProperties();
		executeSearch();
	}
	
	function onRowClick() {
		return false;
	}
	
	function setMasterLinkProperties() {
		setElementValue('masterId', dialogArguments.document.forms[0].elements["masterId"]);
		setElementValue('idProcessoSinistro',  dialogArguments.document.forms[0].elements["processoSinistro.idProcessoSinistro"]);
		setElementValue('nrProcessoSinistro',  dialogArguments.document.forms[0].elements["_processoSinistro.nrProcessoSinistro"]);
		setElementValue('nrReciboIndenizacao', dialogArguments.document.forms[0].elements["_nrReciboIndenizacao"]);
		setElementValue('idCliente',           dialogArguments.document.forms[0].elements["idCliente"]);
		setElementValue('tpBeneficiarioIndenizacao',  dialogArguments.document.forms[0].elements["tpBeneficiarioIndenizacao"]);
	}
	
	function executeSearch() {
			var fb = buildFormBeanFromForm(document.forms[0]);
			doctoServicoGridDef.executeSearch(fb);
	}
	
	function adicionarDocumento() {
		var data = new Array();	
		merge(data, buildFormBeanFromForm(document.forms[0]));
		merge(data, doctoServicoGridDef.getSelectedIds());
		data.myScreen = 'selecao';
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.saveItemDocumentosProcesso", "adicionarDocumento", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function adicionarDocumento_cb(data, error) {
		onDataLoad_cb(data, error);
		if (error == undefined) {
			dialogArguments.window.doctoServicoIndenizacaoGridDef.executeSearch();
			window.close();
		} else {
			alert(error);
		}
	}

</script>
