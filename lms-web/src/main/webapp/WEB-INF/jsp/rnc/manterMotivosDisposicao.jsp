<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMotivosDisposicao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/manterMotivosDisposicao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/manterMotivosDisposicao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
