<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
function onShow() {
	inicializaTela();
	findGrid();
	setDisabled("btnLimpar", false);
	setDisabled("btnVisualizarProposta", false);
	return false;
}

function loadPage() {
	onPageLoad();
	setDisabled("btnLimpar", false);
	setDisabled("btnVisualizarProposta", false);
}

function loadPage_cb(data, error) {
	onPageLoad_cb(data,error);
	setDisabled("btnLimpar", true);
	setDisabled("btnVisualizarProposta", true);
	if(error){
		alert(error);
		return false;
	}	
}

function storePipelineClienteSimulacao_cb(data, error) {
	if(error) {
		alert(error);
		return false;
	}
	limpaCampos();
}

function inicializaTela(){
	var tabGroup = getTabGroup(this.document);	
// 	tipoCliente
	setElementValue("cliente.tpCliente", tabGroup.getTab("cad").getElementById("cliente.tpCliente").value);
	
// 	pipelineCliente
	if(tabGroup.getTab("cad").getElementById("idPipelineCliente").value != "")
		setElementValue("idPipelineCliente", tabGroup.getTab("cad").getElementById("idPipelineCliente").value);
// 	filial
	setElementValue("filialCabecalho.idFilial", tabGroup.getTab("cad").getElementById("filial.idFilial").value);
	setElementValue("filialCabecalho.sgFilial",tabGroup.getTab("cad").getElementById("filial.sgFilial").value);
	setElementValue("filialCabecalho.pessoa.nmFantasia",tabGroup.getTab("cad").getElementById("filial.pessoa.nmFantasia").value);
	
// 	regional
	setElementValue("siglaDescricao", tabGroup.getTab("cad").getElementById("siglaDescricao").value);
// 	funcionario
	setElementValue("usuarioByIdUsuario.idUsuario",tabGroup.getTab("cad").getElementById("usuarioByIdUsuario.idUsuario").value);
	setElementValue("usuarioByIdUsuario.nrMatricula",tabGroup.getTab("cad").getElementById("usuarioByIdUsuario.nrMatricula").value);
	setElementValue("usuarioByIdUsuario.nmUsuario",tabGroup.getTab("cad").getElementById("usuarioByIdUsuario.nmUsuario").value);
// 	cliente
	setElementValue("cliente.idCliente", tabGroup.getTab("cad").getElementById("cliente.idCliente").value);
	setElementValue("cliente.pessoa.nrIdentificacao",tabGroup.getTab("cad").getElementById("cliente.pessoa.nrIdentificacao").value);
	setElementValue("cliente.pessoa.nmPessoa",tabGroup.getTab("cad").getElementById("cliente.pessoa.nmPessoa").value);
	
	setElementValue("tpSituacao",tabGroup.getTab("cad").getElementById("tpSituacao").value);
}

function limpaCampos(){
	resetFilial();
	resetNrProposta();
	resetTabelaPreco();
	resetIdPipelineClienteSimulacao();
}

function resetFilial() {
	setElementValue("filial.idFilial", "");
	setElementValue("filial.sgFilial", "");
	setElementValue("filial.pessoa.nmFantasia", "");
}

function disableFilial(b) {
	setDisabled("filial.idFilial", b);
	setDisabled("filial.sgFilial", b);
	setDisabled("filial.pessoa.nmFantasia", true);
}

function disableStoreButton(b){
	setDisabled("storeButton", b);
}

function resetNrProposta() {
	setElementValue("nrProposta", "");	
}
function disableNrProposta(b) {
	setDisabled("nrProposta", b);	
}

function resetTabelaPreco() {
	setElementValue("tabelaPreco.idTabelaPreco", "");
	setElementValue("tabelaPreco.tabelaPrecoString", "");
	setElementValue("tabelaPreco.dsDescricao", "");
}

function disableTabelaPreco(b) {
	setDisabled("tabelaPreco.idTabelaPreco", b);
	setDisabled("tabelaPreco.tabelaPrecoString", b);
	setDisabled("tabelaPreco.dsDescricao", true);
}

function resetIdPipelineClienteSimulacao() {
	setElementValue("idPipelineClienteSimulacao", "");
}

function changeNrProposta() {
	resetTabelaPreco();
	if(getElementValue('nrProposta') != '') {
		var idFilial = getElementValue('filial.idFilial');
		if(idFilial == '') {
			setElementValue('nrProposta', '');
			setFocus(getElement('nrProposta'), true);
			alertI18nMessage('LMS-01213');
		}
	}
}

function tabelaPrecoLookup(data, error) {
	if(data != undefined) {
		var idTabPrec = data.idTabelaPreco;
		var sdo = createServiceDataObject('lms.vendas.manterPipelineClienteAction.getTabelaPrecoById', 'findTabelaPreco', {'idTabelaPreco':idTabPrec});
		xmit({serviceDataObjects:[sdo]});
	}
}

function tabelaPrecoCallBack_cb(data){
	resetFilial();
	resetNrProposta();
	if(!tabelaPreco_tabelaPrecoString_exactMatch_cb(data)){
		return;
	}
	if(data[0] != undefined) {
		if(data[0].error) {
			alert(data[0].error);
		}
	}
	return;
}

function findTabelaPreco_cb(data, error) {
	resetFilial();
	resetNrProposta();
	if(error) {
		alert(error);
		resetTabelaPreco();
		return false;
	} 
}

function afterStore_cb(data, error) {
	if(error) {
		alert(error);
		return false;
	}
	limpaCampos();
	findGrid();
	setFocusOnFirstFocusableField();
}

function findGrid() {
	findButtonScript('pipelineClienteSimulacao', document.forms[0]);
}

function filialCallBack_cb(data){
	if(!filial_sgFilial_exactMatch_cb(data)){
		return;
	}
	if(data[0] != undefined) {
		if(data[0].error) {
			alert(data[0].error);
		}
		setElementValue("filial.idFilial", data[0].idFilial);
		setElementValue("filial.sgFilial", data[0].sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data[0].pessoa.nmFantasia);
	}
	return;
}

function filialAfterPopupSetValue(data) {
	if(data.error) {
		alert(error);
		resetFilial();
		return;
	}
	setElementValue("filial.idFilial", data.idFilial);
	setElementValue("filial.sgFilial", data.sgFilial);
	setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
}

function initWindow(eventObj) {
	setDisabled("btnLimpar", false);
	setDisabled("btnVisualizarProposta", false);
	
	var tp_situacao = getElementValue('tpSituacao');
	
	var disableFields = (tp_situacao == 'P' || tp_situacao == 'F' || tp_situacao == 'C');
	disableNrProposta(disableFields);
	disableTabelaPreco(disableFields);
	disableFilial(disableFields);
	disableStoreButton(disableFields);
}

function loadPipelineClienteGridRowClick(id) {
	var sdo = createServiceDataObject('lms.vendas.manterPipelineClienteAction.loadPipelineClienteGridRowClick', 'loadPipelineClienteGridRowClick', {'idPipelineClienteSimulacao':id});
	xmit({serviceDataObjects:[sdo]});
	return false;
}

function loadPipelineClienteGridRowClick_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	setElementValue('idPipelineClienteSimulacao', data.idPipelineClienteSimulacao);
	if(data.idFilial != '') {
		setElementValue("filial.idFilial", data.idFilial);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.nmFantasiaFilial);
		setElementValue("nrProposta", data.nrProposta);
	}
	if(data.idTabelaPreco) {
		setElementValue("tabelaPreco.idTabelaPreco", data.idTabelaPreco);
		setElementValue("tabelaPreco.tabelaPrecoString", data.tabelaPrecoString);
		setElementValue("tabelaPreco.dsDescricao", data.dsDescricaoTabelaPreco);
	}
}
</script>
<adsm:window service="lms.vendas.manterPipelineClienteAction" onPageLoad="loadPage" onPageLoadCallBack="loadPage">
	<adsm:form action="/vendas/manterPipelineCliente" idProperty="idPipelineCliente">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-01213"/>
	</adsm:i18nLabels>
	
	<adsm:hidden property="idPipelineClienteSimulacao"/>
	<adsm:hidden property="tpSituacao" serializable="false"/>
	
	<adsm:hidden property="filialCabecalho.idFilial"/>
		
	<adsm:textbox
		label="filial"
		dataType="text"
		property="filialCabecalho.sgFilial"
		size="3"
		maxLength="3"
		required="false"
		disabled="true"
		serializable="false"
		labelWidth="10%"
		width="45%">
		<adsm:textbox
			dataType="text"
			property="filialCabecalho.pessoa.nmFantasia"
			size="25"
			maxLength="60"
			disabled="true"
			serializable="false"
		/>
	</adsm:textbox>
	
	<adsm:hidden property="idRegional" serializable="false" />
	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="35"
		maxLength="60"
		width="30%"
		labelWidth="12%"
		serializable="false"
		required="false"
	/>
	
	<adsm:lookup
		action="/configuracoes/consultarFuncionariosView"
		service="lms.vendas.manterPipelineClienteAction.findLookupFuncionario"
		dataType="text"
		required="false"
		property="usuarioByIdUsuario"
		idProperty="idUsuario"
		criteriaProperty="nrMatricula"
		label="funcionario"
		size="17"
		maxLength="10"
		exactMatch="true"
		width="45%"
		disabled="true"
		labelWidth="10%">
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.nmUsuario"
			modelProperty="nmUsuario" />
		
		<adsm:textbox
			dataType="text" 
			property="usuarioByIdUsuario.nmUsuario" 
			size="35" 
			maxLength="50" 
			disabled="true"
			serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
			required="false"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			disabled="true"
			maxLength="18"
			dataType="text"
			width="45%"
			labelWidth="10%">
			<adsm:propertyMapping
				relatedProperty="clienteSelecionado.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox
			property="cliente.tpCliente" 
			label="tipoCliente"
			domain="DM_TIPO_CLIENTE"
			disabled="true"
			labelWidth="12%"
			width="31%"
			serializable="false"/>
			
	<adsm:section caption="propostasTabelas"/>	
	
	<adsm:lookup 
		property="filial" 
		idProperty="idFilial" 
		required="false" 
		criteriaProperty="sgFilial" 
		maxLength="3"
		service="lms.vendas.manterPipelineClienteAction.findLookupFilial" 
		dataType="text" 
		label="filial" 
		size="3"
		action="/municipios/manterFiliais" 
		labelWidth="10%" 
		width="45%" 
		disabled="false"
		onDataLoadCallBack="filialCallBack" afterPopupSetValue="filialAfterPopupSetValue">
		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmPessoa"/>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true"/>
	</adsm:lookup>
	
	
	<adsm:textbox
		label="numeroProposta"
		dataType="integer" 
		property="nrProposta" 
		size="10" 
		maxLength="10"
		labelWidth="12%"
		width="31%"
		serializable="true"
		onchange="changeNrProposta();"/>
		
		
	<adsm:lookup 
			action="/tabelaPrecos/manterTabelasPreco" 
			criteriaProperty="tabelaPrecoString" 
			dataType="text" 
			exactMatch="true" 
			idProperty="idTabelaPreco" 
			label="tabela"  
			property="tabelaPreco" 
			required="false"
			service="lms.vendas.manterPipelineClienteAction.findLookupTabelaPreco" 
			size="6"
			maxLength="5"
			labelWidth="10%" 
			width="45%"
			onPopupSetValue="tabelaPrecoLookup" onDataLoadCallBack="tabelaPrecoCallBack">
		
			<adsm:propertyMapping relatedProperty="tabelaPreco.dsDescricao" modelProperty="dsDescricao" />
				
			<adsm:textbox
			 	property="tabelaPreco.dsDescricao"
			 	dataType="text"
			 	size="35"
			 	disabled="true" 
			 	maxLength="60"/>
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="btnVisualizarProposta" caption="visualizarProposta" action="/vendas/manterPropostasCliente" cmd="main"/>
			<adsm:storeButton id="storeButton" service="lms.vendas.manterPipelineClienteAction.storePipelineClienteSimulacao" callbackProperty="afterStore"/>
			<adsm:button id="btnLimpar" caption="limpar" onclick="limpaCampos()"/>
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid
		idProperty="idPipelineClienteSimulacao" 
		property="pipelineClienteSimulacao" 
		rowCountService="lms.vendas.manterPipelineClienteAction.getPropostasTabelasRowCountCustom" 
		service="lms.vendas.manterPipelineClienteAction.findPropostasTabelasPaginatedCustom"
		gridHeight="220"
		onRowClick="loadPipelineClienteGridRowClick"
		rows="6"
		unique="false"
		detailFrameName="propostasTabelas">
		<adsm:gridColumn
			title="data"
			property="dataPropostaSimulacao" 
			dataType="JTDate"
			width="10%"
			align="center"/>
		<adsm:gridColumn
			title="numeroProposta" 
			property="numeroProposta" 
			dataType="text"
			width="12%"/>
		<adsm:gridColumn
			title="cliente" 
			property="nmPessoaCliente" 
			width="32%"
			dataType="text" />
		<adsm:gridColumn
			title="divisao" 
			property="divisao" 
			width="13%"
			dataType="text"/>
		<adsm:gridColumn
			title="tabela" 
			property="tabela" 
			width="10%"
			dataType="text"/>
		<adsm:gridColumn
			title="situacao" 
			property="situacao" 
			width="10%"
			isDomain="true"/>		
		<adsm:gridColumn 
			title="efetivada" 
			property="blEfetivada" 
			renderMode="image-check" 
			align="center"/>
		
		<adsm:buttonBar>
			<adsm:removeButton service="lms.vendas.manterPipelineClienteAction.removePipelineClienteSimulacaoByIds"/>
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>