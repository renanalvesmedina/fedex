<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterRegistrosVisitaAction" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-01041"/>
	</adsm:i18nLabels>
	<adsm:form
		action="/vendas/manterRegistrosVisita"
		idProperty="idEtapaVisita"
		id="form_etapa"
		height="186"
		service="lms.vendas.manterRegistrosVisitaAction.findByIdEtapa"
		onDataLoadCallBack="formLoad">

		<adsm:masterLink idProperty="idVisita" showSaveAll="true">
			<adsm:masterLinkItem property="usuarioByIdUsuario.nmUsuario" label="funcionario"/>
			<adsm:masterLinkItem property="dtVisita" label="dataVisita"/>
			<adsm:masterLinkItem property="cliente.pessoa.nmPessoa" label="cliente"/>
		</adsm:masterLink>

		<adsm:hidden property="idUsuario" />
		<adsm:hidden property="idUsuarioVisto"/>
		<adsm:hidden property="dtVisto"/>

		<adsm:hidden property="versao"/>

		<adsm:combobox property="tipoVisita.idTipoVisita" 
			label="tipoVisita"
			optionLabelProperty="dsTipoVisita"
			optionProperty="idTipoVisita" 
			service="lms.vendas.manterRegistrosVisitaAction.findTipoVisita"
			required="true" 
			labelWidth="19%" 
			width="81%"/>

		<adsm:combobox property="campanhaMarketing.idCampanhaMarketing" 
			label="indicadorMarketing" 
			optionLabelProperty="dsCampanhaMarketing" 
			optionProperty="idCampanhaMarketing" 
			service="lms.vendas.manterRegistrosVisitaAction.findIndicadorMarketing"
			onlyActiveValues="true"
			labelWidth="19%" 
			width="31%"
			boxWidth="220"/>

		<adsm:combobox property="tpPerspectivaFaturamento" 
			label="perspectivaFaturamento" 
			domain="DM_PERSPECTIVA_FATUR" 
			required="true" 
			labelWidth="19%" 
			width="28%"/>

		<adsm:textbox property="dsContato" 
			dataType="text" 
			label="contato" 
			required="true" 
			maxLength="60" 
			size="35" 
			labelWidth="19%" 
			width="31%"/>

		<adsm:textbox property="dsAreaAtuacao" 
			dataType="text" 
			label="area" 
			required="true" 
			maxLength="60" 
			size="30" 
			labelWidth="19%" 
			width="28%"/>

		<adsm:textbox property="nrDDI" 
			dataType="text"
			label="ddi"
			maxLength="5" 
			size="10" 
			labelWidth="19%" 
			width="31%"/>

		<adsm:complement label="telefone" 
			required="true" width="31%" labelWidth="19%">
			<adsm:textbox dataType="text" property="nrDDD" maxLength="5" size="10"/>
			<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="20"/>
		</adsm:complement>

		<adsm:textbox property="dsEmail"
			dataType="email" 
			label="email" 
			maxLength="60" 
			required="true" 
			size="35" 
			labelWidth="19%" 
			width="60%"/>

		<adsm:combobox property="tpModal" 
			label="modal" 
			labelWidth="19%" 
			domain="DM_MODAL"
			onchange="findServicoByModalAbrangencia()"
			required="true" 
			width="31%"/>

		<adsm:combobox property="tpAbrangencia" 
			label="abrangencia" 
			domain="DM_ABRANGENCIA"
			onchange="findServicoByModalAbrangencia()"
			required="true" 
			labelWidth="19%" 
			width="28%"/>

		<adsm:listbox property="servicoOferecidos" 
			optionProperty="idServicoOferecido" 
			size="5" 
			boxWidth="400"
			label="servicosBRdescricao"
			onContentChange="contentChange"
			labelWidth="19%"
			width="100%"
			labelStyle="vertical-align: text-top; margin-top: 4px; padding-top:3px">
			<adsm:combobox property="servico"
				optionLabelProperty="dsServico" 
				optionProperty="idServico" 
				service="lms.vendas.manterRegistrosVisitaAction.findServico" />
			<br>
			<adsm:textarea columns="66" 
				rows="3" 
				maxLength="500"
				property="obServicoOferecido"/>
		</adsm:listbox>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novaEtapa" id="cleanButton" onclick="novoClick();"/>
			<adsm:storeButton caption="salvarEtapa" id="storeButton" 
				service="lms.vendas.manterRegistrosVisitaAction.storeEtapa" callbackProperty="storeCallback"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idEtapaVisita" property="etapaVisita" unique="true"
			scrollBars="horizontal" detailFrameName="etapas" 
			rows="5" service="lms.vendas.manterRegistrosVisitaAction.findPaginatedEtapa"
			rowCountService="lms.vendas.manterRegistrosVisitaAction.getRowCountEtapa" 
			onDataLoadCallBack="loadGrid">

		<adsm:gridColumn title="tipoVisita" property="tipoVisita.dsTipoVisita" width="150" />
		<adsm:gridColumn title="indicadorMarketing" property="campanhaMarketing.dsCampanhaMarketing" width="300" />
		<adsm:gridColumn title="perspecFat" property="tpPerspectivaFaturamento" width="100" isDomain="true" />
		<adsm:gridColumn title="contato" property="dsContato" width="200" />
		<adsm:gridColumn title="modal" property="tpModal" width="100" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" width="100" isDomain="true"/>

		<adsm:buttonBar>
			<adsm:button id="emitir" caption="emitir" onclick="emitirReport();" disabled="true"/>
			<adsm:removeButton caption="excluirEtapa" id="removeButton"
				service="lms.vendas.manterRegistrosVisitaAction.removeEtapasByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">

	document.getElementById("idUsuario").masterLink = true;
	document.getElementById("idUsuarioVisto").masterLink = true;
	document.getElementById("dtVisto").masterLink = true;

	function emitirReport() {
		var data = buildFormBeanFromForm(document.forms[0]);
		var sdo = createServiceDataObject('lms.vendas.emitirRegistroVisitaAction.execute', 'openPdf', data); 
		executeReportWindowed(sdo, 'pdf');
	}

	function contentChange(eventObj) {
		if(getElementValue("dtVisto") != null && getElementValue("dtVisto") != ""){ //está aprovado
			return false;
		}
		/** Não permite incluir duas observações para o mesmo Serviço */
		if(eventObj.name == "modifyButton_click") {
			/** Serviço atual */
			var idServico = getElementValue("servicoOferecidos_servico");
			var idServicoOferecido = getElementValue("servicoOferecidos_idServicoOferecido");
			/** Elementos da listBox */
			var listboxElem = getElement("servicoOferecidos");
			for (var idxData = 0; idxData < listboxElem.options.length; idxData++) {
				/** Verifica se já existe algum registro na listBox para esse serviço */
				var data = listboxElem.options[idxData].data;
				if(!hasValue(idServicoOferecido) && data.servico.idServico == idServico) {
					alertI18nMessage("LMS-01041");
					return false;
				}
			}
		}
		return true;
	}

	function testaAprovacao(){
		if(getElementValue("dtVisto") != null && getElementValue("dtVisto") != ""){ //está aprovado
			setDisabled(document, true);
			setDisabled("servicoOferecidos", false);
		} else{
			setDisabled("removeButton", true);
		}
		setDisabled("emitir", isBlank(getElementValue("masterId")));
	}

	function initWindow(eventObj) {
		setDisabled(document, false);
		if(eventObj.name == "tab_click") {
			testaAprovacao();
			novoClick();	
		} else if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton") {
			desabilitaTabEtapas(false);
		}

		if (parent.location.search.indexOf("idProcessoWorkflow")>=0){
			disableAllFields(true);
		}
		
	}

	function disableAllFields(value){
		setDisabled("storeAllButtton",value);
		setDisabled("tipoVisita.idTipoVisita",value);
		setDisabled("campanhaMarketing.idCampanhaMarketing",value);
		setDisabled("tpPerspectivaFaturamento",value);
		setDisabled("dsContato",value);
		setDisabled("dsAreaAtuacao",value);
		setDisabled("nrDDI",value);
		setDisabled("nrDDD",value);
		setDisabled("nrTelefone",value);
		setDisabled("dsEmail",value);
		setDisabled("tpModal",value);
		setDisabled("tpAbrangencia",value);
		setDisabled("servicoOferecidos",value);
		setDisabled("cleanButton",value);
		setDisabled("storeButton",value);
		setDisabled("emitir",value);
		setDisabled("removeButton",value);
		setDisabled("servicoOferecidos_obServicoOferecido",value);
		setDisabled("servicoOferecidos_servico",value);
		setDisabled("servicoOferecidos",value);
	}
	
	function novoClick() {
		cleanButtonScript(this.document);
		setDisabled("cleanButton", false);
		setDisabled("storeButton", false);	
	}

	function desabilitaTabEtapas(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("etapas", disabled);
	}

	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		data.idVisita = getElementValue("masterId");
		etapaVisitaGridDef.executeSearch(data);
	}

	function storeCallback_cb(data, error) {
		if(error==undefined) {
			executeSearch();
		}else{
			if(error.contains('LMS-01216')){
				setFocus(document.getElementById("servicoOferecidos_servico"));
		}
		}
		storeItem_cb(data, error);
	}

/***** Form da aba *****/
	function formLoad_cb(data, error){
		onDataLoad_cb(data,error);
		testaAprovacao();
		if (parent.location.search.indexOf("idProcessoWorkflow")>=0){
			disableAllFields(true);
		}
	}

/***** Grid *****/
	function loadGrid_cb(){
		testaAprovacao();
	}

/***** filtro Servicos *****/
	function findServicoByModalAbrangencia(){
		var data = new Array();
		data.idVisita = getElementValue("masterId");
		data.tpModal = getElementValue("tpModal");
		data.tpAbrangencia = getElementValue("tpAbrangencia");
		var sdo = createServiceDataObject("lms.vendas.manterRegistrosVisitaAction.findServico", "findServicoByModalAbrangencia", data);
		xmit({serviceDataObjects:[sdo]});
	}	

	function findServicoByModalAbrangencia_cb(data, error){
		if (error) {
			alert(error);
			return false;
		}
		comboboxLoadOptions({e:document.getElementById("servicoOferecidos_servico"), data:data});
	}	
</script>