<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	onPageLoad();
}
</script>
<adsm:window service="lms.carregamento.cancelarControleCargasAction" onPageLoad="carregaPagina" onPageLoadCallBack="retornoCarregarPagina" >
	<adsm:form action="/carregamento/cancelarControleCargas">

		<adsm:lookup dataType="text" label="controleCargas"
					 property="controleCarga.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.cancelarControleCargasAction.findLookupFilialByControleCarga" 
					 onDataLoadCallBack="retornoFilialControleCarga"
					 onchange="return controleCargaFilial_OnChange()"
					 action="/municipios/manterFiliais" 
					 size="3" maxLength="3" labelWidth="18%" width="82%" picker="false" serializable="false" >
	 		<adsm:lookup dataType="integer" property="controleCarga" 
	 					 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.cancelarControleCargasAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" 
						 onDataLoadCallBack="retornoControleCarga"
						 onPopupSetValue="popupControleCarga"
						 onchange="return controleCarga_OnChange()"
						 popupLabel="pesquisarControleCarga"
						 size="9" maxLength="8" mask="00000000" disabled="true" serializable="true" required="true" >
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox label="tipo" property="tpControleCarga.description" dataType="text" labelWidth="18%" width="32%" 
					  disabled="true" serializable="false" />

		<adsm:textbox dataType="text" label="solicitContratacao" property="solicitacaoContratacao.filial.sgFilial" 
					  size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="solicitacaoContratacao.nrSolicitacaoContratacao"
						 dataType="integer" size="10" mask="0000000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrFrota" 
					  label="meioTransporte" labelWidth="18%" width="32%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrIdentificador" size="24" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="meioTransporteByIdSemiRebocado.nrFrota" 
					  label="semiReboque" labelWidth="18%" width="32%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdSemiRebocado.nrIdentificador" size="24" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="proprietario" property="proprietario.pessoa.nrIdentificacaoFormatado"
					 dataType="integer" size="20" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:combobox property="motivoCancelamentoCc.idMotivoCancelamentoCc" label="motivoCancelamento"
					   service="lms.carregamento.cancelarControleCargasAction.findMotivoCancelamentoCc" 
					   optionProperty="idMotivoCancelamentoCc" optionLabelProperty="dsMotivoCancelamentoCc" 
					   labelWidth="18%" width="82%" required="true" onlyActiveValues="true" />

		<adsm:textarea label="observacaoCancelamento" property="dsEvento" maxLength="300" columns="100" rows="4" 
					   labelWidth="18%" width="82%" />

		<adsm:section caption="viagem"/>
		<adsm:textbox label="tipoRota" dataType="text" property="tpRotaViagem.description" labelWidth="18%" width="32%" 
					  disabled="true" serializable="false" />

		<adsm:textbox label="rotaViagem" property="rotaIdaVolta.nrRota" dataType="integer" size="4" 
					  labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="rotaIdaVolta.rota.dsRota" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="horaSaida" property="hrPrevisaoSaida" dataType="JTTime" labelWidth="18%" width="32%"
					  picker="false" disabled="true" serializable="false" />

		<adsm:textbox label="tempoViagem" property="hrTempoViagem" dataType="text" labelWidth="18%" width="32%"
					  disabled="true" picker="false" size="6" maxLength="6" serializable="false" />

		<adsm:section caption="coletaEntrega"/>
		<adsm:textbox label="rotaColetaEntrega" property="rotaColetaEntrega.nrRota" dataType="integer" size="3" 
					  disabled="true" serializable="false" labelWidth="18%" width="82%" >
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="cancelarControleCargasItem" id="botaoCancelar" disabled="false" 
						 onclick="cancelar_onClick(this.form); " />
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == 'tab_load') {
		setDisabled("controleCarga.idControleCarga", false);
		setDisabled("controleCarga.nrControleCarga", true);
	}
	setFocusOnFirstFocusableField();
}


function retornoCarregarPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (getElementValue("controleCarga.filialByIdFilialOrigem.idFilial") != "" && getElementValue("controleCarga.idControleCarga") != "") {
		findDataControleCarga(getElementValue("controleCarga.idControleCarga"));
	}
}


function inicializaTela() {
	resetDocumentValue(this.document);
	setDisabled('botaoCancelar', true);
	setFocusOnFirstFocusableField();
}

function cancelar_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}

    var sdo = createServiceDataObject("lms.carregamento.cancelarControleCargasAction.generateCancelamentoControleCarga", "cancelar", 
    		{idControleCarga:getElementValue("controleCarga.idControleCarga"), 
    		 dsEvento:getElementValue("dsEvento"), 
    		 idMotivoCancelamentoCc:getElementValue("motivoCancelamentoCc.idMotivoCancelamentoCc")}
    );
    xmit({serviceDataObjects:[sdo]});
}


function cancelar_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
   	showSuccessMessage();
}




/************************************ INICIO - CONTROLE CARGA ************************************/
function controleCargaFilial_OnChange() {
	var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	if (getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial') == "") {
		setDisabled("controleCarga.nrControleCarga", true);
		inicializaTela();
	}
	else
		setDisabled('controleCarga.idControleCarga', false);
	return r;
}

function controleCarga_OnChange() {
	var r = controleCarga_nrControleCargaOnChangeHandler();
	if (getElementValue('controleCarga.nrControleCarga') == "") {
		setDisabled("controleCarga.nrControleCarga", true);
		inicializaTela();
	}
	return r;
}


function retornoFilialControleCarga_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
	if (r == true) {
		setDisabled('controleCarga.idControleCarga', false);
		setFocus(document.getElementById("controleCarga.nrControleCarga"));
	}
	return r;
}

function retornoControleCarga_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = controleCarga_nrControleCarga_exactMatch_cb(data);
	if (r == true) {
		findDataControleCarga(getElementValue('controleCarga.idControleCarga'));
	}
	return r;
}


function popupControleCarga(data) {
	setDisabled('controleCarga.idControleCarga', false);
	findDataControleCarga(getNestedBeanPropertyValue(data,"idControleCarga"));
}


function findDataControleCarga(idControleCarga) {
	var sdo = createServiceDataObject("lms.carregamento.cancelarControleCargasAction.findDataControleCarga", "resultado_findDataControleCarga", 
			{idControleCarga:idControleCarga});
    xmit({serviceDataObjects:[sdo]});
}

function resultado_findDataControleCarga_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setDisabled('botaoCancelar', true);
		resetValue('controleCarga.idControleCarga');
		setFocus('controleCarga.nrControleCarga');
		return false;
	}
	if (data != undefined) {
		setDisabled("botaoCancelar", false);
		setElementValue("tpControleCarga.description", getNestedBeanPropertyValue(data,"tpControleCarga.description"));
		setElementValue("solicitacaoContratacao.filial.sgFilial", getNestedBeanPropertyValue(data,"solicitacaoContratacao.filial.sgFilial"));
		setElementValue("solicitacaoContratacao.nrSolicitacaoContratacao", setFormat(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"), getNestedBeanPropertyValue(data,"solicitacaoContratacao.nrSolicitacaoContratacao")) );
		setElementValue("meioTransporteByIdTransportado.nrFrota", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrFrota"));
		setElementValue("meioTransporteByIdTransportado.nrIdentificador", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrIdentificador"));
		setElementValue("meioTransporteByIdSemiRebocado.nrFrota", getNestedBeanPropertyValue(data,"meioTransporteByIdSemiRebocado.nrFrota"));
		setElementValue("meioTransporteByIdSemiRebocado.nrIdentificador", getNestedBeanPropertyValue(data,"meioTransporteByIdSemiRebocado.nrIdentificador"));
		setElementValue("proprietario.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietario.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
		setElementValue("tpRotaViagem.description", getNestedBeanPropertyValue(data,"tpRotaViagem.description"));
		setElementValue("rotaIdaVolta.nrRota", getNestedBeanPropertyValue(data,"rotaIdaVolta.nrRota"));
		setElementValue("rotaIdaVolta.rota.dsRota", getNestedBeanPropertyValue(data,"rotaIdaVolta.rota.dsRota"));
		setElementValue("hrPrevisaoSaida", getNestedBeanPropertyValue(data,"hrPrevisaoSaida"));
		setElementValue("hrTempoViagem", getNestedBeanPropertyValue(data,"hrTempoViagem"));
		setElementValue("rotaColetaEntrega.nrRota", getNestedBeanPropertyValue(data,"rotaColetaEntrega.nrRota"));
		setElementValue("rotaColetaEntrega.dsRota", getNestedBeanPropertyValue(data,"rotaColetaEntrega.dsRota"));
		setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
		setDisabled("controleCarga.idControleCarga", false);
		setFocus(document.getElementById("motivoCancelamentoCc.idMotivoCancelamentoCc"));
	}
}

/************************************ FIM - CONTROLE CARGA ************************************/


</script>