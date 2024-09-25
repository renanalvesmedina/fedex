<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoad="myOnPageLoad"
	onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas"
		service="lms.contasreceber.manterFaturasAction" id="formList">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-36218"/>
		</adsm:i18nLabels>		
		
		<adsm:hidden property="tpSituacaoFaturaValido" serializable="true" />

		<adsm:lookup property="filialByIdFilial" idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.contasreceber.manterFaturasAction.findLookupFilial"
			dataType="text" label="filialFaturamento" size="3"
			action="/municipios/manterFiliais" width="9%" required="true"
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px"
			maxLength="3">
			<adsm:propertyMapping
				relatedProperty="filialByIdFilial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialByIdFilial.pessoa.nmFantasia" width="26%" size="30"
				serializable="false" disabled="true" />
		</adsm:lookup>

		<adsm:textbox dataType="integer" label="numero" property="nrFatura"
			size="10" maxLength="10" width="35%" disabled="false" />

		<adsm:lookup action="/vendas/manterDadosIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" exactMatch="true" idProperty="idCliente"
			label="cliente" maxLength="20" property="cliente"
			service="lms.contasreceber.manterFaturasAction.findLookupCliente"
			size="20" labelWidth="15%" width="85%">

			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />

			<adsm:textbox dataType="text" disabled="true"
				property="cliente.pessoa.nmPessoa" serializable="false" size="58" />

		</adsm:lookup>

		<adsm:lookup property="filialByIdFilialCobradora"
			idProperty="idFilial" criteriaProperty="sgFilial"
			service="lms.contasreceber.manterFaturasAction.findLookupFilialCobradora"
			dataType="text" label="filialCobranca" size="3"
			action="/municipios/manterFiliais" width="9%"
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px"
			maxLength="3">
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialCobradora.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialByIdFilialCobradora.pessoa.nmFantasia" width="26%"
				size="30" serializable="false" disabled="true" />
		</adsm:lookup>

		<adsm:textbox dataType="text" label="numeroPreFatura"
			property="nrPreFatura" size="10" maxLength="20" />

		<adsm:combobox
			service="lms.contasreceber.manterFaturasAction.findComboCedentes"
			optionLabelProperty="comboText" optionProperty="idCedente"
			boxWidth="180" property="cedente.idCedente" label="banco">
		</adsm:combobox>

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" />

		<adsm:combobox property="tpAbrangencia" label="abrangencia"
			domain="DM_ABRANGENCIA" />

		<adsm:combobox property="tpSituacaoFatura" label="situacaoFatura"
			domain="DM_STATUS_ROMANEIO" />
		<adsm:range label="dataEmissao" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" size="10"
				maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" size="10"
				maxLength="20" />
		</adsm:range>

		<adsm:range label="dataVencimento" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtVencimentoInicial"
				size="10" maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtVencimentoFinal"
				size="10" maxLength="20" />
		</adsm:range>
		<adsm:range label="dataLiquidacao" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtLiquidacaoInicial"
				size="10" maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtLiquidacaoFinal"
				size="10" maxLength="20" />
		</adsm:range>

		<adsm:hidden property="tpFatura" value="R" serializable="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				buttonType="findButton"
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="return myFindButtonScript('fatura', this.form);"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idFatura" property="fatura"
		scrollBars="horizontal" rows="6" gridHeight="130"
		defaultOrder="filialByIdFilial_.sgFilial, nrFatura"
		service="lms.contasreceber.manterFaturasAction.findPaginatedFatura"
		rowCountService="lms.contasreceber.manterFaturasAction.getRowCountFatura">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="fatura"
				property="filialByIdFilial.sgFilial" />
			<adsm:gridColumn width="85" title="" property="nrFatura"
				mask="0000000000" dataType="integer" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="filialCobranca"
			property="filialByIdFilialCobradora.sgFilial" width="80" />
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="cliente"
				property="cliente.pessoa.nrIdentificacao" width="80" />
			<adsm:gridColumn title="" property="cliente.pessoa.nmPessoa"
				width="150" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="dataEmissao" dataType="JTDate"
			property="dtEmissao" width="80" />
		<adsm:gridColumn title="dataVencimento" dataType="JTDate"
			property="dtVencimento" width="80" />
		<adsm:gridColumn title="dataLiquidacao" dataType="JTDate"
			property="dtLiquidacao" width="80" />

		<adsm:gridColumn title="situacao" width="100" isDomain="true"
			property="tpSituacaoFatura" />

		<adsm:gridColumn title="valorTotal" property="siglaSimbolo" width="55"
			dataType="text" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="vlTotal" dataType="currency"
				width="65" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="valorDesconto" property="siglaSimboloDesconto"
			width="55" dataType="text" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="vlDesconto" dataType="currency"
				width="65" />
		</adsm:gridColumnGroup>

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
	document.getElementById("tpSituacaoFaturaValido").masterLink = "true";
	document.getElementById("tpFatura").masterLink = "true";
	document.getElementById("filialByIdFilial.sgFilial").serializable = true;
	document.getElementById("filialByIdFilialCobradora.sgFilial").serializable = true;

	function myOnShow(x, eventObj){		
		if (eventObj.name == "tab_click"){		
			getTabGroup(this.document).getTab("cad").tabOwnerFrame.initPage();						
		}		
		tab_onShow();
	}
	
	function initWindow(eventObj) {
    	if (eventObj.name == "cleanButton_click") {
	    	initPage();
	    }
	}

	function initPage(){
		if (getElement('filialByIdFilial.sgFilial').masterLink != 'true' && getElement('nrFatura').masterLink != 'true'){
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialSessao", "initPage", null)); 
			xmit(false);
		}
	}
	
	/*
	 * Monta as duas constantes que tem a lista de situação de fatura	
	 */
	function initPage_cb(d,e,o,x){
		if (e == undefined) {	
			fillFormWithFormBeanData(0, d);		
		}
	}
	
	function myOnPageLoad(){
		var url = new URL(parent.location.href);

		/** Caso o idProcessoWorkFlow venha na URL, seleciona a Tab de CAD */
		if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
			setDisabled(document, true);
			getTabGroup(this.document).getTab("pesq").setDisabled(true);
			getTabGroup(this.document).selectTab("cad", "tudoMenosNulo", true);
		} else {
			onPageLoad();
		}
	}
		
	/**
	  * CallBack da página
	  */
	function myOnPageLoad_cb(data, error){
		onPageLoad_cb(data, error);
		initPage();
	}
	
	function myFindButtonScript(callback, form){
		if (!validateForm(document.forms[0])){
			return false;
		}
		
		if ((getElementValue("nrFatura") == "") &&
			(getElementValue("nrPreFatura") == "") &&
			(getElementValue("cliente.idCliente") == "") &&
			(getElementValue("dtEmissaoInicial") == "" || getElementValue("dtEmissaoFinal") == "") &&
			(getElementValue("dtVencimentoInicial") == "" || getElementValue("dtVencimentoFinal") == "") &&
			(getElementValue("dtLiquidacaoInicial") == "" || getElementValue("dtLiquidacaoFinal") == "")
			){
			alertI18nMessage("LMS-36218");
			return false;
		}
		
		return findButtonScript(callback, form);
	}	
</script>
