<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSubtiposTabelaPreco" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterSubtiposTabelaPreco" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/manterSubtiposTabelaPreco" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>