<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.executarRetornarColetasEntregasAction" >

	<adsm:grid property="coletasPendentes" idProperty="idPedidoColeta" 
			   selectionMode="check" 
			   onRowClick="coletas_OnClick" autoSearch="false"
			   gridHeight="210" scrollBars="horizontal" unique="true" 
			   service="lms.coleta.executarRetornarColetasEntregasAction.findPaginatedColetasPendentes"
			   rowCountService="lms.coleta.executarRetornarColetasEntregasAction.getRowCountColetasPendentes">
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" 		property="filialByIdFilialResponsavel.sgFilial" width="45" />
			<adsm:gridColumn title="" 				property="nrColeta" width="75" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="dadosColeta" 	property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="cliente" 		property="cliente.pessoa.nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 				property="cliente.pessoa.nmPessoa" width="250" />
		<adsm:gridColumn title="endereco" 		property="enderecoComComplemento" width="280" />
		<adsm:gridColumn title="volumes" 		property="qtTotalVolumesVerificado" width="80" align="right"/>
		<adsm:gridColumn title="peso" 			property="psTotalVerificado" width="80" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="valor" 			property="moeda.siglaSimbolo" dataType="text" width="60"/>
		<adsm:gridColumn title="" 				property="vlTotalVerificado" dataType="currency" width="90" align="right" />
		<adsm:gridColumn title="horarioColeta" 	property="strHorarioColeta" width="130" align="center"/>
		<adsm:gridColumn title="alterarValores" property="alterarValores" image="/images/popup.gif" openPopup="true" link="javascript:alterarValores" width="100" align="center" />
		<adsm:gridColumn title="veiculo" 		property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="equipe" 		property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>

		<adsm:buttonBar> 
			<adsm:button caption="executar" id="botaoExecutar" onclick="openModalDataHoraEvento()" />
			<adsm:button caption="retornar" id="botaoRetornar" onclick="validatePCERetornarColetasEntregas()" />
		</adsm:buttonBar>
	</adsm:grid>
	
	<adsm:form action="/coleta/executarRetornarColetasEntregas">
		<adsm:hidden property="v1" value="v1" serializable="false"/>
		<script>
			var msgObrigatorio = '<adsm:label key="LMS-00058"/>';
			var msgRegSelec = '<adsm:label key="LMS-00053"/>';
			var msgExecuta = '<adsm:label key="LMS-02012"/>';
		</script>
	</adsm:form>
	
</adsm:window>

<script>
function openModalDataHoraEvento(){
	
	var ids = coletasPendentesGridDef.getSelectedIds().ids;
	if (ids.length == 0) {		
		alert(msgRegSelec);
	}else{
		var idsColeta = ids.join(";");
		var idControleCarga = getIdControleCarga();
		showModalDialog('coleta/informarDataEvento.do?cmd=main&idControleCarga='+idControleCarga+'&idPedidoColeta='+idsColeta ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
	}
}



function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		povoaGrid();
	}
}

function getIdControleCarga() {
    var tabDet = getTabGroup(this.document).parentTab.tabGroup.getTab("conteudo");
    var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");
	if (idControleCarga == undefined)
		idControleCarga = "";
	return idControleCarga;
}

/**
 * Re-carrega a grid apos se clicar em 'alterar valores'
 */
function alterarValores(id){
	showModalDialog('coleta/alteracaoValoresColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
	povoaGrid();
}

function povoaGrid() {
	var idControleCarga = getIdControleCarga();
	if (idControleCarga != "") {
		var filtro = new Array();
		setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
		coletasPendentesGridDef.executeSearch(filtro);
	}
}

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	
	var codigos;
	
	
	/**
	 * Este get existe decorrente de uma necessidade da popUp de alert.
	 */
	function getCodigos() {
		return codigos;
	}
	
	
	function validatePCEExecutarColetasEntregas(data) {
		var service = "lms.coleta.executarRetornarColetasEntregasAction.validatePCEExecutarColetasEntregas";
		var callback = "validatePCEExecutarColetasEntregas";			
		
		var ids = coletasPendentesGridDef.getSelectedIds().ids;
		data.ids = ids;
		
		var sdo = createServiceDataObject(service, callback, data);
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function validatePCERetornarColetasEntregas() {
		var service = "lms.coleta.executarRetornarColetasEntregasAction.validatePCERetornarColetasEntregas";
		var callback = "validatePCERetornarColetasEntregas";
		validatePCE(service, callback);
	}

	/**
	 * Executa a validação do PCE.
	 */	
	function validatePCE(service, callback){
		 
		var ids = coletasPendentesGridDef.getSelectedIds().ids;
		if (ids.length>0) {
			var data = new Object();
			data.ids = ids;
			var sdo = createServiceDataObject(service, callback, data);
			xmit({serviceDataObjects:[sdo]});
		}
		else
			alert(msgRegSelec);
	}
	
	/**
	 * Callback da validacao do "Executar". 
	 *
	 * @param data
	 * @param error
	 */
	function validatePCEExecutarColetasEntregas_cb(data, error) {
		// Janela de chamada para a tela de pce
		// Apos sua chamada cai na funcao de callBack - alertPCE
		codigos = data.codigos;
		if (codigos.length>0) {
			showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
		}			
		
		executar_OnClick(data.dtHoraOcorrencia);
	}
	
	/**
	 * Callback da validacao do "Retornar". 
	 *
	 * @param data
	 * @param error
	 */
	function validatePCERetornarColetasEntregas_cb(data, error) {
		// Janela de chamada para a tela de pce
		// Apos sua chamada cai na funcao de callBack - alertPCE
		codigos = data.codigos;
		if (codigos.length>0) {
			showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
		}
		retornar_OnClick();
	}
	
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################

	function executar_OnClick(dataHora) {
		var idsMap = coletasPendentesGridDef.getSelectedIds().ids;
		var data = new Object();
		data.ids = idsMap;
		data.dtHoraOcorrencia = dataHora;
		
		if (idsMap.length>0) { 
			if (confirm(msgExecuta)) {
				var remoteCall = {serviceDataObjects:new Array()}; 
				remoteCall.serviceDataObjects.push(createServiceDataObject(
					"lms.coleta.executarRetornarColetasEntregasAction.generateExecutarColetasPendentes", "resultadoExecutar", data)); 
				xmit(remoteCall); 		
			}
		} else { 
			alert(msgRegSelec);
		}
	}
	
	function resultadoExecutar_cb(data, error) {
		if (error != undefined) {
			alert(error);
		}
		povoaGrid();
	}
	
	function retornar_OnClick() {
		var idsMap = coletasPendentesGridDef.getSelectedIds();
		if (idsMap.ids.length>0) {
			var idControleCarga = getIdControleCarga();
			showModalDialog('coleta/executarColetasPendentesRetorno.do?cmd=main&idControleCarga='+idControleCarga,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:750px;dialogHeight:200px;');
		}
		else
			alert(msgRegSelec);
	}
	
	function coletas_OnClick(id) {
		return false;
	}
	
	function limpaGrid() {
		coletasPendentesGridDef.resetGrid();
		setDisabled("botaoExecutar", true);
		setDisabled("botaoRetornar", true);
	}
</script>