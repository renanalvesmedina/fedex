<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="eventos" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina">
			 
	<adsm:form action="/carregamento/consultarControleCargasEventos" idProperty="idLacreControleCarga" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findByIdLacre" >
		<adsm:hidden property="controleCarga.idEvento" serializable="true" />
		<adsm:hidden property="controleCarga.idControleCarga" serializable="true" />
		
		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" width="85%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		
		<adsm:textbox dataType="JTDate" 
			label="dataSaidaPortaria" 
			property="dtSaidaPortaria" size="10" 
			maxLength="10" 
			property="controleCarga.dhSaidaColetaEntrega" 
			picker="true" 
			required="true"/>
		
		<adsm:textarea 
				maxLength="255"
				property="controleCarga.observacao"
				label="observacao"
				columns="115" rows="3" width="70%"
				required="true"
				/>
		</adsm:textbox>
		
	</adsm:form>
	
	<adsm:grid idProperty="idEventoControleCarga" property="eventosControleCarga" 
			   selectionMode="none" gridHeight="280" title="eventos" unique="true" rows="12" autoSearch="false"
			   onRowClick="grid_OnClick" defaultOrder="dhEvento:desc" scrollBars="horizontal"
			   onDataLoadCallBack="retornoGrid"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedEventos"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountEventos"
			   >
		<adsm:gridColumn title="filial" 	property="filial.sgFilial" width="55" />
		<adsm:gridColumn title="tipoEvento"	property="tpEventoControleCarga" isDomain="true" width="210" />
		<adsm:gridColumn title="dataHora" 	property="dhEvento" dataType="JTDateTimeZone" align="center" width="140" />
		<adsm:gridColumn title="usuario" 	property="usuario.nmUsuario" width="165" />
		
		<adsm:gridColumn title="dataSolicitada" 	property="dhSolicitada" dataType="JTDateTimeZone" align="center" width="140" />
		<adsm:gridColumn title="dataOriginal" 	property="dhOriginal" dataType="JTDateTimeZone" align="center" width="140" />
		<adsm:gridColumn title="situacao" 	property="situacaoPendencia" width="115" />
		<adsm:gridColumn title="solicitante" 	property="usuarioSolicitacao" width="165" />
		
		
		<adsm:buttonBar>
			<adsm:button caption="limpar" id="cleanButton" onclick="limpaRegistro()"/>
			<adsm:button caption="salvar" id="alteraDtSaidaPortariaButton" onclick="alteraDtSaidaPortaria();" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>


<script>
function grid_OnClick(id) {
	var data = new Object();
	data.id = id;
	var sdo = createServiceDataObject('lms.carregamento.consultarControleCargasJanelasAction.buscaDtSaidaPortariaById', 'grid_OnClick', data); 
	xmit({serviceDataObjects:[sdo]});
	return false;
}

function grid_OnClick_cb(data, error) {
	if (error != undefined){
		alert(error)
	} else {
		setElementValue("controleCarga.dhSaidaColetaEntrega", data.dataSaidaPortaria, true);
		setElementValue("controleCarga.idEvento", data.idEvento, true);
		setElementValue("controleCarga.observacao", data.observacao, true);
	}
	return false;
}


function getGridRowDataObject(id){
	for (i=0; i<eventosControleCargaGridDef.gridState.data.length; i++) {
		if (eventosControleCargaGridDef.gridState.data[i].id==id){
			return eventosControleCargaGridDef.gridState.data[i];
		}
	}
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		inicializaTela();
		povoaGrid(getElementValue("controleCarga.idControleCarga"));
	}
}

function retornoGrid_cb(data, error) {
	if (error == undefined)
		setFocus(document.getElementById("botaoFechar"), true, true);
}

function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
}

function povoaGrid(idControleCarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
    eventosControleCargaGridDef.executeSearch(filtro, true);
    return false;
}

function inicializaTela() {
	resetValue(this.document);
	povoaDadosMaster();
}

function limpaRegistro() {
	setElementValue("controleCarga.dhSaidaColetaEntrega", "", true);
	setElementValue("controleCarga.observacao", "", true);
}		

function alteraDtSaidaPortaria(){
    if(getElementValue('controleCarga.idEvento') != null && getElementValue('controleCarga.idEvento') != ""){
		if(confirm("Você deseja alterar a data de saída da portaria?")){
            var data = new Object();
                    data.dtSaidaPortaria = getElementValue('controleCarga.dhSaidaColetaEntrega');
                    data.idEvento = getElementValue('controleCarga.idEvento');
                    data.observacao = getElementValue('controleCarga.observacao');

            var sdo = createServiceDataObject('lms.carregamento.consultarControleCargasJanelasAction.alteraDtSaidaPortaria',  'alteraDtSaidaPortaria', data); 
            xmit({serviceDataObjects:[sdo]});
        }
    }
    return false;
}

function alteraDtSaidaPortaria_cb(data, error) {
	if (error != undefined){
		alert(error)
	} else {
		if(data.done != null && data.done == 'ok'){
			setElementValue("controleCarga.dhSaidaColetaEntrega", "", true);
			inicializaTela();
			povoaGrid(getElementValue("controleCarga.idControleCarga"));
		}
	}
	return false;
}

</script>