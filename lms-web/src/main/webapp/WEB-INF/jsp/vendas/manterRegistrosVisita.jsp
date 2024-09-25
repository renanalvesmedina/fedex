<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRegistrosVisita" type="main">
  	<adsm:tabGroup selectedTab="0">
  	
  		<adsm:tab title="listagem" id="pesq" src="/vendas/manterRegistrosVisita" cmd="list"/>
 	 	<adsm:tab title="detalhamento" id="cad" src="/vendas/manterRegistrosVisita" cmd="cad"/>
		<adsm:tab title="etapasVisita" id="etapas" src="/vendas/manterRegistrosVisita" cmd="etapas" 
			copyMasterTabProperties="true" masterTabId="cad" disabled="true" boxWidth="120"/>
		
	</adsm:tabGroup>
</adsm:window>
