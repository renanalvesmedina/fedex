<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
function carregaDadosOnPageLoad() {
	onPageLoad();
	setMasterLink(this.document, true);
	var idManifesto = getElementValue("manifesto.idManifesto");
	var map = new Array();
	setNestedBeanPropertyValue(map, "idManifesto", idManifesto);

    var sdo = createServiceDataObject("lms.recepcaodescarga.consultarCargasViagemAction.findDadosPopupDocumentos", "resultadoFindDocumentos", map);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoFindDocumentos_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	if (data!=undefined) {
		setElementValue('manifesto.controleCarga.filialByIdFilialOrigem.sgFilial', data.manifesto.controleCarga.filialByIdFilialOrigem.sgFilial);
		setElementValue('manifesto.controleCarga.nrControleCarga', setFormat(document.getElementById("manifesto.controleCarga.nrControleCarga"), data.manifesto.controleCarga.nrControleCarga));

		setElementValue('manifesto.tpManifesto', data.manifesto.tpManifesto);
		setElementValue('manifesto.filialByIdFilialOrigem.sgFilial', data.manifesto.filialByIdFilialOrigem.sgFilial);
		setElementValue('manifesto.nrManifesto', setFormat(document.getElementById('manifesto.nrManifesto'), data.manifesto.nrManifesto));

		// popula a grid
		var idManifesto = getElementValue("manifesto.idManifesto");
		var map = new Array();
		setNestedBeanPropertyValue(map, "idManifesto", idManifesto);
		doctosServicosGridDef.executeSearch(map);
	}
}

/**
 * Função cancelar o click na grid
 */
function disableRowClick() {
	return false;
}    

</script>

<adsm:window service="lms.recepcaodescarga.consultarCargasViagemAction" onPageLoad="carregaDadosOnPageLoad">

	<adsm:form action="/recepcaoDescarga/consultarCargasViagemDocumentosServico">
		<adsm:section caption="cargasViagemDocumentosServico" />
		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="manifesto.controleCarga.filialByIdFilialOrigem.sgFilial" label="controleCargas" width="84%" disabled="true" dataType="text" size="3">
			<adsm:textbox property="manifesto.controleCarga.nrControleCarga" size="8" dataType="integer" mask="00000000" disabled="true" />
		</adsm:textbox>		
		
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="manifesto.tpManifesto" label="manifesto" width="84%" disabled="true" dataType="text" size="20" >
			<adsm:textbox property="manifesto.filialByIdFilialOrigem.sgFilial" dataType="text" size="3" disabled="true"/>
			<adsm:textbox property="manifesto.nrManifesto" dataType="integer" mask="00000000" size="8" disabled="true"/>
		</adsm:textbox>
	</adsm:form>
	
	<adsm:grid  selectionMode="none"
				idProperty="idDoctoServico" 
				property="doctosServicos" 
				onRowClick="disableRowClick"
				service="lms.recepcaodescarga.consultarCargasViagemAction.findDocumentosConsultarCargasViagem"
				rowCountService="lms.recepcaodescarga.consultarCargasViagemAction.getRowCountCargasEmViagem"
				gridHeight="290"
				rows="15"
				unique="true" 
				scrollBars="horizontal"
				showGotoBox="false" 
				showTotalPageCount="false"
				onPopulateRow="checkDPE">

		<adsm:gridColumn property="doctoServico.servico.dsServico" title="servico" width="220" />

		<adsm:gridColumn title="documentoServico" 	property="doctoServico.tpDoctoServico"  width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn property="doctoServico.filialByIdFilialDestino.sgFilial" title="destino" width="80" />
		<adsm:gridColumn property="doctoServico.dhEmissao" title="emissao" width="120" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" title="remetente" width="200" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" title="destinatario" width="200" />
		<adsm:gridColumn property="doctoServico.qtVolumes" title="volumes" width="120" align="right" />
		<adsm:gridColumn property="doctoServico.psReal" title="peso" width="100" unit="kg" align="right" dataType="decimal" mask="###,###,##0.000"/>

		<adsm:gridColumn property="doctoServico.moeda.siglaSimbolo" dataType="text" title="valor" width="50"/>
		<adsm:gridColumn property="doctoServico.vlMercadoria" title="" width="80" align="right" dataType="currency"/>
		
		<adsm:gridColumn property="doctoServico.dtPrevEntrega" title="dpe" width="120" align="center" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:button id="closeButton" caption="fechar" onclick="closeWindow()" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
document.getElementById("closeButton").disabled = false;

function closeWindow() {
	window.close();
}

/**
 */
function checkDPE(tr, data) {
	if (data.mudaCorItem == "true") {
		tr.style.backgroundColor = "#8FBFD6";
	}
}	
</script>