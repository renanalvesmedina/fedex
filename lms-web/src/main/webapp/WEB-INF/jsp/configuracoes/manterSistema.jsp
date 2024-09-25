<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSistema" type="main">
	<adsm:i18nLabels>
		<adsm:include key="LMS-27053" />
		<adsm:include key="pessoa" />
	</adsm:i18nLabels>
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/seguranca/manterSistema" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterSistema" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
