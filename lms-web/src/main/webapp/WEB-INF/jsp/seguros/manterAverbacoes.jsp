<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAverbacoes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/seguros/manterAverbacoes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterAverbacoes"  cmd="cad"/>
		<adsm:tab title="anexos"       id="anexos" src="/seguros/manterAverbacoes"  cmd="anexos"  masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>