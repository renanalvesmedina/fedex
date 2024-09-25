<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	
		<adsm:grid property="notaFiscalCtoCooperada" idProperty="idNotaFiscalCtoCooperada" onRowClick="returnFalse" scrollBars="vertical" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedNotaFiscalAbaParcerias" showGotoBox="false" showPagging="false" selectionMode="none" unique="true" gridHeight="180">
			<adsm:gridColumn width="150" title="numero" property="nrNotaFiscal" dataType="integer" mask="000000"/>
			<adsm:gridColumn width="100" title="serie" property="dsSerie" dataType="text"/>
			<adsm:gridColumn width="130" title="dataEmissao" property="dtEmissao" dataType="JTDate"/>
			<adsm:gridColumn width="100" title="volumes" property="qtVolumes" dataType="integer"/>
			<adsm:gridColumn width="100" title="peso" property="psMercadoria" unit="kg" dataType="decimal" mask="###,###,###.0.00"/>
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valorMercadoria" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn width="100" title="" property="vlTotal" dataType="currency"/>
		</adsm:grid>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="0" idProperty="idDoctoServico">	
	</adsm:form>
</adsm:window>  
<script>
function findPaginatedNotasFiscaisParcerias(){
	notaFiscalCtoCooperadaGridDef.resetGrid;
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
		
    var data = new Array();
	setNestedBeanPropertyValue(data, "idDoctoServico", idDoctoServico);
	
	notaFiscalCtoCooperadaGridDef.executeSearch(data);
	
}

function returnFalse(){
	return false;
}
	
</script> 