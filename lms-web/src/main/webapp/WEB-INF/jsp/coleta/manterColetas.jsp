<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterColetas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/coleta/manterColetas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/coleta/manterColetas" cmd="cad" disabled="true" />
		<adsm:tab title="detalheColeta" id="detalheColeta" src="/coleta/manterColetas" cmd="detalheColeta" disabled="true" boxWidth="130" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
