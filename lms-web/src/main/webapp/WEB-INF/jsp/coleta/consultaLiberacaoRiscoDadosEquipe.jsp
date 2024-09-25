<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function carregaDadosEquipePageLoad() {
	setMasterLink(document, true);
	carregaGriddadosEquipe();
}
</script>


<adsm:window title="programacaoColetasVeiculos" service="lms.coleta.consultaLiberacaoRiscoAction" onPageLoad="carregaDadosEquipePageLoad">
	<adsm:form action="/coleta/consultaLiberacaoRiscoDadosEquipe">
		<adsm:textbox dataType="text" property="meioTransporte.nrFrota" disabled="true" label="frota" size="6"/>
		<adsm:hidden property="equipeOperacao.idEquipeOperacao"/>
		<adsm:hidden property="idEventoControleCarga" />
	</adsm:form>
	<adsm:grid scrollBars="horizontal" gridHeight="300" unique="true" title="integrantes" rows="10"
			   property="dadosEquipe" onRowClick="disableRowClick"
			   idProperty="idIntegranteEqOperac" selectionMode="none"
			   service="lms.coleta.consultaLiberacaoRiscoAction.findPaginatedDadosEquipe"
			   rowCountService="lms.coleta.consultaLiberacaoRiscoAction.getRowCountDadosEquipe" >
		<adsm:gridColumn title="cargo" 			property="dsCargo" 			width="150"/>
		<adsm:gridColumn title="contratacao" 	property="tpIntegrante" 	width="170" isDomain="true"/>
		<adsm:gridColumn title="nome" 			property="nomeIntegrante" 	width="180"/>
		<adsm:gridColumn title="matricula" 		property="matricula" 		width="120" align="right"/>
		<adsm:gridColumn title="cpf" 			property="cpf" 				width="120" align="right"/>
		<adsm:gridColumn title="cooperativa" 	property="cooperativa" 		width="150"/>
		<adsm:buttonBar> 
			<adsm:button caption="fechar" onclick="window.close()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
/**
 * Função que ira fazer a chamada do findPaginated de notaFiscalColeta
 * baseado no idDetalheColeta.
 */
function carregaGriddadosEquipe() {
	var idEventoControleCarga = getElementValue("idEventoControleCarga");
	var filtro = new Array();
	// Monta um map com o campo para sser realizado o filtro
	setNestedBeanPropertyValue(filtro, "idEventoControleCarga", idEventoControleCarga);
	
    var sdo = createServiceDataObject("lms.coleta.consultaLiberacaoRiscoAction.findById", "resultadoPesquisa", filtro);
    xmit({serviceDataObjects:[sdo]});	
}

function resultadoPesquisa_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	} else {
		setElementValue('meioTransporte.nrFrota', getNestedBeanPropertyValue(data,":meioTransporte.nrFrota"));
		var idEquipeOperacao = getNestedBeanPropertyValue(data,":equipeOperacao.idEquipeOperacao");
		setElementValue('equipeOperacao.idEquipeOperacao', idEquipeOperacao);
		
		if (idEquipeOperacao!= undefined && idEquipeOperacao!="") {
			var filtro = new Array();
			// Monta um map com o campo para sser realizado o filtro
			setNestedBeanPropertyValue(filtro, "equipeOperacao.idEquipeOperacao", idEquipeOperacao);
			//chama a pesquisa da grid 
			dadosEquipeGridDef.executeSearch(filtro);
		}
	}
}

/**
 * Função para não chamar nada no onclick da row da grid
 */
function disableRowClick() {
	return false;
}
</script>