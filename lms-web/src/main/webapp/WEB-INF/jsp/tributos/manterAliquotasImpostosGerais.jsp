<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="manterAliquotasImpostosGerais" type="main">
		
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/tributos/manterAliquotasImpostosGerais" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/tributos/manterAliquotasImpostosGerais" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>