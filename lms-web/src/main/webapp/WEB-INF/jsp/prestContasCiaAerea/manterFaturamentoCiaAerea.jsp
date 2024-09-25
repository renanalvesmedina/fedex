<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="faturamentoCiaAerea" type="main">

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/prestContasCiaAerea/faturamentoCiaAerea" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/prestContasCiaAerea/faturamentoCiaAerea" cmd="cad"/>			
	</adsm:tabGroup>
	
</adsm:window>