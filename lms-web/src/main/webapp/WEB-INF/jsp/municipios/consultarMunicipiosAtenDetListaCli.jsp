<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function realizaPaginacao(){
	onPageLoad();
	setMasterLink(this.document, true);
	var idOperacaoServicoLocaliza = getElementValue("idOperacaoServicoLocaliza");
	if (idOperacaoServicoLocaliza != undefined && idOperacaoServicoLocaliza != ''){
		var dataId = new Array();
		setNestedBeanPropertyValue(dataId, "idOperacaoServicoLocaliza", idOperacaoServicoLocaliza);
		atendimentoClienteGridDef.executeSearch(dataId);
	} else
		atendimentoClienteGridDef.resetGrid();
}

</script>
<adsm:window service="lms.municipios.consultarMunicipiosAction" onPageLoad="realizaPaginacao">
	<adsm:form idProperty="idAtendimentoCliente" action="/municipios/consultarMunicipios" >
		<adsm:hidden property="idOperacaoServicoLocaliza"/>
	</adsm:form>
	<adsm:grid 
		idProperty="idAtendimentoCliente" 
		property="atendimentoCliente" 
		unique="true" 
		service="lms.municipios.consultarMunicipiosAction.findAtendimentosVigentesByIdServicoLocalizacao"  
		selectionMode="none" showPagging="false" gridHeight="100" onRowClick="desabilitaGrid">
		<adsm:gridColumn title="identificacao" property="tpIdentificacao" width="60" isDomain="true"/>
		<adsm:gridColumn title="" property="nrIdentificacaoFormatado" width="130" align="right"/>
		<adsm:gridColumn title="razaoSocial" property="nmPessoa"  />
		
		
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="self.close()" disabled="false"/>
	</adsm:buttonBar>
	
</adsm:window>
<script>
function desabilitaGrid(){
	return false;
}
</script>