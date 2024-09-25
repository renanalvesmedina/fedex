<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterModulos" type="main">
	<adsm:i18nLabels>
	</adsm:i18nLabels>
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/seguranca/manterModulo" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterModulo" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
