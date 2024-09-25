<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRotas" service="lms.municipios.consultarRotasViagemAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/municipios/consultarRotas" idProperty="idRotaViagem" >
 		  <adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="filialOrigem"/>
			<adsm:include key="filialDestino"/>
			<adsm:include key="filialIntegranteRota"/>
			<adsm:include key="numeroRota"/>
		</adsm:i18nLabels> 
		<adsm:hidden property="isLookup" serializable="false" value="N"/>
		<adsm:hidden property="tpEmpresaMercurioValue" value="M" serializable="false" />
		
        <adsm:lookup property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialOrigem" width="32%" labelWidth="18%"
				exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping relatedProperty="filialOrigem.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
		
		<adsm:lookup property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" 
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialDestino" width="32%" labelWidth="18%"
				exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping relatedProperty="filialDestino.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
		
		<adsm:lookup property="filialIntermediaria" idProperty="idFilial" criteriaProperty="sgFilial" 
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialIntegranteRota" width="32%" labelWidth="18%"
				exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping relatedProperty="filialIntermediaria.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialIntermediaria.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
 
		<adsm:textbox dataType="integer" property="nrRota" label="numeroRota" labelWidth="18%" size="3" maxLength="4" width="32%" mask="0000"/>
			
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.municipios.consultarRotasViagemAction.findTipoMeioTransporte"
				label="tipoMeioTransporte" labelWidth="18%" width="32%" boxWidth="180" cellStyle="vertical-align:bottom;" >
		</adsm:combobox>
        
       <adsm:combobox property="tpRota" label="tipoRota" domain="DM_TIPO_ROTA_VIAGEM" labelWidth="18%" width="32%" />

	   <adsm:combobox property="tpSistemaRota" label="sistemaRota" domain="DM_SISTEMA_ROTA" labelWidth="18%" width="32%" />

	   <adsm:textbox dataType="JTTime" label="horarioSaidaRota" property="hrSaida" size="4" labelWidth="18%" width="32%" />

	   <adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" defaultValue="S" labelWidth="18%" width="32%" />
		
       <adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaViagem" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="rotaViagem" selectionMode="none" idProperty="idRotaViagem" scrollBars="horizontal" 
			   unique="true" rows="9"
			   detailFrameName="rtIda" onRowClick="onRowClickRota();" gridHeight="180">	
		<adsm:gridColumn title="tipoRota" property="tpRota" width="90" />
		 
		<adsm:gridColumn title="rotaIda" property="nrRotaIda" dataType="integer" mask="0000" width="30" />
		<adsm:gridColumn title="" property="dsRotaIda" width="180" />
		<adsm:gridColumn title="horarioSaidaRotaIda" property="hrSaidaIda" dataType="JTTime" width="145"/>

		<adsm:gridColumn title="rotaVolta" property="nrRotaVolta" dataType="integer" mask="0000" width="30" />
		<adsm:gridColumn title="" property="dsRotaVolta" width="180" />
		<adsm:gridColumn title="horarioSaidaRotaVolta" property="hrSaidaVolta" dataType="JTTime" width="165"/>
		
		<adsm:gridColumn title="tipoMeioTransporte" property="dsTipoMeioTransporte" width="155" />
		<adsm:gridColumn title="sistemaRota" property="tpSistemaRota" width="110" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="90" />
		
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>

<script>
	
	document.getElementById("tpEmpresaMercurioValue").masterLink = true;

	function onRowClickRota() {
		if (document.getElementById("isLookup").value == "N") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab('rtIda', false );
		}
	}

	function initWindow(event) {
		if (event.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("rtIda", true );
			tabGroup.setDisabledTab("rtVolta", true );
			//tabGroup.setDisabledTab("event", true );
			tabGroup.setDisabledTab("expr", true );
			tabGroup.setDisabledTab("mot", true );
		} else if (event.name == "cleanButton_click"){
				  writeDataSession();
		}
		
		resetTabFlags("rtVolta");
		resetTabFlags("expr");
		resetTabFlags("mot");
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)){
			if (getElementValue("filialOrigem.idFilial") != "" ||
					getElementValue("filialDestino.idFilial") != "" ||
					getElementValue("filialIntermediaria.idFilial") != "" || 
					getElementValue("nrRota") != "") {
				return true;	
			} else {
				alert(i18NLabel.getLabel("LMS-00013") + ' ' + i18NLabel.getLabel("filialOrigem") + ', ' 
							+ i18NLabel.getLabel("filialDestino") + ', ' 
							+ i18NLabel.getLabel("filialIntegranteRota") + ', '
							+ i18NLabel.getLabel("numeroRota") + ".");
				
			}
		}
		
		return false;
	}
	
	function resetTabFlags(nmTab) {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab(nmTab);
		if (tabDet != undefined &&
				tabDet.getDocument() != undefined &&
				tabDet.getDocument().parentWindow != undefined) {
			tabDet.getDocument().parentWindow.ultimoIdRota = -1;
		}
	}
	
	
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"nmFantasia");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (idFilial != null && sgFilial != null && nmFilial != null) {
			setElementValue("filialIntermediaria.idFilial",idFilial);
			setElementValue("filialIntermediaria.sgFilial",sgFilial);
			setElementValue("filialIntermediaria.nmFilial",nmFilial);
		}
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.municipios.consultarRotasViagemAction.findFilialUsuarioLogado","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
</script>