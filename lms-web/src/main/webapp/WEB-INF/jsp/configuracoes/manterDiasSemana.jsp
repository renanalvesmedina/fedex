<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDiasSemana" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterDiasSemana" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterDiasSemana" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
