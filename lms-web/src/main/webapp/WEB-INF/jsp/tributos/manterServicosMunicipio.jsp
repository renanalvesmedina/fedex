<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicosMunicipio" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterServicosMunicipio" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterServicosMunicipio" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
