<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterDescontosINSSOutrasEmpresas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterDescontosINSSOutrasEmpresas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterDescontosINSSOutrasEmpresas" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>