<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

function carregaAwbsPageLoad_cb() {
	setMasterLink(document, true);
	carregaGridAwbsColeta();
}
</script>

<adsm:window title="awbs" service="lms.coleta.awbColetaService" onPageLoadCallBack="carregaAwbsPageLoad"> 
	<adsm:form action="/coleta/cadastrarPedidoColeta">
	
	<adsm:hidden property="idDetalheColeta" />
	
	<adsm:grid property="awbsColeta" onRowClick="disableRowClick"
			   idProperty="idAwbColeta" selectionMode="none" 
			   defaultOrder="awb_.nrAwb" 
			   rows="6"	
			   gridHeight="155" width="373" showTotalPageCount="false"
			   showPagging="true"			   showGotoBox="false">
			<adsm:gridColumn title="ciaAerea" property="awb.ciaFilialMercurio.empresa.pessoa.nmPessoa"/>   
			<adsm:gridColumn title="awb" property="awb.nrAwb"/>
		</adsm:grid>
	</adsm:form>
	<adsm:buttonBar>
		<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>
	</adsm:buttonBar>
</adsm:window>
<script>
/**
 * Função que ira fazer a chamada do findPaginated de awbColeta
 * baseado no idDetalheColeta.
 */
function carregaGridAwbsColeta() {
	var idDetalheColeta = getElementValue("idDetalheColeta");
	
	var filtro = new Array();
	// Monta um map com o campo para ser realizado o filtro
	setNestedBeanPropertyValue(filtro, "detalheColeta.idDetalheColeta", idDetalheColeta);
	//chama a pesquisa da grid 
	awbsColetaGridDef.executeSearch(filtro);
}

/**
 * Função para não chamar nada no onclick da row da grid
 */
function disableRowClick() {
	return false;
}
</script>
