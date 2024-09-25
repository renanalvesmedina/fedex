<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function initWindow(eventObj){

	if (eventObj.name == "tab_click" || eventObj.name == "cleanButton_click"){
		findMunicipiosComRestricaoTransporte();
	}	
	
	if(getElementValue("idTabelaPreco") != ''){
		controlarCamposByIdTabela();
	} else {
		controlarCampos();
	}
}

function controlarCamposByIdTabela(){
	setDisabled("dtVigenciaInicialSolicitada", true);
	setDisabled("dtVigenciaFinalSolicitada", true);
	setDisabled("btnHistoricoWK", true);
	setDisabled("btnRemove", true);	
}

function controlarCampos(){
	setDisabled("btnRemove", true);
	
	setDisabled("dtVigenciaInicial", true);
	setDisabled("dtVigenciaFinal", true);	

	if(getElementValue("tpSituacaoAprovacao") == "E" || getElementValue("possuiFilhoEmAprovacao") == "true" || isVisualizacaoWK()){
		setDisabled("dtVigenciaInicialSolicitada", true);
		setDisabled("dtVigenciaFinalSolicitada", true);
		setDisabled("municipioCobraTRT", true);
		setDisabled("municipioCobraTRT_municipio", true);
		setDisabled("municipioCobraTRT_blCobraTrt", true);
		setDisabled("btnStore", true);

		if(isVisualizacaoWK()){
			setDisabled("btnReset", true);
			setDisabled("btnHistoricoWK", true);
		}
	} else {
		setDisabled("dtVigenciaInicialSolicitada", false);
		setDisabled("dtVigenciaFinalSolicitada", false);
		setDisabled("municipioCobraTRT", false);
		setDisabled("municipioCobraTRT_municipio", false);
		setDisabled("municipioCobraTRT_blCobraTrt", false);
		setDisabled("btnStore", false);
		setDisabled("btnReset", false);
	}
}

function isVisualizacaoWK() {
	var url = new URL(this.parent.location.href);
	return url.parameters.idProcessoWorkflow != undefined;
}

function openModalHistoricoWorkflow() {
	var param = "&idProcesso=" + getElementValue("idTrtCliente");
	param += "&nmTabela=TRT_CLIENTE";
	param += "&cliente.pessoa.nmPessoa=" + escape(document.getElementById("cliente.pessoa.nmPessoa").value);
	param += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("cliente.pessoa.nrIdentificacao").value;

	var url = '/workflow/historicoWorkflow.do?cmd=list' + param;
	var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;';
	showModalDialog(url, window, wProperties);
}

/* Popula a lista de municípios com os municípios default com cobrança de TRT. */
function findMunicipiosComRestricaoTransporte(){
	var service = "lms.vendas.manterTrtClienteAction.findMunicipiosComRestricaoTransporte";
	var data = {idTrtCliente : getElementValue("idTrtCliente")};
	var sdo = createServiceDataObject(service, "findMunicipiosComRestricaoTransporte", data);
	xmit({serviceDataObjects:[sdo]});
}

function findMunicipiosComRestricaoTransporte_cb(data, error){
	municipioCobraTRTListboxDef.renderOptions({municipioCobraTRT: data});
}

function validateDtVigenciaInicial() {
	var service = "lms.vendas.manterTrtClienteAction.validateDtVigenciaInicial";
	var data = {
		dtVigenciaInicial : getElementValue("dtVigenciaInicial"),
		dtVigenciaFinal : getElementValue("dtVigenciaFinal"),
		dtVigenciaInicialSolicitada : getElementValue("dtVigenciaInicialSolicitada")
	};

	var sdo = createServiceDataObject(service, "validateDtVigenciaInicial",	data);
	xmit({serviceDataObjects : [ sdo ]});
	return true;
}

function validateDtVigenciaInicial_cb(data, error) {
	if (error != undefined) {
		setFocus("dtVigenciaInicialSolicitada", true);
		alert(error);
		resetValue("dtVigenciaInicialSolicitada");
		return;
	}
}

function validateDtVigenciaFinal() {
	var service = "lms.vendas.manterTrtClienteAction.validateDtVigenciaFinal";
	var data = {
		dtVigenciaInicialSolicitada : getElementValue("dtVigenciaInicialSolicitada"),
		dtVigenciaFinalSolicitada : getElementValue("dtVigenciaFinalSolicitada")
	};
	var sdo = createServiceDataObject(service, "validateDtVigenciaFinal", data);
	xmit({serviceDataObjects : [ sdo ]});
	return true;
}

function validateDtVigenciaFinal_cb(data, error) {
	if (error != undefined) {
		setFocus("dtVigenciaFinalSolicitada", true);
		alert(error);
		resetValue("dtVigenciaFinalSolicitada");
		return;
	}
}

function validarTrtCliente() {
	var form = getElement("formCad");
	if (!validateForm(form)){
 		return false;
 	}

	var dataCad = buildFormBeanFromForm(form);
	var sdo = createServiceDataObject("lms.vendas.manterTrtClienteAction.validateTrtCliente", "validarTrtCliente", dataCad);
	xmit({serviceDataObjects:[sdo]});
}

function validarTrtCliente_cb(data, error){
	if (error != undefined) {
		alert(error);
		return;
	}

	openPopupMotivoSolicitacao();

	if(getElementValue("dsMotivoSolicitacao") != ''){
		var dataCad = buildFormBeanFromForm(getElement("formCad"));
		var sdo = createServiceDataObject("lms.vendas.manterTrtClienteAction.store", 'store', dataCad);
		xmit({serviceDataObjects:[sdo]});
	}
}

function openPopupMotivoSolicitacao() {
	setElementValue("dsMotivoSolicitacao", '');
	var url = '/vendas/manterTrtCliente.do?cmd=motivoSolicitacao';
	var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';
	showModalDialog(url, window, wProperties);
}

/*
 * Validação de registros duplicados deve ser somente por Município.
 */
function contentChangeMunicipios(event){
	if(event.name == 'modifyButton_afterClick'){
		var count = 0;
		var idMunicipioInsercao = event.src.data.municipio.idMunicipio;

		for (var x = 0; x < municipioCobraTRTListboxDef.getData().length; x++) {
			var idMunicipioExistente = getNestedBeanPropertyValue(municipioCobraTRTListboxDef.getData()[x],"municipio.idMunicipio");
			if(idMunicipioInsercao == idMunicipioExistente){
				count++;
				if(count > 1){
					getElement("municipioCobraTRT").remove(x);
					alert(listbox_duplicatedOption);
				}
			}
		}
	}
}

function myOnPageLoadCallBack_cb(data, error) {
	if (isVisualizacaoWK()){
		var data = new Object();
		data.idProcessoWorkflow = new URL(this.parent.location.href).parameters.idProcessoWorkflow;
		var sdo = createServiceDataObject("lms.vendas.manterTrtClienteAction.findByIdDetalhado", "myDataLoad", data);
		xmit({serviceDataObjects:[sdo]});

	} else {
		onPageLoad_cb(data, error);
	}
	unblockUI();
}

function myDataLoad_cb(data, error) {
	onDataLoad_cb(data, error);
	
	if(getElementValue("idTabelaPreco") != ''){
		controlarCamposByIdTabela();
	} else {
		controlarCampos();
	}

}
</script>

<adsm:window service="lms.vendas.manterTrtClienteAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form idProperty="idTrtCliente" action="/vendas/manterTrtCliente" service="lms.vendas.manterTrtClienteAction.findByIdDetalhado" id="formCad" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="dsMotivoSolicitacao"/>
		<adsm:hidden property="possuiFilhoEmAprovacao"/>

		<adsm:i18nLabels>
			<adsm:include key="LMS-00006"/>
			<adsm:include key="LMS-00007"/>
			<adsm:include key="LMS-00008"/>
			<adsm:include key="LMS-01221"/>
			<adsm:include key="LMS-01222"/>
			<adsm:include key="LMS-02067"/>
		</adsm:i18nLabels>

		<script>
			var LMS02067 = i18NLabel.getLabel('LMS-02067');
			var LMS01221 = i18NLabel.getLabel('LMS-01221');
			var LMS00006 = i18NLabel.getLabel('LMS-00006');
			var LMS00007 = i18NLabel.getLabel('LMS-00007');
			var LMS00008 = i18NLabel.getLabel('LMS-00008');
		</script>

			<adsm:hidden  property="idTabelaPreco"/>
			<adsm:textbox dataType="text" label="tabelaPrecos" labelWidth="17%" width="82%" property="tabelaPrecoDesc" size="70" maxLength="50" disabled="true" serializable="true"/>

			<adsm:lookup dataType="text" property="cliente" idProperty="idCliente" disabled="true"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.vendas.manterTrtClienteAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 label="cliente" size="20" maxLength="20" serializable="true" labelWidth="17%" width="82%">
				<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
				<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="53" maxLength="53" disabled="true" serializable="true"/>
			</adsm:lookup>

			<adsm:range label="vigencia" labelWidth="17%" width="82%">
				<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
				<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true" />
			</adsm:range>

			<adsm:range label="vigenciaSolicitada" labelWidth="17%" width="82%">
				<adsm:textbox dataType="JTDate" property="dtVigenciaInicialSolicitada" picker="true" onchange="return validateDtVigenciaInicial();"/>
				<adsm:textbox dataType="JTDate" property="dtVigenciaFinalSolicitada" picker="true" onchange="return validateDtVigenciaFinal();"/>
			</adsm:range>

			<adsm:combobox domain="DM_STATUS_ACAO_WORKFLOW" property="tpSituacaoAprovacao" label="situacaoAprovacao" labelWidth="17%" disabled="true"/>

			<adsm:listbox label="municipios" property="municipioCobraTRT" required="true"
				size="21" labelWidth="17%" width="82%" labelStyle="vertical-align:top" optionProperty="idMunicipioTrtCliente"
				boxWidth="425" onContentChange="contentChangeMunicipios">
				<adsm:combobox property="municipio" service="lms.vendas.manterTrtClienteAction.findListMunicipiosComUF" optionLabelProperty="nmMunicipioComUF" optionProperty="idMunicipio" renderOptions="true"/>
				<adsm:combobox property="blCobraTrt" domain="DM_SIM_NAO"/>
			</adsm:listbox>

		<adsm:buttonBar>
			<adsm:button caption="historicoWK" id="btnHistoricoWK" onclick="openModalHistoricoWorkflow()" disabled="false"/>
			<adsm:button caption="salvar" buttonType="storeButton" onclick="validarTrtCliente()" id="btnStore"/>
			<adsm:removeButton disabled="true" id="btnRemove" />
			<adsm:resetButton id="btnReset" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>