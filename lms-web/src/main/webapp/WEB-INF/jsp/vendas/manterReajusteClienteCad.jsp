<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterReajusteClienteAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01033"/>
		<adsm:include key="LMS-01034"/>
		<adsm:include key="LMS-01035"/>
		<adsm:include key="LMS-01036"/>
		<adsm:include key="LMS-01162"/>
		<adsm:include key="LMS-29168"/>
		<adsm:include key="requiredField"/>
	</adsm:i18nLabels>
	<adsm:form
		action="/vendas/manterReajusteCliente"
		idProperty="idReajusteCliente"
		onDataLoadCallBack="formLoad">

		<adsm:hidden property="idProcessoWorkflow" />
		<adsm:hidden property="pendenciaAprovacao.idPendencia" />
		<adsm:hidden property="idDivisaoCliente" />
		<adsm:hidden property="tpSituacaoAprovacao.value" />
		<adsm:hidden property="divisaoCliente.dsDivisaoCliente" />
		<adsm:hidden property="usuarioPerfilComercialParametrizacao" />
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.nrVersao"
			serializable="false" />
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"
			serializable="false" />
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco"
			serializable="false" />
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"
			serializable="false" />

		<%----------------------%>
		<%-- NR PROPOSTA TEXT --%>
		<%----------------------%>
		<adsm:textbox
			dataType="text"
			label="numeroReajuste"
			property="nrReajuste"
			size="10"
			maxLength="10"
			labelWidth="16%"
			width="46%"
			disabled="true" />

		<adsm:textbox
			dataType="text"
			property="tpSituacaoAprovacao"
			labelWidth="17%"
			width="21%"
			label="situacaoAprovacao"
			disabled="true"/>

		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.manterReajusteClienteAction.findClienteLookup"
			onDataLoadCallBack="lookupCliente"
			afterPopupSetValue="aferPopupCliente"
			onchange="return onChangeCliente(this);"
			exactMatch="true"
			dataType="text"			
			size="20"
			maxLength="20"
			width="46%"
			labelWidth="16%"
			required="true">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:textbox
				dataType="text"
				disabled="true"
				serializable="false"
				property="cliente.pessoa.nmPessoa"
				size="30" />
		</adsm:lookup>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:combobox
			label="divisao"
			property="divisaoCliente.idDivisaoCliente"
			optionProperty="idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			service="lms.vendas.manterReajusteClienteAction.findDivisaoClienteCombo"
			onchange="return onChangeDivisaoCliente(this);"
			labelWidth="17%"
			width="21%"
			boxWidth="140"
			required="true">
			<adsm:propertyMapping
				criteriaProperty="cliente.idCliente"
				modelProperty="cliente.idCliente" />
			<adsm:propertyMapping
				relatedProperty="divisaoCliente.dsDivisaoCliente"
				modelProperty="dsDivisaoCliente" />
		</adsm:combobox>

		<%---------------------%>
		<%-- TABELA TEXTBOX --%>
		<%---------------------%>	
		<adsm:textbox 
			label="tabela"
			dataType="text" 
			disabled="true" 
			property="tabelaPreco.tabelaPrecoStringDescricao" 
			size="60"
			width="45%"
			labelWidth="16%"
			>
			
		</adsm:textbox>
		<adsm:hidden property="tabelaDivisaoCliente.idTabelaDivisaoCliente" serializable="false"/>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
			label="tabelaNova"
			property="tabelaPreco"
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"
			service="lms.vendas.manterReajusteClienteAction.findTabelaPrecoLookup"
			action="/tabelaPrecos/manterTabelasPreco"
			onclickPicker="onclickPickerLookupTabelaPreco()"
			onDataLoadCallBack="dataLoadTabelaPreco"
			onPopupSetValue="validaTabelaPreco" 
			dataType="text"
			size="10"
			maxLength="9"
			width="46%"
			labelWidth="16%"
			required="true">
			<adsm:propertyMapping
				relatedProperty="tabelaPreco.dsDescricao"
				modelProperty="dsDescricao"/>

			<adsm:textbox
				dataType="text"
				property="tabelaPreco.dsDescricao"
				size="30"
				maxLength="30"
				disabled="true" />
		</adsm:lookup>

		<adsm:textbox
			dataType="JTDate"
			property="dtInicioVigencia"
			label="dataInicioVigencia"
			labelWidth="17%"
			width="21%"
			picker="true"
			required="true"
			 onchange="return onChangeDtVigenciaInicial(this);"/>

		<adsm:textbox 
			dataType="decimal"
			property="pcReajusteSugerido"
			label="percentualReajusteSugerido"
			labelWidth="16%"
			width="46%"
			mask="##0.00"
			onchange="return validatePercentual(this);"
			size="5"
			required="true"/>

		<adsm:textbox 
			dataType="decimal"
			property="pcReajusteAcordado"
			label="percentualReajusteTratado"
			labelWidth="17%"
			width="21%"
			mask="##0.00"
			onchange="return validatePercentual(this);"
			size="5"
			required="true"/>

		<adsm:section caption="reajusteParcelasFrete"/>
		<adsm:checkbox
			property="blMarcarDesmarcarTodos"
			labelWidth="16%"
			width="84%"
			label="marcarTodos"
			disabled="true"
			onclick="marcarDesmarcarCheckReajParcFrete();" />
		
		<adsm:checkbox
			property="blReajustaPercTde"
			labelWidth="16%"
			width="34%"
			label="percentualTde"
			disabled="true"/>
		
		<adsm:checkbox
			property="blReajustaAdValorEm"
			labelWidth="16%"
			width="34%"
			label="advalorem1"
			disabled="true"/>
					
		<adsm:checkbox
			property="blReajustaPercTrt"
			labelWidth="16%"
			width="34%"
			label="percentualTrt"
			disabled="true"/>

		<adsm:checkbox
			property="blReajustaAdValorEm2"
			labelWidth="16%"
			width="34%"
			label="advalorem2"
			disabled="true"/>			

		<adsm:checkbox
			property="blReajustaPercGris"
			labelWidth="16%"
			width="34%"
			label="percentualGris"
			disabled="true"/>
						
		<adsm:checkbox
			property="blReajustaFretePercentual"
			labelWidth="16%"
			width="34%"
			label="percentualFretePercentual"
			disabled="true"/>

		<adsm:textarea
			label="justificativa"
			maxLength="500"
			property="dsJustificativa"
			labelWidth="16%"
			width="84%"
			rows="5"
			columns="80" />

		<adsm:checkbox
			property="blEfetivado"
			labelWidth="16%"
			width="20%"
			label="efetivado"
			disabled="true"/>
			
		<adsm:checkbox
			property="blGerarSomenteMarcados"
			labelWidth="30%"
			width="34%"
			label="considerarParcSelecionadas"
			disabled="true"/>


		<adsm:buttonBar>
			<adsm:button 
				id="btnFluxoAprovacao" 
				caption="fluxoAprovacao"
				onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('pendenciaAprovacao.idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');"/>
			<adsm:button
				id="btnSalvar"
				caption="salvar"
				onclick="return onClickSalvar();" />
			<adsm:button 
				id="btnAprovacao" 
				caption="aprovacao"
				onclick="return onClickAprovacao();"/>
			<adsm:button 
				id="btnRetornoReajuste" 
				caption="retornoReajuste"
				onclick="return onClickRetornoReajuste();"/>
			<adsm:button 
				id="btnReajustar" 
				caption="reajustar"
				onclick="return onClickReajustarCliente();"/>
			<adsm:newButton id="btnLimpar"/>
			<adsm:button
				id="btnExcluir"
				caption="excluir"
				onclick="return onClickExcluir();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	
	var eventObj = "";


	function initWindow(eventObj) {
		changeFieldsStatus();
		disableWorkflow();
		eventObj = eventObj.name;
		if(eventObj.name == "newButton_click") {
			setFocusOnFirstFocusableField();
		}
	}

	function myOnPageLoad_cb() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined
				&& url.parameters.idProcessoWorkflow != undefined
				&& url.parameters.idProcessoWorkflow != '') {
			onDataLoad(url.parameters.idProcessoWorkflow);
		}
		onPageLoad_cb();
	}

	function formLoad_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		onDataLoad_cb(data, error);
		setTpSituacaoAprovacao(data);
		changeFieldsStatus();
		setElementValue("idReajusteCliente", data.idReajusteCliente);
        setElementValue("idDivisaoCliente", data.divisaoCliente.idDivisaoCliente);
        findTabelaDivisaoCliente();
		
		/* Se a proposta foi aberta a partir da tela de workflow. */
		var idProcessoWorkflow = getElementValue("idProcessoWorkflow");
		if (idProcessoWorkflow != "") {
			getTabGroup(document).getTab("pesq").setDisabled(true);
			disableWorkflow();
		}
	}

	function changeFieldsStatus() {
		if (getElementValue("idProcessoWorkflow") == "") {
			setDisabled(document, false);
			setDisabled("cliente.pessoa.nmPessoa", true);
			setDisabled("tabelaPreco.dsDescricao", true);
			setDisabled("nrReajuste", true);
			setDisabled("tpSituacaoAprovacao", true);
		}
		setDisabled("btnFluxoAprovacao", true);
		setDisabled("btnSalvar", false);
		setDisabled("btnAprovacao", false);
		setDisabled("btnRetornoReajuste", true);
		setDisabled("btnReajustar", true);
		setDisabled("btnLimpar", false);
		setDisabled("pcReajusteSugerido", true);
		setDisabled("tabelaPreco.tabelaPrecoStringDescricao", true);

		/* EditMode */
		if (hasValue(getElementValue("idReajusteCliente"))) {
			if(getElementValue("blEfetivado")) {
				setDisabled(document, true);
				setDisabled("btnFluxoAprovacao", false);
				setDisabled("btnLimpar", false);
				setFocus("btnLimpar", false);
			} else {
				if(getElementValue("tpSituacaoAprovacao.value") == "A") {
					setDisabled(document, true);
					setDisabled("btnFluxoAprovacao", false);
					if(getElementValue("pcReajusteAcordado") < 0){
						setDisabled("btnRetornoReajuste", false);
						setDisabled("btnReajustar", true);
					}else{
						setDisabled("btnReajustar", false);
						setDisabled("btnRetornoReajuste", true);
					}
					
					
					setDisabled("btnLimpar", false);
					setFocus("btnReajustar", false);
				} else if(getElementValue("tpSituacaoAprovacao.value") == "E") {
					setDisabled(document, true);
					setDisabled("btnFluxoAprovacao", false);
					setDisabled("btnLimpar", false);
					setFocus("btnFluxoAprovacao", false);
				}
				if (getElementValue("usuarioPerfilComercialParametrizacao") == "S") {
					setDisabled("dtInicioVigencia", false);
					setDisabled("btnSalvar", false);
				}
			}
			setDisabled("btnExcluir", false);
			setDisabled("tabelaPreco.idTabelaPreco", true);
			disabledFieldReajusteParcelaFrete();
		/* InsertMode */ 
		} else {
			setDisabled("btnFluxoAprovacao", true);
			setDisabled("btnAprovacao", true);
			setDisabled("btnRetornoReajuste", true);
			setDisabled("btnReajustar", true);
			setDisabled("btnExcluir", true);

			setDisabled("tabelaPreco.idTabelaPreco", true);
			resetFieldReajusteParcelaFrete();
		}
		setDisabled("blEfetivado", true);
	}

	function onClickSalvar() {
		if (validateTabScript(document.forms) && validatePercentualReajustado()) {
			var data = buildFormBeanFromForm(document.forms[0]);
			
			data["tabelaDivisaoCliente.idTabelaDivisaoCliente"] = getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente");
			
			var service = "lms.vendas.manterReajusteClienteAction.store";
			var sdo = createServiceDataObject(service, "afterStore", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	function afterStore_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		store_cb(data, error);

		setElementValue("nrReajuste", data.nrReajuste);
		setElementValue("idReajusteCliente", data.idReajusteCliente);
		setTpSituacaoAprovacao(data);
		changeFieldsStatus();
		showSuccessMessage();
		setFocus("btnAprovacao", false);
	}
	
	function onClickRetornoReajuste() {
		var service = "lms.vendas.manterReajusteClienteAction.efetuarReajusteCliente";
		var sdo = createServiceDataObject(service, "afterEfetuarReajusteCliente", {idReajusteCliente: getElementValue("idReajusteCliente")});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function onClickAprovacao() {
		var service = "lms.vendas.manterReajusteClienteAction.storePendenciaAprovacao";
		var sdo = createServiceDataObject(service, "afterAprovacao", {idReajusteCliente: getElementValue("idReajusteCliente")});
		xmit({serviceDataObjects:[sdo]});
	}
	function afterAprovacao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}

		/* Seta Pendencia caso tenha sido criada */
		if(data.pendenciaAprovacao != undefined) {
			setElementValue("pendenciaAprovacao.idPendencia", data.pendenciaAprovacao.idPendencia);
		}
		setTpSituacaoAprovacao(data);
		changeFieldsStatus();
		showSuccessMessage();
	}

	function setTpSituacaoAprovacao(data) {
		if (data.tpSituacaoAprovacao != undefined) {
			setElementValue("tpSituacaoAprovacao", data.tpSituacaoAprovacao.description);
			setElementValue("tpSituacaoAprovacao.value", data.tpSituacaoAprovacao.value);
		} else {
			setElementValue("tpSituacaoAprovacao", "");
			setElementValue("tpSituacaoAprovacao.value", "");
		}
	}

	function onClickReajustarCliente() {
		var service = "lms.vendas.manterReajusteClienteAction.agendarReajusteCliente";
		var sdo = createServiceDataObject(service, "afterEfetuarReajusteCliente", {idReajusteCliente: getElementValue("idReajusteCliente"), dtInicioVigencia: getElementValue("dtInicioVigencia")});
		xmit({serviceDataObjects:[sdo]});
	}
	function afterEfetuarReajusteCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		setElementValue("blEfetivado", data.blEfetivado == "true");
		changeFieldsStatus();
		showSuccessMessage();
	}

	function onClickExcluir() {
		if(getElementValue("blEfetivado")) {
			alert("LMS-01036 - " + getI18nMessage("LMS-01036"));
			return false;
		}
		removeButtonScript('lms.vendas.manterReajusteClienteAction.removeById', 'removeById', 'idReajusteCliente', this.document);
		return true;
	}

	/* Valida pedindo uma Justificativa caso pcReajusteAcordado seja menor que pcReajusteSugerido */
	function validatePercentualReajustado() {
		if(stringToNumber(getElementValue("pcReajusteAcordado")) < stringToNumber(getElementValue("pcReajusteSugerido"))) {
			if(!hasValue(getElementValue("dsJustificativa"))) {
				alertRequiredField("dsJustificativa", true);
				return false;
			}
		}
		return true;
	}

	function onChangeCliente(elem) {
		lookupChange({e:document.forms[0].elements["cliente.idCliente"]})
		if(!hasValue(getElementValue(elem))) {
			validateTabelaPrecoFields();
		}
		return true;
	}

	function lookupCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		/** valida se eh cliente Especial */
		if (data.length > 0) {
			if (!validateClienteEspecial(data[0])) {
				return;
			}
		}

		/** Popula dados e formata nrIdentificacao */
		var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data.length > 0) {
			setElementValue("cliente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
		}
		return retorno;
	}

	function aferPopupCliente(data) {
		if (validateClienteEspecial(data)) {
			setElementValue("cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
		}
	}

	function validateClienteEspecial(cliente) {
		if(cliente != undefined && cliente.tpCliente.value != "S") {
			alert("LMS-01033 - " + getI18nMessage("LMS-01033"));
			resetValue("cliente.idCliente");
			resetValue("cliente.pessoa.nmPessoa");
			setFocus("cliente.pessoa.nrIdentificacao", false);
			return false;
		}
		return true;
	}

	function onChangeDivisaoCliente(elem) {
		if(!hasValue(getElementValue(elem))) {
			validateTabelaPrecoFields();
			resetFieldTabela();
		} else {
			findTabelaDivisaoCliente();
		}
		
		setDisabled("tabelaPreco.idTabelaPreco", !hasValue(getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente")));
		return true;
	}

	function tabelaDivisaoClienteDataLoad_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		tabelaDivisaoCliente_idTabelaDivisaoCliente_cb(data);
		if (data.length == 1) {
				setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente", data[0].idTabelaDivisaoCliente);
		}
		validateTabelaPrecoFields();
	}

	function validateTabelaPrecoFields() {
		resetValue("tabelaPreco.idTabelaPreco");
		resetValue("tabelaPreco.tabelaPrecoString");
		resetValue("tabelaPreco.dsDescricao");
		resetValue("pcReajusteSugerido");
		resetValue("pcReajusteAcordado");
		setDisabled("tabelaPreco.idTabelaPreco", !hasValue(getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente")));
	}

	function onclickPickerLookupTabelaPreco() {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}

	function dataLoadTabelaPreco_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data != undefined && data.length == 1) {
			if (!validaTabelaPreco(data[0])) {
				return false; 
			}
		}
		return tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
	}

	/* Valida Tipo e Subtipo das tabelas de Pre�o */
	function validaTabelaPreco(data) {
		if(data == undefined) {
			return false;
		}
		// Valida TipoTabelaPreco
		var tpTipoTabelaPreco = data.tipoTabelaPreco.tpTipoTabelaPreco.value;
		var isValid = true;

		// Valida SubTipoTabelaPreco
		var tpSubtipoTabelaPreco = data.subtipoTabelaPreco.tpSubtipoTabelaPreco;
		if (tpSubtipoTabelaPreco == "F") {
			alert("LMS-01162 - " + getI18nMessage("LMS-01162"));
			isValid = false;
		}

		// Valida Tipo, SubTipo e nrVersao
		// Valida��o removida cfe. quest CQPRO00027254		

		var nrVersao = data.tipoTabelaPreco.nrVersao;
		
		/** Se houve Excecoes */
		if(!isValid) {
			resetValue("tabelaPreco.idTabelaPreco");
			resetValue("tabelaPreco.tabelaPrecoString");
			resetValue("tabelaPreco.dsDescricao");
			setFocus("tabelaPreco.tabelaPrecoString", false);
			return false;
		}

		/** Define PcReajusteSugerido conforme regra de neg�cio */
		definePcReajusteSugerido(stringToNumber(nrVersao), data.pcReajuste);
		return true;
	}

	function definePcReajusteSugerido(nrVersaoTabelaNova, pcReajuste) {
		var nrVersao = stringToNumber(getElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.nrVersao"));
		if(nrVersaoTabelaNova == nrVersao) {
			setFormattedValue("pcReajusteSugerido", "0");
			setFormattedValue("pcReajusteAcordado", "0");
		} else {
			var data = {tpTipoTabelaPreco: getElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco")
						,idTabelaPreco: getElementValue("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco")
						,nrVersao: nrVersaoTabelaNova};
			var service = "lms.vendas.manterReajusteClienteAction.calculaPcReajusteSugerido";
			var sdo = createServiceDataObject(service, "calculaPcReajusteSugerido", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	function calculaPcReajusteSugerido_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		setFormattedValue("pcReajusteSugerido", data.pcReajusteSugerido);
		setFormattedValue("pcReajusteAcordado", data.pcReajusteSugerido);
	}

	function setFormattedValue(property, value) {
		var field = getElement(property);
		setElementValue(field, setFormat(field, value));
	}

	function validatePercentual(field) {
		var vlPercentual = stringToNumber(getElementValue(field));
		if(vlPercentual < -100 || vlPercentual > 100) {
			alert("LMS-01050 - " + getI18nMessage("LMS-01050", field.label, false));
			return false;
		}
		return true;
	}

	function disableWorkflow() {
		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
		}
	}
	
	function onChangeDtVigenciaInicial(field){
		if (getElementValue(field).length == 10) {
			var dtVigenciaInicial = stringToDate(getElementValue(field),"yyyy-MM-dd");
			var hoje = new Date();
			var time1 = LZ(dtVigenciaInicial.getFullYear())+LZ(dtVigenciaInicial.getMonth())+LZ(dtVigenciaInicial.getDate());
			var time2 = LZ(hoje.getFullYear())+LZ(hoje.getMonth())+LZ(hoje.getDate());
			if (time1 <= time2){
				alert("LMS-29168 - " + getI18nMessage("LMS-29168", field.label, false));
				return false;
			}
		}
		return true;
	}
	
	function findTabelaDivisaoCliente() {
		var service = "lms.vendas.manterReajusteClienteAction.findTabelaDivisaoCliente";
		var data = new Array();

        var idDivisaoCliente = getElementValue("idDivisaoCliente");
        if(idDivisaoCliente === null || idDivisaoCliente === undefined || idDivisaoCliente === ""){
            idDivisaoCliente = getElementValue("divisaoCliente.idDivisaoCliente");
        }

        setNestedBeanPropertyValue(data, "divisaoCliente.idDivisaoCliente", idDivisaoCliente);
		setNestedBeanPropertyValue(data, "idReajusteCliente", getElementValue("idReajusteCliente"));
		setNestedBeanPropertyValue(data, "blEfetivado", getElementValue("blEfetivado"));
		var sdo = createServiceDataObject(service, "findTabelaDivisaoCliente", data);
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function findTabelaDivisaoCliente_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		
 		setElementValue("tabelaPreco.tabelaPrecoStringDescricao", data[0].tabelaPreco.tabelaPrecoStringDescricao);
 		setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente", data[0].idTabelaDivisaoCliente);
 		
 		setElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.nrVersao", data[0].tabelaPreco.tipoTabelaPreco.nrVersao);
 		setElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco", data[0].tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.value);
 		setElementValue("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco", data[0].tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco);
 		setElementValue("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco", data[0].tabelaPreco.idTabelaPreco);
 		
 		setDisabled("tabelaPreco.idTabelaPreco", !hasValue(getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente")));
	}
	
	function resetFieldTabela() {
		resetValue("tabelaPreco.tabelaPrecoStringDescricao");
		resetValue("tabelaDivisaoCliente.idTabelaDivisaoCliente");
	}
	
	function resetFieldReajusteParcelaFrete() {
		setElementValue("blReajustaPercTde", false);
		setElementValue("blReajustaPercTrt", false);
		setElementValue("blReajustaPercGris", true);
		setElementValue("blReajustaAdValorEm", true);
		setElementValue("blReajustaAdValorEm2", true);
		setElementValue("blReajustaFretePercentual", true);		
	}
	
	function disabledFieldReajusteParcelaFrete() {
		setDisabled("blMarcarDesmarcarTodos", true);
		setDisabled("blReajustaPercTde", true);
		setDisabled("blReajustaPercTrt", true);
		setDisabled("blReajustaPercGris", true);
		setDisabled("blReajustaAdValorEm", true);
		setDisabled("blReajustaAdValorEm2", true);
		setDisabled("blReajustaFretePercentual", true);
		setDisabled("blGerarSomenteMarcados", true);
	}
	
	function marcarDesmarcarCheckReajParcFrete() {
		if(getElementValue("blMarcarDesmarcarTodos") == true) {
			setElementValue("blReajustaPercTde", true);
			setElementValue("blReajustaPercTrt", true);
			setElementValue("blReajustaPercGris", true);
			setElementValue("blReajustaAdValorEm", true);
			setElementValue("blReajustaAdValorEm2", true);
			setElementValue("blReajustaFretePercentual", true);
		} else {
			setElementValue("blReajustaPercTde", false);
			setElementValue("blReajustaPercTrt", false);
			setElementValue("blReajustaPercGris", false);
			setElementValue("blReajustaAdValorEm", false);
			setElementValue("blReajustaAdValorEm2", false);
			setElementValue("blReajustaFretePercentual", false);			
		}
	}
</script>