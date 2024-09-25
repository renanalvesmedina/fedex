<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRegionalFilial" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterRegionalFilial" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterRegionalFilial" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  