<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterGruposRegioes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 		id="list" 	src="/tabelaPrecos/manterGruposRegioes" cmd="list"/>
		<adsm:tab title="detalhamento" 	id="cad" 	src="/tabelaPrecos/manterGruposRegioes" cmd="cad"/>
		<adsm:tab title="municipios" 	id="mun" 	src="/tabelaPrecos/manterGruposRegioes" cmd="mun"/>
	</adsm:tabGroup>
</adsm:window>
