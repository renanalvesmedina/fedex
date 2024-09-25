<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	    
	    <adsm:grid idProperty="idNotaFiscalConhecimento" property="notaFiscalConhecimento" onRowClick="returnFalse" service="lms.sim.consultarLocalizacoesMercadoriasAction.findNotasFiscaisByConhecimento" scrollBars="both" showGotoBox="false" showPagging="false" selectionMode="none" unique="false" rows="8" gridHeight="170" >
			<adsm:gridColumn width="100" title="numero" property="nrNotaFiscal" dataType="integer" mask="00000000"/>
			<adsm:gridColumn width="100" title="serie" property="dsSerie" dataType="integer"/>
			<adsm:gridColumn width="100" title="dataEmissao" property="dtEmissao"  dataType="JTDate"/>
			<adsm:gridColumn width="100" title="volumes" property="qtVolumes"  dataType="integer"/>
			<adsm:gridColumn width="100" title="peso" property="psMercadoria" unit="kg" dataType="decimal" mask="###,###,###0.000"/>
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valorMercadoria" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn width="150" title="" property="vlTotal"  dataType="currency"/>
			
			
			<adsm:gridColumn width="100" title="dataSaida" property="dtSaida"  dataType="JTDate"/>
			<adsm:gridColumn width="50" title="branco" property="LOOKUP" image="/images/popup.gif" link="sim/consultarLocalizacoesMercadorias.do?cmd=Itens" align="center" linkIdProperty="idNotaFiscalConhecimento"/>
		</adsm:grid>
		
	    
	   
	 
</adsm:window>   
<script>
function returnFalse(){
	return false;
}

function findPaginatedPrincipalNf(){
	notaFiscalConhecimentoGridDef.resetGrid;
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
		
    var data = new Array();
	setNestedBeanPropertyValue(data, "idDoctoServico", idDoctoServico);
	
	notaFiscalConhecimentoGridDef.executeSearch(data);
	
}


</script>