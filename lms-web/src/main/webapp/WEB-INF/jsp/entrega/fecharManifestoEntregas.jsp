<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="fecharManifestoEntregas" type="main"> 
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="manifestosPendentesFechamento" id="proc" src="/entrega/fecharManifestoEntregas" cmd="proc" boxWidth="70"/>
		<adsm:tab title="pendencias" id="pendencias" src="/entrega/fecharManifestoEntregas" cmd="pendencias" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
