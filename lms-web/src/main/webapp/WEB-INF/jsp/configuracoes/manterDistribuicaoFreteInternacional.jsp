<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window title="manterDistribuicaoFreteInternacional" type="main">

	<adsm:i18nLabels>
		<adsm:include key="LMS-00044"/>
	</adsm:i18nLabels>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterDistribuicaoFreteInternacional" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterDistribuicaoFreteInternacional" cmd="cad"/>
		<adsm:tab title="tramos" id="tramos" masterTabId="cad" copyMasterTabProperties="true" 
			src="/configuracoes/manterDistribuicaoFreteInternacional" cmd="tramos" onShow="myOnShow();"/>
	</adsm:tabGroup>
</adsm:window>
