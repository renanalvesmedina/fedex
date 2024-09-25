<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="vincularMunicipios" type="main">
  	<adsm:tabGroup selectedTab="0">  	
  		<adsm:tab title="listagem" id="pesq" src="/integracao/manterMunicipiosVinculo" cmd="list"/>
 	 	<adsm:tab title="detalhamento" id="cad" src="/integracao/manterMunicipiosVinculo" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>