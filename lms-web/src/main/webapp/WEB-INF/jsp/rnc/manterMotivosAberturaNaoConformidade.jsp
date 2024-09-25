<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMotivosAberturaNaoConformidade" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/manterMotivosAberturaNaoConformidade" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/manterMotivosAberturaNaoConformidade" cmd="cad"/>
		<adsm:tab title="descricoesPadrao" id="descricoesPadrao" src="/rnc/manterMotivosAberturaNaoConformidade" cmd="descricoesPadrao"
			disabled="true" masterTabId="cad" />
	</adsm:tabGroup>
</adsm:window>
