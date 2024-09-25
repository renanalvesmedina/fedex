<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.vendas.manterPrazoVencimentoDivisaoAction">
	<adsm:form
		action="/vendas/manterPrazoVencimentoDivisao"
		idProperty="idPrazoVencimento"
		onDataLoadCallBack="myOnDataLoad" id="formDataPrazoVencimento">

		<adsm:complement
			label="cliente"
			labelWidth="17%"
			required="true"
			width="83%"
			separator="branco">

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"
				size="20"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false"
				size="30"/>
		</adsm:complement>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="divisao"
			labelWidth="17%"
			maxLength="60"
			property="divisaoCliente.dsDivisaoCliente"
			required="true"
			serializable="false"
			size="51"
			width="40%"/>

		<adsm:combobox
			domain="DM_MODAL"
			label="modal"
			labelWidth="17%"
			onchange="buscaServicos();"
			property="tpModal"
			required="true"
			width="23%"/>

		<adsm:combobox
			domain="DM_ABRANGENCIA"
			label="abrangencia"
			labelWidth="17%"
			boxWidth="90"
			onchange="buscaServicos();"
			property="tpAbrangencia"
			width="40%"/>

		<adsm:combobox
			boxWidth="200"
			label="servico"
			labelWidth="17%"
			onchange="setModalAbrangencia(this)"
			onDataLoadCallBack="servico"
			onlyActiveValues="true"
			optionLabelProperty="dsServico"
			optionProperty="idServico"
			property="servico.idServico"
			service="lms.configuracoes.servicoService.find"
			width="23%">

			<adsm:propertyMapping
				modelProperty="tpAbrangencia"
				relatedProperty="tpAbrangencia"/>

			<adsm:propertyMapping
				modelProperty="tpModal"
				relatedProperty="tpModal"/>

		</adsm:combobox>

		<adsm:combobox
			domain="DM_TIPO_FRETE"
			label="tipoFrete"
			labelWidth="17%"
			boxWidth="90"
			property="tpFrete"
			width="40%"/>
			
		<adsm:combobox
			domain="DM_DIAS_SEMANA"
			label="diaSemana"
			labelWidth="17%"
			property="tpDiaSemana"
			width="23%"/>

        <adsm:textbox
        	dataType="integer"
        	label="prazoPagamento"
        	labelWidth="17%"
        	maxLength="3"
        	minValue="1"
        	property="nrPrazoPagamento"
        	required="true"
        	disabled="true"
        	size="4"
        	width="40%"/>

		<adsm:textbox
        	dataType="integer"
        	label="prazoPagamentoSolicitado"
        	labelWidth="17%"
        	maxLength="3"
        	property="nrPrazoPagamentoSolicitado"
        	onchange="return onChangePrazoPagamentoSolicitado();"
        	required="true"
        	size="4"
        	width="23%"/>
        <adsm:hidden property="nrPrazoPagamentoAprovado" serializable="true"/>

        <adsm:listbox
        	label="diaVencimento"
        	property="diasVencimento"
			optionProperty="idDiaVencimento"
			optionLabelProperty="nrDiaVencimento"
			labelWidth="17%"
			onContentChange="contentChangeDiasVencimento"
			width="30%"
			size="4"
			boxWidth="90">

			<adsm:textbox
		       	dataType="integer"
		       	maxValue="31"
		       	minValue="1"
		       	maxLength="2"
		    	property="nrDiaVencimento"
		       	size="4"/>

		</adsm:listbox>

		<adsm:hidden
			property="hasAccess" serializable="false"/>
		
		<adsm:hidden
			property="nrPrazoCobranca" serializable="false"/>

		<adsm:hidden 
			property="divisaoCliente.idDivisaoCliente"/>

		<adsm:hidden
			property="idFilial"/>

		<adsm:hidden
			serializable="false"
			property="servicoValueAux"/>

		<adsm:hidden
			serializable="false"
			property="servicoValue"/>

		<adsm:hidden
			serializable="false"
			property="cameFromOutside"
			value="Y"/>

		<adsm:hidden
			serializable="false"
			property="servicoLabelAux"/>

		<adsm:hidden
			serializable="false"
			property="tpModalValueAux"/>

		<adsm:hidden
			serializable="false"
			property="tpAbrangenciaValueAux"/>
		
		<adsm:hidden
			serializable="false"
			property="blPossuiFiliais"/>

		<adsm:buttonBar>
			<adsm:button caption="salvar" buttonType="storeButton" onclick="validaPrazoPagamentoSolicitado()" disabled="true" id="salvar"/>
			<adsm:newButton id="novo"/>
			<adsm:removeButton id="excluir"/>
		</adsm:buttonBar>

		<adsm:i18nLabels>
			<adsm:include key="LMS-01078"/>
			<adsm:include key="LMS-01079"/>
			<adsm:include key="LMS-01144"/>
			<adsm:include key="LMS-01160"/>
			<adsm:include key="LMS-01331"/>
		</adsm:i18nLabels>

	</adsm:form>
</adsm:window>

<script type="text/javascript">
	function cleanCadTrash(){
		setElementValue("servicoValueAux","");
		setElementValue("servicoValue","");
		setElementValue("servicoLabelAux","");
		setElementValue("tpModalValueAux","");
		setElementValue("tpAbrangenciaValueAux","");
	}
	/************************************************************\
	*
	\************************************************************/
	function prepareAuxValues(data)	{
		var servico = getNestedBeanPropertyValue(data, "servico.idServico");
		setElementValue("servicoValue", servico);
		if(getElementValue("servicoValueAux") == "")
			setElementValue("servicoValueAux", servico);
		if(servico)	{
			var servicoLabel = getNestedBeanPropertyValue(data, "servico.dsServico");
			if(getElementValue("servicoLabelAux") == "") 
				setElementValue("servicoLabelAux",servicoLabel);
		}
		var modal = getElementValue("tpModal");
		if(getElementValue("tpModalValueAux") == "") 
			setElementValue("tpModalValueAux", modal);
		var abrangencia = getElementValue("tpAbrangencia");
		if(getElementValue("tpAbrangenciaValueAux") == "")
			setElementValue("tpAbrangenciaValueAux", abrangencia);
	}
	/************************************************************\
	*
	\************************************************************/
	function servico_cb(data,erros) {
	   	servico_idServico_cb(data, erros);
		var servicoValueAux = getElementValue("servicoValueAux");
		var servicoValue = getElementValue("servicoValue");
	   	var modalValueAux = getElementValue("tpModalValueAux");
		var abrangenciaValueAux = getElementValue("tpAbrangenciaValueAux");
	    var modal = getElementValue("tpModal");
	   	var abrangencia = getElementValue("tpAbrangencia");

	    if((modal==modalValueAux || modal=='') 
	    	&& (abrangencia==abrangenciaValueAux || abrangencia=='')) {
	       	var contains = false;
	   	 	var e = document.getElementById("servico.idServico");
	    	for (var i = 0; i < e.options.length; i++) {
	    		var o = e.options[i];
				if (o.value == servicoValueAux)	{
		    		contains = true;
		    		break;
				}
	    	}
	    	if(!contains) {
				var o = new Option(getElementValue("servicoLabelAux"), getElementValue("servicoValueAux"));
				o._inactive = true;
				e.options.add(o);
	    	}
	   	}
	   	if(getElementValue("cameFromOutside") == "Y") {
	    	setElementValue("servico.idServico",servicoValue);
	    	setElementValue("cameFromOutside", "N");
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function resetServicos(criteria) {
		var sdo = createServiceDataObject("lms.configuracoes.servicoService.find", "servico", criteria);
	    xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function buscaServicos(){
		var modal = getElementValue("tpModal");
		var abrangencia = getElementValue("tpAbrangencia");
	    resetServicos({tpModal:modal,tpAbrangencia:abrangencia,tpSituacao:'A'});
	}
	/************************************************************\
	*
	\************************************************************/
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "newButton_click" 
			|| event == "tab_click"){
			//resetServicos({tpSituacao:'A'});
			cleanCadTrash();
		}
		if(event == "storeButton"){
			setElementValue("cameFromOutside", "Y");
		}
		if (event == "tab_click" || event == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value);
			verifyFilialAnaliseCredito();
		}
		findFilialNrPrazoCobranca();
	}

	function verifyFilialAnaliseCredito() {
	    var sdo = createServiceDataObject("lms.vendas.manterClienteAction.verifyFilialAnaliseCredito", "verifyFilialAnaliseCredito", {idFilialComercial:getElementValue("idFilial")});
      	xmit({serviceDataObjects:[sdo]});
	}
	function verifyFilialAnaliseCredito_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		setElementValue("hasAccess", data.hasAccess);
	}

	/************************************************************\
	*
	\************************************************************/
	function setModalAbrangencia(e) {
		comboboxChange({e:e});
		if(getElementValue("servico.idServico") == getElementValue("servicoValueAux")){
			setElementValue("tpModal", getElementValue("tpModalValueAux"));
			setElementValue("tpAbrangencia", getElementValue("tpAbrangenciaValueAux"));
		}
	}

	function onChangePrazoPagamentoSolicitado() {
		var nrPrazoPagamentoSolicitado = getElementValue("nrPrazoPagamentoSolicitado");
		if(nrPrazoPagamentoSolicitado != null && nrPrazoPagamentoSolicitado != '' && parseInt(nrPrazoPagamentoSolicitado) < 5 ){
			alert(getI18nMessage("LMS-01331"));
			return false;
		}
		
		if(!hasValue(getElementValue("nrPrazoPagamento")) || !hasValue(getElementValue("idPrazoVencimento")) || getElementValue("hasAccess") != "true") {
			setElementValue("nrPrazoPagamento", getElementValue("nrPrazoPagamentoSolicitado"));
		}
		return true;
	}

	/************************************************************\
	*
	\************************************************************/  
	function myOnDataLoad_cb(data, error){
		onDataLoad_cb(data, error);

	    buscaServicos();
	    prepareAuxValues(data);
		validaPermissao();
	}
	/************************************************************\
	*
	\************************************************************/
	function validateTab() {
		return validateTabScript(document.forms);
	}
	/************************************************************\
	*
	\************************************************************/
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("salvar", true);
			setDisabled("novo", true);
			setDisabled("excluir", true);
		}
	}
	
	function contentChangeDiasVencimento(eventObj) {
		if (eventObj.name == "deleteButton_click" && getElementValue("idPrazoVencimento") != "") {
			if (getElementValue("blPossuiFiliais") == "true") {
				alert(getI18nMessage("LMS-01144"));
				return false;
			}
		}
	}
	
	/**
	 * Função para validar o Prazo para pagamento solicitado. LMS-4115
	 */
	function validaPrazoPagamentoSolicitado() {
		if ((parseInt(getElementValue("nrPrazoCobranca")) > getElementValue("nrPrazoPagamentoSolicitado")) && getElementValue("nrPrazoPagamentoSolicitado") != "") {
			if (confirm("LMS-01160 - " +getI18nMessage("LMS-01160", getElementValue("nrPrazoCobranca"), false))) {
				storeButtonScript('lms.vendas.manterPrazoVencimentoDivisaoAction.store', 'store', document.getElementById("formDataPrazoVencimento"));
			}
		} else {
			storeButtonScript('lms.vendas.manterPrazoVencimentoDivisaoAction.store', 'store', document.getElementById("formDataPrazoVencimento"));
		}
		return false;
	}
	
	function findFilialNrPrazoCobranca() {
	    var sdo = createServiceDataObject("lms.vendas.manterPrazoVencimentoDivisaoAction.findFilialNrPrazoCobranca", "findFilialNrPrazoCobranca", {idDivisaoCliente:getElementValue("divisaoCliente.idDivisaoCliente")});
      	xmit({serviceDataObjects:[sdo]});
	}
	function findFilialNrPrazoCobranca_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		setElementValue("nrPrazoCobranca", data.nrPrazoCobranca);
	}
</script>
