<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
function carregaPagina() {
	onPageLoad();
	carregaGrid();
}
</script>
<adsm:window service="lms.coleta.manterColetasAction" onPageLoad="carregaPagina">
	<adsm:grid idProperty="idServico" property="servico" selectionMode="none" gridHeight="315"
			   rows="15" scrollBars="horizontal" autoSearch="false" onRowClick="rowClickNone"
			   service="lms.coleta.manterColetasAction.findPaginatedRestricaoColeta"
			   rowCountService="lms.coleta.manterColetasAction.getRowCountRestricaoColeta"
   			   defaultOrder="servico_.dsServico:asc">
		<adsm:gridColumn title="servico" property="servico.dsServico" width="220" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="145" />
		<adsm:gridColumn title="pesoMaximoPorVolume" property="psMaximoVolume" width="99" align="right" unit="kg" />
		<adsm:gridColumn title="produtoProibido" property="produtoProibido.dsProduto" width="400" />
	</adsm:grid>
	
	<adsm:buttonBar>	
	</adsm:buttonBar>
	
	<adsm:form action="/coleta/manterColetas">
	</adsm:form>
	
</adsm:window>

<script type="text/javascript">
function carregaGrid() {
	var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
	servico_cb(fb);
}

function rowClickNone() {
	return false;
}

</script>