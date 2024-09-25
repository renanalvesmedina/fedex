<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAba" type="main">
	<adsm:i18nLabels>
	</adsm:i18nLabels>
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/seguranca/manterAba" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterAba" cmd="cad"   />
	</adsm:tabGroup>
</adsm:window>
