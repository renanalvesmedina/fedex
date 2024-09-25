<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	onPageLoad="myOnPageLoad"
	service="lms.vendas.gerarCotacoesAction">
	<adsm:i18nLabels>
		<adsm:include
			key="LMS-01105"/>
	</adsm:i18nLabels>
	<adsm:form
		action="/vendas/gerarCotacoes" id="formList">
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		
		<adsm:lookup
			service="lms.vendas.gerarCotacoesAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialOrigem"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="12%"
			width="9%">

			<adsm:propertyMapping
				relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

		 	<adsm:textbox
		 		dataType="text"
				property="filialByIdFilialOrigem.pessoa.nmFantasia"
				width="23%"
				size="30"
				serializable="false"
				disabled="true"/>

		</adsm:lookup>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.gerarCotacoesAction.findCliente"
			dataType="text" 
			property="clienteByIdClienteSolicitou"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="remetente"
			size="20"
			maxLength="20"
			width="42%"
			labelWidth="14%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteSolicitou.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteSolicitou.pessoa.nmPessoa"
				size="30"
				maxLength="30"
				disabled="true"
				serializable="false"/>

		</adsm:lookup>

		<adsm:lookup
			service="lms.vendas.gerarCotacoesAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialDestino"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="12%"
			width="9%">

			<adsm:propertyMapping
				relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

		 	<adsm:textbox
		 		dataType="text"
				property="filialByIdFilialDestino.pessoa.nmFantasia"
				width="23%"
				size="30"
				serializable="false"
				disabled="true"/>

		</adsm:lookup>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.gerarCotacoesAction.findCliente"
			dataType="text"
			property="clienteByIdClienteDestino"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="destinatario"
			size="20"
			maxLength="20"
			width="42%"
			labelWidth="14%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestino.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestino.pessoa.nmPessoa"
				size="30"
				maxLength="30"
				disabled="true"
				serializable="false"/>

		</adsm:lookup>

		<adsm:lookup
			service="lms.vendas.gerarCotacoesAction.findFilial"
			action="/municipios/manterFiliais"
			property="filial"
			idProperty="idFilial"
			onchange="return changeFilial(this)"
			onDataLoadCallBack="filial"
			onPopupSetValue="filialPopup"
			criteriaProperty="sgFilial"
			label="numeroCotacao"
			popupLabel="pesquisarFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="12%"
			width="9%">
			
			<adsm:textbox
				property="nrCotacao"
				dataType="integer"
				size="8"
				maxLength="8"
				width="23%"/>
		</adsm:lookup>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.gerarCotacoesAction.findCliente"
			dataType="text"
			property="clienteByIdCliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="responsavelFrete"
			size="20"
			maxLength="20"
			width="42%"
			labelWidth="14%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdCliente.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdCliente.pessoa.nmPessoa"
				size="30"
				maxLength="30"
				disabled="true"
				serializable="false"/>

		</adsm:lookup>

		<adsm:range
			label="periodo"
			labelWidth="12%"
			width="32%">

			<adsm:textbox
				dataType="JTDate"
				property="dtGeracaoCotacaoInicial"/>

			<adsm:textbox
				dataType="JTDate"
				property="dtGeracaoCotacaoFinal"/>
		</adsm:range>

		<adsm:textbox
			property="nrNotaFiscal"
			label="notaFiscal"
			dataType="integer"
			maxLength="6"
			labelWidth="14%"
			width="42%"/>

		<adsm:combobox
			property="tpDocumentoCotacao"
			label="tipoDocumento"
			domain="DM_TIPO_COD_SERV_CONHECIMENTO"
			labelWidth="12%"
			width="88%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="cotacao"/>
			<adsm:button
				onclick="limpar()"
				disabled="false"
				id="btnLimpar"
				caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idCotacao"
		property="cotacao"
		service="lms.vendas.gerarCotacoesAction.findCotacaoPaginated"
		rowCountService="lms.vendas.gerarCotacoesAction.getRowCountCotacao"
		unique="true"
		rows="9"
		width="1010"
		scrollBars="horizontal">
		<adsm:gridColumn title="data" dataType="JTDate" property="dtGeracaoCotacao" width="60"/>
		<adsm:gridColumn title="numeroCotacao" property="nrCotacao" width="90"/>
		<adsm:gridColumn title="remetente" property="nmClienteRemetente" width="140"/>
		<adsm:gridColumn title="destinatario" property="nmClienteDestinatario" width="140"/>
		<adsm:gridColumn title="responsavelFrete" property="nmCliente" width="140"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="130" isDomain="true"/>
		<adsm:gridColumn title="doctoCotacao" property="tpDocumentoCotacao" width="140" isDomain="true"/>
		<adsm:gridColumn title="servico" property="dsServico" width="210"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	function limpar(){
		cleanButtonScript(this.document);
		populateFields();
	}
	/************************************************************\
	*
	\************************************************************/
	function validateTab(){
		var isFind = false;
		var nrCotacao = getElementValue("nrCotacao");

		if(nrCotacao == ''){
			var dtInicial = getElement("dtGeracaoCotacaoInicial");
			var dtFinal = getElement("dtGeracaoCotacaoFinal");

			if((getElementValue(dtInicial) + getElementValue(dtFinal)) != '') {
				isFind = true;
			} else {
				alert(getMessage(erRequired, new Array(dtInicial.label)));
				setFocus(dtInicial, false);
			}
		} else {
			isFind = true;
		}

		return isFind;
	}
	/************************************************************\
	*
	\************************************************************/
	function initWindow(event) {
		switch(event.name){
			case "tab_click":
				var tabGroup = getTabGroup(this.document);
				tabGroup.setDisabledTab("param", true);
				tabGroup.setDisabledTab("servAd", true);
				tabGroup.setDisabledTab("taxas", true);
				tabGroup.setDisabledTab("gen", true);
			case "cleanButton_click":
				setDisabled('btnLimpar', false);
				break;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function filial_cb(data, error){
		validateNrCotacao(data);
		filial_sgFilial_exactMatch_cb(data);
	}

	function filialPopup(data) {
		validateNrCotacao(data);
	}

	function validateNrCotacao(data) {
		if(data != undefined){
			setDisabled('nrCotacao', false);
		} else {
			setDisabled('nrCotacao', true);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function changeFilial(obj){
		var isValid = filial_sgFilialOnChangeHandler();
		if(getElementValue(obj) == '') {
			setElementValue('nrCotacao', '');
			setDisabled('nrCotacao', true);
		}
		return isValid;
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad(){
		onPageLoad();

		if (getElementValue("idProcessoWorkflow") != "") {
			alert('feito');
	   		setDisabled(document, true);
		}else{
			limpar();
		}
			
		
	}
	/************************************************************\
	*
	\************************************************************/
	function populateFields(){
		var service = "lms.vendas.gerarCotacoesAction.getData";
		var sdo = createServiceDataObject(service, "myOnLoadPage", {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnLoadPage_cb(data, error){
		if (error != undefined){
			alert(error);
			return false;
		}

		var url = new URL(parent.location.href);

		if (url.parameters != undefined 
				&& url.parameters.idProcessoWorkflow != undefined 
				&& url.parameters.idProcessoWorkflow != '') {
				
			setDisabled("formList", true);
			getTabGroup(document).selectTab("cad", "tudoMenosNulo", true);
		} else {
			if(data){
				setElementValue('filial.idFilial', data.filial.idFilial);
				setElementValue('filial.sgFilial', data.filial.sgFilial);
				setDisabled('nrCotacao', false);
				setElementValue('dtGeracaoCotacaoInicial', setFormat('dtGeracaoCotacaoInicial', data.dtGeracaoCotacaoInicial));
				setElementValue('dtGeracaoCotacaoFinal', setFormat('dtGeracaoCotacaoFinal', data.dtGeracaoCotacaoFinal));
			}
			setFocusOnFirstFocusableField();	
		}
	}
//-->
</script>
