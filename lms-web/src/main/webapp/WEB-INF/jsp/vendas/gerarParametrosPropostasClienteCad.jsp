<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.vendas.gerarParametrosPropostaAction"
	onPageLoadCallBack="myOnPageLoad">

	<adsm:i18nLabels>
		<adsm:include key="LMS-01040"/>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01051"/>
		<adsm:include key="LMS-01060"/>
		<adsm:include key="LMS-01070"/>
		<adsm:include key="LMS-01155"/>
		<adsm:include key="pagaPesoExcedente"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/gerarParametrosPropostasCliente"
		idProperty="idProposta"
		onDataLoadCallBack="myOnDataLoadCallBack"
		height="370">

		<adsm:hidden property="tpGeracaoProposta"/>		
		<adsm:hidden property="simulacao.idSimulacao"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"/>
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="disableAll"/>

		<%-- FRETE PESO SECTION --%>
		<adsm:section 
			caption="fretePeso"/>

		<%-- MINIMO FRETE PESO COMBO --%>
		<adsm:hidden property="minimoFretePesoFlag" value="false"/>
		<adsm:combobox 
			domain="DM_INDICADOR_FRETE_MINIMO_PROPOSTA" 
			label="minimoFretePeso" 
			labelWidth="23%"
			onchange="return validateTpMinimoFretePeso();"
			property="tpIndicadorMinFretePeso" 
			required="true"
			width="29%">
			<adsm:textbox 
				dataType="decimal" 
				property="vlMinFretePeso" 
				mask="###,###,###,###,##0.00"
				onchange="return validateVlMinimoFretePeso();"
				size="18"/>
		</adsm:combobox>

		<%-- PERCENTUAL MINIMO COMBO --%>
		<adsm:combobox 
			domain="DM_ACRESCIMO_DESCONTO"
			label="percentualMinimo"
			labelWidth="19%"
			onchange="return validateTpPercentualMinimo();"
			property="tpIndicadorFreteMinimo" 
			required="true"
			width="29%">
			<adsm:textbox
				dataType="decimal"
				property="vlFreteMinimo"
				mask="###,##0.00"
				onchange="return validateVlPercentualMinimo();"
				size="18"/>
		</adsm:combobox>

		<%-- FRETE PESO COMBO --%>
		<adsm:combobox
			domain="DM_ACRESCIMO_DESCONTO"
			label="fretePeso"
			labelWidth="23%"
			onchange="return validateTpFretePeso();"
			property="tpIndicadorFretePeso"
			required="true"
			width="29%">
			<adsm:textbox
				dataType="decimal"
				property="vlFretePeso"
				mask="###,###,###,###,##0.00000"
				onchange="return validateVlFretePeso();"
				size="18"/>
		</adsm:combobox>

		<%-------------------------------%>
		<%-- PAGA PESO EXCEDENTE CHECK --%>
		<%-------------------------------%>
		<adsm:checkbox
			property="blPagaPesoExcedente"
			label="pagaPesoExcedente"
			labelWidth="19%"
			width="29%"/>

		<adsm:textbox 
			dataType="decimal"
			property="pcDiferencaFretePeso"
			label="diferencaCapitalInterior"
			mask="##0.00"
			minValue="0.00"
			maxValue="100.00"
			labelWidth="23%"
			width="77%"
			size="18"
			required="true"/>

		<%-- FRETE VALOR SECTION --%>
		<adsm:section
			caption="freteValor"/>

		<%-- ADVALOREM1 COMBO --%>
		<adsm:combobox
			domain="DM_INDICADOR_ADVALOREM"
			label="adValorem"
			labelWidth="23%"
			onchange="return validateTpAdvalorem1();"
			property="tpIndicadorAdvalorem"
			required="true"
			width="40%">
			<adsm:textbox
				dataType="decimal"
				property="vlAdvalorem"
				maxLength="15"
				onchange="return validateVlAdvalorem1();"
				size="18"/>
		</adsm:combobox>

		<adsm:combobox property="simulacao.tpDiferencaAdvalorem" label="tipoDiferencaCapitalInterior" labelWidth="23%" width="28%" domain="DM_TIPO_DIFERENCA" disabled="false" required="true"/>

		<adsm:textbox 
			dataType="decimal"
			property="pcDiferencaAdvalorem"
			label="diferencaCapitalInterior"
			mask="##0.00"
			minValue="0.00"
			maxValue="100.00"
			labelWidth="20%"
			width="20%"
			size="18"
			required="true"/>

		<%-- FRETE PERCENTUAL --%>
		<adsm:section 
			caption="fretePercentual"/>			

		<adsm:textbox 
			dataType="decimal"
			property="pcFretePercentual"
			label="pcFretePercentual"
			mask="##0.00"
			minValue="0.00"
			maxValue="100.00"
			labelWidth="23%"
			width="70%" 
			size="8"
			required="true"/>	
			
		<adsm:textbox 
			dataType="decimal"
			property="vlMinimoFretePercentual"
			label="vlMinimoFretePercentual"
			mask="###,###,###,###,##0.00"
			minValue="0.00"
			labelWidth="23%"
			width="70%"
			size="8"
			required="true"/>	
			
		<adsm:textbox 
			dataType="decimal"
			property="vlToneladaFretePercentual"
			label="vlToneladaFretePercentual"
			mask="###,###,###,###,##0.00000"
			minValue="0.00"
			labelWidth="23%"
			width="70%"
			unit="kg"
			size="8"
			required="true"/>	
			
		<adsm:textbox 
			dataType="decimal"
			property="psFretePercentual"
			label="psFretePercentual"
			mask="###,###,###,###,##0.00"
			minValue="0.00"
			labelWidth="23%"
			width="70%"
			size="8"
			required="true"/>	

		<%-- ESPECIFICAÇÕES SECTION --%>
		<adsm:section 
			caption="especificacoes"/>

		<%-- UFs DE ORIGEM --%>
		<adsm:combobox
			label="origem"
			service="lms.vendas.gerarParametrosPropostaAction.findUnidadeFederativaFromBrasil"
			property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa"
			optionLabelProperty="siglaDescricao"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="23%"
			width="29%"
			boxWidth="150"
			disabled="true"
			required="true"/>

		<%-- LOCALIZACAO DE ORIGEM --%>
		<adsm:combobox
			label="tipoLocalizacao"
			service="lms.vendas.gerarParametrosPropostaAction.findTipoLocalizacaoOperacional"
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"
			optionLabelProperty="dsTipoLocalizacaoMunicipio"
			optionProperty="idTipoLocalizacaoMunicipio"
			onlyActiveValues="true"
			labelWidth="19%"
			width="29%"
			boxWidth="150"
			required="true"/>

		<%-- GERA EXPEDICAO CHECK --%>
		<adsm:checkbox
			property="blFreteExpedido"
			labelWidth="23%"
			width="29%"
			label="geraExpedicao"/>

		<%-- GERA RECEPCAO CHECK --%>
		<adsm:checkbox
			property="blFreteRecebido"
			labelWidth="19%"
			width="29%"
			label="geraRecepcao"/>

		<adsm:hidden property="zonaByIdZonaOrigem.idZona" />
		<adsm:hidden property="paisByIdPaisOrigem.idPais" />
		<adsm:buttonBar>
			<adsm:button caption="continuar" disabled="false" id="btnContinuar" buttonType="continua" onclick="continuar(this.form.document)" />
			<adsm:button caption="limpar" disabled="false" id="btnLimpar" buttonType="newButton" onclick="resetFields();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/parametroClienteProposta.js"></script>
<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("dest", true);
			tabGroup.setDisabledTab("grup", true);
			validateDisabledFields();
		}
	}

	function resetFields(){
		if(getElementValue("tpGeracaoProposta") == "P"){
			resetSessionPercentual();
		}else{
			newButtonScript(this.document,undefined,{name:'newButton'})
			resetSessionPercentual();
		}	
	}

	function resetSessionPercentual(){
		setFormattedValue("pcFretePercentual", "0.00");
		setFormattedValue("vlMinimoFretePercentual", "0.00");
		setFormattedValue("vlToneladaFretePercentual", "0.00");
		setFormattedValue("psFretePercentual", "0.00");
	}

	function continuar(documento) {

		if(documento == undefined) {
			documento = this.document;
		}
		
		var tabGroup = getTabGroup(documento);
		if(validateParametrosProposta()) {
			if(getElementValue("tpGeracaoProposta") == "P"){
				tabGroup.setDisabledTab("grup", false);
				tabGroup.selectTab("grup",{name:"continuar"});
				return true;
				
			}else{
			tabGroup.setDisabledTab("dest", false);
			tabGroup.selectTab("dest",{name:"continuar"});
			return true;
		}
		}
		
		return false;
	}

	function myOnPageLoad_cb() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined) {
			if (hasValue(url.parameters.idSimulacao)) {
				onDataLoad(url.parameters.idSimulacao);
			}
		}
		onPageLoad_cb();

		/*Desabilita os campos através do tipo de geração*/
		disableFieldsByGeracao();

		/*Seta os valores default para os campos percentuais*/
		resetSessionPercentual();
		
	}
	function myOnDataLoadCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		if(!hasValue(getElementValue("idProposta"))) {
			setDefaultValues();
			findSessionData();
		} else {
			if (hasValue(getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio")))
				setDisabled("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", true);
		}
	}

	/**
	  Desabilita os campos da tela através do tipo de Geração da Proposta	
	*/
	function disableFieldsByGeracao(){
		var percentual = false;
		var tpGeracaoProposta = getElementValue("tpGeracaoProposta");
		if(tpGeracaoProposta == "P"){
			percentual = true;				
		}		

		/*Desabilita todos os campos*/
		setDisabled(this.document, percentual);

		/*Habilita os campos da sessao especificacoes*/
		disableSessionEspecificacoes(false);

		/*Habilita ou desabilita a sessao  a sessao percentual*/
		disableSessionPercentual(!percentual);

		/*Obriga os campos da sessao percentual*/
		if(tpGeracaoProposta == "P"){
			
			document.getElementById("simulacao.tpDiferencaAdvalorem").required = "false";
			requiredSessionPercentual("true");	
		}else{
			document.getElementById("simulacao.tpDiferencaAdvalorem").required = "true";
			requiredSessionPercentual("false");	
		}

		/*Obriga os campos da sessao especificacoes*/
		requiredSessionEspecificacoes("true");
	}

	function disableSessionEspecificacoes(disable){
		setDisabled("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", true);
		setDisabled("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", disable);
		setDisabled("blFreteExpedido", disable);
		setDisabled("blFreteRecebido", disable);
	}

	function disableSessionPercentual(disable){
		setDisabled("pcFretePercentual", disable);
		setDisabled("vlMinimoFretePercentual", disable);
		setDisabled("vlToneladaFretePercentual", disable);
		setDisabled("psFretePercentual", disable);
	}

	function requiredSessionEspecificacoes(req){
		document.getElementById("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio").required = req;
		document.getElementById("unidadeFederativaByIdUfOrigem.idUnidadeFederativa").required = req;		
	}
	
	function requiredSessionPercentual(req){
		document.getElementById("pcFretePercentual").required = req;
		document.getElementById("vlMinimoFretePercentual").required = req;
		document.getElementById("vlToneladaFretePercentual").required = req;
		document.getElementById("psFretePercentual").required = req;
	}
	
	/** Busca a UF do Usuario em Sessao, para setar como default */
	function findSessionData() {
		var sdo = createServiceDataObject("lms.vendas.gerarParametrosPropostaAction.findSessionData", "findSessionData");
		xmit({serviceDataObjects:[sdo]});
	}
	function findSessionData_cb(data) {
		if(hasValue(data.idUFUsuarioSessao)) {
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", data.idUFUsuarioSessao);
		}
		if(hasValue(data.pcDiferencaFretePesoParametro)) {
			setFormattedValue("pcDiferencaFretePeso", data.pcDiferencaFretePesoParametro);
		}
	}

	function validateParametrosProposta() {
		return (validateTabScript(document.forms) && validateParametroCliente() && validateFrete());
	}
	function validateFrete() {
		if(!getElementValue("blFreteExpedido") && !getElementValue("blFreteRecebido")) {
			alertI18nMessage("LMS-01051");
			setFocus("blFreteExpedido");
			return false;
		}
		return true;
	}

	function validateDisabledFields() {
		if(getElementValue("disableAll") == "true") {
			setDisabled(document, true);
			setDisabled("btnContinuar", false);
			return false;
		}
		return true;
	}

	function setIdPropertyValue(idProposta) {
		setElementValue("idProposta", idProposta);
	}

	function getValuesFromCad() {
		comboboxChange({e:document.getElementById("unidadeFederativaByIdUfOrigem.idUnidadeFederativa")});
		comboboxChange({e:document.getElementById("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio")});

		var data = new Array();
		setNestedBeanPropertyValue(data, "idProposta", getElementValue("idProposta"));
		setNestedBeanPropertyValue(data, "simulacao.idSimulacao", getElementValue("simulacao.idSimulacao"));
		setNestedBeanPropertyValue(data, "unidadeFederativaByIdUfOrigem.idUnidadeFederativa", getElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		setNestedBeanPropertyValue(data, "tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"));
		setNestedBeanPropertyValue(data, "disableAll", getElementValue("disableAll"));
		setNestedBeanPropertyValue(data, "tpIndicadorMinFretePeso", getElementValue("tpIndicadorMinFretePeso"));
		setNestedBeanPropertyValue(data, "vlMinFretePeso", getElementValue("vlMinFretePeso"));
		setNestedBeanPropertyValue(data, "tpIndicadorFreteMinimo", getElementValue("tpIndicadorFreteMinimo"));
		setNestedBeanPropertyValue(data, "vlFreteMinimo", getElementValue("vlFreteMinimo"));
		setNestedBeanPropertyValue(data, "tpIndicadorFretePeso", getElementValue("tpIndicadorFretePeso"));
		setNestedBeanPropertyValue(data, "vlFretePeso", getElementValue("vlFretePeso"));
		setNestedBeanPropertyValue(data, "blPagaPesoExcedente", getElementValue("blPagaPesoExcedente"));
		setNestedBeanPropertyValue(data, "pcDiferencaFretePeso", getElementValue("pcDiferencaFretePeso"));
		setNestedBeanPropertyValue(data, "tpIndicadorAdvalorem", getElementValue("tpIndicadorAdvalorem"));
		setNestedBeanPropertyValue(data, "vlAdvalorem", getElementValue("vlAdvalorem"));
		setNestedBeanPropertyValue(data, "pcDiferencaAdvalorem", getElementValue("pcDiferencaAdvalorem"));
		setNestedBeanPropertyValue(data, "tpDiferencaAdvalorem", getElementValue("simulacao.tpDiferencaAdvalorem"));
		setNestedBeanPropertyValue(data, "blFreteExpedido", getElementValue("blFreteExpedido"));
		setNestedBeanPropertyValue(data, "blFreteRecebido", getElementValue("blFreteRecebido"));
		setNestedBeanPropertyValue(data, "pcFretePercentual", getElementValue("pcFretePercentual"));
		setNestedBeanPropertyValue(data, "vlMinimoFretePercentual", getElementValue("vlMinimoFretePercentual"));
		setNestedBeanPropertyValue(data, "vlToneladaFretePercentual", getElementValue("vlToneladaFretePercentual"));
		setNestedBeanPropertyValue(data, "psFretePercentual", getElementValue("psFretePercentual"));

		
		return data;
	}
</script>