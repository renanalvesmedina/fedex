<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="emitirValoresINSSCompet" type="main">
	<adsm:i18nLabels>
       <adsm:include key="visualizarPorCPF"/>
       <adsm:include key="visualizar"/>
    </adsm:i18nLabels>
	
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="relatorio" id="pesq" src="/tributos/emitirValoresINSSCompet" cmd="pesq"/>
	</adsm:tabGroup>
</adsm:window>
