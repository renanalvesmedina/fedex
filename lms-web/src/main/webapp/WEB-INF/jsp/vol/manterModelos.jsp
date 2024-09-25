<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterModelos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 	   id="pesq"    src="/vol/manterModelos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"		src="/vol/manterModelos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
