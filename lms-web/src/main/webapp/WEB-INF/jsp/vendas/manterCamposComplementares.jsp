<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCamposComplementares" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterCamposComplementares" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterCamposComplementares" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
