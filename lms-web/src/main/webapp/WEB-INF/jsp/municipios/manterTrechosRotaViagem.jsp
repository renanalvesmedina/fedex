<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTrechosRotaViagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterTrechosRotaViagem" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterTrechosRotaViagem" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  