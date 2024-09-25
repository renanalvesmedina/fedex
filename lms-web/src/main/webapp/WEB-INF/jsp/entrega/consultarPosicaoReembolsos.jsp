<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPosicaoReembolsos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="consulta" id="pesq" src="entrega/consultarPosicaoReembolsos" cmd="list" onShow="tabShow"/>
			<adsm:tab title="detalhamento" id="cad" src="entrega/consultarPosicaoReembolsos" cmd="cad" disabled="true"/>
		</adsm:tabGroup>
	<adsm:i18nLabels>
        <adsm:include key="LMS-09084"/>    
    </adsm:i18nLabels>
		
</adsm:window>
