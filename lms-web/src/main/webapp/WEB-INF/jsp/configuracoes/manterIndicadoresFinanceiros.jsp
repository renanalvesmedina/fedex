<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterIndicadoresFinanceiros" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterIndicadoresFinanceiros" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterIndicadoresFinanceiros" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
