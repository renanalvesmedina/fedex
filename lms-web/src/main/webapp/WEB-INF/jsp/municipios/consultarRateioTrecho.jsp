<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRateioTrecho">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/consultarRateioTrecho" cmd="list" height="410" />
			<adsm:tab title="detalhamento" id="det" src="/municipios/consultarRateioTrecho" cmd="det" height="410" />
		</adsm:tabGroup>
</adsm:window>