<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPCEs" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterVersoesPCE" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterVersoesPCE" cmd="cad"/>
		<adsm:tab title="descritivos" id="descritivos" src="/vendas/manterVersoesPCE" cmd="descritivos" onShow="onShowTabDescritivos" copyMasterTabProperties="true" masterTabId="cad" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
