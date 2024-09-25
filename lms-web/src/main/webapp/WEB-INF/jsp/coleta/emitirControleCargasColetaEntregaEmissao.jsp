<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirControleCargasColetaEntrega" onPageLoadCallBack="retornoCarregarPagina"
			 service="lms.coleta.emitirControleCargasColetaEntregaAction" >

	<adsm:i18nLabels>
		<adsm:include key="LMS-05354"/>
	</adsm:i18nLabels>

	<adsm:form action="/coleta/emitirControleCargasColetaEntrega" idProperty="idControleCarga" >
	
		<adsm:hidden property="blEmissao" serializable="true" />
		<adsm:hidden property="blStore" serializable="true" />
		<adsm:hidden property="blInformaKmPortaria" serializable="false" />
		<adsm:hidden property="blQuilometragemInformadoManualmente" />
		
		<adsm:hidden property="veiculoInformadoManualmente" value="false" serializable="true"/>
		<adsm:hidden property="semiReboqueInformadoManualmente" value="false" serializable="true"/>
		<adsm:hidden property="motoristaInformadoManualmente" value="false" serializable="true"/>
		
		<adsm:hidden property="controleCargaConcatenado" serializable="false"/>
		<adsm:hidden property="idEquipe" serializable="true" />
		<adsm:hidden property="idEquipeOperacao" serializable="true" />
		
		<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" serializable="false" value="46" />
		<adsm:hidden property="tpMeioTransporte" serializable="false" value="R" />
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="blEntregaDireta" serializable="false" />
		<adsm:hidden property="idTipoTabelaColetaEntrega" serializable="false" />

		<adsm:hidden property="idFilialUsuario" serializable="false" />
		<adsm:hidden property="sgFilialUsuario" serializable="false" />
		<adsm:hidden property="nmFantasiaFilialUsuario" serializable="false" />
		
		<adsm:hidden property="idReciboPostoPassagem" serializable="false" />

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>


		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:textbox label="filial" property="filialUsuario.sgFilial" dataType="text" size="3" 
					  labelWidth="21%" width="79%" disabled="true" serializable="false" >
			<adsm:textbox property="filialUsuario.pessoa.nmFantasia" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:lookup dataType="text" label="controleCargas"
					 property="controleCarga.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupFilialByControleCarga" 
					 onDataLoadCallBack="retornoFilialControleCarga"
					 onchange="return controleCargaFilial_OnChange()"
					 action="/municipios/manterFiliais" 
					 popupLabel="pesquisarFilial" 
					 size="3" maxLength="3" labelWidth="21%" width="79%" picker="false" serializable="false" >
			<adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="flagBuscaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="flagDesabilitaEmpresaUsuarioLogado" criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" />
	 		<adsm:lookup dataType="integer" property="controleCarga" 
	 					 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" 
						 onDataLoadCallBack="retornoControleCarga"
						 onPopupSetValue="popupControleCarga"
						 onchange="return controleCarga_OnChange()"
						 popupLabel="pesquisarControleCarga"
						 size="9" maxLength="8" mask="00000000" disabled="true" serializable="true" >
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="true" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="true" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" disable="false"/>
 				 <adsm:propertyMapping modelProperty="tpControleCarga" criteriaProperty="tpControleCarga" disable="true" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="tpControleCarga" value="C" serializable="false" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />


		<adsm:textbox label="acao" property="acao" dataType="text" labelWidth="21%" width="34%" disabled="true" serializable="false"/>
		<adsm:textbox label="status" property="tpStatusControleCarga" dataType="text" size="31" labelWidth="12%" width="33%" disabled="true" serializable="false"/>

		<adsm:hidden property="rotaColetaEntrega.idRotaColetaEntrega" />
		<adsm:textbox label="rotaColetaEntrega" property="rotaColetaEntrega.nrRota" dataType="integer" size="4" 
					  labelWidth="21%" width="79%" disabled="true" serializable="false" >
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:textbox>


		<adsm:hidden property="tpVinculo" serializable="false" />

		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoFrota"
					 onchange="return meioTransporteByIdTransportado2_OnChange()"
					 picker="false" label="meioTransporte" labelWidth="21%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportado.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportado.nrIdentificador" />
			<adsm:propertyMapping modelProperty="tpVinculo.value" relatedProperty="tpVinculo" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoPlaca"
					 onPopupSetValue="popupMeioTransporteTransportado"
					 onchange="return meioTransporteByIdTransportado_OnChange()"
					 picker="true" maxLength="25" width="27%" size="16" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="tpVinculo.value" relatedProperty="tpVinculo" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocado2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoFrota"
					 picker="false" label="semiReboque" labelWidth="12%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdSemiRebocado.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdSemiRebocado.nrIdentificador" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocado" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoPlaca"
					 onPopupSetValue="popupMeioTransporteSemiRebocado"
					 picker="true" maxLength="25" width="26%" size="16" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdSemiRebocado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdSemiRebocado2.nrFrota" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>

		<adsm:hidden property="proprietario.idProprietario" serializable="true" />
		<adsm:textbox label="proprietario" property="proprietario.pessoa.nrIdentificacaoFormatado" dataType="text" size="20" 
					  labelWidth="21%" width="79%" disabled="true" serializable="false" >
			<adsm:textbox property="proprietario.pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="21%" width="79%"
					 property="motorista" 
					 idProperty="idMotorista"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 onPopupSetValue="validarMotorista"
					 onDataLoadCallBack="validarMotorista"
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" serializable="true" 
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupMotorista" 
					 >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motorista.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="motorista.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="idMotorista" relatedProperty="motorista.idMotorista" />
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:textbox label="ociosidadeVisual" property="pcOciosidadeVisual" dataType="integer" size="4" 
					  labelWidth="21%" width="79%" maxLength="3" minValue="0" maxValue="100" />

		<adsm:combobox property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tabelaColetaEntrega"
					   service="" 
					   optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" 
					   labelWidth="21%" width="79%" autoLoad="false" >
			<adsm:propertyMapping modelProperty="idMeioTransporte" criteriaProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="idTabelaColetaEntrega" relatedProperty="tabelaColetaEntrega.idTabelaColetaEntrega" />
		</adsm:combobox>
		<adsm:hidden property="tabelaColetaEntrega.idTabelaColetaEntrega" serializable="true" />

		<adsm:textbox label="quilometragemSaida" property="nrQuilometragem" dataType="decimal" mask="###,###" size="8" maxLength="6" labelWidth="21%" width="34%"/>
		
		<adsm:checkbox label="virouHodometro" property="blVirouHodometro" labelWidth="12%" width="10%" />


        <adsm:textarea label="observacaoQuilometragem" property="obControleQuilometragem" maxLength="200" 
        			   rows="3" columns="57" labelWidth="21%" width="79%"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="21%" width="79%"
		               property="tpFormatoRelatorio"
		               domain="DM_FORMATO_RELATORIO"
		               renderOptions="true" defaultValue="pdf" />	
		
		<adsm:buttonBar>
			<adsm:button buttonType="button" id="btnMdfe" caption="monitorarMdfe" onclick="openMdfe()" />
			<adsm:button id="emitirButton" caption="emitir" buttonType="reportViewerButton" onclick="imprimeRelatorio(this.form);" disabled="false" />
			<adsm:button id="storeButton" caption="salvar" onclick="salvar_onClick(this.form);"/>
			<adsm:button id="resetButton" caption="novo" onclick="novo_onClick()" disabled="false" />
		</adsm:buttonBar>
		
		<script>
			var lms_05009 = '<adsm:label key="LMS-05009"/>';
			var lms_05147 = "<adsm:label key='LMS-05147'/>";
			var LMS_02021 = '<adsm:label key="LMS-02021"/>';
		</script>
		
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/mdfe.js"></script>

<script type="text/javascript">
var mdfe = inicializaMdfe();

function inicializaMdfe() {
	var mdfe = new Mdfe();
	
	var action = "lms.coleta.emitirControleCargasColetaEntregaAction";
	
	mdfe.urlGerarMdfe = action+ ".generatePreEmissaoControleCarga";
	
	mdfe.gerarMdfeCallId = "resultadoSalvarEmitir";
	
	mdfe.urVerificaAutorizacaoMdfe = action+ ".verificaAutorizacaoMdfe";
	mdfe.verificaAutorizacaoMdfeCallId = "verificaAutorizacaoMdfe";
	
	mdfe.urlEncerrarMdfesAutorizados = action+ ".encerrarMdfesAutorizados";
	mdfe.encerrarMdfesAutorizadosCallId = "encerrarMdfesAutorizados";
	
	mdfe.urlVerificaEncerramentoMdfe = action+ ".verificaEncerramentoMdfe";
	mdfe.verificaEncerramentoMdfeCallId = "verificaEncerramentoMdfe";

	mdfe.urlImprimirMdfe = action+ ".imprimirMDFe";
	
	mdfe.chaveMensagemFinal = "LMS-05354";
	
	return mdfe;
	
}

function initWindow(eventObj) {
	setDisabled("btnMdfe", true);
	if (eventObj.name == 'tab_load') {
		setDisabled("controleCarga.idControleCarga", false);
		setDisabled("controleCarga.filialByIdFilialOrigem.sgFilial", true);
		desabilitaCampos(true);
		setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
		setDisabled("storeButton", true);
		setDisabled("emitirButton", true);
		getDadosUrl();
	} else if (eventObj.name == "tab_click") {
		verificaAcao();
		verificaCamposPreenchidos();
	   	if (getElementValue("meioTransporteByIdTransportado.idMeioTransporte") == "") {
	   		desabilitaTab("postos", true);
	}
	}
	setFocusOnFirstFocusableField();
	if(eventObj.name == "tab_load" || eventObj.name == 'cleanButton_click'){
		loadDadosSessao();
	}
}

function validarMotorista_cb(data) {
	var r = motorista_pessoa_nrIdentificacao_exactMatch_cb(data);
	retornoMotorista_cb(getNestedBeanPropertyValue(data[0],"idMotorista"));
	return r;
}
function validarMotorista(data) {
	retornoMotorista_cb(getNestedBeanPropertyValue(data,"idMotorista"));
}

function retornoMotorista_cb(idMotora) {
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.validateCNHMotorista", "retornoMotorista_cb", 
			{idMotorista:idMotora});
    xmit({serviceDataObjects:[sdo]});
}

function retornoMotorista_cb_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetaMotorista();
		setFocus(document.getElementById('motorista.pessoa.nrIdentificacao'));
	}
}


function resetaMotorista() {
	resetValue("motorista.idMotorista");
	resetValue("motorista.pessoa.nrIdentificacao");
	resetValue("motorista.pessoa.nmPessoa");
}

//Chama o servico que retorna os dados do usuario logado 
function loadDadosSessao(){
	var data = new Array();
	var sdo = createServiceDataObject("lms.coleta.executarRetornarColetasEntregasAction.findDadosSessao",
				"preencheDadosSessao",data);
	xmit({serviceDataObjects:[sdo]});
}

//Funcao de callback do servico que retorna os dados do usuario logado. 
function preencheDadosSessao_cb(data, exception){
	if (exception == null){
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
		setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));
	}
}


function retornoCarregarPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	setDisabled("resetButton", false);
	if (error == undefined) {
		if (getElementValue("controleCarga.idControleCarga") != "") {
			findDataControleCarga(getElementValue('controleCarga.idControleCarga'));
		}
		else {
			var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.newMaster");
			xmit({serviceDataObjects:[sdo]});
		}
	}
}

function getDadosUrl() {
	var url = new URL(parent.location.href);
	var recarrega = url.parameters["recarrega"];
	if (recarrega != undefined && recarrega == "true") {
		var idControleCarga = url.parameters["idControleCarga"];
		var nrControleCarga = url.parameters["nrControleCarga"];
		var idFilial = url.parameters["idFilial"];
		var sgFilial = url.parameters["sgFilial"];
		povoaDadosRecarregaTela(idControleCarga, nrControleCarga, idFilial, sgFilial);
	}
	else {
		var idControleCarga = url.parameters["idControleCarga"];
		if (idControleCarga != undefined && idControleCarga != "") {
			var nrControleCarga = url.parameters["nrControleCarga"];
			var idFilialOrigem = url.parameters["idFilialOrigem"];
			var sgFilialOrigem = url.parameters["sgFilialOrigem"];
			povoaDadosRecarregaTela(idControleCarga, nrControleCarga, idFilialOrigem, sgFilialOrigem);
		}
	}
}

function povoaDadosRecarregaTela(idControleCarga, nrControleCarga, idFilial, sgFilial) {
	setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
	setElementValue("controleCarga.idControleCarga", idControleCarga);
	setElementValue("controleCarga.nrControleCarga", getFormattedValue("integer", nrControleCarga, "00000000", true));
	setDisabled("controleCarga.nrControleCarga", false);
	findDataControleCarga(idControleCarga);
}

function desabilitaCampos(valor) {
	setDisabled("meioTransporteByIdTransportado2.idMeioTransporte", valor);
	setDisabled("meioTransporteByIdTransportado.idMeioTransporte", valor);
	setDisabled("meioTransporteByIdSemiRebocado2.idMeioTransporte", valor);
	setDisabled("meioTransporteByIdSemiRebocado.idMeioTransporte", valor);
	setDisabled("motorista.idMotorista", valor);
	setDisabled("pcOciosidadeVisual", valor);
	setDisabled("nrQuilometragem", valor);
	setDisabled("blVirouHodometro", valor);
	setDisabled("obControleQuilometragem", valor);
}


function validaPreenchimentoTabelaColetaEntrega() {
	if (document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega").disabled == false) {
		if (getElementValue("blEntregaDireta") == "false" && getElementValue("tabelaColetaEntrega.idTabelaColetaEntrega") == "" && 
			document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').selectedIndex == 0 &&
			getElementValue("meioTransporteByIdTransportado.idMeioTransporte") != "")
		{
			alert(lms_05147);
			setFocus(document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega'));
			return false;
		}
	}
	return true;
}


/**
 * Responsável por habilitar/desabilitar uma tab
 */
function desabilitaTab(aba, disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab(aba, disabled);
}


function verificaNrQuilometragem() {
	if (getElementValue("blEmissao") == "true" && getElementValue("nrQuilometragem") == "") {
		if (getElementValue("blInformaKmPortaria") == "true") {
			setDisabled("nrQuilometragem", true);
			setDisabled("blVirouHodometro", true);
			setDisabled("obControleQuilometragem", true);
			document.getElementById("nrQuilometragem").required = "false";
		}
		else {
			setDisabled("nrQuilometragem", false);
			setDisabled("blVirouHodometro", false);
			setDisabled("obControleQuilometragem", false);
			document.getElementById("nrQuilometragem").required = "true";
		}
	}
	else {
		setDisabled("nrQuilometragem", true);
		setDisabled("blVirouHodometro", true);
		setDisabled("obControleQuilometragem", true);
		document.getElementById("nrQuilometragem").required = "false";
	}
}


function verificaCamposPreenchidos() {
	if (getElementValue("meioTransporteByIdTransportado.idMeioTransporte") == "") {
		setDisabled("meioTransporteByIdTransportado2.idMeioTransporte", false);
		setDisabled("meioTransporteByIdTransportado.idMeioTransporte", false);
		setElementValue("veiculoInformadoManualmente", "true");
		document.getElementById("meioTransporteByIdTransportado2.idMeioTransporte").required = "true";
		
		setDisabled("meioTransporteByIdSemiRebocado2.idMeioTransporte", true);
		setDisabled("meioTransporteByIdSemiRebocado.idMeioTransporte", true);
	}
	else {
		setElementValue("veiculoInformadoManualmente", "false");
		document.getElementById("meioTransporteByIdTransportado2.idMeioTransporte").required = "false";
	}
	
	if (getElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte") == "")
		setElementValue("semiReboqueInformadoManualmente", "true");
	else
		setElementValue("semiReboqueInformadoManualmente", "false");
	
	if (getElementValue("motorista.idMotorista") == "") {
		setDisabled("motorista.idMotorista", false);
		setElementValue("motoristaInformadoManualmente", "true");
	}
	else {
		setDisabled("motorista.idMotorista", true);
		setElementValue("motoristaInformadoManualmente", "false");
	}
	
	if (getElementValue("pcOciosidadeVisual") == "") {
		setDisabled("pcOciosidadeVisual", false);
	}
}





/************************************ INICIO - CONTROLE CARGA ************************************/

function controleCargaFilial_OnChange() {
	var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	if (getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial') != "") {
		setDisabled('controleCarga.idControleCarga', false);
	}
	else {
		inicializaTela();
	}
	return r;
}

function controleCarga_OnChange() {
	var r = controleCarga_nrControleCargaOnChangeHandler();
	if (getElementValue('controleCarga.nrControleCarga') == "") {
		var idFilial = getElementValue('controleCarga.filialByIdFilialOrigem.idFilial');
		var sgFilial = getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial');
		inicializaTela();
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		setDisabled('controleCarga.idControleCarga', false);
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
		var idFilial = getElementValue('controleCarga.filialByIdFilialOrigem.idFilial');
		var sgFilial = getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial');
		inicializaTela();
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);

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
		inicializaControleCarga(data);
	}
	return r;
}


function popupControleCarga(data) {
	if (getNestedBeanPropertyValue(data,"tpControleCarga.value") != "C") {
		alert(lms_05009);
		setFocus(document.getElementById('controleCarga.nrControleCarga'));
		return false;
	}
	inicializaControleCarga(data);
}


function inicializaControleCarga(data) {
	
	if (data.length != 1 || data[0].btnMdfe != "true") {
		setDisabled("btnMdfe", true);
	} else {
		setDisabled("btnMdfe", false);
	}
	
	var idFilial = getElementValue('controleCarga.filialByIdFilialOrigem.idFilial');
	var sgFilial = getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial');
	var idControleCarga = getElementValue('controleCarga.idControleCarga');
	var nrControleCarga = getElementValue('controleCarga.nrControleCarga');
	inicializaTela(true);
	setDisabled('controleCarga.idControleCarga', false);
	setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
	setElementValue("controleCarga.idControleCarga", idControleCarga);
	setElementValue("controleCarga.nrControleCarga", getFormattedValue("integer",  nrControleCarga, "00000000", true));
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.newMaster", "retornoNewMaster");
	xmit({serviceDataObjects:[sdo]});
}

function retornoNewMaster_cb(data, error) {
	findDataControleCarga(getElementValue('controleCarga.idControleCarga'));
}


function findDataControleCarga(idControleCarga) {
	setElementValue('idControleCarga', idControleCarga);

	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.findDataControleCarga", "resultado_findDataControleCarga", 
			{idControleCarga:idControleCarga});
    xmit({serviceDataObjects:[sdo]});
}

function resultado_findDataControleCarga_cb(data, error) {

	if (error != undefined) {
		alert(error);
		resetValue('controleCarga.idControleCarga');
		setFocus('controleCarga.nrControleCarga');
		return false;
	}
	if (data != undefined) {
	  	setElementValue("acao", getNestedBeanPropertyValue(data,"acao"));
	   	setElementValue("blEmissao", getNestedBeanPropertyValue(data,"blEmissao"));
	   	setElementValue("tpStatusControleCarga", getNestedBeanPropertyValue(data,"tpStatusControleCarga"));
	   	setElementValue("meioTransporteByIdTransportado.idMeioTransporte", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.idMeioTransporte"));
	   	setElementValue("meioTransporteByIdTransportado2.idMeioTransporte", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.idMeioTransporte"));
		setElementValue("meioTransporteByIdTransportado2.nrFrota", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrFrota"));
		setElementValue("meioTransporteByIdTransportado.nrIdentificador", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrIdentificador"));
		setElementValue("tpVinculo", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.tpVinculo"));
		setElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte", getNestedBeanPropertyValue(data,"meioTransporteByIdSemiRebocado.idMeioTransporte"));
		setElementValue("meioTransporteByIdSemiRebocado2.idMeioTransporte", getNestedBeanPropertyValue(data,"meioTransporteByIdSemiRebocado.idMeioTransporte"));
		setElementValue("meioTransporteByIdSemiRebocado2.nrFrota", getNestedBeanPropertyValue(data,"meioTransporteByIdSemiRebocado.nrFrota"));
		setElementValue("meioTransporteByIdSemiRebocado.nrIdentificador", getNestedBeanPropertyValue(data,"meioTransporteByIdSemiRebocado.nrIdentificador"));
		setElementValue("rotaColetaEntrega.idRotaColetaEntrega", getNestedBeanPropertyValue(data,"rotaColetaEntrega.idRotaColetaEntrega"));
		setElementValue("rotaColetaEntrega.nrRota", getNestedBeanPropertyValue(data,"rotaColetaEntrega.nrRota"));
		setElementValue("rotaColetaEntrega.dsRota", getNestedBeanPropertyValue(data,"rotaColetaEntrega.dsRota"));
		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		setElementValue("proprietario.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietario.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
		setElementValue("motorista.idMotorista", getNestedBeanPropertyValue(data,"motorista.idMotorista"));
		setElementValue("motorista.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data,"motorista.pessoa.nrIdentificacaoFormatado"));
		setElementValue("motorista.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"motorista.pessoa.nmPessoa"));
	   	setElementValue("pcOciosidadeVisual", setFormat(document.getElementById("pcOciosidadeVisual"), getNestedBeanPropertyValue(data,"pcOciosidadeVisual")) );
	   	setElementValue("nrQuilometragem", getNestedBeanPropertyValue(data,"nrQuilometragem"));
	   	setElementValue("blInformaKmPortaria", getNestedBeanPropertyValue(data,"blInformaKmPortaria"));
		setElementValue("filialUsuario.idFilial", getNestedBeanPropertyValue(data, "filialUsuario.idFilial"));
	   	setElementValue("filialUsuario.sgFilial", getNestedBeanPropertyValue(data, "filialUsuario.sgFilial"));
	   	setElementValue("filialUsuario.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filialUsuario.pessoa.nmFantasia"));
	   	setElementValue("idEquipeOperacao", getNestedBeanPropertyValue(data,"idEquipeOperacao"));
	   	setElementValue("idEquipe", getNestedBeanPropertyValue(data,"idEquipe"));
	   	setElementValue("blEntregaDireta", getNestedBeanPropertyValue(data,"blEntregaDireta"));
	   	var nrControleCarga = getFormattedValue("integer",  getElementValue("controleCarga.nrControleCarga"), "00000000", true);
	   	setElementValue("controleCargaConcatenado", getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") + " " + nrControleCarga);

	   	if (getElementValue("nrQuilometragem") != ""){
	   		setElementValue("blQuilometragemInformadoManualmente", "false");
	   	}else{
		   	setElementValue("blQuilometragemInformadoManualmente", "true");
	   	}

		var tabelas = getNestedBeanPropertyValue(data, "tabelas");
		var isTpCalculo2 = checkIsTpCalculo2(tabelas);
		
		if(isTpCalculo2){
			tabelas = [{idTipoTabelaColetaEntrega:tabelas[0].idTipoTabelaColetaEntrega,
						dsTipoTabelaColetaEntrega:"Cálculo 2",
						tpCalculo : 'C2',
						idTabelaColetaEntrega:tabelas[0].idTabelaColetaEntrega}];
			setElementValue("tabelaColetaEntrega.idTabelaColetaEntrega", getNestedBeanPropertyValue(data, "tabelas:0:idTabelaColetaEntrega"));
		}		

		tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb( tabelas );
		//alert(tabelas.length);
		//if(tabelas.length == 0 ){
			//resetaComboTipoTabela();
		//}else{
			//setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
		//}
		if(!isTpCalculo2){
		setElementValue("idTipoTabelaColetaEntrega", getNestedBeanPropertyValue(data,"tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
		setElementValue("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", getElementValue("idTipoTabelaColetaEntrega"));
		setElementValue("tabelaColetaEntrega.idTabelaColetaEntrega", getNestedBeanPropertyValue(data,"tabelaColetaEntrega.idTabelaColetaEntrega"));
		}

		if (getElementValue("tabelaColetaEntrega.idTabelaColetaEntrega") != "" && getElementValue("idTipoTabelaColetaEntrega") == "") {
			if (document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].text == "") {
				document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
			}
		}
		setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);

		if (isTpCalculo2) {
			document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
			setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
			if(!isTpCalculo2){
				setElementValue("tabelaColetaEntrega.idTabelaColetaEntrega", getNestedBeanPropertyValue(data, "tabelas:0:idTabelaColetaEntrega"));
			}
		} else if (getElementValue("blEmissao") == "true" && getElementValue('blEntregaDireta') == "false") {
			if (getElementValue("tabelaColetaEntrega.idTabelaColetaEntrega") == "" && getElementValue("meioTransporteByIdTransportado.idMeioTransporte") != "") {
				/*
				setElementValue("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", getNestedBeanPropertyValue(data,"idTipoTabelaColetaEntrega_blNormal"));
				comboboxChange({e:document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega")});
				setElementValue("tabelaColetaEntrega.idTabelaColetaEntrega", getNestedBeanPropertyValue(data, "tabelas:0:idTabelaColetaEntrega"));
				if(document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1]){
				document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true;
			}
				*/
				setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", false);
		}
		}

	   	verificaAcao();
	   	verificaCamposPreenchidos();
	   	verificaNrQuilometragem();
	   	
	   	if (getElementValue("meioTransporteByIdTransportado.idMeioTransporte") == "") {
	   		desabilitaTab("postos", true);
	   	}
	   	
	    var formEquipe = getTabGroup(this.document).getTab("equipe").tabOwnerFrame.window.document.forms[0]; 
	    if (formEquipe != undefined) {
			formEquipe.idEquipeOperacao.value = getElementValue("idEquipeOperacao");
			formEquipe.idEquipe.value = getElementValue("idEquipe");
		}

	   	setFocusOnFirstFocusableField();
	   	
		if (data.btnMdfe != "true") {
			setDisabled("btnMdfe", true);
		} else {
			setDisabled("btnMdfe", false);
		}
	
		if("PA" == getNestedBeanPropertyValue(data,"tpCalculoTabelas")){						
			document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
		}

	}
}

function verificaAcao() {
	// Reemissão
	if (getElementValue("blEmissao") == "false") {
		setDisabled("storeButton", true);
		setDisabled("emitirButton", false);
		desabilitaCampos(true);
	}
	// Emissão
	else {
		desabilitaCampos(true);
		setDisabled("storeButton", false);
		setDisabled("emitirButton", false);
	}
	desabilitaTabs(false);
}

/************************************ FIM - CONTROLE CARGA ************************************/






/************************************ INICIO - VEICULO ************************************/
function retornoMeioTransportadoFrota_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdTransportado2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		findDadosVeiculo(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function retornoMeioTransportadoPlaca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue('meioTransporteByIdTransportado2.idMeioTransporte');
		resetValue('meioTransporteByIdTransportado.idMeioTransporte');
		return false;
	}
	var r = meioTransporteByIdTransportado_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		findDadosVeiculo(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function popupMeioTransporteTransportado(data) {
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.validateMeioTransporte", "popupMeioTransporteTransportado", 
			{idMeioTransporte:getNestedBeanPropertyValue(data,"idMeioTransporte"),
			tpVinculo:getNestedBeanPropertyValue(data,"tpVinculo"),
			tipoMeioTransporte:"transportado"});
    xmit({serviceDataObjects:[sdo]});
}

function popupMeioTransporteTransportado_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdTransportado2.idMeioTransporte');
		resetValue('meioTransporteByIdTransportado.idMeioTransporte');
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdTransportado2.nrFrota'));
		return false;
	}
	findDadosVeiculo(getElementValue('meioTransporteByIdTransportado.idMeioTransporte'));
}

function findDadosVeiculo(idMeioTransporte) {
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.findDadosVeiculo", "retornoFindDadosVeiculo", 
			{idMeioTransporteTransportado:idMeioTransporte,
			idMeioTransporteSemiRebocado:getElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte"),
			idControleCarga:getElementValue("controleCarga.idControleCarga"),
			idRotaColetaEntrega:getElementValue("rotaColetaEntrega.idRotaColetaEntrega")});
    xmit({serviceDataObjects:[sdo]});
}

function checkIsTpCalculo2(tabelas){
	return tabelas.length > 0 && tabelas[0].tpCalculo == 'C2';
}

function retornoFindDadosVeiculo_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdTransportado2.idMeioTransporte');
		resetValue('meioTransporteByIdTransportado.idMeioTransporte');
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdTransportado2.nrFrota'));
		return false;
	}
	if (data != undefined) {
		var tabelas = getNestedBeanPropertyValue(data, "tabelas");
		var isTpCalculo2 = checkIsTpCalculo2(tabelas);
		if(isTpCalculo2){
			tabelas = [{idTipoTabelaColetaEntrega:tabelas[0].idTipoTabelaColetaEntrega,
						dsTipoTabelaColetaEntrega:"Cálculo 2",
						tpCalculo : 'C2',
						idTabelaColetaEntrega:tabelas[0].idTabelaColetaEntrega}];
			setElementValue("tabelaColetaEntrega.idTabelaColetaEntrega", getNestedBeanPropertyValue(data, "tabelas:0:idTabelaColetaEntrega"));
		}
		tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb( tabelas );

		setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', false);
		if ( document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').length == 2 ) {
			if (document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].text == "" || isTpCalculo2) {
				document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
				setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
				if(!isTpCalculo2){
					setElementValue("tabelaColetaEntrega.idTabelaColetaEntrega", getNestedBeanPropertyValue(data, "tabelas:0:idTabelaColetaEntrega"));
				}
			}
		}

		if (getElementValue("blEntregaDireta") == "false" && !isTpCalculo2) {
			setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", false);
		}

		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		setElementValue("proprietario.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietario.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
		if (getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != undefined && getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != "") {
			setDisabled("meioTransporteByIdSemiRebocado2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocado.idMeioTransporte", false);
		}
		else {
			setDisabled("meioTransporteByIdSemiRebocado2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocado.idMeioTransporte", true);
		}
		desabilitaTab("postos", false);
	}
}


function meioTransporteByIdTransportado2_OnChange() {
	var r = meioTransporteByIdTransportado2_nrFrotaOnChangeHandler();
	if (getElementValue('meioTransporteByIdTransportado2.nrFrota') == "") {
		onChangeMeioTransportado();
	}
	return r;
}

function meioTransporteByIdTransportado_OnChange() {
	var r = meioTransporteByIdTransportado_nrIdentificadorOnChangeHandler();
	if (getElementValue('meioTransporteByIdTransportado.nrIdentificador') == "") {
		onChangeMeioTransportado();
	}
	return r;
}

function onChangeMeioTransportado() {
	resetaComboTipoTabela();	
	resetaProprietario();
	resetValue("meioTransporteByIdSemiRebocado2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
	setDisabled("meioTransporteByIdSemiRebocado2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocado.idMeioTransporte", true);
	resetaDadosTabPostos();
}

function resetaProprietario() {
	resetValue("proprietario.idProprietario");
	resetValue("proprietario.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietario.pessoa.nmPessoa");
}
/************************************ FIM - VEICULO ************************************/






function resetaDadosTabPostos() {
	resetTabPostos();

	var tabGroup = getTabGroup(this.document);
    var tabDetPostos = tabGroup.getTab("postos");
    tabDetPostos.tabOwnerFrame.window.limpaGrids();
	
	desabilitaTab("postos", true);
}

function resetTabPostos() {
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.resetTabPostos", "retornoPadrao", 
			{idControleCarga:getElementValue("controleCarga.idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoPadrao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
}






/************************************ INICIO - SEMI REBOQUE ************************************/
function retornoMeioSemiRebocadoFrota_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdSemiRebocado2_nrFrota_exactMatch_cb(data);
	return r;
}

function retornoMeioSemiRebocadoPlaca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdSemiRebocado_nrIdentificador_exactMatch_cb(data);
	return r;
}

function popupMeioTransporteSemiRebocado(data) {
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.validateMeioTransporte", "popupMeioTransporteSemiRebocado", 
			{idMeioTransporte:getNestedBeanPropertyValue(data,"idMeioTransporte"),
			tipoMeioTransporte:"semiRebocado"});
    xmit({serviceDataObjects:[sdo]});
}

function popupMeioTransporteSemiRebocado_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdSemiRebocado2.idMeioTransporte');
		resetValue('meioTransporteByIdSemiRebocado.idMeioTransporte');
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdSemiRebocado2.nrFrota'));
		return false;
	}
}
/************************************ FIM - SEMI REBOQUE ************************************/




function validaTela(form, blCamposObrigatorios) {
	document.getElementById("meioTransporteByIdTransportado2.nrFrota").required = blCamposObrigatorios;
	document.getElementById("motorista.pessoa.nrIdentificacao").required = blCamposObrigatorios;
	document.getElementById("pcOciosidadeVisual").required = blCamposObrigatorios;
	document.getElementById("tpFormatoRelatorio").required = blCamposObrigatorios;

	if (!validaPreenchimentoTabelaColetaEntrega())
		return "false";
	
	
	if (!validateForm(form)) {
		return "false";
	}

	var tabGroup = getTabGroup(this.document);
    var tabDetEquipe = tabGroup.getTab("equipe");
    setElementValue('idEquipe', tabDetEquipe.getFormProperty("equipe.idEquipe"));
    setElementValue('idEquipeOperacao', tabDetEquipe.getFormProperty("idEquipeOperacao"));

    var tabDetPostos = tabGroup.getTab("postos");
    var formPagamentos = tabDetPostos.tabOwnerFrame.window.document.forms[1];
    var formPostos = tabDetPostos.tabOwnerFrame.window.document.forms[2];
    
    if (!validateForm(formPostos)) {
		return "false";
	}

    if (!validateForm(formPagamentos)) {
		return "false";
	}

	var data = editGridFormBean(form, formPostos);
	var pagamentos = getNestedBeanPropertyValue(editGridFormBean(form, formPagamentos), "pagamentos");
	if (pagamentos != undefined) {
		setNestedBeanPropertyValue(data, "pagamentos", pagamentos);
	}
	return data;
}


function salvar_onClick(form) {
	setElementValue("blStore", true);
	var data = validaTela(form, "false");
	if (data == "false")
		return false;

    var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.store", "store", data);
    xmit({serviceDataObjects:[sdo]});
}


function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var tabGroup = getTabGroup(this.document);
    var formEquipe = tabGroup.getTab("equipe").tabOwnerFrame.window.document.forms[0]; 
    var idEquipeOperacao = getNestedBeanPropertyValue(data, "idEquipeOperacao");
	if (idEquipeOperacao != undefined && idEquipeOperacao != "") {
		tabGroup.getTab("equipe").tabOwnerFrame.window.desabilitaTela();
		formEquipe.idEquipeOperacao.value = idEquipeOperacao;
		setElementValue("idEquipeOperacao", idEquipeOperacao);
		tabGroup.getTab("equipe").tabOwnerFrame.window.povoaGrid(formEquipe.idEquipe);
	}

	var tabDetPostos = tabGroup.getTab("postos");
	tabDetPostos.tabOwnerFrame.window.postosGridDef.executeLastSearch();

	verificaCamposPreenchidos();
	if (getElementValue("tabelaColetaEntrega.idTabelaColetaEntrega") != "") {
		setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
	}
   	showSuccessMessage();
}


function imprimeRelatorio(form) {
	setElementValue("blStore", false);
	var data = validaTela(form, "true");
	if (data == "false")
		return false;

	mdfe.gerarMdfe(data, false);

}

function resultadoSalvarEmitir_cb(data, error) {
	if (error != undefined) {
		alert(error);
		mdfe.hideMessageMDFe();
		return false;
	}
	var msgBloqueio = data.mensagensEnquadramento; 
	
	var msgWorkflow = '';
	if (data.mensagensWorkflowRegras != undefined && data.mensagensWorkflowRegras != '') {
		msgWorkflow += data.mensagensWorkflowRegras;  
	}
	
	if (data.mensagensWorkflowExigencias != undefined && data.mensagensWorkflowExigencias != '') {
		msgWorkflow += data.mensagensWorkflowExigencias;  
	} 
	var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:310px;';
	var idControleCarga = getElementValue("controleCarga.idControleCarga");
	
	if (msgBloqueio != undefined && msgBloqueio != '') {
		var url = '/carregamento/emitirControleCargas.do?cmd=bloqueios&msgBloqueio=' + escape(msgBloqueio);
		showModalDialog(url, window, wProperties);
		return;
	}
	
	if (msgWorkflow != undefined && msgWorkflow != '') {
		var url = '/carregamento/emitirControleCargas.do?cmd=workflow&idControleCarga=' + idControleCarga + '&msgWorkflow=' + escape(msgWorkflow);
		showModalDialog(url, window, wProperties);
		return;
	}

	if (mdfe.gerarMdfeCallback(data))  {
		finalizarEmissaoControleCarga();
	}

}

function verificaAutorizacaoMdfe_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setDisabled("btnMdfe", false);
		return false;
	}

	
	if (mdfe.verificaAutorizacaoMdfeCallback(data)) {
		finalizarEmissaoControleCarga();
	}
	
}
		
function finalizarEmissaoControleCarga() {
	mdfe.imprimirMDFe();
	executeReportWithCallback('lms.coleta.emitirControleColetaEntregaAction.execute', 'verificaEmissao', document.forms[0]);
}


/**
 * Função para verificar se o relatorio foi impresso, caso seja
 */
function verificaEmissao_cb(strFile, error) {
    openReportWithLocator(strFile, error);

    if (error != undefined) {
        return false;
    }
    
    if (getElementValue('blEmissao') == "true") {
	    var registraEmissao = confirm(LMS_02021);
		if (!registraEmissao)
			return;
	}

    var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("equipe");
    var idEquipeOperacao = tabDet.getFormProperty("idEquipeOperacao");
    setElementValue('idEquipeOperacao', idEquipeOperacao);
    
    var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.generatePosEmissaoControleCarga", 
    	"resultadoEmissao", 
    	{idControleCarga:getElementValue('idControleCarga'),
    	idMeioTransporte:getElementValue('meioTransporteByIdTransportado.idMeioTransporte'),
    	idSemiReboque:getElementValue('meioTransporteByIdSemiRebocado.idMeioTransporte'),
    	idEquipeOperacao:idEquipeOperacao,
    	blEmissao:getElementValue('blEmissao')});
    xmit({serviceDataObjects:[sdo]});
}


function resultadoEmissao_cb(data, error) {
    if (error != undefined) {
        alert(error);
        return false;
	}
	if (getElementValue('blEmissao') == "true") {
   		showSuccessMessage();
   	}
   	var idReciboPostoPassagem = getNestedBeanPropertyValue(data, "idReciboPostoPassagem");
   	setElementValue("idReciboPostoPassagem", idReciboPostoPassagem);
   	if (idReciboPostoPassagem != undefined && idReciboPostoPassagem != "")
   		geraRecibo(idReciboPostoPassagem);
   	else
   		recarregaTela();
}


function geraRecibo(idReciboPostoPassagem) {
	var dataObject = new Object();
	dataObject.idReciboPostoPassagem = idReciboPostoPassagem;
	var sdo = createServiceDataObject('lms.carregamento.emitirReciboPostoPassagemAction',  'openPdfRecibo', dataObject); 
	xmit({serviceDataObjects:[sdo]});
}

function openPdfRecibo_cb(data, error) {
	if (error == null) {
		openReport(data._value, "LMS_REPORT_VIEWER_1");

		var dataObject = new Object();
		dataObject.idReciboPostoPassagem = getElementValue("idReciboPostoPassagem");

		var sdo = createServiceDataObject("lms.carregamento.emitirReciboPostoPassagemAction.updateReciboPostoPassagem", "updateRecibo", dataObject);
	    xmit({serviceDataObjects:[sdo]});
	}
	else {
		alert(error + '');
		recarregaTela();
	}
}
	
function updateRecibo_cb(data, error) {
	if (error != undefined) {
		alert(error);
	}
	recarregaTela();
}


function recarregaTela() {
	if (getElementValue("blEmissao") == "false")
		return;
	var parametros = 
		'&recarrega=true' +
		'&idFilial=' + getElementValue("controleCarga.filialByIdFilialOrigem.idFilial") +
		'&sgFilial=' + getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") +
		'&idControleCarga=' + getElementValue("controleCarga.idControleCarga") +
		'&nrControleCarga=' + getElementValue("controleCarga.nrControleCarga");
	var tabGroup = getTabGroup(this.document);
	tabGroup.document.parentWindow.navigate(window.location.pathname + "?cmd=main" + parametros);
}


function inicializaTela(blIgnoraChamadaNewMaster) {
	setDisabled("controleCarga.idControleCarga", false);
	setDisabled("controleCarga.filialByIdFilialOrigem.sgFilial", true);

	resetaComboTipoTabela();
	resetValue(this.document);
	desabilitaCampos(true);

	setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
	setDisabled("storeButton", true);
	setDisabled("emitirButton", true);

    var tabDetEquipe = getTabGroup(this.document).getTab("equipe");
    tabDetEquipe.tabOwnerFrame.window.inicializaTela();

	desabilitaTabs(true);

	if (blIgnoraChamadaNewMaster == undefined) {
		var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.newMaster");
		xmit({serviceDataObjects:[sdo]});
	}
	loadDadosSessao();
}


function desabilitaTabs(valor) {
	desabilitaTab("equipe", valor);
	desabilitaTab("postos", valor);
	desabilitaTab("riscos", valor);
}


function novo_onClick() {
	inicializaTela();
	setFocusOnFirstFocusableField();
}

function resetaComboTipoTabela() {
	tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(new Array());
	setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
}

function verificaAutorizacaoMdfe_cb(data, error) {
	
	if (error) {
		alert(error);
		mdfe.hideMessageMDFe();
		return false;
	}
	
	if (mdfe.verificaAutorizacaoMdfeCallback(data)) {
		finalizarEmissaoControleCarga();
	}
	
}
		
function encerrarMdfesAutorizados_cb(data, error) {
	if (error) {
		alert(error);
		return false;
	}
	
	mdfe.verificaEncerramentoMdfe(data);
	
}

function verificaEncerramentoMdfe_cb(data, error) {
	if (error) {
		mdfe.hideMessageMDFe();
		alert(error);
		return false;
	}
	
	if (mdfe.verificaEncerramentoMdfeCallback(data)) {
		imprimeRelatorio(document.forms[0]);
	}
	
}


function openMdfe(){
	janela_closed = false;
	janela = showModalDialog('/carregamento/mdfeControleCargasPesq.do?cmd=pesq',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:640px;dialogHeight:400px;');
	janela_closed = true;
	setFocusOnFirstFocusableField();
}

</script>