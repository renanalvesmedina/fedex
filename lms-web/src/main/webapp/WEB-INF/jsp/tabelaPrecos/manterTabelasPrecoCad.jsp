<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.tabelaprecos.manterTabelasPrecoAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30027"/>
		<adsm:include key="LMS-04109"/>
		<adsm:include key="LMS-01172"/>
		<adsm:include key="LMS-30067"/>
		<adsm:include key="LMS-30071"/>
		
	</adsm:i18nLabels>
	<adsm:form onDataLoadCallBack="carregaSubtipos" action="/tabelaPrecos/manterTabelasPreco" idProperty="idTabelaPreco">
		<adsm:hidden property="tabelaPreco.idTabelaPreco"/>
		<adsm:hidden property="idTabelaPrecoVigente"/>
		<adsm:hidden property="blDesativaTabelaAntiga" value="false"/>
		<adsm:hidden property="mensagensImportacao"/>
		<adsm:hidden property="temPendenciaWorkflow"/>
		<adsm:hidden property="dsMotivoSolicitacao"/>
		<adsm:hidden property="idFilialSolicitacao"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="idPendencia" serializable="false"/>
		<adsm:hidden property="idPendenciaDesefetivacao" serializable="false"/>
		<adsm:hidden property="idPendenciaEfetivacao" serializable="false"/>
		<adsm:hidden property="temPendenciaAprovada" serializable="false"/>

		<adsm:complement label="tabelaBase" width="38%">
			<adsm:textbox
				dataType="text" serializable="false" property="tabelaPreco.tabelaPrecoString"
				size="8"
				maxLength="7"
				disabled="true"/>
			<adsm:textbox
				dataType="text" maxLength="60" property="tabelaPreco.dsDescricao"
				size="30" serializable="false" disabled="true"/>
		</adsm:complement>

		<adsm:combobox property="moeda.idMoeda" service="lms.configuracoes.moedaPaisService.findMoedaByPaisUsuarioLogado"
			optionLabelProperty="dsSimbolo" optionProperty="idMoeda" label="moeda"
			width="32%" required="true" onDataLoadCallBack="verificaMoedaPais"/>

		<adsm:combobox
			label="categoria"
			domain="DM_CATEGORIA_TABELA"
			property="tpCategoria" width="38%"
			onchange="onSelectCategoria(this);"/>

		<adsm:combobox
			label="tipoServico"
			domain="DM_TIPO_SERVICO_TABELA"
			property="tpServico" width="32%"/>

		<adsm:combobox onchange="carregaSubTipos(this); habilitaTabelaCustoTnt();" label="tipo" property="tipoTabelaPreco.idTipoTabelaPreco"
			service="lms.tabelaprecos.tipoTabelaPrecoService.findTipoTabelaPrecoAtivo" width="38%" required="true"
			optionProperty="idTipoTabelaPreco" optionLabelProperty="tpTipoTabelaPrecoNrVersao">
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.empresaByIdEmpresaCadastrada.idEmpresa"
				modelProperty="empresaByIdEmpresaCadastrada.idEmpresa"/>
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa"
				modelProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.cliente.pessoa.nmPessoa"
				modelProperty="cliente.pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.servico.idServico"
				modelProperty="servico.idServico"/>
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.servico.dsServico"
				modelProperty="servico.dsServico"/>
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.tpTipoTabelaPreco"
				modelProperty="tpTipoTabelaPreco"/>
			<adsm:propertyMapping relatedProperty="tipoTabelaPreco.servico.tpModal"
				modelProperty="servico.tpModal.value"/>
		</adsm:combobox>
		<adsm:hidden property="tipoTabelaPreco.servico.idServico"/>
		<adsm:hidden property="tipoTabelaPreco.servico.dsServico"/>
		<adsm:hidden property="tipoTabelaPreco.servico.tpModal"/>
		<adsm:hidden property="tipoTabelaPreco.tpTipoTabelaPreco"/>
		<adsm:combobox autoLoad="false" label="subTipo" property="subtipoTabelaPreco.idSubtipoTabelaPreco"
			service="lms.tabelaprecos.subtipoTabelaPrecoService.findByTpTipoTabelaPreco"
			width="32%" optionProperty="idSubtipoTabelaPreco"
			optionLabelProperty="tpSubtipoTabelaPreco" required="true">
			<adsm:propertyMapping relatedProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco"
				modelProperty="tpSubtipoTabelaPreco"/>
		</adsm:combobox>
		<adsm:hidden property="subtipoTabelaPreco.tpSubtipoTabelaPreco"/>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup 
		 	idProperty="idTabelaPreco"
			label="tabelaCustoTnt" 
			property="tabelaPrecoCustoTnt"
			service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
			action="/tabelaPrecos/manterTabelasPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text" 
			size="8"
			maxLength="7" 
			width="85%" 
			labelWidth="15%" >

			<adsm:propertyMapping modelProperty="dsDescricao" relatedProperty="tabelaPrecoCustoTnt.dsDescricao" />
			<adsm:propertyMapping relatedProperty="subtipoTabelaPreco.idSubtipoTabelaPreco" modelProperty="tabelaPrecoCustoTnt.subtipoTabelaPreco.idSubtipoTabelaPreco" />
			<adsm:propertyMapping relatedProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco" modelProperty="tabelaPrecoCustoTnt.subtipoTabelaPreco.tpSubtipoTabelaPreco" />
			<adsm:propertyMapping modelProperty="tpCategoria" criteriaProperty="tabelaPrecoCustoTnt.tpCategoria" />

			<adsm:textbox dataType="text" property="tabelaPrecoCustoTnt.dsDescricao" size="30" maxLength="30" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="tabelaPrecoCustoTnt.tpCategoria" value="T" serializable="false" />

		<adsm:hidden property="tipoTabelaPreco.empresaByIdEmpresaCadastrada.idEmpresa" serializable="true"/>
		<adsm:textbox label="empresa" serializable="false" property="tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa"
			dataType="text" disabled="true" size="30" width="38%" maxLength="60"/>

		<adsm:textbox dataType="text" property="dsDescricao" label="descricao" size="40" maxLength="60" width="32%"/>

		<adsm:textbox label="cliente" serializable="false" property="tipoTabelaPreco.cliente.pessoa.nmPessoa"
			dataType="text" disabled="true" size="30" width="38%" maxLength="60"/>

		<adsm:range label="vigencia" width="32%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:combobox property="tpCalculoFretePeso" label="tipoCalculo" domain="DM_CALCULO_FRETE_PESO" width="38%" required="true"/>

		<adsm:checkbox property="blEfetivada" label="efetivada" width="32%" disabled="true"/>

		<adsm:textbox dataType="weight" minValue="0.001" property="psMinimo" label="pesoMinimo"
			unit="kg" maxLength="18" size="10" width="38%"/>

		<adsm:textbox dataType="decimal" minValue="0.01" property="pcReajuste" mask="##0.00" label="percentualReajuste"
			maxLength="5" size="10" width="32%"/>

		<adsm:textbox serializable="false" dataType="JTDate" property="dtGeracao" label="dataGeracao" disabled="true"
			picker="false" width="38%"/>

		<adsm:combobox label="icmsDestacado" property="blIcmsDestacado" domain="DM_SIM_NAO" width="32%"/>

		<adsm:textbox dataType="decimal" minValue="0.00" maxValue="100.00" property="pcDescontoFreteMinimo" mask="##0.00" label="descontoFreteMinimo"
			maxLength="5" size="10" width="38%"/>

		<adsm:combobox label="TP_CALC_PEDAGIO" serializable="true" disabled="false" property="tpCalculoPedagio" domain="DM_TIPO_CALCULO_PEDAGIO" width="32%"/>

		<adsm:textarea
			property="obTabelaPreco"
			label="observacao"
			maxLength="4000"
			columns="80" rows="3"
			labelWidth="15%"
			width="85%"
			required="false"/>
			
			
		<adsm:textarea
			property="informacoesTabelaPreco"
			label="informacoes"
			maxLength="4000"
			columns="80" rows="3"
			labelWidth="15%"
			width="85%"
			required="false"/>
			
			
		<adsm:checkbox property="blImprimeTabela" label="imprimeTabela" width="32%"/>

		<adsm:buttonBar lines="3">
			<adsm:button id="btnMunicipiosTRT"  caption="manterTRT"     	onclick="municipioTRT()" />
			<adsm:button id="atualizacaoManual" caption="atualizacaoManual" onclick="atualizarTabelaCusto()" disabled="true"/>
			<adsm:button id="importar"   		caption="importar"   		onclick="importarPopup();" 		 disabled="true"/>
			<adsm:button id="dicionario" 		caption="dicionario" 		onclick="dicionarioTags()"/>

			<adsm:storeButton
				callbackProperty="storePrimeiraFase"
				disabled="true"
				id="storeButton"/>
			<adsm:button id="btnLimpar" caption="limpar" onclick="limpar()" boxWidth="70"/>
			<adsm:removeButton id="btnExcluir"/>
			<adsm:button id="btnTarifasXRotas" caption="tarifasXRotas" onclick="chamarTarifasXRotas()" boxWidth="120" disabled="true"/>
			<adsm:button id="btnSolEfetivacao" caption="solicitarEfetivacao" onclick="solicitarEfetivacao()" boxWidth="115" disabled="true"/>
			<adsm:button id="btnSolDesefetivacao" caption="solicitarDesefetivacao" onclick="solicitarDesefetivacao()" boxWidth="138" disabled="true"/>
			<adsm:button caption="cancelarWorkflow" id="btnCancelarWorkflow" onclick="cancelarWorkflow()" boxWidth="112" disabled="true"/>
			<adsm:button caption="solicitarAprovacao" id="btnSolicitarAprovacao" onclick="validateTarifaRota()" boxWidth="120" disabled="true"/>
			<adsm:button caption="reiniciarAlteracao" id="btnReiniciarAlteracao" onclick="reiniciarAlteracao()" boxWidth="120" disabled="true"/>
			<adsm:button caption="historicoWK" id="btnHistoricoWK" onclick="openModalHistoricoWorkflow()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function myOnPageLoad_cb() {
		onPageLoad_cb();
		retornoCarregaPagina_cb();
	}

	function openModalHistoricoWorkflow() {
		var data = getDadosTabela();

		var param = "&idProcesso=" + getElementValue("idTabelaPreco");
		param += "&nmTabela=TABELA_PRECO";
		param += "&tabelaPreco.tabelaPrecoString=" + data.sgTabelaPreco;
		param += "&tabelaPreco.dsDescricao=" + document.getElementById("dsDescricao").value;

		var url = '/workflow/historicoWorkflow.do?cmd=list' + param;
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;';
		showModalDialog(url, window, wProperties);
	}

	function retornoCarregaPagina_cb() {
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("parcela", false);
			tabGroup.setDisabledTab("anexo", false);			
			
	 	}		
	}

	function solicitarAprovacao() {
		solicitarWKGenerico("gerarPendenciaWorkflow");
	}

	function solicitarWKGenerico(tipoWorkflow) {
		var tpTipoTabelaPreco = getElementValue("tipoTabelaPreco.tpTipoTabelaPreco")
		setElementValue("dsMotivoSolicitacao", '');
		setElementValue("idFilialSolicitacao", '');

		var url = '/tabelaPrecos/manterTabelasPreco.do?cmd=motivoSolicitacao&tpTipoTabelaPreco=' + tpTipoTabelaPreco;
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';
		showModalDialog(url, window, wProperties);

		if (isMotivoSetado() && isFilialSolicitacaoSetada()) {
			var data = new Array();
			setNestedBeanPropertyValue(data, "dsMotivoSolicitacao", getElementValue("dsMotivoSolicitacao"));
			setNestedBeanPropertyValue(data, "idTabelaPreco", getElementValue("idTabelaPreco"));
			setNestedBeanPropertyValue(data, "idFilialSolicitacao", getElementValue("idFilialSolicitacao"));
			setNestedBeanPropertyValue(data, "dtVigenciaInicial", getElementValue("dtVigenciaInicial"));
			setNestedBeanPropertyValue(data, "dtVigenciaFinal", getElementValue("dtVigenciaFinal"));

			var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction." + tipoWorkflow,"validateTemPendenciaWK", data);
		    xmit({serviceDataObjects:[sdo]});
		}
	}

	function validateTarifaRota() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idTabelaPreco", getElementValue("idTabelaPreco"));
		var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction.findPossuiTarifaRota","validateTarifaRota", data);
	    xmit({serviceDataObjects:[sdo]});
	}

	function validateTarifaRota_cb(data, errorMsg) {
		if (errorMsg != undefined) {
			alert(errorMsg);
			return false;
		} else {
			solicitarAprovacao();
		}
	}

	function validateTemPendenciaWK_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
		if (errorMsg != undefined) {
			alert(errorMsg);
		} else {
			setElementValue('temPendenciaWorkflow', data.temPendenciaWorkflow);
			setElementValue('temPendenciaAprovada', data.temPendenciaAprovada);
			setElementValue('idPendencia', data.idPendencia);
			setElementValue('idPendenciaDesefetivacao', data.idPendenciaDesefetivacao);
			setElementValue('idPendenciaEfetivacao', data.idPendenciaEfetivacao);
			store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
			var efetivada = getElementValue("blEfetivada") + "";
			validateTemPendenciaWK(efetivada, data.possuiParcela);
		}
	}

	function cancelarWorkflow() {
		var msg = i18NLabel.getLabel("LMS-30067");
		var option = window.confirm(msg);
		if (option == false || option == null) {
			return;
		} else {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idTabelaPreco", getElementValue("idTabelaPreco"));
			var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction.cancelWorkflow","cancelWorkflow", data);
		    xmit({serviceDataObjects:[sdo]});
		}
	}

	function reiniciarAlteracao() {
		var msg = i18NLabel.getLabel("LMS-30067");
		var option = window.confirm(msg);
		if (option == false || option == null) {
			return;
		} else {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idTabelaPreco", getElementValue("idTabelaPreco"));
			var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction.reiniciarAlteracao","validateTemPendenciaWK", data);
		    xmit({serviceDataObjects:[sdo]});
		}
	}

	function cancelWorkflow_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
		if (errorMsg != undefined) {
			alert(errorMsg);
		} else {
			setElementValue("temPendenciaWorkflow", "false");
			setElementValue("idPendencia", "");
			store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
			validateTemPendenciaWK("false", "true");
		}
	}

	function isMotivoSetado() {
		txtMotivo = getElementValue("dsMotivoSolicitacao");
		if (txtMotivo != null && txtMotivo != '') {
			return true;
		} return false;
	}

	function isFilialSolicitacaoSetada() {
		idFilialSolicitacao = getElementValue("idFilialSolicitacao");
		if (idFilialSolicitacao != null && idFilialSolicitacao != '') {
			return true;
		} return false;
	}

	function atualizarTabelaCusto() {
		if (confirm('Deseja realmente atualizar as tabelas de custo TNT?')) {
			storeButtonScript('lms.tabelaprecos.manterTabelasPrecoAction.atualizarTabelaCustoTnt', 'atualizarTabelaCusto', document.getElementById("form_idTabelaPreco"));
		}
	}

	function atualizarTabelaCusto_cb(data, error) {
		if (error!=undefined) {
			alert(error);
		} else if (data.retornoAtualiza) {
			alert(data.retornoAtualiza);
		}
	}

	function disabledAtualizacaoAutomatica(){
	    var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction.findAtualizacaoAutomatica","setaBotaoAtualizacaoManual");
	    xmit({serviceDataObjects:[sdo]});
	}

	function setaBotaoAtualizacaoManual_cb(data,error){
		var idTabelaPreco = getElementValue("idTabelaPreco");
		var tpCategoria = getElementValue("tpCategoria");
		var tpServico = getElementValue("tpServico");
		var blAtualizaAutomatica = (data.atualizacaoAutomatica == "true") ? true : false;

		if(blAtualizaAutomatica == false && idTabelaPreco && tpCategoria && tpCategoria == "A" && tpServico && tpServico != "M") {
			setDisabled("atualizacaoManual", false);
		}else{
			setDisabled("atualizacaoManual", true);
		}
	}

	function importarPopup() {
		var url = 'tabelaPrecos/manterTabelasPrecoPopupErro.do?cmd=main&idTabelaPreco=' + getElementValue("idTabelaPreco");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:630px;dialogHeight:320px;');
		if(data && data.action == "retornoImportar") {
			setDisabled("importar", true);
	}
	}

	function validateTab() {
		return validateTabScript(document.forms) && validateTipoCalculo();
	}

	function validateTipoCalculo() {
		var psMinimo = getElement("psMinimo");
		var tpCalculo = getElementValue("tpCalculoFretePeso");
		if(tpCalculo == "E" && psMinimo.value == "") {
			alertI18nMessage("LMS-04109", psMinimo.label,false);
			setFocusOnFirstFocusableField();
			return false;
		}
		return true;
	}

	function solicitarEfetivacao(){
		var dtVig = document.getElementById("dtVigenciaInicial");
		dtVig.required = "true";

		var valid = true;
		var tab = getTab(document.getElementById("form_idTabelaPreco").document);

		if (tab != null) {
			valid = tab.validate({name:"storeButton_click"});
		}
		dtVig.required = "false";

		if(valid == true){
			solicitarWKGenerico("gerarPendenciaEfetivacao");
		}
	}

	function storePrimeiraFase_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
		if(errorKey) {
			if (errorKey == "LMS-30027") {
				var ret = window.confirm(errorMsg, "");
				if (ret == true) {
					setElementValue("blDesativaTabelaAntiga", "true");
					setElementValue("idTabelaPrecoVigente", data._value);
					storeButtonScript("lms.tabelaprecos.manterTabelasPrecoAction.store", "storeAtualizacao", document.getElementById("form_idTabelaPreco"));
					return true;
				} else {
					setElementValue("blDesativaTabelaAntiga", "false");
					setElementValue("blEfetivada", "false");
					return false;
				}
			}
			alert(errorMsg);
		} else {
			var blPossueParcela = data.blPossuiParcela;
			if(manterTabelasPrecoCadOnDataLoadData){
				manterTabelasPrecoCadOnDataLoadData.blPossueParcela = data.blPossuiParcela;
			}
			store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
			if (data.msgAtualizacaoAutomatica != undefined){
				alert(data.msgAtualizacaoAutomatica);
			}
			setElementValue("temPendenciaWorkflow", data.temPendenciaWorkflow);

			desabilitaImportacao(data.blEfetivada, blPossueParcela, "false");
			validateTemPendenciaWK(data.blEfetivada, blPossueParcela);
			desabilitaCampos(data.blEfetivada);
			setFocus("btnLimpar",false);
		}
	}

	function storeAtualizacao_cb(data, erros){
		if (data.msgAtualizacaoAutomatica != undefined){
			alert(data.msgAtualizacaoAutomatica)
		}
		setElementValue("temPendenciaWorkflow", data.temPendenciaWorkflow);
		desabilitaImportacao(data.blEfetivada, manterTabelasPrecoCadOnDataLoadData.blPossueParcela, "false");
		validateTemPendenciaWK(data.blEfetivada, manterTabelasPrecoCadOnDataLoadData.blPossueParcela);
		desabilitaCampos(data.blEfetivada);
		store_cb(data);
	}


	function carregaSubTipos(e) {
		if(e.selectedIndex > 0) {
			var v = getNestedBeanPropertyValue(e.data, ":" + (e.selectedIndex - 1) + ".tpTipoTabelaPreco.value");
			var sdo = createServiceDataObject("lms.tabelaprecos.subtipoTabelaPrecoService.findByTpTipoTabelaPreco", "subtipoTabelaPreco.idSubtipoTabelaPreco", {tpTipoTabelaPreco:v});
			xmit({serviceDataObjects:[sdo]});
		} else {
			limpaComboSubtipos();
		}
		comboboxChange({e:e});
	}

	function limpaComboSubtipos(){
		var combo = document.getElementById("subtipoTabelaPreco.idSubtipoTabelaPreco");
		for(var i = combo.options.length; i >= 1; i--) {
			combo.options.remove(i);
		}
		combo.selectedIndex = 0
	}

	function getDadosTabela() {
		var e = getElement("tipoTabelaPreco.idTipoTabelaPreco");
		var tpTipoTabelaPreco = e.options[e.selectedIndex].text.substring(0, 1);
		var sgTabelaPreco = e.options[e.selectedIndex].text + "-";
		e = getElement("subtipoTabelaPreco.idSubtipoTabelaPreco");
		sgTabelaPreco += e.options[e.selectedIndex].text;

		e = getElement("moeda.idMoeda");
		var idMoeda = e.options[e.selectedIndex].value;
		var sgMoeda = e.options[e.selectedIndex].text;

		var data = {sgTabelaPreco:sgTabelaPreco,
					tpTipoTabelaPreco:tpTipoTabelaPreco,
					dsTabelaPreco:getElementValue("dsDescricao"),
					blEfetivada:getElementValue("blEfetivada"),
					idPendencia:getElementValue("idPendencia"),
					idPendenciaEfetivacao:getElementValue("idPendenciaEfetivacao"),
					idPendenciaDesefetivacao:getElementValue("idPendenciaDesefetivacao"),
					idTabelaPreco:getElementValue("idTabelaPreco"),
					moeda:{idMoeda:idMoeda, sgMoeda:sgMoeda}};
		return data;
	}

	var manterTabelasPrecoCadOnDataLoadData;
	var manterTabelasPrecoCadOnDataLoadError;
	function carregaSubtipos_cb(dados, erros){
		manterTabelasPrecoCadOnDataLoadData = dados;
		manterTabelasPrecoCadOnDataLoadError = erros;
		var v = getNestedBeanPropertyValue(dados, "tipoTabelaPreco.tpTipoTabelaPreco.value");
		var sdo = createServiceDataObject("lms.tabelaprecos.subtipoTabelaPrecoService.findByTpTipoTabelaPreco", "configuraSubtipo", {tpTipoTabelaPreco:v});
		xmit({serviceDataObjects:[sdo]});
	}

	function configuraSubtipo_cb(dados, erros){
		subtipoTabelaPreco_idSubtipoTabelaPreco_cb(dados);
		onDataLoad_cb(manterTabelasPrecoCadOnDataLoadData, manterTabelasPrecoCadOnDataLoadError);

		var nova = getElementValue("idTabelaPreco") == undefined ? true : false;
		var pussueParcelas = manterTabelasPrecoCadOnDataLoadData.blPossueParcela;
		var efetivada = manterTabelasPrecoCadOnDataLoadData.blEfetivada;
		desabilitaImportacao(efetivada, pussueParcelas, nova);
		disabledAtualizacaoAutomatica();
		handleButtonsWK(efetivada, pussueParcelas);
		desabilitaCampos(manterTabelasPrecoCadOnDataLoadData.blEfetivada);
		disableButtonsWK();
	}

	function handleButtonsWK(efetivada, possuiParcelas) {
		if (!hasValue(getElementValue("idTabelaPreco"))) {
			setDisabled("btnSolicitarAprovacao", true);
			setDisabled("btnSolEfetivacao", true);
			setDisabled("btnSolDesefetivacao", true);
			setDisabled("btnReiniciarAlteracao", true);
			setDisabled("btnCancelarWorkflow", true);
			return;
		}

		validateTemPendenciaWK(efetivada, possuiParcelas);
	}

	function validateTemPendenciaWK(efetivada, possuiParcelas) {
		setDisabled("btnCancelarWorkflow", true);
		setDisabled("btnSolDesefetivacao", true);
		setDisabled("btnReiniciarAlteracao", true);
		setDisabled("btnSolicitarAprovacao", true);
		setDisabled("btnSolEfetivacao", true);

		if (getElementValue("idPendenciaEfetivacao") != "") {
			setDisabled("importar", true);
			setDisabled("storeButton", true);
			setDisabled("btnLimpar", true);
			setDisabled("atualizacaoManual", true);
			return;
		}
		if (getElementValue("temPendenciaWorkflow") == "true") {
			setDisabled("btnCancelarWorkflow", false);
			setDisabled("importar", true);
			setDisabled("storeButton", true);
			setDisabled("btnLimpar", true);
			setDisabled("atualizacaoManual", true);
			return;
		}

		if(efetivada == "false"){
			if (getElementValue("temPendenciaAprovada") == "true"
					&& getElementValue("idPendenciaEfetivacao") == "") {
				setDisabled("btnReiniciarAlteracao", false);
				setDisabled("btnSolEfetivacao", false);
			}
			if (possuiParcelas == "true") {
				setDisabled("btnSolicitarAprovacao", false);
			}
		} else {
			if (getElementValue("idPendenciaDesefetivacao") == "") {
				setDisabled("btnSolDesefetivacao", false);
			}
		}

		if (getElementValue("idPendencia") != "") {
			setDisabled("storeButton", true);
			setDisabled("btnSolicitarAprovacao", true);
		}
	}

	function disableTabParcela(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("parcela", disabled);
		desabilitaImportacao(false, false, true);
	}

	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}

	var moedas = undefined;
	var moedaDefault = undefined;
	function initWindow(eventObj) {
		var event = eventObj.name;
		var tabGroup = getTabGroup(this.document);
		var idTab = tabGroup.oldSelectedTab.properties.id ;
		if(event != "tab_click" && idTab != "parcela"){
			if((event != "storeButton" && event != "gridRow_click")) {
				habilitaCampos();
				setElementValue("tpCalculoFretePeso","Q");

				if(idTab != "parcela" || event != "tab_click") {
					if(moedaDefault)
						setElementValue("moeda.idMoeda", moedaDefault);
					disableTabParcela(true);
					disableTabAnexos(true);
					limpaComboSubtipos();
					getElement("dtVigenciaInicial").required = "false";
				}
			} else {
				disableTabParcela(false);
				disableTabAnexos(false);
			}
		} else if(event == "tab_click" && idTab == "pesq"){
			habilitaCampos();
			setElementValue("tpCalculoFretePeso","Q");
			handleButtonsWK("false", "false");
		} else if(event == "removeButton") {
			habilitaCampos();
			setElementValue("tpCalculoFretePeso","Q");
			disableTabParcela(true);
			disableTabAnexos(true);
		} else if(event == "storeButton") {
			disableTabParcela(false);
			disableTabAnexos(false);
		}

		if(event == "tab_click" && (idTab == "parcela" || idTab == "anexo")){
			var nova = getElementValue("idTabelaPreco") == undefined ? true : false;
			var possuiParcelas = "false";
			if(manterTabelasPrecoCadOnDataLoadData){
				possuiParcelas = hasParcelas();
			}
			var efetivada = getElementValue("blEfetivada");
			desabilitaImportacao(efetivada, possuiParcelas, nova);
			handleButtonsWK(efetivada + '', possuiParcelas);
		}
		disabledAtualizacaoAutomatica();
		disableButtonsWK();
		if (event == "storeButton") {
			var efetivada = getElementValue("blEfetivada");
			possuiParcelas = manterTabelasPrecoCadOnDataLoadData.blPossueParcela;
			handleButtonsWK(efetivada + '', possuiParcelas + "");
		}
	}

	function hasParcelas() {
		var tabGroup = getTabGroup(this.document);
		var tabParcela = tabGroup.getTab("parcela");
		var abaParcela = tabParcela.tabOwnerFrame.window;
		if(abaParcela.parcelaPrecoGridDef.gridState.rowCount > 0) {
			return "true";
		} return "false";
	}

	function verificaMoedaPais_cb(dados) {
		moeda_idMoeda_cb(dados);
		moedas = dados;
		if (dados) {
			for ( var i = 0; i < dados.length; i++) {
				var indUtil = getNestedBeanPropertyValue(dados[i],
						"blIndicadorMaisUtilizada");
				if (indUtil == true || indUtil == 'true') {
					moedaDefault = getNestedBeanPropertyValue(dados[i],
							"idMoeda");
					setElementValue("moeda.idMoeda", moedaDefault);
					return;
				}
			}
		}
	}

	function desabilitaImportacao(efetivada, pussueParcelas, nova) {
		var blEfetivada = (efetivada == "true") ? true : false;
		var blPessueParcelas = (pussueParcelas == "true") ? true : false;
		var blNova = (nova == "true") ? true : false;

		setDisabled("importar", false);
		if (blEfetivada == true || blPessueParcelas == true || blNova == true) {
			setDisabled("importar", true);
		}
	}

	/* Verifica os campos que devem ser desabilitados
	 * caso a tebela esteja efetivada.
	 */
	function desabilitaCampos(blEfetivada) {
		var blDisabled = (blEfetivada == "true") ? true : false;
		setDisabled("moeda.idMoeda", blDisabled);
		setDisabled("tpCalculoFretePeso", blDisabled);
		setDisabled("dtVigenciaInicial", blDisabled);
		setDisabled("psMinimo", blDisabled);
		setDisabled("dsDescricao", blDisabled);
		setDisabled("tipoTabelaPreco.idTipoTabelaPreco", true);
		setDisabled("subtipoTabelaPreco.idSubtipoTabelaPreco", true);
		setDisabled("pcReajuste", true);
		setDisabled("pcDescontoFreteMinimo", blDisabled);
		setDisabled("tpCategoria", true);
		setDisabled("tpServico", true);
		setDisabled("tabelaPrecoCustoTnt.idTabelaPreco", true);
		setDisabled("blIcmsDestacado", blDisabled);

	}

	function habilitaCampos() {
		setDisabled("moeda.idMoeda", false);
		setDisabled("tpCalculoFretePeso", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("psMinimo", false);
		setDisabled("dsDescricao", false);
		setDisabled("tipoTabelaPreco.idTipoTabelaPreco", false);
		setDisabled("subtipoTabelaPreco.idSubtipoTabelaPreco", false);
		setDisabled("pcReajuste", false);
		setDisabled("pcDescontoFreteMinimo", false);
		setDisabled("btnLimpar", false);
		setDisabled("dicionario", false);
		setDisabled("importar", true);
		setDisabled("tpCategoria", false);
		setDisabled("tpServico", false);
		setDisabled("tabelaPrecoCustoTnt.idTabelaPreco", true);
		setDisabled("blIcmsDestacado", false);
	}

	function limpar() {
		disableTabParcela(true);
		disableTabAnexos(true);
		habilitaCampos();
		newButtonScript();
		setElementValue("tpCalculoFretePeso", "Q");		
	}

	function dicionarioTags() {
		showModalDialog(
				'tabelaPrecos/consultarTagsTabelaPreco.do?cmd=main',
				window,
				'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}

	function onSelectCategoria(e) {
		if (e.selectedIndex > 0) {
			getElement("tpServico").required = "true";
		} else {
			getElement("tpServico").required = "false";
		}
	}

	function chamarTarifasXRotas() {
		var url = "tabelaPrecos/manterTarifasPrecoRotas.do";
		isEfetivada = document.getElementById("blEfetivada").checked;
		var data = getDadosTabela();
		var params = "?cmd=main";
		params += "&sgTabelaPreco=" + data.sgTabelaPreco;
		params += "&idPendencia=" + data.idPendencia + data.idPendenciaEfetivacao + data.idPendenciaDesefetivacao;
		params += "&idTabelaPreco=" + data.idTabelaPreco;
		params += "&isEfetivada=" + isEfetivada;
		params += "&dsTabelaPreco=" + data.dsTabelaPreco;

		redirectTarifaXRota(url + params);
	}

	function redirectTarifaXRota(url) {
	 	if (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != '') {
			url += "&idProcessoWorkflow=" + getIdProcessoWorkflow();
			showModalDialog(url, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		} else {
			parent.parent.redirectPage(url);
		}
	}
	
	function municipioTRT(){
		var url = "vendas/manterTrtCliente.do?cmd=main";
		var data = getDadosTabela();
		
		var e = getElement("tipoTabelaPreco.idTipoTabelaPreco");		
		var tipo = e.options[e.selectedIndex].text; 
	
		var subTipo = document.getElementById("subtipoTabelaPreco.tpSubtipoTabelaPreco").value;
		var desc    = document.getElementById("dsDescricao").value;
		
		url += "&idTabelaPreco=" + data.idTabelaPreco;
		url += "&tabelaPrecoDesc="+ tipo + "-" + subTipo + " - " + desc;		
		
		parent.parent.redirectPage(url);
	}

	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}

	function disableButtonsWK() {
		if (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != '') {
			setDisabled(document, true);
			setDisabled("storeButton", true);
			setDisabled("btnLimpar", true);
			setDisabled("btnTarifasXRotas", false);
		}
	}

	function solicitarDesefetivacao() {
		solicitarWKGenerico("gerarPendenciaDesefetivacao");
	}
	
	function onChangeTabelaPreco() {
		var result = tabelaPreco_tabelaPrecoStringOnChangeHandler();

		return result;
	}	
	
	function habilitaTabelaCustoTnt() {
		var tpModal = getElementValue('tipoTabelaPreco.servico.tpModal');
		
		if(tpModal !== '' && tpModal === 'A'){
			setDisabled("tabelaPrecoCustoTnt.idTabelaPreco", false);
		} else {
			setDisabled("tabelaPrecoCustoTnt.idTabelaPreco", true);
		}
	}
</script>
