<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterAnaliseCreditoClienteAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01164"/>
		<adsm:include key="LMS-01165"/>
		<adsm:include key="requiredField"/>
		<adsm:include key="periodicidadeFatAprovada"/>
		<adsm:include key="prazoAprovado"/>
	</adsm:i18nLabels>
	<adsm:form
		action="/vendas/manterAnaliseCreditoCliente"
		idProperty="idAnaliseCreditoCliente"
		onDataLoadCallBack="myOnDataLoadCallBack">

		<adsm:hidden property="userCanAccess" value="false" />

		<!-- Periodo de solicitacao -->
		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhSolicitacao"
			label="dataHoraSolicitacao"
			labelWidth="18%"
			width="32%"
			picker="false"
			disabled="true"/>

		<!-- Lookup de filial -->
		<adsm:lookup
			label="filialComercial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais" 
			service="lms.vendas.manterAnaliseCreditoClienteAction.findFilialLookup"
			dataType="text"
			labelWidth="10%"
			width="40%"
			size="3" 
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			disabled="true">
			<adsm:propertyMapping
				relatedProperty="filial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping
				relatedProperty="filial.sgFilial"
				modelProperty="sgFilial"/>

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				serializable="false"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<!-- Lookup de funcionario -->
		<adsm:lookup
			property="usuario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			service="lms.vendas.manterAnaliseCreditoClienteAction.findFuncionarioLookup" 
			dataType="text" label="usuarioSolicitante" size="16" maxLength="16"
			labelWidth="18%" 
			width="82%" action="/configuracoes/consultarFuncionariosView"
			disabled="true">
			<adsm:propertyMapping
				relatedProperty="usuario.nmUsuario"
				modelProperty="nmUsuario"/>

			<adsm:textbox
				dataType="text"
				property="usuario.nmUsuario"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<!-- Lookup de cliente -->
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			criteriaProperty="pessoa.nrIdentificacao" 
			exactMatch="true" 
			idProperty="idCliente" 
			property="cliente" 
			label="cliente" 
			size="20"
			maxLength="20"
			width="42%" 
			labelWidth="18%"
			service="lms.vendas.manterAnaliseCreditoClienteAction.findClienteLookup"
			disabled="true">
			<adsm:propertyMapping 
				modelProperty="pessoa.nmPessoa" 
				relatedProperty="cliente.pessoa.nmPessoa" />

			<adsm:textbox 
				dataType="text" 
				serializable="false"
				property="cliente.pessoa.nmPessoa" 
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<!-- Combo de situacao do Cliente -->
		<adsm:combobox 
			property="tpSituacaoCliente" 
			domain="DM_STATUS_PESSOA" 
			label="situacao" 
			labelWidth="8%" 
			width="32%"
			disabled="true"
			serializable="false"
			renderOptions="true"/>

		<!-- Combo de situacao -->
		<adsm:combobox 
			property="tpSituacao" 
			domain="DM_SITUACAO_ANALISE_CREDITO" 
			label="situacaoAnalise" 
			labelWidth="18%" 
			width="32%"
			boxWidth="240"
			disabled="true"
			renderOptions="true"/>

		<!-- Data de conclusao -->
		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhConclusao"
			label="dataHoraConclusao"
			labelWidth="18%"
			width="32%"
			picker="false"
			disabled="true"/>

		<!-- Combo segmento de mercado -->
		<adsm:combobox
			property="segmentoMercado.idSegmentoMercado"
			onlyActiveValues="true"
			optionLabelProperty="dsSegmentoMercado"
			optionProperty="idSegmentoMercado"
			service="lms.vendas.manterClienteAction.findSegmentoMercado"
			label="segmentoMercado"
			labelWidth="18%"
			width="32%"
			boxWidth="240"
			disabled="true"
			renderOptions="true"
		/>

		<!-- Faturamento previsto -->
		<adsm:combobox
			label="faturamentoPrevisto"
			onlyActiveValues="true"
			property="moedaByIdMoedaFatPrev.idMoeda"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			service="lms.vendas.manterClienteAction.findMoedaPaisCombo"
			labelWidth="18%"
			width="32%"
			boxWidth="65"
			autoLoad="false"
			disabled="true"
			renderOptions="true">

			<adsm:textbox
				dataType="currency"
				property="vlFaturamentoPrevisto"
				size="10"
				maxLength="18"
				disabled="true"/>		
		</adsm:combobox>

		<!-- Combo tipo cobrança solicitado -->
		<adsm:combobox
			property="tpCobrancaSolicitado"
			domain="DM_TIPO_COBRANCA"
			label="tipoCobrancaSolicitado"
			labelWidth="18%"
			width="32%"
			renderOptions="true"
			disabled="true"/>

		<!-- Combo tipo cobrança aprovado -->
		<adsm:combobox
			property="tpCobrancaAprovado"
			domain="DM_TIPO_COBRANCA"
			label="tipoCobrancaAprovado"
			labelWidth="18%"
			width="32%"
			renderOptions="true"/>

		<!-- Limite de crédito do cliente -->
		<adsm:combobox
			label="valorLimiteCredito"
			onlyActiveValues="true"
			property="moedaByIdMoedaLimCred.idMoeda"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			service="lms.vendas.manterClienteAction.findMoedaPaisCombo"
			labelWidth="18%"
			width="82%"
			boxWidth="70"
			autoLoad="false"
			renderOptions="true">
			<adsm:textbox 
				dataType="currency"
				property="vlLimiteCredito"
				size="10"
				maxLength="18"/>
		</adsm:combobox>

		<%--------------------------------%>
		<%-----------FATURAMENTO----------%>
		<%--------------------------------%>
		<adsm:section caption="faturamento"/>
		<adsm:grid
			idProperty="idDiaFaturamento"
			property="faturamentoCliente"
			onDataLoadCallBack="gridFaturamentoCallBack"
			selectionMode="none"
			scrollBars="vertical"
			autoSearch="false"
			showGotoBox="false"
			showPagging="false"
			showTotalPageCount="false"
			onRowClick="onRowClick"
			gridHeight="55"
			unique="true"
			rows="3">
			<adsm:gridColumn 
				title="divisao" 
				property="divisaoCliente.dsDivisaoCliente"
				width="15%" />
			<adsm:gridColumn
				title="modal"
				property="tpModal"
				isDomain="true"
				width="15%"/>
			<adsm:gridColumn
				title="abrangencia"
				property="tpAbrangencia"
				isDomain="true"
				width="15%"/>
			<adsm:gridColumn
				title="tipoFrete"
				property="tpFrete"
				isDomain="true"
				width="15%"/>
			<adsm:gridColumn
				title="servico"
				property="servico.dsServico"
				width="20%"/>
			<adsm:gridColumn 
				title="periodicidadeFatSolicitada" 
				property="tpPeriodicidadeSolicitado"
				isDomain="true"
				width="20%" />
			<adsm:editColumn
				title="periodicidadeFatAprovada" 
				property="tpPeriodicidadeAprovado"
				domain="DM_PERIODICIDADE_FATURAMENTO"
				field="combobox"
				width="100"/>
		</adsm:grid>

		<%--------------------------------------%>
		<%-----------PRAZOS PAGAMENTO ----------%>
		<%--------------------------------------%>
		<adsm:section caption="prazoPagamento"/>
		<adsm:grid
			idProperty="idPrazoVencimento"
			property="prazoPagamentoCliente"
			selectionMode="none"
			scrollBars="vertical"
			autoSearch="false"
			showGotoBox="false"
			showPagging="false"
			showTotalPageCount="false"
			onRowClick="onRowClick"
			gridHeight="55"
			unique="true"
			rows="3">
			<adsm:gridColumn 
				title="divisao" 
				property="divisaoCliente.dsDivisaoCliente"
				width="15%" />
			<adsm:gridColumn
				title="modal"
				property="tpModal"
				isDomain="true"
				width="15%"/>
			<adsm:gridColumn
				title="abrangencia"
				property="tpAbrangencia"
				isDomain="true"
				width="15%"/>
			<adsm:gridColumn
				title="tipoFrete"
				property="tpFrete"
				isDomain="true"
				width="15%"/>
			<adsm:gridColumn
				title="servico"
				property="servico.dsServico"
				width="20%"/>
			<adsm:gridColumn 
				title="prazoSolicitado" 
				property="nrPrazoPagamentoSolicitado"
				dataType="integer"
				width="20%" />
			<adsm:editColumn
				title="prazoAprovado" 
				property="nrPrazoPagamentoAprovado"
				dataType="integer"
				field="textbox"
				maxLength="18"
				width="65"/>
			<adsm:gridColumn 
				title="" 
				property="descricaoDias" 
				width="35" 
				align="right"/>
		</adsm:grid>

		<!-- Arquivo PDF Serasa -->
		<adsm:textbox
			label="pdfSerasa"
			property="imPdfSerasa"
			blobColumnName="IM_PDF_SERASA"
			tableName="SERASA_CLIENTE"
			primaryKeyColumnName="ID_SERASA_CLIENTE"
			primaryKeyValueProperty="idSerasaCliente"
			dataType="picture"
			labelWidth="10%"
			width="30%"/>
		<adsm:hidden property="idSerasaCliente" />

		<!-- Data de ultima consulta -->
		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhUltimaConsultaSerasa"
			label="ultimaConsultaSerasa"
			labelWidth="17%"
			width="25%"/>

		<!-- Credito liberado -->
		<adsm:checkbox
			label="creditoLiberado"
			property="blCreditoLiberado"
			labelWidth="12%"
			width="3%"/>

		<adsm:buttonBar>
			<adsm:button id="btnManterCliente" caption="manterClientesAccess" action="/vendas/manterDadosIdentificacao" cmd="main">
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="false"/>
			</adsm:button>
			<adsm:button 
				id="btnHistorico" 
				caption="historico"
				onclick="openHistoricoPopup();"/>
			<adsm:button
				id="btnConcluirAnalise"
				caption="concluirAnalise"
				onclick="return validateConclusao();" />
			<adsm:button 
				id="btnAprovacaoGerente" 
				caption="aprovacaoGerente"
				onclick="return onClickAprovacaoGerente();"/>
			<adsm:button 
				id="btnAprovacaoDiretor" 
				caption="aprovacaoDiretor"
				onclick="return onClickAprovacaoDiretor();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	/** Seta label para alerta de campo obrigatório */
	getElement("vlLimiteCredito").label = getElement("moedaByIdMoedaLimCred.idMoeda").label;
	setDisplay("filial_lupa", false);
	setDisplay("usuario_lupa", false);
	setDisplay("cliente_lupa", false);

	function initWindow(eventObj) {
		validateButtons();
		/** Para execução automatica da listagem caso a mesma esteja ativa */
		parent.document.frames["pesq_iframe"].stopCurrentTimer();
	}

	function myOnPageLoad_cb() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined && hasValue(url.parameters.idAnaliseCreditoCliente)) {
			getTabGroup(document).getTab("pesq").setDisabled(true);
			onDataLoad(url.parameters.idAnaliseCreditoCliente);
		}
		onPageLoad_cb();
	}

	function myOnDataLoadCallBack_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}

		onDataLoad_cb(data, error);
		faturamentoClienteGridDef._resetGridStatsObject();
		prazoPagamentoClienteGridDef._resetGridStatsObject();
		faturamentoClienteGridDef.onDataLoad_cb(data.diasFaturamento);
		prazoPagamentoClienteGridDef.onDataLoad_cb(data.prazosVencimento);

		validateButtons();
	}

	function gridFaturamentoCallBack_cb(data, error) {
		if(data != undefined) {
			for (rowIndex=0; rowIndex < data.length; rowIndex++) {
				if(data[rowIndex].tpPeriodicidadeAprovado != null) {
					setElementValue(faturamentoClienteGridDef.getCellObject(rowIndex,"tpPeriodicidadeAprovado"), data[rowIndex].tpPeriodicidadeAprovado.value);
				}
		    }
		}
	}

	function validateButtons() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined && hasValue(url.parameters.idAnaliseCreditoCliente)) {
			setDisabled("btnManterCliente", true);
		} else setDisabled("btnManterCliente", false);

		setDisabled("btnHistorico", false);
		setDisabled("btnConcluirAnalise", true);
		setDisabled("btnAprovacaoGerente", true);
		setDisabled("btnAprovacaoDiretor", true);

		if(getElementValue("userCanAccess") == "true") {
			var tpSituacao = getElementValue("tpSituacao");
			if(tpSituacao != "C" && hasValue(tpSituacao)) {
				if(tpSituacao != "2") {
					setDisabled("btnConcluirAnalise", false);
				}
				if(tpSituacao == "M") {
					setDisabled("btnAprovacaoGerente", false);
				} else if(tpSituacao == "3") {
					setDisabled("btnAprovacaoDiretor", false);
				}
			}
		}
	}

	function openHistoricoPopup() {
		var url = "&analiseCreditoCliente.idAnaliseCreditoCliente="+getElementValue("idAnaliseCreditoCliente");
		url += "&cliente.pessoa.nrIdentificacao="+getElementValue("cliente.pessoa.nrIdentificacao");
		url += "&cliente.pessoa.nmPessoa="+getElementValue("cliente.pessoa.nmPessoa");
		showModalDialog("vendas/listarHistoricoAnaliseCredito.do?cmd=list"+url,window,"unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:420px;");		
	}

	function validateConclusao() {
		if (getElementValue("blCreditoLiberado")) {
			if(!hasValue(getElementValue("tpCobrancaAprovado"))) {
				alertRequiredField("tpCobrancaAprovado", true);
				return; 
			}
			if(!validateFaturamentoCliente()) {
				alertI18nMessage("requiredField", "periodicidadeFatAprovada", true);
				return;
			}
			if(!validatePrazoPagamentoCliente()) {
				alertI18nMessage("requiredField", "prazoAprovado", true);
				return;
			}
			if(!hasValue(getElementValue("moedaByIdMoedaLimCred.idMoeda"))) {
				alertRequiredField("moedaByIdMoedaLimCred.idMoeda", true);
				return;
			}
			if(!hasValue(getElementValue("vlLimiteCredito"))) {
				alertRequiredField("vlLimiteCredito", true);
				return;
			}
		} else if(!confirmI18nMessage("LMS-01164")) {
			return;
		}

		/* Chama popup para informar observacao */
		var obEvento = openObservacaoAnaliseCreditoPopup();
		if(obEvento != undefined) {
			concluirAnaliseCreditoCliente(obEvento);
		}
	}

	function validateFaturamentoCliente() {
		var dataTable = document.getElementById("faturamentoCliente.dataTable"); 
    	for (rowIndex=0; rowIndex < dataTable.rows.length; rowIndex++) {
    		var currentElement = faturamentoClienteGridDef.getCellObject(rowIndex, "tpPeriodicidadeAprovado");
    		if(!hasValue(getElementValue(currentElement))) {
    			setFocus(currentElement, false);
				return false;
    		}
    	}
		return true;
	}
	function validatePrazoPagamentoCliente() {
		var dataTable = document.getElementById("prazoPagamentoCliente.dataTable"); 
    	for (rowIndex=0; rowIndex < dataTable.rows.length; rowIndex++) {
    		var currentElement = prazoPagamentoClienteGridDef.getCellObject(rowIndex, "nrPrazoPagamentoAprovado");
    		if(!hasValue(getElementValue(currentElement))) {
    			setFocus(currentElement, false);
				return false;
    		}
    	}
		return true;
	}

	function openObservacaoAnaliseCreditoPopup() {
		var data = showModalDialog("vendas/gerarAnaliseCreditoCliente.do?cmd=main&labelTitle=historicoAnaliseCredito&idCliente="+getElementValue("cliente.idCliente")+"&isRequired="+!getElementValue("blCreditoLiberado"),window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:200px;');
		if(data != undefined && hasValue(data.obEvento)) {
			return data.obEvento;
		}
		return undefined;
	}

	function concluirAnaliseCreditoCliente(obEvento) {
		var data = new Array();	
		merge(data, buildFormBeanFromForm(document.forms[0]));
		data.obEvento = obEvento;

		var service = "lms.vendas.manterAnaliseCreditoClienteAction.storeConcluirAnaliseCredito";
		var sdo = createServiceDataObject(service, "afterStoreConcluirAnaliseCredito", data);
		xmit({serviceDataObjects:[sdo]});
	}
	function afterStoreConcluirAnaliseCredito_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		store_cb(data, error);

		/* Mensagem de alerta para o usuario */
		if(data.processWarning != undefined) {
			alert(data.processWarning);
		}
		validateButtons();
		showSuccessMessage();
	}

	function onClickAprovacaoGerente() {
		if (!getElementValue("blCreditoLiberado")) {
			alertI18nMessage("LMS-01165");
			return;
		}

		var obEvento = openObservacaoAnaliseCreditoPopup();
		if(obEvento != undefined) {
			var data = new Array();	
			merge(data, buildFormBeanFromForm(document.forms[0]));
			data.obEvento = obEvento;

			var service = "lms.vendas.manterAnaliseCreditoClienteAction.storeAprovacaoGerente";
			var sdo = createServiceDataObject(service, "afterAprovacaoGerente", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	function afterAprovacaoGerente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		/** Atualiza campos e verifica status da tela */
		store_cb(data, error);

		validateButtons();
		showSuccessMessage();
	}

	function onClickAprovacaoDiretor() {
		if (!getElementValue("blCreditoLiberado")) {
			alertI18nMessage("LMS-01165");
			return;
		}

		var obEvento = openObservacaoAnaliseCreditoPopup();
		if(obEvento != undefined) {
			var data = new Array();	
			merge(data, buildFormBeanFromForm(document.forms[0]));
			data.obEvento = obEvento;

			var service = "lms.vendas.manterAnaliseCreditoClienteAction.storeAprovacaoDiretor";
			var sdo = createServiceDataObject(service, "afterAprovacaoDiretor", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	function afterAprovacaoDiretor_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		/** Atualiza campos e verifica status da tela */
		store_cb(data, error);

		validateButtons();
		showSuccessMessage();
	}

	function changePage(url) {
		parent.parent.redirectPage(url);
	}

	function onRowClick() {
		return false;
	}
</script>