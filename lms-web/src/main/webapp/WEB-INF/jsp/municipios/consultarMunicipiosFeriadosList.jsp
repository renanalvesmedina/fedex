<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarMunicipiosAction" >
	<adsm:form idProperty="idFeriado" action="/municipios/consultarMunicipios" >
		<adsm:hidden property="idMunicipio"/>
	</adsm:form>
	<adsm:grid idProperty="idFeriado" property="feriado" gridHeight="327" unique="true"  showPagging="false" onRowClick="retornaFalse" selectionMode="none" service="lms.municipios.consultarMunicipiosAction.findFeriadosVigentesByIdMunicipio">
		<adsm:gridColumn title="diaMes" property="dtFeriado" width="100" dataType="date" mask="dd/MM"/>
	 	<adsm:gridColumn title="descricao" property="dsFeriado" width="150" />
	 	<adsm:gridColumn title="facultativo" property="blFacultativo" renderMode="image-check" width="100"/>
		<adsm:gridColumn title="tipo" property="tpFeriado" width="100" isDomain="true" />
		<adsm:gridColumn title="abrangencia" property="abrangencia" width="100" />
		<adsm:gridColumn title="local" property="local" width="200" />
		
		<adsm:buttonBar freeLayout="true"/> 
	</adsm:grid>
</adsm:window>
<script>
    var ultimoIdMunicipio = -1;
	function detalhamentoAba() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idMunicipio = tabDet.getFormProperty("idMunicipio");
		if (idMunicipio == ultimoIdMunicipio)
			return false;
		ultimoIdMunicipio = idMunicipio;

		realizaPaginacaoFeriados(idMunicipio);
	}

 function realizaPaginacaoFeriados(idMunicipio){
	if (idMunicipio != undefined && idMunicipio != ''){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMunicipio", idMunicipio);
		feriadoGridDef.executeSearch(data);
	} else
		feriadoGridDef.resetGrid();
}

function retornaFalse(){
	return false;
}
</script>

