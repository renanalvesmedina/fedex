<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lm.entrega.reemitirCancelarManifestosAction" onPageLoadCallBack="pageLoadCustom">
	<adsm:i18nLabels>
		<adsm:include key="LMS-09081"/>
		<adsm:include key="LMS-09087"/>
	</adsm:i18nLabels>
	 
	<adsm:form action="/entrega/reemitirCancelarManifestos" idProperty="idManifestoEntrega">	
					
		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3"
					 service="lms.entrega.reemitirCancelarManifestosAction.findLookupFilial" dataType="text" label="filial" size="3"
					 action="/municipios/manterFiliais" labelWidth="28%" width="65%" minLengthForAutoPopUpSearch="3"
					 exactMatch="false" disabled="true"
					 onDataLoadCallBack="filialDataLoad"
	   				 onPopupSetValue="filialPopup"
	   				 onchange="return filialOnChange(this)">
			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.idFilial" modelProperty="idFilial" />		
			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.sgFilial" modelProperty="sgFilial" />		
			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />	
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />		
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="idFiial" />		
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial" />	
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />	
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />		
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		
		<adsm:combobox property="manifesto.tpManifestoEntrega" label="tipoManifesto" domain="DM_TIPO_MANIFESTO_ENTREGA" 
					   cellStyle="vertical-align=bottom;" labelWidth="28%" width="32%" renderOptions="true"/>

		<adsm:lookup dataType="text" 
   				property="manifestoEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
   				service="lms.entrega.reemitirCancelarManifestosAction.findLookupFilial"
   				action="/entrega/consultarManifestosEntrega" cmd="list"
   				onchange="return manifestoEntrega_filialChange(this);"
   				label="manifestoEntrega" size="3" maxLength="3" disabled="true"
   				labelWidth="18%" width="22%" exactMatch="true" picker="false">
   			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
   			
   			<adsm:lookup dataType="integer" 
	   				property="manifestoEntrega" idProperty="idManifestoEntrega" criteriaProperty="nrManifestoEntrega" 
	   				service="lms.entrega.reemitirCancelarManifestosAction.findLookupManifestoEntrega"
	   				action="/entrega/consultarManifestosEntrega" cmd="lookup" popupLabel="pesquisarManifestoEntrega"
	   				size="7" maxLength="8" mask="00000000" exactMatch="true" disabled="true" >
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.idFilial" modelProperty="filial.idFilial" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
   			</adsm:lookup>
   			
   			<adsm:hidden property="manifestoEntrega.filial.pessoa.nmFantasia" serializable="false" />
   		</adsm:lookup>
		
		<adsm:combobox property="tpSituacaoManifesto" domain="DM_STATUS_MANIFESTO"	
		               label="situacaoManifesto" labelWidth="28%" width="32%" renderOptions="true"/>
				
		<adsm:lookup label="controleCargas" 
					 labelWidth="18%"  width="22%" 	 size="3"  maxLength="3" disabled="true"
					 picker="false"  serializable="false" popupLabel="pesquisarFilial"	 dataType="text" 
		    		 property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" 	 criteriaProperty="sgFilial" 
					 service="lms.entrega.reemitirCancelarManifestosAction.findLookupFilial"
					 action="/municipios/manterFiliais"	 onchange="return sgFilialOnChangeHandler();" 
			 		 onDataLoadCallBack="retornoControleCargaFilial">
			<adsm:lookup dataType="integer"
						 popupLabel="pesquisarControleCarga"
				         property="controleCarga" 
				         idProperty="idControleCarga" 
				         criteriaProperty="nrControleCarga"
						 service="lms.entrega.reemitirCancelarManifestosAction.findControleCarga" 
						 action="/carregamento/manterControleCargas"
						 maxLength="8" 
						 size="8"
						 mask="00000000" disabled="true"
						 onPopupSetValue="popupControleCarga">
	 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" />
	 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" />
	 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"  />
			</adsm:lookup>		
		</adsm:lookup>	
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
		
		<adsm:hidden property="tpSituacao" value="A"/>
 		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
				service="lms.entrega.reemitirCancelarManifestosAction.findLookupMeioTransporteRodoviario" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="28%" width="82%" size="8" serializable="false" maxLength="6" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
								  
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
								  modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
				
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
					service="lms.entrega.reemitirCancelarManifestosAction.findLookupMeioTransporteRodoviario" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="20" required="false" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />
									  
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
									  modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />		
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			</adsm:lookup>
        </adsm:lookup>

		<adsm:lookup service="lms.entrega.reemitirCancelarManifestosAction.findRotaLookup" idProperty="idRotaColetaEntrega" 
					 dataType="integer" property="rotaColetaEntrega" criteriaProperty="nrRota"
					 label="rota" size="3" maxLength="3" labelWidth="28%" width="58%" action="/municipios/manterRotaColetaEntrega">
            <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota"/>
            <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
            <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
            <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true"/>             
        </adsm:lookup>
	
		<adsm:range label="periodoEmissaoManifesto" labelWidth="28%" width="70%" maxInterval="15">
			<adsm:textbox dataType="JTDate" property="dhEmissaoManifestoInicial"/>
			<adsm:textbox dataType="JTDate" property="dhEmissaoManifestoFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" buttonType="findButton" onclick="find();"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
		<adsm:grid idProperty="idManifestoEntrega" property="manifestoEntrega" gridHeight="160" scrollBars="horizontal" rows="8" 
				   service="lms.entrega.reemitirCancelarManifestosAction.findPaginatedGridCancelarManifesto"
				   rowCountService="lms.entrega.reemitirCancelarManifestosAction.getRowCountGridCancelarManifesto" 
				   onRowClick="manifestoRowClick" onDataLoadCallBack="gridDataLoad">
				   
			<adsm:gridColumn title="tipoDeManifesto" property="tpManifestoEntrega" isDomain="true" width="120"/>
			
			<adsm:gridColumnGroup separatorType="MANIFESTO">
				<adsm:gridColumn title="numeroManifesto" property="sgFilial" width="75" align="left"/>
				<adsm:gridColumn title="" mask="00000000" dataType="integer" property="nrManifestoEntrega" width="75" />
			</adsm:gridColumnGroup>
						
			<adsm:gridColumn title="dataEmissao" property="dhEmissaoManifesto" dataType="JTDateTimeZone" width="120" align="center"/>
			
			<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
				<adsm:gridColumn title="controleCarga" property="sgFilialCC" width="60" />
				<adsm:gridColumn title="" mask="00000000" dataType="integer" property="nrControleCarga" width="60" align="right"/>
			</adsm:gridColumnGroup>			

			<adsm:gridColumn title="meioTransporte" property="nrFrota"  width="105"/>
			<adsm:gridColumn title="" align="left" property="nrIdentificador"  width="105"/>
				
			<adsm:gridColumnGroup customSeparator=" - ">
				<adsm:gridColumn title="rota" property="nrRota" width="120" mask="000" dataType="integer" align="left"/>
				<adsm:gridColumn title="" dataType="text" property="dsRota" width="120"/>
			</adsm:gridColumnGroup>	
		
			<adsm:gridColumn title="quantidadeEntregas" property="quantidadeEntregas" width="150" align="right"/>
			
			<adsm:gridColumn title="situacaoManifesto" property="tpStatusManifesto" isDomain="true" width="150"/>
			
		<adsm:buttonBar freeLayout="false">
			<adsm:button id="buttonEmitir" buttonType="removeButton" caption="emitir" onclick="emitirManifesto()" />
			<adsm:button id="buttonCancelar" buttonType="removeButton" caption="cancelar" onclick="cancelar()"/>
			<adsm:button id="buttonCancelarAproveitamento" buttonType="removeButton" caption="cancelarAproveitamentoBotao" onclick="cancelarAproveitamento()"/>
		</adsm:buttonBar>
	</adsm:grid>
 
	
</adsm:window>
<script>

	function find(){
		var tab = getTab(document);
		
		if (tab.validate({name:"findButton_click"})) {
			if (getElementValue("manifestoEntrega.idManifestoEntrega") != "" ||
				getElementValue("controleCarga.idControleCarga") != "" ||
				(getElementValue("dhEmissaoManifestoInicial") != "" && getElementValue("dhEmissaoManifestoFinal") != "")) {
				
				findButtonScript('manifestoEntrega', document.getElementById("form_idManifestoEntrega"))
				
			} else {
				
				var msg = i18NLabel.getLabel("LMS-09081")	
				alert(msg);
			}
		}
	}

	function emitirManifesto() {

		var data = manifestoEntregaGridDef.getSelectedIds();
				
		if (data != undefined && data.ids != undefined && data.ids.length > 0){
			
			var ids = data.ids;
			_serviceDataObjects = new Array(); 						
			
			for (var i=0; i < ids.length; i++){
				var newData = new Object();		
				newData.idManifesto = ids[i];	
				newData._reportCall = true; 			
				addServiceDataObject(createServiceDataObject("lms.entrega.reemitirCancelarManifestosAction.executeManifesto", "openReportWithLocator", newData)); 

			}		
			xmit(false);	
		} else {
			alert(i18NLabel.getLabel("LMS-09087"));
		}
	}

	function cancelar() {
		var data = manifestoEntregaGridDef.getSelectedIds();
		setNestedBeanPropertyValue(data,"botaoCancelar","botaoCancelar");
		if (data != undefined && data.ids != undefined && data.ids.length > 0){
			var sdo = createServiceDataObject("lms.entrega.reemitirCancelarManifestosAction.executeCancelar",
					"executeCancelar",data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			alert(i18NLabel.getLabel("LMS-09087"));
		}
	}
	
	function cancelarAproveitamento() {
		var data = manifestoEntregaGridDef.getSelectedIds();
		setNestedBeanPropertyValue(data,"botaoCancelar","botaoCancelarAproveitamento");
		if (data != undefined && data.ids != undefined && data.ids.length > 0){
			var sdo = createServiceDataObject("lms.entrega.reemitirCancelarManifestosAction.executeCancelar",
					"executeCancelar",data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			alert(i18NLabel.getLabel("LMS-09087"));
		}
	}
	
	
	function executeCancelar_cb(data, error){
		if (error != undefined){
			alert(error);			
			return;
		}
		
		manifestoEntregaGridDef.executeLastSearch(true);
				
	}
	
	function filialOnChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		
		if (obj.value == ''){
			setDisabled("manifestoEntrega.idManifestoEntrega", true);
			resetValue("manifestoEntrega.idManifestoEntrega");
			
			setDisabled("controleCarga.idControleCarga", true);
			resetValue("controleCarga.idControleCarga");
		}
		return retorno;
	}
	
	
	function filialDataLoad_cb(data){
		lookupExactMatch({e:document.getElementById("filial.idFilial"), data:data, callBack:"filialDataLoadLikeEnd"});
				
		if (data != undefined){
			setDisabled("manifestoEntrega.idManifestoEntrega", false);
			setDisabled("controleCarga.idControleCarga", false);
		}
	}
	
	function filialDataLoadLikeEnd_cb(data){
		
		if (data != undefined){
			setDisabled("manifestoEntrega.idManifestoEntrega", false);
			setDisabled("controleCarga.idControleCarga", false);
		}
		
		return filial_sgFilial_likeEndMatch_cb(data);
	}
	
	function filialPopup(data){
		if (data != undefined){
			setDisabled("manifestoEntrega.idManifestoEntrega", false);
			setDisabled("controleCarga.idControleCarga", false);
		}
		
		return true;
	}
	
	function manifestoEntrega_filialChange(elem) {
		setDisabled("manifestoEntrega.idManifestoEntrega",elem.value == "");
		return manifestoEntrega_filial_sgFilialOnChangeHandler();
	}
	
	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	
	function initWindow(eventObj) {	
		
		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_load") {
			setaValoresFilial();
			if (manifestoEntregaGridDef.gridState["data"].length > 0){
				//setDisabled("buttonEmitir", false);
				//setDisabled("buttonCancelar", false);
			}
		}
    }
    
    function gridDataLoad_cb(data){    	
    	if (data.list == undefined || data.list.length <= 0) {
    		setDisabled("buttonEmitir", true);
			setDisabled("buttonCancelar", true);
    	}
    }
    
    function pageLoadCustom_cb(){
    	onPageLoad_cb();
    	setMasterLink(document, true);
    	if (!document.getElementById("filial.idFilial").masterLink) {
    		getFilialUsuario();
    	}
    }
     
    function setaValoresFilial() { 
		if (!document.getElementById("filial.idFilial").masterLink) {
			setElementValue("filial.idFilial", idFilialLogado);
			setElementValue("filial.sgFilial", sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
			
			setElementValue("manifestoEntrega.filial.idFilial", idFilialLogado);
			setElementValue("manifestoEntrega.filial.sgFilial", sgFilialLogado);
			setElementValue("manifestoEntrega.filial.pessoa.nmFantasia", nmFilialLogado);
			setDisabled("manifestoEntrega.idManifestoEntrega", false);
	
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilialLogado);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilialLogado);
			setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", nmFilialLogado);			
			setDisabled("controleCarga.idControleCarga", false);
			
			setFocusOnFirstFocusableField();
		}
	}
	
	
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.entrega.reemitirCancelarManifestosAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}


	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"nmFantasia");
			setaValoresFilial();
		}
	}

	function manifestoRowClick(){
		return false;
	}
	
	
	/**
	 * Controla o objeto de controle carga
	 */	
	function sgFilialOnChangeHandler() {
		var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
		} else {
			disableNrControleCarga(false);
		}
		return r;
	}
	
	function retornoControleCargaFilial_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
		if (r == true) {
			setDisabled('controleCarga.idControleCarga', false);
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
		}
		return r;
	}
	
	function popupControleCarga(data) {
		setDisabled('controleCarga.idControleCarga', false);
	}
	
	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.idControleCarga", disable);
	}
	

</script>