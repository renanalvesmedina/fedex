<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="inclusaoCobrancaInadimplentes" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/inclusaoCobrancaInadimplentes" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/inclusaoCobrancaInadimplentes" cmd="cad" onShow="myOnShow"/>
            <adsm:tab title="faturas" id="item" src="/contasReceber/inclusaoCobrancaInadimplentes" cmd="item" 
            	copyMasterTabProperties="true" disabled="false" masterTabId="cad"/>
            <adsm:tab title="tratativa" id="trat" src="/contasReceber/inclusaoCobrancaInadimplentes" cmd="trat" 
            	copyMasterTabProperties="true" disabled="false" masterTabId="cad"/>
		</adsm:tabGroup>
</adsm:window>
