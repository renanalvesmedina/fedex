<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterZonasServicos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterZonasServicos" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterZonasServicos" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>