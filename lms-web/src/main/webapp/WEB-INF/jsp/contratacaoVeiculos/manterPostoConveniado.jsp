<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPostoConveniado" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"		id="pesq"	src="/contratacaoVeiculos/manterPostoConveniado" cmd="list"/>
		<adsm:tab title="detalhamento" 	id="cad"	src="/contratacaoVeiculos/manterPostoConveniado" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
