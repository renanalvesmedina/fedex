<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProcedimentosFaixasValores" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterProcedimentosFaixasValores" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterProcedimentosFaixasValores" cmd="cad"/>
		<adsm:tab title="exigencias" id="exigencias" src="/sgr/manterProcedimentosFaixasValores" cmd="exigencias" disabled="true" masterTabId="cad"/>
		<adsm:tab title="naturezasImpedidas" id="naturezas" src="/sgr/manterProcedimentosFaixasValores" cmd="naturezas" disabled="true" masterTabId="cad"/>
	</adsm:tabGroup>
</adsm:window>
