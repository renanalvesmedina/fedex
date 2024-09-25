<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEquipes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterEquipes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterEquipes" cmd="cad"/>
		<adsm:tab title="funcionarios" id="funcionarios" src="/carregamento/manterEquipes" cmd="funcionarios"  masterTabId="cad" copyMasterTabProperties="true"/>
		<adsm:tab title="terceiros" id="terceiros" src="/carregamento/manterEquipes" cmd="terceiros" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
