<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterExcecaoICMSCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterExcecaoICMSCliente" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterExcecaoICMSCliente" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
