<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposCombustivel" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroViagem/manterTiposCombustivel" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroViagem/manterTiposCombustivel" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
