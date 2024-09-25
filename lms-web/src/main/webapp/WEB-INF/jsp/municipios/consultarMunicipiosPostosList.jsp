<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarMunicipiosAction" >
	<adsm:form action="/municipios/consultarMunicipios" idProperty="idPostoPassagemMunicipio" height="0">
		<adsm:hidden property="idMunicipioFilial" />
	</adsm:form>
	<adsm:grid 
	idProperty="idPostoPassagemMunicipio" 
	property="postoPassagemMunicipio" 
	unique="true"  
	showPagging="false" 
	service="lms.municipios.consultarMunicipiosAction.findPostosPassagemVigentesByMunFil" scrollBars="both" gridHeight="265" selectionMode="none" onRowClick="onRowClickList" detailFrameName="cad">
		<adsm:gridColumn title="postoPassagem" property="postoPassagem.tpPostoPassagem" isDomain="true" width="150" />
		<adsm:gridColumn title="localizacao" property="postoPassagem.municipio.nmMunicipio" width="210" />
		<adsm:gridColumn title="concessionaria" property="postoPassagem.concessionaria.pessoa.nmPessoa"  width="300" />
		<adsm:gridColumn title="sentido" property="postoPassagem.tpSentidoCobranca" isDomain="true" width="150" />
		<adsm:gridColumn title="rodovia" property="postoPassagem.rodovia.sgRodovia" width="180" />
		<adsm:gridColumn title="km" property="postoPassagem.nrKm" width="100" dataType="integer" mask="##,###.#"/>
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>
		
<script>
//função que está declarada no onShow do pai ->(consultarMunicipiosPostos)

 function realizaPaginacao(){
 	var idMunicipioFilial = getElementValue("idMunicipioFilial");
	if (idMunicipioFilial != undefined && idMunicipioFilial != ''){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMunicipioFilial", idMunicipioFilial);
		postoPassagemMunicipioGridDef.executeSearch(data);
	} else
		postoPassagemMunicipioGridDef.resetGrid();
 }
 
 function onRowClickList(){
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", false);
}

function initWindow(eventObj){
    if(eventObj.name == 'tab_click')
		setDisabledTabsDet(true);
}

function setDisabledTabsDet(disabled) {
	    var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", disabled);
}		
</script>