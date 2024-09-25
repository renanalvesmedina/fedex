<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarMunicipiosAction">
	<adsm:form idProperty="idMunicipioFilial" action="/municipios/consultarMunicipios">
		<adsm:hidden property="idMunicipio"/>
	</adsm:form> 
	<adsm:grid 
	idProperty="idMunicipioFilial" 
	property="municipioFilial" 
	unique="true" 
	service="lms.municipios.consultarMunicipiosAction.findMunicipioFilialVigenteAtualByMunicipio"  
	selectionMode="none" showPagging="false" scrollBars="vertical" gridHeight="295" onRowClick="onRowClickList" detailFrameName="cad">
		<adsm:gridColumn title="empresa" property="nmEmpresa" width="350"/>
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="sgFilial" width="250"/>
			<adsm:gridColumn title="" property="nmFilial" width="100"/>
		</adsm:gridColumnGroup>	
		<adsm:buttonBar freeLayout="true" />
	</adsm:grid>
</adsm:window>
<script>
//função que está declarada no onShow do pai ->(consultarMunicipiosAten)

function realizaPaginacao(){
	var idMunicipio = getElementValue("idMunicipio");
	if (idMunicipio != undefined && idMunicipio != ''){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMunicipio", idMunicipio);
		municipioFilialGridDef.executeSearch(data);
	} else
		municipioFilialGridDef.resetGrid();
}

function onRowClickList(){
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("postos", false);
		
}

function initWindow(eventObj){
    if(eventObj.name == 'tab_click')
		setDisabledTabsDet(true);
}

function setDisabledTabsDet(disabled) {
	    var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", disabled);
		tabGroup.setDisabledTab("postos", disabled);
}		
</script>