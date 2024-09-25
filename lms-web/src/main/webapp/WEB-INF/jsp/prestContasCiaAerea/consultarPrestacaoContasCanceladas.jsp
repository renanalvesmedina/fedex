<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.consultarPrestacaoContasAction">
	<adsm:form action="/prestContasCiaAerea/consultarPrestacaoContas">

		<adsm:hidden property="prestacaoConta.idPrestacaoConta" />

	<adsm:grid idProperty="idAwbCancelado" property="awbsCanceladas" showPagging="false" defaultOrder="nrAwbCancelado" autoSearch="false"
		gridHeight="260" rows="7" unique="false" selectionMode="none" onRowClick="returnFalse();" scrollBars="vertical"
		service="lms.prestcontasciaaerea.consultarPrestacaoContasAction.findPaginatedAwbsCanceladas"
		rowCountService="lms.prestcontasciaaerea.consultarPrestacaoContasAction.getRowCountAwbsCanceladas">
		<adsm:gridColumn title="numero" property="nrAwbCancelado" align="right"/>
	</adsm:grid>
	</adsm:form>
</adsm:window>
<script>
	function returnFalse(){
		return false;
	}

	function myOnShow(){
	
		awbsCanceladasGridDef.resetGrid();
		var filtro = {
			prestacaoConta : { idPrestacaoConta	: getElementValue("prestacaoConta.idPrestacaoConta")}
		};
		awbsCanceladasGridDef.executeSearch(filtro);

	}

</script>