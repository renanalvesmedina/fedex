<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterParametrosSubstituicaoTributaria" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterParametrosSubstituicaoTributaria" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterParametrosSubstituicaoTributaria" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
