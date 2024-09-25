<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="gerencialTratativas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 	   id="pesq" src="/vol/gerencialTratativas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"	 src="/vol/gerencialTratativas" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
