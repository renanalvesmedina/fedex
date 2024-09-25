<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterDominiosIntegracao" type="main">
		<adsm:tabGroup selectedTab="0">
		
			<adsm:tab title="listagem" id="pesq" src="/integracao/manterDominiosIntegracao" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/integracao/manterDominiosIntegracao" cmd="cad"/>
	
		</adsm:tabGroup>
</adsm:window>
