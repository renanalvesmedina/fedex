<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.consultarControleCargasAction" onPageLoadCallBack="retornoCarregaPagina">
	
	<adsm:form action="/carregamento/consultarControleCargas" height="140" >

		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="tpVigente" value="S" serializable="false" />

		<adsm:hidden property="idFilialUsuario" serializable="false" />
		<adsm:hidden property="sgFilialUsuario" serializable="false" />
		<adsm:hidden property="nmFantasiaFilialUsuario" serializable="false" />

		<adsm:hidden property="blmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="blDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>


		<adsm:range label="periodo" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="JTDate" size="10" maxLength="10" property="dtGeracaoInicial" picker="true" />
			<adsm:textbox dataType="JTDate" size="10" maxLength="10" property="dtGeracaoFinal" picker="true" />
		</adsm:range>

		<adsm:combobox label="tipo" property="tpControleCarga" domain="DM_TIPO_CONTROLE_CARGAS" labelWidth="18%" width="32%" renderOptions="true"
					   onchange="return tpControleCarga_OnChange(this);" />

		<adsm:combobox label="tipoRotaViagem" property="tpRotaViagem" domain="DM_TIPO_ROTA_VIAGEM_CC" labelWidth="17%" width="33%" renderOptions="true"/>

		<adsm:lookup label="rotaViagem" property="rotaIdaVolta" 
			 		 idProperty="idRotaIdaVolta" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/consultarRotas" cmd="idaVolta" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupRotaIdaVolta" 
					 dataType="integer" size="4" maxLength="4" labelWidth="18%" width="32%" >
			<adsm:propertyMapping modelProperty="vigentes" criteriaProperty="tpVigente" disable="true" />
			<adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="rotaIdaVolta.rota.dsRota" />
			<adsm:textbox property="rotaIdaVolta.rota.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:lookup>

		<adsm:lookup label="rotaViagemEventual" property="rota" 
			 		 idProperty="idRota" 
			 		 criteriaProperty="dsRota"
					 action="/municipios/manterPostosPassagemRotasViagem" cmd="list" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupRota" 
					 onchange="return rotaViagemEventual_OnChange();"
					 dataType="text" size="30" maxLength="30" labelWidth="17%" width="33%" />

		<adsm:lookup label="origem" dataType="text" 
					 property="filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 onDataLoadCallBack="retornoFilialOrigem"
					 size="3" maxLength="3" labelWidth="18%" width="32%" >
	        <adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="blmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="flagDesabilitaEmpresaUsuarioLogado" criteriaProperty="blDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:textbox label="numero" dataType="integer" property="nrControleCarga" 
					  size="9" maxLength="8" mask="00000000" onchange="return nrControleCarga_OnChange()" 
					  labelWidth="17%" width="33%" />



		<adsm:lookup label="destino" dataType="text" 
					 property="filialByIdFilialDestino"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 size="3" maxLength="3" labelWidth="18%" width="32%" >
	        <adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="blmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="flagDesabilitaEmpresaUsuarioLogado" criteriaProperty="blDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<!-- LMSA-6340 -->
		<adsm:combobox label="cargaCompartilhada"
                       property="tpCargaCompartilhada"
		               domain="DM_TIPO_CARGA_COMPARTILHADA"
		               labelWidth="17%" width="33%" renderOptions="true" />



		<adsm:range label="horaSaidaViagem" labelWidth="18%" width="32%" >
			<adsm:textbox dataType="JTTime" size="5" maxLength="5" property="hrPrevisaoSaidaInicial" picker="true" />
			<adsm:textbox dataType="JTTime" size="5" maxLength="5" property="hrPrevisaoSaidaFinal" picker="true"/>
		</adsm:range>
		
		<adsm:lookup dataType="text" label="solicitContratacao"
					 property="solicitacaoContratacao.filial"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupFilial" 
					 onDataLoadCallBack="retornoFilialSolicitacaoContratacao"
					 onchange="return solicitacaoContratacaoFilial_OnChange();" 
					 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 size="3" maxLength="3" labelWidth="17%" width="33%" picker="false" serializable="false" >
	        <adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="blmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="flagDesabilitaEmpresaUsuarioLogado" criteriaProperty="blDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" />	
			<adsm:lookup property="solicitacaoContratacao" 
						 idProperty="idSolicitacaoContratacao" criteriaProperty="nrSolicitacaoContratacao"
						 action="/contratacaoVeiculos/manterSolicitacoesContratacao"
						 service="lms.carregamento.consultarControleCargasAction.findLookupSolicitacaoContratacao"
						 onPopupSetValue="popupSolicitacaoContratacao"
						 dataType="integer" size="10" maxLength="10" mask="0000000000" >
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="solicitacaoContratacao.filial.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="solicitacaoContratacao.filial.sgFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" disable="false" />
				<adsm:propertyMapping modelProperty="tpSolicitacaoContratacao" criteriaProperty="tpControleCarga" disable="true" />
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="solicitacaoContratacao.filial.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="solicitacaoContratacao.filial.sgFilial" blankFill="false" /> 
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" blankFill="false" />
			 </adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="solicitacaoContratacao.filial.pessoa.nmFantasia" serializable="false" />
		
		<adsm:lookup label="rotaColetaEntrega" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/manterRotaColetaEntrega" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupRotaColetaEntrega" 
					 dataType="integer" size="3" maxLength="3" labelWidth="18%" width="32%" >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="idFilialUsuario" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="sgFilialUsuario" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="nmFantasiaFilialUsuario" />
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:lookup>


		<adsm:lookup dataType="text" label="notaCredito" 
					 property="notaCredito.filial" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 action="/municipios/manterFiliais" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupFilial"
					 onDataLoadCallBack="retornoFilialNotaCredito"
					 onchange="return notaCreditoFilial_OnChange();" popupLabel="pesquisarFilial"
					 size="3" maxLength="3" labelWidth="17%" width="33%" picker="false" serializable="false" >
	        <adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="blmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="flagDesabilitaEmpresaUsuarioLogado" criteriaProperty="blDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="notaCredito.filial.pessoa.nmFantasia" />	
			<adsm:lookup dataType="integer" property="notaCredito" 
						 idProperty="idNotaCredito" criteriaProperty="nrNotaCredito"
						 service="lms.carregamento.consultarControleCargasAction.findLookupNotaCredito" 
						 action="/freteCarreteiroColetaEntrega/manterNotasCredito" 
						 onPopupSetValue="popupNotaCredito"
						 onDataLoadCallBack="retornoNrNotaCredito"
						 size="10" maxLength="10" mask="0000000000" >
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="notaCredito.filial.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="notaCredito.filial.sgFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="notaCredito.filial.pessoa.nmFantasia" disable="false" />
				<adsm:propertyMapping modelProperty="reciboFreteCarreteiro2.filial.sgFilial" criteriaProperty="notaCredito.filial.sgFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="notaCredito.filial.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="notaCredito.filial.sgFilial" blankFill="false" /> 
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="notaCredito.filial.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="notaCredito.filial.pessoa.nmFantasia" serializable="false" />


		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.consultarControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="false" label="meioTransporte" size="6" maxLength="6" labelWidth="18%" width="7%" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportado.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportado.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.consultarControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="true" size="24" maxLength="25" width="25%" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />		
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocado2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.consultarControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="false" label="semiReboque" size="6" maxLength="6" labelWidth="17%" width="7%" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdSemiRebocado.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdSemiRebocado.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocado" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.consultarControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="true" size="24" maxLength="25" width="26%" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdSemiRebocado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdSemiRebocado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />
		</adsm:lookup>


		<adsm:lookup label="proprietario" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
					 idProperty="idProprietario"
					 property="proprietario" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterProprietarios" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupProprietario" 
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="proprietario.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="proprietario.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" disable="true" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
					 idProperty="idMotorista"
					 property="motorista" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupMotorista" 
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motorista.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="motorista.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" disable="true" />
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:combobox label="status" property="tpStatusControleCarga" domain="DM_STATUS_CONTROLE_CARGA" labelWidth="18%" width="82%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="botaoConsultar" onclick="consultar_OnClick(this.form);" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid idProperty="idControleCarga" property="controleCargas" 
			   selectionMode="none" scrollBars="horizontal" unique="true" gridHeight="140"
			   onRowClick="controleCargas_OnClick"
			   service="lms.carregamento.consultarControleCargasAction.findPaginated"
			   rowCountService="lms.carregamento.consultarControleCargasAction.getRowCount"
			   rows="7" >

		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">	   
			<adsm:gridColumn title="controleCargas" 	property="filialByIdFilialOrigem.sgFilial" width="50" />
			<adsm:gridColumn title="" 					property="nrControleCarga" width="80" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="geracao" 			property="dhGeracao" width="125" dataType="JTDateTimeZone" align="center" />		
		<adsm:gridColumn title="tipo" 				property="tpControleCarga" isDomain="true" width="110" />
		<adsm:gridColumn title="rota" 				property="dsRota" width="120" />
		<adsm:gridColumn title="tipoRota" 			property="tpRotaViagem" width="120" />
		<adsm:gridColumn title="meioTransporte" 	property="meioTransporteByIdTransportado.nrFrota" width="60" />
		<adsm:gridColumn title="" 					property="meioTransporteByIdTransportado.nrIdentificador" width="110" />
		<adsm:gridColumn title="semiReboque" 		property="meioTransporteByIdSemiRebocado.nrFrota" width="60" />
		<adsm:gridColumn title="" 					property="meioTransporteByIdSemiRebocado.nrIdentificador" width="110" />
		<adsm:gridColumn title="horaSaidaViagem" 	property="hrPrevisaoSaida" dataType="JTTime" width="120" align="center" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	setDisabled("botaoConsultar", false);
	if (eventObj.name == "tab_click") {
		desabilitaTabs(true);
	}
	else
	if (eventObj.name == "tab_load") {
		desabilitaSolicitacaoContratacao();
		desabilitaNotaCredito();
	}
	else 
	if (eventObj.name == "cleanButton_click") {
		setDisabled("tpControleCarga", false);
		inicializaCampos();
		loadDataUsuario();
	}
	setFocusOnFirstFocusableField();
}

function consultar_OnClick(form) {
	controleCargasGridDef.resetGrid();
	var sdo = createServiceDataObject("lms.carregamento.consultarControleCargasAction.validatePaginatedControleCarga", 
		"retornoConsultar", buildFormBeanFromForm(form, 'LIKE_END'));
	xmit({serviceDataObjects:[sdo]});
}

function retornoConsultar_cb(data, error) {
	if (error != undefined) {
		alert(error);
		if (getElementValue("dtGeracaoInicial") == "" || getElementValue("dtGeracaoFinal") == "") {
			if (getElementValue("dtGeracaoInicial") == "")
				setFocus(document.getElementById("dtGeracaoInicial"));
			else
				setFocus(document.getElementById("dtGeracaoFinal"));
		}
		else
			setFocus(document.getElementById("filialByIdFilialOrigem.sgFilial"));

		return false;
	}
	findButtonScript('controleCargas', document.forms[0]);
}


function controleCargas_OnClick(id) {
	desabilitaTabs(false);
	return true;
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined){
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}
		setDisabled("botaoConsultar", false);
		loadDataUsuario();
	}
}

function loadDataUsuario() {
	var sdo = createServiceDataObject("lms.carregamento.consultarControleCargasAction.getDataUsuario", "resultado_loadDataUsuario");
  	xmit({serviceDataObjects:[sdo]});
}


function resultado_loadDataUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("idFilialUsuario", getNestedBeanPropertyValue(data,"filial.idFilial"));
	setElementValue("sgFilialUsuario", getNestedBeanPropertyValue(data,"filial.sgFilial"));
	setElementValue("nmFantasiaFilialUsuario", getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
	
	//Jira LMS-4170
	if (getElementValue("notaCredito.idNotaCredito") != "") {
		resetValue("dtGeracaoInicial");
		resetValue("dtGeracaoFinal");
		setFocus(document.getElementById("notaCredito.filial"));
	} else {
		setElementValue("dtGeracaoInicial", setFormat(document.getElementById("dtGeracaoInicial"), getNestedBeanPropertyValue(data, "dtInicial")));
		setElementValue("dtGeracaoFinal", setFormat(document.getElementById("dtGeracaoFinal"), getNestedBeanPropertyValue(data, "dtAtual")));
	}

	//Caso seja uma consulta externa, deixa os campos de dataIni/Fim vazios (CQ00028494)
	if (getElementValue("nrControleCarga") != "") {
		verificaSiglaNumeroPreenchidos();
}

}


function inicializaCampos() {
	setDisabled("rotaIdaVolta.idRotaIdaVolta", false);
	setDisabled("tpRotaViagem", false);
	setDisabled("filialByIdFilialOrigem.idFilial", false);
	setDisabled("filialByIdFilialDestino.idFilial", false);
	setDisabled("hrPrevisaoSaidaInicial", false);
	setDisabled("hrPrevisaoSaidaFinal", false);
	desabilitaSolicitacaoContratacao();
	desabilitaNotaCredito();
	setDisabled("rotaColetaEntrega.idRotaColetaEntrega", false);
	resetValue("tpRotaViagem");
	resetValue("rotaIdaVolta.idRotaIdaVolta");
	resetValue("filialByIdFilialDestino.idFilial");
	resetValue("hrPrevisaoSaidaInicial");
	resetValue("hrPrevisaoSaidaFinal");
	resetValue("solicitacaoContratacao.filial.idFilial");
	resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
	resetValue("rotaColetaEntrega.idRotaColetaEntrega");
}

function tpControleCarga_OnChange(combo) {
	inicializaCampos();
	if (getElementValue('tpControleCarga') == "C") {
		setDisabled("rotaIdaVolta.idRotaIdaVolta", true);
		setDisabled("tpRotaViagem", true);
		setDisabled("filialByIdFilialDestino.idFilial", true);
		setDisabled("hrPrevisaoSaidaInicial", true);
		setDisabled("hrPrevisaoSaidaFinal", true);
		setDisabled("solicitacaoContratacao.filial.idFilial", true);
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", true);
	}
	else
		if (getElementValue('tpControleCarga') == "V") {
			setDisabled("rotaColetaEntrega.idRotaColetaEntrega", true);
		}
	return comboboxChange({e:combo});
}


/************************************ INICIO - TABS ************************************/
function desabilitaTabs(valor) {
	var tabGroup = getTabGroup(this.document);
	if (tabGroup != null) {
		tabGroup.setDisabledTab("cad", valor);
		if (valor == true) {
			tabGroup.setDisabledTab("trechos", true);
			tabGroup.setDisabledTab("pontosParada", true);
			tabGroup.setDisabledTab("postosPassagem", true);
			//tabGroup.setDisabledTab("adiantamentos", true);
			tabGroup.setDisabledTab("adiantamentoTrecho", true);
			tabGroup.setDisabledTab("trechoCorporativo", true);
		}
	}
}
/************************************ FIM - TABS ************************************/






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

function desabilitaSolicitacaoContratacao() {
	if (getElementValue("solicitacaoContratacao.filial.sgFilial") != "") {
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", false);
	}
	else {
		setDisabled("solicitacaoContratacao.filial.idFilial", false);
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", false);
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
	}
}
/************************************ FIM - SOLICITACAO CONTRATACAO ************************************/




/************************************ INICIO - NOTA CREDITO ************************************/
function notaCreditoFilial_OnChange() {
	var r = notaCredito_filial_sgFilialOnChangeHandler();
	if (getElementValue('notaCredito.filial.sgFilial') == "") {
		resetValue('notaCredito.idNotaCredito');
		resetValue('notaCredito.filial.idFilial');
		setDisabled("notaCredito.idNotaCredito", false);
		setDisabled("notaCredito.nrNotaCredito", true);
	}
	else
		setDisabled('notaCredito.idNotaCredito', false);
	return r;
}

function retornoFilialNotaCredito_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = notaCredito_filial_sgFilial_exactMatch_cb(data);
	if (r == true) {
		setDisabled('notaCredito.idNotaCredito', false);
		setFocus(document.getElementById("notaCredito.nrNotaCredito"));
	}
	return r;
}

//LMS-4170
function retornoNrNotaCredito_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = notaCredito_nrNotaCredito_exactMatch_cb(data);
	if (r == true) {
		setDisabled('notaCredito.nrNotaCredito', false);
		resetValue("dtGeracaoInicial");
		resetValue("dtGeracaoFinal");
		setFocus(document.getElementById("meioTransporteByIdTransportado2.nrFrota"));
	}
	return r;
}

function popupNotaCredito(data) {
	setDisabled("notaCredito.nrNotaCredito", false);
}

function desabilitaNotaCredito() {
	if (getElementValue("notaCredito.filial.sgFilial") != "" || getElementValue("notaCredito.nrNotaCredito") != "") {
		setDisabled("notaCredito.idNotaCredito", false);
	}
	else {
		setDisabled("notaCredito.idNotaCredito", false);
		setDisabled("notaCredito.filial.idFilial", false);
		setDisabled("notaCredito.idNotaCredito", false);
		setDisabled("notaCredito.nrNotaCredito", true);
	}
}
/************************************ FIM - NOTA CREDITO ************************************/



function rotaViagemEventual_OnChange() {
	var r = rota_dsRotaOnChangeHandler();
	if (getElementValue('rota.dsRota') == "") {
		resetValue("tpControleCarga");
		resetValue("tpRotaViagem");
		setDisabled("tpControleCarga", false);
		setDisabled("tpRotaViagem", false);
	}
	else {
		setElementValue("tpControleCarga", "V");
		setElementValue("tpRotaViagem", "EV");
		setDisabled('tpControleCarga', true);
		setDisabled('tpRotaViagem', true);
	}
	return r;
}


function retornoFilialOrigem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	if (r == true) {
		verificaSiglaNumeroPreenchidos();
	}
	return r;
}


function popupFilialOrigem(data) {
	verificaSiglaNumeroPreenchidos();
}

function verificaSiglaNumeroPreenchidos() {
	if ((getElementValue("filialByIdFilialOrigem.idFilial") != "" && getElementValue("nrControleCarga") != "") || getElementValue("nrControleCarga") != "") {
		resetValue("dtGeracaoInicial");
		resetValue("dtGeracaoFinal");
	}
}

function nrControleCarga_OnChange() {
	if (getElementValue("nrControleCarga") != "") {
		verificaSiglaNumeroPreenchidos();
	}
	return true;
}

</script>