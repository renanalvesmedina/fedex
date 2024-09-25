<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterDescontos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterDescontos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterDescontos" cmd="cad"/>
			<adsm:tab title="anexos" id="anexo" src="/contasReceber/manterDescontos" cmd="anexo" masterTabId="cad" copyMasterTabProperties="true" />
		</adsm:tabGroup>
</adsm:window>
