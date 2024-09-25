<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterAeroportos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterAeroportos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterAeroportos" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>