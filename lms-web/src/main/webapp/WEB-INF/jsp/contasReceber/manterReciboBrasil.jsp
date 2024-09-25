<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterReciboBrasil" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterReciboBrasil" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterReciboBrasil" cmd="cad"/>
		<adsm:tab title="documentos" id="faturas" src="/contasReceber/manterReciboBrasil" cmd="faturas"
		masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>