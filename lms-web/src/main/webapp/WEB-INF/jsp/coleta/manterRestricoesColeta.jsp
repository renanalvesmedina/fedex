<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRestricoesColeta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/coleta/manterRestricoesColeta" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/coleta/manterRestricoesColeta" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
