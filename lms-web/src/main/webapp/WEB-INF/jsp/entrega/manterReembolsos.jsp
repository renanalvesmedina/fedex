<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterReembolsos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="entrega/manterReembolsos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="entrega/manterReembolsos" cmd="cad" disabled="true"/>
			<adsm:tab title="cheques" id="cheq" src="entrega/manterReembolsos" cmd="cheq" disabled="true" onShow="myOnShow" masterTabId="cad" copyMasterTabProperties="false"/>
		</adsm:tabGroup>
</adsm:window>
