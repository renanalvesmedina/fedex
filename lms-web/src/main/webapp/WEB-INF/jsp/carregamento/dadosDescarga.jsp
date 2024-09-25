<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="dadosDescarga" >
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="descarga" id="list" src="/carregamento/dadosDescarga" cmd="list"/>
		<adsm:tab title="fotos" id="fotos" src="/carregamento/dadosDescarga" cmd="fotos"/>
		<adsm:tab title="integrantes" id="integrantes" src="/carregamento/dadosDescarga" cmd="integrantes"/>
	</adsm:tabGroup>
</adsm:window>
