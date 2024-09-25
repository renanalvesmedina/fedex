<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function carregaNotasPageLoad() {
	setMasterLink(document, true);
	carregaGridNotasFiscaisColeta();
}
</script>

<adsm:window title="notasFiscais" service="lms.coleta.notaFiscalColetaService" onPageLoad="carregaNotasPageLoad">
	<adsm:form action="/coleta/cadastrarPedidoColeta">

		<adsm:hidden property="idDetalheColeta" />

		<adsm:grid property="notasFiscaisColeta" onRowClick="disableRowClick"
				   idProperty="idNotaFiscalColeta" 
				   defaultOrder="nrNotaFiscal" 
				   selectionMode="none"
				   rows="6" gridHeight="155" width="373" showPagging="true" showTotalPageCount="false"
				   showGotoBox="false">
			<adsm:gridColumn title="notaFiscal" property="nrNotaFiscal"  />
		</adsm:grid>
		<adsm:buttonBar/> 
		
		<script>
			var branco = '<adsm:label key="branco"/>';
			var salvarDetalheNotaFiscal = '<adsm:label key="salvarDetalheNotaFiscal"/>';
		</script>
		
	</adsm:form>
	<adsm:buttonBar>
		<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>
	</adsm:buttonBar>
</adsm:window>
<script>
/**
 * Função que ira fazer a chamada do findPaginated de notaFiscalColeta
 * baseado no idDetalheColeta.
 */
function carregaGridNotasFiscaisColeta() {
	var idDetalheColeta = getElementValue("idDetalheColeta");
	if(idDetalheColeta > 0) {
		var filtro = new Array();
		// Monta um map com o campo para sser realizado o filtro
		setNestedBeanPropertyValue(filtro, "detalheColeta.idDetalheColeta", idDetalheColeta);
		//chama a pesquisa da grid 
		notasFiscaisColetaGridDef.executeSearch(filtro);
	} else {
		alert(salvarDetalheNotaFiscal);
	}
}

/**
 * Função para não chamar nada no onclick da row da grid
 */
function disableRowClick() {
	return false;
}
</script>