<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterValoresDominiosIntegracao" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/integracao/manterValoresDominiosIntegracao" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/integracao/manterValoresDominiosIntegracao" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>