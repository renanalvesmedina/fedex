<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="200" idProperty="idDoctoServico">
		<adsm:i18nLabels>
			<adsm:include key="valor"/>
		</adsm:i18nLabels>
        <adsm:section caption="parcelasFrete2"/>
		<adsm:grid property="parcelaCtoCooperada" onDataLoadCallBack="onDataLoadGrid" idProperty="idParcelaCtoCooperada" selectionMode="none" onRowClick="returnFalse" showGotoBox="false" showPagging="false" unique="false" gridHeight="120"   service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedDadosFrete" >
			<adsm:gridColumn width="350" title="parcela" property="nmParcelaPreco" dataType="text"/>
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valor" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn width="320" title="" property="vlParcela" dataType="currency"/>
		</adsm:grid>
		
		<adsm:label key="branco" width="50%" style="border:none;"/>
		<adsm:textbox dataType="text" property="totalFrete" label="totalFrete" size="25" disabled="true" labelWidth="15%" width="35%" style="text-align:right"/>
		
		
	</adsm:form>

</adsm:window>   
<script>
function findPaginatedDadosFrete(){
	parcelaCtoCooperadaGridDef.resetGrid;
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
		
    var data = new Array();
	setNestedBeanPropertyValue(data, "idDoctoServico", idDoctoServico);
	
	parcelaCtoCooperadaGridDef.executeSearch(data);
	
}

//função chamada no onDataLoad da grid1
function onDataLoadGrid_cb(data, errorMessage){
	var idDoctoServico =  getElementValue("idDoctoServico");
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findVlFreteParceriasDadosFrete", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
}

function returnFalse(){
	return false;
}

</script>