<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMilkRun" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/coleta/manterMilkRun" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/coleta/manterMilkRun" cmd="cad"/>
		<adsm:tab title="remetentes" id="remetentes" src="/coleta/manterMilkRun" disabled="true" cmd="remetentes" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
