<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<script>
	
	function loadObjects() {
		onPageLoad();
		loadObjectsFromParent();
		loadObjectsFromURL();
		disableButtons(false);
		
		var data = new Object();
		var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoPreManifestosAction.findBasicDataUsuario", "loadBasicDataUsuario", data);
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	var basicData;
	function loadBasicDataUsuario_cb(data, error) {	
		basicData = data;
		loadDataEmpresa(basicData);
		
		var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoPreManifestosAction.newMaster");
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega os dados da empresa do usuario logado.
	 *
	 * @param data 
	 */
	function loadDataEmpresa(data) {
		setElementValue("empresa.idEmpresa", data.idEmpresa);
		setElementValue("empresa.pessoa.nrIdentificacao", data.nrIdentificacao);
		setElementValue("empresa.pessoa.tpIdentificacao.value", data.tpIdentificacao);
		setElementValue("empresa.pessoa.nmPessoa", data.nmPessoa);
	}
	
	function loadObjectsFromParent(){
		var parentWindow = dialogArguments.window.document;
		
		setElementValue("_controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);	
		setElementValue("_sgFilial", parentWindow.getElementById("sgFilial").value);
		setElementValue("_sgFilialPostoAvancado", parentWindow.getElementById("sgFilialPostoAvancado").value);
	}
	
	function loadObjectsFromURL(){
		var url = new URL(parent.location.href);
		var idManifesto = url.parameters["idManifesto"];
		var nrPreManifesto = url.parameters["nrPreManifesto"];
		
		setElementValue("masterId", idManifesto);
		setElementValue("_nrPreManifesto", nrPreManifesto);
	}

	function disableButtons(disable){
		document.getElementById("finalizar").disabled=disable;
	}

</script>

<adsm:window service="lms.carregamento.finalizarCarregamentoPreManifestosAction" onPageLoad="loadObjects">
	<adsm:form action="/carregamento/finalizarCarregamentoControleCargas" id="manifestoColetaForm" idProperty="idDispCarregDescQtde">
		
		<adsm:masterLink idProperty="idManifesto" showSaveAll="false">
			<adsm:masterLinkItem property="controleCarga.nrControleCarga" label="controleCargas" itemWidth="50" />
			<adsm:masterLinkItem property="sgFilial" label="filial" itemWidth="50"/>
			<adsm:masterLinkItem property="sgFilialPostoAvancado" label="postoAvancado" itemWidth="50"/>
			<adsm:masterLinkItem property="nrPreManifesto" label="preManifesto" itemWidth="50"/>
		</adsm:masterLink>
		
		
		<adsm:combobox property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao"
					   optionProperty="idTipoDispositivoUnitizacao" optionLabelProperty="dsTipoDispositivoUnitizacao"
					   service="lms.carregamento.finalizarCarregamentoPreManifestosAction.findTipoDispositivo"
					   label="tipoDispositivo" onlyActiveValues="true" required="true"/>
		
		<adsm:lookup dataType="text" property="empresa" idProperty="idEmpresa" 
			criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.carregamento.finalizarCarregamentoPreManifestosAction.findEmpresas" action="/municipios/manterEmpresas" 
			exactMatch="false" 	minLengthForAutoPopUpSearch="3" label="empresa"  width="85%" required="true" size="18" maxLength="20">
			
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="empresa.pessoa.nmPessoa"  modelProperty="pessoa.nmPessoa" disable="false"/>
			<adsm:propertyMapping criteriaProperty="empresa.pessoa.tpIdentificacao.value" modelProperty="pessoa.tpIdentificacao" disable="false"/>
			<adsm:propertyMapping criteriaProperty="empresa.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacao" disable="false"/>
			
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa"  modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="empresa.pessoa.tpIdentificacao.value"  modelProperty="pessoa.tpIdentificacao.value" />
			
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" disabled="true" size="40" maxLength="50"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="integer" property="qtDispositivo" 
					  label="quantidade" size="6"  maxLength="6" width="85%" required="true" />		
		
		<adsm:hidden property="empresa.pessoa.tpIdentificacao.value"/>	
			
		<adsm:hidden property="tipoDispositivoUnitizacao.tpControleDispositivo" value="Q"/>			  
		
		<adsm:hidden property="controleCarga.idControleCarga"/>			  
		<adsm:hidden property="controleCarga.nrControleCarga"/>
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:hidden property="idCarregamentoDescarga"/>
		<adsm:hidden property="idFilial"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDispositivoSemIdentificacao" service="lms.carregamento.finalizarCarregamentoPreManifestosAction.saveDispositivosSemIdentificacao" callbackProperty="loadGrid"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="dispCarregDescQtde" idProperty="idDispCarregDescQtde" unique="true"
			   service="lms.carregamento.finalizarCarregamentoPreManifestosAction.findPaginatedDispositivosSemIdentificacao" 
			   rowCountService="lms.carregamento.finalizarCarregamentoPreManifestosAction.getRowCountDispositivosSemIdentificacao"
			   detailFrameName="dispositivoSemIdentificacao" onRowClick="populateFormOnRowClick" onDataLoadCallBack="gridCallBack">
		<adsm:gridColumn property="tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao" title="tipoDispositivo" width="15%" />
		<adsm:gridColumn property="empresa.pessoa.nmPessoa" title="empresa" width="70%" />
		<adsm:gridColumn property="qtDispositivo" title="quantidade" width="15%" align="right"/>
		<adsm:buttonBar>
			<adsm:button id="finalizar" caption="finalizar" onclick="finalizarPreManifesto()"/>
			<adsm:removeButton caption="excluirDispositivoSemIdentificacao" service="lms.carregamento.finalizarCarregamentoPreManifestosAction.removeByIdsDispositivosSemIdentificacao"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	//Deixa com que estes atributos nao sejam limpados quando o usuario utilizar o botao 'limpar'
	document.getElementById("controleCarga.idControleCarga").masterLink = true;
	document.getElementById("controleCarga.nrControleCarga").masterLink = true;
	document.getElementById("tpSituacao").masterLink = true;
	document.getElementById("idCarregamentoDescarga").masterLink = true;
	document.getElementById("idFilial").masterLink = true;

	function initWindow(eventObj) {
		if(eventObj.name == "tab_click") {
			disableButtons(false);
		} else if (eventObj.name == "newItemButton_click"){
			loadDataEmpresa(basicData);
			disableButtons(false);
		}
	}

	//##################################
    // Comportamentos apartir de objetos
	//##################################

	/**
	 * Finaliza o pre-manifesto em questao
	 */
	function finalizarPreManifesto(){
		var parentWindow = dialogArguments.window.document;
	
		setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);	
		setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);	
		setElementValue("idCarregamentoDescarga", parentWindow.getElementById("idCarregamentoDescarga").value);	
		setElementValue("idFilial", parentWindow.getElementById("idFilial").value);
	
		var formData = buildFormBeanFromForm(document.getElementById("manifestoColetaForm"));
		
		var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoPreManifestosAction.storeFinalizarCarregamentoPreManifesto", "finalizarPreManifesto", formData);
	    xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Retorno da chamada de finalizar pre manifesto
	 */
	function finalizarPreManifesto_cb(data, error){
		if (data._exception==undefined) {
			returnToParent();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Carrega a grid apos o retorno da persistencia do form
	 *
	 * &param
	 * &param
	 */
	function loadGrid_cb(data, error) {
	
		if (data._exception!=undefined) {
			alert(data._exception._message);
			setFocusOnFirstFocusableField();
		} else {
			resetValue(this.document);
			showSuccessMessage();
			var formData = buildFormBeanFromForm(document.getElementById("manifestoColetaForm"));
			dispCarregDescQtdeGridDef.executeSearch( formData, true);
		}
	}
	
	/**
	 * Carrega o form apartir do objeto data contido na grid
	 */
	function populateFormOnRowClick(id){
		var formData = new Object();		
		setNestedBeanPropertyValue(formData, "detailId", id);
		setNestedBeanPropertyValue(formData, "masterId", getElementValue("masterId"));
		
    	var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoPreManifestosAction.findByIdDispositivosSemIdentificacao", "populateFormOnRowClick", formData);
    	xmit({serviceDataObjects:[sdo]});
		
		return false;
	}

	function populateFormOnRowClick_cb(data, error) {
		setElementValue("idDispCarregDescQtde", data.idDispCarregDescQtde);
		setElementValue("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao", data.tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao);
		setElementValue("empresa.idEmpresa", data.empresa.idEmpresa);
		setElementValue("empresa.pessoa.nrIdentificacao", data.empresa.pessoa.nrIdentificacao);
		setElementValue("empresa.pessoa.tpIdentificacao.value", data.empresa.pessoa.tpIdentificacao.value);
		setElementValue("empresa.pessoa.nmPessoa", data.empresa.pessoa.nmPessoa);
		setElementValue("qtDispositivo", data.qtDispositivo);
	}
	
	/**
	 * Carrega os dados da empresa do usuario no callback da grid
	 * usado quando o usario insere um dado na grid.
	 */
	function gridCallBack_cb(data, error){
		loadDataEmpresa(basicData);
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

</script>