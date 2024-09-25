<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:grid property="manifestoEntrega" scrollBars="vertical" idProperty="idManifestoEntrega" showGotoBox="false" showPagging="false" selectionMode="none" onRowClick="returnFalse" gridHeight="163" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedManifestoEntrega" unique="true" >
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numero" property="sgFilial" width="100" dataType="text"/>
			<adsm:gridColumn title="" property="nrManifestoEntrega" dataType="integer" mask="00000000" width="0" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="100" title="tipo" property="tpManifestoEntrega" isDomain="true"/>
		<adsm:gridColumn width="150" title="dataEmissao" property="dhEmissaoManifesto" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="150" title="ocorrencia" property="dsOcorrenciaEntrega" dataType="text"/>
		<adsm:gridColumn width="170" title="recebedor" property="nmRecebedor" dataType="text"/>
	</adsm:grid>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="0" idProperty="idDoctoServico">		
	</adsm:form>
	
</adsm:window>   
<script>
	function findManifestoEntregaByIdDocto(){
		var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
		var data = new Array();
		setNestedBeanPropertyValue(data, "idDoctoServico",idDoctoServico );
		manifestoEntregaGridDef.executeSearch(data);
	  
  	}
  	function returnFalse(){
  		return false;
  	}


</script>  