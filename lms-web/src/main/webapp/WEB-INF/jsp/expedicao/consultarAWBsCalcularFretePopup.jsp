<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.expedicao.consultarAWBsCalcularFreteAction"
	onPageLoadCallBack="myOnPageLoad"
	title="branco">
	
	<adsm:form 
		action="/expedicao/consultarAWBsCalcularFrete">
		
		<adsm:section 
			caption="calculoFreteTitulo" 
			width="55%" />
		
	</adsm:form>
	
	<adsm:grid 
		property="parcelas"
		idProperty="idParcelaPreco"
		selectionMode="none" 
		gridHeight="80" 
		unique="true" 
		showPagging="false" 
		width="380" 
		scrollBars="vertical"
		onRowClick="parcelasRowClick">
		
		<adsm:gridColumn 
			property="nmParcelaPreco" 
			title="parcela" 
			width="230" />
			
		<adsm:gridColumn
			dataType="currency" 
			property="vlParcelaPreco" 
			title="valor" 
			width="150" 
			unit="reais" 
			align="right" />
			
	</adsm:grid>
	
	<adsm:form id="bottomForm" action="/expedicao/consultarAWBsCalcularFrete">
	
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox 
			label="aliquotaIcms" 
			property="pcAliquotaICMS" 
			dataType="currency" 
			size="8" 
			labelWidth="12%" 
			width="11%" 
			disabled="true"/>

		<adsm:textbox 
			label="vlIcms" 
			property="vlICMS" 
			dataType="currency" 
			size="15" 
			labelWidth="13%" 
			width="50%" 
			disabled="true"/>
			
		<adsm:label key="branco" width="24%"/>

		<adsm:textbox 
			label="totalFreteReais"
			property="vlFrete" 
			dataType="currency" 
			size="15" 
			labelWidth="13%" 
			width="30%" 
			style="align:right"
			disabled="true"/>
				
		<adsm:buttonBar>
		
			<adsm:button 
				id="btnFechar"
				caption="fechar"
				onclick="self.close();" />	
				
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnFechar", false);
	populaGrid();
	setElementValue("vlFrete", setFormat("vlFrete", dialogArguments.getElementValue("vlFrete")));
	setElementValue("pcAliquotaICMS", setFormat("pcAliquotaICMS", dialogArguments.getElementValue("pcAliquotaICMS")));
	setElementValue("vlICMS", setFormat("vlICMS", dialogArguments.getElementValue("vlICMS")));
	setFocus("btnFechar", false);
}

/*****************************/
/* ONROWCLICK DIMENSOES GRID */
/*****************************/
function parcelasRowClick(valor) {
	return false;
}

function populaGrid() {
	var vlFretePeso = dialogArguments.getElementValue("vlFretePeso");
	var vlTaxaTerrestre = dialogArguments.getElementValue("vlTaxaTerrestre");
	var vlTaxaCombustivel = dialogArguments.getElementValue("vlTaxaCombustivel");
	
	var dados = {
		vlFretePeso : vlFretePeso,
		vlTaxaTerrestre : vlTaxaTerrestre,
		vlTaxaCombustivel : vlTaxaCombustivel
	}
	
	parcelasGridDef.executeSearch(dados, true);
}
-->
</script>