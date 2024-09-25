<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	    <adsm:grid property="controleCarga" idProperty="idControleCarga" gridHeight="170" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedControleCarga" showGotoBox="false" showPagging="false" selectionMode="none" scrollBars="horizontal" onRowClick="returnFalse" unique="false" >
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="controleCarga" property="sgFilialOrigem" width="100" dataType="text"/>
				<adsm:gridColumn title="" property="nrControle" dataType="integer" mask="00000000" width="0" />
			</adsm:gridColumnGroup>
				<adsm:gridColumn width="50" title="filialDestino" property="sgFilialDest" dataType="text"/>
				<adsm:gridColumn width="100" title="tipo" property="tpControleCarga" isDomain="true"/>
				<adsm:gridColumn width="150" title="rota" property="rota" dataType="text"/>
				<adsm:gridColumn width="120" title="dataGeracao" property="dhGeracao" dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="saida" property="dhSaida" dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="120" title="chegada" property="dhChegada" dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="100" title="situacao" property="tpStatusControleCarga" isDomain="true"/>
		</adsm:grid>
	 <adsm:form action="/sim/consultarLocalizacoesMercadorias" idProperty="idDoctoServico" height="1">	
     </adsm:form>
</adsm:window>  
<script>
function buscaIdControleCargaByDoctoServico(){
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	
	var data = new Array();
  	setNestedBeanPropertyValue(data,"idDoctoServico", idDoctoServico);
  	
	controleCargaGridDef.executeSearch(data);
	
}

function returnFalse(){
	return false;
}
</script> 