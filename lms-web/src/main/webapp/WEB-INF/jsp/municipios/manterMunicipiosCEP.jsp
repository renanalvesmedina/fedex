<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMunicipiosCEP" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterMunicipiosCEP" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterMunicipiosCEP" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>