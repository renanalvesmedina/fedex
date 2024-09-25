<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="simularNovosPrecos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/simularNovosPrecos" cmd="list"/>
		<adsm:tab title="reajuste" id="reaj" src="/tabelaPrecos/simularNovosPrecos" cmd="reaj"/>
		<adsm:tab title="taxas" id="taxas" src="/tabelaPrecos/simularNovosPrecos" cmd="taxas" onHide="hide"/>
		<adsm:tab title="generalidades" id="gen" src="/tabelaPrecos/simularNovosPrecos" cmd="gen" onHide="hide"/>
	</adsm:tabGroup>
</adsm:window>
