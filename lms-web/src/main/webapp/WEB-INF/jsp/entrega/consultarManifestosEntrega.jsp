<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarManifestosEntrega" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="list" src="entrega/consultarManifestosEntrega" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="entrega/consultarManifestosEntrega" cmd="cad" disabled="true" />
			<adsm:tab title="documentosServicoTitulo" id="doc" src="entrega/consultarManifestosEntrega"
					onShow="tabShowCustom" cmd="doc" disabled="true" />
		</adsm:tabGroup>
</adsm:window>
