<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="180" idProperty="idDoctoServico">
		<adsm:i18nLabels>
			<adsm:include key="valor"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="nrDoctoServico"/>
		<adsm:hidden property="tpDoctoServico"/>
		<adsm:hidden property="sgFilialDoctoServico"/>
		<adsm:hidden property="idFilial"/>		
		<adsm:hidden property="flagFretePrincipal" serializable="false" value="true"/>
		
        <adsm:section caption="calculoFrete"/>
			<adsm:grid onDataLoadCallBack="setaTotalGrid1" property="parcelaDoctoServico" onRowClick="returnFalse" idProperty="idParcelaDoctoServico" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedParcelaPreco" selectionMode="none" showPagging="false" showGotoBox="false" unique="false" rows="5" scrollBars="vertical" gridHeight="100" >
				<adsm:gridColumn width="200" title="parcela" property="nmParcelaPreco"/>
				<adsm:gridColumnGroup customSeparator=" " >
					<adsm:gridColumn title="valor" property="sgMoeda" width="50" />
					<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
				</adsm:gridColumnGroup>
				<adsm:gridColumn width="200" title="" property="vlParcela" align="right" dataType="currency"/>
				<adsm:gridColumn width="250" title="analiseVertical" property="analise" unit="percent" dataType="decimal" align="right"/>
			</adsm:grid>
			<adsm:label key="branco" width="34%" style="border:none;"/>
			<adsm:textbox dataType="text" property="vlTotalParcelas"  label="totalFrete" size="25" disabled="true" labelWidth="15%" width="21%" style="text-align:right"/>			
			
	    <adsm:section caption="calculoServicos"/>
			<adsm:grid onDataLoadCallBack="setaTotalGrid2" property="parcelaPreco" onRowClick="returnFalse" idProperty="idParcelaPreco" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedCalculoServico" selectionMode="none" showGotoBox="false" showPagging="false" unique="false" scrollBars="vertical" gridHeight="100" >
				<adsm:gridColumn width="200" title="servicoAdicional" property="nmParcelaPreco"/>
				<adsm:gridColumnGroup customSeparator=" " >
					<adsm:gridColumn title="valor" property="sgMoeda" width="50" />
					<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
				</adsm:gridColumnGroup>
				<adsm:gridColumn width="200" title="" property="vlParcela" align="right" dataType="currency"/>
				<adsm:gridColumn width="250" title="analiseVertical" property="analise" dataType="decimal" unit="percent" align="right" />
			</adsm:grid>
			<adsm:label key="branco" width="30%" style="border:none;"/>
			<adsm:textbox dataType="text" property="vlTotalServicos" label="totalServicos" size="25" disabled="true" labelWidth="20%" width="21%" style="text-align:right"/>			
			<adsm:label key="branco" width="30%" style="border:none;"/>
			<adsm:textbox dataType="text" property="vlTotalDocServico" label="totalCTRCServicos" size="25" disabled="true" labelWidth="20%" width="21%" style="text-align:right"/>			
			<adsm:label key="branco" width="30%" style="border:none;"/>
			<adsm:textbox dataType="text" property="vlICMSST" label="retencaoIcmsSt" size="25" disabled="true" labelWidth="20%" width="21%" style="text-align:right"/>			
			<adsm:label key="branco" width="30%" style="border:none;"/>
			<adsm:textbox dataType="text" property="vlLiquido" label="totalFrete" size="25" disabled="true" labelWidth="20%" width="21%" style="text-align:right"/>			
			
			<adsm:buttonBar>
				<adsm:button caption="descontos" action="/contasReceber/manterDescontos" id="descontos" disabled="false" cmd="main">
					<%--adsm:linkProperty src="idDoctoServico" target="devedorDocServFat.idDevedorDocServFat"/--%>
					<adsm:linkProperty src="idDoctoServico" target="devedorDocServFat.doctoServico.idDoctoServico"/>	
					<adsm:linkProperty src="tpDoctoServico" target="devedorDocServFat.doctoServico.tpDocumentoServico" disabled="true"/>
					<adsm:linkProperty src="sgFilialDoctoServico" target="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial" disabled="true"/>
					<adsm:linkProperty src="nrDoctoServico" target="devedorDocServFat.doctoServico.nrDoctoServico" disabled="true"/>
					<adsm:linkProperty src="idFilial" target="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"/>	
					<adsm:linkProperty src="flagFretePrincipal" target="flagFretePrincipal"/>						
				</adsm:button>
			</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>
document.getElementById('descontos').onclick=function() {
	parent.parent.parent.parent.redirectPage(contextRoot + '/contasReceber/manterDescontos.do?cmd=main' + buildLinkPropertiesQueryString_descontos())
};

function returnFalse(){
	return false;
}
function findPaginatedParcelas(){
	var idDoctoServico = parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
	var data = new Array();
	setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
	
	parcelaDoctoServicoGridDef.executeSearch(data);
	
}

//função chamada no onDataLoad da grid1
function setaTotalGrid1_cb(data, errorMessage){
	if(data.length > 0){
		var vlTotalParcelas = data[0].vlTotalParcelas;
		var moeda = data[0].moeda;
		if(vlTotalParcelas != null)
			setElementValue("vlTotalParcelas",vlTotalParcelas);
	}
	var dataP = new Array();
	setNestedBeanPropertyValue(dataP,"idDoctoServico",getElementValue("idDoctoServico"));
	parcelaPrecoGridDef.executeSearch(dataP);
}

//função chamada no onDataLoad da grid2
function setaTotalGrid2_cb(data, errorMessage){
		var idDoctoServico = getElementValue("idDoctoServico");	
		
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findTotaisCalculoServico", "totaisLoad", {idDoctoServico:idDoctoServico}));
	  	xmit();
	  	
		verifyTpDoctoServico();	
			
}

function totaisLoad_cb(data, exception){
	if(data!= undefined){
		if(data.vlTotalServicos!= null)
			setElementValue("vlTotalServicos",data.vlTotalServicos);
		if(data.vlTotalDocServico!= null)
			setElementValue("vlTotalDocServico",data.vlTotalDocServico);
		if(data.vlICMSST!= null)
			setElementValue("vlICMSST",data.vlICMSST);
		if(data.vlLiquido!= null)
			setElementValue("vlLiquido",data.vlLiquido);		
	}	
}



function verifyTpDoctoServico(){
	var tpDoctoServico =  parent.parent.document.getElementById("tpDocumentoServicoLinkProperty").value;
	if(tpDoctoServico=='CTR' || tpDoctoServico=='NFT'){
		var nrDoctoServico =  parent.parent.document.getElementById("nrDoctoServico").value;
		var sgFilialDoctoServico =  parent.parent.document.getElementById("dsSgFilial").value;
		var idFilial =  parent.parent.document.getElementById("idFilial").value;
		setElementValue("nrDoctoServico",nrDoctoServico);
		setElementValue("tpDoctoServico", tpDoctoServico);
		setElementValue("sgFilialDoctoServico",sgFilialDoctoServico);
		setElementValue("idFilial",idFilial);
		setDisabled("descontos", false);
		
	}else{
		setDisabled("descontos", true);
	}	
}


</script>