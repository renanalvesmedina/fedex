<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterParametros" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 	   id="pesq"    src="/vol/manterParametros" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"		src="/vol/manterParametros" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
