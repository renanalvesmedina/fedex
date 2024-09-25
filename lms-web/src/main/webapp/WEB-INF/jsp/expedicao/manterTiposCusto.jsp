<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposCusto" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterTiposCusto" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterTiposCusto" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
