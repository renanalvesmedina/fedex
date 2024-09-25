<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterRegistrosVisitaAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/vendas/manterRegistrosVisita" idProperty="idVisita">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01127"/>
	</adsm:i18nLabels>

	<adsm:hidden property="idProcessoWorkflow" />
	<adsm:hidden property="idGerente" serializable="false"/>
	<adsm:hidden property="tpAcesso" value="F"/>

	<adsm:lookup 
		property="filial" 
		idProperty="idFilial" 
		required="false" 
		criteriaProperty="sgFilial" 
		maxLength="3"
		service="lms.vendas.manterRegistrosVisitaAction.findLookupFilial" 
			dataType="text"
		label="filial" size="3"
		action="/municipios/manterFiliais" 
		labelWidth="10%" width="43%" 
		minLengthForAutoPopUpSearch="3"
		exactMatch="false" disabled="false" onDataLoadCallBack="filialCallBack" afterPopupSetValue="filialAfterPopupSetValue">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="siglaDescricao" modelProperty="lastRegional.dsRegional" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
	</adsm:lookup>

	<adsm:hidden property="idRegional" serializable="false" />
	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="35"
		maxLength="60"
		width="41%"
		labelWidth="6%"
		serializable="false"
		required="false"
	/>

	<adsm:lookup
		action="/configuracoes/consultarFuncionariosView"
		service="lms.vendas.manterRegistrosVisitaAction.findLookupFuncionario"
		dataType="text"
		property="usuarioByIdUsuario"
		idProperty="idUsuario"
		criteriaProperty="nrMatricula"
		label="funcionario"
		size="17"
		maxLength="10"
		exactMatch="true"
		width="43%"
		labelWidth="10%"
		onDataLoadCallBack="funcCallBack"
		onPopupSetValue="funcCallBack"
	>
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.nmUsuario"
			modelProperty="nmUsuario"/>
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.vfuncionario.dsFuncao"
			modelProperty="dsFuncao"/>
		<adsm:textbox
			dataType="text" 
			property="usuarioByIdUsuario.nmUsuario" 
			size="35" 
			maxLength="50" 
			disabled="true"
			serializable="false"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="text" 
			label="cargo"
			property="usuarioByIdUsuario.vfuncionario.dsFuncao" 
			maxLength="30" 
			size="35"
			disabled="true" 
			width="20%"
			labelWidth="6%"
			serializable="false"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			maxLength="18"
			dataType="text"
			width="43%"
			labelWidth="10%">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>

		<adsm:combobox
			property="tipoVisita.idTipoVisita" 
			label="tipoVisita"
			optionLabelProperty="dsTipoVisita"
			optionProperty="idTipoVisita" 
			service="lms.vendas.manterRegistrosVisitaAction.findTipoVisita"
			labelWidth="10%" 
			width="81%"/>	

		<adsm:range label="dataVisita" width="43%" labelWidth="10%">
			<adsm:textbox dataType="JTDate" property="dtVisitaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVisitaFinal" />
		</adsm:range>
		<adsm:range label="dataGeracao" labelWidth="12%">
			<adsm:textbox dataType="JTDate" property="dtRegistroInicial" />
			<adsm:textbox dataType="JTDate" property="dtRegistroFinal" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="visita" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
		idProperty="idVisita" 
		property="visita" 
		unique="true"
		rowCountService="lms.vendas.manterRegistrosVisitaAction.getRowCountCustom" 
		service="lms.vendas.manterRegistrosVisitaAction.findPaginatedCustom"
		gridHeight="220"
		rows="10">
		<adsm:gridColumn
			title="funcionario"
			property="usuarioByIdUsuario.nmUsuario" 
			dataType="text"
			width="35%" />
		<adsm:gridColumn
			title="filial" 
			property="filial.sgFilial" 
			dataType="text"
			width="10%" />
		<adsm:gridColumn
			title="cliente" 
			property="cliente.pessoa.nmPessoa" 
			width="40%"
			dataType="text" />
		<adsm:gridColumn
			title="dataVisita" 
			property="dtVisita" 
			width="15%"
			dataType="JTDate" 
			align="center" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script type="text/javascript">

/*DADOS BÁSICOS DA TELA*/
	var msgException = null;
	var teveException = null;

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var idRegional = null;
	var siglaRegional = null;

	var idUsuario = null;
	var nrMatricula = null;
	var nmUsuario = null;
	var dsFuncaoUsuario = null;

	var idUsuarioVisto = null;
	var nmUsuarioVisto = null;	

	var isGerente = null;
	var changeFilial = null;	

	var isMatriz = null;

	function myOnPageLoad() {
		onPageLoad();
		var url = new URL(parent.location.href);
		if (url.parameters != undefined 
			&& url.parameters.idProcessoWorkflow != undefined 
			&& url.parameters.idProcessoWorkflow != '') {
			return;
		}
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.manterRegistrosVisitaAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function myOnPageLoadCallBack_cb(data, error) {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined 
			&& url.parameters.idProcessoWorkflow != undefined 
			&& url.parameters.idProcessoWorkflow != '') {
			setDisabledTabCustom("etapas",false);
			getTabGroup(document).selectTab("cad", "tudoMenosNulo", true);
			return;
		}
		onPageLoad_cb(data, error);
	}
	
	function dataSession_cb(data,error) {
		teveException = getNestedBeanPropertyValue(data, "teveException");

		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");

		idRegional = getNestedBeanPropertyValue(data, "regional.idRegional");
		siglaRegional = getNestedBeanPropertyValue(data, "regional.siglaDescricao");

		idUsuario = getNestedBeanPropertyValue(data, "usuario.idUsuario");
		nmUsuario = getNestedBeanPropertyValue(data, "usuario.nmUsuario");
		nrMatricula = getNestedBeanPropertyValue(data, "usuario.nrMatricula");
		dsFuncaoUsuario = getNestedBeanPropertyValue(data, "usuario.dsFuncao");		

		dtAtual = getNestedBeanPropertyValue(data, "dtAtual");
		dtAtualMinus15 = getNestedBeanPropertyValue(data, "dtAtualMinus15");

		idUsuarioVisto = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.idUsuario");
		nmUsuarioVisto = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.nmUsuario");

		isGerente = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.isGerente");
		changeFilial = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.changeFilial");

		isMatriz = getNestedBeanPropertyValue(data, "isMatriz");
		
		writeDataSession();
		initWindow(null);
		
		if(teveException != null){
			setDisabledTabCustom("cad", true);
			alert(''+ i18NLabel.getLabel("LMS-01127"));
		}
	}

	function initWindow(eventObj) {
		
		if(isMatriz != 'true') {
		//desabilita a aba etapas
		if(eventObj!=null){
			setDisabledTabCustom("etapas", true);
		}
			
		//se nao for um gerente comercial
		if(isGerente == 'true') { 
			setDisabled("usuarioByIdUsuario.nrMatricula", false);
			setDisabled("usuarioByIdUsuario.idUsuario", false);
		} else {
			setDisabled("usuarioByIdUsuario.nrMatricula", true);
			setDisabled("usuarioByIdUsuario.idUsuario", true);
		}

		//pode trocar a filial
		if(changeFilial == 'true') {
			setDisabled("filial.idFilial", false); 
		} else {
			setDisabled("filial.idFilial", true);
		}

		if(eventObj != null){
			if(eventObj.name == "tab_click" || eventObj.name == 'cleanButton_click'){
				writeDataSession();
			}
		}
		}else{
			setDisabled("filial.idFilial", false);
			setDisabled("usuarioByIdUsuario.nrMatricula", false);
			setDisabled("usuarioByIdUsuario.idUsuario", false);
			if(eventObj!=null){
				setDisabledTabCustom("etapas", true);
	}
		}
	}

	/**
	 * Preenche os dados basicos da tela
	 */
	function writeDataSession() {
		
		if(isMatriz != 'true') {
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);

		setElementValue("idRegional", idRegional);
		setElementValue("siglaDescricao", siglaRegional);

		if(!hasValue(getElementValue("dtVisitaInicial"))) {
			setElementValue("dtVisitaInicial", setFormat(getElement("dtVisitaInicial"),dtAtualMinus15));
		}
		if(!hasValue(getElementValue("dtVisitaFinal"))) {
			setElementValue("dtVisitaFinal", setFormat(getElement("dtVisitaFinal"),dtAtual));
		}
		//se o usuario nao é o gerente comercial da filial
		if(idUsuario != idUsuarioVisto) {
			writeDataUsuario();
		}
		}else{
			setDisabled("filial.idFilial", false);
			setDisabled("usuarioByIdUsuario.nrMatricula", false);
			setDisabled("usuarioByIdUsuario.idUsuario", false);
	}
	}

	/**
	 * Preenche os dados do usuário
	 */
	function writeDataUsuario(){
		setElementValue("usuarioByIdUsuario.idUsuario", idUsuario);
		setElementValue("usuarioByIdUsuario.nrMatricula", nrMatricula);
		setElementValue("usuarioByIdUsuario.nmUsuario", nmUsuario);
		setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", dsFuncaoUsuario);
	}	

	function clickRow(){
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.manterRegistrosVisitaAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});		
	}

	/**
	 * Abilita/Desabilita tabs
	 */
	function setDisabledTabCustom(tabName,disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab(tabName, disabled);
	}

	/***** Lookup Funcionario *****/
	function funcCallBack_cb(data){
		if(!usuarioByIdUsuario_nrMatricula_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data[0].idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data[0].nmFuncionario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data[0].dsFuncao);
		}
		return;
	}

	function funcCallBack(data){
		if(data != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data.idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data.nmUsuario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data.dsFuncao);
		}
		return;
	}
	
	function filialAfterPopupSetValue(data) {
		if(data != undefined) {
			if(data.error) {
				alert(data.error);
				return;
			}
			var sdo = createServiceDataObject("lms.vendas.manterRegistrosVisitaAction.validaFilialSelecionadaById", "filialAfterPopupSetValue", {'idFilial': data.idFilial});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function filialAfterPopupSetValue_cb(data, error) {
		if(error) {
			alert(error);
			resetFilial();
			return;
		}
	}
	
	function filialCallBack_cb(data){
		if(!filial_sgFilial_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			if(data[0].error) {
				alert(data[0].error);
				writeDataSession();
				return;
			}
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("siglaDescricao", data[0].lastRegional.dsRegional);
		}
		return;
	}
	
	function resetFilial() {
		setElementValue("filial.idFilial", '');
		setElementValue("filial.sgFilial", '');
		setElementValue("filial.pessoa.nmFantasia", '');
		setElementValue("siglaDescricao", '');
	}
</script>