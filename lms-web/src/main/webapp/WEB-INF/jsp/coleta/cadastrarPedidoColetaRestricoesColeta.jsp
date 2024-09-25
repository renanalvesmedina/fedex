<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
function carregaPagina() {
	onPageLoad();
	carregaGrid();
}
</script>
<adsm:window service="lms.coleta.cadastrarPedidoColetaAction" onPageLoad="carregaPagina">
	<adsm:form action="/coleta/cadastrarPedidoColeta">
	<adsm:section caption="produtosProibidos" />
	<adsm:grid idProperty="idServico" property="servico" selectionMode="none" gridHeight="400"
			   rows="17" autoSearch="false" onRowClick="rowClickNone" unique="true"
			   service="lms.coleta.cadastrarPedidoColetaAction.findPaginatedRestricaoColeta"
			   rowCountService="lms.coleta.cadastrarPedidoColetaAction.getRowCountRestricaoColeta"
   			   defaultOrder="servico_.dsServico:asc">
		<adsm:gridColumn title="servico" property="servico.sgServico" width="60" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="145" />
		<adsm:gridColumn title="pesoMaximoPorVolume" dataType="decimal" mask="###,###,##0.000" property="psMaximoVolume" width="99" align="right" unit="kg" />
		<adsm:gridColumn title="produtoProibido" property="produtoProibido.dsProduto" />
	</adsm:grid>
	</adsm:form>
	
	<adsm:buttonBar>
		<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>
	</adsm:buttonBar>
	
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