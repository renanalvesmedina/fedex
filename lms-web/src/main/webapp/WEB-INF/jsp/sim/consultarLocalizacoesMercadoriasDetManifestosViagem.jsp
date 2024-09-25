<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	
			<adsm:grid property="manifesto" idProperty="idManifesto" showGotoBox="false" showPagging="false" selectionMode="none" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedManifestoViagem" onRowClick="returnFalse" unique="true" scrollBars="both" gridHeight="163">
			  	<adsm:gridColumnGroup customSeparator=" " >
					<adsm:gridColumn title="numero" property="sgFilialOrigem" width="100" dataType="text"/>
					<adsm:gridColumn title="" property="nrManifesto" dataType="integer" mask="00000000" width="0" />
				</adsm:gridColumnGroup>
				<adsm:gridColumn width="50" title="filialDestino" property="sgFilialDest" dataType="text"/>
				<adsm:gridColumn width="120" title="dataEmissao" property="dhEmissaoMan"  dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="saidaProgramada" property="dhSaidaProg"  dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="saidaEfetiva" property="dhSaidaEfet"  dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="previsaoChegada" property="dhPrevisaoCheg"  dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="chegada" property="dhCheg" align="center" dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="inicioDescarga" property="dhInicioDesc"  dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="fimDescarga" property="dhFimDesc"  dataType="JTDateTimeZone"/>
			</adsm:grid>
			
			<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="0" idProperty="idDoctoServico">
			</adsm:form>
	
</adsm:window> 
<script>
	function findManifestoViagemByIdDocto(){
		var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
		var data = new Array();
		setNestedBeanPropertyValue(data, "idDoctoServico",idDoctoServico );
		manifestoGridDef.executeSearch(data);
	  
  	}
  	function returnFalse(){
  		return false;
  	}


</script>  