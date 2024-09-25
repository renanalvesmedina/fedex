<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirControleCargasViagem" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="emitirControleCargas" id="pesq" src="/carregamento/emitirControleCargas" cmd="pesq"/>
		
		<adsm:tab title="riscosTitulo" id="riscos" src="/carregamento/emitirControleCargas" cmd="riscos"/>
	</adsm:tabGroup>
</adsm:window>
