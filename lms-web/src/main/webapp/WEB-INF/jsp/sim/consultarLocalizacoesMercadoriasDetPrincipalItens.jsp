<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction" onPageLoad="realizaPaginacao">
		<adsm:section caption="itensNotaFiscal"/>
		<adsm:grid property="itemNfCto" idProperty="idItemNfCto" scrollBars="vertical" onRowClick="returnFalse" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedItemNFC" showGotoBox="false" showPagging="false" selectionMode="none" unique="true" gridHeight="360">
			<adsm:gridColumn width="70" title="codigo" property="cdItemNfCto" dataType="integer"/>
			<adsm:gridColumn width="150" title="descricao" property="dsItemNfCto" dataType="text"/>
			<adsm:gridColumn width="70" title="quantidade" property="qtVolumes" dataType="integer"/>
			<adsm:gridColumn width="150" title="tipoEmbalagem" property="dsEmbalagem" dataType="text"/>
			
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valor" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn width="100" title="" property="vlUnitario" dataType="currency"/>
			
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="total" property="sgMoeda2" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo2" dataType="text" width="0" />
			</adsm:gridColumnGroup>			
			<adsm:gridColumn width="100" title="" property="total" dataType="currency"/>
			
			<adsm:buttonBar freeLayout="false">
				<adsm:button caption="fechar" onclick="self.close()" disabled="false"/>
			</adsm:buttonBar>
		</adsm:grid>
		<adsm:form action="/sim/consultarLocalizacoesMercadorias">
			<adsm:hidden property="idNotaFiscalConhecimento"/>
		</adsm:form>
	
</adsm:window>  
<script>
function realizaPaginacao(){
	onPageLoad();
	setMasterLink(this.document, true);
	var data = new Array();
	setNestedBeanPropertyValue(data,"idNotaFiscalConhecimento",getElementValue("idNotaFiscalConhecimento"));
	itemNfCtoGridDef.executeSearch(data);
}

function returnFalse(){
	return false;
}
</script> 