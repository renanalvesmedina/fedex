<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterCotacaoIndicadoresFinanceiros" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterCotacaoIndicadoresFinanceiros" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterCotacaoIndicadoresFinanceiros" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
