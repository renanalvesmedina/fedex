<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEmbasamentoLegalICMS" type="main">

	<adsm:i18nLabels>
		<adsm:include key="LMS-23001"/>
	</adsm:i18nLabels>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterEmbasamentoLegalICMS" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterEmbasamentoLegalICMS" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
