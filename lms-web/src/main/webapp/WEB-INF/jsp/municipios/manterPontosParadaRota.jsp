<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPontosParada" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterPontosParadaRota" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterPontosParadaRota" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>