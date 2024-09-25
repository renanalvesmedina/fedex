<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="registrarRecebimentosEntregaDocumento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/registrarRecebimentosEntregaDocumento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/registrarRecebimentosEntregaDocumento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
