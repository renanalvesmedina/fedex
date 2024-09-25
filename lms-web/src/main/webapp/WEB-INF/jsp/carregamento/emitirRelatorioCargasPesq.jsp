<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.emitirRelatorioCargasAction"
	onPageLoadCallBack="retornoCarregaPagina">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-05399"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/carregamento/emitirRelatorioCargas">

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true"
			serializable="false" />
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado"
			value="false" serializable="false" />
		<adsm:hidden property="tpAcesso" value="A" />

		<adsm:hidden property="blLoadDefaultData" value="true"
			serializable="false" />
		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia"
			serializable="false" />

		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false" />

		<adsm:lookup label="filialLocalizacao" dataType="text"
			property="filialLocalizacao" idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.carregamento.emitirRelatorioCargasAction.findLookupFilial"
			action="/municipios/manterFiliais" size="3" maxLength="3" width="85%"
			required="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="filialLocalizacao.pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialLocalizacao.pessoa.nmFantasia" size="30"
				disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:hidden property="localizacaoMercadoria.dsLocalizacaoMercadoria" />
		<adsm:combobox label="localizacao"
			property="localizacaoMercadoria.cdLocalizacaoMercadoria"
			optionProperty="cdLocalizacaoMercadoria"
			optionLabelProperty="dsLocalizacaoMercadoria"
			service="lms.carregamento.emitirRelatorioCargasAction.findLocalizacaoMercadoria"
			width="85%" required="false"
			onchange="hideOrShowAgendamento();">
			<adsm:propertyMapping modelProperty="dsLocalizacaoMercadoria"
				relatedProperty="localizacaoMercadoria.dsLocalizacaoMercadoria" />
		</adsm:combobox>

		<adsm:hidden property="moeda.siglaSimbolo" />
		<adsm:combobox property="moeda.idMoeda" label="moeda"
			service="lms.carregamento.emitirRelatorioCargasAction.findMoeda"
			optionProperty="idMoeda" optionLabelProperty="siglaSimbolo"
			width="85%" required="true">
			<adsm:propertyMapping modelProperty="siglaSimbolo"
				relatedProperty="moeda.siglaSimbolo" />
		</adsm:combobox>

		<adsm:range label="diasTitulo" width="85%">
			<adsm:textbox dataType="integer" property="diaInicial" size="3"
				maxLength="3" />
			<adsm:textbox dataType="integer" property="diaFinal" size="3"
				maxLength="3" />
		</adsm:range>

		<adsm:lookup label="origem" dataType="text" property="filialOrigem"
			idProperty="idFilial" criteriaProperty="sgFilial"
			service="lms.carregamento.emitirRelatorioCargasAction.findLookupFilial"
			action="/municipios/manterFiliais" size="3" maxLength="3" width="85%">
			<adsm:propertyMapping
				modelProperty="flagDesabilitaEmpresaUsuarioLogado"
				criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="filialOrigem.pessoa.nmFantasia" />
			<adsm:propertyMapping modelProperty="sgFilial"
				relatedProperty="filialOrigem.sgFilial" />
			<adsm:textbox dataType="text"
				property="filialOrigem.pessoa.nmFantasia" size="50" disabled="true"
				serializable="false" />
		</adsm:lookup>

		<adsm:lookup label="destino" dataType="text" property="filialDestino"
			idProperty="idFilial" criteriaProperty="sgFilial"
			service="lms.carregamento.emitirRelatorioCargasAction.findLookupFilial"
			action="/municipios/manterFiliais" size="3" maxLength="3" width="85%">
			<adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado"
				criteriaProperty="flagBuscaEmpresaUsuarioLogado" />
			<adsm:propertyMapping
				modelProperty="flagDesabilitaEmpresaUsuarioLogado"
				criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="tpAcesso"
				criteriaProperty="tpAcesso" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="filialDestino.pessoa.nmFantasia" />
			<adsm:propertyMapping modelProperty="sgFilial"
				relatedProperty="filialDestino.sgFilial" />
			<adsm:textbox dataType="text"
				property="filialDestino.pessoa.nmFantasia" size="50" disabled="true"
				serializable="false" />
		</adsm:lookup>

		<adsm:lookup label="rotaEntrega" property="rotaColetaEntrega"
			idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
			action="/municipios/manterRotaColetaEntrega"
			service="lms.carregamento.emitirRelatorioCargasAction.findLookupRotaColetaEntrega"
			dataType="integer" size="3" maxLength="3" width="85%">
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="dsRota" />
			<adsm:propertyMapping modelProperty="filial.idFilial"
				criteriaProperty="filialUsuario.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial"
				criteriaProperty="filialUsuario.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia"
				criteriaProperty="filialUsuario.pessoa.nmFantasia" />
			<adsm:textbox property="dsRota" dataType="text" size="30"
				disabled="true" />
		</adsm:lookup>

		<adsm:lookup dataType="text" label="remetente"
			property="clienteRemetente" idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.carregamento.emitirRelatorioCargasAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" size="20" maxLength="20"
			width="85%" serializable="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteRemetente.pessoa.nmPessoa" />
			<adsm:textbox dataType="text"
				property="clienteRemetente.pessoa.nmPessoa" size="50"
				disabled="true" />
		</adsm:lookup>

		<adsm:hidden property="dsTpDestinatario" />
		<adsm:combobox property="tpDestinatario" label="tipoDestinatario"
			domain="DM_TIPO_PESSOA" renderOptions="true"
			onchange="onChangeTpDestinatario()" width="85%" />

		<adsm:lookup dataType="text" label="destinatario"
			property="clienteDestinatario" idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.carregamento.emitirRelatorioCargasAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" size="20" maxLength="20"
			width="85%" serializable="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteDestinatario.pessoa.nmPessoa" />
			<adsm:textbox dataType="text"
				property="clienteDestinatario.pessoa.nmPessoa" size="50"
				disabled="true" />
		</adsm:lookup>

		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox label="servico" property="servico.idServico"
			width="85%" optionProperty="idServico"
			optionLabelProperty="dsServico"
			service="lms.carregamento.emitirRelatorioCargasAction.findServico">
			<adsm:propertyMapping modelProperty="dsServico"
				relatedProperty="servico.dsServico" />
		</adsm:combobox>

		<adsm:hidden property="dsAgrupadoPor" />
		<adsm:combobox property="agrupadoPor"
			optionLabelProperty="description" optionProperty="value" width="100%"
			disabled="false" onchange="onChangeAgrupadoPor()"
			service="lms.carregamento.emitirRelatorioCargasAction.findTpAgrupadoPor"
			label="agrupadoPor" required="true" defaultValue="FD">
		</adsm:combobox>
		
		<adsm:hidden property="formatoRelatorio.description" />
		<adsm:combobox property="formatoRelatorio"
			optionLabelProperty="description" optionProperty="value" width="85%"
			disabled="false"
			service="lms.carregamento.emitirRelatorioCargasAction.findTpFormatoRelatorio"
			label="formatoRelatorio" required="true" defaultValue="pdf" 
			onchange="desabilitaCampoAgrupadoPor()">
			<adsm:propertyMapping modelProperty="description"
				relatedProperty="formatoRelatorio.description" />
		</adsm:combobox>
		
		<adsm:checkbox property="blAguardandoAgendamento" label="blAguardandoAgendamento" labelWidth="25%" width="43%" disabled="false"/>

		<adsm:checkbox property="blDocumentosComAgendamento" label="blDocumentosComAgendamento" labelWidth="25%" width="43%" disabled="false" onclick="onclickBlAgendamento()"/>
		<adsm:checkbox property="blDocumentosComProdutoPerigoso" label="blDocumentosComProdutoPerigoso" labelWidth="25%" width="43%" disabled="false" onclick="onclickBlProduto()"/>
		<adsm:checkbox property="blDocumentosComProdutoControlado" label="blDocumentosComProdutoControlado" labelWidth="25%" width="43%" disabled="false" onclick="onclickBlProduto()"/>

		<adsm:checkbox property="blRoteirizacao" label="blRoteirizacao" labelWidth="15%" width="43%" disabled="false"/>

		<adsm:buttonBar>
			<adsm:button id="emitirDetalhadoButton" caption="visualizarDetalhado"
				buttonType="reportViewerButton"
				onclick="imprimeRelatorioDetalhado(this.form);" disabled="false" />
			<adsm:button id="emitirResumidoButton" caption="visualizarResumido"
				buttonType="reportViewerButton"
				onclick="imprimeRelatorioResumido(this.form);" disabled="false" />
			<adsm:button id="btnLimpar" caption="limpar" disabled="false"
				buttonType="resetButton" onclick="limpaTela()" />
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script language="javascript" type="text/javascript">

	function initWindow() {
		setMasterLink(this.document, true);
		document.getElementById("filialLocalizacao.sgFilial").serializable = "true";
		document.getElementById("filialOrigem.sgFilial").serializable = "true";
		document.getElementById("filialDestino.sgFilial").serializable = "true";
		setFocusOnFirstFocusableField();
		if (getElementValue("formatoRelatorio") == "") {
			setDisabled("agrupadoPor", true);
		}
		setRowVisibility("blAguardandoAgendamento", false, document);
		setRowVisibility("blDocumentosComAgendamento", false, document);
		setRowVisibility("blDocumentosComProdutoPerigoso", false, document);
		setRowVisibility("blDocumentosComProdutoControlado", false, document);
	}

	function retornoCarregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		if (getElementValue("blLoadDefaultData") == "true") {
			loadDataUsuario();
		}
	}

	function loadDataUsuario() {
		var sdo = createServiceDataObject(
				"lms.carregamento.emitirRelatorioCargasAction.getDataUsuario",
				"resultado_loadDataUsuario");
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	function resultado_loadDataUsuario_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		setElementValue("moeda.idMoeda", getNestedBeanPropertyValue(data,"moeda.idMoeda"));
		setElementValue("moeda.siglaSimbolo", getNestedBeanPropertyValue(data,"moeda.siglaSimbolo"));
		setElementValue("filialUsuario.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
		setElementValue("filialUsuario.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
		setElementValue("filialUsuario.pessoa.nmFantasia",getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
		if (getNestedBeanPropertyValue(data, "blFilialIsMatriz") == "false") {
			setElementValue("filialLocalizacao.idFilial",getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filialLocalizacao.sgFilial",getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filialLocalizacao.pessoa.nmFantasia",getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
			setDisabled("filialLocalizacao.idFilial", false);
		} else {
			resetValue("filialLocalizacao.idFilial");
			setDisabled("filialLocalizacao.idFilial", false);
		}
	}

	function imprimeRelatorioResumido(form) {
		if (!validateForm(form)) {
			return false;
		}

		onChangeAgrupadoPor();
		
		if (validateFieldsToPrintReport()) {
			reportButtonScript('lms.carregamento.emitirRelatorioCargasAction.executeRelatorioResumido','openPdf', document.forms[0]);
		}else{
			alert("LMS-05399 - " + i18NLabel.getLabel("LMS-05399"));
			
		}
	}

	function imprimeRelatorioDetalhado(form) {
		if (!validateForm(form)) {
			return false;
		}
		
		onChangeAgrupadoPor();
				
		if (validateFieldsToPrintReport()) {
			reportButtonScript('lms.carregamento.emitirRelatorioCargasAction.executeRelatorioDetalhado','openPdf', document.forms[0]);
		}else{
			alert("LMS-05399 - " + i18NLabel.getLabel("LMS-05399"));	
		}
	}
	
	function validateFieldsToPrintReport()
	{
		if (validateRuleFilialAndMercadoria()) return true;
		if (validateRuleRemetenteOrDestinatario()) return true;

		return false;
	}
	
	function validateRuleFilialAndMercadoria()
	{
 		if (document.getElementById("localizacaoMercadoria.cdLocalizacaoMercadoria").value != "" && document.getElementById("filialLocalizacao.idFilial").value != "") 
 			return true;
 		else
 			return false;
	}
	
	function validateRuleRemetenteOrDestinatario()
	{
		if (document.getElementById("clienteRemetente.idCliente").value != "" || document.getElementById("clienteDestinatario.idCliente").value != "") 
			return true;
		else
			return false;
	}	
	
	function desabilitaCampoAgrupadoPor(){
		if (getElementValue("formatoRelatorio") == "pdf") {
			var combo = document.getElementById("agrupadoPor");
			setDisabled("agrupadoPor", true);
			setElementValue("agrupadoPor", "FD");
		}else{
			setDisabled("agrupadoPor", false);
		}
	}
	function onChangeTpDestinatario() {
		var combo = document.getElementById("tpDestinatario");
		setElementValue('dsTpDestinatario', combo.options[combo.selectedIndex].text);
	}

	function onChangeAgrupadoPor() {
		var combo = document.getElementById("agrupadoPor");
		setElementValue('dsAgrupadoPor', combo.options[combo.selectedIndex].text);
	}
	
	function limpaTela() {
		cleanButtonScript(this.document);
		loadDataUsuario();
		desabilitaCampoAgrupadoPor();
	}
	
	function onclickBlAgendamento() {
		document.getElementById("blDocumentosComProdutoPerigoso").checked = false;
		document.getElementById("blDocumentosComProdutoControlado").checked = false;
	}
	
	function onclickBlProduto() {
		document.getElementById("blDocumentosComAgendamento").checked = false;
	}
	
	function hideOrShowAgendamento() {
		var localizacao = document.getElementById("localizacaoMercadoria.cdLocalizacaoMercadoria").value;
		if(localizacao === '24'){
			setRowVisibility("blAguardandoAgendamento", false, document);
			setRowVisibility("blDocumentosComAgendamento", true, document);
			setRowVisibility("blDocumentosComProdutoPerigoso", true, document);
			setRowVisibility("blDocumentosComProdutoControlado", true, document);
			setElementValue("blAguardandoAgendamento", false);
			setRowVisibility("blRoteirizacao", true, document);
		}else if(localizacao === '5'){
			setRowVisibility("blAguardandoAgendamento", true, document);
			setRowVisibility("blDocumentosComAgendamento", false, document);
			setRowVisibility("blDocumentosComProdutoPerigoso", false, document);
			setRowVisibility("blDocumentosComProdutoControlado", false, document);
			setElementValue("blDocumentosComAgendamento", false);
			setRowVisibility("blRoteirizacao", true, document);
			setElementValue("blDocumentosComProdutoPerigoso", false);
			setElementValue("blDocumentosComProdutoControlado", false);
		}else {
			setRowVisibility("blAguardandoAgendamento", false, document);
			setRowVisibility("blDocumentosComAgendamento", false, document);
			setRowVisibility("blDocumentosComProdutoPerigoso", false, document);
			setRowVisibility("blDocumentosComProdutoControlado", false, document);
			setElementValue("blAguardandoAgendamento", false);
			setElementValue("blDocumentosComAgendamento", false);
			setRowVisibility("blRoteirizacao", false, document);
			setElementValue("blRoteirizacao", false);
			setElementValue("blDocumentosComProdutoPerigoso", false);
			setElementValue("blDocumentosComProdutoControlado", false);
		}
	}

</script>