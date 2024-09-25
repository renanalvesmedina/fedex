<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirManifesto" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="manifestos" id="pesq" src="/entrega/emitirManifesto" cmd="pesq" boxWidth="100"/>
		<adsm:tab title="emissaoManifesto" id="cad" src="/entrega/emitirManifesto" cmd="cad" disabled="true" boxWidth="150"/>
	</adsm:tabGroup>
</adsm:window>