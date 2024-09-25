<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
		<adsm:grid property="ocorrenciaDoctoServico" idProperty="idOcorrenciaDoctoServico" gridHeight="215" 
				   service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedBloqueiosLiberacoes" 
				  onRowClick="returnFalse" scrollBars="both"  showGotoBox="false" showPagging="false" selectionMode="none" unique="false" width="750">
			<adsm:gridColumn width="150" title="descricaoBloqueio" property="dsOcorrenciaBloqueio" dataType="text"/>
			<adsm:gridColumn width="150" title="dataBloqueio" property="dhBloqueio" dataType="JTDateTimeZone"/>
			<adsm:gridColumn width="100" title="filialBloqueio" property="sgFilialBloqueio" dataType="text"/>
			<adsm:gridColumn width="150" title="usuarioBloqueio" property="nmUsuarioBloqueio"  dataType="text"/>
			
			<adsm:gridColumn width="150" title="descricaoLiberacao" property="dsOcorrenciaLiberacao" dataType="text"/>	   
			<adsm:gridColumn width="150" title="dataLiberacao" property="dhLiberacao" dataType="JTDateTimeZone"/>
			<adsm:gridColumn width="100" title="filialLiberacao" property="sgFilialLiberacao" dataType="text"/>
			<adsm:gridColumn width="150" title="usuarioLiberacao" property="nmUsuarioLiberacao"  dataType="text"/>
		
		</adsm:grid>
		<adsm:form action="/sim/consultarLocalizacoesMercadorias" idProperty="idDoctoServico" height="2">
		</adsm:form>
						
	
</adsm:window>   
<script>
	function findBloqueiosLiberacoesByIdDocto(){
    	var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
		var data = new Array();
		setNestedBeanPropertyValue(data, "idDoctoServico",idDoctoServico);
		ocorrenciaDoctoServicoGridDef.executeSearch(data);
	}
  	
  	function returnFalse(){
  		return false;
  	}


</script>   