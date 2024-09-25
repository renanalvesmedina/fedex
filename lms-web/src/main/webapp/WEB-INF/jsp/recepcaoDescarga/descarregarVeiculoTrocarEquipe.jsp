<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<script type="text/javascript">

	/**
	 * Inicia objetos da tela
	 */
	function loadDataObjects() {	 
		onPageLoad();
		
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.newMaster", "loadDataObjects");
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega a grid apos setar a aba como master da DF2
	 */
	function loadDataObjects_cb(data, error){
		var parentWindow = dialogArguments.window.document;
		
		setElementValue("idCarregamentoDescarga", parentWindow.getElementById("idCarregamentoDescarga").value);
		setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);
		
		document.getElementById("fechar").disabled=false; 
		
		if (getElementValue("idCarregamentoDescarga")!="") {
			var idCarregamentoDescargaValue = getElementValue("idCarregamentoDescarga");
	
			disableTrocarEnableSalvarButtons(false);
	
			var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.findUltimaEquipe", "loadGridData", {idCarregamentoDescarga:idCarregamentoDescargaValue});
	    	xmit({serviceDataObjects:[sdo]});
	    }
	}
	
	/**
	 * Carrega os dados da grid apos setar os dados da tela
	 */
	function loadGridData_cb(data, error){
		
		if (data.equipeOperacao!=undefined) {
			setElementValue("idEquipeOperacao", data.equipeOperacao.idEquipeOperacao);
			
			setElementValue("equipe.dsEquipe", data.equipeOperacao.equipe.dsEquipe);
			setElementValue("equipe.idEquipe", data.equipeOperacao.equipe.idEquipe);
			
			setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), data.equipeOperacao.dhInicioOperacao));
			
			if (getElementValue("dhFimOperacao")!="") {
				setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), data.equipeOperacao.dhFimOperacao));
			}
			
			var idCarregamentoDescargaValue = getElementValue("idCarregamentoDescarga");
			
			equipeOperacaoGridDef.executeSearch({idCarregamentoDescarga:idCarregamentoDescargaValue}, true);
		}
	}
	
	/**
	 * Desabilita ou abilita os botoes desta aba
	 *
	 * @param disable 
	 */
	function disableTrocarEnableSalvarButtons(disable) {
		document.getElementById("trocar").disabled=disable;
		document.getElementById("salvar").disabled=!disable;
	}
</script>

<adsm:window title="descarregarVeiculo" service="lms.recepcaodescarga.descarregarVeiculoTrocarAction" onPageLoad="loadDataObjects">
	<adsm:form action="/recepcaoDescarga/descarregarVeiculo" idProperty="idEquipeOperacao" id="equipeForm">
		<adsm:section caption="trocarEquipe" />
		
		<adsm:hidden property="idCarregamentoDescarga"/>
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial"/>
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					 label="controleCargas" width="85%" size="3" maxLength="3" disabled="true">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" maxLength="8" size="8" disabled="true"/>
		</adsm:textbox>
		
		<adsm:hidden property="idFilial"/>
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" maxLength="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="sgFilialPostoAvancado" 
					  label="postoAvancado" maxLength="3" size="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoaPostoAvancado" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:lookup property="equipe" dataType="text" idProperty="idEquipe" criteriaProperty="dsEquipe"
					 action="/carregamento/manterEquipes" service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findEquipes" 
					 onDataLoadCallBack="loadEquipes" onPopupSetValue="loadEquipesPopUp" onchange="return equipeOnChange();"
					 label="equipe" width="85%" maxLength="50" size="50" required="true" 
					 minLengthForAutoPopUpSearch="3" exactMatch="false" disabled="true">
			<adsm:propertyMapping modelProperty="idEquipe" relatedProperty="equipe.idEquipe"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
		</adsm:lookup>
		
		<adsm:textbox property="dhInicioOperacao" dataType="JTDateTimeZone" label="inicioOperacao" disabled="true"/>
		
		<adsm:textbox property="dhFimOperacao" dataType="JTDateTimeZone" label="fimOperacao" disabled="true"/>
		
		<adsm:hidden property="dataTerminoEquipe"/>
		<adsm:hidden property="idEquipeOld" value="0"/>
		
		<adsm:hidden property="controleCargaConcatenado" serializable="false"/>
		<adsm:hidden property="filialConcatenado" serializable="false"/>
		<adsm:hidden property="PostoAvancadoConcatenado" serializable="false"/>		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="trocar" caption="trocarEquipe" onclick="trocarEquipe()"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="equipeOperacao" idProperty="idEquipeOperacao" 
		service="lms.recepcaodescarga.descarregarVeiculoTrocarAction.findPaginatedTrocarEquipe"
		rowCountService="lms.recepcaodescarga.descarregarVeiculoTrocarAction.getRowCountTrocarEquipe"
		gridHeight="250" selectionMode="none" onRowClick="equipeOperacaoOnRowClick" rows="9">	
		<adsm:gridColumn property="equipe.dsEquipe" title="equipe" width="60%" />
		<adsm:gridColumn property="dhInicioOperacao" title="inicioOperacao" width="20%" dataType="JTDateTimeZone" align="center"/>
		<adsm:gridColumn property="dhFimOperacao" title="fimOperacao" width="20%" dataType="JTDateTimeZone" align="center"/>
		<adsm:buttonBar>
			<adsm:button id="salvar" caption="confirmar" onclick="salvarEquipe()" disabled="true"/>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent()"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	
	document.getElementById("dataTerminoEquipe").dataType="JTDateTimeZone";
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_load") {
			//Busca a tela pai
			var parentWindow = dialogArguments.window.document;
			
			//carrega os objetos
			setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value);
			
			setElementValue("idFilial", parentWindow.getElementById("idFilial").value);
			setElementValue("sgFilial", parentWindow.getElementById("sgFilial").value);
			setElementValue("pessoa.nmFantasia", parentWindow.getElementById("pessoa.nmFantasia").value);
			
			setElementValue("sgFilialPostoAvancado", parentWindow.getElementById("sgFilialPostoAvancado").value);
			setElementValue("nmPessoaPostoAvancado", parentWindow.getElementById("nmPessoaPostoAvancado").value);
			
			//Seta os hiddens com seus valores concatenados
			setElementValue("controleCargaConcatenado", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value + " - " + parentWindow.getElementById("controleCarga.nrControleCarga").value);
			setElementValue("filialConcatenado", parentWindow.getElementById("sgFilial").value + " - " + parentWindow.getElementById("pessoa.nmFantasia").value);
			
			if (getElementValue("nmPessoaPostoAvancado")!="") {
				setElementValue("postoAvancadoConcatenado", parentWindow.getElementById("sgFilialPostoAvancado").value + " - " + parentWindow.getElementById("nmPessoaPostoAvancado").value);
			} else {
				setElementValue("postoAvancadoConcatenado", parentWindow.getElementById("sgFilialPostoAvancado").value);
			}
		}
	}
	
	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	/**
	 * Dispara a operacao de troca de equipe
	 */
	function trocarEquipe(){
		setDisableTab(true);

		var idCarregamentoDescargaValue = getElementValue("idCarregamentoDescarga");
	
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.findDataTerminoEquipe", "trocarEquipe", {idCarregamentoDescarga:idCarregamentoDescargaValue});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Efetua as operacoes pos-finalizacao da equipe.
	 *
	 * @data
	 * @error
	 */
	function trocarEquipe_cb(data, error) {
		setDisabled("equipe.idEquipe", false);
		resetValue(document.getElementById("equipe.idEquipe"));
		setDisabled("salvar", false);
		
		setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), data.dataTerminoEquipe));
		setElementValue("dhFimOperacao", "");
		
		var idCarregamentoDescargaValue = getElementValue("idCarregamentoDescarga");

		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.findUltimaEquipe", "loadGridTrocarEquipe", {idCarregamentoDescarga:idCarregamentoDescargaValue});
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Captura o retorno contendo a informacao de qual e o id da ultima equipe a ter sido 
	 * setada nesta tela.
	 * 
	 * @data
	 * @error
	 */
	function loadGridTrocarEquipe_cb(data, error) {
		setElementValue("idEquipeOld", data.equipeOperacao.equipe.idEquipe);
	
		var formData = new Object();
		setNestedBeanPropertyValue(formData, "dataTerminoEquipe", getElementValue("dhInicioOperacao"));
		setNestedBeanPropertyValue(formData, "idCarregamentoDescarga", getElementValue("idCarregamentoDescarga"));
		
		equipeOperacaoGridDef.executeSearch( formData, true);
		setFocus(document.getElementById("equipe.dsEquipe"));
	}	
	
	/**
	 * Dispara a operacao de salvar a equipe
	 */
	function salvarEquipe(){
	
		if (getElementValue("idEquipeOld")=="0") return false;
	
		valid = validateTabScript(document.getElementById("equipeForm"));
	
		if (valid == false) return false;
	
		var formData = buildFormBeanFromForm(document.getElementById("equipeForm"));
		
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.storeTrocarEquipe", "salvarEquipe", formData);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback do metodo de salvaer a equipe
	 */
	function salvarEquipe_cb(data, error){
		if (data._exception==undefined) {
			returnToParent();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Tratamento para o callback da lookup de equipes
	 *
	 * @param data
	 * @param error
	 */
	function equipeOnChange() {
		if (getElementValue("equipe.dsEquipe")=="") {
			removeIntegrantes(0); 
			setDisableTab(true);
		} else {
			setDisableTab(false);
		}
		return equipe_dsEquipeOnChangeHandler();
	}
	
	/**
	 * Tratamento para o callback da lookup de equipes
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipes_cb(data, error) {
		if (data[0]!=undefined) {
			setDisableTab(false);
			setElementValue("equipe.idEquipe", data[0].idEquipe);
			removeIntegrantes(data[0].idEquipe); 
			setIdEquipe(data[0].idEquipe);
		} else {	
			setDisableTab(true);
		}
		
		return lookupExactMatch({e:document.getElementById("equipe.idEquipe"), data:data, callBack:"loadEquipesLikeMatch"});
	}
	
	/**
	 * Tratamento para o callback LikeMatch da lookup de equipes
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipesLikeMatch_cb(data, error) {
		if (data[0]!=undefined) {
			setDisableTab(false);
			setElementValue("equipe.idEquipe", data[0].idEquipe);
			removeIntegrantes(data[0].idEquipe); 
			setIdEquipe(data[0].idEquipe);
		} else {	
			setDisableTab(true);
		}
		
		return equipe_dsEquipe_likeEndMatch_cb(data);
	}	
	
	/**
	 * Tratamento para o callback da lookup de equipes quando a mesma vem da popup
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipesPopUp(data, error) {
		if (data.dsEquipe!=undefined) {
			setDisableTab(false);
			setIdEquipe(data.idEquipe);
			removeIntegrantes(data.idEquipe);
		}
	}
	
	/**
	 * Carrega a grid de equipes
	 */
	function loadEquipesGrid(){
		var idCarregamentoDescargaValue = getElementValue("idCarregamentoDescarga");
		equipeOperacaoGridDef.executeSearch({idCarregamentoDescarga:idCarregamentoDescargaValue}, true);
	}
	
	/**
	 * Remove todos os filhos casoo id desta tela seja diferente da do filho
	 */
	function removeIntegrantes(idEquipe){
		var tabGroup = getTabGroup(this.document);
		var abaCarregamento = tabGroup.getTab("cad");
		
		if (abaCarregamento.getElementById("idEquipe").value!=idEquipe){
			var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.newMaster");
			xmit({serviceDataObjects:[sdo]});
		}	
	}
	
	/**
	 * Desabilita o click na row
	 */
	function equipeOperacaoOnRowClick(idEquipeOperacao) {
		//Mata os antigos filhos postos na sessao...
		var data = new Object();
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoTrocarAction.newMaster", "equipeOperacaoOnRowClick", data);
		xmit({serviceDataObjects:[sdo]});
		
		populateFormWithGridRow(idEquipeOperacao);
		setElementValue("idEquipeOperacao", idEquipeOperacao);
		
		return false;
	}
	
	function equipeOperacaoOnRowClick_cb(data, error) {
	
		setDisableTab(false);
		setDisabled("equipe.idEquipe", true);
		
		//Seta o valor do idEquipe para vazio para caracterizar o clique na grid...
		setIdEquipe("");
		//Seta o valor da data de termino para vazio para a grid nao popular o valor de data termino na ultima equipe...
		setElementValue("dataTerminoEquipe", "");
		
		setElementValue("idEquipeOld", "0");
				
		updateGridState();
		
		var tabGroup = getTabGroup(this.document);
		executeBlockingUI({closure:function() { tabGroup.selectTab('cad', {name:'tab_click'}); }, thiz:tabGroup});
	}	
	
	/**
	 * Popula o form com a equipe selecionada
	 *
	 * @param idEquipeOperacao
	 */
	function populateFormWithGridRow(idEquipeOperacao) {
		var gridRow = getGridRowDataObject(idEquipeOperacao);
		
		setElementValue("equipe.dsEquipe", gridRow.equipe.dsEquipe);
		setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), gridRow.dhInicioOperacao));
		setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), gridRow.dhFimOperacao));
		
	}
	
	function getGridRowDataObject(idEquipeOperacao){
		for (i=0; i<equipeOperacaoGridDef.gridState.data.length; i++) {
			if (equipeOperacaoGridDef.gridState.data[i].idEquipeOperacao==idEquipeOperacao){
				return equipeOperacaoGridDef.gridState.data[i];
			}
		}
	}
	
	function updateGridState(){
		var filter = new Object();
		filter.idCarregamentoDescarga = equipeOperacaoGridDef.gridState.filters.idCarregamentoDescarga;
		filter._currentPage = equipeOperacaoGridDef.gridState.filters._currentPage;
		filter._pageSize = equipeOperacaoGridDef.gridState.filters._pageSize;
		filter._order = equipeOperacaoGridDef.gridState.filters._order;
		
		equipeOperacaoGridDef.gridState.filters = filter;
	}		
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
	/**
	 * Desabilita a aba de integrantes
	 *
	 * @param disableTab aba gerenciamento
	 */
	function setDisableTab(disableTab) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup!=null) {
			if (disableTab!=null) tabGroup._tabsIndex[1].setDisabled(disableTab);
		}
	}
	
	/**
	 * Seta o id equipe existente na masterLinK 
	 */ 
	function setIdEquipe(idEquipe) {
	
		var tabGroup = getTabGroup(this.document);
		var abaCarregamento = tabGroup.getTab("cad");
		
		abaCarregamento.getElementById("idEquipe").value = idEquipe;
	}

</script>
