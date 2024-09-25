<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {

    /* quando a tela é chamada pela Visualizar Andamento (VOL) seta os campos */	
	var url = new URL(parent.location.href);
	var nrFrota = url.parameters["nrFrota"];
	var nrIdentificador = url.parameters["nrIdentificador"];
	var idMeioTransporte = url.parameters["idMeioTransporte"];
	
    setElementValue('meioTransporte2.nrFrota',nrFrota); 
	setElementValue('meioTransporte.nrIdentificador',nrIdentificador); 
	setElementValue('meioTransporte.idMeioTransporte',idMeioTransporte);
	 
	setMasterLink(document, true);
	 
	/* caso a tela não tenha sido chamada pela Visualizar Andamento realiza o procedimento padrão da tela, ou seja,
	   o procedimento que realizava antes da alteração acima */
	if ( idMeioTransporte == undefined ){    
		buscarDadosMaster();
	}
}
</script>
<adsm:window service="lms.coleta.programacaoColetasVeiculosAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/programacaoColetasVeiculos" height="103" idProperty="idPedidoColeta" >
		
		<adsm:hidden property="idPedidoColetaMaster" serializable="false"/>
		
		<adsm:textbox label="numeroColeta" property="filialByIdFilialResponsavel.sgFilial" dataType="text" 
			  		  size="3" width="41%" disabled="true" serializable="false" >
			<adsm:textbox property="nrColeta" dataType="integer" size="10" mask="0000000000" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:hidden property="rotaColetaEntrega1.idRotaColetaEntrega" serializable="false"/>
		<adsm:textbox label="rota" property="rotaColetaEntrega1.dsRota" dataType="text" size="40" labelWidth="14%" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacaoFormatado" dataType="text" size="20" 
					  width="41%" disabled="true" serializable="false" >
			<adsm:textbox property="cliente.pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox label="endereco" property="enderecoComComplemento" dataType="text" size="40" labelWidth="14%" width="30%" 
					  disabled="true" serializable="false" />

		<adsm:textbox label="volumes" property="qtTotalVolumesVerificado" dataType="integer" size="12" width="41%"
				      disabled="true" serializable="false"  />
		<adsm:textbox label="peso" property="psTotalVerificado" dataType="weight"
					  unit="kg" size="14" labelWidth="14%" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="valor" property="moeda.dsSimbolo" dataType="text" size="6" width="41%" disabled="true" 
					  serializable="false" >
			<adsm:textbox dataType="currency" property="vlTotalVerificado" mask="###,###,###,###,##0.00" size="18" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox label="pesoAforado" property="psTotalAforadoVerificado" dataType="weight"
					  unit="kg" size="14"  labelWidth="14%" width="30%" disabled="true" serializable="false" />


		<adsm:hidden property="idFilialUsuario" serializable="false" />
		<adsm:hidden property="sgFilialUsuario" serializable="false" />
		<adsm:hidden property="nmFantasiaFilialUsuario" serializable="false" />
		<adsm:lookup label="rota" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/manterRotaColetaEntrega" 
					 service="lms.coleta.programacaoColetasVeiculosAction.findLookupRotaColetaEntrega" 
					 onDataLoadCallBack="retornoRotaColetaEntrega"
					 onPopupSetValue="popupRotaColetaEntrega"
					 dataType="integer" size="3" maxLength="3" width="41%" serializable="false" >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="dsRotaColetaEntrega" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="idFilialUsuario" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="sgFilialUsuario" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="nmFantasiaFilialUsuario" />
			<adsm:textbox property="dsRotaColetaEntrega" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:lookup>


		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:lookup dataType="text" property="meioTransporte2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.coleta.programacaoColetasVeiculosAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoFrota"
					 picker="false" label="meioTransporte" labelWidth="14%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporte.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificador" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 service="lms.coleta.programacaoColetasVeiculosAction.findLookupMeioTransporte" 
					 onDataLoadCallBack="retornoPlaca"
					 onPopupSetValue="popupMeioTransporte"
					 picker="true" maxLength="25" width="18%" size="16" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporte2.nrFrota" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte2.idMeioTransporte" />	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrota" />
		</adsm:lookup>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="botaoConsultar" onclick="consultar_OnClick()" disabled="false" />
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limpar_OnClick()" disabled="false" />
		</adsm:buttonBar>

		<script>
			var msgRegSelec = '<adsm:label key="LMS-00053"/>';
			var msgExecuta = '<adsm:label key="LMS-02042"/>';
			var msgProcedGerRisco = '<adsm:label key="LMS-02070"/>';
		</script>
		
		<adsm:hidden property="blRedirecionar" serializable="false" />
		
		<adsm:hidden property="nmUsuarioLiberacao" serializable="false" />
		<adsm:hidden property="dsMotivoLiberacao" serializable="false" />
	</adsm:form>

	<adsm:grid property="coletas" idProperty="idControleCarga" 
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedProgramacaoColetasVeiculos"
			   rowCountService="lms.coleta.programacaoColetasVeiculosAction.getRowCountProgramacaoColetasVeiculos"
			   onRowClick="coletas_OnClick" 
			   rows="9" gridHeight="180" scrollBars="horizontal" unique="true" selectionMode="radio" autoSearch="false" >

		<adsm:gridColumn title="meioTransporte" property="meioTransporteByIdTransportado.nrFrota" width="60" />
		<adsm:gridColumn title="" 				property="meioTransporteByIdTransportado.nrIdentificador" width="90" />
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">	
			<adsm:gridColumn title="controleCargas" property="filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" 				property="nrControleCarga" width="100" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="veiculo" 		property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="equipe" 		property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="capacidade" 	property="meioTransporteByIdTransportado.nrCapacidadeKg" width="120" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="valorVeiculo" 	property="siglaSimbolo1" width="60" />
		<adsm:gridColumn title="" 				property="vlTotalFrota" width="100" dataType="currency" align="right" />
		<adsm:gridColumn title="valorColetar" 	property="siglaSimbolo2" width="60" />
		<adsm:gridColumn title="" 				property="vlAColetar" width="100" dataType="currency" align="right" />
		<adsm:gridColumn title="pesoVeiculo" 	property="psTotalFrota" width="120" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="pesoColetar" 	property="psAColetar" width="120" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="capacidadeOciosa" property="pcOcupacaoInformado" width="140" align="right" dataType="decimal" unit="percent" />
		<adsm:gridColumn title="tipoVeiculo" 	property="meioTransporteByIdTransportado.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" width="150" />
		<adsm:buttonBar> 
			<adsm:button caption="retornar" id="botaoRetornar" onclick="javaScript:parent.parent.redirectPage('coleta/programacaoColetas.do?cmd=main');" disabled="false" />
			<adsm:button caption="transmitir" id="botaoTransmitir" onclick="transmitir_OnClick()" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	setElementValue("blRedirecionar", "");
	setDisabled('botaoConsultar', false);
	setDisabled('botaoLimpar', false);
	setDisabled('botaoRetornar', false);
	setDisabled('botaoTransmitir', false);
	if (eventObj.name == "tab_click") {
		desabilitaTabs(true);
		consultar_OnClick();
	}
}

function desabilitaTabs(disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("cad", disabled);
	tabGroup.setDisabledTab("pendentes", disabled);
	tabGroup.setDisabledTab("realizadas", disabled);
	tabGroup.setDisabledTab("riscos", disabled);
}

function buscarDadosMaster() {
	var sdo = createServiceDataObject("lms.coleta.programacaoColetasVeiculosAction.findById", "resultado_buscarDadosMaster", 
			{idPedidoColeta:getElementValue('idPedidoColetaMaster')});
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa os campos com os dados retornados da busca em manifesto
 */
function resultado_buscarDadosMaster_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue('idPedidoColeta', getNestedBeanPropertyValue(data,"idPedidoColeta"));
	setElementValue('filialByIdFilialResponsavel.sgFilial', getNestedBeanPropertyValue(data,"filialByIdFilialResponsavel.sgFilial"));
	setElementValue('nrColeta', getNestedBeanPropertyValue(data,"nrColeta"));
	setElementValue('psTotalAforadoVerificado', getNestedBeanPropertyValue(data,"psTotalAforadoVerificado"));
	setElementValue("psTotalAforadoVerificado", setFormat(document.getElementById("psTotalAforadoVerificado"), getNestedBeanPropertyValue(data,"psTotalAforadoVerificado")) );
	setElementValue('rotaColetaEntrega1.idRotaColetaEntrega', getNestedBeanPropertyValue(data,"rotaColetaEntrega.idRotaColetaEntrega"));
	setElementValue('rotaColetaEntrega1.dsRota', getNestedBeanPropertyValue(data,"rotaColetaEntrega.dsRota"));
	setElementValue('rotaColetaEntrega.idRotaColetaEntrega', getNestedBeanPropertyValue(data,"rotaColetaEntrega.idRotaColetaEntrega"));
	setElementValue('rotaColetaEntrega.nrRota', getNestedBeanPropertyValue(data,"rotaColetaEntrega.nrRota"));
	setElementValue('dsRotaColetaEntrega', getNestedBeanPropertyValue(data,"rotaColetaEntrega.dsRota"));
	setElementValue('qtTotalVolumesVerificado', getNestedBeanPropertyValue(data,"qtTotalVolumesVerificado"));
	setElementValue('moeda.dsSimbolo', getNestedBeanPropertyValue(data,"moeda.siglaSimbolo"));
	setElementValue("vlTotalVerificado", setFormat(document.getElementById("vlTotalVerificado"), getNestedBeanPropertyValue(data,"vlTotalVerificado")) );
	setElementValue('enderecoComComplemento', getNestedBeanPropertyValue(data,"enderecoComComplemento"));
	setElementValue('cliente.pessoa.nrIdentificacaoFormatado', getNestedBeanPropertyValue(data,"cliente.pessoa.nrIdentificacaoFormatado"));
	setElementValue('cliente.pessoa.nmPessoa', getNestedBeanPropertyValue(data,"cliente.pessoa.nmPessoa"));
	setElementValue("psTotalVerificado", setFormat(document.getElementById("psTotalVerificado"), getNestedBeanPropertyValue(data,"psTotalVerificado")) );
	setElementValue('idFilialUsuario', getNestedBeanPropertyValue(data, "idFilialUsuario"));
	setElementValue('sgFilialUsuario', getNestedBeanPropertyValue(data, "sgFilialUsuario"));
	setElementValue('nmFantasiaFilialUsuario', getNestedBeanPropertyValue(data, "nmFantasiaFilialUsuario"));
	format(document.getElementById('nrColeta'));
	povoaGrid(getElementValue('rotaColetaEntrega.idRotaColetaEntrega'));
}


function coletas_OnClick(idControleCarga) {
	desabilitaTabs(false);
}

function povoaGrid(idRotaColetaEntrega, idMeioTransporte, autoFocusOnGrid) {
	var filtro = new Array();
	setNestedBeanPropertyValue(filtro, "idPedidoColeta", getElementValue('idPedidoColetaMaster'));
	if (idRotaColetaEntrega != undefined)
		setNestedBeanPropertyValue(filtro, "rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega);
	if (idMeioTransporte != undefined)
		setNestedBeanPropertyValue(filtro, "meioTransporte.idMeioTransporte", idMeioTransporte);

	if (autoFocusOnGrid != undefined)
		coletasGridDef.executeSearch(filtro, null, null, true);
	else
		coletasGridDef.executeSearch(filtro);
	return false;
}

function limpar_OnClick(){
	resetValue('meioTransporte.idMeioTransporte');
	resetValue('meioTransporte2.idMeioTransporte');
	resetValue('rotaColetaEntrega.idRotaColetaEntrega');
	povoaGrid(getElementValue('rotaColetaEntrega.idRotaColetaEntrega'));
	setFocusOnFirstFocusableField();
}

function consultar_OnClick(){
	if (getElementValue('meioTransporte.idMeioTransporte') != "" || getElementValue('rotaColetaEntrega.idRotaColetaEntrega') != "") {
		povoaGrid(getElementValue('rotaColetaEntrega.idRotaColetaEntrega'), getElementValue('meioTransporte.idMeioTransporte'), true);
	}
	else
		povoaGrid(null, null, true);
}



function transmitir_OnClick() {
	var idsMap = coletasGridDef.getSelectedIds();
	if (idsMap.ids.length>0) { 
		if (confirm(msgExecuta)) {
			var idMeioTransporte = coletasGridDef.gridState.data[getPosSelectedId()].meioTransporteByIdTransportado.idMeioTransporte;
			var sdo = createServiceDataObject("lms.coleta.programacaoColetasVeiculosRetornoAction.generateLiberacaoGerRisco", 
				"resultadoLiberacaoGerRisco", 
				{idControleCarga:idsMap.ids[0], idMeioTransporte:idMeioTransporte, idPedidoColeta:getElementValue('idPedidoColetaMaster')}
			); 
			xmit({serviceDataObjects:[sdo]});
		}
	} else
		alert(msgRegSelec);
}

function resultadoLiberacaoGerRisco_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (getNestedBeanPropertyValue(data, "blPossuiLiberacaoGerRisco") == "false") {
		resetValue("nmUsuarioLiberacao");
		resetValue("dsMotivoLiberacao");
		showModalDialog('coleta/transmitirColetasLiberacaoGerRisco.do?cmd=liberacao',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:700px;dialogHeight:200px;');
		if (getElementValue("nmUsuarioLiberacao") != "") {
			generateTransmitirColeta(getNestedBeanPropertyValue(data,"idControleCarga"), getNestedBeanPropertyValue(data,"blRedirecionar"));
		}
	}
	else
		verificaRedirecionamento(getNestedBeanPropertyValue(data,"blRedirecionar"));
}



function generateTransmitirColeta(idControleCarga, blRedirecionar) {
	var idMeioTransporte = coletasGridDef.gridState.data[getPosSelectedId()].meioTransporteByIdTransportado.idMeioTransporte;
	var sdo = createServiceDataObject("lms.coleta.programacaoColetasVeiculosRetornoAction.generateTransmitirColeta", 
		"resultadoTransmitirColeta", 
		{idControleCarga:idControleCarga, idMeioTransporte:idMeioTransporte, 
		idPedidoColeta:getElementValue('idPedidoColetaMaster'), blRedirecionar:blRedirecionar, 
		nmUsuarioByLiberacao:getElementValue("nmUsuarioLiberacao"), dsMotivoByLiberacao:getElementValue("dsMotivoLiberacao")}
	); 
	xmit({serviceDataObjects:[sdo]});
}

function resultadoTransmitirColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	verificaRedirecionamento(getNestedBeanPropertyValue(data,"blRedirecionar"));
}


function verificaRedirecionamento(blRedirecionar) {
	if (blRedirecionar == "true") {
		alert(msgProcedGerRisco);
		desabilitaTabs(false);
		setElementValue("blRedirecionar", "true");
		coletasGridDef.detailGridRow("onDataLoad", coletasGridDef.gridState.data[getPosSelectedId()].idControleCarga);
	}
	else
		parent.parent.redirectPage("coleta/programacaoColetas.do?cmd=main");
}




/**
 * Retorna o nr da linha do item selecionado.
 */
function getPosSelectedId() {
	var gridFormElems = document.forms[1].document.getElementById("coletas.form").elements;
	var selectedIds = new Array();
	for (var j = 0; j < gridFormElems.length; j++) 
	{
		if ((gridFormElems[j].name.indexOf("."+this.idProperty)>0) || (gridFormElems[j].type == "radio")) {
			if (gridFormElems[j].checked) {
				if (gridFormElems[j].value != "undefined" && gridFormElems[j].value != "null") {
					return extractRow(gridFormElems[j+1].name);
				}
			}
		}
	}
}

function extractRow(id) {
	var x = parseInt(parseInt(id.substring(id.lastIndexOf(':')+1,id.indexOf('.'))));
	return x;
}

function retornoRotaColetaEntrega_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = rotaColetaEntrega_nrRota_exactMatch_cb(data);
	if (r == true) {
		resetValue('meioTransporte2.idMeioTransporte');
		resetValue('meioTransporte.idMeioTransporte');
	}
	return r;
}

function retornoFrota_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		resetValue('rotaColetaEntrega.idRotaColetaEntrega');
	}
	return r;
}

function retornoPlaca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		resetValue('rotaColetaEntrega.idRotaColetaEntrega');
	}
	return r;
}

function popupRotaColetaEntrega(data) {
	resetValue('meioTransporte2.idMeioTransporte');
	resetValue('meioTransporte.idMeioTransporte');
}

function popupMeioTransporte(data) {
	resetValue('rotaColetaEntrega.idRotaColetaEntrega');
}
</script>