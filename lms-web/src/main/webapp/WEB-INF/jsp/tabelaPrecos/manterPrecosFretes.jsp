<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="precificarFreteTitulo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterPrecosFretes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/manterPrecosFretes" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
