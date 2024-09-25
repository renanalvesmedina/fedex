<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterParcelasFreteColetaEntrega" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterParcelasFreteColetaEntrega" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterParcelasFreteColetaEntrega" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>