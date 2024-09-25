<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  

<script>

	/**
	 * Faz um pre-carregamento da pagina para setar determinados parametros que nao sao setados em
	 * em seu comportamento natural
	 */
	function preLoadPage() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.getDataUsuario", "loadDataUsuario", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	function loadDataUsuario_cb(data, error) {
		setElementValue("idFilial", data.idFilial);
		setElementValue("sgFilial", data.sgFilial);
		setElementValue("nmFantasia", data.nmFantasia);
		
		document.getElementById("idFilial").masterLink=true;
		document.getElementById("sgFilial").masterLink=true;
		document.getElementById("nmFantasia").masterLink=true;
		
		onPageLoad();
	}
</script>

<adsm:window service="lms.configuracoes.manterJobAction" onPageLoad="preLoadPage">

	<adsm:form action="/configuracoes/manterJob" idProperty="id" id="formJobCad" onDataLoadCallBack="onDetailDataLoad">

		<adsm:hidden property="id" serializable="false"/>
		<adsm:textbox property="idJob" dataType="integer" label="codigo" width="85%" size="15" disabled="true"/>
		
		<adsm:combobox label="grupoExecucao" 
			property="jobGroup"
			service="lms.configuracoes.manterJobAction.findJobGroups"
			optionLabelProperty="jobGroupName" 
			width="40"
			optionProperty="jobGroupId"/>

		<adsm:combobox property="schedulableUnit" service="lms.configuracoes.manterJobAction.findAllSchedulableUnit" 
					label="processo"
					optionProperty="schedulableUnitId" optionLabelProperty="schedulableUnitName" width="80">
			<adsm:propertyMapping criteriaProperty="jobGroup" modelProperty="jobGroupId" />
		</adsm:combobox>					

		<adsm:textbox property="description" dataType="text" required="true"
					  label="descricaoJob" width="85%" size="80"/>
					  
		<%-- 
		<adsm:textbox property="jobGroup" dataType="text" 
					  label="grupoExecucao" size="20" disabled="true"/> 
			--%>					 		  
		<%-- 
		<adsm:lookup dataType="text" property="grupoJob" idProperty="idGrupoJob" criteriaProperty="nmGrupo"
					 service="lms.configuracoes.manterJobAction.findLookupGrupoJob" action="/configuracoes/manterGrupoJob"
					 label="grupoJob" size="40" maxLength="80"/>			
		--%>

		<adsm:textbox property="login" dataType="text" 
					  label="usuario" size="40" maxLength="50" required="true"/>
					  
		<adsm:textbox property="cronExpression" dataType="text" 
					  onchange="validateCronExpression();"
					  label="expressaoCron" size="20" width="85%" maxLength="25" required="true"/>
					  
		<adsm:listbox property="ccUsers" optionProperty="idUsuario" optionLabelProperty="nmUsuario"
					  label="usuariosInformados" width="85%" size="2" boxWidth="320" showOrderControls="false" showIndex="false" serializable="true">	 
					  
            <adsm:lookup dataType="text" property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula"
			     	     service="lms.configuracoes.manterJobAction.findLookupUsuario" 
				         action="/configuracoes/consultarFuncionariosView"
				         onDataLoadCallBack="lookupUsuario" onPopupSetValue="lookupUsuarioPopUp"
				         size="16" maxLength="16">
				<adsm:propertyMapping relatedProperty="ccUsers_usuario.nmUsuario" modelProperty="nmUsuario"/>	
				
				<adsm:propertyMapping criteriaProperty="ccUsers_usuario.nmUsuario" modelProperty="nmUsuario"/>	
				<adsm:propertyMapping criteriaProperty="idFilial" modelProperty="filial.idFilial"/>	
				<adsm:propertyMapping criteriaProperty="sgFilial" modelProperty="filial.sgFilial"/>	
				<adsm:propertyMapping criteriaProperty="nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>	
				
				<adsm:textbox dataType="text" property="usuario.nmUsuario" size="35" maxLength="45" disabled="true" serializable="false"/>
			</adsm:lookup>
		</adsm:listbox>
		
		<adsm:checkbox property="pausaJob" label="pausaJob" onclick="checkMotivo();"/>
		<adsm:textbox dataType="text" property="motivoPausa" label="motivoPausa" disabled="true" size="50"/> 
		
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="sgFilial" serializable="false"/>
		<adsm:hidden property="nmFantasia" serializable="false"/>
		
		<adsm:buttonBar/> 
		
		<script>
			function removeMessage() {
				return "<adsm:label key='LMS-02067'/>";
			}
			function LMS_27077() {
				return "<adsm:label key='LMS-27077'/>";
			}
			function expressaoCronInvalida() {
				return "<adsm:label key='expressaoCronInvalida'/>";
			}
		</script>
		
	</adsm:form>
		
	<adsm:grid property="parameters" idProperty="id" showPagging="false" autoSearch="false"
			   onRowClick="onGridRowClick();" title="parametros" gridHeight="110" scrollBars="vertical">
		<adsm:editColumn property="name" title="nome" dataType="text" field="textbox" width="365" maxLength="50"/>	   
		<adsm:editColumn property="value" title="valor" dataType="text" field="textbox" width="365" maxLength="50"/>
		<adsm:buttonBar>
			<adsm:button id="btnAdicionarParametro" caption="adicionarParametro" onclick="addParameter();"/>
			<adsm:button id="btnRemoverParametro" caption="removerParametro" onclick="removeParameter();"/>
			
			<adsm:button id="btnProximasExecucoes" caption="proximasExecucoes" onclick="showProximasExecucoes();"/>
			<adsm:button id="btnGM" caption="carregarGM" onclick="carregamentoManual();" /><!-- FIXME Remover o botão quando existir a tela GM para execucao da rotina -->
			<adsm:button id="btnSalvar" caption="salvar" onclick="salvar();"/>
			<adsm:button id="btnLimpar" caption="limpar" onclick="resetView();"/>
			<adsm:removeButton id="btnExcluir" service="lms.configuracoes.manterJobAction.removeJob" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	var cronExpressionCache = "";

	function initWindow(eventObj) {
	
		setDisabled("btnExcluir", true);
	
		if (eventObj.name == "tab_click"){
			setButtonsDisable(false);
			
			parametersGridDef.resetGrid();
			 
			parametersGridDef.gridState.data = new Array();
			cronExpressionCache = "";
			
		}
		
		checkDisableBtnProximasExecucoes();
	}
	
	/**
	 * Substitui a funcao padrao de 'onDataLoad'. Decorrente da necessidade de
	 * se ter como parametros para esta tela dois ids (serviceName e executionGroup).
	 *
	 * @param serviceName
	 * @param executionGroup
	 */
	function onDetailDataLoad(executionGroup, serviceName) {
	
		resetValue(this.document);
		resetValue(document.getElementById("ccUsers"));
	
		var data = new Object();
		data.serviceName = serviceName;
		data.executionGroup = executionGroup;
		
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.findById", "onDetailDataLoad", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da rotina de 'onDetailDataLoad'.
	 *
	 * @param data
	 * @param error
	 */
	function onDetailDataLoad_cb(data, error) {
	
		onDataLoad_cb(data,error);
	/**
		setElementValue("id", data.id);
		setElementValue("idJob", data.idJob);
		setElementValue("schedulableUnit", data.schedulableUnit);
		setElementValue("description", data.description);
		setElementValue("jobGroup", data.jobGroup);
		setElementValue("cronExpression", data.cronExpression);
		setElementValue("login", data.login);

		if (data.grupoJob!=undefined) {
			setElementValue("grupoJob.idGrupoJob", data.grupoJob.idGrupoJob);
			setElementValue("grupoJob.nmGrupo", data.grupoJob.nmGrupo);
		}
		
		setElementValue("pausaJob", data.pausaJob);
		setElementValue("motivoPausa", data.motivoPausa);
		*/
		
		cronExpressionCache = data.cronExpression;
		parametersGridDef.gridState.stats = new Object();
		parametersGridDef.gridState.stats.elapsedRenderTime = 0;
		parametersGridDef.gridState.data = new Array();
		populateParameterGrid(data.parameters);
		
		ccUsers_cb(data, error);
		
		setButtonsDisable(false);
		setDisabled("btnExcluir", false);
		
		checkMotivo();
		checkDisableBtnProximasExecucoes();
	}
	
	/**
	 * Valida a expressao cron.
	 */
	function validateCronExpression(){
		
		var cronExpression = getElementValue("cronExpression");
		if (cronExpression != "") {
			if ((cronExpressionCache=="") || (cronExpression != cronExpressionCache)) {
				var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.validateCronExpression", "validateCronExpression", {value:cronExpression});
				xmit({serviceDataObjects:[sdo]});
			}
		}
		
		checkDisableBtnProximasExecucoes();
	}
	
	/**
	 * Callback da validacao da expressao cron.
	 */
	function validateCronExpression_cb(data, error) {
	
		if (data._exception!=undefined) {
			alert(expressaoCronInvalida());
			setFocus("cronExpression");
		} else {
			cronExpressionCache = getElementValue("cronExpression");
		}
		
		return true;
	}
	
	/**
	 * Persiste o registro.
	 *
	 * @param form
	 */
	function salvar() {
	
		if ((validateTabScript(document.getElementById("formJobCad")))==false) return false;
	
		updateGridValues(parametersGridDef.gridState.data);
	
		var data = new Object();
		
		data.id =  getElementValue("id");
		data.idJob =  getElementValue("idJob");
		data.schedulableUnit = getElementValue("schedulableUnit");
		data.jobGroup = getElementValue("jobGroup");
//		data.grupoJob = getElementValue("grupoJob.idGrupoJob");
		data.description = getElementValue("description");
		data.cronExpression = getElementValue("cronExpression");
		data.login = getElementValue("login");
		data.pausaJob = getElementValue("pausaJob");
		data.motivoPausa = getElementValue("motivoPausa");
		
		if (getElementValue("ccUsers")!=undefined) {
			data.idsUsuariosMails = getElementValue("cronExpression");
		} 
		
		data.parameters = parametersGridDef.gridState.data;
			
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.schedule", "salvar", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function carregamentoManual(){
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.executeCarregamentoManual", "carregamentoManual");
		xmit({serviceDataObjects:[sdo]});
	}
	
	function carregamentoManual_cb(data,error){
		if (error!=undefined) {
			alert(error);
		}
	}
	
	function salvar_cb(data, error) {
		if (error!=undefined) {
			alert(error);
			setFocus(document.getElementById("schedulableUnit"), true);
		} else {
			showSuccessMessage();
			
			var tab = getTab(this.document);
			tab.setChanged(false);
			tab.itemTabChanged = false;
			
			setElementValue("idJob", data.idJob);
			setDisabled("btnExcluir", false);
			setFocus(document.getElementById("btnLimpar"), true, true);
		}
	}
	
	/**
	 * Altera o estado da textBox de motivoJob de acordo com o valor da 
	 * checkBox de 'pausaJob'.
	 */
	function checkMotivo() {
		var paused = getElementValue("pausaJob");
		setDisabled("motivoPausa", paused==false);
		document.getElementById("motivoPausa").required = ''+paused;
		if (paused == false) {
			setElementValue("motivoPausa", "");
		}
	}
	
	/** 
	 * Gera um alert com as proximas execucoes do metodo, de acordo com expressao
	 * inserida na cron.
	 */
	function showProximasExecucoes(){	
		var cronExpression = getElementValue("cronExpression");
		if (cronExpression!="") {
			var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.findNextExecutions", "showProximasExecucoes", {value:cronExpression});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/** 
	 * Callback da rotina de proximas execucoes.
	 *
	 * @param data
	 * @param error
	 */
	function showProximasExecucoes_cb(data, error){
		if (data._exception==undefined) {
			alert(data._value);
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Callback da exclusao.
	 *
	 * @param data
	 * @param error
	 */
	function excluir_cb(data, error){
		if (data._exception==undefined) {
			resetView();
			setButtonsDisable(false);
			showSuccessMessage();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Retorno da lookup de metodo.
	 *
	 * @param data
	 * @param error
	 */
/*	function lookupMetodo_cb(data, error) {
		recurso_nmRecurso_exactMatch_cb(data);
		
		if(data.length==1) {
			populateGrupoExecucao(data[0].idMetodo);
			
			if (data[0].recurso!=undefined) {
				setElementValue("idMetodo", data[0].idMetodo);
				setElementValue("nmRecurso", data[0].nmRecurso);
			}
		}
	}
	*/
	
	/**
	 * Retorno da lookup de metodo, quando chamada pela popUp.
	 *
	 * @param data
	 */
	/*function lookupMetodoPopUp(data) {
		populateGrupoExecucao(data.idRecurso);
	}*/
	
	/**
	 * Faz a pesquisa para determinar o grupo de execucao do metodo 
	 * em questao.
	 *
	 * @param
	 */
	/*function populateGrupoExecucao(id) {
		var data = new Object();
		data.idMetodo = id;
		
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.validateJob", "populateGrupoExecucao", data);
		xmit({serviceDataObjects:[sdo]});
	}*/
	
	/**
	 * Retorno data funcao de populateGrupoExecucao;
	 *
	 * @param data
	 * @param error
	 */
	/*function populateGrupoExecucao_cb(data, error) {
		if (error==undefined) {
			setElementValue("grupoExecucao", data.grupoExecucao);
		} else {
			alert(error);
		}
	}*/
	
	/**
	 * Retorno data lookup de usuario.
	 *
	 * @param data
	 * @param error
	 */
	function lookupUsuario_cb(data, error) {
		ccUsers_usuario_nrMatricula_exactMatch_cb(data);
		
		if(data.length==1) {
			validateEmailUsuario(data[0].idUsuario);
		}
	}
	
	/**
	 * Retorno da lookup de usuario, quando chamada pela popUp.
	 *
	 * @param data
	 */
	function lookupUsuarioPopUp(data) {
		validateEmailUsuario(data.idUsuario);
	}
	
	/**
	 * Valida apartir do idUsuario se ele possui um email cadastrado.
	 *
	 * @param idUsuario
	 */
	function validateEmailUsuario(idUsuario) {
		var data = new Object();
		data.idUsuario = idUsuario;
		
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.validateEmailUsuario", "validateEmailUsuario", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno data funcao de populateGrupoExecucao;
	 *
	 * @param data
	 * @param error
	 */
	function validateEmailUsuario_cb(data, error) {
		if (error!=undefined) {
			alert(error);
			setElementValue("ccUsers_usuario.idUsuario", "");
			setElementValue("ccUsers_usuario.nrMatricula", "");
			setElementValue("ccUsers_usuario.nmUsuario", "");
			
			setFocus("ccUsers_usuario.nrMatricula");
		}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	function onGridRowClick() {
		return false;
	}
	
	/**
	 * Limpa os dados da tela.
	 */
	function resetView(){
		newButtonScript(this.document, true);
		setButtonsDisable(false);
		parametersGridDef.resetGrid();
	}
	
	/** 
	 * Disabilita os botoes da tela.
	 *
	 * @param disable
	 */
	function setButtonsDisable(disable) {
		setDisabled("btnAdicionarParametro", disable);
		setDisabled("btnRemoverParametro", disable);
		setDisabled("btnSalvar", disable);
		setDisabled("btnLimpar", disable);
		setDisabled("btnGM", false); // FIXME Remover o set quando existir a tela GM para execucao da rotina
	}
	
	/**
	 * Verifica se o botao de proximas execucoes deve ficar desabilitado ou nao.
	 * Abilita ou desabilita o mesma caso se faca necessario.
	 */
	function checkDisableBtnProximasExecucoes() {
		if (getElementValue("cronExpression")=="") {
			setDisabled("btnProximasExecucoes", true);
		} else {
			setDisabled("btnProximasExecucoes", false);
		}
	}
	
	//###################################################################
    // Funcoes referentes ao controle da grid
	//###################################################################
	
	function populateParameterGrid(list){
		
		var resultSetPage = new Object();
		resultSetPage.list = list;
		resultSetPage.hasNextPage = "true";
		resultSetPage.hasPriorPage = "false";
		resultSetPage.currentPage = "1";

		parametersGridDef.populateGrid(resultSetPage);
	}

	function addParameter() {
		var resultSetPage = new Object();
		var gridData = parametersGridDef.gridState.data;
		
		document.getElementById("parameters.chkSelectAll").disabled=false;
		
		//Necessario para primeira carga da grid.
		if (gridData.length==0) {
			parametersGridDef.gridState.stats = new Object();
			parametersGridDef.gridState.stats.elapsedRenderTime = 0;
		} else {
			updateGridValues(gridData);
		}
		
		var dataObject = new Object();
		dataObject.idParametro = gridData.length;
		dataObject.name="";
		dataObject.value="";
		gridData[gridData.length] = dataObject;
			
		populateParameterGrid(gridData);
	}
	
	/**
	 * Captura os ultimos objetos de dados para dentro do objeto de dados ds grid.
	 */
	function updateGridValues(gridData) {
		for (i=0; gridData.length>i; i++) {
			gridData[i].name = document.getElementById("parameters:" + (i) + ".name").value;
			gridData[i].value = document.getElementById("parameters:" + (i) + ".value").value;
		}
	}
	
	/**
	 * Remove o dado da grid...
	 */
	function removeParameter() {
		
		if ((parametersGridDef.gridState.data.length==0) || (parametersGridDef.getSelectedIds().ids.length==0)){
			alert(LMS_27077());
			return false;
		}
		
		updateGridValues(parametersGridDef.gridState.data);
		
		for (i=0; i<parametersGridDef.getSelectedIds().ids.length; i++) {
			idObject = parametersGridDef.getSelectedIds().ids[i];
			
			for (j=0; j<parametersGridDef.gridState.data.length; j++) {
				var rowData = parametersGridDef.gridState.data[j];
				
				if (rowData.idParametro==idObject){    				
   					parametersGridDef.gridState.data.splice(j, 1);
				}
			}
		}	
		
		if (parametersGridDef.gridState.data.length==0) {
			document.getElementById("parameters.chkSelectAll").disabled=true;
		}
			
		populateParameterGrid(parametersGridDef.gridState.data);
	}
</script>