<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	title="manterDiasVencimentoDivisao"
	type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab
			title="listagem"
			id="pesq"
			src="/vendas/manterPrazoVencimentoDivisao"
			cmd="list"
			onShow="onShowListTab();"/>

		<adsm:tab
			title="detalhamento"
			id="cad"
			src="/vendas/manterPrazoVencimentoDivisao"
			cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
