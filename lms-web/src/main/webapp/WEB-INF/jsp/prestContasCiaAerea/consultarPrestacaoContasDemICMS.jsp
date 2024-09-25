<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.consultarPrestacaoContasAction">

	<adsm:form action="/prestContasCiaAerea/consultarPrestacaoContas">

		<adsm:hidden property="prestacaoConta.idPrestacaoConta" />
	
		<adsm:grid property="demonstrativoICMS" idProperty="idIcmsPrestacao" gridHeight="260" rows="7" unique="false" autoSearch="false"
			defaultOrder="pcAliquota" selectionMode="none" onRowClick="returnFalse();" scrollBars="vertical" showPagging="false"
			service="lms.prestcontasciaaerea.consultarPrestacaoContasAction.findPaginatedIcmsPrestacao"
			rowCountService="lms.prestcontasciaaerea.consultarPrestacaoContasAction.getRowCountIcmsPrestacao">
			<adsm:gridColumn title="percentualAliquota" property="pcAliquota" dataType="currency"/>
			<adsm:gridColumn title="frete" property="vlFrete" dataType="currency"/>
			<adsm:gridColumn title="taxas" property="vlTaxa" dataType="currency"/>
			<adsm:gridColumn title="adValorem" property="vlAdvalorem" dataType="currency"/>
			<adsm:gridColumn title="icms" property="vlIcms" dataType="currency"/>
			<adsm:gridColumn title="total" property="vlTotal" dataType="currency"/>
		</adsm:grid>

	</adsm:form>

</adsm:window>

<script>

	function returnFalse(){
		return false;
	}
	
	function myOnShow(){
	
		demonstrativoICMSGridDef.resetGrid();
		var filtro = {
			prestacaoConta : { idPrestacaoConta	: getElementValue("prestacaoConta.idPrestacaoConta")}
		};
		demonstrativoICMSGridDef.executeSearch(filtro);

	}
</script>