<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="dadosCarregamento" >
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="carregamento" id="list" src="/carregamento/dadosCarregamento" cmd="list"/>
		<adsm:tab title="fotos" id="fotos" src="/carregamento/dadosCarregamento" cmd="fotos"/>
		<adsm:tab title="integrantes" id="integrantes" src="/carregamento/dadosCarregamento" cmd="integrantes"/>
	</adsm:tabGroup>
</adsm:window>
