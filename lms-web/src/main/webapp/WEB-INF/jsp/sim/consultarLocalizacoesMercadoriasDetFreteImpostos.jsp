<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" idProperty="idDoctoServico">
		<adsm:textbox label="situacaoTributaria" dataType="text" property="dsTipoTributacaoIcms" width="35%" disabled="true"/>
		<adsm:textbox label="cfop" dataType="integer" property="nrCfop" width="35%" disabled="true"/>
	</adsm:form>	
       	<adsm:grid service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedImpostos" onRowClick="returnFalse" property="impostoServico" idProperty="idImpostoServico" showGotoBox="false" showPagging="false" selectionMode="none" unique="false" rows="5" scrollBars="vertical" gridHeight="145" >
			<adsm:gridColumn width="150" title="imposto" property="tpImposto" isDomain="true"/>
			<adsm:gridColumn width="200" title="baseCalculo" property="vlBaseCalculo" align="right" dataType="currency"/>
			<adsm:gridColumn width="200" title="aliquota" property="pcAliquota" unit="percent" align="right"/>
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valorImposto" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn width="100" title="" property="vlImpostoServico"  align="right" dataType="currency"/>
		</adsm:grid>
	
</adsm:window>   
<script>
function findPaginatedImpostos(){
	
	var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
	var data = new Array();
	setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
	
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findTipoTributacaoIcms", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
	
	impostoServicoGridDef.executeSearch(data);
	
}

function returnFalse(){
	return false;
}
</script>