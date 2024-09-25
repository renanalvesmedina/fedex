<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterValoresCombustivel" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroViagem/manterValoresCombustivel" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroViagem/manterValoresCombustivel" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
