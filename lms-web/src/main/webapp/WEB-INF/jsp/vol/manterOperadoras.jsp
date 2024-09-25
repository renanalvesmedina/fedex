<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterOperadoras" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/vol/manterOperadoras" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"  src="/vol/manterOperadoras" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
