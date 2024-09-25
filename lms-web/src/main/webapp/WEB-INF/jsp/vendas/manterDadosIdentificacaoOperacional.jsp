<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterClienteAction"
	onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form
		action="/vendas/manterDadosIdentificacao"
		id="formOperacional"
		idProperty="idCliente"
		height="230">

		<adsm:hidden property="idInformacaoDoctoCliente" serializable="false"/>
		<adsm:hidden property="idInformacaoDoctoClienteEDI" serializable="false"/>

		<adsm:hidden property="pessoa.tpPessoa" serializable="false"/>
		<adsm:hidden property="pessoa.tpIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nmPessoa" serializable="false"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="temPendenciaWKFilialOpe" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false"/>
		<adsm:hidden property="blNfeConjulgada"  serializable="true"/>

		<adsm:lookup
			label="filialResponsavel"
			property="filialByIdFilialAtendeOperacional"
			service="lms.vendas.manterClienteAction.findLookupFilial"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%"
			required="true">
			<adsm:propertyMapping
				criteriaProperty="tpAcesso"
				relatedProperty="tpAcesso"/>
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialAtendeOperacional.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialAtendeOperacional.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="filialRespSolicitada"
			property="filialByIdFilialOperacionalSolicitada"
			service="lms.vendas.manterClienteAction.findLookupFilial"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%"
			required="true">
			<adsm:propertyMapping
				criteriaProperty="tpAcesso"
				relatedProperty="tpAcesso"/>
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialOperacionalSolicitada.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialOperacionalSolicitada.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="regionalResponsavel"
			property="regionalOperacional"
			service="lms.vendas.manterClienteAction.findLookupRegional"
			action="/municipios/manterRegionais"
			idProperty="idRegional"
			criteriaProperty="sgRegional"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%">

			<adsm:propertyMapping
				relatedProperty="regionalOperacional.dsRegional"
				modelProperty="dsRegional"/>

			<adsm:textbox
				dataType="text"
				property="regionalOperacional.dsRegional"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:combobox
			label="observacaoDoctoServico"
			property="observacaoConhecimento.idObservacaoConhecimento"
			optionProperty="idObservacaoConhecimento"
			optionLabelProperty="dsObservacaoConhecimento"
			onlyActiveValues="true"
			service="lms.vendas.manterClienteAction.findObservacaoConhecimento"
			labelWidth="29%"
			width="71%"
			boxWidth="230"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:combobox
			label="grauDificuldadeColeta"
			property="tpDificuldadeColeta"
			onlyActiveValues="true"
			domain="DM_GRAU_DIFICULDADE"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"
			required="true"
		/>

		<adsm:combobox
			label="grauDificuldadeEntrega"
			property="tpDificuldadeEntrega"
			onlyActiveValues="true"
			domain="DM_GRAU_DIFICULDADE"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"
			required="true"
		/>

		<adsm:combobox
			label="grauDificuldadeClassificacao"
			property="tpDificuldadeClassificacao"
			onlyActiveValues="true"
			domain="DM_GRAU_DIFICULDADE"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"
			required="true"
		/>

		<adsm:combobox
			property="tpLocalEmissaoConReent"
			onlyActiveValues="true"
			domain="DM_LOCAL_EMISSAO_CON_REENT"
			label="localEmissaoConhecimentoReentrega"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:checkbox
			property="blAgendamentoPessoaFisica"
			label="agendamentoPessoaFisica"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAgendamentoPessoaJuridica"
			label="agendamentoPessoaJuridica"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blColetaAutomatica"
			label="coletaAutomatica"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAgrupaNotas"
			label="agrupamentoNotasFiscais"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blCadastradoColeta"
			label="cadastroColeta"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blObrigaRecebedor"
			label="obrigaInformarRecebedor"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blDificuldadeEntrega"
			onclick="validateDificuldadeEntrega()"
			label="dificuldadeEntrega"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blRetencaoComprovanteEntrega"
			label="retencaoComprovanteEntrega"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blVeiculoDedicado"
			onclick="validateVeiculoDedicado()"
			label="veiculoDedicado"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAgendamentoEntrega"
			label="agendamentoEntrega"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blPaletizacao"
			label="paletizacao"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blCustoDescarga"
			label="custoDescarga"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blPermiteCte"
			label="permiteEmissaoCte"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blObrigaSerie"
			label="obrigaSerieNF"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAgendamento"
			label="possuiAgendamento"
			onclick="validatePossuiAgendamento()"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blConfAgendamento"
			label="confirmaAgendamento"
			onclick="validateConfirmaAgendamento()"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blRecolheICMS"
			label="recolheIcms"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blPesagemOpcional"
			label="pesagemOpcional"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blPaleteFechado"
			label="paletesFechados"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

		<adsm:checkbox
			property="blEtiquetaPorVolume"
			label="etiquetaPorVolume"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blPermiteEmbarqueRodoNoAereo"
			label="permiteEmbarqueRodoAereo"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

		<adsm:checkbox
			property="blSeguroDiferenciadoAereo"
			label="seguroDiferenciadoAereo"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
			
		<adsm:checkbox
			property="blClienteCCT"
			label="clienteCCT"
			labelWidth="29%"
			width="21%"
			disabled="false"
		/>
	
		<adsm:checkbox
			property="blEmissorNfe"
			label="emissorNFe"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

		<adsm:checkbox
			property="bldesconsiderarDificuldade"
			label="desconsiderarDificuldadeEntrega"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

		<adsm:checkbox
			property="blEmissaoDiaNaoUtil"
			label="emissaoDiaNaoUtil"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

		<adsm:checkbox
			property="blObrigaBO"
			label="obrigaBONaGeracao"
			labelWidth="29%"
			width="21%"
			disabled="false"
			onclick="controleTpModalObrigaBO()"
		/>
			
		<adsm:checkbox
			property="blEmissaoSabado"
			label="emissaoSabado"
			labelWidth="29%"
			width="21%"
			disabled="false"
		/>		
			
		<adsm:checkbox
			property="blRemetenteOTD"
			label="prazoEspecialClientesSelecionados"
			labelWidth="29%"
			width="21%"
			disabled="false"
			onclick="updateRemetentesOTDButtonStatus()"
		/>
		
		<adsm:combobox
			property="tpModalObrigaBO"
			onlyActiveValues="true"
			domain="DM_MODAL_OBRIGA_BO"
			label="modal"
			labelWidth="29%"
			width="21%" style="width:90px;"
			autoLoad="false"
			renderOptions="true"
			disabled="false"/>
		
		<adsm:checkbox
			property="blGerarParcelaFreteValorLiquido"
			label="gerarParcelaFreteValorLiquido"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

		<adsm:combobox
			property="tpEmissaoDoc"
			onlyActiveValues="true"
			domain="DM_TIPO_EMISSAO_DOC_CLIENTE"
			label="tpGeracaoDocto"
			labelWidth="29%"
			width="21%" style="width:90px;"
			autoLoad="false"
			renderOptions="true"
			disabled="false"/>
			
	  <adsm:checkbox
			property="blGeraNovoDPE"
			label="geraNovoDPE"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
	
	
	  <adsm:checkbox
			property="blAssinaturaDigital"
			label="assinaturaDigital"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

          <adsm:checkbox
			property="blDpeFeriado"
			label="consideraFeriadoDiaUtil"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
          
          <adsm:checkbox
			property="blObrigaComprovanteEntrega"
			label="obrigaComprovanteEntrega"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
          
          <adsm:checkbox
			property="blObrigaRg"
			label="obrigaRg"
			labelWidth="29%"
			width="21%"
			disabled="false"/>

          <adsm:checkbox
			property="blPermiteBaixaParcial"
			label="permiteBaixaParcial"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
          
          <adsm:checkbox
			property="blObrigaBaixaPorVolume"
			label="obrigaBaixaPorVolume"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
          
          <adsm:checkbox
			property="blObrigaQuizBaixa"
			label="obrigaQuizBaixa"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
          
          <adsm:checkbox
			property="blObrigaParentesco"
			label="obrigaParentesco"
			labelWidth="29%"
			width="21%"
			disabled="false"/>
			          
		<adsm:section caption="informacoesParaEDI" />

		<adsm:combobox
			property="tpAgrupamentoEDI"
			onlyActiveValues="true"
			domain="DM_TIPO_AGRUPAMENTO_EDI"
			label="tipoAgrupamentoEDI"
			labelWidth="29%"
			width="21%" style="width:90px;"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:combobox
			label="campoAgrupamentoEspecial"
			property="informacaoDoctoCliente.idInformacaoDoctoCliente"
			optionProperty="idInformacaoDoctoCliente"
			optionLabelProperty="dsCampo"
			onlyActiveValues="true"
			onchange="setValueComboAEspecial();"
			service="lms.vendas.manterInformacoesDocumentoClienteAction.findInformacaoDoctoCliente"
			labelWidth="29%" style="width:150px;"
			width="21%"
			renderOptions="true"/>

		<adsm:combobox
			property="tpOrdemEmissaoEDI"
			onlyActiveValues="true"
			domain="DM_TP_ORDEM_EMISSAO_CTRC_EDI"
			label="ordemEmissaoCtrcEDI"
			labelWidth="29%"
			width="21%" style="width:90px;"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:checkbox
			property="blNumeroVolumeEDI"
			label="volumesInfoEDI"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blUtilizaPesoEDI"
			label="utilizarPesoInformadoEDI"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAtualizaDestinatarioEdi"
			label="atualizarDestinatarioEdi"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blUtilizaFreteEDI"
			label="utilizarFreteInformadoEDI"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAtualizaConsignatarioEdi"
			label="atualizarConsignatarioEdi"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blAtualizaIEDestinatarioEdi"
			label="atualizarIEDestinatarioEdi"
			labelWidth="29%"
			width="21%"/>
			
		<adsm:checkbox
			property="blLiberaEtiquetaEdi"
			label="liberaEtiquetaEdi"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blSemChaveNfeEdi"
			label="semChaveNfeEdi"
			labelWidth="29%"
			width="21%"/>

		<adsm:combobox
			label="processamentoEspecialEDI"
			property="informacaoDoctoClienteEDI.idInformacaoDoctoCliente"
			optionProperty="idInformacaoDoctoCliente"
			optionLabelProperty="dsCampo"
			onlyActiveValues="true"
			onchange="setValueComboAEspecialEDI();"
			service="lms.vendas.manterInformacoesDocumentoClienteAction.findInformacaoDoctoCliente"
			labelWidth="29%" style="width:150px;"
			width="21%"
			renderOptions="true"/>

		<adsm:checkbox
			property="blAtualizaRazaoSocialDest"
			label="atualizaRazaoSocialDestEdi"
			labelWidth="29%"
			width="21%"/>
			
		<adsm:checkbox
			property="blObrigaPesoCubadoEdi"
			label="obrigaPesoCubadoEdi"
			labelWidth="29%"
			width="21%"/>
			
		<adsm:checkbox
			property="blDivisao"
			label="utilizarDivisaoEdi"
			labelWidth="29%"
			width="21%"/>


		<adsm:textbox label="limiteValorMercadoriaCteAereo" property="vlLimiteValorMercadoriaCteAereo" dataType="currency" labelWidth="29%" width="21%" size="10"/>
		<adsm:textbox label="limiteValorMercadoriaCteRodo" property="vlLimiteValorMercadoriaCteRodo" dataType="currency" labelWidth="29%" width="21%" size="10"/>

		<adsm:buttonBar lines="2">
			<adsm:button
				caption="coletasAutomaticas"
				id="btnColetasAutomaticas"
				onclick="return changePage('vendas/manterHorariosColetaAutomaticaCliente.do?cmd=main');"
				boxWidth="140"/>

			<adsm:button
				caption="horariosCorteColetaEntrega"
				id="btnHorariosCorteColetaEntrega"
				onclick="return changePage('vendas/manterHorariosCorteColetaEntrega.do?cmd=main');"
				boxWidth="230"/>

			<adsm:button
				caption="prazosEntrega"
				id="btnPrazosEntrega"
				onclick="return changePage('vendas/manterPrazosEntregaCliente.do?cmd=main');"
				boxWidth="130"/>

			<adsm:button
				caption="ciasAereas"
				id="btnCiasAereas"
				onclick="return changePage('vendas/manterCiasAereasCliente.do?cmd=main');"
				breakBefore="true"/>

			<adsm:button
				caption="regioes"
				id="btnRegioes"
				onclick="return changePage('vendas/manterRegioesCliente.do?cmd=main');"/>

			<adsm:button
				caption="seguros"
				id="btnSeguros"
				onclick="return changePage('vendas/manterSegurosCliente.do?cmd=main');"/>

			<adsm:button
				caption="despachantes"
				id="btnDespachantes"
				onclick="return changePage('vendas/manterDespachantesCliente.do?cmd=main');"/>

			<adsm:button
				caption="documentos"
				id="btnDocumentos"
				onclick="return changePage('vendas/manterDocumentosCliente.do?cmd=main');"/>

			<adsm:button
				caption="eventos"
				id="btnEventos"
				onclick="return changePage('vendas/manterEventosCliente.do?cmd=main');"/>

			<adsm:button
				caption="ocorrencias"
				id="btnOcorrencias"
				onclick="return changePage('vendas/manterOcorrenciasCliente.do?cmd=main');"/>

			<adsm:button
				caption="remetentesOTD"
				id="btnRemetentesOTD"
				onclick="return changePage('vendas/manterRemetentesOTD.do?cmd=main');"
				boxWidth="140"/>

		</adsm:buttonBar>

		<script>
			var lms_01217 = '<adsm:label key="LMS-01217"/>';
		</script>

	</adsm:form>
</adsm:window>
<script>
	var tpCliente = {EVENTUAL:'E', ESPECIAL:'S', POTENCIAL:'P', FILIAL:'F'};
	var modo = {INCLUSAO:"1", EDICAO:"2"};
	var firstLoad = true;

	getElement("labelPessoaTemp").masterLink = "true";
	getElement("tpAcesso").masterLink = "true";

	function myOnShow() {
		hideMessage(this.document);
		return false;
	}

	function loadData(data) {
		firstLoad = false;

		var idc = data.idInformacaoDoctoCliente;
		if(idc != undefined){
			setElementValue("idInformacaoDoctoCliente",idc);
		}
		if(data.informacaoDoctoClienteEDI != undefined) {
			var idce = data.informacaoDoctoClienteEDI.idInformacaoDoctoCliente;
			if(idce != undefined){
				setElementValue("idInformacaoDoctoClienteEDI",idce);
			}
		}

		loadComboAEspecial(data);
		loadComboAEspecialEDI(data);

		onDataLoad_cb(data);
		verificaDadosDefault();
	}
	
	function reloadEtiquetaPorVolume() {
		setElementValue("blEtiquetaPorVolume",false);
	}

	function reloadBlEmissaoSabado() {
		setElementValue("blEmissaoSabado",false);
	}
	
	function loadComboAEspecial(dados){
		var sdo = createServiceDataObject(
				"lms.vendas.manterInformacoesDocumentoClienteAction.findInformacaoDoctoCliente",
				"loadComboAEspecial", dados);

		xmit({serviceDataObjects:[sdo]});
	}
	function loadComboAEspecial_cb(data){
		if (data != undefined) {
			comboboxLoadOptions({e:document.getElementById("informacaoDoctoCliente.idInformacaoDoctoCliente"), data:data});
			setElementValue("informacaoDoctoCliente.idInformacaoDoctoCliente", getElementValue('idInformacaoDoctoCliente'));
		}
	}
	function validateVeiculoDedicado(){
		if(document.getElementById("blDificuldadeEntrega").checked){
			alert(lms_01217);
			document.getElementById("blVeiculoDedicado").checked = false;
		}
	}
	function validateConfirmaAgendamento(){
		if(document.getElementById("blConfAgendamento").checked){
			document.getElementById("blAgendamento").checked = true;
		}
	}
	function validatePossuiAgendamento(){
		if(!document.getElementById("blAgendamento").checked){
			document.getElementById("blConfAgendamento").checked = false;
		}
	}
	function validateDificuldadeEntrega(){
		if(document.getElementById("blVeiculoDedicado").checked){
			alert(lms_01217);
			document.getElementById("blDificuldadeEntrega").checked = false;
		}
	}
	function loadComboAEspecialEDI(dados){
		var sdo = createServiceDataObject(
				"lms.vendas.manterInformacoesDocumentoClienteAction.findInformacaoDoctoCliente",
				"loadComboAEspecialEDI", dados);

		xmit({serviceDataObjects:[sdo]});
	}
	function loadComboAEspecialEDI_cb(data){
		if (data != undefined) {
			comboboxLoadOptions({e:document.getElementById("informacaoDoctoClienteEDI.idInformacaoDoctoCliente"), data:data});
			setElementValue("informacaoDoctoClienteEDI.idInformacaoDoctoCliente", getElementValue('idInformacaoDoctoClienteEDI'));
		}
	}

	function setValueComboAEspecial(){
		setElementValue("idInformacaoDoctoCliente",getElementValue("informacaoDoctoCliente.idInformacaoDoctoCliente"));
	}
	function setValueComboAEspecialEDI(){
		setElementValue("idInformacaoDoctoClienteEDI",getElementValue("informacaoDoctoClienteEDI.idInformacaoDoctoCliente"));
	}

	function clean() {
		resetValue(document);
	}

	function exibirPopupWKFilial(){
		return getElementValue("filialByIdFilialAtendeOperacional.idFilial") !== getElementValue("filialByIdFilialOperacionalSolicitada.idFilial") && getElementValue("temPendenciaWKFilialOpe") !== "true";
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
			var mode = telaCad.getModoTela();
			var tipoCliente = telaCad.getTpCliente();

			enableFields(mode, telaCad.getTpClienteRegras());

			var idc = getElementValue("idInformacaoDoctoCliente");
			if (idc != undefined && idc != "") {
				setElementValue(
						"informacaoDoctoCliente.idInformacaoDoctoCliente", idc);
			}
			var idce = getElementValue("idInformacaoDoctoClienteEDI");
			if (idce != undefined && idce != "") {
				setElementValue(
						"informacaoDoctoClienteEDI.idInformacaoDoctoCliente",
						idce);
			}

			if(telaCad.getIdProcessoWorkflow() != undefined && telaCad.getIdProcessoWorkflow() != ''){
				disableButtons(true);
				disableAll();
			}
		}
		
		if (mode == modo.INCLUSAO){
			if (getElementValue("blEmissorNfe") == "") {
				setElementValue("blEmissorNfe", "S");
			}
		}
		controleTpModalObrigaBO();
	}

	function enableFields(mode, tipoCliente) {
		changeAbaStatus(false);
		disableAll();

		setDisabled("blAgendamento", false);
		setDisabled("blConfAgendamento", false);
		setDisabled("blRecolheICMS", false);
		setDisabled("blPermiteEmbarqueRodoNoAereo", false);
		setDisabled("blSeguroDiferenciadoAereo", false);
		setDisabled("blClienteCCT", false);
		setDisabled("blEmissorNfe", false);
		setDisabled("bldesconsiderarDificuldade", false);
		setDisabled("blGerarParcelaFreteValorLiquido", false);
		setDisabled("blEmissaoDiaNaoUtil", false);
		setDisabled("blEmissaoSabado", false);
		setDisabled("blObrigaBO", false);
		setDisabled("tpModalObrigaBO", false);
		setDisabled("blGeraNovoDPE", false);
		setDisabled("blAssinaturaDigital", false);
        setDisabled("blDpeFeriado", false);	
        setDisabled("blObrigaComprovanteEntrega", false);	
        setDisabled("blObrigaRg", false);	
        setDisabled("blPermiteBaixaParcial", false);	
        setDisabled("blObrigaBaixaPorVolume", false);	
        setDisabled("blObrigaQuizBaixa", false);
        setDisabled("blObrigaParentesco", false);
        setDisabled("blPaleteFechado", false);
        	
		if (tipoCliente == tpCliente.POTENCIAL
				|| tipoCliente == tpCliente.EVENTUAL) {
			setDisabled("tpDificuldadeColeta", false);
			setDisabled("tpDificuldadeClassificacao", false);
			setDisabled("tpDificuldadeEntrega", false);
			setDisabled("blDificuldadeEntrega", false);
			setDisabled("blVeiculoDedicado", false);
			setDisabled("blAgendamentoEntrega", false);
			setDisabled("blPaletizacao", false);
			setDisabled("blEtiquetaPorVolume", false);
			setDisabled("blCustoDescarga", false);

			setDisabled("blPesagemOpcional", false);
		} else if (tipoCliente == tpCliente.ESPECIAL
				|| tipoCliente == tpCliente.FILIAL) {
			setDisabled(this.document, false);
			if (mode == modo.INCLUSAO) {
				disableButtons(true);
			} else if (mode == modo.EDICAO) {
				disableButtons(false);
			}
		}

		if (mode == modo.INCLUSAO) {
			setDisabled("filialByIdFilialOperacionalSolicitada.idFilial", true);
			setDisabled("regionalOperacional.idRegional", true);
			
		} else {
			if(getElementValue("temPendenciaWKFilialOpe") == "true" || getTpSituacaoCliente() === "N"){
				setDisabled("filialByIdFilialOperacionalSolicitada.idFilial", true);
			} else {
				setDisabled("filialByIdFilialOperacionalSolicitada.idFilial", false);
			}

			setDisabled("regionalOperacional.idRegional", false);
		}

		setDisabled("filialByIdFilialAtendeOperacional.idFilial", true);
		setDisabled("filialByIdFilialAtendeOperacional.pessoa.nmFantasia", true);
		setDisabled("filialByIdFilialOperacionalSolicitada.pessoa.nmFantasia", true);
		setDisabled("regionalOperacional.dsRegional", true);
		updateRemetentesOTDButtonStatus();
	}

	function getTpSituacaoCliente(){
		var tabGroup = getTabGroup(this.document);
		var telaIdentificacao = tabGroup.getTab("identificacao").tabOwnerFrame;
		return telaIdentificacao.getTpSituacao();
	}

	function disableAll() {
		setDisabled(this.document, true);
	}

	function changeAbaStatus(status) {
		var tabGroup = getTabGroup(document);
		tabGroup.setDisabledTab("operacional", status);
	}

	function disableButtons(status) {
		setDisabled("btnColetasAutomaticas", status);
		setDisabled("btnHorariosCorteColetaEntrega", status);
		setDisabled("btnPrazosEntrega", status);
		setDisabled("btnCiasAereas", status);
		setDisabled("btnRegioes", status);
		setDisabled("btnSeguros", status);
		setDisabled("btnDespachantes", status);
		setDisabled("btnDocumentos", status);
		setDisabled("btnEventos", status);
		setDisabled("btnOcorrencias", status);
	}

	function ajustaDadosDefault(data) {
		setElementValue("filialByIdFilialAtendeOperacional.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialAtendeOperacional.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialAtendeOperacional.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		setElementValue("filialByIdFilialOperacionalSolicitada.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialOperacionalSolicitada.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialOperacionalSolicitada.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		if (data.regional != undefined) {
			setElementValue("regionalOperacional.idRegional", data.regional.idRegional);
			setElementValue("regionalOperacional.sgRegional", data.regional.sgRegional);
			setElementValue("regionalOperacional.dsRegional", data.regional.dsRegional);
		}
		setElementValue("tpDificuldadeColeta", "0");
		setElementValue("tpDificuldadeEntrega", "0");
		setElementValue("tpDificuldadeClassificacao", "0");
		setElementValue("blAgrupaNotas", "S");
		setElementValue("blPermiteCte", "N");
		setElementValue("blObrigaSerie", "N");
		setElementValue("blEmissorNfe", "S");
		setElementValue("blAgendamento", "N");
		setElementValue("blAtualizaConsignatarioEdi", "S");
	}

	function verificaDadosDefault() {
		if (getElementValue("tpDificuldadeColeta") == "") {
			setElementValue("tpDificuldadeColeta", "0");
		}
		if (getElementValue("tpDificuldadeEntrega") == "") {
			setElementValue("tpDificuldadeEntrega", "0");
		}
		if (getElementValue("tpDificuldadeClassificacao") == "") {
			setElementValue("tpDificuldadeClassificacao", "0");
		}

		if (isEmpty(getElementValue("blAgrupaNotas"))) {
			setElementValue("blAgrupaNotas", "S");
		}
		if (getElementValue("blPermiteCte") == "") {
			setElementValue("blPermiteCte", "N");
		}
		if (getElementValue("blObrigaSerie") == "") {
			setElementValue("blObrigaSerie", "N");
		}
		if (getElementValue("blAgendamento") == "") {
			setElementValue("blAgendamento", "N");
		}
		if (isEmpty(getElementValue("blAtualizaConsignatarioEdi"))) {
			setElementValue("blAtualizaConsignatarioEdi", "S");
		}
	}

	function changeCamposObrigatorios() {
		// nothing to do
	}

	function changePage(url) {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		url += "&idFilial="
				+ getElementValue("filialByIdFilialAtendeOperacional.idFilial");
		telaCad.changePageCliente(url);
	}

	function ajustaDadosDefaultEdicao(tipoCliente, data) {
		if (tipoCliente == tpCliente.POTENCIAL
				|| tipoCliente == tpCliente.EVENTUAL) {

			var idFilial = getElementValue("filialByIdFilialAtendeOperacional.idFilial");
			var sgFilial = getElementValue("filialByIdFilialAtendeOperacional.sgFilial");
			var nmFantasia = getElementValue("filialByIdFilialAtendeOperacional.pessoa.nmFantasia");

			var idRegional = getElementValue("regionalOperacional.idRegional");
			var sgRegional = getElementValue("regionalOperacional.sgRegional");
			var dsRegional = getElementValue("regionalOperacional.dsRegional");

			clean();
			ajustaDadosDefault(data);

			setElementValue("filialByIdFilialAtendeOperacional.idFilial", idFilial);
			setElementValue("filialByIdFilialAtendeOperacional.sgFilial", sgFilial);
			setElementValue("filialByIdFilialAtendeOperacional.pessoa.nmFantasia", nmFantasia);

			setElementValue("filialByIdFilialOperacionalSolicitada.idFilial", idFilial);
			setElementValue("filialByIdFilialOperacionalSolicitada.sgFilial", sgFilial);
			setElementValue("filialByIdFilialOperacionalSolicitada.pessoa.nmFantasia", nmFantasia);

			setElementValue("regionalOperacional.idRegional", idRegional);
			setElementValue("regionalOperacional.sgRegional", sgRegional);
			setElementValue("regionalOperacional.dsRegional", dsRegional);
		}
	}

	function myOnPageLoadCallBack_cb() {
		onPageLoad_cb();
		unblockUiWhenEverythingIsLoaded();
	}
	
	function unblockUiWhenEverythingIsLoaded() {
		if (firstLoad) {
		unblockUI();
	}
	}

	function isEmpty(o) {
		return o == undefined || o == null || o.toString() == "";
	}
	
	function updateRemetentesOTDButtonStatus() {
	    var prazoEspecialSelected = document.getElementById("blRemetenteOTD").checked
	    setDisabled("btnRemetentesOTD", !prazoEspecialSelected);
	}
	
	function controleTpModalObrigaBO() {
		blObrigaBO = getElementValue('blObrigaBO');
		
		
		if(!blObrigaBO) {
			setDisabled('tpModalObrigaBO', true);
			resetValue('tpModalObrigaBO');
		} else {
			setDisabled('tpModalObrigaBO', false);
		}
			
	}
	
</script>