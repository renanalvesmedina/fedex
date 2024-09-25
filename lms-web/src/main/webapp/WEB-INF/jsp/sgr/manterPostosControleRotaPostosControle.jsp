<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.sgr.manterPostosControleRotaAction">
	<adsm:form action="/sgr/manterPostosControleRota" idProperty="idRotaPostoControle">
	
		<adsm:textbox dataType="text" property="rota.dsRota" serializable="false" 
					  label="rota" labelWidth="20%" width="80%" maxLength="60" size="30" disabled="true" />
		
		<adsm:hidden property="rota.idRota" />
		<adsm:hidden property="nrOrdem" />
		<adsm:hidden property="active" value="A" serializable="false"/>
		
		<adsm:lookup dataType="text" property="postoControle" idProperty="idPostoControle" criteriaProperty="nmPostoControlePassaporte"
					 action="/sgr/manterPostosControle" service="lms.sgr.postoControleService.findLookup" minLengthForAutoPopUpSearch="3" exactMatch="false"
					 label="postoControle" labelWidth="20%" width="80%" size="54" maxLength="50" required="true">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="active"/> 
		</adsm:lookup>
					 
		<adsm:textbox dataType="JTTime" mask="hhh:mm" property="nrTempoProximoPosto" label="tempoProximoPosto" labelWidth="20%" width="30%" size="6" maxLength="6"/>
				      
		<adsm:textbox dataType="integer" property="nrKmProximoPosto" 
					  label="distanciaProximoPosto" labelWidth="24%" width="26%" size="5" maxLength="5" unit="km2" mask="#,###"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton/>
			<adsm:newButton/>
		</adsm:buttonBar>	
	</adsm:form>
	
	<adsm:grid property="rotaPostoControles" idProperty="idRotaPostoControle" 
			   onRowClick="populaForm" defaultOrder="nrOrdem:asc" autoSearch="false" scrollBars="horizontal"
			   service="lms.sgr.manterPostosControleRotaAction.findPaginated" rows="10" gridHeight="210"
			   rowCountService="lms.sgr.manterPostosControleRotaAction.getRowCount">
		<adsm:gridColumn property="nrOrdem" title="ordem" width="42" align="right" />
		<adsm:gridColumn property="postoControle.nmPostoControlePassaporte" title="postoControle" width="175" />
		<adsm:gridColumn property="postoControle.nmLocal" title="nomePostoLocal"  width="175" />
		<adsm:gridColumn property="postoControle.municipio.nmMunicipio" title="municipio" width="167" />
		<adsm:gridColumn property="postoControle.municipio.unidadeFederativa.sgUnidadeFederativa" title="uf" width="42" />
		<adsm:gridColumn property="postoControle.rodovia.sgRodovia" title="rodovia" width="56" />
		<adsm:gridColumn property="postoControle.nrKm" title="km" width="42" align="right" />
		<adsm:gridColumn property="nrTempoProximoPosto" title="tempoProximoPosto" width="110" align="center" dataType="text" />
		<adsm:gridColumn property="nrKmProximoPosto" title="distanciaProximoPosto" width="110" align="right" unit="km2" mask="###,###" dataType="decimal" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	var rota = new Array();
	function initWindow(eventObj) {
		if (eventObj.name == "removeButton_grid" || eventObj.name == "newButton_click"){
			novaDescricaoPadrao();
		} else if (eventObj.name == "tab_click") {
			resetValue("idRotaPostoControle");
			preparaDescricaoPadrao();
		} else if (eventObj.name == "storeButton") {
			populaGrid();
		}
	}
	
	function novaDescricaoPadrao() {
		populaDadosMaster();
		resetValue("postoControle.nmPostoControlePassaporte");
		resetValue("nrTempoProximoPosto");
		resetValue("nrKmProximoPosto");
		setDefaultFieldsValues();
	}
	
	function populaDadosMaster() {
		var idRotaValue = getNestedBeanPropertyValue(rota, "idRota");
		var dsRotaValue = getNestedBeanPropertyValue(rota, "dsRota");
	
		setElementValue("rota.idRota", idRotaValue);
		setElementValue("rota.dsRota", dsRotaValue);
	}
	
	function preparaDescricaoPadrao() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		
		var idRota = tabDet.getFormProperty("idRota");
		
		if (idRota != undefined && idRota != '') {
			var frame = parent.document.frames["cad_iframe"];
			var dsRota = tabDet.getFormProperty("rota.dsRota");
	
			setElementValue("rota.idRota", idRota);
			setElementValue("rota.dsRota", dsRota);
	
			setNestedBeanPropertyValue(rota, "idRota", idRota);
			setNestedBeanPropertyValue(rota, "dsRota", dsRota);
			
			populaGrid();
			novaDescricaoPadrao();
		}
	}
		
	function populaGrid() {
		rotaPostoControlesGridDef.executeSearch(
			{
				rota:{idRota:getElementValue("rota.idRota")}
			}, true);
	}
	
	function populaForm(valor) {
		onDataLoad(valor);
		populaDadosMaster();
		return false;
	}
	
</script>