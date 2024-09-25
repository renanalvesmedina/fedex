<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicosISSMunicipio" type="main">

	<adsm:i18nLabels>
		<adsm:include key="LMS-27044"/>
	</adsm:i18nLabels>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterServicosISSMunicipio" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterServicosISSMunicipio" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
