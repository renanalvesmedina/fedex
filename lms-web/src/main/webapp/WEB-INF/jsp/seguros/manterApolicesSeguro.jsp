<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterApolicesSeguro" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/manterApolicesSeguro" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterApolicesSeguro" cmd="cad"/>
		<adsm:tab title="parcelas" id="parcelas" src="/seguros/manterApolicesSeguro" cmd="parcelas" masterTabId="cad" copyMasterTabProperties="true"/>
		<adsm:tab title="anexos" id="anexos" src="/seguros/manterApolicesSeguro" cmd="anexos" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
