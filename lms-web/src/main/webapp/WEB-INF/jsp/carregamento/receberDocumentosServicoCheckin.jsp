<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="receberDocumentosServicoCheckin" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/receberDocumentosServicoCheckin" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/receberDocumentosServicoCheckin" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
