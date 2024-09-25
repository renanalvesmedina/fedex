<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<script>
	
	function loadDataObjects() {
		onPageLoad();
		loadObjectsFromParent();
		loadObjectsFromURL();
		
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
		setElementValue("idFilial", parentWindow.getElementById("idFilial").value);
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

</script>

<adsm:window service="lms.carregamento.finalizarCarregamentoPreManifestosAction" onPageLoad="loadDataObjects">
	<adsm:form action="/carregamento/finalizarCarregamentoControleCargas" id="manifestoColetaForm" idProperty="idDispCarregIdentificado">
		
		<adsm:masterLink idProperty="idManifesto" showSaveAll="false">
			<adsm:masterLinkItem property="controleCarga.nrControleCarga" label="controleCargas" itemWidth="50" />
			<adsm:masterLinkItem property="sgFilial" label="filial" itemWidth="50"/>
			<adsm:masterLinkItem property="sgFilialPostoAvancado" label="postoAvancado" itemWidth="50"/>
			<adsm:masterLinkItem property="nrPreManifesto" label="preManifesto" itemWidth="50"/>			
		</adsm:masterLink>
		
		<adsm:combobox property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao"
					   optionProperty="idTipoDispositivoUnitizacao" optionLabelProperty="dsTipoDispositivoUnitizacao"
					   service="lms.carregamento.finalizarCarregamentoPreManifestosAction.findTipoDispositivoIdentificacao"
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
		
		<adsm:lookup dataType="integer" property="dispositivoUnitizacao" idProperty="idDispositivoUnitizacao" criteriaProperty="nrIdentificacao"
					 service="lms.carregamento.finalizarCarregamentoPreManifestosAction.findDispositivoUnitizacao" 
					 action="/carregamento/manterControleCargas" cmd="list" 
					 label="numeroIdentificacao" size="10" maxLength="10" required="true" picker="false">	
					 
			<adsm:propertyMapping criteriaProperty="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" modelProperty="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" />
			<adsm:propertyMapping criteriaProperty="empresa.idEmpresa"  modelProperty="empresa.idEmpresa" />
		</adsm:lookup>
		
		<adsm:hidden property="empresa.pessoa.tpIdentificacao.value"/>	
					 
		<adsm:hidden property="tipoDispositivoUnitizacao.tpControleDispositivo" value="Q"/>		
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:hidden property="idFilial"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDispositivoComIdentificacao" service="lms.carregamento.finalizarCarregamentoPreManifestosAction.saveDispositivosComIdentificacao" callbackProperty="loadGrid"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="dispCarregIdentificado" idProperty="idDispCarregIdentificado"
			   service="lms.carregamento.finalizarCarregamentoPreManifestosAction.findPaginatedDispositivosComIdentificacao" 
			   rowCountService="lms.carregamento.finalizarCarregamentoPreManifestosAction.getRowCountDispositivosComIdentificacao"
			   onRowClick="populateFormOnRowClick" onDataLoadCallBack="gridCallBack">
		<adsm:gridColumn property="dispositivoUnitizacao.tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao" title="tipoDispositivo" width="15%"/>
		<adsm:gridColumn property="dispositivoUnitizacao.empresa.pessoa.nmPessoa" title="empresa" width="65%" />
		<adsm:gridColumn property="dispositivoUnitizacao.nrIdentificacao" title="numeroIdentificacao" width="20%" align="right" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDispositivoComIdentificacao" service="lms.carregamento.finalizarCarregamentoPreManifestosAction.removeByIdsDispositivosComIdentificacao"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	document.getElementById("tpSituacao").masterLink = true;
	document.getElementById("idFilial").masterLink = true;
	
	document.getElementById("dispositivoUnitizacao.nrIdentificacao").style.textAlign = "right";

	function initWindow(eventObj) {
		if (eventObj.name == "newItemButton_click"){
			loadDataEmpresa(basicData);
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
			dispCarregIdentificadoGridDef.executeSearch( formData, true);
		}
	}
	
	/**
	 * Carrega o form apartir do objeto data contido na grid
	 */
	function populateFormOnRowClick(id){
		var formData = new Object();		
		setNestedBeanPropertyValue(formData, "detailId", id);
		setNestedBeanPropertyValue(formData, "masterId", getElementValue("masterId"));
		
    	var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoPreManifestosAction.findByIdDispositivosComIdentificacao", "populateFormOnRowClick", formData);
    	xmit({serviceDataObjects:[sdo]});
		
		return false;
	}
	
	/**
	 * Carrega os dados da empresa do usuario no callback da grid
	 * usado quando o usario insere um dado na grid.
	 */
	function gridCallBack_cb(data, error){
		loadDataEmpresa(basicData);
	}
	
	function populateFormOnRowClick_cb(data, error) {
		setElementValue("idDispCarregIdentificado", data.idDispCarregIdentificado);
		setElementValue("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao", data.tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao);
		setElementValue("empresa.idEmpresa", data.empresa.idEmpresa);
		setElementValue("empresa.pessoa.nrIdentificacao", data.empresa.pessoa.nrIdentificacao);
		setElementValue("empresa.pessoa.tpIdentificacao.value", data.empresa.pessoa.tpIdentificacao.value);
		setElementValue("empresa.pessoa.nmPessoa", data.empresa.pessoa.nmPessoa);
		setElementValue("dispositivoUnitizacao.idDispositivoUnitizacao", data.dispositivoUnitizacao.idDispositivoUnitizacao);
		setElementValue("dispositivoUnitizacao.nrIdentificacao", data.dispositivoUnitizacao.nrIdentificacao);
	}

</script>
