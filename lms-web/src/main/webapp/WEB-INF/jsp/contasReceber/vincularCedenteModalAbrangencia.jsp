<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="vincularCedenteModalAbrangencia" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/vincularCedenteModalAbrangencia" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/vincularCedenteModalAbrangencia" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>