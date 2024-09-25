<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.liberacaoEmbarqueService" onPageLoadCallBack="myPageLoad">
	<adsm:form id="formCad" action="/vendas/manterLiberacoesEmbaqueMunicipiosCliente" idProperty="idLiberacaoEmbarque" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="pendencia.idPendencia"/>
		<adsm:hidden property="tpSituacaoAtivo" serializable="false" value="A"/>
		<adsm:hidden property="dsMotivoSolicitacao"/>

		<adsm:textbox
			label="cliente"
			property="cliente.pessoa.nrIdentificacao"
			dataType="text"
			size="20" maxLength="20"
			labelWidth="10%" width="90%"
			disabled="true"
			serializable="false">
			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				size="60" maxLength="60"
				disabled="true"
				serializable="false"/>
		</adsm:textbox>
		<adsm:lookup
			label="municipio"
			property="municipio"
			idProperty="idMunicipio"
			dataType="text"
			criteriaProperty="nmMunicipio"
			service="lms.municipios.municipioService.findLookup"
			size="30" maxLength="30"
			action="/municipios/manterMunicipios"
			minLengthForAutoPopUpSearch="3"
			exactMatch="false"
			labelWidth="10%"
			width="40%"
			onDataLoadCallBack="municipioDataLoad"
			onchange="return municipioOnChange();"
			required="true">
			<adsm:propertyMapping
				relatedProperty="municipio.unidadeFederativa.siglaDescricao"
				modelProperty="unidadeFederativa.siglaDescricao" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa"
				modelProperty="unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping
				criteriaProperty="tpSituacaoAtivo"
				modelProperty="tpSituacao" />
		</adsm:lookup>
		<adsm:hidden property="municipio.unidadeFederativa.idUnidadeFederativa"></adsm:hidden>
		<adsm:textbox
			label="uf"
			dataType="text"
			property="municipio.unidadeFederativa.siglaDescricao"
			size="50"
			maxLength="71"
			width="40%"
			labelWidth="10%"
			disabled="true"
			labelStyle="text-align: right; padding-right: 5px"
			serializable="false"/>
		<adsm:combobox
			property="tpModal"
			label="modal"
			domain="DM_MODAL"
			labelWidth="10%"
			width="15%"/>

		<adsm:checkbox property="blLiberaGrandeCapital"
			label="liberaMunicipiosCapital"
			labelWidth="35%"
			labelStyle="text-align: right;padding-right: 5px"
			disabled="true"
			serializable="true"/>

		<adsm:checkbox property="blEfetivado" label="efetivado"
			width="30%"	labelWidth="10%" disabled="true"/>

		<adsm:combobox domain="DM_STATUS_ACAO_WORKFLOW" property="tpSituacaoAprovacao" label="situacaoAprovacao"
		 	labelStyle="text-align: right;padding-right: 5px"
			labelWidth="20%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="historicoWK" id="btnHistorico" onclick="openModalHistoricoWorkflow()" disabled="true"/>
			<adsm:button id="btnEfetivacao" caption="solicitarEfetivacao"	disabled="true" onclick="validateAtendimentoMunicipio('validateEfetivacao')"/>
			<adsm:button id="btnDesefetivacao" caption="solicitarDesefetivacao"	disabled="true" onclick="validateAtendimentoMunicipio('validateDesefetivacao')"/>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function myPageLoad_cb() {
		if (isVisualizacaoWK()){
			var data = new Object();
			data.idProcessoWorkflow = new URL(this.parent.location.href).parameters.idProcessoWorkflow;
			var sdo = createServiceDataObject("lms.vendas.manterLiberacoesEmbaqueMunicipiosClienteAction.findByIdProcessoWorkflow", "myDataLoad", data);
			xmit({serviceDataObjects:[sdo]});

		} else {
			onPageLoad_cb();
		}
	}

	/*
	 Criada para validar acesso do usuário
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}

	function validateAtendimentoMunicipio(callBack) {
		var data = new Object();
		data.idMunicipio = getElementValue("municipio.idMunicipio");
		var sdo = createServiceDataObject("lms.vendas.manterLiberacoesEmbaqueMunicipiosClienteAction.validateAtendimentoMunicipio", callBack, data);
		xmit({serviceDataObjects:[sdo]});
	}

	function validateDesefetivacao_cb(data, error) {
		if (error != undefined) {
			alert(error);
		} else {
			onClickBtnDesefetivacao();
		}
	}

	function validateEfetivacao_cb(data, error) {
		if (error != undefined) {
			alert(error);
		} else {
			onClickBtnEfetivacao();
		}
	}

	function onClickBtnDesefetivacao() {
		openPopupMotivoSolicitacao()
		if(getElementValue("dsMotivoSolicitacao") != ''){
			var dataCad = buildFormBeanFromForm(getElement("formCad"));
			var sdo = createServiceDataObject("lms.vendas.manterLiberacoesEmbaqueMunicipiosClienteAction.executeSolicitarDesefetivacao", 'store', dataCad);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function onClickBtnEfetivacao() {
		openPopupMotivoSolicitacao()
		if(getElementValue("dsMotivoSolicitacao") != ''){
			var dataCad = buildFormBeanFromForm(getElement("formCad"));
			var sdo = createServiceDataObject("lms.vendas.manterLiberacoesEmbaqueMunicipiosClienteAction.executeSolicitarEfetivacao", 'store', dataCad);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function openPopupMotivoSolicitacao() {
		mostrouPopupMotivo = true;
		setElementValue("dsMotivoSolicitacao", '');
		var url = '/vendas/manterLiberacoesEmbaqueMunicipiosCliente.do?cmd=motivoSolicitacao';
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';
		showModalDialog(url, window, wProperties);
	}

	function openModalHistoricoWorkflow() {
		var param = "&idProcesso=" + getElementValue("idLiberacaoEmbarque");
		param += "&nmTabela=LIBERACAO_EMBARQUE";
		param += "&cliente.pessoa.nmPessoa=" + escape(document.getElementById("cliente.pessoa.nmPessoa").value);
		param += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("cliente.pessoa.nrIdentificacao").value;

		var url = '/workflow/historicoWorkflow.do?cmd=list' + param;
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;';
		showModalDialog(url, window, wProperties);
	}

	function initWindow(evtObj) {
		// No caso de edição vai passar por aqui após carregar os dados na tela.
		if(evtObj !== undefined && evtObj.name === "gridRow_click"){
			return;
		}

		controlarCampos();
		controlarBotoes();
	}

	function controlarBotoes(){
		if(isVisualizacaoWK()){
			setDisabled("btnHistorico", true);
			setDisabled("btnEfetivacao", true);
			setDisabled("btnDesefetivacao", true);
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);

		} else {

			if(hasValue(getElementValue("tpSituacaoAprovacao"))){
				setDisabled("btnHistorico", false);
			} else {
				setDisabled("btnHistorico", true);
			}

			if(isEdicao()){
				setDisabled("storeButton", true);
			} else {
				setDisabled("storeButton", false);
			}

			if(getElementValue("tpSituacaoAprovacao") === "E"){
				setDisabled("removeButton", true);
				setDisabled("newButton", true);
				setDisabled("btnEfetivacao", true);
				setDisabled("btnHistorico", false);
				setDisabled("btnDesefetivacao", true);

			} else if(getElementValue("blEfetivado") === false){
				setDisabled("newButton", false);
				setDisabled("btnDesefetivacao", true);

				if(isEdicao()){
					setDisabled("removeButton", false);
					setDisabled("btnEfetivacao", false);
				} else {
					setDisabled("removeButton", true);
					setDisabled("btnEfetivacao", true);
				}

			} else if(getElementValue("blEfetivado") === true){
				setDisabled("removeButton", true);
				setDisabled("newButton", true);
				setDisabled("btnEfetivacao", true);
				setDisabled("btnDesefetivacao", false);
			}
		}
	}

	function controlarCampos() {
		if (isEdicao() || isVisualizacaoWK()) {
			setDisabled("municipio.idMunicipio", true);
			setDisabled("tpModal", true);
			setDisabled("blLiberaGrandeCapital", true);
		} else {
			setDisabled("municipio.idMunicipio", false);
			setDisabled("tpModal", false);
		}
	}

	function isEdicao() {
		return getElementValue("idLiberacaoEmbarque") != undefined && getElementValue("idLiberacaoEmbarque") != "";
	}

	function isVisualizacaoWK() {
		var url = new URL(this.parent.location.href);
		return url.parameters.idProcessoWorkflow != undefined;
	}

	/*
	Função de callback da lookup de municipio
	 */
	function municipioDataLoad_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		var retorno = municipio_nmMunicipio_exactMatch_cb(data);

		/**	Chama a validação do municipio para verificar se é Capital, caso afirmativo, deve habilitar o
		 *checkBox "Libera todos os municípios da grande capital" */
		validateCapital(getElementValue("municipio.idMunicipio"));
		return retorno;
	}

	function validateCapital(idMunicipio) {
		if (idMunicipio != undefined && idMunicipio != "") {
			var criteria = new Object();
			criteria.idMunicipio = idMunicipio;
			var service = "lms.vendas.manterLiberacoesEmbaqueMunicipiosClienteAction.validateCapital";
			var sdo = createServiceDataObject(service, "validateCapital",
					criteria);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		}
	}

	function validateCapital_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data.isCapital != undefined && data.isCapital == "true") {
			setDisabled("blLiberaGrandeCapital", false);
		} else {
			setDisabled("blLiberaGrandeCapital", true);
			setElementValue("blLiberaGrandeCapital", false);
		}
	}

	function municipioOnChange() {
		var retorno = municipio_nmMunicipioOnChangeHandler();
		if (getElementValue("municipio.nmMunicipio") == "") {
			setDisabled("blLiberaGrandeCapital", true);
			setElementValue("blLiberaGrandeCapital", false);
		}
		return retorno;
	}
</script>