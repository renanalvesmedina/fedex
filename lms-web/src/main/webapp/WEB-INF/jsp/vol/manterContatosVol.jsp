<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterContatosRecusa" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq"	src="/vol/manterContatosVol" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"	src="/vol/manterContatosVol" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>