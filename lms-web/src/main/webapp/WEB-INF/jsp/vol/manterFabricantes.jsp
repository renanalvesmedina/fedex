<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterFabricantes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 	   id="pesq"    src="/vol/manterFabricantes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"		src="/vol/manterFabricantes" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
