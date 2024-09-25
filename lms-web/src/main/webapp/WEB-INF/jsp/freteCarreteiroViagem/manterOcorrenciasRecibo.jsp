<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrenciasRecibo" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroViagem/manterOcorrenciasRecibo" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroViagem/manterOcorrenciasRecibo" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
