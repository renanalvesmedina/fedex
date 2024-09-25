<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.mediaIndicadoreFinanceiroMoedaService">
	<adsm:form action="/configuracoes/consultarMediaMensalIndicadoresFinanceiros" idProperty="mes" onDataLoadCallBack="myOnDataLoad">
	<%--adsm:grid idProperty="" gridHeight="200" scrollBars="vertical" rows="31" showPaging="false">
		<adsm:gridColumn width="50%" title="dia" property="dia"/>
		<adsm:gridColumn width="50%" title="cotacao" property="cotacao"/>
	</adsm:grid--%>	
	<adsm:grid service="lms.configuracoes.mediaIndicadoreFinanceiroMoedaService.findDiarioPaginated" idProperty="id" property="mediaDiario" showPagging="false" 
	selectionMode="none" showGotoBox="false" gridHeight="240" showTotalPageCount="false" scrollBars="vertical" onRowClick="funcaoVazil">
		<adsm:gridColumn width="50%" title="dia" dataType="integer" align="right" property="day"/>
		<adsm:gridColumn width="50%" title="cotacao" dataType="decimal" mask="###,###,###,###,##0.0000" property="media"/>
	</adsm:grid>	
	</adsm:form>
</adsm:window>
<script>

function myOnShow(){
	return false;
}

function funcaoVazil(){
	return false;
}

function myOnDataLoad_cb(data,erro){
	onDataLoad_cb(data,erro);
	var tabGroup = getTabGroup(this.document);
	var tabMensal = tabGroup.getTab("mensal");
	
	var tela = tabMensal.tabOwnerFrame;
	
	var filtros = tela.mediaMensalGridDef.gridState.filters;
	
	var filtrosDiario = new Object();
	filtrosDiario.idPais = filtros.idPais;
	filtrosDiario.idMoeda = filtros.idMoeda;
	filtrosDiario.idIndicadorFinanceiro = filtros.idIndicadorFinanceiro;	
	filtrosDiario.ano = filtros.ano;
	filtrosDiario.mes = getElementValue("mes");	
		
	mediaDiarioGridDef.executeSearch(filtrosDiario);	
}
</script>
