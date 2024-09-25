<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="totaisPorMotivo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 	   id="pesq"    src="/vol/metricasTotaisPorMotivo" cmd="list" />
		<!--adsm:tab title="detalhamento" id="det"		src="/vol/metricasTotaisPorMotivo" cmd="det" disabled="true"/-->
	</adsm:tabGroup>
</adsm:window>