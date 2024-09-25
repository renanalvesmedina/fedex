<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.programacaoColetasVeiculosAction" >
	<adsm:form action="/coleta/programacaoColetasVeiculos">

		<adsm:hidden property="idControleCarga" serializable="false" />

		<adsm:textbox label="numeroColeta" property="manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" dataType="text" 
			  		  size="3" width="35%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.pedidoColeta.nrColeta" dataType="text" size="10" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="meioTransporte" property="meioTransporte.nrFrota" dataType="text" width="35%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporte.nrIdentificador" size="19" serializable="false" disabled="true" />
		</adsm:textbox>
		
		<script>
			var msgRegSelec = '<adsm:label key="LMS-00053"/>';
			var msgExecuta = '<adsm:label key="LMS-02012"/>';
		</script>
	</adsm:form>

	<adsm:grid property="coletasPendentes" idProperty="idPedidoColeta" 
			   selectionMode="check" gridHeight="303" 
			   scrollBars="both" showPagging="false" unique="true" title="coletasSerRealizadas" 
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedColetasSerRealizadas"
			   rowCountService=""
			   onRowClick="coletas_OnClick" autoSearch="false" >
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" 		property="filialByIdFilialResponsavel.sgFilial" width="45" />
			<adsm:gridColumn title="" 				property="nrColeta" width="80" align="right" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="dadosColeta" 	property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="cliente" 		property="cliente.pessoa.nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 				property="cliente.pessoa.nmPessoa" width="250" />
		<adsm:gridColumn title="endereco" 		property="enderecoComComplemento" width="280" />
		<adsm:gridColumn title="volumes" 		property="qtTotalVolumesVerificado" width="80" align="right"/>
		<adsm:gridColumn title="peso" 			property="psTotalVerificado" width="80" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 				property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 			property="moeda.dsSimbolo" width="40" />	
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlTotalVerificado" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="horarioColeta" 	property="strHorarioColeta" width="130" align="center"/>
		<adsm:gridColumn title="alterarValores" property="alterarValores" image="/images/popup.gif" openPopup="true" link="javascript:alterarValores" width="100" align="center" />

		<adsm:buttonBar freeLayout="false"> 
			<adsm:button caption="executar" id="botaoExecutar" onclick="openModalDataHoraEvento()" />
			<adsm:button caption="retornar" id="botaoRetornar" onclick="validatePCERetornarColetasEntregas()" />
		</adsm:buttonBar>
	</adsm:grid>
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
		povoaDadosMaster();
		var idControleCarga = getElementValue('idControleCarga');
		if (idControleCarga != undefined && idControleCarga != "") {
			setDisabled("botaoRetornar", false);
			setDisabled("botaoExecutar", false);
			setFocus(setFocus(document.getElementById("botaoRetornar")));
			povoaGrid();
		}
	}
}

function getIdControleCarga() {
	var idControleCarga = getElementValue('idControleCarga');    
	if (idControleCarga == undefined)
		idControleCarga = "";
	return idControleCarga;
}

function povoaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var tabDetPesq = tabGroup.getTab("veiculo");
    var filialColeta = tabDetPesq.getFormProperty("filialByIdFilialResponsavel.sgFilial");
    var numeroColeta = tabDetPesq.getFormProperty("nrColeta");

    var tabDetCad = tabGroup.getTab("cad");
    var nrFrota = tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrFrota");
    var nrIdentificador = tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrIdentificador");
    var idControleCarga = tabDetCad.getFormProperty("idControleCarga");
    
    setElementValue("manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" , filialColeta);
    setElementValue("manifestoColeta.pedidoColeta.nrColeta" , numeroColeta);
    setElementValue("meioTransporte.nrFrota" , nrFrota);
    setElementValue("meioTransporte.nrIdentificador" , nrIdentificador);
    setElementValue("idControleCarga" , idControleCarga);
}

/**
 * Re-carrega a grid apos se clicar em 'alterar valores'
 */
function alterarValores(id){
	showModalDialog('coleta/alteracaoValoresColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
	povoaGrid();
}

function povoaGrid() {
	var idControleCarga = getElementValue('idControleCarga');
	var filtro = new Array();
	setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
	coletasPendentesGridDef.executeSearch(filtro);
	return false;
}

function coletas_OnClick(id) {
	return false;
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
				"lms.coleta.programacaoColetasVeiculosAction.generateExecutarColetasPendentes", "resultadoExecutar", data)); 
			xmit(remoteCall); 		
		}
	} else
		alert(msgRegSelec);
}

function resultadoExecutar_cb(data, error) {
	if (error)
		alert(error);
	else {
		showSuccessMessage();
	}
	povoaGrid();
}

function retornar_OnClick() {
	var idsMap = coletasPendentesGridDef.getSelectedIds();
	if (idsMap.ids.length>0) {
		showModalDialog('coleta/programacaoColetasVeiculosRetorno.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:750px;dialogHeight:172px;');
	}
	else
		alert(msgRegSelec);
}
</script>