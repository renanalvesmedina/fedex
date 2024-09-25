<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function myOnPageLoad(){
		onPageLoad();

		initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa"),
		tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"),
		numberElement:document.getElementById("pessoa.idPessoa") });		
	}
</script>
<adsm:window 
	service="lms.vendas.manterClienteAction" 
	onPageLoad="myOnPageLoad">
	
	<adsm:i18nLabels>
		<adsm:include key="cliente"/>
		<adsm:include key="LMS-01104"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/vendas/manterDadosIdentificacao">
		
		<adsm:hidden property="filialByIdFilialAtendeOperacional.idFilial"/>
		<adsm:hidden property="usuariosCliente.usuarioLMS.idUsuario"/>
		<adsm:hidden property="usuariosCliente.usuarioLMS.blIrrestritoCliente"/>
		
		<adsm:combobox 
			definition="TIPO_PESSOA.list" 
			property="pessoa.tpPessoa" 
			onlyActiveValues="true" 
			labelWidth="15%" 
			width="35%" 
			label="tipoPessoa" 
			domain="DM_TIPO_PESSOA"
			autoLoad="false"
			renderOptions="true"/>
			
		<adsm:complement label="identificacao">
			<adsm:combobox 
				definition="TIPO_IDENTIFICACAO_PESSOA.list" 
				autoLoad="false"
				renderOptions="true"/>
			<adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
		
		<adsm:textbox 
			dataType="text" 
			size="47" 
			property="pessoa.nmPessoa" 
			label="nomeRazaoSocial" 
			maxLength="50" 
			labelWidth="15%" 
			width="81%"/>
			
		<adsm:textbox 
			dataType="text" 
			property="nmFantasia" 
			label="nomeFantasia" 
			labelWidth="15%" 
			width="35%" 
			maxLength="50" 
			size="36"/>
			
		<adsm:textbox 
			dataType="integer" 
			property="nrConta" 
			label="numeroConta" 
			labelWidth="15%" 
			width="35%" 
			maxLength="12" 
			size="15"/>
			
		<adsm:combobox 
			property="tpCliente" 
			domain="DM_TIPO_CLIENTE" 
			label="tipoCliente" 
			labelWidth="15%" 
			width="35%"
			autoLoad="false"
			renderOptions="true"/>
			
		<adsm:combobox 
			property="tpSituacao" 
			domain="DM_STATUS_PESSOA" 
			label="situacao" 
			labelWidth="15%" 
			width="35%"
			autoLoad="false"
			renderOptions="true"/>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				caption="consultar" 
				onclick="consultar(this);" 
				buttonType="findButton" 
				id="__buttonBar:0.findButton" 
				disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idCliente" 
		property="cliente" 
		gridHeight="200" 
		unique="true"
		scrollBars="horizontal">
		
		<adsm:gridColumn 
			title="identificacao" 
			property="pessoa.tpIdentificacao" 
			isDomain="true" 
			width="70" />
			
		<adsm:gridColumn 
			title="" 
			property="pessoa.nrIdentificacaoFormatado" 
			width="100" 
			align="right"/>
			
		<adsm:gridColumn 
			title="nome" 
			property="pessoa.nmPessoa" 
			width="160"/>
			
		<adsm:gridColumn 
			title="nomeFantasia" 
			property="pessoa.nmFantasia" 
			width="115"/>
			
		<adsm:gridColumn 
			title="numeroConta" 
			property="nrConta" 
			width="115" 
			align="right"/>
			
		<adsm:gridColumn 
			title="tipoCliente" 
			property="tpCliente" 
			isDomain="true" 
			width="115"/>
			
		<adsm:gridColumn 
			title="situacao" 
			property="tpSituacao" 
			isDomain="true"
			width="100"/>
		
		<adsm:gridColumn 
			title="endereco"
			property="enderecoFormatado"
			width="350"/>
			
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	var telaCad;
	var telaIdentificacao;
	var telaComercial;
	var telaOperacional;
	var telaFinanceiro;
	/*
	 * Validação da consulta de cliente.
	 * Não pode consultar sem antes ter informado Identificação, Nome/Razão social, Nome fantasia ou Número da conta.
	 * Se não informado nenhum mostrar mensagem LMS-01104
	 */
	function validateConsultar() {
		if(getElementValue("pessoa.nrIdentificacao") == "" &&
			getElementValue("pessoa.nmPessoa") == "" &&
			getElementValue("nmFantasia") == "" &&
			getElementValue("nrConta") == "")
		{
			alert(i18NLabel.getLabel("LMS-01104"));
			return false;
		}
		return true;
	}

	function consultar(eThis) {
		if(validateConsultar()) {
			findButtonScript('cliente', eThis.form);
		}
	}

	function myOnShow(x, eventObj) {
		tab_onShow();
	}
	
	function initTabVariables() {
		if (tabIdentificacao == undefined) {
			var tabGroup = getTabGroup(document);
			var tabCad = tabGroup.getTab("cad");
			
			var childTabGroup = tabCad.childTabGroup;
			var tabIdentificacao = childTabGroup.getTab("identificacao");
			var tabComercial = childTabGroup.getTab("comercial");
			var tabOperacional = childTabGroup.getTab("operacional");
			var tabFinanceiro = childTabGroup.getTab("financeiro");
			
			telaCad = tabCad.tabOwnerFrame;
			telaIdentificacao = tabIdentificacao.tabOwnerFrame;
			telaComercial = tabComercial.tabOwnerFrame;
			telaOperacional = tabOperacional.tabOwnerFrame;
			telaFinanceiro = tabFinanceiro.tabOwnerFrame;
		}
	}
	
	function loadCombos() {
		initTabVariables();
		var sdos = [
			/*** LIST ***/
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "pessoa.tpPessoa", {domain:{name:"DM_TIPO_PESSOA"}}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "pessoa.tpIdentificacao", {domain:{name:"DM_TIPO_IDENTIFICACAO_PESSOA"}}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "tpCliente", {domain:{name:"DM_TIPO_CLIENTE"}}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "tpSituacao", {domain:{name:"DM_STATUS_PESSOA"}}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "tpEmissaoDoc", {domain:{name:"DM_TIPO_EMISSAO_DOC_CLIENTE"}}),
			/*** CAD ***/
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "cad.pessoa.tpPessoa", {domain:{name:"DM_TIPO_PESSOA"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "cad.pessoa.tpIdentificacao", {domain:{name:"DM_TIPO_IDENTIFICACAO_PESSOA"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "cad.tpCliente", {domain:{name:"DM_TIPO_CLIENTE"}, status:true}),
			/*** IDENTIFICACAO ***/
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "identificacao.tpSituacao", {domain:{name:"DM_STATUS_PESSOA"}, status:true}),
			createServiceDataObject("lms.vendas.manterClienteAction.findGrupoEconomico", "identificacao.grupoEconomico.idGrupoEconomico", {tpSituacao:"A"}),
			createServiceDataObject("lms.vendas.manterClienteAction.findRamoAtividade", "identificacao.ramoAtividade.idRamoAtividade", {tpSituacao:"A"}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "identificacao.tpAtividadeEconomica", {domain:{name:"DM_ATIVIDADE_ECONOMICA"}, status:true}),
			createServiceDataObject("lms.vendas.manterClienteAction.findSegmentoMercado", "identificacao.segmentoMercado.idSegmentoMercado", {tpSituacao:"A"}),
			/*** COMERCIAL ***/
			createServiceDataObject("lms.vendas.manterClienteAction.findMoedaPaisCombo", "comercial.moedaByIdMoedaFatPrev.idMoeda", {tpSituacao:"A"}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "comercial.tpFormaArredondamento", {domain:{name:"DM_FORMA_ARREDONDAMENTO"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "comercial.tpFrequenciaVisita", {domain:{name:"DM_FREQUENCIA_VISITA"}, status:true}),
			/*** OPERACIONAL ***/
			createServiceDataObject("lms.vendas.manterClienteAction.findObservacaoConhecimento", "operacional.observacaoConhecimento.idObservacaoConhecimento", {tpSituacao:"A"}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "operacional.tpDificuldadeColeta", {domain:{name:"DM_GRAU_DIFICULDADE"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "operacional.tpDificuldadeEntrega", {domain:{name:"DM_GRAU_DIFICULDADE"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "operacional.tpDificuldadeClassificacao", {domain:{name:"DM_GRAU_DIFICULDADE"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "operacional.tpLocalEmissaoConReent", {domain:{name:"DM_LOCAL_EMISSAO_CON_REENT"}, status:true}),
			/*** FINANCEIRO ***/
			createServiceDataObject("lms.vendas.manterClienteAction.findMoedaPaisCombo", "financeiro.moedaByIdMoedaLimCred.idMoeda", {tpSituacao:"A"}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "financeiro.tpCobranca", {domain:{name:"DM_TIPO_COBRANCA"}, status:true}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "financeiro.tpMeioEnvioBoleto", {domain:{name:"DM_MEIO_ENVIO"}, status:true}),
			createServiceDataObject("lms.vendas.manterClienteAction.findActiveCedentes", "financeiro.cedente.idCedente", {tpSituacao:"A"}),
			createServiceDataObject("adsm.configuration.domainValueService.findValues", "financeiro.tpPeriodicidadeTransf", {domain:{name:"DM_PERIODICIDADE_FATURAMENTO"}, status:true}),
			createServiceDataObject("lms.vendas.manterClienteAction.findMoedaPaisCombo", "financeiro.moedaByIdMoedaLimDoctos.idMoeda", {tpSituacao:"A"})
		];
		xmit({serviceDataObjects:sdos});
	}
	
	/*** CAD ***/
	function cad_pessoa_tpPessoa_cb(data) {
		telaCad.pessoa_tpPessoa_cb(data);
	}
	function cad_pessoa_tpIdentificacao_cb(data) {
		telaCad.pessoa_tpIdentificacao_cb(data);
	}
	function cad_tpCliente_cb(data) {
		telaCad.tpCliente_cb(data);
	}
	/*** IDENTIFICACAO ***/
	function identificacao_tpSituacao_cb(data) {
		telaIdentificacao.tpSituacao_cb(data);
	}
	function identificacao_grupoEconomico_idGrupoEconomico_cb(data) {
		telaIdentificacao.grupoEconomico_idGrupoEconomico_cb(data);
	}
	function identificacao_ramoAtividade_idRamoAtividade_cb(data) {
		telaIdentificacao.ramoAtividade_idRamoAtividade_cb(data);
	}
	function identificacao_tpAtividadeEconomica_cb(data) {
		telaIdentificacao.tpAtividadeEconomica_cb(data);
	}
	function identificacao_segmentoMercado_idSegmentoMercado_cb(data) {
		telaIdentificacao.segmentoMercado_idSegmentoMercado_cb(data);
	}
	/*** COMERCIAL ***/
	function comercial_moedaByIdMoedaFatPrev_idMoeda_cb(data) {
		telaComercial.moedaByIdMoedaFatPrev_idMoeda_cb(data);
	}
	function comercial_tpFormaArredondamento_cb(data) {
		telaComercial.tpFormaArredondamento_cb(data);
	}
	function comercial_tpFrequenciaVisita_cb(data) {
		telaComercial.tpFrequenciaVisita_cb(data);
	}
	/*** OPERACIONAL ***/
	function operacional_observacaoConhecimento_idObservacaoConhecimento_cb(data) {
		telaOperacional.observacaoConhecimento_idObservacaoConhecimento_cb(data);
	}
	function operacional_tpDificuldadeColeta_cb(data) {
		telaOperacional.tpDificuldadeColeta_cb(data);
	}
	function operacional_tpDificuldadeEntrega_cb(data) {
		telaOperacional.tpDificuldadeEntrega_cb(data);
	}
	function operacional_tpDificuldadeClassificacao_cb(data) {
		telaOperacional.tpDificuldadeClassificacao_cb(data);
	}
	function operacional_tpLocalEmissaoConReent_cb(data) {
		telaOperacional.tpLocalEmissaoConReent_cb(data);
	}
	/*** FINANCEIRO ***/
	function financeiro_moedaByIdMoedaLimCred_idMoeda_cb(data) {
		telaFinanceiro.moedaByIdMoedaLimCred_idMoeda_cb(data);
	}
	function financeiro_tpCobranca_cb(data) {
		telaFinanceiro.tpCobranca_cb(data);
	}
	function financeiro_tpMeioEnvioBoleto_cb(data) {
		telaFinanceiro.tpMeioEnvioBoleto_cb(data);
	}
	function financeiro_cedente_idCedente_cb(data) {
		telaFinanceiro.cedente_idCedente_cb(data);
	}
	function financeiro_tpPeriodicidadeTransf_cb(data) {
		telaFinanceiro.tpPeriodicidadeTransf_cb(data);
	}
	function financeiro_moedaByIdMoedaLimDoctos_idMoeda_cb(data) {
		telaFinanceiro.moedaByIdMoedaLimDoctos_idMoeda_cb(data);
	}
</script>