<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterQuilometragensSaidaChegadaAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/portaria/manterQuilometragensSaidaChegada" idProperty="idControleQuilometragem" >
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/> 
		
		<adsm:lookup service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupFilial" dataType="text"
				property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				width="83%" labelWidth="17%" action="/municipios/manterFiliais" idProperty="idFilial" disabled="true">
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>
        
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte"
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupMeioTransp" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="17%"
				width="83%" size="8" serializable="false" maxLength="6" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
					
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte"
					service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupMeioTransp" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="20" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
			</adsm:lookup>
		</adsm:lookup>
		
		
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>

		<adsm:lookup dataType="text" 
				property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupFilial" popupLabel="pesquisarFilial"
				label="controleCarga" size="3" maxLength="3" width="33%" labelWidth="17%" picker="false" serializable="false"
				action="/municipios/manterFiliais" onchange="return onFilialControleCargaChange(this);" >
				
			<adsm:lookup dataType="integer"
					property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupControleCarga"
					action="carregamento/manterControleCargas" size="8" mask="00000000" disabled="true" popupLabel="pesquisarControleCarga" >
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" 
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" 
						modelProperty="filialByIdFilialOrigem.sgFilial" />
	        </adsm:lookup>
	     </adsm:lookup>

		<adsm:combobox property="blSaida" label="saida" domain="DM_SIM_NAO" labelWidth="15%" width="35%" />		
		
		<adsm:range label="dataHoraRegistro" labelWidth="17%" width="83%" maxInterval="30" >
             <adsm:textbox dataType="JTDate" property="dtMedicaoInicial" />
             <adsm:textbox dataType="JTDate" property="dtMedicaoFinal" />
        </adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="controleQuilometragem" /> 
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="controleQuilometragem" idProperty="idControleQuilometragem" selectionMode="none"
			gridHeight="180" unique="true" rows="11" onRowClick="onRowClickDef"
			service="lms.portaria.manterQuilometragensSaidaChegadaAction.findPaginatedTela"
			rowCountService="lms.portaria.manterQuilometragensSaidaChegadaAction.getRowCountTela" >
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="sgFilial" width="70" />
			<adsm:gridColumn title="" property="nmFantasia" width="70" />
		</adsm:gridColumnGroup> 
		
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="60"  />
		<adsm:gridColumn title="" property="nrIdentificador" width="70" />
		
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA" >
			<adsm:gridColumn title="controleCarga" property="sgFilialControleCarga" width="60" />
			<adsm:gridColumn title="" property="nrControleCarga" width="60" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
				
		<adsm:gridColumn title="dataHoraRegistro" property="dhMedicao" width="130" align="center" dataType="JTDateTimeZone" />	
		<adsm:gridColumn title="tipo" property="tipo" width="70"/>
		<adsm:gridColumn title="quilometragem" property="nrQuilometragem" mask="###,###,###" width="100" dataType="integer" />
		
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	function onRowClickDef() {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab('cad', false );
	}

	function initWindow(event) {
		if (event.name == "tab_click" || event.name == "tab_load") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("cad", true );
		} else if (event.name == "cleanButton_click") {
			populateFilial();
			setDisabled("controleCarga.idControleCarga",true);
		}
	}
	
	function onFilialControleCargaChange(elem) {
		setDisabled("controleCarga.idControleCarga",elem.value == "");
		return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	
	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		if (getElementValue("idProcessoWorkflow") == "")
			findFilialUsuarioLogado();
	}	
	
	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.portaria.manterQuilometragensSaidaChegadaAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var idFilial = undefined;
	var sgFilial = undefined;
	var nmFilial = undefined;
	
	function findFilialUsuarioLogado_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
		
		populateFilial();
	}

	function populateFilial() {
		setElementValue("filial.idFilial",idFilial);
		setElementValue("filial.sgFilial",sgFilial);
		setElementValue("filial.pessoa.nmFantasia",nmFilial);
		
		//LMS-3468
		if(sgFilial == "MTZ") {
			setDisabled('filial.idFilial',false);
		}
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("meioTransporteRodoviario2.idMeioTransporte") != "" ||
					(getElementValue("dtMedicaoInicial") != "" && getElementValue("dtMedicaoFinal") != "")) {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013")
						+ document.getElementById("meioTransporteRodoviario2.idMeioTransporte").label + ', '
						+ document.getElementById("dtMedicaoInicial").label + ".");
			}
		}
		return false;
	}
	
//-->
</script>

