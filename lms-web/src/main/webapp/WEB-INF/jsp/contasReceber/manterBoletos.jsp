<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterBoletos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterBoletos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterBoletos" cmd="cad"/>
		<adsm:tab title="anexos" id="anexo" src="/contasReceber/manterBoletos" cmd="anexo" masterTabId="cad" copyMasterTabProperties="true" />
	</adsm:tabGroup>
</adsm:window>
