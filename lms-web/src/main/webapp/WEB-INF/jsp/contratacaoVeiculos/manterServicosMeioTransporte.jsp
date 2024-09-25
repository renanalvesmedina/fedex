<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicosMeioTransporte" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterServicosMeioTransporte" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterServicosMeioTransporte" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>