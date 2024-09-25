<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPessoas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" onShow="onShowAba" id="pesq" src="/configuracoes/manterPessoas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterPessoas" cmd="cad" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
