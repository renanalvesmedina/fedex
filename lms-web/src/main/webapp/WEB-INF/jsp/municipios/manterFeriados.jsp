<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFeriados" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterFeriados" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterFeriados" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>

