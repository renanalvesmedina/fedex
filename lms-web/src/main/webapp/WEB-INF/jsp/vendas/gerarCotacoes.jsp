<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarCotacoes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab
			title="listagem"
			id="pesq"
			src="/vendas/gerarCotacoes"
			cmd="list"/>

		<adsm:tab
			title="dadosGerais"
			id="cad"
			src="/vendas/gerarCotacoes"
			cmd="dados"
			boxWidth="110"/>

		<adsm:tab
			title="parametros"
			autoLoad="false"
			disabled="true"
			masterTabId="cad"
			id="param"
			src="/vendas/gerarCotacoes"
			cmd="param"/>

		<adsm:tab
			title="taxas"
			autoLoad="false"
			disabled="true"
			id="taxas"
			masterTabId="param"
			src="/vendas/gerarCotacoes"
			cmd="taxas"/>

		<adsm:tab
			title="generalidades"
			autoLoad="false"
			disabled="true"
			masterTabId="param"
			id="gen"
			src="/vendas/gerarCotacoes"
			cmd="gen"/>

		<adsm:tab
			title="servicosAdicionaisTitulo"
			autoLoad="false"
			masterTabId="cad"
			disabled="true"
			id="servAd"
			src="/vendas/gerarCotacoes"
			cmd="servAd"
			boxWidth="120"/>

	</adsm:tabGroup>
</adsm:window>