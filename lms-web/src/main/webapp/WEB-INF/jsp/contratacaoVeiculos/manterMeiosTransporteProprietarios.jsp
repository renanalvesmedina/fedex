<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMeiosTransporteProprietarios" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterMeiosTransporteProprietarios" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterMeiosTransporteProprietarios" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
