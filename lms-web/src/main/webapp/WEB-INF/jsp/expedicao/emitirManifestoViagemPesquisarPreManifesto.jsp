<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoad="myOnPageLoad">
	<adsm:grid
		property="manifesto" 
		idProperty="idManifesto"
		service="lms.expedicao.emitirManifestoViagemAction.findGridPreManifestoInit"
		gridHeight="350"
		showPagging="false"
		showGotoBox="false"
		scrollBars="vertical">
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="preManifesto" property="filialByIdFilialOrigem.sgFilial" width="150"/>
			<adsm:gridColumn title="" property="nrPreManifesto" width="150" dataType="integer" mask="000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="filialDestino2" property="filialByIdFilialDestino.sgFilial"/>

		<adsm:buttonBar>
			<adsm:button 
				caption="fechar"
				onclick="self.close();"/>
		</adsm:buttonBar>
	</adsm:grid>
	<adsm:form action="/expedicao/emitirManifestoViagem">
    	<adsm:hidden property="idFilial"/>
    	<adsm:hidden property="nrPreManifesto"/>
    	<adsm:hidden property="tpManifesto" value="V"/>
    	<adsm:hidden property="tpStatusManifesto" value="PM"/>
    	<adsm:hidden property="tpAbrangencia" value="N"/>    	
    </adsm:form>
</adsm:window>
<script>
	function myOnPageLoad(){
	    onPageLoad();
		populaGrid();
	}

	function populaGrid() {
		var filter = {};
		filter.idFilial = getElementValue("idFilial");
		filter.tpManifesto = getElementValue("tpManifesto");
		filter.tpStatusManifesto = getElementValue("tpStatusManifesto");
		filter.tpAbrangencia = getElementValue("tpAbrangencia"); 
		manifestoGridDef.executeSearch(filter, true);
	}
</script>