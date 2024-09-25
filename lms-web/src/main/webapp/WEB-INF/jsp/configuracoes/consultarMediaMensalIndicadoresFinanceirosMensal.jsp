<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.mediaIndicadoreFinanceiroMoedaService">
	<adsm:form action="/configuracoes/consultarMediaMensalIndicadoresFinanceiros">
	<adsm:grid onRowClick="myOnRowClick" service="lms.configuracoes.mediaIndicadoreFinanceiroMoedaService.findMensalPaginated" idProperty="mes" showPagging="false" property="mediaMensal" rows="12" selectionMode="none" showGotoBox="false" showTotalPageCount="false" gridHeight="200">
		<adsm:gridColumn width="30%" title="mes" property="mesDominio"/>
		<adsm:gridColumn width="25%" dataType="decimal" mask="###,###,###,###,##0.0000" title="media" property="media"/>
		<adsm:gridColumn width="25%" dataType="decimal" mask="###,###,###,###,##0.0000" title="percentualVariacao" property="percentual" />
		<adsm:gridColumn width="20%" title="diasMes" property="nrDia" align="right"/>
	</adsm:grid>
	</adsm:form>
</adsm:window>
<script>
	function myOnRowClick(pkValue){
		//ref para tabgroup    	
		var tabGroup = getTabGroup(this.document);
		//ref para tab diario
		var tabDiario = tabGroup.getTab("cad");
		
		tabDiario.setDisabled(false);
	}
</script>