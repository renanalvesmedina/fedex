<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.manterClienteAction"
	onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:i18nLabels>
		<adsm:include key="cliente"/>
		<adsm:include key="LMS-01104"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/manterDadosIdentificacao"
		id="formIdentificacao"
		idProperty="idCliente"
		height="230">

		<adsm:hidden property="_tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="_tpCliente" value="S" serializable="false" />
		<adsm:hidden property="pessoa.enderecoPessoa.idEnderecoPessoa" />

		<adsm:textbox
			dataType="text"
			property="pessoa.nmFantasia"
			label="nomeFantasia"
			labelWidth="15%"
			width="35%"
			maxLength="60"
			size="36"
		/>
		<adsm:textbox
			dataType="integer"
			property="nrConta"
			label="numeroConta"
			labelWidth="15%"
			width="35%"
			maxLength="10"
			size="15"
			disabled="true"
		/>
		<adsm:textbox
			dataType="text"
			property="pessoa.nrRg"
			label="rg"
			labelWidth="15%"
			width="25%"
			size="20"
			maxLength="20"
		/>
		<adsm:textbox
			dataType="text"
			property="pessoa.dsOrgaoEmissorRg"
			label="orgaoEmissor"
			labelWidth="10%"
			width="17%"
			size="10"
			maxLength="10"
		/>
		<adsm:textbox
			dataType="JTDate"
			property="pessoa.dtEmissaoRg"
			label="dataEmissao"
			labelWidth="12%"
			width="21%"
		/>
		<adsm:textbox
			dataType="text"
			property="pessoa.nrInscricaoMunicipal"
			label="inscricaoMunicipal"
			size="15"
			maxLength="15"
			labelWidth="15%"
			width="23%"
		/>
		<adsm:textbox
			dataType="text"
			property="nrInscricaoSuframa"
			label="inscricaoSuframa"
			size="10"
			maxLength="10"
			labelWidth="12%"
			width="17%"
		/>
		<adsm:checkbox
			property="blMatriz"
			label="matriz"
			labelWidth="12%"
			width="21%"
		/>
		<adsm:textbox
			dataType="JTDate"
			property="dtFundacaoEmpresa"
			label="dataFundacao"
			labelWidth="15%"
			width="35%"
		/>
		<adsm:combobox
			property="tpSituacao"
			onlyActiveValues="true"
			domain="DM_STATUS_PESSOA"
			label="situacao"
			labelWidth="15%"
			width="35%"
			required="true"
		/>
		<adsm:textbox
			dataType="email"
			property="pessoa.dsEmail"
			label="email"
			maxLength="60"
			labelWidth="15%"
			width="35%"
			size="36"
		/>
		<adsm:textbox
			dataType="text"
			property="dsSite"
			label="homepage"
			labelWidth="15%"
			width="35%"
			maxLength="60"
			size="36"
		/>
		<adsm:combobox
			property="grupoEconomico.idGrupoEconomico"
			onlyActiveValues="true"
			optionLabelProperty="dsGrupoEconomico"
			optionProperty="idGrupoEconomico"
			service="lms.vendas.manterClienteAction.findGrupoEconomico"
			label="grupoEconomico"
			labelWidth="15%"
			width="35%"
			boxWidth="260"
			autoLoad="false"
			renderOptions="true"
			disabled="true"
		/>
		<adsm:combobox
			property="ramoAtividade.idRamoAtividade"
			onlyActiveValues="true"
			optionLabelProperty="dsRamoAtividade"
			optionProperty="idRamoAtividade"
			service="lms.vendas.manterClienteAction.findRamoAtividade"
			label="ramoAtividade"
			labelWidth="15%"
			width="35%"
			boxWidth="240"
			autoLoad="false"
			renderOptions="true"
			onchange="return ramoAtividadeOnChange();"
		/>
		<adsm:combobox
			property="tpAtividadeEconomica"
			onlyActiveValues="true"
			domain="DM_ATIVIDADE_ECONOMICA"
			label="atividadeEconomica"
			labelWidth="15%"
			width="35%"
			boxWidth="260"
			autoLoad="false"
			renderOptions="true"
		/>
		<adsm:combobox
			property="segmentoMercado.idSegmentoMercado"
			onlyActiveValues="true"
			optionLabelProperty="dsSegmentoMercado"
			optionProperty="idSegmentoMercado"
			service="lms.vendas.manterClienteAction.findSegmentoMercado"
			label="segmentoMercado"
			labelWidth="15%"
			width="35%"
			boxWidth="240"
			autoLoad="false"
			renderOptions="true"
		/>
		<adsm:textbox
			dataType="JTDate"
			property="dtGeracao"
			label="dataGeracao"
			disabled="true"
			labelWidth="15%"
			width="22%"
		/>
		<adsm:textbox
			dataType="JTDate"
			property="dtUltimoMovimento"
			label="ultimoMovimento"
			disabled="true"
			labelWidth="12%"
			width="20%"
		/>

		<adsm:combobox
			property="tpFormaCobranca"
			onlyActiveValues="true"
			domain="TP_FORMA_COBRANCA"
			label="tpFormaCobranca"
			labelWidth="16%"
			width="14%"
			boxWidth="90"
			autoLoad="true"
			renderOptions="true"
			required="true"
		/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			dataType="text"
			exactMatch="true"
			idProperty="idCliente"
			label="clienteMatriz"
			maxLength="20"
			property="clienteMatriz"
			service="lms.vendas.manterClienteAction.findLookupCliente"
			onDataLoadCallBack="lookupCliente"
			afterPopupSetValue="aferPopupClienteMatriz"
			size="20"
			labelWidth="15%"
			width="81%"
		>
			<adsm:propertyMapping
				relatedProperty="clienteMatriz.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa"/>

			<adsm:propertyMapping
				criteriaProperty="_tpSituacao"
				modelProperty="tpSituacao"/>

			<adsm:propertyMapping
				criteriaProperty="_tpCliente"
				modelProperty="tpCliente"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="clienteMatriz.pessoa.nmPessoa"
				serializable="false"
				size="58"/>
		</adsm:lookup>

		<adsm:textarea
			columns="115"
			rows="4"
			maxLength="500"
			property="obCliente"
			label="observacao"
			labelWidth="15%"
			width="81%"
		/>
		<adsm:checkbox
			property="pessoa.blAtualizacaoCountasse"
			label="atualizacaoDbi"
			disabled="true"
			labelWidth="15%"
			width="10%"
		/>
		<adsm:checkbox
			property="blNaoAtualizaDBI"
			label="naoAtualizaDbi"
			labelWidth="15%"
			width="10%"
		/>
		<adsm:checkbox
			property="blEnviaDacteXmlFat"
			label="enviaDacteXmlFat"
			labelWidth="15%"
			width="10%"
		/>

		<adsm:buttonBar>
			<adsm:button
				caption="inscricoesEstaduais"
				id="btnInscricoesEstaduais"
				onclick="return changePage('configuracoes/manterInscricoesEstaduais.do?cmd=main');"
				boxWidth="140"
			/>
			<adsm:button
				caption="dadosBancarios"
				id="btnDadosBancarios"
				onclick="return changePage('configuracoes/manterDadosBancariosPessoa.do?cmd=main');"
				boxWidth="120"
			/>
			<adsm:button
				caption="camposComplementares"
				id="btnCamposComplementares"
				onclick="return changePageCliente('vendas/manterValoresCamposComplementaresCliente.do?cmd=main');"
				boxWidth="170"
			/>
			<adsm:button
				caption="contatos"
				id="btnContatos"
				onclick="return changePage('configuracoes/manterContatos.do?cmd=main');"
			/>
			<adsm:button
				caption="enderecos"
				id="btnEnderecos"
				onclick="return changePage('configuracoes/manterEnderecoPessoa.do?cmd=main');"
			/>
			<adsm:button
				caption="telefones"
				id="btnTelefones"
				onclick="return changePage('configuracoes/manterTelefonesPessoa.do?cmd=main');"
			/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	var tpPessoa = {FISICA:'F', JURIDICA:'J'};
	var tpCliente = {EVENTUAL:'E', ESPECIAL:'S', POTENCIAL:'P', FILIAL:'F'};
	var tpSituacao = {ATIVO:'A', INATIVO:'I', INCOMPLETO:'N'};
	var modo = {INCLUSAO:"1", EDICAO:"2"};
	var _tipoPessoa;

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
			var mode = telaCad.getModoTela();
			var tipoCliente = telaCad.getTpCliente();
			enableFields(mode, telaCad.getTpClienteRegras());
			tpPessoaOnChange(_tipoPessoa);

			if(telaCad.getIdProcessoWorkflow() != undefined && telaCad.getIdProcessoWorkflow() != ''){
				disableButtons(true);
				disableAll();
			}
		}
	}

	function myOnShow() {
		return false;
	}

	function loadData(data) {
		onDataLoad_cb(data);
		validateTpIncompleto();
		disableNomeFantasia(data.nomeFantasia.inativo);
	}

	function clean() {
		resetValue(document);
	}

	function disableNomeFantasia(inativo) {
		setDisabled("pessoa.nmFantasia", inativo=='true'?true:false);
	}

	function disableAll() {
		setDisabled(this.document, true);
	}

	function enableFields(mode, tipoCliente) {
		changeAbaStatus(false);
		setDisabled(document, false);

		if (tipoCliente == tpCliente.POTENCIAL ||
			tipoCliente == tpCliente.EVENTUAL ||
			tipoCliente == tpCliente.ESPECIAL) {
			setDisabled("clienteMatriz.idCliente", true);
			setDisabled("clienteMatriz.pessoa.nrIdentificacao", true);
		} else {
			setDisabled("blEnviaDacteXmlFat", true);
		}

		if (mode == modo.INCLUSAO) {
			disableButtons(true);
			setDisabled("tpSituacao", true);
		} else if (mode == modo.EDICAO) {
			disableButtons(false);
			if (getElementValue("tpSituacao") == tpSituacao.INCOMPLETO) {
				setDisabled("tpSituacao", true);
			}
		}
		setDisabled("clienteMatriz.pessoa.nmPessoa", true);
		setDisabled("dtGeracao", true);
		setDisabled("dtUltimoMovimento", true);
		setDisabled("nrConta", true);
		setDisabled("pessoa.blAtualizacaoCountasse", true);
		setDisabled("grupoEconomico.idGrupoEconomico", true);
		
	}

	function changeAbaStatus(status) {
		var tabGroup = getTabGroup(document);
		tabGroup.setDisabledTab("identificacao", status);
	}

	function disableButtons(status) {
		setDisabled("btnInscricoesEstaduais", status);
		setDisabled("btnDadosBancarios", status);
		setDisabled("btnCamposComplementares", status);
		setDisabled("btnContatos", status);
		setDisabled("btnEnderecos", status);
		setDisabled("btnTelefones", status);
	}

	function setDadosPessoa(data) {
		setElementValue("pessoa.dsEmail", data.dsEmail);
		setElementValue("pessoa.nrRg", data.nrRg);
		setElementValue("pessoa.dsOrgaoEmissorRg", data.dsOrgaoEmissorRg);
		setElementValue("pessoa.dtEmissaoRg", setFormat("pessoa.dtEmissaoRg", data.dtEmissaoRg));
	}

	function tpPessoaOnChange(value) {
		_tipoPessoa = value;

		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
		var mode = telaCad.getModoTela();
		var id_produtor_rural = 6;

		if (value == tpPessoa.FISICA) {
			setDisabled("blMatriz", true);
			setElementValue("blMatriz", false);
			setDisabled("btnInscricoesEstaduais", true);
			if (mode == modo.EDICAO) {
				if (id_produtor_rural == getElementValue("ramoAtividade.idRamoAtividade")) {
					setDisabled("btnInscricoesEstaduais", false);
				}
			}
			setDisabled("pessoa.nrRg", false);
			setDisabled("pessoa.dsOrgaoEmissorRg", false);
			setDisabled("pessoa.dtEmissaoRg", false);
		} else if (value == tpPessoa.JURIDICA) {
			setDisabled("pessoa.nrRg", true);
			setDisabled("pessoa.dsOrgaoEmissorRg", true);
			setDisabled("pessoa.dtEmissaoRg", true);

			if (mode == modo.EDICAO) {
				setDisabled("btnInscricoesEstaduais", false);
			}
			setDisabled("blMatriz", false);

			resetValue("pessoa.nrRg");
			resetValue("pessoa.dsOrgaoEmissorRg");
			resetValue("pessoa.dtEmissaoRg");
		}
	}

	function getTpSituacao(){
		return getElementValue("tpSituacao");
	}

	function validateTpIncompleto() {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		if (getElementValue("tpSituacao") != tpSituacao.INCOMPLETO) {
			setDisabled("tpSituacao", false);
			var index = getIncompletoIndex();
			if (index > -1) {
				var combo = getElement("tpSituacao");
				combo.options[index] = null;
			}
			telaCad.changeConcluirStatus(true);
		} else {
			setDisabled("tpSituacao", true);
			telaCad.changeConcluirStatus(false);
		}
	}

	function getIncompletoIndex() {
		var combo = getElement("tpSituacao");
		for (var i = 0; i < combo.length; i++) {
			if (combo.options[i].value == tpSituacao.INCOMPLETO) {
				return i;
			}
		}
		return -1;
	}

	function ajustaDadosDefault(data) {
		reloadTpSituacao();
	}

	function reloadTpSituacao() {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
		var mode = telaCad.getModoTela();
		if (mode == modo.INCLUSAO) {
			if (getIncompletoIndex() == -1) {
				var sdo = createServiceDataObject("adsm.configuration.domainValueService.findValues", "reloadTpSituacao", {domain:{name:"DM_STATUS_PESSOA"}, status:true});
				xmit({serviceDataObjects:[sdo]});
			} else {
				setElementValue("tpSituacao", tpSituacao.INCOMPLETO);
			}
		}
	}

	function reloadTpSituacao_cb(data, error) {
		tpSituacao_cb(data, error);
		setElementValue("tpSituacao", tpSituacao.INCOMPLETO);
		setElementValue("blEnviaDacteXmlFat", data.blEnviaDacteXmlFat);
		validateTpIncompleto();
	}

	function changeCamposObrigatorios() {
		if (getElementValue("tpSituacao") == tpSituacao.INATIVO) {
			getElement("obCliente").required = "true";
		} else {
			getElement("obCliente").required = "false";
		}

		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
		if (telaCad.getTpClienteRegras() == tpCliente.FILIAL) {
			getElement("clienteMatriz.pessoa.nrIdentificacao").required = "true";
		} else {
			getElement("clienteMatriz.pessoa.nrIdentificacao").required = "false";
		}

		if (getElement("pessoa.tpPessoa", telaCad.document).value == tpPessoa.JURIDICA) {
			getElement("tpAtividadeEconomica").required = "true";
			getElement("segmentoMercado.idSegmentoMercado").required = "true";
			getElement("ramoAtividade.idRamoAtividade").required = "true";
		} else {
			getElement("tpAtividadeEconomica").required = "false";
			getElement("segmentoMercado.idSegmentoMercado").required = "false";
			getElement("ramoAtividade.idRamoAtividade").required = "false";
		}
	}

	function changePage(url) {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		telaCad.changePage(url);
	}

	function changePageCliente(url) {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		telaCad.changePageCliente(url);
	}

	function afterStore(data) {
		setElementValue("dtGeracao", setFormat("dtGeracao", data.dtGeracao));
		setElementValue("nrConta", data.nrConta);
		if (data.tpSituacao != undefined) {
			if(data.tpSituacao == tpSituacao.INCOMPLETO) {
				var sdo = createServiceDataObject("adsm.configuration.domainValueService.findValues", "reloadTpSituacao", {domain:{name:"DM_STATUS_PESSOA"}, status:true});
				xmit({serviceDataObjects:[sdo]});
			} else {
				setElementValue("tpSituacao", data.tpSituacao);
				validateTpIncompleto();
			}
		}
	}

	function lookupCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		var retorno = clienteMatriz_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data.length > 0) {
			setElementValue("clienteMatriz.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
		}
		return retorno;
	}

	function aferPopupClienteMatriz(data) {
		setElementValue("clienteMatriz.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
	}

	function ajustaDadosDefaultEdicao(tipoCliente, data) {
		if (tipoCliente == tpCliente.POTENCIAL ||
			tipoCliente == tpCliente.EVENTUAL ||
			tipoCliente == tpCliente.ESPECIAL) {

			setElementValue("clienteMatriz.idCliente", "");
			setElementValue("clienteMatriz.pessoa.nrIdentificacao", "");
			setElementValue("clienteMatriz.pessoa.nmPessoa", "");
		}
	}

	function myOnPageLoadCallBack_cb() {
		onPageLoad_cb();
		unblockUI();
	}

	function ramoAtividadeOnChange() {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
		var mode = telaCad.getModoTela();

		var id_produtor_rural = 6;
	
		if (mode != modo.EDICAO) {
			setDisabled("btnInscricoesEstaduais", true);
			return true;
		}		
	
		setDisabled("btnInscricoesEstaduais", true);
		if (_tipoPessoa == tpPessoa.FISICA) {
			if (id_produtor_rural == getElementValue("ramoAtividade.idRamoAtividade")) {
				setDisabled("btnInscricoesEstaduais", false);
			}
		} else if (_tipoPessoa == tpPessoa.JURIDICA) {
			setDisabled("btnInscricoesEstaduais", false);
		}
		return true;			
	}

</script>