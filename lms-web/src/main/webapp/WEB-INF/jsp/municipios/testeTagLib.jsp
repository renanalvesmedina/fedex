<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFeriados" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/testeTagLib" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/testeTagLib" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>

