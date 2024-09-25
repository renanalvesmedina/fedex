<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMeiosTransporteRodoviarios" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterMeiosTransporteRodoviarios" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterMeiosTransporteRodoviarios" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>