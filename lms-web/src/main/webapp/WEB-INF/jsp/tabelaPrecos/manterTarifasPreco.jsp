<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTarifasPreco" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterTarifasPreco" cmd="list"/>
		<adsm:tab title="detalhamento"  id="cad" src="/tabelaPrecos/manterTarifasPreco" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
