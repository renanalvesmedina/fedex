<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControleCargasSemiReboques" service="lms.carregamento.manterControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina"  >
	<adsm:form action="/carregamento/manterControleCargas">

		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false" />
		<adsm:hidden property="tpControleCarga" serializable="false" />
		<adsm:hidden property="blEntregaDireta" serializable="false" />
		<adsm:hidden property="idRotaIdaVolta" serializable="false" />
		<adsm:hidden property="idControleCarga" />
		<adsm:hidden property="idVeiculoControleCarga" />
		<adsm:hidden property="idTipoMeioTransporteTransportado" />

		<adsm:section caption="novoVeiculo" />

		<adsm:lookup dataType="text" label="solicitContratacao"
					 property="solicitacaoContratacao.filial"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupFilial" 
					 onDataLoadCallBack="retornoFilialSolicitacaoContratacao"
					 onchange="return solicitacaoContratacaoFilial_OnChange();" 
					 action="/municipios/manterFiliais" 
					 size="3" maxLength="3" width="85%" picker="false" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" />	
			<adsm:lookup property="solicitacaoContratacao" 
						 idProperty="idSolicitacaoContratacao" criteriaProperty="nrSolicitacaoContratacao"
						 action="/contratacaoVeiculos/manterSolicitacoesContratacao"
						 service="lms.carregamento.manterControleCargasJanelasAction.findLookupSolicitacaoContratacao"
						 onPopupSetValue="popupSolicitacaoContratacao"
						 dataType="integer" size="10" maxLength="10" mask="0000000000" >
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="solicitacaoContratacao.filial.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="solicitacaoContratacao.filial.sgFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" disable="true" />
				<adsm:propertyMapping modelProperty="tpSolicitacaoContratacao" criteriaProperty="tpControleCarga" disable="true" />
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="solicitacaoContratacao.filial.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="solicitacaoContratacao.filial.sgFilial" blankFill="false" /> 
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" blankFill="false" />
				<adsm:propertyMapping modelProperty="nrIdentificacaoMeioTransp.nrFrota" criteriaProperty="meioTransporte2.nrFrota" disable="true" />
				<adsm:propertyMapping modelProperty="nrIdentificacaoMeioTransp.nrPlaca" criteriaProperty="meioTransporte.nrIdentificador" disable="true" />
			 </adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="solicitacaoContratacao.filial.pessoa.nmFantasia" serializable="false" />


		<adsm:lookup dataType="text" property="meioTransporte2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransporteFrota"
					 onPopupSetValue="popupMeioTransporte"
					 onchange="return meioTransporte2_OnChange()"
					 picker="false" label="meioTransporte" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporte.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" relatedProperty="idTipoMeioTransporteTransportado" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportePlaca"
					 onPopupSetValue="popupMeioTransporte"
					 onchange="return meioTransporte_OnChange()"
					 picker="true" maxLength="25" width="78%" size="24" serializable="true" required="true" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporte2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrota" />
		</adsm:lookup>


		<adsm:hidden property="proprietario.idProprietario" />
		<adsm:textbox label="proprietario" property="proprietario.pessoa.nrIdentificacaoFormatado"
					 dataType="text" size="20" width="85%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:hidden property="tabelaColetaEntrega.idTabelaColetaEntrega" serializable="true" />
		<adsm:combobox property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tabelaColetaEntrega" 
					   service="" 
					   optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" 
					   boxWidth="220" width="85%" serializable="true" autoLoad="false" >
			<adsm:propertyMapping modelProperty="idTabelaColetaEntrega" relatedProperty="tabelaColetaEntrega.idTabelaColetaEntrega" />
		</adsm:combobox>


		<adsm:section caption="localTroca" />
		<adsm:textbox property="dhTroca" label="dataHoraTroca" dataType="JTDateTimeZone" size="20" width="85%" required="true" />

		<adsm:combobox property="pontoParada.idPontoParada" label="pontoParada" 
					   optionProperty="idPontoParada" optionLabelProperty="nmPontoParada" 
					   service="lms.carregamento.manterControleCargasJanelasAction.findPontoParada" 
					   onchange="return pontoParada_OnChange(this)" width="85%" >
			<adsm:propertyMapping modelProperty="idRotaIdaVolta" criteriaProperty="idRotaIdaVolta" />
			<adsm:propertyMapping modelProperty="municipio.idMunicipio" relatedProperty="municipio.idMunicipio" />
			<adsm:propertyMapping modelProperty="municipio.nmMunicipio" relatedProperty="municipio.nmMunicipio" />
			<adsm:propertyMapping modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" relatedProperty="unidadeFederativa.sgUnidadeFederativa" />
		</adsm:combobox>


		<adsm:combobox property="controleTrecho.idControleTrecho" label="trecho" 
					   optionProperty="idControleTrecho" optionLabelProperty="trecho" 
					   service="lms.carregamento.manterControleCargasJanelasAction.findControleTrecho" 
					   width="85%" >
			<adsm:propertyMapping modelProperty="idControleCarga" criteriaProperty="idControleCarga" />
		</adsm:combobox>


		<adsm:lookup dataType="text" label="rodovia" 
					 property="rodovia" idProperty="idRodovia"
					 criteriaProperty="sgRodovia"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupRodovia"
					 action="/municipios/manterRodovias" 
					 size="5" maxLength="10" width="45%"
					 exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="30" disabled="true"/>
		</adsm:lookup>
		

		<adsm:textbox property="nrKmRodoviaTroca" label="km" dataType="text" size="5" maxLength="5" width="25%" />


		<adsm:lookup label="municipio" dataType="text" 
					 property="municipio" idProperty="idMunicipio" 
					 criteriaProperty="nmMunicipio"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupMunicipio"
					 action="/municipios/manterMunicipios" 
					 size="30" maxLength="60" width="45%" 
					 exactMatch="false" minLengthForAutoPopUpSearch="3" required="true" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:propertyMapping modelProperty="unidadeFederativa.sgUnidadeFederativa" relatedProperty="unidadeFederativa.sgUnidadeFederativa" />
		</adsm:lookup>

		<adsm:textbox property="unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" size="5"  width="25%" disabled="true" />

		<adsm:textarea property="dsTroca" label="descricaoLocal" maxLength="300" rows="4" columns="90" width="85%" required="true" />

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="confirmar" id="botaoConfirmar" onclick="javascript:confirmar_onClick(this.form);" disabled="false" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
		
		<script>
			var LMS_05162 = "<adsm:label key='LMS-05162'/>";
		</script>
	</adsm:form>
</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error){
		alert(error);
		return false;
	}

	setElementValue("idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("tpControleCarga", dialogArguments.window.document.getElementById('tpControleCarga').value);
	setElementValue("blEntregaDireta", dialogArguments.window.document.getElementById('blEntregaDireta').value);
	setElementValue("idRotaIdaVolta", dialogArguments.window.document.getElementById('idRotaIdaVolta').value);
	notifyElementListeners({e:document.getElementById("idRotaIdaVolta")});
	notifyElementListeners({e:document.getElementById("idControleCarga")});
	document.getElementById("meioTransporte2.nrFrota").required = "true";
	resetaComboTipoTabela();
	inicializaSolicitacaoContratacao();
	if (getElementValue("tpControleCarga") == "C")
		document.getElementById("controleTrecho.idControleTrecho").required = "false";
	else
		document.getElementById("controleTrecho.idControleTrecho").required = "true";
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.getDataLocalTroca", "resultado_loadDataLocalTroca");
	xmit({serviceDataObjects:[sdo]});
}


function resultado_loadDataLocalTroca_cb(data, error) {
	if (error) {
		alert(error);
		return false;
	}
	setElementValue("dhTroca", setFormat(document.getElementById("dhTroca"), getNestedBeanPropertyValue(data,"dhAtual")));
	setFocusOnFirstFocusableField();
}

function pontoParada_OnChange(combo) {
	var r = comboboxChange({e:combo});
	if (getElementValue('municipio.nmMunicipio') != "")
		setDisabled("municipio.idMunicipio", true);
	else
		setDisabled("municipio.idMunicipio", false);
	return r;
}



/************************************ INICIO - SOLICITACAO CONTRATACAO ************************************/
function solicitacaoContratacaoFilial_OnChange() {
	var r = solicitacaoContratacao_filial_sgFilialOnChangeHandler();
	if (getElementValue('solicitacaoContratacao.filial.sgFilial') == "") {
		resetValue('solicitacaoContratacao.idSolicitacaoContratacao');
		resetValue('solicitacaoContratacao.filial.idFilial');
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", false);
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
	}
	else
		setDisabled('solicitacaoContratacao.idSolicitacaoContratacao', false);
	return r;
}

function retornoFilialSolicitacaoContratacao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = solicitacaoContratacao_filial_sgFilial_exactMatch_cb(data);
	if (r == true) {
		setDisabled('solicitacaoContratacao.idSolicitacaoContratacao', false);
		setFocus(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"));
	}
	return r;
}

function popupSolicitacaoContratacao(data) {
	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", false);
}


function disabledSolicitContratacao(valor) {
	setDisabled("solicitacaoContratacao.filial.idFilial", valor);
	setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", valor);
	if (valor == false) {
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
		document.getElementById("solicitacaoContratacao.filial.sgFilial").required = "true";
		document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao").required = "true";
	}
	else {
		document.getElementById("solicitacaoContratacao.filial.sgFilial").required = "false";
		document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao").required = "false";
	}
}


function inicializaSolicitacaoContratacao() {
	resetValue("solicitacaoContratacao.filial.idFilial");
	resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
	document.getElementById("solicitacaoContratacao.filial.sgFilial").required = "false";
	document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao").required = "false";
	setDisabled("solicitacaoContratacao.filial.idFilial", true);
	setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", true);
}
/************************************ FIM - SOLICITACAO CONTRATACAO ************************************/





/************************************ INICIO - VEICULO ************************************/
function retornoMeioTransporteFrota_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransporte(data);
	}
	return r;
}

function retornoMeioTransportePlaca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransporte(data);
	}
	return r;
}

function callBackMeioTransporte(data) {
	findDadosVeiculo(getNestedBeanPropertyValue(data,"0:idMeioTransporte"), getNestedBeanPropertyValue(data, "0:tpVinculo"));
}

function popupMeioTransporte(data) {
	findDadosVeiculo(getNestedBeanPropertyValue(data,"idMeioTransporte"), getNestedBeanPropertyValue(data, "tpVinculo.value"));
}

function meioTransporte2_OnChange() {
	var r = meioTransporte2_nrFrotaOnChangeHandler();
	if (getElementValue('meioTransporte2.nrFrota') == "") {
		onChangeMeioTransporte();
	}
	return r;
}

function meioTransporte_OnChange() {
	var r = meioTransporte_nrIdentificadorOnChangeHandler();
	if (getElementValue('meioTransporte.nrIdentificador') == "") {
		onChangeMeioTransporte();
	}
	return r;
}

function onChangeMeioTransporte() {
	inicializaSolicitacaoContratacao();
	resetValue("proprietario.idProprietario");
	resetValue("proprietario.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietario.pessoa.nmPessoa");
	resetaComboTipoTabela();
}

function resetMeioTransporte() {
	resetValue("meioTransporte.idMeioTransporte");
	resetValue("meioTransporte.nrIdentificador");
	resetValue("meioTransporte2.idMeioTransporte");
	resetValue("meioTransporte2.nrFrota");
}
/************************************ FIM - VEICULO ************************************/





function findDadosVeiculo(idMeioTransporte, tpVinculo) {
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.findDadosVeiculo", 
		"retornoFindDadosVeiculo", 
		{idMeioTransporte:idMeioTransporte, 
		tpVinculo:tpVinculo, 
		idControleCarga:getElementValue("idControleCarga"),
		idTipoMeioTransporteTransportado:getElementValue("idTipoMeioTransporteTransportado")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindDadosVeiculo_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetMeioTransporte();
		setFocus(document.getElementById("meioTransporte2.nrFrota"));
		return false;
	}
	if (data != undefined) {
		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		setElementValue("proprietario.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietario.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
		setElementValue("idVeiculoControleCarga", getNestedBeanPropertyValue(data,"idVeiculoControleCarga"));
		if (getNestedBeanPropertyValue(data,"blDesabilitaSolicitContratacao") == "false")
			disabledSolicitContratacao(false);
		else
			disabledSolicitContratacao(true);


		if (getElementValue("blEntregaDireta") == "false" && getElementValue("tpControleCarga") == "C") {
			setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', false);
			tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(getNestedBeanPropertyValue(data, "tabelas"));
			if ( document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').length == 2 ) {
				if (document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].text == "") {
					document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
					comboboxChange({e:document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega')});
					setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
				}
			}
		}
	}
}


function resetaComboTipoTabela() {
	tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(new Array());
	setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
}


function confirmar_onClick(form) {
	if (getElementValue("blEntregaDireta") == "false" && getElementValue("tpControleCarga") == "C") {
		document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega").required = "true";

		if (document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').length == 2
			&& document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].text == "" 
			&& document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').selectedIndex == 1)
		{
			document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega").required = "false";
		}
	}
	else
		document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega").required = "false";


	if (!validateForm(form)) {
		return false;
	}
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.storeTrocarVeiculo", 
		"retornoStoreTrocarVeiculo", 
		buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}


function retornoStoreTrocarVeiculo_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	showSuccessMessage();

	var blNecessitaCartaoPedagio = getNestedBeanPropertyValue(data, "blNecessitaCartaoPedagio");
	if (blNecessitaCartaoPedagio != undefined && blNecessitaCartaoPedagio == "true") {
		alert(LMS_05162);
	}
	window.close();
}
</script>