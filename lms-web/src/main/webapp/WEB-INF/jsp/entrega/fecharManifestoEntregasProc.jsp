<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	/**
	 *	FUNCÕES PADROES DE TELA
	 */
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		document.getElementById("__buttonBar:1.removeButton").buttonType = "removeButtonGrid";
		var sdo = createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.findDataPageLoad","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	
	function fechaByIds() {
		removeCallback = "ManifestoEntregaGridDef#removeByIds";
		var idsMap = ManifestoEntregaGridDef.getSelectedIds(); 
		if (idsMap.ids.length > 0) { 
			if (window.confirm(i18NLabel.getLabel("LMS-09094"))) {
			  	var remoteCall = {serviceDataObjects:new Array()}; 
				remoteCall.serviceDataObjects.push(createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.executeFechamentosManifestos", removeCallback, idsMap)); 
			  	xmit(remoteCall); 		
			}
		}
	}



	function onDataLoadSamples_cb(data) {
		fillFormWithFormBeanData(document.getElementById(mainForm).tabIndex, data);
	}
	/**
	 * IMPLEMENTA A FILIAL DA SESSAO
	 */
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
			setFilialChildrens(sgFilial);
		}
	}
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_click") {
			if (eventObj.name != "tab_click")
				writeDataSession();
			getTabGroup(this.document).setDisabledTab("pendencias",true);
		}
	}
	
	function setFilialChildrens(sgFilial) {
		if (sgFilial == "") {
			resetValue("controleCarga.filial.sgFilial");
			resetValue("manifestoEntrega.filial.sgFilial");
			setDisabled("manifestoEntrega.idManifestoEntrega",true);
			setDisabled("controleCarga.idControleCarga",true);
		}else{
			setElementValue("controleCarga.filial.sgFilial",sgFilial);
			setElementValue("manifestoEntrega.filial.sgFilial",sgFilial);
			setDisabled("manifestoEntrega.idManifestoEntrega",false);
			setDisabled("controleCarga.idControleCarga",false);
		}
		setFocus(document.getElementById("controleCarga.nrControleCarga"));
	}
	/**
	 * CONTROL LOOKUP FILIAL
	 */
	function dataLoadFilial_cb(data) {
		filial_sgFilial_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			setFilialChildrens(getNestedBeanPropertyValue(data[0],"sgFilial"));		
	}

	function changeFilial(data) {
		var flag = filial_sgFilialOnChangeHandler();
		if (getElementValue("filial.idFilial") == "")
			setFilialChildrens("");		
		return flag;
	}

	function setPopUpFilial(data) {
		setFilialChildrens(getNestedBeanPropertyValue(data,"sgFilial"));		
		return true;
	}

	/**
	 * CONTROL LOOKUP MANIFESTO DE ENTREGA
	 */
	function dataLoadManifesto_cb(data) {
		manifestoEntrega_nrManifestoEntrega_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadControleCarga(getNestedBeanPropertyValue(data[0],"idManifestoEntrega"));		
	}

	function setPopUpManifesto(data) {
		loadControleCarga(getNestedBeanPropertyValue(data,"idManifestoEntrega"));		
		return true;
	}

	function loadControleCarga(idManifestoEntrega) {
		var sdo = createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.findControleCargaByManifesto","onDataLoadSamples",{e:idManifestoEntrega});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function controleCargaDataLoad_cb(data) {
		if (data.length == 1) {
			resetValue("manifestoEntrega.idManifestoEntrega");
		}
		return controleCarga_nrControleCarga_exactMatch_cb(data);
	}
	
	function controleCargaPopupSetValue(data) {
		resetValue("manifestoEntrega.idManifestoEntrega");
	}
	
	/**
	 * COMPORTAMENTO DA GRID
	 */
	function rowGridClick(id) {
		var data = ManifestoEntregaGridDef.findById(id);
		
		var tabGroup = getTabGroup(document);
		var formPendencia = tabGroup.getTab('pendencias').getDocument().forms[0];
		tabGroup.setDisabledTab("pendencias",false);
		tabGroup.selectTab(1);
		eval("tabGroup.getTab('pendencias').getDocument().parentWindow." + formPendencia.onDataLoadCallBack + "_cb(data)");
		return false;
	}
//-->
</script>
<adsm:window service="lms.entrega.fecharManifestoEntregasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="entrega/fecharManifestoEntregas">
		<adsm:i18nLabels>
			<adsm:include key="LMS-09094"/>
		</adsm:i18nLabels>
		<adsm:lookup label="filial" service="lms.entrega.fecharManifestoEntregasAction.findLookupFilial" onPopupSetValue="setPopUpFilial"
				property="filial" idProperty="idFilial" dataType="text" required="true" size="3" onDataLoadCallBack="dataLoadFilial"
				criteriaProperty="sgFilial" action="/municipios/manterFiliais" maxLength="3" width="34%" labelWidth="16%" onchange="return changeFilial();" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/> 
		</adsm:lookup>
		
		
		<adsm:textbox dataType="text" property="controleCarga.filial.sgFilial" label="controleCargas" disabled="true" size="3" labelWidth="16%" width="34%" serializable="false">

			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.entrega.fecharManifestoEntregasAction.findLookupControleCarga" mask="00000000" disabled="true"
						 action="/carregamento/manterControleCargas" cmd="list" maxLength="8" size="8" popupLabel="pesquisarControleCarga"
						 onDataLoadCallBack="controleCargaDataLoad" onPopupSetValue="controleCargaPopupSetValue" >
				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false"/>									  
				<adsm:propertyMapping criteriaProperty="controleCarga.tpControleCarga" modelProperty="tpControleCarga"/>
			</adsm:lookup>
			
			<adsm:hidden property="controleCarga.tpControleCarga" serializable="false" value="C"/>

		</adsm:textbox>	
		
		<adsm:textbox label="manifestoEntrega" labelWidth="16%" width="34%" dataType="text" disabled="true" size="3" property="manifestoEntrega.filial.sgFilial" serializable="false">

			<adsm:lookup serializable="true" service="lms.entrega.fecharManifestoEntregasAction.findLookupManifestoEntrega" 
   					 	 dataType="integer" property="manifestoEntrega" idProperty="idManifestoEntrega" criteriaProperty="nrManifestoEntrega" 
   						 size="20" maxLength="16" action="/entrega/consultarManifestosEntrega" cmd="lookup" mask="00000000" disabled="true"
   						 onPopupSetValue="setPopUpManifesto" onDataLoadCallBack="dataLoadManifesto" popupLabel="pesquisarManifestoEntrega">
   				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/> 	  	 
   				<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/> 	  	 
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/> 	  	 
   		   </adsm:lookup>
    
        </adsm:textbox>
        
        <adsm:combobox domain="DM_TIPO_MANIFESTO_ENTREGA" label="tipo" labelWidth="16%" width="34%" property="tpManifesto" renderOptions="true"/>
		
		<adsm:lookup action="/municipios/manterRotaColetaEntrega" dataType="integer" property="rotaColetaEntrega"
	        		labelWidth="16%" service="lms.entrega.fecharManifestoEntregasAction.findLookupRotaColetaEntrega" 
			        idProperty="idRotaColetaEntrega" label="rota" size="3" width="80%" maxLength="3" criteriaProperty="nrRota">

	        <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
   	        <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
   	        <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
   	        
	        <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota"/>
			<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>
        
        
        <adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" picker="false"
				service="lms.entrega.fecharManifestoEntregasAction.findLookupMeioTransporte"
				action="/contratacaoVeiculos/manterMeiosTransporte" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="16%" width="9%" size="8" serializable="false" maxLength="6">
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte" required="false"
				service="lms.entrega.fecharManifestoEntregasAction.findLookupMeioTransporte" maxLength="25" size="20"
				action="/contratacaoVeiculos/manterMeiosTransporte" criteriaProperty="nrIdentificador" width="25%">
			<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota"/>
			<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte"/>	
			<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota"/>		
		</adsm:lookup>


        <adsm:lookup dataType="text" property="meioTransporte2SemiReboque" idProperty="idMeioTransporte" picker="false"
				service="lms.entrega.fecharManifestoEntregasAction.findLookupMeioTransporte"
				action="/contratacaoVeiculos/manterMeiosTransporte" criteriaProperty="nrFrota"
				label="semiReboque" labelWidth="16%" width="9%" size="8" serializable="false" maxLength="6">
			<adsm:propertyMapping criteriaProperty="meioTransporteSemiReboque.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporteSemiReboque.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporteSemiReboque.nrIdentificador" modelProperty="nrIdentificador"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteSemiReboque" idProperty="idMeioTransporte" required="false"
				service="lms.entrega.fecharManifestoEntregasAction.findLookupMeioTransporte" maxLength="25" size="20"
				action="/contratacaoVeiculos/manterMeiosTransporte" criteriaProperty="nrIdentificador" width="25%">
			<adsm:propertyMapping criteriaProperty="meioTransporte2SemiReboque.nrFrota" modelProperty="nrFrota"/>
			<adsm:propertyMapping relatedProperty="meioTransporte2SemiReboque.idMeioTransporte" modelProperty="idMeioTransporte"/>	
			<adsm:propertyMapping relatedProperty="meioTransporte2SemiReboque.nrFrota" modelProperty="nrFrota"/>		
		</adsm:lookup> 
		
		<adsm:buttonBar freeLayout="true">
				<adsm:findButton callbackProperty="ManifestoEntrega"/>
				<adsm:resetButton/>
		</adsm:buttonBar>
		</adsm:form>
		<adsm:grid property="ManifestoEntrega" idProperty="MANI_ID_MANIFESTO" gridHeight="200" 
				   unique="true" scrollBars="horizontal" onRowClick="rowGridClick">		
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoEntrega" property="MANI_SG_FILIAL" width="100"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="MANI_NR_MANIFESTO" width="60" />
		</adsm:gridColumnGroup>		
	
		<adsm:gridColumn width="150" title="tipo" property="TP_MANIFESTO" isDomain="true"/>

		<adsm:gridColumn width="130" title="dataHoraEmissao" property="DH_EMISSAO_MANIFESTO" dataType="JTDateTimeZone"/>		
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="controleCarga" property="CC_FI_SG" width="60"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="CC_NR" width="60"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="rota" property="ROTA_NR" width="120" mask="000" dataType="integer" align="left"/>
			<adsm:gridColumn title="" dataType="text" property="ROTA_DS" width="120"/>
		</adsm:gridColumnGroup>		
		
		<adsm:gridColumn width="45" title="meioTransporte" property="MT_NR_FROTA" align="left" />
		<adsm:gridColumn width="95" title="" property="MT_NR_IDENT" align="left"/>

		<adsm:gridColumn width="45" title="semiReboque" property="MTSR_NR_FROTA" align="left"/>
		<adsm:gridColumn width="95" title="" property="MTSR_NR_IDENT" align="left"/>
		
		<adsm:gridColumn width="95" title="pendencias" property="PENDENCIAS" dataType="integer" />	
		<adsm:gridColumn width="200" title="dataHoraChegadaPortaria" property="DH_CHEGADA_COLETA_ENTREGA" dataType="JTDateTimeZone"/>
		
		<adsm:gridColumn width="150" title="situacao" property="TP_STATUS_MANIFESTO" isDomain="true"/>

			<adsm:buttonBar>
				<adsm:button caption="fechar" id="__buttonBar:1.removeButton" onclick="fechaByIds()"/> 
			</adsm:buttonBar>
		</adsm:grid>
</adsm:window>	