<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.consultarPrestacaoContasAction">

	<adsm:grid 
			property="intervalosAWB" 
			idProperty="idIntervaloAwb" defaultOrder="nrIntervaloInicial"
			gridHeight="260" showPagging="false"
			selectionMode="none" scrollBars="vertical"
			unique="false" onRowClick="returnFalse();" autoSearch="false"
			service="lms.prestcontasciaaerea.consultarPrestacaoContasAction.findPaginatedIntervalosAwb"
			rowCountService="lms.prestcontasciaaerea.consultarPrestacaoContasAction.getRowCountIntervalosAwb">
			
		<adsm:gridColumn title="de" property="nrIntervaloInicial" dataType="integer"/>
		<adsm:gridColumn title="ateTitulo" property="nrIntervaloFinal" dataType="integer"/>
		<adsm:gridColumn title="quantidade" property="qtIntervalo" dataType="integer"/>
		
	</adsm:grid>
	
</adsm:window>
<script>
	function returnFalse(){
		return false;
	}
</script>